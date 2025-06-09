package com.std_data_mgmt.app.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.std_data_mgmt.app.dtos.TranscriptDto;
import com.std_data_mgmt.app.entities.Transcript;
import com.std_data_mgmt.app.entities.TranscriptAccessInquiry;
import com.std_data_mgmt.app.entities.TranscriptAccessInquiryParticipant;
import com.std_data_mgmt.app.enums.TranscriptAccessInquiryStatus;
import com.std_data_mgmt.app.enums.TranscriptStatus;
import com.std_data_mgmt.app.repositories.TranscriptAccessInquiryRepository;
import com.std_data_mgmt.app.repositories.TranscriptRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TranscriptService {
    public static final int MINIMUM_PARTICIPANTS = 2;

    private final TranscriptRepository transcriptRepository;
    private final TranscriptAccessInquiryRepository transcriptAccessInquiryRepository;

    public Optional<Transcript> getTranscriptById(String transcriptId) {
        return this.transcriptRepository.findById(transcriptId);
    }

    public Transcript createTranscript(Transcript transcript) {
        // Check if transcript with student ID already exists
        String studentId = transcript.getStudentId();
        Transcript probe = new Transcript();
        probe.setStudentId(studentId);
        if (this.transcriptRepository.count(Example.of(probe)) != 0) {
            throw new IllegalArgumentException(String.format("Transcript with student ID %s exists", studentId));
        }
        return this.transcriptRepository.save(transcript);
    }

    public Optional<TranscriptDto> findByStudentId(String studentId) {
        return this.transcriptRepository.findByStudentId(studentId)
                .map(u -> u.toDto(false, false));
    }

    public void updateTranscript(Transcript transcript) {
        // Ensure that the sign property cannot be updated, so transcript.signature must
        // be null when the transcript is updated
        if (transcript.getHodDigitalSignature() != null
                && transcript.getTranscriptStatus() != null) {
            throw new IllegalArgumentException("Can not update digital signature");
        }

        // Check if transcript with ID already exists to update
        String transcriptId = transcript.getTranscriptId();
        Transcript probe = new Transcript();
        probe.setTranscriptId(transcriptId);
        if (this.transcriptRepository.count(Example.of(probe)) == 0) {
            throw new IllegalArgumentException(String.format("Transcript with ID %s does not exist", transcriptId));
        }
        transcript.setHodDigitalSignature(null);
        transcript.setTranscriptStatus(TranscriptStatus.PENDING);
        this.transcriptRepository.save(transcript);
    }

    public void signTranscript(String transcriptId, String signature) {
        var transcript = this.transcriptRepository.findById(transcriptId).get();
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
        var transcript = this.transcriptRepository.findWithStudent(transcriptId).get();
        var requesteeId = transcript.getStudent().getSupervisorId();
        var studentDepartment = transcript.getStudent().getDepartmentId();

        if (!requesterDepartment.equals(studentDepartment)) {
            throw new IllegalArgumentException(
                    String.format("Transcript access request must be to students of the same department %s %s",
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

    public void joinTranscriptAccessInquiry(String inquiryId, String participantId, String participantDepartmentId) {
        // Check inquiry status if new participants are able to join
        var transcriptAccessInquiry = this.transcriptAccessInquiryRepository.findWithRequestee(inquiryId).get();
        var inquiryStatus = transcriptAccessInquiry.getInquiryStatus();
        if (!(inquiryStatus.equals(TranscriptAccessInquiryStatus.WAITING_FOR_PARTICIPANTS)
                || inquiryStatus.equals(TranscriptAccessInquiryStatus.WAITING_FOR_REQUESTEE))) {
            throw new IllegalArgumentException("Inquiry does not accept participants");
        }

        var requesteeDepartment = transcriptAccessInquiry.getRequestee().getDepartmentId();
        if (!requesteeDepartment.equals(participantDepartmentId)) {
            throw new IllegalArgumentException("Participant is not from the same department!");
        }

        // Create new participant entity
        var newParticipant = new TranscriptAccessInquiryParticipant(participantId);

        var participants = transcriptAccessInquiry.getParticipants();
        if (participants.contains(newParticipant)) {
            throw new IllegalArgumentException(String.format("User with ID %s already joined", participantId));
        }
        participants.add(newParticipant);

        if (participants.size() == MINIMUM_PARTICIPANTS) {
            transcriptAccessInquiry.setInquiryStatus(TranscriptAccessInquiryStatus.WAITING_FOR_REQUESTEE);
        }
        this.transcriptAccessInquiryRepository.save(transcriptAccessInquiry);
    }

    public void approveTranscriptAccessInquiry(String inquiryId, Map<String, String> encrpytedShares) {
        var transcriptAccessInquiry = this.transcriptAccessInquiryRepository.findById(inquiryId).get();
        if (!transcriptAccessInquiry.getInquiryStatus().equals(TranscriptAccessInquiryStatus.WAITING_FOR_REQUESTEE)) {
            throw new IllegalArgumentException("Transcript inquiry does not meet minimum participant requirement");
        }
        // Set the encrypted shares for each participant
        transcriptAccessInquiry.getParticipants()
                .forEach(p -> p.setEncryptedShare(encrpytedShares.get(p.getId())));
        // Set the status to approved
        transcriptAccessInquiry.setInquiryStatus(TranscriptAccessInquiryStatus.APPROVED);
        this.transcriptAccessInquiryRepository.save(transcriptAccessInquiry);
    }

    public void rejectTranscriptAccessInquiry(String inquiryId) {
        var transcriptAccessInquiry = this.transcriptAccessInquiryRepository.findById(inquiryId).get();
        transcriptAccessInquiry.setInquiryStatus(TranscriptAccessInquiryStatus.CLOSED);
        this.transcriptAccessInquiryRepository.save(transcriptAccessInquiry);
    }
}
