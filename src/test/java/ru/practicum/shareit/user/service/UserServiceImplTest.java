package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;
    @InjectMocks
    UserServiceImpl userService;

    @Test
    void findAllUsers_whenUsersAre2_thenReturnListOfUsersSize2() {
        User user1 = new User(1, "Billy", "email55@yandex.com");
        User user2 = new User(2, "Felix", "felix@yandex.com");
        List<User> expectedList = List.of(user1, user2);
        Mockito.when(userRepository.findAll()).thenReturn(expectedList);

        List<User> actualList = userService.findAllUsers();

        assertEquals(2, actualList.size(), "Не равны");
        assertIterableEquals(expectedList, actualList, "Не равны");
    }

    @Test
    void findUserById_whenUserIdIsValid_thenReturnUser() {
        User expectedUser = new User(1, "Billy", "email55@yandex.com");
        Mockito.when(userRepository.findById(expectedUser.getId())).thenReturn(Optional.of(expectedUser));

        User actualUser = userService.findUserById(expectedUser.getId());

        assertEquals(expectedUser, actualUser, "Не равны");
    }

    @Test
    void createUser_whenUserIsValid_thenReturnUser() {
        User expectedUser = new User(1, "Billy", "email55@yandex.com");
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(expectedUser);

        User actualUser = userService.createUser(expectedUser);

        assertEquals(expectedUser, actualUser, "Не равны");
        verify(userRepository).save(expectedUser);
    }


    @Test
    void updateUser_whenUserToUpdateIsValid_thenReturnUser() {
        User oldUser = new User(1, "Billy", "email55@yandex.com");
        User newUser = new User(1, "Felix", "felix@yandex.com");
        Mockito.when(userRepository.findById(oldUser.getId())).thenReturn(Optional.of(oldUser));

        User actualUser = userService.updateUser(newUser, oldUser.getId());
        verify(userRepository).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();

        assertEquals(1, savedUser.getId());
        assertEquals("Felix", savedUser.getName());
        assertEquals("felix@yandex.com", savedUser.getEmail());
    }

    @Test
    void deleteUser_whenItemIdIsValid() {
        Integer userId = 1;
        userRepository.deleteById(userId);

        verify(userRepository).deleteById(userId);
    }


    @Test
    void validateUserPatchRequest_whenItemNameIsNotCorrect_thenExceptionThrown() {
        User user = new User(1, "Billy", "yandex.com");

        assertThrows(ValidationException.class, () -> userService.validateUserPatchRequest(user), "Не равны");
    }
}
