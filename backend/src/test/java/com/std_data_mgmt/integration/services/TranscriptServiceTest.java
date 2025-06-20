package com.std_data_mgmt.integration.services;

import com.std_data_mgmt.app.entities.Transcript;
import com.std_data_mgmt.app.entities.TranscriptAccessInquiryParticipant;
import com.std_data_mgmt.app.entities.TranscriptEntry;
import com.std_data_mgmt.app.entities.User;
import com.std_data_mgmt.app.enums.Role;
import com.std_data_mgmt.app.enums.Score;
import com.std_data_mgmt.app.enums.TranscriptAccessInquiryStatus;
import com.std_data_mgmt.app.enums.TranscriptStatus;
import com.std_data_mgmt.app.repositories.TranscriptAccessInquiryRepository;
import com.std_data_mgmt.app.repositories.TranscriptRepository;
import com.std_data_mgmt.app.repositories.UserRepository;
import com.std_data_mgmt.app.services.TranscriptService;
import com.std_data_mgmt.integration.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Assumes no encryption (yet)
 */
@DisplayName("TranscriptService Integration Tests")
public class TranscriptServiceTest extends BaseIntegrationTest {
    public static final int MINIMUM_PARTICIPANTS = 2;
    public static final String department135Id = "135";
    public static final String department182Id = "182";

    @Autowired
    private TranscriptService transcriptService;

    @Autowired
    private TranscriptRepository transcriptRepository;

    @Autowired
    private TranscriptAccessInquiryRepository transcriptAccessInquiryRepository;

    @Autowired
    private UserRepository userRepository;

    private Transcript transcript1;
    private Transcript transcript2;

    private String hod135Id;
    private String hod182Id;

    private String student_135_1_id;
    private String student_182_1_id;
    private String student_182_2_id;
    private String supervisor_135_1_id;
    private String supervisor_135_2_id;
    private String supervisor_135_3_id;
    private String supervisor_135_4_id;
    private String supervisor_135_5_id;
    private String supervisor_182_1_id;

    @BeforeEach
    void setUp() {
        transcriptAccessInquiryRepository.deleteAll(); // Clean up inquiries before each transcript setup
        transcriptRepository.deleteAll();
        userRepository.deleteAll();
        setUpUsers();
        setUpTranscripts();
    }

    @Test
    @DisplayName("Should get transcript by ID")
    void shouldGetTranscriptById() {
        var foundTranscript1 = transcriptService.getTranscriptById(this.transcript1.getTranscriptId());
        var foundTranscript2 = transcriptService.getTranscriptById(this.transcript2.getTranscriptId());

        assertThat(foundTranscript1).isPresent();
        assertThat(foundTranscript2).isPresent();
    }

    @Test
    @DisplayName("Should not get transcript with non-existing ID")
    void shouldNotGetTranscriptWithNonExistingId() {
        var nonExistentTranscript = transcriptService.getTranscriptById("NON-EXISTENT-ID");
        assertThat(nonExistentTranscript).isEmpty();
    }

    @Test
    @DisplayName("Should create valid transcript")
    void shouldCreateTranscript() {
        var transcript = Transcript.builder()
                .hodId(hod182Id)
                .studentId(student_182_2_id)
                .encryptedKeyHod("aaaaaaaaaaaaaaaa")
                .encryptedKeyStudent("bbbbbbbbbbbbbbbb")
                .encryptedKeySupervisor("cccccccccccccccccc")
                .encryptedTranscriptData("")
                .transcriptStatus(TranscriptStatus.PENDING)
                .build();

        var savedTranscript = transcriptService.createTranscript(transcript, supervisor_182_1_id, department182Id);
        var foundTranscript = transcriptRepository.findById(savedTranscript.getTranscriptId());

        assertThat(foundTranscript).isPresent();
    }

    @Test
    @DisplayName("Should fail to create transcript if the student already has one")
    void shouldFailToCreateTranscriptIfTheStudentAlreadyHasOne() {
        var transcript = Transcript.builder()
                .hodId(hod182Id)
                .studentId(student_182_1_id)
                .encryptedKeyHod("aaaaaaaaaaaaaaaa")
                .encryptedKeyStudent("bbbbbbbbbbbbbbbb")
                .encryptedKeySupervisor("cccccccccccccccccc")
                .encryptedTranscriptData("")
                .transcriptStatus(TranscriptStatus.PENDING)
                .build();

        assertThrows(
                IllegalArgumentException.class, () -> {
                    transcriptService.createTranscript(transcript, supervisor_182_1_id, department182Id);
                });
    }

