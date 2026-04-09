package com.habitia.reviews.infrastructure.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewJpaRepository extends JpaRepository<ReviewJpaEntity, UUID> {

    List<ReviewJpaEntity> findByBookingId(UUID bookingId);
    
    @Query("SELECT r FROM ReviewJpaEntity r " +
           "JOIN BookingJpaEntity b ON r.bookingId = b.id " +
           "WHERE b.roomId = :roomId")
    List<ReviewJpaEntity> findByRoomId(@Param("roomId") UUID roomId);
    boolean existsByBookingIdAndReviewerId(UUID bookingId, UUID reviewerId);


} 
