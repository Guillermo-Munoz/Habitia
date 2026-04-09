package com.habitia.reviews.infrastructure.web;

import java.util.UUID;

// El reviewerId no se incluye aquí — se extrae del token JWT en el controller
public record SubmitReviewRequest(
    UUID bookingId,
    int rating,
    String comment,
    boolean isReviewForHost,
    boolean isPublic
) {}
