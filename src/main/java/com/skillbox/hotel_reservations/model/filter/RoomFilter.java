package com.skillbox.hotel_reservations.model.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomFilter {

    private Long id;
    private String title;
    private Float minPrice;
    private Float maxPrice;
    private Integer guestCapacity;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Long hotelId;
}