    @Test
    @DisplayName("Should fail to create transcript if the digital signature or status is set")
    void shouldFailToCreateTranscriptIfDigitalSignatureOrStatusIsSet() {
        // GIVEN
        var presignedTranscript = Transcript.builder()
                .hodId(hod182Id)
                .studentId(student_182_1_id)
                .encryptedKeyHod("aaaaaaaaaaaaaaaa")
                .encryptedKeyStudent("bbbbbbbbbbbbbbbb")
                .encryptedKeySupervisor("cccccccccccccccccc")
                .encryptedTranscriptData("")
                .hodDigitalSignature("ddddddddddddddddd")
                .transcriptStatus(TranscriptStatus.APPROVED)
                .build();

        var approvedUnsignedTranscript = Transcript.builder()
                .hodId(hod182Id)
                .studentId(student_182_1_id)
                .encryptedKeyHod("aaaaaaaaaaaaaaaa")
                .encryptedKeyStudent("bbbbbbbbbbbbbbbb")
                .encryptedKeySupervisor("cccccccccccccccccc")
                .encryptedTranscriptData("")
                .transcriptStatus(TranscriptStatus.APPROVED)
                .build();

        var pendingPresignedTranscript = Transcript.builder()
                .hodId(hod182Id)
                .studentId(student_182_1_id)
                .encryptedKeyHod("aaaaaaaaaaaaaaaa")
                .encryptedKeyStudent("bbbbbbbbbbbbbbbb")
                .encryptedKeySupervisor("cccccccccccccccccc")
                .encryptedTranscriptData("")
                .hodDigitalSignature("ddddddddddddddddd")
                .transcriptStatus(TranscriptStatus.PENDING)
                .build();

        // WHEN & THEN
        assertThrows(
                IllegalArgumentException.class, () -> {
                    transcriptService.createTranscript(presignedTranscript, supervisor_182_1_id, department182Id);
                });
        assertThrows(
                IllegalArgumentException.class, () -> {
                    transcriptService.createTranscript(approvedUnsignedTranscript, supervisor_182_1_id,
                            department182Id);
                });
        assertThrows(
                IllegalArgumentException.class, () -> {
                    transcriptService.createTranscript(pendingPresignedTranscript, supervisor_182_1_id,
                            department182Id);
                });
    }

    @Test
    @DisplayName("Should update valid transcript")
    void shouldUpdateValidTranscript() {
        var foundTranscript1 = transcriptService.getTranscriptById(transcript1.getTranscriptId());
        assertThat(foundTranscript1).isPresent();

        var transcriptToUpdate = foundTranscript1.get();

        List<TranscriptEntry> newTranscriptData = List.of(
                new TranscriptEntry("IF2211", 3, Score.C),
                new TranscriptEntry("IF2240", 3, Score.B),
                new TranscriptEntry("IF4033", 3, Score.BC));
        transcriptToUpdate.setEncryptedTranscriptData(newTranscriptData.toString());

        transcriptService.updateTranscript(transcriptToUpdate);

        var updatedTranscript = transcriptService.getTranscriptById(transcriptToUpdate.getTranscriptId());

        assertThat(updatedTranscript).isPresent();
        assertThat(updatedTranscript.get().getHodDigitalSignature()).isNull();
        assertThat(updatedTranscript.get().getEncryptedTranscriptData()).isEqualTo(newTranscriptData.toString());
    }

    @Test
    @DisplayName("Should fail to update non-existing transcript")
    void shouldFailToUpdateNonExistingTranscript() {
        var transcript = Transcript.builder()
                .transcriptId(UUID.randomUUID().toString())
                .hodId(hod182Id)
                .studentId(student_182_2_id)
                .encryptedKeyHod("aaaaaaaaaaaaaaaa")
                .encryptedKeyStudent("bbbbbbbbbbbbbbbb")
                .encryptedKeySupervisor("cccccccccccccccccc")
                .encryptedTranscriptData("")
                .transcriptStatus(TranscriptStatus.PENDING)
                .build();

        assertThrows(
                IllegalArgumentException.class, () -> {
                    transcriptService.updateTranscript(transcript);
                });
    }

