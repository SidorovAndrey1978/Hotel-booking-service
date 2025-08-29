package com.skillbox.hotel_reservations.service.Impl;

import com.skillbox.hotel_reservations.enyity.Room;
import com.skillbox.hotel_reservations.exception.EntityNotFoundException;
import com.skillbox.hotel_reservations.model.filter.RoomFilter;
import com.skillbox.hotel_reservations.repository.RoomRepository;
import com.skillbox.hotel_reservations.repository.specification.RoomSpecification;
import com.skillbox.hotel_reservations.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.skillbox.hotel_reservations.utils.messageUtils.Message.NOT_FOUND_ROOM;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Room> getFilteredRooms(RoomFilter filter, Pageable pageable) {
        List<Specification<Room>> specifications = new ArrayList<>();
        specifications.add(RoomSpecification.byId(filter.getId()));
        specifications.add(RoomSpecification.byTitleLike(filter.getTitle()));
        specifications.add(RoomSpecification.byPriceRange(filter.getMinPrice(), filter.getMaxPrice()));
        specifications.add(RoomSpecification.byGuestCapacity(filter.getGuestCapacity()));
        specifications.add(RoomSpecification.byHotelId(filter.getHotelId()));
        specifications.add(RoomSpecification.availableOnDates(filter.getCheckInDate(), filter.getCheckOutDate()));
        
        Specification<Room> combinedSpecs = RoomSpecification.combineSpecifications(specifications);

        return roomRepository.findAll(combinedSpecs, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Room getById(Long id) {
        return roomRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format(
                        NOT_FOUND_ROOM, id
                )));
    }

    @Override
    @Transactional
    public Room create(Room room) {
        return roomRepository.save(room);
    }

    @Override
    @Transactional
    public Room update(Room room) {
        return roomRepository.save(room);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        roomRepository.delete(getById(id));
    }
}
