package com.app.globenotes_backend.repository;

import com.app.globenotes_backend.entity.MomentMedia;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface MomentMediaRepository extends JpaRepository<MomentMedia, Long> {
    Optional<MomentMedia> findByMediaUrlAndMoment_Id(String mediaUrl, Long momentId);
    List<MomentMedia> findAllByMoment_Id(Long momentId);
    List<MomentMedia> findAllByMoment_IdAndIsDeletedFalse(Long id);
    @Modifying
    @Transactional
    @Query("""
           DELETE FROM MomentMedia mm
           WHERE mm.moment.id = :momentId
             AND mm.id NOT IN :ids
           """)
    void deleteByMoment_IdAndIdNotIn(Long momentId, Collection<Long> ids);
}
