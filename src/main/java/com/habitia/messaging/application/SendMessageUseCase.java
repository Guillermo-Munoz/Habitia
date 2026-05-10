package com.habitia.messaging.application;

import com.habitia.bookings.domain.Booking;
import com.habitia.bookings.domain.BookingRepository;
import com.habitia.messaging.domain.Conversation;
import com.habitia.messaging.domain.ConversationRepository;
import com.habitia.messaging.domain.Message;
import com.habitia.messaging.domain.MessageRepository;
import com.habitia.notifications.application.CreateNotificationUseCase;
import com.habitia.notifications.domain.NotificationType;
import com.habitia.shared.domain.exception.BusinessRuleException;
import com.habitia.shared.domain.exception.ResourceNotFoundException;
import com.habitia.shared.domain.moderation.ContentModerationService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SendMessageUseCase {

    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final BookingRepository bookingRepository;
    private final CreateNotificationUseCase createNotification;
    private final ContentModerationService moderationService;

    public SendMessageUseCase(MessageRepository messageRepository,
                              ConversationRepository conversationRepository,
                              BookingRepository bookingRepository,
                              CreateNotificationUseCase createNotification,
                              ContentModerationService moderationService) {
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
        this.bookingRepository = bookingRepository;
        this.createNotification = createNotification;
        this.moderationService = moderationService;
    }

    public Message execute(UUID bookingId, UUID senderId, String content) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", bookingId.toString()));

        UUID guestId = booking.getGuestId().value();
        UUID hostId  = booking.getHostId().value();

        if (!senderId.equals(guestId) && !senderId.equals(hostId)) {
            throw new BusinessRuleException("Only the guest or host of this booking can send messages");
        }

        var moderation = moderationService.analyze(content);
        if (!moderation.passed()) {
            throw new BusinessRuleException("Message blocked: " + moderation.reason());
        }

        Conversation conversation = conversationRepository.findByBookingId(bookingId)
                .orElseGet(() -> conversationRepository.save(
                        new Conversation(bookingId, guestId, hostId)));

        Message saved = messageRepository.save(new Message(conversation.getId(), senderId, content));

        // Notificar al destinatario (el que no es el remitente)
        UUID recipientId = senderId.equals(guestId) ? hostId : guestId;
        createNotification.execute(recipientId, NotificationType.MESSAGE_RECEIVED,
                "You have a new message", bookingId);

        return saved;
    }
}
