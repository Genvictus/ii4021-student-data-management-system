package com.std_data_mgmt.app.services;

import com.std_data_mgmt.app.entities.Transcript;
import com.std_data_mgmt.app.entities.TranscriptAccessInquiry;
import com.std_data_mgmt.app.enums.TranscriptAccessInquiryStatus;
import com.std_data_mgmt.app.repositories.TranscriptAccessInquiryRepository;
import com.std_data_mgmt.app.repositories.TranscriptRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TranscriptService {
    private final TranscriptRepository transcriptRepository;
    private final TranscriptAccessInquiryRepository transcriptAccessInquiryRepository;

    public TranscriptService(
            TranscriptRepository transcriptRepository,
            TranscriptAccessInquiryRepository transcriptAccessInquiryRepository
    ) {
        this.transcriptRepository = transcriptRepository;
        this.transcriptAccessInquiryRepository = transcriptAccessInquiryRepository;
    }

    public Optional<Transcript> getTranscriptById(String transcriptId) {
//        TODO: @Genvictus
        return Optional.empty();
    }

    public Transcript createTranscript(Transcript transcript) {
//        TODO: @Genvictus
        return transcript;
    }

    public void updateTranscript(Transcript transcript) {
//        TODO: @Genvictus: Ensure that the sign property cannot be updated
        this.transcriptRepository.save(transcript);
    }

    public void signTranscript(String transcriptId, String signature) {
//        TODO: @Genvictus
    }

    public Optional<String> getStudentTranscriptId(String studentId) {
//        TODO: @Genvictus
        return Optional.empty();
    }

    public TranscriptAccessInquiry createTranscriptAccessInquiry(String requesterId, String transcriptId) {
        TranscriptAccessInquiry accessInquiry = new TranscriptAccessInquiry(
                requesterId,
                TranscriptAccessInquiryStatus.WAITING_FOR_PARTICIPANTS,
                Collections.emptyList(),
                transcriptId
        );
        return this.transcriptAccessInquiryRepository.save(accessInquiry);
    }

    public List<TranscriptAccessInquiry> getTranscriptAccessInquiries() {
//        TODO: @Genvictus: add some filters if necessary
        return List.of();
    }

    public void joinTranscriptAccessInquiry() {
//        TODO: @Genvictus
    }

    public void approveTranscriptAccessInquiry() {
//        TODO: @Genvictus
    }

    public void rejectTranscriptAccessInquiry() {
//        TODO: @Genvictus
    }
}
