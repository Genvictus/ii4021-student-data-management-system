package com.std_data_mgmt.app.entities;

import com.std_data_mgmt.app.enums.Score;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TranscriptEntry {
    private String courseCode;
    private Integer credits;
    private Score score;
}
