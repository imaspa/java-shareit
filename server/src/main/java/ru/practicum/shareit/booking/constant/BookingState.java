package ru.practicum.shareit.booking.constant;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public enum BookingState {
    ALL("все") {
        @Override
        public Predicate toPredicate(Root<Booking> root, CriteriaBuilder builder, LocalDateTime now) {
            return null;
        }
    },
    CURRENT("текущее") {
        @Override
        public Predicate toPredicate(Root<Booking> root, CriteriaBuilder builder, LocalDateTime now) {
            return builder.and(
                    builder.lessThanOrEqualTo(root.get("start"), now),
                    builder.greaterThanOrEqualTo(root.get("end"), now)
            );
        }
    },
    PAST("завершенные") {
        @Override
        public Predicate toPredicate(Root<Booking> root, CriteriaBuilder builder, LocalDateTime now) {
            return builder.lessThan(root.get("end"), now);
        }
    },
    FUTURE("будущие") {
        @Override
        public Predicate toPredicate(Root<Booking> root, CriteriaBuilder builder, LocalDateTime now) {
            return builder.greaterThan(root.get("start"), now);
        }
    },
    WAITING("ожидающие подтверждения") {
        @Override
        public Predicate toPredicate(Root<Booking> root, CriteriaBuilder builder, LocalDateTime now) {
            return builder.equal(root.get("status"), BookingStatus.WAITING);
        }
    },
    REJECTED("отклоненные") {
        @Override
        public Predicate toPredicate(Root<Booking> root, CriteriaBuilder builder, LocalDateTime now) {
            return builder.equal(root.get("status"), BookingStatus.REJECTED);
        }
    };

    private final String name;

    public abstract Predicate toPredicate(Root<Booking> root, CriteriaBuilder builder, LocalDateTime now);
}

