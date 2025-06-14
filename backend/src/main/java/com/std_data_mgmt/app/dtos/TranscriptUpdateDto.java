package com.std_data_mgmt.app.dtos;

import java.util.List;

import com.std_data_mgmt.app.entities.Transcript;
import com.std_data_mgmt.app.entities.TranscriptEntry;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TranscriptUpdateDto {
    private String studentId;
    private String encryptedTranscriptData;

    private String hodDigitalSignature;

    private String encryptedKeyStudent;
    private String encryptedKeySupervisor;
    private String encryptedKeyHod;

    public Transcript toTranscript() {
        Transcript transcript = new Transcript();
        transcript.setStudentId(studentId);
        transcript.setEncryptedTranscriptData(encryptedTranscriptData);
        transcript.setEncryptedKeyStudent(encryptedKeyStudent);
        transcript.setEncryptedKeySupervisor(encryptedKeySupervisor);
        transcript.setEncryptedKeyHod(encryptedKeyHod);
        return transcript;
    }
}
