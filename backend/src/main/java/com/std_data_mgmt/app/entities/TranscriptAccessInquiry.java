package com.std_data_mgmt.app.entities;

import java.util.List;

import com.std_data_mgmt.app.enums.TranscriptAccessInquiryStatus;
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
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
public class TranscriptAccessInquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "inquiry_id")
    private String inquiryId;

    @NonNull
    @Column(name = "requester_id")
    private String requesterId;

    @NonNull
    @Column(name = "status")
    private TranscriptAccessInquiryStatus transcriptStatus;

    @NonNull
    @Convert(converter = TranscriptDataConverter.class)
    @Column(name = "participants", columnDefinition = "jsonb")
    private List<TranscriptAccessInquiryParticipant> participants;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "transcript_id", referencedColumnName = "transcript_id", insertable = false, updatable = false)
    private Transcript transcript;

    @NonNull
    @Column(name = "transcript_id")
    private String transcriptId;
}
