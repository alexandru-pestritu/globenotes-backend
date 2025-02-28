package com.app.globenotes_backend.dto.sync;

import com.app.globenotes_backend.dto.category.CategoryDTO;
import com.app.globenotes_backend.dto.journal.JournalDTO;
import com.app.globenotes_backend.dto.moment.MomentDetailsDTO;
import com.app.globenotes_backend.dto.user.UserDTO;
import com.app.globenotes_backend.dto.userProfile.UserProfileDetailsDTO;
import com.app.globenotes_backend.dto.userVisitedCountry.UserVisitedCountryDetailsDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyncDTO {
    UserDTO user;
    UserProfileDetailsDTO userProfile;
    List<UserVisitedCountryDetailsDTO> userVisitedCountries;
    List<JournalDTO> journals;
    List<MomentDetailsDTO> moments;
}
