package com.habitia.reviews.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.habitia.reviews.domain.Review;
import com.habitia.reviews.domain.ReviewRepository;

@Service
public class GetFlaggedReviewsUseCase {

    private final ReviewRepository reviewRepository;

    public GetFlaggedReviewsUseCase(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Page<Review> execute(Pageable pageable) {
        return reviewRepository.findFlaggedPendingReview(pageable);
    }
}
