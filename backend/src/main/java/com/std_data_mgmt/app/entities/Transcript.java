package com.std_data_mgmt.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.std_data_mgmt.app.enums.TranscriptStatus;
import com.std_data_mgmt.app.utils.TranscriptDataConverter;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

//TODO: Figure out if we'll use UUID type for all of the IDs in the Entity classes
//TODO: Figure out if we'll use String or other types (e.g. bytes) for encrypted data, keys, and digital signatures

@Entity
@Table(name = "transcript")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Transcript {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "transcript_id")
    private String transcriptId;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User student;

    @NonNull
    @Column(name = "student_id")
    private String studentId;

    @NonNull
    @Column(name = "status")
    private TranscriptStatus transcriptStatus;

    @NonNull
    @Convert(converter = TranscriptDataConverter.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "encrypted_transcript_data", columnDefinition = "jsonb")
    private List<TranscriptEntry> encryptedTranscriptData;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hod_id", referencedColumnName = "user_id")
    private User headOfDepartment;

    @Nonnull
    @Column(name = "hod_id", insertable = false, updatable = false)
    private String hodId;

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

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToMany(mappedBy = "transcript", fetch = FetchType.LAZY)
    private List<TranscriptAccessInquiry> transcriptAccessInquiries;
}
