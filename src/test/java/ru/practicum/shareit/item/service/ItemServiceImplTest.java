package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.booking.dto.BookingDtoForOwner;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.IncorrectIdException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.entity.Comment;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.entity.Request;
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

    User user = new User(1, "Billy", "email55@yandex.com");
    User booker = new User(2, "Felix", "felix@yandex.com");
    Item item1 = new Item(1L, "Дрель", "Простая дрель", true, user, null);
    Item item2 = new Item(2L, "Отвертка", "Незаменимая вещь", true, user, null);
    Booking booking1 = Booking.builder()
            .id(1L)
            .start(LocalDateTime.now().minusMinutes(20))
            .end(LocalDateTime.now().minusMinutes(10))
            .item(item1)
            .booker(booker)
            .status(APPROVED)
            .build();
    Booking booking2 = Booking.builder()
            .id(2L)
            .start(LocalDateTime.now().minusMinutes(60))
            .end(LocalDateTime.now().minusMinutes(40))
            .item(item1)
            .booker(booker)
            .status(APPROVED)
            .build();

    @Test
    void findAllItems_whenUserIsCorrect_thenReturnListOfItemsSize2() {
        List<Item> expectedList = List.of(item1, item2);
        Mockito.when(itemRepository.findByOwnerIdOrderByIdAsc(anyInt(), Mockito.any()))
                .thenReturn(new PageImpl<>(expectedList));

        List<Item> actualList = itemService.findAllItems(user.getId(), 1, 10).getContent();

        assertEquals(2, actualList.size(), "Не равны");
        assertIterableEquals(expectedList, actualList, "Не равны");
    }

    @Test
    void findAllItemsByOwnerAndRequest_whenRequestNotNullAndOwnerIsCorrect_theReturnListOfItemsSize2() {
        List<Item> expectedList = List.of(item1, item2);
        Mockito.when(itemRepository.findByRequestNotNullOrderByIdAsc((anyInt()))).thenReturn(expectedList);

        List<Item> actualList = itemService.findAllItemsByOwnerAndRequest(user.getId());

        assertEquals(2, actualList.size(), "Не равны");
        assertIterableEquals(expectedList, actualList, "Не равны");
    }

    @Test
    void findItemById_whenItemIdIsValid_thenReturnItem() {
        Item expectedItem = item1;
        Mockito.when(itemRepository.findById(expectedItem.getId())).thenReturn(Optional.of(expectedItem));

        Item actualItem = itemService.findItemById(expectedItem.getId());

        assertEquals(expectedItem, actualItem, "Не равны");
    }


    @Test
    void findItem_whenSearchTextIsValid_thenReturnListOfItemsSize2() {
        List<Item> expectedList = List.of(item1, item2);
        Mockito.when(itemRepository.findInNameAndDescription(anyString(), Mockito.any()))
                .thenReturn(new PageImpl<>(expectedList));

        List<Item> actualList = itemService.findItem("ДрЕль", 1, 10).getContent();

        assertEquals(2, actualList.size(), "Не равны");
        assertIterableEquals(expectedList, actualList, "Не равны");
    }

    @Test
    void createItem_whenItemAndUserAreValid_thenReturnItem() {
        Item expectedItem = item1;
        Mockito.when(itemRepository.save(expectedItem)).thenReturn(expectedItem);
        Mockito.when(userService.findUserById(user.getId())).thenReturn(user);

        Item actualItem = itemService.createItem(user.getId(), expectedItem);

        assertEquals(expectedItem, actualItem, "Не равны");
        verify(itemRepository).save(expectedItem);
    }

    @Test
    void updateItem_whenItemToUpdateIsValid_thenReturnItem() {
        Item oldItem = item1;
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
    void updateItem_whenUserIdNotOwner_thenReturnItem() {
        Item oldItem = new Item(1L, "Дрель", "Простая дрель", true, user, null);
        User wrongUser = new User();
        wrongUser.setId(50);
        Item newItem = new Item();
        newItem.setDescription("Электрическая дрель");
        newItem.setOwner(wrongUser);
        newItem.setAvailable(false);
        Mockito.when(itemRepository.findById(oldItem.getId())).thenReturn(Optional.of(oldItem));

        assertThrows(IncorrectIdException.class,
                () -> itemService.updateItem(wrongUser.getId(), oldItem.getId(), newItem),
                "Не равны");

        verify(itemRepository, never()).save(oldItem);
    }

    @Test
    void updateItem_whenItemToUpdateHasName_thenReturnItem() {
        Item oldItem = new Item(1L, "Дрель", "Простая дрель", true, user, null);
        Item newItem = new Item();
        newItem.setName("Дрелище");
        Mockito.when(itemRepository.findById(oldItem.getId())).thenReturn(Optional.of(oldItem));

        Item actualItem = itemService.updateItem(user.getId(), oldItem.getId(), newItem);

        verify(itemRepository).save(itemArgumentCaptor.capture());
        Item savedItem = itemArgumentCaptor.getValue();

        assertEquals("Дрелище", savedItem.getName());
    }

    @Test
    void updateItem_whenItemToUpdateHasDescription_thenReturnItem() {
        Item oldItem = new Item(1L, "Дрель", "Простая дрель", true, user, null);
        User wrongUser = new User();
        wrongUser.setId(50);
        Item newItem = new Item();
        newItem.setDescription("Электрическая дрель");
        newItem.setOwner(wrongUser);
        newItem.setAvailable(false);
        Mockito.when(itemRepository.findById(oldItem.getId())).thenReturn(Optional.of(oldItem));

        Item actualItem = itemService.updateItem(user.getId(), oldItem.getId(), newItem);
        verify(itemRepository).save(itemArgumentCaptor.capture());
        Item savedItem = itemArgumentCaptor.getValue();

        assertEquals("Электрическая дрель", savedItem.getDescription());
    }

    @Test
    void updateItem_whenItemToUpdateHasAvailable_thenReturnItem() {
        Item oldItem = new Item(1L, "Дрель", "Простая дрель", true, user, null);
        Item newItem = new Item();
        newItem.setAvailable(false);
        Mockito.when(itemRepository.findById(oldItem.getId())).thenReturn(Optional.of(oldItem));

        Item actualItem = itemService.updateItem(user.getId(), oldItem.getId(), newItem);

        verify(itemRepository).save(itemArgumentCaptor.capture());
        Item savedItem = itemArgumentCaptor.getValue();

        assertEquals(false, savedItem.getAvailable());
    }

    @Test
    void updateItem_whenItemToUpdateHasOwner_thenReturnItem() {
        Item oldItem = new Item(1L, "Дрель", "Простая дрель", true, user, null);
        User wrongUser = new User();
        wrongUser.setId(50);
        Item newItem = new Item();
        newItem.setOwner(wrongUser);
        Mockito.when(itemRepository.findById(oldItem.getId())).thenReturn(Optional.of(oldItem));

        Item actualItem = itemService.updateItem(user.getId(), oldItem.getId(), newItem);
        verify(itemRepository).save(itemArgumentCaptor.capture());
        Item savedItem = itemArgumentCaptor.getValue();

        assertEquals(50, savedItem.getOwner().getId());
    }

    @Test
    void updateItem_whenItemToUpdateHasRequest_thenReturnItem() {
        Item oldItem = new Item(1L, "Дрель", "Простая дрель", true, user, null);
        Request newRequest = new Request();
        newRequest.setId(51L);
        Item newItem = new Item();
        newItem.setRequest(newRequest);
        Mockito.when(itemRepository.findById(oldItem.getId())).thenReturn(Optional.of(oldItem));

        Item actualItem = itemService.updateItem(user.getId(), oldItem.getId(), newItem);
        verify(itemRepository).save(itemArgumentCaptor.capture());
        Item savedItem = itemArgumentCaptor.getValue();

        assertEquals(51, savedItem.getRequest().getId());
    }

    @Test
    void deleteItem_whenItemIdIsValid() {
        Long itemId = 1L;
        itemRepository.deleteById(itemId);

        verify(itemRepository).deleteById(itemId);
    }

    @Test
    void validateItemPatchRequest_whenItemNameIsBlank_thenExceptionThrown() {
        Item item = new Item(1L, " ", "Электрическая дрель", true, user, null);
        assertThrows(ValidationException.class, () -> itemService.validateItemPatchRequest(item), "Не равны");
    }

    @Test
    void validateItemPatchRequest_whenItemNameIsMore30Symbols_thenExceptionThrown() {
        Item item = new Item(1L, "abcdefghijklmnopkqrsuvwxyzabcdefghijklmnopkqrsuvwxyz",
                "Простая дрель", true, user, null);
        assertThrows(ValidationException.class, () -> itemService.validateItemPatchRequest(item), "Не равны");
    }

    @Test
    void validateItemPatchRequest_whenItemDescriptionIsBlank_thenExceptionThrown() {
        Item item = new Item(1L, "Дрель", " ", true, user, null);
        assertThrows(ValidationException.class, () -> itemService.validateItemPatchRequest(item), "Не равны");
    }

    @Test
    void validateItemPatchRequest_whenItemDescriptionIsMore200Symbols_thenExceptionThrown() {
        Item item = new Item(1L, "Дрель", "uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu" +
                "uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu" +
                "uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu" +
                "uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu ", true, user, null);
        assertThrows(ValidationException.class, () -> itemService.validateItemPatchRequest(item), "Не равны");
    }

    @Test
    void findLastBookingsOfItem_whenBookingsAreValid_thenReturnLastBooking() {
        List<Booking> bookingsOfItem = Arrays.asList(booking1, booking2);
        BookingDtoForOwner expectedBookingDtoForOwner = new BookingDtoForOwner(
                booking1.getId(), booking1.getStart(), booking1.getEnd(), booking1.getBooker().getId());
        when(bookingRepository.findAllBookingsByItem_Id(anyLong())).thenReturn(bookingsOfItem);

        BookingDtoForOwner lastBooking = itemService.findLastBookingsOfItem(item1.getId());

        assertEquals(expectedBookingDtoForOwner.getId(), lastBooking.getId(), "Не равны");
        assertEquals(expectedBookingDtoForOwner.getStart(), lastBooking.getStart(), "Не равны");
        assertEquals(expectedBookingDtoForOwner.getEnd(), lastBooking.getEnd(), "Не равны");
        assertEquals(expectedBookingDtoForOwner.getBookerId(), lastBooking.getBookerId(), "Не равны");
    }

    @Test
    void findLastBookingsOfItem_whenStartIdBeforeNowAndEndIsAfterNow_thenReturnLastBooking() {
        booking1.setStart(LocalDateTime.now().minusMinutes(10));
        booking1.setEnd(LocalDateTime.now().plusMinutes(10));
        List<Booking> bookingsOfItem = Arrays.asList(booking1, booking2);
        BookingDtoForOwner expectedBookingDtoForOwner = new BookingDtoForOwner(
                booking2.getId(), booking2.getStart(), booking2.getEnd(), booking2.getBooker().getId());
        when(bookingRepository.findAllBookingsByItem_Id(anyLong())).thenReturn(bookingsOfItem);

        BookingDtoForOwner lastBooking = itemService.findLastBookingsOfItem(item1.getId());

        assertEquals(expectedBookingDtoForOwner.getId(), lastBooking.getId(), "Не равны");
        assertEquals(expectedBookingDtoForOwner.getStart(), lastBooking.getStart(), "Не равны");
        assertEquals(expectedBookingDtoForOwner.getEnd(), lastBooking.getEnd(), "Не равны");
        assertEquals(expectedBookingDtoForOwner.getBookerId(), lastBooking.getBookerId(), "Не равны");
    }

    @Test
    void findLastBookingsOfItem_whenLastBookingIsNull_thenReturnNull() {
        booking1.setStart(LocalDateTime.now().plusMinutes(10));
        booking1.setEnd(LocalDateTime.now().plusMinutes(20));
        booking2.setStart(LocalDateTime.now().plusMinutes(30));
        booking2.setEnd(LocalDateTime.now().plusMinutes(40));
        List<Booking> bookingsOfItem = Arrays.asList(booking1, booking2);
        when(bookingRepository.findAllBookingsByItem_Id(anyLong())).thenReturn(bookingsOfItem);

        BookingDtoForOwner lastBooking = itemService.findLastBookingsOfItem(item1.getId());

        assertNull(lastBooking, "Не равны");

    }

    @Test
    void findNextBookingsOfItem_whenBookingsAreValid_thenReturnNextBooking() {
        Item item = new Item(1L, "Дрель", "Простая дрель", true, user, null);
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
        User author = new User(2, "Felix", "felix@yandex.com");
        Comment comment = new Comment(1L, "Add comment from user", item1, author, LocalDateTime.now());
        Mockito.when(commentRepository.save(Mockito.any())).thenReturn(comment);

        Comment actualComment = itemService.createComment(comment);

        assertEquals(actualComment, comment, "Не равны");
        verify(commentRepository).save(comment);
    }

    @Test
    void findAllCommentsByItem_Id_whenItemIdIsValid_thenReturnListOfCommentsSize2() {
        User author = new User(2, "Felix", "felix@yandex.com");
        Comment comment1 = new Comment(
                1L, "Add comment1 from user", item1, author, LocalDateTime.now().minusMinutes(10));
        Comment comment2 = new Comment(
                2L, "Add comment2 from user", item1, author, LocalDateTime.now().minusMinutes(5));
        List<Comment> expectedList = List.of(comment1, comment2);
        Mockito.when(commentRepository.findAllByItem_id(anyLong())).thenReturn(expectedList);

        List<Comment> actualList = itemService.findAllCommentsByItem_Id(item1.getId());

        assertEquals(2, actualList.size(), "Не равны");
        assertIterableEquals(expectedList, actualList, "Не равны");
    }

    @Test
    void checkUserBookedItemInPast_whenUserIdIsValid_thenReturnListOfBookingsSize1() {
        Booking booking1 = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().minusMinutes(20))
                .end(LocalDateTime.now().minusMinutes(10))
                .item(item1)
                .booker(booker)
                .status(APPROVED)
                .build();
        List<Booking> expectedList = Collections.singletonList(booking1);

        when(bookingRepository.findByItem_idAndBooker_idAndEnd_dateIsBefore(anyLong(), anyInt(), any()))
                .thenReturn(expectedList);

        List<Booking> actualList = itemService.checkUserBookedItemInPast(item1.getId(), booker.getId());

        assertEquals(1, actualList.size(), "Не равны");
        assertIterableEquals(expectedList, actualList, "Не равны");
    }
}