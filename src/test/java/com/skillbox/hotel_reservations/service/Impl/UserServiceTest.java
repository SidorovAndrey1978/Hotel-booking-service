package com.skillbox.hotel_reservations.service.Impl;

import com.skillbox.hotel_reservations.enyity.Role;
import com.skillbox.hotel_reservations.enyity.User;
import com.skillbox.hotel_reservations.exception.AlreadyExistsException;
import com.skillbox.hotel_reservations.exception.EntityNotFoundException;
import com.skillbox.hotel_reservations.kafka.events.UserRegisteredEvent;
import com.skillbox.hotel_reservations.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static com.skillbox.hotel_reservations.utils.messageUtils.Message.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ApplicationEventPublisher publisher;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("test_user")
                .password("password")
                .email("test@example.com")
                .roles(Set.of(Role.USER))
                .build();
    }

    @Test
    public void shouldSuccessfullyCreateUser() {
        // Arrange
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        doNothing().when(publisher).publishEvent(any(UserRegisteredEvent.class));
        when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");

        // Act
        User savedUser = userService.create(testUser);

        //Assert
        verify(userRepository, times(1)).save(any());
        assertThat(savedUser.getUsername()).isEqualTo("test_user");
        assertThat(savedUser.getPassword()).isEqualTo("encoded_password");
    }

    @Test
    public void shouldFailIfUsernameAlreadyExists() {

        // Arrange
        when(userRepository.existsByUsername("test_user")).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> userService.create(testUser))
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessageContaining(USERNAME_EXISTS);
    }

    @Test
    public void shouldFailIfEmailAlreadyExists() {

        // Arrange
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> userService.create(testUser))
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessageContaining(EMAIL_EXISTS);
    }

    @Test
    public void shouldFindUserByUsername() {
        // Arrange
        when(userRepository.findByUsername("test_user")).thenReturn(Optional.of(testUser));

        // Act
        User foundUser = userService.findByUsername("test_user");

        // Assert
        assertThat(foundUser.getUsername()).isEqualTo("test_user");
    }

    @Test
    public void shouldFailWhenUserNotFoundByUsername() {
        // Arrange
        when(userRepository.findByUsername("non_existent_user")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userService.findByUsername("non_existent_user"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(NOT_FOUND_USER_BY_USERNAME, "non_existent_user");
    }

    @Test
    public void shouldUpdateUser() {
        // Arrange
        User updatedUser = User.builder()
                .id(1L)
                .username("new_test_user")
                .email("new_test@example.com")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        // Act
        User result = userService.update(updatedUser);

        // Assert
        assertThat(result.getUsername()).isEqualTo("new_test_user");
        assertThat(result.getEmail()).isEqualTo("new_test@example.com");
    }

    @Test
    public void shouldDeleteUser() {
        // Arrange
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        doNothing().when(userRepository).delete(any());

        // Act
        userService.deleteById(testUser.getId());

        // Assert
        verify(userRepository, times(1)).delete(any());
    }
}