package com.skillbox.hotel_reservations.kafka.events;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import static com.skillbox.hotel_reservations.utils.messageUtils.Message.ROOM_BOOKING;

@Getter
@Setter
public class RoomBookedEvent extends Event {
    
    private Long userId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    public RoomBookedEvent(Long userId, LocalDate checkInDate, LocalDate checkOutDate) {
        super(ROOM_BOOKING);
        this.userId = userId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;

    }
}
