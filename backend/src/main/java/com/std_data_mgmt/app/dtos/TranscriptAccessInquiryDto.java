package com.std_data_mgmt.app.dtos;

import java.util.List;

import com.std_data_mgmt.app.entities.TranscriptAccessInquiryParticipant;
import com.std_data_mgmt.app.enums.TranscriptAccessInquiryStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TranscriptAccessInquiryDto {
    private String inquiryId;
    private String requesterId;
    private String requesteeId;
    private TranscriptAccessInquiryStatus inquiryStatus;
    private List<TranscriptAccessInquiryParticipant> participants;
    private String transcriptId;
}
