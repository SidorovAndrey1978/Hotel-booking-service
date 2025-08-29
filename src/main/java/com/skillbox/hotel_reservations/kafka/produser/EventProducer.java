package com.skillbox.hotel_reservations.kafka.produser;

import com.skillbox.hotel_reservations.kafka.events.Event;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventProducer {

    private final KafkaTemplate<String, Event> kafkaTemplate;

    public void sendEvent(Event event, String topicName) {
        kafkaTemplate.send(new ProducerRecord<>(topicName, event));
    }
}
