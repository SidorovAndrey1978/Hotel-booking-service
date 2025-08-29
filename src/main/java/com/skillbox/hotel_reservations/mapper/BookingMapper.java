package com.skillbox.hotel_reservations.mapper;

import com.skillbox.hotel_reservations.enyity.Booking;
import com.skillbox.hotel_reservations.model.bookingDTO.BookingResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookingMapper {
    
    @Mapping(target = "roomId", source = "room.id")
    @Mapping(target = "userId", source = "user.id")
    BookingResponse toResponse(Booking booking);
    
    List<BookingResponse> toList(List<Booking> bookings);
}