    @Test
    @DisplayName("Should fail to update approval/digital signature property")
    void shouldFailToUpdateApprovalOrDigitalSignatureProperty() {
        var foundTranscript1 = transcriptService.getTranscriptById(transcript1.getTranscriptId());
        assertThat(foundTranscript1).isPresent();

        var transcriptToUpdate1 = foundTranscript1.get();
        transcriptToUpdate1.setHodDigitalSignature("ffffffffffffffff");

        assertThrows(
                IllegalArgumentException.class, () -> {
                    transcriptService.updateTranscript(transcriptToUpdate1);
                });

        var foundTranscript2 = transcriptService.getTranscriptById(transcript2.getTranscriptId());
        assertThat(foundTranscript2).isPresent();
    }

    @Test
    @DisplayName("Should sign transcript with pending status")
    void shouldSignTranscriptWithPendingStatus() {
        var digitalSignature = "digital signature";
        transcriptService.signTranscript(transcript1.getTranscriptId(), digitalSignature);

        var foundTranscript1 = transcriptService.getTranscriptById(transcript1.getTranscriptId());
        assertThat(foundTranscript1).isPresent();
        assertThat(foundTranscript1.get().getHodDigitalSignature()).isNotNull();
        assertThat(foundTranscript1.get().getHodDigitalSignature()).isEqualTo(digitalSignature);
    }

    @Test
    @DisplayName("Should fail to sign transcript with approved status")
    void shouldFailToSignTranscriptWithApprovedStatus() {
        var digitalSignature = "digital signature";
        assertThrows(
                IllegalArgumentException.class, () -> {
                    transcriptService.signTranscript(
                            transcript2.getTranscriptId(),
                            digitalSignature);
                });
    }

    @Test
    @DisplayName("Should get existing transcript by student ID")
    void shouldGetExistingTranscriptByStudentId() {
        var foundTranscriptId = transcriptService.getStudentTranscriptId(student_135_1_id);
        assertThat(foundTranscriptId).isPresent();
        assertThat(foundTranscriptId.get()).isEqualTo(transcript1.getTranscriptId());
    }

    @Test
    @DisplayName("Should get empty when getting non-existing transcript by student ID")
    void shouldGetEmptyWhenGettingNonExistingTranscriptByStudentId() {
        var nonExistingTranscriptId = transcriptService.getStudentTranscriptId(student_182_2_id);
        assertThat(nonExistingTranscriptId).isEmpty();
    }

    @Test
    @DisplayName("Should create transcript access inquiry")
    void shouldCreateTranscriptAccessInquiry() {
        var transcriptAccessInquiryToCreate = transcriptService.createTranscriptAccessInquiry(
                transcript1.getTranscriptId(),
                supervisor_135_2_id,
                department135Id);

        var inquiries = transcriptService.getTranscriptAccessInquiries();
        assertThat(inquiries.size()).isEqualTo(1);

        var createdTranscriptAccessInquiry = inquiries.getFirst();
        assertThat(createdTranscriptAccessInquiry.getInquiryId())
                .isEqualTo(transcriptAccessInquiryToCreate.getInquiryId());
        assertThat(createdTranscriptAccessInquiry.getInquiryStatus())
                .isEqualTo(TranscriptAccessInquiryStatus.WAITING_FOR_PARTICIPANTS);
        assertThat(createdTranscriptAccessInquiry.getRequesterId()).isEqualTo(supervisor_135_2_id);
        assertThat(createdTranscriptAccessInquiry.getParticipants().getFirst().getId())
                .isEqualTo(supervisor_135_2_id);
    }

