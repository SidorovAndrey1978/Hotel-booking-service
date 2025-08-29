package com.skillbox.hotel_reservations.service.Impl;

import com.skillbox.hotel_reservations.enyity.Booking;
import com.skillbox.hotel_reservations.enyity.Room;
import com.skillbox.hotel_reservations.enyity.User;
import com.skillbox.hotel_reservations.kafka.events.RoomBookedEvent;
import com.skillbox.hotel_reservations.repository.BookingRepository;
import com.skillbox.hotel_reservations.service.BookingService;
import com.skillbox.hotel_reservations.service.RoomService;
import com.skillbox.hotel_reservations.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.skillbox.hotel_reservations.utils.messageUtils.Message.ROOM_IS_ALREADY_BOOKED;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final RoomService roomService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public Booking makeBooking(Long roomId, Long userId,
                               LocalDate checkIn, LocalDate checkOut) {

        Room room = roomService.getById(roomId);
        User user = userService.getById(userId);

        if (!isRoomAvailable(room, checkIn, checkOut)) {
            throw new IllegalArgumentException(ROOM_IS_ALREADY_BOOKED);
        }

        Booking booking = Booking.builder()
                .room(room)
                .user(user)
                .checkInDate(checkIn)
                .checkOutDate(checkOut)
                .build();

        publishEvent(booking);

        return bookingRepository.save(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isRoomAvailable(Room room, LocalDate checkIn, LocalDate checkOut) {
        return !bookingRepository.existsOverlappingBooking(room, checkIn, checkOut);
    }

    private void publishEvent(Booking booking) {
        applicationEventPublisher.publishEvent(new RoomBookedEvent(
                booking.getUser().getId(),
                booking.getCheckInDate(),
                booking.getCheckOutDate()));
    }

}
