package com.habitia.moderation.application;

import com.habitia.moderation.domain.BannedWord;
import com.habitia.moderation.domain.BannedWordRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AddBannedWordsBulkUseCase {

    private final BannedWordRepository bannedWordRepository;

    public AddBannedWordsBulkUseCase(BannedWordRepository bannedWordRepository) {
        this.bannedWordRepository = bannedWordRepository;
    }

    public int execute(Map<String, Integer> words) {
        int added = 0;
        for (Map.Entry<String, Integer> entry : words.entrySet()) {
            String normalized = entry.getKey().trim().toLowerCase();
            int sureness = entry.getValue();
            if (normalized.isBlank() || sureness < 0 || sureness > 2) continue;
            if (bannedWordRepository.existsByWord(normalized)) continue;
            bannedWordRepository.save(new BannedWord(normalized, sureness));
            added++;
        }
        return added;
    }
}
