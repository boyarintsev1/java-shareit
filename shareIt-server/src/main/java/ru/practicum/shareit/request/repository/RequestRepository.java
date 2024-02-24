package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.entity.Request;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query(value = "SELECT * FROM public.REQUESTS r WHERE r.requestor_id = ?1 ORDER BY r.created DESC",
            nativeQuery = true)
    List<Request> findAllRequestsByRequestor_id(Integer userId);

    @Query(value = "SELECT * FROM public.REQUESTS r WHERE r.requestor_id NOT IN (?1) ORDER BY r.created DESC",
            nativeQuery = true)
    Page<Request> findAllRequestsPageable(Integer requestorId, Pageable page);

}