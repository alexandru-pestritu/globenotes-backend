package com.app.globenotes_backend.service.moment;

import com.app.globenotes_backend.dto.moment.MomentDetailsDTO;

import java.util.List;

public interface MomentService {
    MomentDetailsDTO addMoment(Long userId, MomentDetailsDTO momentDetailsDTO);
    MomentDetailsDTO updateMoment(Long userId, MomentDetailsDTO momentDetailsDTO);
    void deleteMoment(Long userId, Long momentId);
    MomentDetailsDTO getMoment(Long userId, Long momentId);
    List<MomentDetailsDTO> getAllMomentsByJournal(Long userId, Long journalId);
}
