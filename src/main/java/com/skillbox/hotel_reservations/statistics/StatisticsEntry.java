package com.skillbox.hotel_reservations.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "statistics")
public class StatisticsEntry {

    private Long userId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String eventType;
    private Instant timestamp;
}
