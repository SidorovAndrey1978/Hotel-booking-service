package com.skillbox.hotel_reservations.statistics;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface StatisticsRepository extends MongoRepository<StatisticsEntry, String> {
}
