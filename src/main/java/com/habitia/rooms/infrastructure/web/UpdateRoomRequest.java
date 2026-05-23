package com.habitia.rooms.infrastructure.web;

import com.habitia.rooms.domain.Amenity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.Set;

public record UpdateRoomRequest(
        @NotBlank String title,
        String description,
        String street,
        @NotBlank String city,
        @NotBlank String country,
        @NotNull @Positive BigDecimal priceAmount,
        @NotBlank String priceCurrency,
        @Positive int maxGuests,
        Set<Amenity> amenities
) {}
