package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.booking.dto.BookingDtoForOwner;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.entity.Comment;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.booking.enums.BookingStatus.APPROVED;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    @Mock
    private UserService userService;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;

    @Captor
    private ArgumentCaptor<Item> itemArgumentCaptor;

    @InjectMocks
    ItemServiceImpl itemService;

    @Test
    void findAllItems_whenUserIsCorrect_thenReturnListOfItemsSize2() {
        User user = new User(1, "Billy", "email55@yandex.com");
        Item item1 = new Item(1L, "Дрель", "Простая дрель", true, user, null);
        Item item2 = new Item(2L, "Отвертка", "Незаменимая вещь", true, user, null);
        List<Item> expectedList = List.of(item1, item2);
        Mockito.when(itemRepository.findByOwnerIdOrderByIdAsc(anyInt(), Mockito.any()))
                .thenReturn(new PageImpl<>(expectedList));

        List<Item> actualList = itemService.findAllItems(user.getId(), 1, 10).getContent();

        assertEquals(2, actualList.size(), "Не равны");
        assertIterableEquals(expectedList, actualList, "Не равны");
    }

    @Test
    void findAllItemsByOwnerAndRequest_whenRequestNotNullAndOwnerIsCorrect_theReturnListOfItemsSize2() {
        User user = new User(1, "Billy", "email55@yandex.com");
        Item item1 = new Item(1L, "Дрель", "Простая дрель", true, user, null);
        Item item2 = new Item(2L, "Отвертка", "Незаменимая вещь", true, user, null);
        List<Item> expectedList = List.of(item1, item2);
        Mockito.when(itemRepository.findByRequestNotNullOrderByIdAsc((anyInt()))).thenReturn(expectedList);

        List<Item> actualList = itemService.findAllItemsByOwnerAndRequest(user.getId());

        assertEquals(2, actualList.size(), "Не равны");
        assertIterableEquals(expectedList, actualList, "Не равны");
    }

    @Test
    void findItemById_whenItemIdIsValid_thenReturnItem() {
        User user = new User(1, "Billy", "email55@yandex.com");
        Item expectedItem = new Item(1L, "Дрель", "Простая дрель", true, user, null);
        Mockito.when(itemRepository.findById(expectedItem.getId())).thenReturn(Optional.of(expectedItem));

        Item actualItem = itemService.findItemById(expectedItem.getId());

        assertEquals(expectedItem, actualItem, "Не равны");
    }


    @Test
    void findItem_whenSearchTextIsValid_thenReturnListOfItemsSize2() {
        User user = new User(1, "Billy", "email55@yandex.com");
        Item item1 = new Item(1L, "Дрель", "Простая дрель", true, user, null);
        Item item2 = new Item(2L, "Дрель", "Электрическая дрель", true, user, null);
        List<Item> expectedList = List.of(item1, item2);
        Mockito.when(itemRepository.findInNameAndDescription(anyString(), Mockito.any()))
                .thenReturn(new PageImpl<>(expectedList));

        List<Item> actualList = itemService.findItem("ДрЕль", 1, 10).getContent();

        assertEquals(2, actualList.size(), "Не равны");
        assertIterableEquals(expectedList, actualList, "Не равны");
    }


    @Test
    void createItem_whenItemAndUserAreValid_thenReturnItem() {
        User user = new User(1, "Billy", "email55@yandex.com");
        Item expectedItem = new Item(1L, "Дрель", "Простая дрель", true, user, null);
        Mockito.when(itemRepository.save(expectedItem)).thenReturn(expectedItem);
        Mockito.when(userService.findUserById(user.getId())).thenReturn(user);

        Item actualItem = itemService.createItem(user.getId(), expectedItem);

        assertEquals(expectedItem, actualItem, "Не равны");
        verify(itemRepository).save(expectedItem);
    }

    @Test
    void updateItem_whenItemToUpdateIsValid_thenReturnItem() {
        User user = new User(1, "Billy", "email55@yandex.com");
        Item oldItem = new Item(1L, "Дрель", "Простая дрель", true, user, null);
        Item newItem = new Item(1L, "Дрель", "Электрическая дрель", false, user, null);
        Mockito.when(itemRepository.findById(oldItem.getId())).thenReturn(Optional.of(oldItem));

        Item actualItem = itemService.updateItem(user.getId(), oldItem.getId(), newItem);
        verify(itemRepository).save(itemArgumentCaptor.capture());
        Item savedItem = itemArgumentCaptor.getValue();

        assertEquals("Дрель", savedItem.getName());
        assertEquals("Электрическая дрель", savedItem.getDescription());
        assertEquals(false, savedItem.getAvailable());
    }

    @Test
    void deleteItem_whenItemIdIsValid() {
        Long itemId = 1L;
        itemRepository.deleteById(itemId);

        verify(itemRepository).deleteById(itemId);
    }

    @Test
    void validateItemPatchRequest_whenItemNameIsNotCorrect_thenExceptionThrown() {
        User user = new User(1, "Billy", "email55@yandex.com");
        Item item = new Item(1L, " ", "Простая дрель", true, user, null);
        assertThrows(ValidationException.class, () -> itemService.validateItemPatchRequest(item), "Не равны");
    }

    @Test
    void findLastBookingsOfItem_whenBookingsAreValid_thenReturnLastBooking() {
        User owner = new User(1, "Billy", "email55@yandex.com");
        User booker = new User(2, "Felix", "felix@yandex.com");
        Item item = new Item(1L, "Дрель", "Простая дрель", true, owner, null);
        Booking booking1 = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().minusMinutes(20))
                .end(LocalDateTime.now().minusMinutes(10))
                .item(item)
                .booker(booker)
                .status(APPROVED)
                .build();
        Booking booking2 = Booking.builder()
                .id(2L)
                .start(LocalDateTime.now().minusMinutes(60))
                .end(LocalDateTime.now().minusMinutes(40))
                .item(item)
                .booker(booker)
                .status(APPROVED)
                .build();
        List<Booking> bookingsOfItem = Arrays.asList(booking1, booking2);
        BookingDtoForOwner expectedBookingDtoForOwner = new BookingDtoForOwner(
                booking1.getId(), booking1.getStart(), booking1.getEnd(), booking1.getBooker().getId());
        when(bookingRepository.findAllBookingsByItem_Id(anyLong())).thenReturn(bookingsOfItem);

        BookingDtoForOwner lastBooking = itemService.findLastBookingsOfItem(item.getId());

        assertEquals(expectedBookingDtoForOwner.getId(), lastBooking.getId(), "Не равны");
        assertEquals(expectedBookingDtoForOwner.getStart(), lastBooking.getStart(), "Не равны");
        assertEquals(expectedBookingDtoForOwner.getEnd(), lastBooking.getEnd(), "Не равны");
        assertEquals(expectedBookingDtoForOwner.getBookerId(), lastBooking.getBookerId(), "Не равны");
    }

    @Test
    void findNextBookingsOfItem_whenBookingsAreValid_thenReturnNextBooking() {
        User owner = new User(1, "Billy", "email55@yandex.com");
        User booker = new User(2, "Felix", "felix@yandex.com");
        Item item = new Item(1L, "Дрель", "Простая дрель", true, owner, null);
        Booking booking1 = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().plusMinutes(20))
                .end(LocalDateTime.now().plusMinutes(40))
                .item(item)
                .booker(booker)
                .status(APPROVED)
                .build();
        Booking booking2 = Booking.builder()
                .id(2L)
                .start(LocalDateTime.now().plusMinutes(60))
                .end(LocalDateTime.now().plusMinutes(120))
                .item(item)
                .booker(booker)
                .status(APPROVED)
                .build();
        List<Booking> bookingsOfItem = Arrays.asList(booking1, booking2);
        BookingDtoForOwner expectedBookingDtoForOwner = new BookingDtoForOwner(
                booking1.getId(), booking1.getStart(), booking1.getEnd(), booking1.getBooker().getId());
        when(bookingRepository.findAllBookingsByItem_Id(anyLong())).thenReturn(bookingsOfItem);

        BookingDtoForOwner nextBooking = itemService.findNextBookingsOfItem(item.getId());

        assertEquals(expectedBookingDtoForOwner.getId(), nextBooking.getId(), "Не равны");
        assertEquals(expectedBookingDtoForOwner.getStart(), nextBooking.getStart(), "Не равны");
        assertEquals(expectedBookingDtoForOwner.getEnd(), nextBooking.getEnd(), "Не равны");
        assertEquals(expectedBookingDtoForOwner.getBookerId(), nextBooking.getBookerId(), "Не равны");
    }

    @Test
    void createComment_whenItemIsValid_thenReturnComment() {
        User owner = new User(1, "Billy", "email55@yandex.com");
        User author = new User(2, "Felix", "felix@yandex.com");
        Item item = new Item(1L, "Дрель", "Простая дрель", true, owner, null);
        Comment comment = new Comment(1L, "Add comment from user", item, author, LocalDateTime.now());
        Mockito.when(commentRepository.save(Mockito.any())).thenReturn(comment);

        Comment actualComment = itemService.createComment(comment);

        assertEquals(actualComment, comment, "Не равны");
        verify(commentRepository).save(comment);
    }

    @Test
    void findAllCommentsByItem_Id_whenItemIdIsValid_thenReturnListOfCommentsSize2() {
        User owner = new User(1, "Billy", "email55@yandex.com");
        User author = new User(2, "Felix", "felix@yandex.com");
        Item item = new Item(1L, "Дрель", "Простая дрель", true, owner, null);
        Comment comment1 = new Comment(
                1L, "Add comment1 from user", item, author, LocalDateTime.now().minusMinutes(10));
        Comment comment2 = new Comment(
                2L, "Add comment2 from user", item, author, LocalDateTime.now().minusMinutes(5));
        List<Comment> expectedList = List.of(comment1, comment2);
        Mockito.when(commentRepository.findAllByItem_id(anyLong())).thenReturn(expectedList);

        List<Comment> actualList = itemService.findAllCommentsByItem_Id(item.getId());

        assertEquals(2, actualList.size(), "Не равны");
        assertIterableEquals(expectedList, actualList, "Не равны");
    }

    @Test
    void checkUserBookedItemInPast_whenUserIdIsValid_thenReturnListOfBookingsSize1() {
        User owner = new User(1, "Billy", "email55@yandex.com");
        User booker = new User(2, "Felix", "felix@yandex.com");
        Item item = new Item(1L, "Дрель", "Простая дрель", true, owner, null);
        Booking booking1 = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().minusMinutes(20))
                .end(LocalDateTime.now().minusMinutes(10))
                .item(item)
                .booker(booker)
                .status(APPROVED)
                .build();
        List<Booking> expectedList = Collections.singletonList(booking1);

        when(bookingRepository.findByItem_idAndBooker_idAndEnd_dateIsBefore(anyLong(), anyInt(), any()))
                .thenReturn(expectedList);

        List<Booking> actualList = itemService.checkUserBookedItemInPast(item.getId(), booker.getId());

        assertEquals(1, actualList.size(), "Не равны");
        assertIterableEquals(expectedList, actualList, "Не равны");
    }

}