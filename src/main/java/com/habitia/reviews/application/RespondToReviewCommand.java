package com.habitia.reviews.application;

import java.util.UUID;

public record RespondToReviewCommand(
    UUID reviewId,
    UUID hostId,
    String response
) {}
