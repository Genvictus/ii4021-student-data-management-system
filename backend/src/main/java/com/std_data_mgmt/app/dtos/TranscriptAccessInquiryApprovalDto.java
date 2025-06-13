package com.std_data_mgmt.app.dtos;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TranscriptAccessInquiryApprovalDto {
    private Map<String, String> encryptedShares;
}
