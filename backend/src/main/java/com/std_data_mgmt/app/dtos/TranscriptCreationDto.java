package com.std_data_mgmt.app.dtos;

import java.util.List;

import com.std_data_mgmt.app.entities.Transcript;
import com.std_data_mgmt.app.entities.TranscriptEntry;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TranscriptCreationDto {
    @NotBlank
    private String studentId;
    @NotBlank
    private List<TranscriptEntry> encryptedTranscriptData;

    @NotBlank
    private String encryptedKeyStudent;
    @NotBlank
    private String encryptedKeySupervisor;
    @NotBlank
    private String encryptedKeyHod;

    public Transcript toTranscript() {
        return Transcript.builder()
                .studentId(studentId)
                .encryptedTranscriptData(encryptedTranscriptData)
                .encryptedKeyStudent(encryptedKeyStudent)
                .encryptedKeySupervisor(encryptedKeySupervisor)
                .encryptedKeyHod(encryptedKeyHod)
                .build();
    }
}
