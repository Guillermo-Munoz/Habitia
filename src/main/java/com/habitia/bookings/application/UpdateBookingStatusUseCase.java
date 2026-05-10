package com.habitia.bookings.application;

import com.habitia.bookings.domain.Booking;
import com.habitia.bookings.domain.BookingRepository;
import com.habitia.notifications.application.CreateNotificationUseCase;
import com.habitia.notifications.domain.NotificationType;
import com.habitia.shared.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateBookingStatusUseCase {

    private final BookingRepository bookingRepository;
    private final CreateNotificationUseCase createNotification;

    public UpdateBookingStatusUseCase(BookingRepository bookingRepository,
                                      CreateNotificationUseCase createNotification) {
        this.bookingRepository = bookingRepository;
        this.createNotification = createNotification;
    }

    public Booking accept(UUID bookingId) {
        Booking booking = findOrThrow(bookingId);
        booking.accept();
        Booking saved = bookingRepository.save(booking);
        createNotification.execute(saved.getGuestId().value(), NotificationType.BOOKING_ACCEPTED,
                "Your booking request has been accepted", bookingId);
        return saved;
    }

    public Booking reject(UUID bookingId) {
        Booking booking = findOrThrow(bookingId);
        booking.reject();
        Booking saved = bookingRepository.save(booking);
        createNotification.execute(saved.getGuestId().value(), NotificationType.BOOKING_REJECTED,
                "Your booking request has been rejected", bookingId);
        return saved;
    }

    public Booking confirm(UUID bookingId) {
        Booking booking = findOrThrow(bookingId);
        booking.confirm();
        Booking saved = bookingRepository.save(booking);
        createNotification.execute(saved.getHostId().value(), NotificationType.BOOKING_CONFIRMED,
                "A guest has confirmed their booking", bookingId);
        return saved;
    }

    public Booking cancel(UUID bookingId) {
        Booking booking = findOrThrow(bookingId);
        UUID guestId = booking.getGuestId().value();
        UUID hostId  = booking.getHostId().value();
        booking.cancel();
        Booking saved = bookingRepository.save(booking);
        createNotification.execute(hostId, NotificationType.BOOKING_CANCELLED,
                "A booking has been cancelled", bookingId);
        createNotification.execute(guestId, NotificationType.BOOKING_CANCELLED,
                "Your booking has been cancelled", bookingId);
        return saved;
    }

    public Booking complete(UUID bookingId) {
        Booking booking = findOrThrow(bookingId);
        booking.complete();
        Booking saved = bookingRepository.save(booking);
        createNotification.execute(saved.getGuestId().value(), NotificationType.BOOKING_COMPLETED,
                "Your stay is complete. Leave a review!", bookingId);
        return saved;
    }

    private Booking findOrThrow(UUID id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", id.toString()));
    }
}