package com.habitia.moderation.infrastructure.web;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.habitia.moderation.application.AddBannedWordUseCase;
import com.habitia.moderation.application.AddBannedWordsBulkUseCase;
import com.habitia.moderation.application.DeleteBannedWordUseCase;
import com.habitia.moderation.application.GetAllBannedWordsUseCase;
import com.habitia.moderation.domain.BannedWord;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/banned-words")
@PreAuthorize("hasRole('ADMIN')")
public class AdminBannedWordController {
    private final AddBannedWordUseCase addBannedWord;
    private final AddBannedWordsBulkUseCase addBannedWordsBulk;
    private final DeleteBannedWordUseCase deleteBannedWord;
    private final GetAllBannedWordsUseCase getAllBannedWords;

    public AdminBannedWordController(AddBannedWordUseCase addBannedWord,
                                     AddBannedWordsBulkUseCase addBannedWordsBulk,
                                     DeleteBannedWordUseCase deleteBannedWord,
                                     GetAllBannedWordsUseCase getAllBannedWords){
            this.addBannedWord = addBannedWord;
            this.addBannedWordsBulk = addBannedWordsBulk;
            this.deleteBannedWord = deleteBannedWord;
            this.getAllBannedWords = getAllBannedWords;
    }
    @GetMapping
    public ResponseEntity<Page<BannedWord>> getAll(
            @PageableDefault(size = 20, sort = "word", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(getAllBannedWords.execute(pageable));
    }
    @PostMapping
    public ResponseEntity<BannedWord> add(@RequestParam String word,
                                          @RequestParam(defaultValue = "2") int sureness) {
        return ResponseEntity.status(201).body(addBannedWord.execute(word, sureness));
    }

    @PostMapping("/bulk")
    public ResponseEntity<Map<String, Integer>> addBulk(
            @org.springframework.web.bind.annotation.RequestBody Map<String, Integer> words) {
        int added = addBannedWordsBulk.execute(words);
        return ResponseEntity.status(201).body(Map.of("added", added));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        deleteBannedWord.execute(id);
        return ResponseEntity.noContent().build();
    }
}
