package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.entity.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select i from Item i " +
            "where ?1 is not null and i.available = true and " +
            "(lower (i.name) like concat('%', ?1, '%') or lower (i.description) like concat('%', ?1, '%'))")
    List<Item> findInNameAndDescription(String text);

    List<Item> findByOwnerIdOrderByIdAsc(Integer userId);

}