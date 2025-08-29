package com.skillbox.hotel_reservations.service;

import com.skillbox.hotel_reservations.enyity.Hotel;
import com.skillbox.hotel_reservations.model.filter.HotelFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HotelService extends CrudService<Hotel, Long> {

    Hotel changeRating(Long hotelId, float newMark);

    Page<Hotel> getFilteredHotels(HotelFilter hotelFilter, Pageable pageable);
}
