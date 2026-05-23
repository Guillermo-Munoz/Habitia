package com.habitia.rooms.application;

import com.habitia.rooms.domain.Room;
import com.habitia.rooms.domain.RoomRepository;
import com.habitia.shared.domain.exception.BusinessRuleException;
import com.habitia.shared.domain.valueobject.Money;
import org.springframework.stereotype.Service;

@Service
public class UpdateRoomUseCase {

    private final RoomRepository roomRepository;

    public UpdateRoomUseCase(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room execute(UpdateRoomCommand command) {
        Room room = roomRepository.findById(command.roomId())
                .orElseThrow(() -> new BusinessRuleException("Room not found"));

        if (!room.getHostId().value().toString().equals(command.requesterId())) {
            throw new BusinessRuleException("Only the host can edit this room");
        }

        room.update(
                command.title(),
                command.description(),
                command.street(),
                command.city(),
                command.country(),
                Money.of(command.priceAmount(), command.priceCurrency()),
                command.maxGuests(),
                command.amenities()
        );

        return roomRepository.save(room);
    }
}
