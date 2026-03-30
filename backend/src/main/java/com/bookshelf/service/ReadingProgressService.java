package com.bookshelf.service;

import com.bookshelf.dto.request.ProgressRequest;
import com.bookshelf.entity.Book;
import com.bookshelf.entity.ReadingProgress;
import com.bookshelf.entity.User;
import com.bookshelf.repository.BookRepository;
import com.bookshelf.repository.ReadingProgressRepository;
import com.bookshelf.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReadingProgressService {

    private final ReadingProgressRepository progressRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReadingProgress updateProgress(String email, Long bookId, ProgressRequest req) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = bookRepository.findByIdAndUser_Id(bookId, user.getId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        ReadingProgress progress = progressRepository.findByBook_Id(bookId)
                .orElse(ReadingProgress.builder().book(book).build());

        if (req.getPagesRead() != null) progress.setPagesRead(req.getPagesRead());
        if (req.getIsCompleted() != null) progress.setIsCompleted(req.getIsCompleted());

        return progressRepository.save(progress);
    }
}
