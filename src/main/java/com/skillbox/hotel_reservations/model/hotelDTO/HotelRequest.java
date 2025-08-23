package com.skillbox.hotel_reservations.model.hotelDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotelRequest {

    @NotBlank(message = "Вы не указали название отеля!")
    private String title;

    @NotBlank(message = "Вы не указали описание!")
    @Size(min = 3, max = 50,
            message = "Описание отеля не может быть меньше {min} и больше {max}!")
    private String description;

    @NotBlank(message = "Вы не указали город!")
    private String city;

    @NotBlank(message = "Вы не указали адрес!")
    private String address;

    @NotBlank(message = "Расстояние от центра обязательно.")
    private double distanceFromCenter;
}
