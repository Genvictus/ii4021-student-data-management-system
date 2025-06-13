package com.std_data_mgmt.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.std_data_mgmt.app.dtos.TranscriptDto;
import com.std_data_mgmt.app.dtos.UserDto;
import com.std_data_mgmt.app.enums.Role;
import com.std_data_mgmt.app.enums.TranscriptStatus;
import com.std_data_mgmt.app.utils.TranscriptDataConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.Optional;

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

    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
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

    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hod_id", referencedColumnName = "user_id")
    private User headOfDepartment;

    @NonNull
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

    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @OneToMany(mappedBy = "transcript", fetch = FetchType.LAZY)
    private List<TranscriptAccessInquiry> transcriptAccessInquiries;

    public TranscriptDto toDto(boolean includeStudent, boolean includeHod, Role viewerRole) {
        Optional<UserDto> studentDto = Optional
                .ofNullable(includeStudent ? this.student.toDto(false) : null);
        Optional<UserDto> hodDto = Optional
                .ofNullable(includeHod ? this.headOfDepartment.toDto(false) : null);

        String encryptedKey = switch (viewerRole) {
            case STUDENT -> this.encryptedKeyStudent;
            case SUPERVISOR -> this.encryptedKeySupervisor;
            case HOD -> this.encryptedKeyHod;
        };

        return new TranscriptDto(
                transcriptId,
                studentDto,
                studentId,
                transcriptStatus,
                encryptedTranscriptData,
                hodId,
                hodDto,
                hodDigitalSignature,
                encryptedKey);
    }
}
