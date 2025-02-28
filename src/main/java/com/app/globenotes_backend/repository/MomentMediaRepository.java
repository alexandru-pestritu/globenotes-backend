package com.app.globenotes_backend.repository;

import com.app.globenotes_backend.entity.MomentMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MomentMediaRepository extends JpaRepository<MomentMedia, Long> {
    Optional<MomentMedia> findByMediaUrlAndMoment_Id(String mediaUrl, Long momentId);
    List<MomentMedia> findAllByMoment_Id(Long momentId);
}
