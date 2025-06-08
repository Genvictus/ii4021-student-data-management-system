package com.std_data_mgmt.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.std_data_mgmt.app.enums.TranscriptAccessInquiryStatus;
import com.std_data_mgmt.app.utils.TranscriptDataConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User requester;

    @NonNull
    @Column(name = "requester_id")
    private String requesterId;

    @NonNull
    @Column(name = "requestee_id")
    private String requesteeId;

//    TODO: decide if we need this
//    @NonNull
//    @Column(name = "student_id")
//    private String studentId;

    @NonNull
    @Column(name = "status")
    private TranscriptAccessInquiryStatus transcriptStatus;

    @NonNull
    @Convert(converter = TranscriptDataConverter.class)
    @Column(name = "participants", columnDefinition = "jsonb")
    private List<TranscriptAccessInquiryParticipant> participants;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transcript_id", referencedColumnName = "transcript_id", insertable = false, updatable = false)
    private Transcript transcript;

    @NonNull
    @Column(name = "transcript_id")
    private String transcriptId;
}
