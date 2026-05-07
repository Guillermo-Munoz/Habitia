package com.habitia.moderation.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class BannedWord {
    private final UUID id;
    private final String word;
    private final int sureness;
    private final LocalDateTime createdAt;

    public BannedWord(String word, int sureness) {
        if (word == null || word.isBlank()) throw new IllegalArgumentException("Word cannot be null or blank");
        if (sureness < 0 || sureness > 2) throw new IllegalArgumentException("Sureness must be 0, 1 or 2");
        this.id = UUID.randomUUID();
        this.word = word.toLowerCase();
        this.sureness = sureness;
        this.createdAt = LocalDateTime.now();
    }

    public BannedWord(UUID id, String word, int sureness, LocalDateTime createdAt) {
        this.id = id;
        this.word = word.toLowerCase();
        this.sureness = sureness;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public String getWord() { return word; }
    public int getSureness() { return sureness; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
