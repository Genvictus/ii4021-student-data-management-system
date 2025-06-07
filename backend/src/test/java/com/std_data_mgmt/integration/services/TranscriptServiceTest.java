package com.std_data_mgmt.integration.services;

import com.std_data_mgmt.app.entities.Transcript;
import com.std_data_mgmt.app.entities.User;
import com.std_data_mgmt.app.enums.Role;
import com.std_data_mgmt.app.enums.TranscriptStatus;
import com.std_data_mgmt.app.repositories.TranscriptRepository;
import com.std_data_mgmt.app.repositories.UserRepository;
import com.std_data_mgmt.app.services.TranscriptService;
import com.std_data_mgmt.integration.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

/**
 * Assumes no encryption (yet)
 */
@DisplayName("TranscriptService Integration Tests")
public class TranscriptServiceTest extends BaseIntegrationTest {
    @Autowired
    private TranscriptService transcriptService;

    @Autowired
    private TranscriptRepository transcriptRepository;

    @Autowired
    private UserRepository userRepository;

    private Transcript transcript1;
    private Transcript transcript2;

    private String hod135Id;
    private String hod182Id;

    private String student_135_1_id;
    private String student_182_1_id;

    @BeforeEach
    void setUp() {
        setUpUsers();
        setUpTranscripts();
    }

    @Test
    @DisplayName("to do")
    void todo() {

    }

    private void setUpTranscripts() {
        transcriptRepository.deleteAll();

        transcript1 = Transcript.builder()
                .hodId(hod135Id)
                .studentId(student_135_1_id)
                .encryptedKeyHod("lasdfklasdkf")
                .encryptedKeyStudent("lkdfjdslkfjsldf")
                .encryptedKeySupervisor("jdflkjfdlakfjsd")
                .encryptedTranscriptData(List.of())
                .transcriptStatus(TranscriptStatus.PENDING)
                .build();

        transcript2 = Transcript.builder()
                .hodId(hod182Id)
                .studentId(student_182_1_id)
                .encryptedKeyHod("lasdfklasdkf")
                .encryptedKeyStudent("lkdfjdslkfjsldf")
                .encryptedKeySupervisor("jdflkjfdlakfjsd")
                .encryptedTranscriptData(List.of())
                .transcriptStatus(TranscriptStatus.APPROVED)
                .build();

        transcript1 = transcriptRepository.save(transcript1);
        transcript2 = transcriptRepository.save(transcript2);
    }

    private void setUpUsers() {
        userRepository.deleteAll();

        hod135Id = UUID.randomUUID().toString();
        User hod135 = User.builder()
                .userId(hod135Id)
                .fullName("HOD 135")
                .departmentId("135")
                .role(Role.HOD)
                .email("hod135@email.com")
                .password("passhod135")
                .publicKey("pkhod135")
                .build();

        hod182Id = UUID.randomUUID().toString();
        User hod182 = User.builder()
                .userId(hod182Id)
                .fullName("HOD 182")
                .departmentId("182")
                .role(Role.HOD)
                .email("hod182@email.com")
                .password("passhod182")
                .publicKey("pkhod182")
                .build();

        User supervisor135_1 = User.builder()
                .userId(UUID.randomUUID().toString())
                .fullName("Supervisor 135_1")
                .departmentId("135")
                .role(Role.SUPERVISOR)
                .email("supervisor135_1@email.com")
                .password("passsup135_1")
                .publicKey("pksup135_1")
                .build();

        User supervisor135_2 = User.builder()
                .userId(UUID.randomUUID().toString())
                .fullName("Supervisor 135_2")
                .departmentId("135")
                .role(Role.SUPERVISOR)
                .email("supervisor135_2@email.com")
                .password("passsup135_2")
                .publicKey("pksup135_2")
                .build();

        User supervisor182_1 = User.builder()
                .userId(UUID.randomUUID().toString())
                .fullName("Supervisor 182_1")
                .departmentId("182")
                .role(Role.SUPERVISOR)
                .email("supervisor182_1@email.com")
                .password("passsup182_1")
                .publicKey("pksup182_1")
                .build();

        student_135_1_id = UUID.randomUUID().toString();
        User student135_1 = User.builder()
                .userId(student_135_1_id)
                .fullName("Student 135_1")
                .departmentId("135")
                .role(Role.STUDENT)
                .email("student135_1@email.com")
                .password("passstd135_1")
                .publicKey("pkstd135_1")
                .build();

        student_182_1_id = UUID.randomUUID().toString();
        User student182_1 = User.builder()
                .userId(student_182_1_id)
                .fullName("Student 182_1")
                .departmentId("182")
                .role(Role.STUDENT)
                .email("student182_1@email.com")
                .password("passstd182_1")
                .publicKey("pkstd182_1")
                .build();

        userRepository.saveAll(List.of(
                hod135,
                hod182,
                supervisor135_1,
                supervisor135_2,
                supervisor182_1,
                student135_1,
                student182_1
        ));
    }
}
