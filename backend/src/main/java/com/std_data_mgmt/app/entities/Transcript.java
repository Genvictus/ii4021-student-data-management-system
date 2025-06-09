package com.std_data_mgmt.app.entities;

import java.util.List;
import java.util.Optional;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.std_data_mgmt.app.dtos.TranscriptDto;
import com.std_data_mgmt.app.dtos.UserDto;
import com.std_data_mgmt.app.enums.TranscriptStatus;
import com.std_data_mgmt.app.utils.TranscriptDataConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
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

        public TranscriptDto toDto(boolean student, boolean headOfDepartment) {
                Optional<UserDto> studentDto = Optional
                                .ofNullable(student ? this.student.toDto(false) : null);
                Optional<UserDto> hodDto = Optional
                                .ofNullable(headOfDepartment ? this.headOfDepartment.toDto(false) : null);
                return new TranscriptDto(
                                transcriptId,
                                studentDto,
                                studentId,
                                transcriptStatus,
                                encryptedTranscriptData,
                                hodDto,
                                hodDigitalSignature,
                                encryptedKeyStudent,
                                encryptedKeySupervisor,
                                encryptedKeyHod);
        }
}
