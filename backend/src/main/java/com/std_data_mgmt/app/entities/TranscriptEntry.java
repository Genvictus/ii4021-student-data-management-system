package com.std_data_mgmt.app.entities;

import com.std_data_mgmt.app.enums.Score;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TranscriptEntry {
    private String courseCode;
    private int credits;
    private Score score;
}
