package com.std_data_mgmt.app.dtos;

import com.std_data_mgmt.app.entities.TranscriptEntry;
import com.std_data_mgmt.app.enums.TranscriptStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TranscriptDto {
    private String transcriptId;
    private Optional<UserDto> student;
    private String studentId;
    private TranscriptStatus transcriptStatus;
    private List<TranscriptEntry> encryptedTranscriptData;
    private String hodId;
    private Optional<UserDto> hod;
    private String hodDigitalSignature;
    private String encryptedKey;
}
