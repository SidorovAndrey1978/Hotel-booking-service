package com.skillbox.hotel_reservations.mapper;

import com.skillbox.hotel_reservations.enyity.User;
import com.skillbox.hotel_reservations.model.userDTO.UserRequest;
import com.skillbox.hotel_reservations.model.userDTO.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User toEntity(UserRequest request);

    default User toEntity(Long id, UserRequest request){
        User user = toEntity(request);
        user.setId(id);
        return user;
    };

    UserResponse toResponse(User entity);

    List<UserResponse> toList(List<User> entities);
}
