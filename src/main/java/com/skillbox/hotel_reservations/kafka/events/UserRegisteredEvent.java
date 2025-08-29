package com.skillbox.hotel_reservations.kafka.events;

import lombok.Getter;
import lombok.Setter;

import static com.skillbox.hotel_reservations.utils.messageUtils.Message.USER_REGISTER;

@Getter
@Setter
public class UserRegisteredEvent extends Event {

    private Long userId;
    
    public UserRegisteredEvent(Long userId) {
        super(USER_REGISTER);
        this.userId = userId;
    }
}
