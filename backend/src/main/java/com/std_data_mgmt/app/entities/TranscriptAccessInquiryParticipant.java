package com.std_data_mgmt.app.entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TranscriptAccessInquiryParticipant {
    private String id;
    private String public_key;
    private String encrypted_share;
}
