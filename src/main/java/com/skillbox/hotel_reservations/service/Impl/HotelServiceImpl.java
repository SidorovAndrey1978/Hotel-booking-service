package com.skillbox.hotel_reservations.service.Impl;

import com.skillbox.hotel_reservations.enyity.Hotel;
import com.skillbox.hotel_reservations.exception.EntityNotFoundException;
import com.skillbox.hotel_reservations.model.filter.HotelFilter;
import com.skillbox.hotel_reservations.repository.HotelRepository;
import com.skillbox.hotel_reservations.repository.specification.HotelSpecification;
import com.skillbox.hotel_reservations.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.skillbox.hotel_reservations.utils.messageUtils.Message.NOT_FOUND_HOTEL;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Hotel> findAll() {
        return hotelRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Hotel> getFilteredHotels(HotelFilter filter, Pageable pageable) {

        List<Specification<Hotel>> specifications = new ArrayList<>();
        specifications.add(HotelSpecification.byId(filter.getId()));
        specifications.add(HotelSpecification.byTitleLike(filter.getTitle()));
        specifications.add(HotelSpecification.byDescriptionLike(filter.getDescription()));
        specifications.add(HotelSpecification.byCityEqual(filter.getCity()));
        specifications.add(HotelSpecification.byAddressLike(filter.getAddress()));
        specifications.add(HotelSpecification.byDistanceLessThan(filter.getDistance()));
        specifications.add(HotelSpecification.byRatingBetween(filter.getMinRating(), filter.getMaxRating()));

        Specification<Hotel> combinedSpecs = HotelSpecification.combineSpecifications(specifications);

        return hotelRepository.findAll(combinedSpecs, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Hotel getById(Long id) {
        return hotelRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format(
                        NOT_FOUND_HOTEL, id
                )));
    }

    @Override
    @Transactional
    public Hotel create(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    @Override
    @Transactional
    public Hotel update(Hotel hotel) {
        Hotel existsHotel = getById(hotel.getId());
        BeanUtils.copyProperties(hotel, existsHotel, "id");
        return hotelRepository.save(existsHotel);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        hotelRepository.delete(getById(id));
    }

    @Override
    @Transactional
    public Hotel changeRating(Long hotelId, float newMark) {

        Hotel hotel = getById(hotelId);

        if (newMark < 1 || newMark > 5) {
            throw new IllegalArgumentException("Оценка должна быть от 1 до 5");
        }

        float oldRating = hotel.getRating();
        int numberOfRaters = hotel.getNumberOfRatings();

        float totalRating = oldRating * numberOfRaters;

        totalRating -= oldRating;
        totalRating += newMark;

        float newAverageRating = Math.round((totalRating / (numberOfRaters + 1)) * 10f) / 10f;

        hotel.setNumberOfRatings(numberOfRaters + 1);
        hotel.setRating(newAverageRating);

        return hotelRepository.save(hotel);
    }
}
