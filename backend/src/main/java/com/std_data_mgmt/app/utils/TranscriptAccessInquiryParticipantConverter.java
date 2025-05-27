package com.std_data_mgmt.app.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.std_data_mgmt.app.entities.TranscriptAccessInquiryParticipant;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// TODO: decide if we can generalize this with the TranscriptDataConverter

@Converter
public class TranscriptAccessInquiryParticipantConverter implements AttributeConverter<List<TranscriptAccessInquiryParticipant>, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<TranscriptAccessInquiryParticipant> transcriptEntries) {
        if (transcriptEntries == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(transcriptEntries);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting transcript entries to JSON", e);
        }
    }

    @Override
    public List<TranscriptAccessInquiryParticipant> convertToEntityAttribute(String json) {
        if (json == null || json.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(
                    json,
                    objectMapper.getTypeFactory()
                            .constructCollectionType(List.class, TranscriptAccessInquiryParticipant.class)
            );
        } catch (IOException e) {
            throw new IllegalArgumentException("Error reading transcript entries from JSON", e);
        }
    }
}

