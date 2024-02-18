package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.entity.Item;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select i from Item i " +
            "where ?1 is not null and i.available = true and " +
            "(lower (i.name) like concat('%', ?1, '%') or lower (i.description) like concat('%', ?1, '%'))")
    Page<Item> findInNameAndDescription(String text, Pageable page);

    Page<Item> findByOwnerIdOrderByIdAsc(Integer userId, Pageable page);

    @Query(value = "SELECT * FROM public.ITEMS WHERE request_id > 0",
            nativeQuery = true)
    List<Item> findByRequestNotNullOrderByIdAsc(Integer userId);

}