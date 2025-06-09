package com.std_data_mgmt.app.dtos;

import java.util.List;
import java.util.Optional;

import com.std_data_mgmt.app.entities.TranscriptEntry;
import com.std_data_mgmt.app.enums.TranscriptStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Optional<UserDto> headofDepartment;
    private String hodDigitalSignature;
    private String encryptedKeyStudent;
    private String encryptedKeySupervisor;
    private String encryptedKeyHod;
}
