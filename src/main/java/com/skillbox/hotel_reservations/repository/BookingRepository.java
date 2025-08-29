package com.skillbox.hotel_reservations.repository;

import com.skillbox.hotel_reservations.enyity.Booking;
import com.skillbox.hotel_reservations.enyity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
                SELECT COUNT(b) > 0 FROM Booking b 
                WHERE b.room = :room 
                AND (:checkIn < b.checkOutDate AND :checkOut > b.checkInDate)
            """)
    boolean existsOverlappingBooking(Room room, LocalDate checkIn, LocalDate checkOut);
}