    @Test
    @DisplayName("Should join transcript access inquiry if status is WAITING_FOR_PARTICIPANTS")
    void shouldJoinTranscriptAccessInquiryWhenWaitingForParticipants() {
        // GIVEN
        var createdInquiry = transcriptService.createTranscriptAccessInquiry(
                transcript1.getTranscriptId(),
                supervisor_135_2_id,
                department135Id);

        // WHEN
        transcriptService.joinTranscriptAccessInquiry(
                createdInquiry.getInquiryId(),
                supervisor_135_3_id,
                department135Id);

        // THEN
        var inquiries = transcriptService.getTranscriptAccessInquiries();
        assertThat(inquiries.size()).isEqualTo(1);
        var updatedInquiry = inquiries.getFirst();
        assertThat(updatedInquiry.getInquiryStatus())
                .isEqualTo(TranscriptAccessInquiryStatus.WAITING_FOR_REQUESTEE);
        assertThat(updatedInquiry.getParticipants()
                .stream()
                .anyMatch(p -> p.getId().equals(supervisor_135_3_id))).isTrue();
    }

    @Test
    @DisplayName("Should join transcript access inquiry if status is WAITING_FOR_REQUESTEE")
    void shouldJoinTranscriptAccessInquiryWhenWaitingForRequestee() {
        // GIVEN
        var createdInquiry = transcriptService.createTranscriptAccessInquiry(
                transcript1.getTranscriptId(),
                supervisor_135_2_id,
                department135Id);
        transcriptService.joinTranscriptAccessInquiry(
                createdInquiry.getInquiryId(),
                supervisor_135_3_id,
                department135Id);

        // WHEN
        transcriptService.joinTranscriptAccessInquiry(
                createdInquiry.getInquiryId(),
                supervisor_135_4_id,
                department135Id);

        // THEN
        var inquiries = transcriptService.getTranscriptAccessInquiries();
        assertThat(inquiries.size()).isEqualTo(1);
        var updatedInquiry = inquiries.getFirst();
        assertThat(updatedInquiry.getInquiryStatus())
                .isEqualTo(TranscriptAccessInquiryStatus.WAITING_FOR_REQUESTEE);
        assertThat(updatedInquiry.getParticipants()
                .stream()
                .anyMatch(p -> p.getId().equals(supervisor_135_4_id))).isTrue();
    }

    @Test
    @DisplayName("Should fail to join transcript access inquiry if participant has already joined")
    void shouldFailToJoinTranscriptAccessInquiryIfParticipantAlreadyJoined() {
        // GIVEN
        var createdInquiry = transcriptService.createTranscriptAccessInquiry(
                transcript1.getTranscriptId(),
                supervisor_135_2_id,
                department135Id);
        transcriptService.joinTranscriptAccessInquiry(
                createdInquiry.getInquiryId(),
                supervisor_135_3_id,
                department135Id);

        // WHEN & THEN
        assertThrows(
                IllegalArgumentException.class, () -> {
                    transcriptService.joinTranscriptAccessInquiry(
                            createdInquiry.getInquiryId(),
                            supervisor_135_3_id, // Trying to join again
                            department135Id);
                });
    }

    @Test
    @DisplayName("Should fail to join transcript access inquiry if participant is the requestee")
    void shouldFailToJoinTranscriptAccessInquiryIfParticipantIsRequestee() {
        // GIVEN
        var createdInquiry = transcriptService.createTranscriptAccessInquiry(
                transcript1.getTranscriptId(),
                supervisor_135_2_id,
                department135Id);

        // WHEN & THEN
        assertThrows(
                IllegalArgumentException.class, () -> {
                    transcriptService.joinTranscriptAccessInquiry(
                            createdInquiry.getInquiryId(),
                            supervisor_135_1_id, // Requestee trying to join
                            department135Id);
                });
    }

    @Test
    @DisplayName("Should fail to join transcript access inquiry if participant is from different department")
    void shouldFailToJoinTranscriptAccessInquiryIfParticipantFromDifferentDepartment() {
        // GIVEN
        var createdInquiry = transcriptService.createTranscriptAccessInquiry(
                transcript1.getTranscriptId(),
                supervisor_135_2_id,
                department135Id);

        // WHEN & THEN
        assertThrows(
                IllegalArgumentException.class, () -> {
                    transcriptService.joinTranscriptAccessInquiry(
                            createdInquiry.getInquiryId(),
                            supervisor_182_1_id, // From different department
                            department182Id);
                });
    }

