package com.habitia.rooms.application;

import com.habitia.rooms.domain.Amenity;

import java.math.BigDecimal;
import java.util.Set;

public record PublishRoomCommand(
        String hostId,
        String title,
        String description,
        String street,
        String city,
        String country,
        Double latitude,
        Double longitude,
        BigDecimal priceAmount,
        String priceCurrency,
        int maxGuests,
        Set<Amenity> amenities
) {}