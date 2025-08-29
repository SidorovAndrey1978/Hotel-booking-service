package com.skillbox.hotel_reservations.model.bookingDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse {

    private Long id;
    private Long roomId;
    private Long userId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
}
