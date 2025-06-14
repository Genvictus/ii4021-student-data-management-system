package com.std_data_mgmt.app.services;

import com.std_data_mgmt.app.entities.Transcript;
import com.std_data_mgmt.app.entities.TranscriptAccessInquiry;
import com.std_data_mgmt.app.entities.TranscriptAccessInquiryParticipant;
import com.std_data_mgmt.app.entities.User;
import com.std_data_mgmt.app.enums.Role;
import com.std_data_mgmt.app.enums.TranscriptAccessInquiryStatus;
import com.std_data_mgmt.app.enums.TranscriptStatus;
import com.std_data_mgmt.app.exceptions.ForbiddenException;
import com.std_data_mgmt.app.repositories.TranscriptAccessInquiryRepository;
import com.std_data_mgmt.app.repositories.TranscriptRepository;
import com.std_data_mgmt.app.repositories.UserRepository;
import com.std_data_mgmt.app.security.jwt.JwtAuthenticationFilter;

import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;

@Service
@AllArgsConstructor
@Transactional
public class TranscriptService {
    private static final Logger logger = LoggerFactory.getLogger(TranscriptService.class);
    public static final int MINIMUM_PARTICIPANTS = 2;

    private final UserRepository userRepository;
    private final TranscriptRepository transcriptRepository;
    private final TranscriptAccessInquiryRepository transcriptAccessInquiryRepository;

    public Optional<Transcript> getTranscriptById(String transcriptId) {
        return this.transcriptRepository.findById(transcriptId);
    }

    public Transcript createTranscript(Transcript transcript, String supervisorId, String departmentId) {
        // Check if transcript with student ID already exists
        String studentId = transcript.getStudentId();
        Transcript transcriptProbe = new Transcript();
        transcriptProbe.setStudentId(studentId);
        if (this.transcriptRepository.count(Example.of(transcriptProbe)) != 0) {
            throw new IllegalArgumentException(format("Transcript with student ID %s exists", studentId));
        }

        User hodProbe = new User();
        hodProbe.setDepartmentId(departmentId);
        hodProbe.setRole(Role.HOD);
        User hod = this.userRepository.findOne(Example.of(hodProbe)).get();

        transcript.setTranscriptStatus(TranscriptStatus.PENDING);
        transcript.setHodId(hod.getUserId());
        transcript.setHodDigitalSignature(null);

        // TODO: validate supervisorId is the same with student's supervisorId

        return this.transcriptRepository.save(transcript);
    }

    public Optional<Transcript> findTranscriptByStudentId(String studentId) {
        return this.transcriptRepository.findByStudentId(studentId);
    }

    public List<Transcript> findTranscriptBySupervisor(String supervisorId) {
        return this.transcriptRepository.findBySupervisorId(supervisorId);
    }

    public List<Transcript> findTranscriptByHod(String hodId) {
        return this.transcriptRepository.findByHodId(hodId);
    }

    public void updateTranscript(Transcript transcript) {
        this.logger.info(transcript.toString());
        // Ensure that the sign property cannot be updated, so transcript.signature must
        // be null when the transcript is updated
        if (transcript.getHodDigitalSignature() != null) {
            throw new IllegalArgumentException("Can not update digital signature");
        }

        // Check if transcript with ID already exists to update
        String transcriptId = transcript.getTranscriptId();
        Optional<Transcript> dbTranscript = this.transcriptRepository.findById(transcriptId);
        dbTranscript.ifPresentOrElse(t -> {
            t.setTranscriptStatus(TranscriptStatus.PENDING);
            t.setEncryptedTranscriptData(transcript.getEncryptedTranscriptData());
            t.setHodDigitalSignature(null);

            logger.info(t.getEncryptedTranscriptData());
            this.transcriptRepository.save(t);
        }, () -> {
            throw new IllegalArgumentException(format("Transcript with ID %s does not exist", transcriptId));
        });
    }

    public void signTranscript(String transcriptId, String signature) {
        var transcript = this.transcriptRepository.findById(transcriptId)
                .orElseThrow(() -> new IllegalArgumentException(
                        format("Transcript with id %s does not exist", transcriptId)));

        if (transcript.getTranscriptStatus().equals(TranscriptStatus.APPROVED)) {
            throw new IllegalArgumentException("Can not approve transcript that has been approved");
        }

        transcript.setHodDigitalSignature(signature);
        transcript.setTranscriptStatus(TranscriptStatus.APPROVED);
        this.transcriptRepository.save(transcript);
    }

    public Optional<String> getStudentTranscriptId(String studentId) {
        return this.transcriptRepository.findByStudentId(studentId)
                .map(Transcript::getTranscriptId);
    }

    public TranscriptAccessInquiry createTranscriptAccessInquiry(
            String transcriptId,
            String requesterId,
            String requesterDepartment) {
        var transcript = this.transcriptRepository.findWithStudent(transcriptId)
                .orElseThrow(() -> new IllegalArgumentException(
                        format("Transcript with id %s does not exist", transcriptId)));

        var requesteeId = transcript.getStudent().getSupervisorId();
        var studentDepartment = transcript.getStudent().getDepartmentId();

        if (!requesterDepartment.equals(studentDepartment)) {
            throw new IllegalArgumentException(
                    format(
                            "Transcript access request must be to students of the same department %s %s",
                            requesterDepartment, studentDepartment));
        }

        var newParticipant = new TranscriptAccessInquiryParticipant(requesterId);
        TranscriptAccessInquiry accessInquiry = new TranscriptAccessInquiry(
                requesterId,
                requesteeId,
                TranscriptAccessInquiryStatus.WAITING_FOR_PARTICIPANTS,
                List.of(newParticipant),
                transcriptId);
        return this.transcriptAccessInquiryRepository.save(accessInquiry);
    }

