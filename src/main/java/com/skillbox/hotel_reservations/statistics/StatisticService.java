package com.skillbox.hotel_reservations.statistics;

import com.skillbox.hotel_reservations.kafka.events.Event;
import com.skillbox.hotel_reservations.kafka.events.RoomBookedEvent;
import com.skillbox.hotel_reservations.kafka.events.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final StatisticsRepository statisticsRepository;

    public void processEvent(Event event) {
        StatisticsEntry entry = new StatisticsEntry();
        entry.setUserId(event instanceof UserRegisteredEvent ?
                ((UserRegisteredEvent) event).getUserId() : ((RoomBookedEvent) event).getUserId());
        entry.setCheckInDate(event instanceof RoomBookedEvent ?
                ((RoomBookedEvent) event).getCheckInDate() : null);
        entry.setCheckOutDate(event instanceof RoomBookedEvent ?
                ((RoomBookedEvent) event).getCheckOutDate() : null);
        entry.setEventType(event.getEventType());
        entry.setTimestamp(event.getTimestamp());
        statisticsRepository.save(entry);
    }
}