    @Test
    @DisplayName("Should approve transcript access inquiry if status is WAITING_FOR_REQUESTEE")
    void shouldApproveTranscriptAccessInquiryWhenWaitingForRequestee() {
        // GIVEN
        var createdInquiry = transcriptService.createTranscriptAccessInquiry(
                transcript1.getTranscriptId(),
                supervisor_135_2_id,
                department135Id);
        transcriptService.joinTranscriptAccessInquiry(
                createdInquiry.getInquiryId(),
                supervisor_135_3_id,
                department135Id);
        transcriptService.joinTranscriptAccessInquiry(
                createdInquiry.getInquiryId(),
                supervisor_135_4_id,
                department135Id);

        Map<String, String> encryptedShares = Map.of(
                supervisor_135_2_id, "encrypted share 1",
                supervisor_135_3_id, "encrypted share 2",
                supervisor_135_4_id, "encrypted share 3");

        // WHEN
        transcriptService.approveTranscriptAccessInquiry(
                createdInquiry.getInquiryId(),
                encryptedShares,
                createdInquiry.getRequesteeId());

        // THEN
        var inquiries = transcriptService.getTranscriptAccessInquiries();
        assertThat(inquiries.size()).isEqualTo(1);

        var approvedInquiry = inquiries.getFirst();
        assertThat(approvedInquiry.getInquiryStatus()).isEqualTo(TranscriptAccessInquiryStatus.APPROVED);

        var participants = approvedInquiry.getParticipants();
        var approvedInquiryEncryptedShares = participants.stream()
                .map(TranscriptAccessInquiryParticipant::getEncryptedShare).toList();
        var generatedInquiryEncryptedShares = participants.stream()
                .map(p -> encryptedShares.get(p.getId())).toList();
        assertThat(approvedInquiryEncryptedShares).isEqualTo(generatedInquiryEncryptedShares);
    }

    @Test
    @DisplayName("Should reject transcript access inquiry if status is WAITING_FOR_REQUESTEE")
    void shouldRejectTranscriptAccessInquiryWhenWaitingForRequestee() {
        // GIVEN
        var createdInquiry = transcriptService.createTranscriptAccessInquiry(
                transcript1.getTranscriptId(),
                supervisor_135_2_id,
                department135Id);
        transcriptService.joinTranscriptAccessInquiry(
                createdInquiry.getInquiryId(),
                supervisor_135_3_id,
                department135Id);

        // WHEN
        transcriptService.rejectTranscriptAccessInquiry(createdInquiry.getInquiryId(),
                createdInquiry.getRequesteeId());

        // THEN
        var inquiries = transcriptService.getTranscriptAccessInquiries();
        assertThat(inquiries.size()).isEqualTo(1);
        var rejectedInquiry = inquiries.getFirst();
        assertThat(rejectedInquiry.getInquiryStatus()).isEqualTo(TranscriptAccessInquiryStatus.CLOSED);
    }

    @Test
    @DisplayName("Should fail to join transcript access inquiry if status is APPROVED")
    void shouldFailToJoinTranscriptAccessInquiryIfStatusIsApproved() {
        // GIVEN
        var createdInquiry = transcriptService.createTranscriptAccessInquiry(
                transcript1.getTranscriptId(),
                supervisor_135_2_id,
                department135Id);
        transcriptService.joinTranscriptAccessInquiry(
                createdInquiry.getInquiryId(),
                supervisor_135_3_id,
                department135Id);
        transcriptService.approveTranscriptAccessInquiry(
                createdInquiry.getInquiryId(),
                Map.of(
                        supervisor_135_2_id, "share1",
                        supervisor_135_3_id, "share2"),
                createdInquiry.getRequesteeId());

        // WHEN & THEN
        assertThrows(
                IllegalArgumentException.class, () -> {
                    transcriptService.joinTranscriptAccessInquiry(
                            createdInquiry.getInquiryId(),
                            supervisor_135_4_id,
                            department135Id);
                });
    }

    @Test
    @DisplayName("Should fail to join transcript access inquiry if status is CLOSED")
    void shouldFailToJoinTranscriptAccessInquiryIfStatusIsClosed() {
        // GIVEN
        var createdInquiry = transcriptService.createTranscriptAccessInquiry(
                transcript1.getTranscriptId(),
                supervisor_135_2_id,
                department135Id);
        transcriptService.rejectTranscriptAccessInquiry(createdInquiry.getInquiryId(),
                createdInquiry.getRequesteeId());

        // WHEN & THEN
        assertThrows(
                IllegalArgumentException.class, () -> {
                    transcriptService.joinTranscriptAccessInquiry(
                            createdInquiry.getInquiryId(),
                            supervisor_135_3_id,
                            department135Id);
                });
    }

