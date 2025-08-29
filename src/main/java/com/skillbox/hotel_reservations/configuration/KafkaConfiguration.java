package com.skillbox.hotel_reservations.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillbox.hotel_reservations.kafka.events.Event;
import com.skillbox.hotel_reservations.kafka.events.RoomBookedEvent;
import com.skillbox.hotel_reservations.kafka.events.UserRegisteredEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.topics.register.group-id}")
    private String registerGroupId;

    @Value("${spring.kafka.topics.booking.group-id}")
    private String bookingGroupId;

    private final ObjectMapper objectMapper;

    @Bean
    public ProducerFactory<String, Event> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "com.skillbox.hotel_reservations.kafka.events");
        return new DefaultKafkaProducerFactory<>(configProps, new StringSerializer(),
                new JsonSerializer<>(objectMapper));
    }

    @Bean
    public KafkaTemplate<String, Event> kafkaTemplate(
            ProducerFactory<String, Event> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public ConsumerFactory<String, UserRegisteredEvent> registerConsumerFactory() {
        return createConsumerFactory(UserRegisteredEvent.class, registerGroupId);
    }

    @Bean
    public ConsumerFactory<String, RoomBookedEvent> bookingConsumerFactory() {
        return createConsumerFactory(RoomBookedEvent.class, bookingGroupId);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserRegisteredEvent> registerKafkaListenerContainerFactory(
            ConsumerFactory<String, UserRegisteredEvent> registerConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, UserRegisteredEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(registerConsumerFactory);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RoomBookedEvent> bookingKafkaListenerContainerFactory(
            ConsumerFactory<String, RoomBookedEvent> bookingConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, RoomBookedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(bookingConsumerFactory);
        return factory;
    }

    private <T> ConsumerFactory<String, T> createConsumerFactory(Class<T> clazz, String groupId) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "com.skillbox.hotel_reservations.kafka.events");

        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(),
                new JsonDeserializer<>(clazz, objectMapper));
    }
}
