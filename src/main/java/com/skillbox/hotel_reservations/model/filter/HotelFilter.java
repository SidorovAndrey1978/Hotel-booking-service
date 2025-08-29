package com.skillbox.hotel_reservations.model.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelFilter {

    private Long id;
    private String title;
    private String description;
    private String city;
    private String address;
    private Double distance;
    private Float minRating;
    private Float maxRating;
}
