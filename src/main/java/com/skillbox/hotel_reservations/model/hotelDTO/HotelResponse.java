package com.skillbox.hotel_reservations.model.hotelDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelResponse {

    private Long id;
    private String title;
    private String description;
    private String city;
    private String address;
    private double distanceFromCenter;
    private int rating;
    private int numberOfRatings;
}
