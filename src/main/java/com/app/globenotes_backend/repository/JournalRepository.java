package com.app.globenotes_backend.repository;

import com.app.globenotes_backend.entity.Journal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JournalRepository extends JpaRepository<Journal, Long> {
    Optional<Journal> findByUserIdAndId(Long userId, Long journalId);
    List<Journal> findAllByUserId(Long userId);
}
