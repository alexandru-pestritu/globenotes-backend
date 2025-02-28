package com.app.globenotes_backend.service.momentMedia;

import com.app.globenotes_backend.dto.momentMedia.MomentMediaDTO;
import com.app.globenotes_backend.entity.MomentMedia;

public interface MomentMediaService {
    MomentMedia createMomentMedia(MomentMediaDTO momentMedia);
}
