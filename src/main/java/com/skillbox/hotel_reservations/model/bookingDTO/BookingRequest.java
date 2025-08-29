package com.skillbox.hotel_reservations.model.bookingDTO;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequest {

    @NotNull(message = "Комната обязательна")
    private Long roomId;

    @NotNull(message = "Заезд обязателен")
    @Future(message = "Дата заезда должна быть в будущем")
    private LocalDate checkInDate;

    @NotNull(message = "Выезд обязателен")
    @Future(message = "Дата выезда должна быть в будущем")
    private LocalDate checkOutDate;
}
