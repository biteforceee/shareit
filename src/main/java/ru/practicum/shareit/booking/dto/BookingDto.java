package ru.practicum.shareit.booking.dto;

import lombok.Data;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class BookingDto {
    private Long id;

    private String start;

    private String end;

    private Long itemId;

    private String status;
}
