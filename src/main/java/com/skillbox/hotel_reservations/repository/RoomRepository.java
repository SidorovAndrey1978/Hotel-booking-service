package com.skillbox.hotel_reservations.repository;

import com.skillbox.hotel_reservations.enyity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
