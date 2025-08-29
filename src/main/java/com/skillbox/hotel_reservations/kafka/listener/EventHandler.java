package com.skillbox.hotel_reservations.kafka.listener;

import com.skillbox.hotel_reservations.kafka.events.RoomBookedEvent;
import com.skillbox.hotel_reservations.kafka.events.UserRegisteredEvent;
import com.skillbox.hotel_reservations.kafka.produser.EventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventHandler {

    private final EventProducer eventProducer;

    @Value("${spring.kafka.topics.register.topic-name}")
    private String registerTopic;

    @Value("${spring.kafka.topics.booking.topic-name}")
    private String bookingTopic;

    @EventListener(condition = "#events.eventType == 'USER_REGISTERED'")
    public void handleUserRegisteredEvent(UserRegisteredEvent event) {
        eventProducer.sendEvent(event, registerTopic);
    }

    @EventListener(condition = "#events.eventType == 'ROOM_BOOKING'")
    public void handleRoomBookingEvent(RoomBookedEvent event) {
        eventProducer.sendEvent(event, bookingTopic);
    }
}
