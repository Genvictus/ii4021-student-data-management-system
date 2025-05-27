package com.std_data_mgmt.app.entities;

import com.std_data_mgmt.app.enums.TranscriptAccessInquiryStatus;
import com.std_data_mgmt.app.utils.TranscriptDataConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Id;

import java.util.List;

public class TranscriptAccessInquiry {
    @Id
    @Column(name = "inquiry_id")
    private String inquiryId;

    @Column(name = "requester_id")
    private String requesterId;

    @Column(name = "status")
    private TranscriptAccessInquiryStatus transcriptStatus;

    @Convert(converter = TranscriptDataConverter.class)
    @Column(name = "participants", columnDefinition = "jsonb")
    private List<TranscriptAccessInquiryParticipant> participants;
}
