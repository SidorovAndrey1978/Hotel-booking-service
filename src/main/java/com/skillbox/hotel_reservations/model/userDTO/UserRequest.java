package com.skillbox.hotel_reservations.model.userDTO;

import com.skillbox.hotel_reservations.enyity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    @NotEmpty(message = "Имя пользователя обязательно")
    private String username;

    @NotEmpty(message = "Пароль обязателен")
    private String password;

    @Email(message = "Неверный формат адреса электронной почты")
    @NotEmpty(message = "Электронная почта обязательна")
    private String email;

    @NotEmpty(message = "Роль пользователя обязательно")
    private Set<Role> roles;
}
