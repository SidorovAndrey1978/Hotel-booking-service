package com.skillbox.hotel_reservations.repository;

import com.skillbox.hotel_reservations.enyity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
}
