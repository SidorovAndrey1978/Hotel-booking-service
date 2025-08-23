package com.skillbox.hotel_reservations.model.roomDTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomRequest {

    private Long id;

    @NotBlank(message = "Заголовок обязателен")
    private String title;

    @NotBlank(message = "Описание обязательно")
    private String description;

    @Positive(message = "Номер комнаты должен быть положительным")
    private Integer numberRoom;

    @Positive(message = "Цена должна быть положительной")
    @DecimalMin(value = "0.01", inclusive = false)
    private BigDecimal pricePerNight;

    @Positive(message = "Максимальное количество гостей должно быть положительным числом")
    private Integer capacity;

    private Set<LocalDate> unavailableDates;
}
