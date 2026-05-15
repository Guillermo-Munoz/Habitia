package com.habitia.moderation.application;

import com.habitia.moderation.domain.BannedWord;
import com.habitia.moderation.domain.BannedWordRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GetAllBannedWordsUseCase {
    private final BannedWordRepository bannedWordRepository;

    public GetAllBannedWordsUseCase(BannedWordRepository bannedWordRepository) {
        this.bannedWordRepository = bannedWordRepository;
    }

    public Page<BannedWord> execute(Pageable pageable) {
        return bannedWordRepository.findAll(pageable);
    }
}
