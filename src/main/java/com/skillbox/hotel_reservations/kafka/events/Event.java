package com.skillbox.hotel_reservations.kafka.events;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public abstract class Event {
    
    private String eventType;
    private Instant timestamp;

    public Event(String eventType) {
        this.eventType = eventType;
        this.timestamp = Instant.now();
    }
}
