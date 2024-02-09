package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.request.entity.Request;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class RequestRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RequestRepository requestRepository;

    @BeforeEach
    public void addItems() {
        User requestor1 = new User(1, "Billy", "email55@yandex.com");
        User requestor2 = new User(2, "Felix", "felix@yandex.com");
        userRepository.save(requestor1);
        userRepository.save(requestor2);
        requestRepository.save(Request.builder()
                .description("Хотел бы воспользоваться щёткой для обуви")
                .requestor(userRepository.findAll().get(0))
                .created(LocalDateTime.now().minusHours(10))
                .build());
        requestRepository.save(Request.builder()
                .description("Нужна модель Солнечной системы")
                .requestor(userRepository.findAll().get(1))
                .created(LocalDateTime.now().minusHours(16))
                .build());
    }

    @Test
    void findAllRequestsByRequestor_id_whenIdIsCorrect_thenTestOK() {
        List<Request> expectedList = requestRepository.findAllRequestsByRequestor_id(
                userRepository.findAll().get(0).getId());

        assertEquals(1, expectedList.size());
        assertEquals("Хотел бы воспользоваться щёткой для обуви", expectedList.get(0).getDescription());
    }

    @Test
    void findAllRequestsPageable_whenIdIsCorrect_thenTestOK() {
        Page<Request> newPage = requestRepository.findAllRequestsPageable(userRepository.findAll().get(0).getId(),
                PageRequest.of(0, 100));

        assertEquals(1, newPage.getContent().size());
        assertEquals("Нужна модель Солнечной системы", newPage.getContent().get(0).getDescription());
    }
}