    @Test
    @DisplayName("Should fail to approve transcript access inquiry if status is not WAITING_FOR_REQUESTEE")
    void shouldFailToApproveTranscriptAccessInquiryIfStatusIsNotWaitingForRequestee() {
        // GIVEN: Inquiry in WAITING_FOR_PARTICIPANTS
        var inquiryWaitingForParticipants = transcriptService.createTranscriptAccessInquiry(
                transcript1.getTranscriptId(),
                supervisor_135_2_id,
                department135Id);

        // WHEN & THEN
        assertThrows(
                IllegalArgumentException.class, () -> {
                    transcriptService.approveTranscriptAccessInquiry(
                            inquiryWaitingForParticipants.getInquiryId(),
                            Map.of(supervisor_135_2_id, "share1"),
                            inquiryWaitingForParticipants.getRequesteeId());
                });

        // GIVEN: Inquiry in APPROVED status
        var inquiryApproved = transcriptService.createTranscriptAccessInquiry(
                transcript1.getTranscriptId(),
                supervisor_135_2_id,
                department135Id);
        transcriptService.joinTranscriptAccessInquiry(
                inquiryApproved.getInquiryId(),
                supervisor_135_3_id,
                department135Id);
        transcriptService.approveTranscriptAccessInquiry(
                inquiryApproved.getInquiryId(),
                Map.of(
                        supervisor_135_2_id, "share1",
                        supervisor_135_3_id, "share2"),
                inquiryApproved.getRequesteeId());

        // WHEN & THEN
        assertThrows(
                IllegalArgumentException.class, () -> {
                    transcriptService.approveTranscriptAccessInquiry(
                            inquiryApproved.getInquiryId(),
                            Map.of(
                                    supervisor_135_2_id, "share3",
                                    supervisor_135_3_id, "share4"),
                            inquiryApproved.getRequesteeId());
                });

        // GIVEN: Inquiry in CLOSED status
        var inquiryClosed = transcriptService.createTranscriptAccessInquiry(
                transcript1.getTranscriptId(),
                supervisor_135_2_id,
                department135Id);
        transcriptService.rejectTranscriptAccessInquiry(inquiryClosed.getInquiryId(),
                inquiryClosed.getRequesteeId());

        // WHEN & THEN
        assertThrows(
                IllegalArgumentException.class, () -> {
                    transcriptService.approveTranscriptAccessInquiry(
                            inquiryClosed.getInquiryId(),
                            Map.of(
                                    supervisor_135_2_id, "share5",
                                    supervisor_135_3_id, "share6"),
                            inquiryClosed.getRequesteeId());
                });
    }

    private void setUpTranscripts() {
        transcript1 = Transcript.builder()
                .hodId(hod135Id)
                .studentId(student_135_1_id)
                .encryptedKeyHod("lasdfklasdkf")
                .encryptedKeyStudent("lkdfjdslkfjsldf")
                .encryptedKeySupervisor("jdflkjfdlakfjsd")
                .encryptedTranscriptData("")
                .transcriptStatus(TranscriptStatus.PENDING)
                .build();

        transcript2 = Transcript.builder()
                .hodId(hod182Id)
                .studentId(student_182_1_id)
                .encryptedKeyHod("lasdfklasdkf")
                .encryptedKeyStudent("lkdfjdslkfjsldf")
                .encryptedKeySupervisor("jdflkjfdlakfjsd")
                .encryptedTranscriptData("")
                .transcriptStatus(TranscriptStatus.APPROVED)
                .build();

        transcript1 = transcriptRepository.save(transcript1);
        transcript2 = transcriptRepository.save(transcript2);
    }

