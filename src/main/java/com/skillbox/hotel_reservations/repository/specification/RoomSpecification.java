package com.skillbox.hotel_reservations.repository.specification;

import com.skillbox.hotel_reservations.enyity.Booking;
import com.skillbox.hotel_reservations.enyity.Room;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface RoomSpecification {

    static Specification<Room> combineSpecifications(List<Specification<Room>> specs) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            for (Specification<Room> spec : specs) {
                Predicate predicate = spec.toPredicate(root, query, cb);
                if (predicate != null) {
                    predicates.add(predicate);
                }
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    static Specification<Room> byId(Long id) {
        return (root, query, cb) -> {
            if (id == null) {
                return null;
            }
            return cb.equal(root.get("id"), id);
        };
    }

    static Specification<Room> byTitleLike(String title) {
        return (root, query, cb) -> {
            if (title == null || title.trim().isEmpty()) {
                return null;
            }
            return cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
        };
    }

    static Specification<Room> byPriceRange(Float minPrice, Float maxPrice) {
        return (root, query, cb) -> {
            if (minPrice == null && maxPrice == null) {
                return null;
            }
            Path<BigDecimal> pricePath = root.get("pricePerNight");
            Predicate predicate = minPrice != null ? cb.ge(pricePath, minPrice) : cb.conjunction();
            predicate = maxPrice != null ? cb.and(predicate, cb.le(pricePath, maxPrice)) : predicate;
            return predicate;
        };
    }

    static Specification<Room> byGuestCapacity(Integer guests) {
        return (root, query, cb) -> {
            if (guests == null) {
                return null;
            }
            return cb.equal(root.get("capacity"), guests);
        };
    }

    static Specification<Room> byHotelId(Long hotelId) {
        return (root, query, cb) -> {
            if (hotelId == null) {
                return null;
            }
            return cb.equal(root.get("hotel").get("id"), hotelId);
        };
    }

    static Specification<Room> availableOnDates(LocalDate checkIn, LocalDate checkOut) {
        return (root, query, cb) -> {
            if (checkIn == null || checkOut == null) {
                return null;
            }
            
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Booking> bookingRoot = subquery.from(Booking.class);
            Join<Booking, Room> roomJoin = bookingRoot.join("room");
            
            subquery.select(roomJoin.get("id")).distinct(true).where(
                    cb.and(
                            cb.equal(roomJoin, root), 
                            cb.or(
                                    cb.between(bookingRoot.get("checkInDate"), checkIn, checkOut),
                                    cb.between(bookingRoot.get("checkOutDate"), checkIn, checkOut)
                            )
                    )
            );
            
            return root.get("id").in(subquery).not();
        };
    }
}
