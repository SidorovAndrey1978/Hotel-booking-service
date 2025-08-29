package com.skillbox.hotel_reservations.kafka.listener;

import com.skillbox.hotel_reservations.kafka.events.RoomBookedEvent;
import com.skillbox.hotel_reservations.kafka.events.UserRegisteredEvent;
import com.skillbox.hotel_reservations.statistics.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventConsumer {

    private final StatisticService statisticService;

    @KafkaListener(topics = "${spring.kafka.topics.register.topic-name}", 
            groupId = "${spring.kafka.topics.register.group-id}",
    containerFactory = "registerKafkaListenerContainerFactory")
    public void handleRegisterEvent(UserRegisteredEvent event) {
        
        statisticService.processEvent(event);
    }

    @KafkaListener(topics = "${spring.kafka.topics.booking.topic-name}", 
            groupId = "${spring.kafka.topics.booking.group-id}",
            containerFactory = "bookingKafkaListenerContainerFactory")
    public void handleBookingEvent(RoomBookedEvent event) {
        
        statisticService.processEvent(event);
    }
}
