package com.std_data_mgmt.app.dtos;

import com.std_data_mgmt.app.entities.Transcript;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TranscriptUpdateDto {
    private String transcriptId;
    private String studentId;
    private String encryptedTranscriptData;

    private String hodDigitalSignature;

    private String encryptedKeyStudent;
    private String encryptedKeySupervisor;
    private String encryptedKeyHod;

    public Transcript toTranscript() {
        Transcript transcript = new Transcript();
        transcript.setTranscriptId(transcriptId);
        transcript.setStudentId(studentId);
        transcript.setEncryptedTranscriptData(encryptedTranscriptData);
        transcript.setEncryptedKeyStudent(encryptedKeyStudent);
        transcript.setEncryptedKeySupervisor(encryptedKeySupervisor);
        transcript.setEncryptedKeyHod(encryptedKeyHod);
        return transcript;
    }
}