    public List<TranscriptAccessInquiry> getTranscriptAccessInquiries() {
        return this.transcriptAccessInquiryRepository.findAll();
    }

    public Optional<TranscriptAccessInquiry> getTranscriptAccessInquiryById(String inquiryId, boolean populateKey) {
        Optional<TranscriptAccessInquiry> inquiry = this.transcriptAccessInquiryRepository.findById(inquiryId);
        return inquiry.map(i -> {
            List<TranscriptAccessInquiryParticipant> participants = i.getParticipants();
            List<String> userIds = participants.stream().map(TranscriptAccessInquiryParticipant::getId).toList();
            // Populate the public keys of the users
            if (populateKey) {
                List<User> users = this.userRepository.findAllById(userIds);
                users.forEach(u -> {
                    for (TranscriptAccessInquiryParticipant participant : participants) {
                        if (participant.getId().equals(u.getUserId())) {
                            participant.setPublicKey(u.getPublicKey());
                            break;
                        }
                    }
                });
            }
            return i;
        });
    }

    public void joinTranscriptAccessInquiry(String inquiryId, String participantId, String participantDepartmentId) {
        // Check inquiry status if new participants are able to join
        var transcriptAccessInquiry = this.transcriptAccessInquiryRepository.findWithRequestee(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException(
                        format("Transcript Access Inquiry with id %s does not exist", inquiryId)));

        // Check if participant is the requestee (intended supervisor)
        if (participantId.equals(transcriptAccessInquiry.getRequesteeId())) {
            throw new IllegalArgumentException("The requestee may not join as another participant");
        }
        // Check if status permits joining
        var inquiryStatus = transcriptAccessInquiry.getInquiryStatus();
        if (!(inquiryStatus.equals(TranscriptAccessInquiryStatus.WAITING_FOR_PARTICIPANTS)
                || inquiryStatus.equals(TranscriptAccessInquiryStatus.WAITING_FOR_REQUESTEE))) {
            throw new IllegalArgumentException("Inquiry does not accept participants");
        }
        // Check if participant is from the correct department
        var requesteeDepartment = transcriptAccessInquiry.getRequestee().getDepartmentId();
        if (!requesteeDepartment.equals(participantDepartmentId)) {
            throw new IllegalArgumentException("Participant is not from the same department!");
        }

        // Create new participant entity
        var newParticipant = new TranscriptAccessInquiryParticipant(participantId);
        // Check if participant has joined
        var participants = transcriptAccessInquiry.getParticipants();
        if (participants.contains(newParticipant)) {
            throw new IllegalArgumentException(format("User with ID %s already joined", participantId));
        }
        participants.add(newParticipant);

        if (participants.size() == MINIMUM_PARTICIPANTS) {
            transcriptAccessInquiry.setInquiryStatus(TranscriptAccessInquiryStatus.WAITING_FOR_REQUESTEE);
        }
        this.transcriptAccessInquiryRepository.save(transcriptAccessInquiry);
    }

    public void approveTranscriptAccessInquiry(
            String inquiryId,
            Map<String, String> encryptedShares,
            String approverId) {
        var transcriptAccessInquiry = this.transcriptAccessInquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException(
                        format("Transcript access inquiry with id %s does not exist", inquiryId)));

        if (!transcriptAccessInquiry.getRequesteeId().equals(approverId)) {
            throw new ForbiddenException("Only the corresponding requestee may approve the transcript access inquiry");
        }

        if (!transcriptAccessInquiry.getInquiryStatus().equals(TranscriptAccessInquiryStatus.WAITING_FOR_REQUESTEE)) {
            throw new IllegalArgumentException("Transcript inquiry does not meet minimum participant requirement");
        }
        // Set the encrypted shares for each participant
        transcriptAccessInquiry.getParticipants()
                .forEach(p -> p.setEncryptedShare(encryptedShares.get(p.getId())));
        // Set the status to approved
        transcriptAccessInquiry.setInquiryStatus(TranscriptAccessInquiryStatus.APPROVED);
        this.transcriptAccessInquiryRepository.save(transcriptAccessInquiry);
    }

    public void rejectTranscriptAccessInquiry(String inquiryId, String rejecterId) {
        var transcriptAccessInquiry = this.transcriptAccessInquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException(
                        format("Transcript Access Inquiry with id %s does not exist", inquiryId)));

        if (!transcriptAccessInquiry.getRequesteeId().equals(rejecterId)) {
            throw new ForbiddenException("Only the corresponding requestee may reject the transcript access inquiry");
        }
        transcriptAccessInquiry.setInquiryStatus(TranscriptAccessInquiryStatus.CLOSED);
        this.transcriptAccessInquiryRepository.save(transcriptAccessInquiry);
    }
}
