package com.habitia.moderation.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BannedWordRepository {
    BannedWord save(BannedWord word);
    void deleteById(UUID id);
    Optional<BannedWord> findById(UUID id);
    List<BannedWord> findAll();
    Page<BannedWord> findAll(Pageable pageable);
    boolean existsByWord(String word);
}
