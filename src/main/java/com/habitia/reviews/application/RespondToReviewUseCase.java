package com.habitia.reviews.application;

import org.springframework.stereotype.Service;

import com.habitia.bookings.domain.BookingRepository;
import com.habitia.reviews.domain.Review;
import com.habitia.reviews.domain.ReviewRepository;
import com.habitia.shared.domain.exception.ResourceNotFoundException;

@Service
public class RespondToReviewUseCase {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;

    public RespondToReviewUseCase(ReviewRepository reviewRepository, BookingRepository bookingRepository) {
        this.reviewRepository = reviewRepository;
        this.bookingRepository = bookingRepository;
    }

    /**
     * Permite al anfitrión responder a una reseña recibida.
     * Valida que el hostId del token corresponde al anfitrión de la reserva asociada.
     */
    public Review execute(RespondToReviewCommand command) {
        Review review = reviewRepository.findById(command.reviewId())
                .orElseThrow(() -> new ResourceNotFoundException("Review", command.reviewId().toString()));

        var booking = bookingRepository.findById(review.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking", review.getBookingId().toString()));

        // Solo el anfitrión de la reserva puede responder
        if (!booking.getHostId().value().equals(command.hostId())) {
            throw new IllegalArgumentException("Host is not the owner of the booking related to this review.");
        }

        review.respondToReview(command.response(), command.hostId());
        return reviewRepository.save(review);
    }
}
