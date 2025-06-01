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
@RequiredArgsConstructor
@AllArgsConstructor
@ToString(of = {"userId", "role", "departmentId"})
public class User {
    @Id
    @Column(name = "user_id")
    private String userId;

    @NonNull
    @Column(name = "email")
    private String email;

    @NonNull
    @Column(name = "password")
    private String password;

    @NonNull
    @Column(name = "full_name")
    private String fullName;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @NonNull
    @Column(name = "public_key")
    private String publicKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", insertable = false, updatable = false)
    private Department department;

    @NonNull
    @Column(name = "department_id")
    private String departmentId;

    @Column(name = "supervisor_id")
    private String supervisorId;
}
