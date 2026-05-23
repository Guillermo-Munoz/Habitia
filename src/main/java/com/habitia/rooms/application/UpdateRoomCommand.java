package com.habitia.rooms.application;

import com.habitia.rooms.domain.Amenity;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public record UpdateRoomCommand(
        UUID roomId,
        String requesterId,
        String title,
        String description,
        String street,
        String city,
        String country,
        BigDecimal priceAmount,
        String priceCurrency,
        int maxGuests,
        Set<Amenity> amenities
) {}
