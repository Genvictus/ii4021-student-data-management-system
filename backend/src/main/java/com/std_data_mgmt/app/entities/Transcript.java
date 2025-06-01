package com.std_data_mgmt.app.entities;

import java.util.List;

import com.std_data_mgmt.app.enums.TranscriptStatus;
import com.std_data_mgmt.app.utils.TranscriptDataConverter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

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
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "transcript_id")
    private String transcriptId;

    @NonNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "student_id", referencedColumnName = "user_id")
    private User student;

    @NonNull
    @Column(name = "status")
    private TranscriptStatus transcriptStatus;

    @NonNull
    @Convert(converter = TranscriptDataConverter.class)
    @Column(name = "encrypted_transcript_data", columnDefinition = "jsonb")
    private List<TranscriptEntry> encryptedTranscriptData;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "hod_id", referencedColumnName = "user_id")
    private User headOfDepartment;

    @NonNull
    @Column(name = "hod_digital_signature")
    private String hodDigitalSignature;

    @NonNull
    @Column(name = "encrypted_key_student")
    private String encryptedKeyStudent;

    @NonNull
    @Column(name = "encrypted_key_supervisor")
    private String encryptedKeySupervisor;

    @NonNull
    @Column(name = "encrypted_key_hod")
    private String encryptedKeyHod;
}
