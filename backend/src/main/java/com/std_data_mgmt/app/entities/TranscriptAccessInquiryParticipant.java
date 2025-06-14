package com.std_data_mgmt.app.entities;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TranscriptAccessInquiryParticipant {
    @NonNull
    private String id;
    private String encryptedShare;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String publicKey;
}
