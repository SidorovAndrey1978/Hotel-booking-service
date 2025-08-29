package com.skillbox.hotel_reservations.controller;

import com.skillbox.hotel_reservations.enyity.Booking;
import com.skillbox.hotel_reservations.enyity.User;
import com.skillbox.hotel_reservations.mapper.BookingMapper;
import com.skillbox.hotel_reservations.model.bookingDTO.BookingRequest;
import com.skillbox.hotel_reservations.model.bookingDTO.BookingResponse;
import com.skillbox.hotel_reservations.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    @PostMapping
    public ResponseEntity<BookingResponse> makeBooking(@Valid @RequestBody BookingRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User authenticatedUser  = (User) authentication.getPrincipal(); 
        
        Booking booking = bookingService.makeBooking(
                request.getRoomId(),
                authenticatedUser .getId(),
                request.getCheckInDate(),
                request.getCheckOutDate()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookingMapper.toResponse(booking));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookingMapper.toList(bookings));
    }
}
