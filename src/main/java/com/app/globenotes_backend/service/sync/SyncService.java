package com.app.globenotes_backend.service.sync;

import com.app.globenotes_backend.dto.sync.SyncDTO;

import java.time.Instant;

public interface SyncService {
    SyncDTO getSyncData(Long userId, Instant lastSync);
}
