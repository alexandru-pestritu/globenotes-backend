package com.app.globenotes_backend.service.journal;

import com.app.globenotes_backend.dto.journal.JournalDTO;
import com.app.globenotes_backend.dto.journal.JournalDetailsDTO;

import java.util.List;

public interface JournalService {
    JournalDTO createJournal(JournalDTO journalDTO);
    JournalDetailsDTO getJournalDetails(Long userId, Long journalId);
    JournalDTO updateJournal(JournalDTO journalDTO);
    void deleteJournal(Long userId, Long journalId);
    List<JournalDTO> getJournals(Long userId);

}
