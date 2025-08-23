package com.skillbox.hotel_reservations.model.roomDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomResponse {

    private Long id;
    private String title;
    private String description;
    private Integer numberRoom;
    private BigDecimal pricePerNight;
    private Integer capacity;
    private Set<LocalDate> unavailableDates;
}
