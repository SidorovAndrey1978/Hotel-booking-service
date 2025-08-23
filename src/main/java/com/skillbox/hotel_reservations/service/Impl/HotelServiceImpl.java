package com.skillbox.hotel_reservations.service.Impl;

import com.skillbox.hotel_reservations.enyity.Hotel;
import com.skillbox.hotel_reservations.exception.EntityNotFoundException;
import com.skillbox.hotel_reservations.repository.HotelRepository;
import com.skillbox.hotel_reservations.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.skillbox.hotel_reservations.utils.messageUtils.Message.NOT_FOUND_HOTEL;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Hotel> findAll() {
        return hotelRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Hotel getById(Long id) {
        return hotelRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format(
                        NOT_FOUND_HOTEL, id
                )));
    }

    @Override
    @Transactional
    public Hotel create(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    @Override
    @Transactional
    public Hotel update(Hotel hotel) {
        Hotel existsHotel = getById(hotel.getId());
        BeanUtils.copyProperties(hotel, existsHotel, "id");
        return hotelRepository.save(existsHotel);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        hotelRepository.delete(getById(id));
    }
}
