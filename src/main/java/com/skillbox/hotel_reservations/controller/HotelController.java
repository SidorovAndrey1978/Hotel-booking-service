package com.skillbox.hotel_reservations.controller;

import com.skillbox.hotel_reservations.enyity.Hotel;
import com.skillbox.hotel_reservations.mapper.HotelMapper;
import com.skillbox.hotel_reservations.model.filter.HotelFilter;
import com.skillbox.hotel_reservations.model.hotelDTO.HotelRequest;
import com.skillbox.hotel_reservations.model.hotelDTO.HotelResponse;
import com.skillbox.hotel_reservations.model.hotelDTO.PagedHotelResponse;
import com.skillbox.hotel_reservations.service.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<PagedHotelResponse> getPagedHotels(@RequestBody HotelFilter hotelFilter,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {

        Pageable paging = PageRequest.of(page, size);
        Page<Hotel> resultPage = hotelService.getFilteredHotels(hotelFilter, paging);

        List<HotelResponse> hotels = hotelMapper.toList(resultPage.getContent());

        PagedHotelResponse pagedResponse = PagedHotelResponse.builder()
                .hotels(hotels)
                .totalItems(resultPage.getTotalElements())
                .totalPages(resultPage.getTotalPages())
                .currentPage(resultPage.getNumber())
                .hasNext(resultPage.hasNext())
                .hasPrevious(resultPage.hasPrevious())
                .build();

        return ResponseEntity.ok(pagedResponse);

    }

    @PutMapping("/{id}/rate/{mark}")
    public ResponseEntity<HotelResponse> changeHotelRating(@PathVariable Long id,
                                                           @PathVariable Float newMark) {
        Hotel hotel = hotelService.changeRating(id, newMark);
        return ResponseEntity.ok(hotelMapper.toResponse(hotel));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<HotelResponse> create(@Valid @RequestBody HotelRequest request) {
        Hotel createHotel = hotelService.create(hotelMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(hotelMapper.toResponse(createHotel));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<HotelResponse> update(@PathVariable Long id,
                                                @Valid @RequestBody HotelRequest request) {

        Hotel updateHotel = hotelService.update(hotelMapper.toEntity(id, request));
        return ResponseEntity.ok(hotelMapper.toResponse(updateHotel));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        hotelService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
