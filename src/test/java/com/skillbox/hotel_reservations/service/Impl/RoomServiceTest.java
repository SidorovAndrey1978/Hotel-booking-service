package com.skillbox.hotel_reservations.service.Impl;

import com.skillbox.hotel_reservations.enyity.Room;
import com.skillbox.hotel_reservations.exception.EntityNotFoundException;
import com.skillbox.hotel_reservations.model.filter.RoomFilter;
import com.skillbox.hotel_reservations.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.skillbox.hotel_reservations.utils.messageUtils.Message.NOT_FOUND_ROOM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomServiceImpl roomService;

    private Room testRoom;

    @BeforeEach
    public void setUp() {
        testRoom = Room.builder()
                .id(1L)
                .title("Test Room")
                .description("Comfortable room")
                .numberRoom(101)
                .pricePerNight(BigDecimal.valueOf(100))
                .capacity(2)
                .build();
    }

    @Test
    public void shouldSuccessfullyCreateRoom() {
        // Arrange
        when(roomRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        // Act
        Room savedRoom = roomService.create(testRoom);

        // Assert
        verify(roomRepository, times(1)).save(any());
        assertThat(savedRoom.getTitle()).isEqualTo("Test Room");
    }

    @Test
    public void shouldFindRoomById() {
        // Arrange
        when(roomRepository.findById(1L)).thenReturn(Optional.of(testRoom));

        // Act
        Room foundRoom = roomService.getById(1L);

        // Assert
        assertThat(foundRoom.getTitle()).isEqualTo("Test Room");
    }

    @Test
    public void shouldFailWhenRoomNotFoundById() {
        // Arrange
        when(roomRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> roomService.getById(999L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(NOT_FOUND_ROOM, 999);
    }

    @Test
    public void shouldUpdateRoom() {
        // Arrange
        Room updatedRoom = Room.builder()
                .id(1L)
                .title("Updated Title")
                .build();

        when(roomRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        // Act
        Room result = roomService.update(updatedRoom);

        // Assert
        assertThat(result.getTitle()).isEqualTo("Updated Title");
    }

    @Test
    public void shouldDeleteRoom() {
        // Arrange
        when(roomRepository.findById(testRoom.getId())).thenReturn(Optional.of(testRoom));
        doNothing().when(roomRepository).delete((Room) any());

        // Act
        roomService.deleteById(1L);

        // Assert
        verify(roomRepository, times(1)).delete((Room) any());
    }

    @Test
    public void shouldFindAllRooms() {
        // Arrange
        when(roomRepository.findAll()).thenReturn(Collections.singletonList(testRoom));

        // Act
        List<Room> allRooms = roomService.findAll();

        // Assert
        assertThat(allRooms.size()).isEqualTo(1);
        assertThat(allRooms.getFirst().getTitle()).isEqualTo("Test Room");
    }

    @Test
    public void shouldGetFilteredRooms() {
        // Arrange
        RoomFilter filter = new RoomFilter();
        filter.setTitle("Test");

        when(roomRepository.findAll((Specification<Room>) any(), (Pageable) isNull()))
                .thenReturn(new PageImpl<>(Collections.singletonList(testRoom)));

        // Act
        List<Room> filteredRooms = roomService.getFilteredRooms(filter, null).getContent();

        // Assert
        assertThat(filteredRooms.size()).isEqualTo(1);
        assertThat(filteredRooms.getFirst().getTitle()).isEqualTo("Test Room");
    }
}