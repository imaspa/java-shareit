package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwner_Id(Long ownerId);

    @Query("select i from Item i where (lower(i.name) like lower(concat('%', :search, '%')) " +
            "or lower(i.description) like lower(concat('%', :search, '%')))     " +
            "and i.available = true")
    List<Item> findAvailable(@Param("search") String text);

}