package com.habitia.admin.application;

import com.habitia.reviews.infrastructure.persistence.ReviewJpaRepository;
import com.habitia.rooms.infrastructure.persistence.RoomJpaRepository;
import com.habitia.users.infrastructure.persistence.UserJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class GetAdminStatsUseCase {

    private final UserJpaRepository userRepository;
    private final RoomJpaRepository roomRepository;
    private final ReviewJpaRepository reviewRepository;

    public GetAdminStatsUseCase(UserJpaRepository userRepository,
                                RoomJpaRepository roomRepository,
                                ReviewJpaRepository reviewRepository) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.reviewRepository = reviewRepository;
    }

    public AdminStatsResponse execute() {
        long totalUsers    = userRepository.count();
        long activeRooms   = roomRepository.countByStatus("ACTIVE");
        long pendingReviews = reviewRepository.countFlaggedPendingReview();
        return new AdminStatsResponse(totalUsers, activeRooms, pendingReviews);
    }
}
