package com.skillbox.hotel_reservations.mapper;

import com.skillbox.hotel_reservations.enyity.Room;
import com.skillbox.hotel_reservations.model.roomDTO.RoomRequest;
import com.skillbox.hotel_reservations.model.roomDTO.RoomResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomMapper {
    
    Room toEntity(RoomRequest request);
    
    @Mapping(target = "hotel", ignore = true)
    default Room toEntity(Long id, RoomRequest request){
        Room room = toEntity(request);
        room.setId(id);
        return room;
    };

    RoomResponse toResponse(Room entity);
    
    List<RoomResponse> toList(List<Room> entities);
}
