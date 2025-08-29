package com.skillbox.hotel_reservations.service;

import com.skillbox.hotel_reservations.enyity.Booking;
import com.skillbox.hotel_reservations.enyity.Room;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {
    
    Booking makeBooking(Long roomId, Long userId, LocalDate checkIn, LocalDate checkOut);
    
    List<Booking> getAllBookings();
    
    boolean isRoomAvailable(Room room, LocalDate checkIn, LocalDate checkOut);
}
