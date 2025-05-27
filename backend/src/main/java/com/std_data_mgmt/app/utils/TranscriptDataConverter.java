package com.std_data_mgmt.app.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.std_data_mgmt.app.entities.TranscriptEntry;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Converter
public class TranscriptDataConverter implements AttributeConverter<List<TranscriptEntry>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<TranscriptEntry> transcriptEntries) {
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
    public List<TranscriptEntry> convertToEntityAttribute(String json) {
        if (json == null || json.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(
                    json,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, TranscriptEntry.class)
            );
        } catch (IOException e) {
            throw new IllegalArgumentException("Error reading transcript entries from JSON", e);
        }
    }
}
