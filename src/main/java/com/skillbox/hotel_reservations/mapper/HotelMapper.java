package com.skillbox.hotel_reservations.mapper;

import com.skillbox.hotel_reservations.enyity.Hotel;
import com.skillbox.hotel_reservations.model.hotelDTO.HotelRequest;
import com.skillbox.hotel_reservations.model.hotelDTO.HotelResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HotelMapper {


    Hotel toEntity(HotelRequest request);

    default Hotel toEntity(Long id, HotelRequest request) {
        Hotel hotel = toEntity(request);
        hotel.setId(id);
        return hotel;
    }

    HotelResponse toResponse(Hotel entity);

    List<HotelResponse> toList(List<Hotel> entities);

}