    private void setUpUsers() {
        hod135Id = UUID.randomUUID().toString();
        User hod135 = User.builder()
                .userId(hod135Id)
                .fullName("HOD 135")
                .departmentId(department135Id)
                .role(Role.HOD)
                .email("hod135@email.com")
                .password("passhod135")
                .publicKey("pkhod135")
                .build();

        hod182Id = UUID.randomUUID().toString();
        User hod182 = User.builder()
                .userId(hod182Id)
                .fullName("HOD 182")
                .departmentId(department182Id)
                .role(Role.HOD)
                .email("hod182@email.com")
                .password("passhod182")
                .publicKey("pkhod182")
                .build();

        supervisor_135_1_id = UUID.randomUUID().toString();
        User supervisor135_1 = User.builder()
                .userId(supervisor_135_1_id)
                .fullName("Supervisor 135_1")
                .departmentId(department135Id)
                .role(Role.SUPERVISOR)
                .email("supervisor135_1@email.com")
                .password("passsup135_1")
                .publicKey("pksup135_1")
                .build();

        supervisor_135_2_id = UUID.randomUUID().toString();
        User supervisor135_2 = User.builder()
                .userId(supervisor_135_2_id)
                .fullName("Supervisor 135_2")
                .departmentId(department135Id)
                .role(Role.SUPERVISOR)
                .email("supervisor135_2@email.com")
                .password("passsup135_2")
                .publicKey("pksup135_2")
                .build();

        supervisor_135_3_id = UUID.randomUUID().toString();
        User supervisor135_3 = User.builder()
                .userId(supervisor_135_3_id)
                .fullName("Supervisor 135_3")
                .departmentId(department135Id)
                .role(Role.SUPERVISOR)
                .email("supervisor135_3@email.com")
                .password("passsup135_3")
                .publicKey("pksup135_3")
                .build();

        supervisor_135_4_id = UUID.randomUUID().toString();
        User supervisor135_4 = User.builder()
                .userId(supervisor_135_4_id)
                .fullName("Supervisor 135_4")
                .departmentId(department135Id)
                .role(Role.SUPERVISOR)
                .email("supervisor135_4@email.com")
                .password("passsup135_4")
                .publicKey("pksup135_4")
                .build();

        supervisor_135_5_id = UUID.randomUUID().toString();
        User supervisor135_5 = User.builder()
                .userId(supervisor_135_5_id)
                .fullName("Supervisor 135_5")
                .departmentId(department135Id)
                .role(Role.SUPERVISOR)
                .email("supervisor135_5@email.com")
                .password("passsup135_5")
                .publicKey("pksup135_5")
                .build();

        supervisor_182_1_id = UUID.randomUUID().toString();
        User supervisor182_1 = User.builder()
                .userId(supervisor_182_1_id)
                .fullName("Supervisor 182_1")
                .departmentId(department182Id)
                .role(Role.SUPERVISOR)
                .email("supervisor182_1@email.com")
                .password("passsup182_1")
                .publicKey("pksup182_1")
                .build();

        student_135_1_id = UUID.randomUUID().toString();
        User student135_1 = User.builder()
                .userId(student_135_1_id)
                .fullName("Student 135_1")
                .departmentId(department135Id)
                .role(Role.STUDENT)
                .email("student135_1@email.com")
                .password("passstd135_1")
                .publicKey("pkstd135_1")
                .supervisorId(supervisor_135_1_id)
                .build();

        student_182_1_id = UUID.randomUUID().toString();
        User student182_1 = User.builder()
                .userId(student_182_1_id)
                .fullName("Student 182_1")
                .departmentId(department182Id)
                .role(Role.STUDENT)
                .email("student182_1@email.com")
                .password("passstd182_1")
                .publicKey("pkstd182_1")
                .supervisorId(supervisor_182_1_id)
                .build();

        student_182_2_id = UUID.randomUUID().toString();
        User student182_2 = User.builder()
                .userId(student_182_2_id)
                .fullName("Student 182_2")
                .departmentId(department182Id)
                .role(Role.STUDENT)
                .email("student182_2@email.com")
                .password("passstd182_2")
                .publicKey("pkstd182_2")
                .supervisorId(supervisor_182_1_id)
                .build();

        userRepository.saveAll(List.of(
                hod135,
                hod182,
                supervisor135_1,
                supervisor135_2,
                supervisor135_3,
                supervisor135_4,
                supervisor135_5,
                supervisor182_1,
                student135_1,
                student182_1,
                student182_2));
    }
}