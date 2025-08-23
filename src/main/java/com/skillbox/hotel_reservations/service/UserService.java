package com.skillbox.hotel_reservations.service;

import com.skillbox.hotel_reservations.enyity.User;

public interface UserService extends CrudService<User, Long> {

    User findByUsername(String username);
}
