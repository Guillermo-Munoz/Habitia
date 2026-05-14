package com.habitia.admin.application;

public record AdminStatsResponse(
        long totalUsers,
        long activeRooms,
        long pendingReviews
) {}
