package com.std_data_mgmt.integration.services;

import com.std_data_mgmt.app.entities.User;
import com.std_data_mgmt.app.enums.Role;
import com.std_data_mgmt.app.repositories.UserRepository;
import com.std_data_mgmt.app.services.UserService;
import com.std_data_mgmt.integration.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserService Integration Tests")
public class UserServiceTest extends BaseIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Should retrieve a user by ID when it exists")
    void shouldGetUserByIdWhenExists() {
        // Given
        User user1 = User.builder()
                .userId("13521170")
                .departmentId("135")
                .email("haziqabiyyu@gmail.com")
                .password("haziqpass")
                .fullName("Haziq Abiyyu Mahdy")
                .publicKey("123456789")
                .role(Role.STUDENT)
                .build();

        var savedUser = userRepository.save(user1);
        var savedUserId = savedUser.getUserId();

        // When
        Optional<User> foundUser = userService.getUserById(savedUserId);

        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getFullName()).isEqualTo("Haziq Abiyyu Mahdy");
        assertThat(foundUser.get().getDepartmentId()).isEqualTo("135");
    }


    @Test
    @DisplayName("Should return empty when user by ID does not exist")
    void shouldReturnEmptyWhenUserByIdDoesNotExist() {
        // When
        Optional<User> foundUser = userService.getUserById("nonExistentId");

        // Then
        assertThat(foundUser).isNotPresent();
    }

    @Test
    @DisplayName("Should retrieve all users when no filters are provided")
    void shouldGetAllUsersWhenNoFilters() {
        // Given
        User user1 = User.builder()
                .userId("user1")
                .fullName("User One")
                .departmentId("135")
                .role(Role.STUDENT)
                .email("u1@example.com")
                .password("pass1")
                .publicKey("pk1")
                .build();
        User user2 = User.builder()
                .userId("user2")
                .fullName("User Two")
                .departmentId("182")
                .role(Role.SUPERVISOR)
                .email("u2@example.com")
                .password("pass2")
                .publicKey("pk2")
                .build();
        User user3 = User.builder()
                .userId("user3")
                .fullName("User Three")
                .departmentId("135")
                .role(Role.HOD)
                .email("u3@example.com")
                .password("pass3")
                .publicKey("pk3")
                .build();
        userRepository.saveAll(List.of(user1, user2, user3));

        // When
        List<User> users = userService.getUsers(Optional.empty(), Optional.empty());

        // Then
        assertThat(users).hasSize(3);
        assertThat(users).extracting(User::getUserId)
                .containsExactlyInAnyOrder("user1", "user2", "user3");
        assertThat(users).extracting(User::getFullName)
                .containsExactlyInAnyOrder("User One", "User Two", "User Three");
        assertThat(users).extracting(User::getDepartmentId)
                .containsOnly("135", "182"); // Ensure only valid department IDs are present
    }

    @Test
    @DisplayName("Should retrieve users filtered by department ID")
    void shouldGetUsersFilteredByDepartmentId() {
        // Given
        User user1 = User.builder()
                .userId("userA1")
                .fullName("Alice Dept 135")
                .departmentId("135")
                .role(Role.STUDENT)
                .email("alice@example.com")
                .password("pass")
                .publicKey("pk")
                .build();
        User user2 = User.builder()
                .userId("userB1")
                .fullName("Bob Dept 182")
                .departmentId("182")
                .role(Role.SUPERVISOR)
                .email("bob@example.com")
                .password("pass")
                .publicKey("pk")
                .build();
        User user3 = User.builder()
                .userId("userA2")
                .fullName("Charlie Dept 135")
                .departmentId("135")
                .role(Role.HOD)
                .email("charlie@example.com")
                .password("pass")
                .publicKey("pk")
                .build();
        User user4 = User.builder()
                .userId("userC1")
                .fullName("David Dept 182")
                .departmentId("182")
                .role(Role.STUDENT)
                .email("david@example.com")
                .password("pass")
                .publicKey("pk")
                .build();
        userRepository.saveAll(List.of(user1, user2, user3, user4));

        // When
        List<User> dept135Users = userService.getUsers(Optional.of("135"), Optional.empty());

        // Then
        assertThat(dept135Users).hasSize(2);
        assertThat(dept135Users).extracting(User::getUserId)
                .containsExactlyInAnyOrder("userA1", "userA2");
        assertThat(dept135Users).extracting(User::getFullName)
                .containsExactlyInAnyOrder("Alice Dept 135", "Charlie Dept 135");
        assertThat(dept135Users).extracting(User::getDepartmentId)
                .containsOnly("135"); // Ensure only users from department "135" are returned
    }

    @Test
    @DisplayName("Should retrieve users filtered by supervisor ID")
    void shouldGetUsersFilteredBySupervisorId() {
        // Given
        User user1 = User.builder()
                .userId("userS1A")
                .fullName("Student A Sup1")
                .departmentId("135")
                .supervisorId("Sup1")
                .role(Role.STUDENT)
                .email("s1a@example.com")
                .password("pass")
                .publicKey("pk")
                .build();
        User user2 = User.builder()
                .userId("userS2A")
                .fullName("Student A Sup2")
                .departmentId("182")
                .supervisorId("Sup2")
                .role(Role.STUDENT)
                .email("s2a@example.com")
                .password("pass")
                .publicKey("pk")
                .build();
        User user3 = User.builder()
                .userId("userS1B")
                .fullName("Student B Sup1")
                .departmentId("135")
                .supervisorId("Sup1")
                .role(Role.STUDENT)
                .email("s1b@example.com")
                .password("pass")
                .publicKey("pk")
                .build();
        userRepository.saveAll(List.of(user1, user2, user3));

        // When
        List<User> sup1Users = userService.getUsers(Optional.empty(), Optional.of("Sup1"));

        // Then
        assertThat(sup1Users).hasSize(2);
        assertThat(sup1Users).extracting(User::getUserId)
                .containsExactlyInAnyOrder("userS1A", "userS1B");
        assertThat(sup1Users).extracting(User::getSupervisorId)
                .containsOnly("Sup1");
        assertThat(sup1Users).extracting(User::getDepartmentId)
                .containsOnly("135");
    }

    @Test
    @DisplayName("Should retrieve users filtered by both department and supervisor ID")
    void shouldGetUsersFilteredByBothDepartmentAndSupervisorId() {
        // Given
        User user1 = User.builder()
                .userId("userD135S1")
                .fullName("User D135 S1")
                .departmentId("135")
                .supervisorId("Sup1")
                .role(Role.STUDENT)
                .email("d135s1@example.com")
                .password("pass")
                .publicKey("pk")
                .build();
        User user2 = User.builder()
                .userId("userD135S2")
                .fullName("User D135 S2")
                .departmentId("135") // Same department, different supervisor
                .supervisorId("Sup2")
                .role(Role.STUDENT)
                .email("d135s2@example.com")
                .password("pass")
                .publicKey("pk")
                .build();
        User user3 = User.builder()
                .userId("userD182S1")
                .fullName("User D182 S1")
                .departmentId("182") // Different department, same supervisor
                .supervisorId("Sup1")
                .role(Role.STUDENT)
                .email("d182s1@example.com")
                .password("pass")
                .publicKey("pk")
                .build();
        userRepository.saveAll(List.of(user1, user2, user3));

        // When
        List<User> filteredUsers = userService.getUsers(Optional.of("135"), Optional.of("Sup1"));

        // Then
        assertThat(filteredUsers).hasSize(1);
        assertThat(filteredUsers.getFirst().getUserId()).isEqualTo("userD135S1");
        assertThat(filteredUsers.getFirst().getFullName()).isEqualTo("User D135 S1");
        assertThat(filteredUsers.getFirst().getDepartmentId()).isEqualTo("135");
        assertThat(filteredUsers.getFirst().getSupervisorId()).isEqualTo("Sup1");
    }

    @Test
    @DisplayName("Should return empty list when no users match provided filters")
    void shouldReturnEmptyListWhenNoUsersMatchFilters() {
        // Given
        User user1 = User.builder()
                .userId("userOnly")
                .fullName("Only User")
                .departmentId("135")
                .supervisorId("OnlySup")
                .role(Role.STUDENT)
                .email("only@example.com")
                .password("pass")
                .publicKey("pk")
                .build();
        userRepository.save(user1);

        // When
        List<User> noMatchUsers = userService.getUsers(
                Optional.of("182"),
                Optional.of("NonExistentSup")
        );

        // Then
        assertThat(noMatchUsers).isEmpty();
    }
}