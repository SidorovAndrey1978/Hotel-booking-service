package com.skillbox.hotel_reservations.service;

import com.skillbox.hotel_reservations.enyity.Room;
import com.skillbox.hotel_reservations.model.filter.RoomFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoomService extends CrudService<Room, Long> {

    Page<Room> getFilteredRooms(RoomFilter roomFilter, Pageable pageable);
}
