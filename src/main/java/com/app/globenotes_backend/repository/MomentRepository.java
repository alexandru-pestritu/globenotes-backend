package com.app.globenotes_backend.repository;

import com.app.globenotes_backend.entity.Moment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MomentRepository extends JpaRepository<Moment, Long> {
    Optional<Moment> findByIdAndJournal_User_IdAndIsDeletedFalse(Long momentId, Long userId);

    List<Moment> findAllByJournal_IdAndJournal_User_IdAndIsDeletedFalse(Long journalId, Long userId);
}
