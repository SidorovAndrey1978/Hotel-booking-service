package com.skillbox.hotel_reservations.model.roomDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagedRoomResponse {

    private List<RoomResponse> rooms;
    private long totalItems;
    private long totalPages;
    private long currentPage;
    private boolean hasNext;
    private boolean hasPrevious;
}
