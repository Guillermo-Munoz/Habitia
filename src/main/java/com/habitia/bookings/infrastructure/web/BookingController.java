package com.habitia.bookings.infrastructure.web;

import com.habitia.bookings.application.*;
import com.habitia.rooms.application.GetRoomUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final RequestBookingUseCase requestBookingUseCase;
    private final UpdateBookingStatusUseCase updateBookingStatusUseCase;
    private final GetBookingsAsGuestUseCase getBookingsAsGuestUseCase;
    private final GetBookingsAsHostUseCase getBookingsAsHostUseCase;
    private final GetRoomUseCase getRoomUseCase;

    public BookingController(
            RequestBookingUseCase requestBookingUseCase,
            UpdateBookingStatusUseCase updateBookingStatusUseCase,
            GetBookingsAsGuestUseCase getBookingsAsGuestUseCase,
            GetBookingsAsHostUseCase getBookingsAsHostUseCase,
            GetRoomUseCase getRoomUseCase) {
        this.requestBookingUseCase = requestBookingUseCase;
        this.updateBookingStatusUseCase = updateBookingStatusUseCase;
        this.getBookingsAsGuestUseCase = getBookingsAsGuestUseCase;
        this.getBookingsAsHostUseCase = getBookingsAsHostUseCase;
        this.getRoomUseCase = getRoomUseCase;
    }

    @PostMapping
    public ResponseEntity<BookingResponse> request(
            @Valid @RequestBody BookingRequest request,
            Authentication auth) {
        var booking = requestBookingUseCase.execute(new RequestBookingCommand(
                auth.getName(),
                request.roomId(),
                request.hostId(),
                request.checkIn(),
                request.checkOut(),
                request.guests(),
                request.message()
        ));
        return ResponseEntity.status(HttpStatus.CREATED).body(BookingResponse.from(booking));
    }

    @PatchMapping("/{id}/accept")
    public ResponseEntity<BookingResponse> accept(@PathVariable UUID id) {
        return ResponseEntity.ok(toEnrichedResponse(updateBookingStatusUseCase.accept(id)));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<BookingResponse> reject(@PathVariable UUID id) {
        return ResponseEntity.ok(toEnrichedResponse(updateBookingStatusUseCase.reject(id)));
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<BookingResponse> confirm(@PathVariable UUID id) {
        return ResponseEntity.ok(toEnrichedResponse(updateBookingStatusUseCase.confirm(id)));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<BookingResponse> cancel(@PathVariable UUID id) {
        return ResponseEntity.ok(toEnrichedResponse(updateBookingStatusUseCase.cancel(id)));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<BookingResponse> complete(@PathVariable UUID id) {
        return ResponseEntity.ok(toEnrichedResponse(updateBookingStatusUseCase.complete(id)));
    }

    @GetMapping("/guest/me")
    public ResponseEntity<List<BookingResponse>> getMyBookingsAsGuest(Authentication auth) {
        return ResponseEntity.ok(getBookingsAsGuestUseCase.execute(auth.getName()).stream()
                .map(b -> toEnrichedResponse(b)).toList());
    }

    @GetMapping("/host/me")
    public ResponseEntity<List<BookingResponse>> getMyBookingsAsHost(Authentication auth) {
        return ResponseEntity.ok(getBookingsAsHostUseCase.execute(auth.getName()).stream()
                .map(b -> toEnrichedResponse(b)).toList());
    }

    private BookingResponse toEnrichedResponse(com.habitia.bookings.domain.Booking b) {
        try {
            var room = getRoomUseCase.execute(b.getRoomId());
            String image = room.getImageUrls().isEmpty() ? null : room.getImageUrls().get(0);
            return BookingResponse.from(b, room.getTitle(), image);
        } catch (Exception e) {
            return BookingResponse.from(b);
        }
    }
}