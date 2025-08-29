package com.skillbox.hotel_reservations.service.Impl;

import com.skillbox.hotel_reservations.enyity.User;
import com.skillbox.hotel_reservations.exception.AlreadyExistsException;
import com.skillbox.hotel_reservations.exception.EntityNotFoundException;
import com.skillbox.hotel_reservations.kafka.events.UserRegisteredEvent;
import com.skillbox.hotel_reservations.repository.UserRepository;
import com.skillbox.hotel_reservations.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.skillbox.hotel_reservations.utils.messageUtils.Message.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;
    
    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(String.format(
                        NOT_FOUND_USER_BY_USERNAME, username
                )));
    }

    @Override
    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(
                        NOT_FOUND_USER_BY_ID, id
                )));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User create(User user) {

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new AlreadyExistsException(USERNAME_EXISTS);
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new AlreadyExistsException(EMAIL_EXISTS);
        }

        User createUser = User.builder()
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();

        publishEvent(createUser);

        return userRepository.save(createUser);
    }

    @Override
    @Transactional
    public User update(User user) {
        User existingUser = getById(user.getId());
        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
            existingUser.setUsername(user.getUsername());
        }
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            existingUser.setEmail(user.getEmail());
        }

        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        userRepository.delete(getById(id));
    }
    
    private void publishEvent(User user) {
        applicationEventPublisher.publishEvent(
                new UserRegisteredEvent(user.getId()));
    }
}
