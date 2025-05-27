package com.std_data_mgmt.app.entities;

import com.std_data_mgmt.app.enums.Role;
import jakarta.persistence.*;
import lombok.*;

// TODO: add null-safety with annotation for every Entity classes

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"userId", "role", "departmentId"})
public class User {
    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "full_name")
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Column(name = "public_key")
    private String publicKey;

    @Column(name = "department_id")
    private String departmentId;

    @Column(name = "supervisor_id")
    private String supervisorId;
}
