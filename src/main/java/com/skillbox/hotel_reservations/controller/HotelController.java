package com.skillbox.hotel_reservations.controller;

import com.skillbox.hotel_reservations.enyity.Hotel;
import com.skillbox.hotel_reservations.mapper.HotelMapper;
import com.skillbox.hotel_reservations.model.hotelDTO.HotelRequest;
import com.skillbox.hotel_reservations.model.hotelDTO.HotelResponse;
import com.skillbox.hotel_reservations.service.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;
    private final HotelMapper hotelMapper;

    @GetMapping("/{id}")
    public ResponseEntity<HotelResponse> getHotel(@PathVariable Long id) {
        Hotel hotel = hotelService.getById(id);
        return ResponseEntity.ok(hotelMapper.toResponse(hotel));
    }

    @GetMapping
    public ResponseEntity<List<HotelResponse>> listHotels() {
        List<Hotel> hotels = hotelService.findAll();
        return ResponseEntity.ok(hotelMapper.toList(hotels));
    }

    @PostMapping
    public ResponseEntity<HotelResponse> create(@Valid @RequestBody HotelRequest request) {
        Hotel createHotel = hotelService.create(hotelMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(hotelMapper.toResponse(createHotel));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelResponse> update(@PathVariable Long id,
                                                @Valid @RequestBody HotelRequest request) {

        Hotel updateHotel = hotelService.update(hotelMapper.toEntity(id, request));
        return ResponseEntity.ok(hotelMapper.toResponse(updateHotel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        hotelService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
