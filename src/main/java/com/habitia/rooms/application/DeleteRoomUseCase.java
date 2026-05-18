package com.habitia.rooms.application;

import com.habitia.rooms.domain.Room;
import com.habitia.rooms.domain.RoomRepository;
import com.habitia.shared.domain.exception.BusinessRuleException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteRoomUseCase {

    private final RoomRepository roomRepository;

    public DeleteRoomUseCase(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public void execute(UUID roomId, String requesterId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new BusinessRuleException("Room not found"));

        if (!room.getHostId().value().toString().equals(requesterId)) {
            throw new BusinessRuleException("Only the host can delete this room");
        }

        room.delete();
        roomRepository.save(room);
    }
}
