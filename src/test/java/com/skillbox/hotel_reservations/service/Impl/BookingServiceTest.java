package com.skillbox.hotel_reservations.service.Impl;

import com.skillbox.hotel_reservations.enyity.Booking;
import com.skillbox.hotel_reservations.enyity.Room;
import com.skillbox.hotel_reservations.enyity.User;
import com.skillbox.hotel_reservations.kafka.events.RoomBookedEvent;
import com.skillbox.hotel_reservations.repository.BookingRepository;
import com.skillbox.hotel_reservations.service.RoomService;
import com.skillbox.hotel_reservations.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserService userService;

    @Mock
    private RoomService roomService;

    @Mock
    private ApplicationEventPublisher publisher;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private Room testRoom;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testRoom = Room.builder()
                .id(1L)
                .title("Test Room")
                .build();

        testUser = User.builder()
                .id(1L)
                .username("test_user")
                .build();
    }

    @Test
    public void shouldMakeBooking() {
        // Arrange
        LocalDate checkIn = LocalDate.of(2025, 8, 1);
        LocalDate checkOut = LocalDate.of(2025, 8, 5);

        when(roomService.getById(1L)).thenReturn(testRoom);
        when(userService.getById(1L)).thenReturn(testUser);
        doNothing().when(publisher).publishEvent(any(RoomBookedEvent.class));
        when(bookingRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        // Act
        Booking booking = bookingService.makeBooking(1L, 1L, checkIn, checkOut);

        // Assert
        assertThat(booking.getRoom().getId()).isEqualTo(1L);
        assertThat(booking.getUser().getId()).isEqualTo(1L);
        assertThat(booking.getCheckInDate()).isEqualTo(checkIn);
        assertThat(booking.getCheckOutDate()).isEqualTo(checkOut);
    }

    @Test
    public void shouldFailWhenRoomIsUnavailable() {
        // Arrange
        LocalDate checkIn = LocalDate.of(2025, 8, 1);
        LocalDate checkOut = LocalDate.of(2025, 8, 5);

        when(roomService.getById(1L)).thenReturn(testRoom);
        when(userService.getById(1L)).thenReturn(testUser);
        when(bookingRepository.existsOverlappingBooking(testRoom, checkIn, checkOut)).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> bookingService.makeBooking(1L, 1L, checkIn, checkOut));
    }

    @Test
    public void shouldGetAllBookings() {
        // Arrange
        Booking booking1 = Booking.builder()
                .id(1L)
                .room(testRoom)
                .user(testUser)
                .checkInDate(LocalDate.of(2025, 8, 1))
                .checkOutDate(LocalDate.of(2025, 8, 5))
                .build();

        Booking booking2 = Booking.builder()
                .id(2L)
                .room(testRoom)
                .user(testUser)
                .checkInDate(LocalDate.of(2025, 8, 10))
                .checkOutDate(LocalDate.of(2025, 8, 15))
                .build();

        when(bookingRepository.findAll()).thenReturn(Arrays.asList(booking1, booking2));

        // Act
        List<Booking> bookings = bookingService.getAllBookings();

        // Assert
        assertThat(bookings.size()).isEqualTo(2);
        assertThat(bookings.contains(booking1)).isTrue();
        assertThat(bookings.contains(booking2)).isTrue();
    }

    @Test
    public void shouldCheckRoomAvailability() {
        // Arrange
        LocalDate checkIn = LocalDate.of(2025, 8, 1);
        LocalDate checkOut = LocalDate.of(2025, 8, 5);

        when(bookingRepository.existsOverlappingBooking(testRoom, checkIn, checkOut)).thenReturn(false);

        // Act
        boolean isAvailable = bookingService.isRoomAvailable(testRoom, checkIn, checkOut);

        // Assert
        assertThat(isAvailable).isTrue();
    }

    @Test
    public void shouldDetectRoomOverlap() {
        // Arrange
        LocalDate checkIn = LocalDate.of(2025, 8, 1);
        LocalDate checkOut = LocalDate.of(2025, 8, 5);

        when(bookingRepository.existsOverlappingBooking(testRoom, checkIn, checkOut)).thenReturn(true);

        // Act
        boolean isAvailable = bookingService.isRoomAvailable(testRoom, checkIn, checkOut);

        // Assert
        assertThat(isAvailable).isFalse();
    }
}