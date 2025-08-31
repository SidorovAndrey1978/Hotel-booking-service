package com.skillbox.hotel_reservations.service.Impl;

import com.skillbox.hotel_reservations.enyity.Hotel;
import com.skillbox.hotel_reservations.exception.EntityNotFoundException;
import com.skillbox.hotel_reservations.repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.skillbox.hotel_reservations.utils.messageUtils.Message.NOT_FOUND_HOTEL;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelServiceImpl hotelService;

    private Hotel testHotel;

    @BeforeEach
    public void setUp() {
        testHotel = Hotel.builder()
                .id(1L)
                .title("Test Hotel")
                .description("Beautiful place to stay.")
                .city("Test City")
                .address("Main street, 1")
                .distanceFromCenter(2.5)
                .rating(4.5f)
                .numberOfRatings(10)
                .build();
    }

    @Test
    public void shouldSuccessfullyCreateHotel() {
        // Arrange
        when(hotelRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        // Act
        Hotel saveHotel = hotelService.create(testHotel);

        // Assert
        verify(hotelRepository, times(1)).save(any());
        assertThat(saveHotel.getTitle()).isEqualTo("Test Hotel");
    }

    @Test
    public void shouldFindHotelById() {
        // Arrange
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(testHotel));

        // Act
        Hotel foundHotel = hotelService.getById(1L);

        // Assert
        assertThat(foundHotel.getTitle()).isEqualTo("Test Hotel");
    }

    @Test
    public void shouldFailWhenHotelNotFoundById() {
        // Arrange
        when(hotelRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> hotelService.getById(999L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(NOT_FOUND_HOTEL, 999);
    }

    @Test
    public void shouldUpdateHotel() {
        // Arrange
        Hotel updatedHotel = Hotel.builder()
                .id(1L)
                .title("Updated Title")
                .build();

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(testHotel));
        when(hotelRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        // Act
        Hotel result = hotelService.update(updatedHotel);

        // Assert
        assertThat(result.getTitle()).isEqualTo("Updated Title");
    }

    @Test
    public void shouldDeleteHotel() {
        // Arrange
        when(hotelRepository.findById(testHotel.getId())).thenReturn(Optional.of(testHotel));
        doNothing().when(hotelRepository).delete((Hotel) any());

        // Act
        hotelService.deleteById(1L);

        // Assert
        verify(hotelRepository, times(1)).delete((Hotel) any());
    }

    @Test
    public void shouldChangeHotelRating() {
        // Arrange
        when(hotelRepository.findById(testHotel.getId())).thenReturn(Optional.of(testHotel));
        when(hotelRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        // Act
        Hotel changedHotel = hotelService.changeRating(testHotel.getId(), 4.0F);

        // Assert
        assertThat(changedHotel).isNotNull();
        assertThat(changedHotel.getRating()).isCloseTo(4.0F, within(0.01F));
        assertThat(changedHotel.getNumberOfRatings()).isEqualTo(11);
        verify(hotelRepository, times(1)).save(any());
    }
}