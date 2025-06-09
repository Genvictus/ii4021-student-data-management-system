package com.std_data_mgmt.unit.utils;

import com.std_data_mgmt.app.entities.TranscriptAccessInquiryParticipant;
import com.std_data_mgmt.app.utils.TranscriptAccessInquiryParticipantConverter;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TranscriptAccessInquiryParticipantConverterTest {

    private final TranscriptAccessInquiryParticipantConverter converter = new TranscriptAccessInquiryParticipantConverter();

    @Test
    void testConvertToDatabaseColumn() {
        List<TranscriptAccessInquiryParticipant> participants = List.of(
                TranscriptAccessInquiryParticipant.builder()
                        .id("111222333444")
                        .publicKey("public_key_1")
                        .encryptedShare("share_1")
                        .build(),
                TranscriptAccessInquiryParticipant.builder()
                        .id("555666777888")
                        .publicKey("public_key_2")
                        .encryptedShare("share_2")
                        .build());

        String json = converter.convertToDatabaseColumn(participants);

        assertNotNull(json);

        List<TranscriptAccessInquiryParticipant> deserialized = converter.convertToEntityAttribute(json);
        assertEquals(participants.size(), deserialized.size());

        for (int i = 0; i < participants.size(); i++) {
            TranscriptAccessInquiryParticipant original = participants.get(i);
            TranscriptAccessInquiryParticipant parsed = deserialized.get(i);

            assertEquals(original.getId(), parsed.getId());
            assertEquals(original.getEncryptedShare(), parsed.getEncryptedShare());
        }
    }

    @Test
    void testConvertToDatabaseColumnWithNullEncryptedShare() {
        List<TranscriptAccessInquiryParticipant> participants = List.of(
                TranscriptAccessInquiryParticipant.builder()
                        .id("111222333444")
                        .encryptedShare(null)
                        .build(),
                TranscriptAccessInquiryParticipant.builder()
                        .id("555666777888")
                        .encryptedShare(null)
                        .build());

        String json = converter.convertToDatabaseColumn(participants);

        assertNotNull(json);

        List<TranscriptAccessInquiryParticipant> deserialized = converter.convertToEntityAttribute(json);
        assertEquals(participants.size(), deserialized.size());

        for (int i = 0; i < participants.size(); i++) {
            TranscriptAccessInquiryParticipant original = participants.get(i);
            TranscriptAccessInquiryParticipant parsed = deserialized.get(i);

            assertEquals(original.getId(), parsed.getId());
            assertNull(parsed.getEncryptedShare());
        }
    }

    @Test
    void testConvertToEntityAttribute() {
        String json = """
                [
                    {"id":"111222333444","encryptedShare":"share_1"},
                    {"id":"555666777888","encryptedShare":"share_2"}
                ]
                """;

        List<TranscriptAccessInquiryParticipant> participants = converter.convertToEntityAttribute(json);

        assertNotNull(participants);
        assertEquals(2, participants.size());

        TranscriptAccessInquiryParticipant first = participants.getFirst();
        assertEquals("111222333444", first.getId());
        assertEquals("share_1", first.getEncryptedShare());

        TranscriptAccessInquiryParticipant second = participants.get(1);
        assertEquals("555666777888", second.getId());
        assertEquals("share_2", second.getEncryptedShare());
    }

    @Test
    void testConvertEmptyOrNull() {
        assertNull(converter.convertToDatabaseColumn(null));

        List<TranscriptAccessInquiryParticipant> emptyList = converter.convertToEntityAttribute(null);
        assertNotNull(emptyList);
        assertTrue(emptyList.isEmpty());

        emptyList = converter.convertToEntityAttribute("");
        assertNotNull(emptyList);
        assertTrue(emptyList.isEmpty());
    }
}
