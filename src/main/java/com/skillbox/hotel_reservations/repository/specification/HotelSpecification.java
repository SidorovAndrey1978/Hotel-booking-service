package com.skillbox.hotel_reservations.repository.specification;

import com.skillbox.hotel_reservations.enyity.Hotel;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public interface HotelSpecification {

    static Specification<Hotel> combineSpecifications(List<Specification<Hotel>> specs) {
        return ((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            for (Specification<Hotel> spec : specs) {
                Predicate predicate = spec.toPredicate(root, query, cb);
                if (predicate != null) {
                    predicates.add(predicate);
                }
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        });
    }
    
    static Specification<Hotel> byId(Long id) {
        return (root, query, cb) -> {
            if (id == null){
                return null;
            }
            return cb.equal(root.get("id"), id);
        };
    }
    
    static Specification<Hotel> byTitleLike(String title) {
        return (root, query, cb) -> {
            if (title == null || title.trim().isEmpty()) {
                return null;
            }
            return cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"); 
        };
    }

    static Specification<Hotel> byDescriptionLike(String description) {
        return (root, query, cb) -> {
            if (description == null || description.trim().isEmpty()) {
                return null;
            }
            return cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%");
        };
    }

    static Specification<Hotel> byCityEqual(String city) {
        return (root, query, cb) -> {
            if (city == null || city.trim().isEmpty()) {
                return null;
            }
            return cb.equal(root.get("city"), city);
        };
    }

    static Specification<Hotel> byAddressLike(String address) {
        return (root, query, cb) -> {
            if (address == null || address.trim().isEmpty()) {
                return null;
            }
            return cb.like(cb.lower(root.get("address")), "%" + address.toLowerCase() + "%");
        };
    }

    static Specification<Hotel> byDistanceLessThan(Double distance) {
        return (root, query, cb) -> {
            if (distance == null) {
                return null;
            }
            return cb.lessThanOrEqualTo(root.get("distanceFromCenter"), distance);
        };
    }

    static Specification<Hotel> byRatingBetween(Float minRating, Float maxRating) {
        return (root, query, cb) -> {
            if (minRating == null && maxRating == null) {
                return null;
            }

            Path<Float> ratingPath = root.get("rating");
            
            Predicate predicate = minRating != null ? 
                    cb.ge(ratingPath, minRating) :
                    cb.conjunction(); 

            predicate = maxRating != null ?
                    cb.and(predicate, cb.le(ratingPath, maxRating)) :
                    predicate;

            return predicate;
        };
    }
}
