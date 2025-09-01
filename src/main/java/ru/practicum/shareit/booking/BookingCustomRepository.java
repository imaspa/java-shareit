package ru.practicum.shareit.booking;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.constant.BookingState;
import ru.practicum.shareit.booking.constant.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookingCustomRepository {

    public static Specification<Booking> prepareSpecification(BookingCustomRepositoryFilter filter) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.getIsOwner()) {
                predicates.add(builder.equal(root.get("item").get("owner").get("id"), filter.getUserId())
                );
            } else {
                predicates.add(builder.equal(root.get("booker").get("id"), filter.getUserId())
                );
            }

            LocalDateTime now = LocalDateTime.now();
            BookingState state = filter.getState();

            Predicate statePredicate = switch (state) {
                case ALL -> null;
                case CURRENT -> builder.and(
                        builder.lessThanOrEqualTo(root.get("start"), now),
                        builder.greaterThanOrEqualTo(root.get("end"), now)
                );
                case PAST -> builder.lessThan(root.get("end"), now);
                case FUTURE -> builder.greaterThan(root.get("start"), now);
                case WAITING -> builder.equal(root.get("status"), BookingStatus.WAITING);
                case REJECTED -> builder.equal(root.get("status"), BookingStatus.REJECTED);
            };

            if (statePredicate != null) {
                predicates.add(statePredicate);
            }

            if (predicates.isEmpty()) {
                return builder.conjunction();
            }

            return builder.and(predicates.toArray(Predicate[]::new));
        };
    }
}