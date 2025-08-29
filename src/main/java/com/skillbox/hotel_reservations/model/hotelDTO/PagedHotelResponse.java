package com.skillbox.hotel_reservations.model.hotelDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagedHotelResponse {
    private List<HotelResponse> hotels;
    private long totalItems;
    private long totalPages;
    private long currentPage;
    private boolean hasNext;
    private boolean hasPrevious;
}
