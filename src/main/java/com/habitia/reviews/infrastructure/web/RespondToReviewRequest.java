package com.habitia.reviews.infrastructure.web;

// El hostId se extrae del token JWT en el controller
public record RespondToReviewRequest(
        String response
) {}
