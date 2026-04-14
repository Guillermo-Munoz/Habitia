package com.habitia.reviews.application;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.habitia.reviews.domain.Review;
import com.habitia.reviews.domain.ReviewRepository;

@Service
public class GetReviewsByRoomUseCase {
    private final ReviewRepository reviewRepository;

    public GetReviewsByRoomUseCase(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Page<Review> execute(UUID roomId, Pageable pageable){
        return reviewRepository.findByRoomId(roomId, pageable);
    }
}
