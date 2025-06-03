package com.std_data_mgmt.app.services;

import com.std_data_mgmt.app.entities.Transcript;
import com.std_data_mgmt.app.entities.TranscriptAccessInquiry;
import com.std_data_mgmt.app.enums.TranscriptAccessInquiryStatus;
import com.std_data_mgmt.app.repositories.TranscriptAccessInquiryRepository;
import com.std_data_mgmt.app.repositories.TranscriptRepository;
import com.std_data_mgmt.app.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class TranscriptService {
    private final TranscriptRepository transcriptRepository;
    private final TranscriptAccessInquiryRepository transcriptAccessInquiryRepository;

    public TranscriptService(
            UserRepository userRepository, TranscriptRepository transcriptRepository,
            TranscriptAccessInquiryRepository transcriptAccessInquiryRepository
    ) {
        this.transcriptRepository = transcriptRepository;
        this.transcriptAccessInquiryRepository = transcriptAccessInquiryRepository;
    }

    public void updateTranscript(Transcript transcript) {
        this.transcriptRepository.save(transcript);
    }

    public void initiateTranscriptAccessInquiry(String requesterId, String transcriptId) {
        TranscriptAccessInquiry accessInquiry = new TranscriptAccessInquiry(
                requesterId,
                TranscriptAccessInquiryStatus.WAITING_FOR_PARTICIPANTS,
                Collections.emptyList(),
                transcriptId
        );
        this.transcriptAccessInquiryRepository.save(accessInquiry);

    }
}
