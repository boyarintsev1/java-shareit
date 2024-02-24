package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    private void addItems() {
        User user = new User(1, "Billy", "email55@yandex.com");
        userRepository.save(user);
        itemRepository.save(Item.builder()
                .name("Дрель")
                .description("Простая дрель")
                .owner(userRepository.findAll().get(0))
                .available(true)
                .request(null)
                .build());
        itemRepository.save(Item.builder()
                .name("Отвертка")
                .description("Незаменимая вещь")
                .owner(userRepository.findAll().get(0))
                .available(true)
                .request(null)
                .build());
    }

    @Test
    void findInNameAndDescription_whenTextIsCorrect_thenEquals() {
        Page<Item> newPage = itemRepository.findInNameAndDescription("дрель", PageRequest.of(0, 100));

        assertEquals(1, newPage.getContent().size());
        assertEquals("Дрель", newPage.getContent().get(0).getName());
    }

    @Test
    void findByOwnerIdOrderByIdAsc_whenIdIsCorrect_thenListSizeIsOne() {
        Page<Item> newPage = itemRepository.findByOwnerIdOrderByIdAsc(
                userRepository.findAll().get(0).getId(), PageRequest.of(0, 100));

        assertEquals("Дрель", newPage.getContent().get(0).getName());
    }

    @Test
    void findByRequestNotNullOrderByIdAsc_whenAllRequestsIdAreNull_thenListIsZero() {
        List<Item> expectedList = itemRepository.findByRequestNotNullOrderByIdAsc(1);
        assertEquals(0, expectedList.size());
    }

    @AfterEach
    private void deleteItems() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
    }
}


