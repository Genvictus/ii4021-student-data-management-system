package com.std_data_mgmt.app.entities;

import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student extends User {
    @OneToOne(mappedBy = "user")
    private Transcript transcript;
}
