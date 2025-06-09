package com.std_data_mgmt.app.entities;

import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.std_data_mgmt.app.enums.TranscriptAccessInquiryStatus;
import com.std_data_mgmt.app.utils.TranscriptAccessInquiryParticipantConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "transcript_access")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
public class TranscriptAccessInquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "inquiry_id")
    private String inquiryId;

    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User requester;

    @NonNull
    @Column(name = "requester_id")
    private String requesterId;

    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requestee_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User requestee;

    @NonNull
    @Column(name = "requestee_id")
    private String requesteeId;

    // TODO: decide if we need this
    // @NonNull
    // @Column(name = "student_id")
    // private String studentId;

    @NonNull
    @Column(name = "status")
    private TranscriptAccessInquiryStatus inquiryStatus;

    @NonNull
    @Convert(converter = TranscriptAccessInquiryParticipantConverter.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "participants", columnDefinition = "jsonb")
    private List<TranscriptAccessInquiryParticipant> participants;

    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transcript_id", referencedColumnName = "transcript_id", insertable = false, updatable = false)
    private Transcript transcript;

    @NonNull
    @Column(name = "transcript_id")
    private String transcriptId;
}
