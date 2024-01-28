package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.entity.Booking;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query(value = "SELECT * FROM public.BOOKINGS b WHERE b.booker_id = ?1 ORDER BY b.start_date DESC",
            nativeQuery = true)
    List<Booking> findAllByBookerId(Integer userId);

    @Query(value = "SELECT * FROM public.BOOKINGS b WHERE b.booker_id = ?1 and b.status = ?2 " +
            "ORDER BY b.start_date DESC",
            nativeQuery = true)
    List<Booking> findAllByBooker_IdAndStatus(Integer userId, String status);

    @Query(value = "SELECT * FROM public.BOOKINGS b WHERE b.booker_id = ?1 and b.end_date <= ?2 " +
            "ORDER BY b.start_date DESC",
            nativeQuery = true)
    List<Booking> findPastBookingsForBooker(Integer userId, LocalDateTime now);

    @Query(value = "SELECT * FROM public.BOOKINGS b WHERE b.booker_id = ?1 and b.start_date >= ?2 " +
            "ORDER BY b.start_date DESC",
            nativeQuery = true)
    List<Booking> findFutureBookingsForBooker(Integer userId, LocalDateTime now);

    @Query(value = "SELECT * FROM public.BOOKINGS b WHERE b.booker_id = ?1 and b.start_date < ?2 and b.end_date > ?3 " +
            "ORDER BY b.start_date",
            nativeQuery = true)
    List<Booking> findCurrentBookingsForBooker(Integer userId, LocalDateTime now1, LocalDateTime now2);

    @Query(value = "SELECT b.*, i.owner_id FROM public.BOOKINGS b INNER JOIN public.ITEMS i ON i.id=b.item_id" +
            " WHERE i.owner_id = ?1 ORDER BY b.start_date DESC",
            nativeQuery = true)
    List<Booking> findAllBookingsForOwner(Integer ownerId);

    @Query(value = "SELECT b.*, i.owner_id FROM public.BOOKINGS b INNER JOIN public.ITEMS i ON i.id=b.item_id" +
            " WHERE i.owner_id = ?1 and b.start_date < ?2 and b.end_date > ?3 ORDER BY b.start_date",
            nativeQuery = true)
    List<Booking> findCurrentBookingsForOwner(Integer ownerId, LocalDateTime now1, LocalDateTime now2);

    @Query(value = "SELECT b.*, i.owner_id FROM public.BOOKINGS b INNER JOIN public.ITEMS i ON i.id=b.item_id" +
            " WHERE i.owner_id = ?1 and b.end_date <= ?2 ORDER BY b.start_date DESC",
            nativeQuery = true)
    List<Booking> findPastBookingsForOwner(Integer ownerId, LocalDateTime now);

    @Query(value = "SELECT b.*, i.owner_id FROM public.BOOKINGS b INNER JOIN public.ITEMS i ON i.id=b.item_id" +
            " WHERE i.owner_id = ?1 and b.start_date >= ?2 ORDER BY b.start_date DESC",
            nativeQuery = true)
    List<Booking> findFutureBookingsForOwner(Integer ownerId, LocalDateTime now);

    @Query(value = "SELECT b.*, i.owner_id FROM public.BOOKINGS b INNER JOIN public.ITEMS i ON i.id=b.item_id" +
            " WHERE i.owner_id = ?1 and b.status = ?2  ORDER BY b.start_date",
            nativeQuery = true)
    List<Booking> findStatusBookingsForOwner(Integer ownerId, String status);

    @Query(value = "SELECT * FROM public.BOOKINGS b WHERE b.item_id = ?1 and b.status like '%APPROVED%'",
            nativeQuery = true)
    List<Booking> findAllBookingsByItem_Id(Long itemId);

    @Query(value = "SELECT * FROM public.BOOKINGS b WHERE b.item_id = ?1 and b.booker_id = ?2 and b.end_date <= ?3 ",
            nativeQuery = true)
    List<Booking> findByItem_idAndBooker_idAndEnd_dateIsBefore(Long itemId, Integer userId, LocalDateTime now);
}