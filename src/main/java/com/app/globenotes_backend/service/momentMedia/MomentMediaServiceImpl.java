package com.app.globenotes_backend.service.momentMedia;

import com.app.globenotes_backend.dto.momentMedia.MomentMediaDTO;
import com.app.globenotes_backend.dto.momentMedia.MomentMediaMapper;
import com.app.globenotes_backend.entity.MomentMedia;
import com.app.globenotes_backend.repository.MomentMediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MomentMediaServiceImpl implements MomentMediaService{
    private final MomentMediaRepository momentMediaRepository;
    private final MomentMediaMapper momentMediaMapper;

    @Override
    public MomentMedia createMomentMedia(MomentMediaDTO momentMedia) {
       Optional<MomentMedia> existingMomentMedia = momentMediaRepository.findByMediaUrlAndMoment_Id(momentMedia.getMediaUrl(), momentMedia.getMomentId());

        return existingMomentMedia.orElseGet(() -> momentMediaRepository.save(momentMediaMapper.toEntity(momentMedia)));
    }
}
