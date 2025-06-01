package com.std_data_mgmt.app.entities;

import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Student extends User {
    @OneToOne(mappedBy = "student", fetch = FetchType.EAGER)
    private Transcript transcript;
}
