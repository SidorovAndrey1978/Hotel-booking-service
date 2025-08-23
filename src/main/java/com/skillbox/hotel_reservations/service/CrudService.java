package com.skillbox.hotel_reservations.service;

import com.skillbox.hotel_reservations.enyity.Hotel;

import java.util.List;

public interface CrudService<T, ID> {

    T getById(ID id);

    List<T> findAll();

    T create(T t);

    T update(T t);

    void deleteById(ID id);
}
