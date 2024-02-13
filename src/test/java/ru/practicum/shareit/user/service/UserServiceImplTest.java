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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;
    @InjectMocks
    UserServiceImpl userService;

    User user1 = new User(1, "Billy", "email55@yandex.com");
    User user2 = new User(2, "Felix", "felix@yandex.com");

    @Test
    void findAllUsers_whenUsersAre2_thenReturnListOfUsersSize2() {
        List<User> expectedList = List.of(user1, user2);
        Mockito.when(userRepository.findAll()).thenReturn(expectedList);

        List<User> actualList = userService.findAllUsers();

        assertEquals(2, actualList.size(), "Не равны");
        assertIterableEquals(expectedList, actualList, "Не равны");
    }

    @Test
    void findUserById_whenUserIdIsValid_thenReturnUser() {
        User expectedUser = user1;
        Mockito.when(userRepository.findById(expectedUser.getId())).thenReturn(Optional.of(expectedUser));

        User actualUser = userService.findUserById(expectedUser.getId());

        assertEquals(expectedUser, actualUser, "Не равны");
    }

    @Test
    void createUser_whenUserIsValid_thenReturnUser() {
        User expectedUser = user1;
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(expectedUser);

        User actualUser = userService.createUser(expectedUser);

        assertEquals(expectedUser, actualUser, "Не равны");
        verify(userRepository).save(expectedUser);
    }


    @Test
    void updateUser_whenUserToUpdateIsValid_thenReturnUser() {
        User oldUser = user1;
        User newUser = user2;
        Mockito.when(userRepository.findById(oldUser.getId())).thenReturn(Optional.of(oldUser));

        User actualUser = userService.updateUser(newUser, oldUser.getId());
        verify(userRepository).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();

        assertEquals(1, savedUser.getId());
        assertEquals("Felix", savedUser.getName());
        assertEquals("felix@yandex.com", savedUser.getEmail());
    }

    @Test
    void updateUser_whenUserToUpdateHasName_thenReturnUser() {
        User oldUser = user1;
        User newUser = new User();
        newUser.setName("Felix");
        Mockito.when(userRepository.findById(oldUser.getId())).thenReturn(Optional.of(oldUser));

        User actualUser = userService.updateUser(newUser, oldUser.getId());
        verify(userRepository).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();

        assertEquals(1, savedUser.getId());
        assertEquals("Felix", savedUser.getName());
    }

    @Test
    void updateUser_whenNewNameIsName_thenReturnUser() {
        User oldUser = user1;
        User newUser = new User();
        newUser.setName(" ");
        Mockito.when(userRepository.findById(oldUser.getId())).thenReturn(Optional.of(oldUser));

        User actualUser = userService.updateUser(newUser, oldUser.getId());
        verify(userRepository).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();

        assertEquals(1, savedUser.getId());
        assertEquals("Billy", savedUser.getName());
    }

    @Test
    void updateUser_whenNewEmailIsNotNull_thenReturnUser() {
        User oldUser = user1;
        User newUser = new User();
        newUser.setName("Felix");
        newUser.setEmail("felix@yandex.com");
        Mockito.when(userRepository.findById(oldUser.getId())).thenReturn(Optional.of(oldUser));
        Mockito.when(userRepository.findByEmailContainingIgnoreCase(anyString())).thenReturn(List.of());

        User actualUser = userService.updateUser(newUser, oldUser.getId());
        verify(userRepository).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();

        assertEquals(1, savedUser.getId());
        assertEquals("Felix", savedUser.getName());
        assertEquals("felix@yandex.com", savedUser.getEmail());
    }

    @Test
    void updateUser_whenNewEmailEqualsOldEmail_thenReturnUser() {
        User oldUser = user1;
        User newUser = new User();
        newUser.setName("Felix");
        newUser.setEmail("email55@yandex.com");
        Mockito.when(userRepository.findById(oldUser.getId())).thenReturn(Optional.of(oldUser));
        Mockito.when(userRepository.findByEmailContainingIgnoreCase(anyString())).thenReturn(List.of());

        User actualUser = userService.updateUser(newUser, oldUser.getId());
        verify(userRepository).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();

        assertEquals(1, savedUser.getId());
        assertEquals("Felix", savedUser.getName());
        assertEquals("email55@yandex.com", savedUser.getEmail());
    }

    @Test
    void updateUser_whenNewEmailIsNotNullAndNotFound_thenReturnUser() {
        User oldUser = user1;
        User newUser = new User();
        newUser.setName("Felix");
        newUser.setEmail("achtung@yandex.com");
        Mockito.when(userRepository.findById(oldUser.getId())).thenReturn(Optional.of(oldUser));
        Mockito.when(userRepository.findByEmailContainingIgnoreCase(anyString())).thenReturn(List.of(oldUser));

        assertThrows(ValidationException.class,
                () -> userService.updateUser(newUser, oldUser.getId()),
                "Не равны");

        verify(userRepository, never()).save(oldUser);
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
