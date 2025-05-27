package com.std_data_mgmt.app.entities;

import com.std_data_mgmt.app.enums.TranscriptStatus;
import com.std_data_mgmt.app.utils.TranscriptDataConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

//TODO: Figure out if we'll use UUID type for all of the IDs in the Entity classes
//TODO: Figure out if we'll use String or other types (e.g. bytes) for encrypted data, keys, and digital signatures


@Entity
@Table(name = "transcript")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Transcript {
    @Id
    @Column(name = "transcript_id")
    private String transcriptId;

    @Column(name = "student_id")
    private String studentId;

    @Column(name = "status")
    private TranscriptStatus transcriptStatus;

    @Convert(converter = TranscriptDataConverter.class)
    @Column(name = "encrypted_transcript_data", columnDefinition = "jsonb")
    private List<TranscriptEntry> encryptedTranscriptData;

    @Column(name = "hod_id")
    private String hodId;

    @Column(name = "hod_digital_signature")
    private String hodDigitalSignature;

    @Column(name = "encrypted_key_student")
    private String encryptedKeyStudent;

    @Column(name = "encrypted_key_supervisor")
    private String encryptedKeySupervisor;

    @Column(name = "encrypted_key_hod")
    private String encryptedKeyHod;
}
