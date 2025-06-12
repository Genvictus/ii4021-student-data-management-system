package com.std_data_mgmt.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.std_data_mgmt.app.dtos.DepartmentDto;
import com.std_data_mgmt.app.dtos.UserDto;
import com.std_data_mgmt.app.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.Optional;

@Entity
@Table(name = "user")
@Getter
@Setter
@Builder
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
    @Column(name = "public_key", columnDefinition = "text")
    private String publicKey;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", insertable = false, updatable = false)
    private Department department;

    @NonNull
    @Column(name = "department_id")
    private String departmentId;

    @Column(name = "supervisor_id")
    private String supervisorId;

    public UserDto toDto(boolean includeDepartment) {
        Optional<DepartmentDto> departmentDto = Optional
                .ofNullable(includeDepartment ? this.department.toDto() : null);
        return new UserDto(
                userId,
                email,
                fullName,
                role,
                publicKey,
                departmentDto,
                departmentId,
                supervisorId
        );
    }
}
