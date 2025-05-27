package com.std_data_mgmt.app.utils;


import com.std_data_mgmt.app.entities.TranscriptAccessInquiryParticipant;
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
                        .public_key("public_key_1")
                        .encrypted_share("share_1")
                        .build(),
                TranscriptAccessInquiryParticipant.builder()
                        .id("555666777888")
                        .public_key("public_key_2")
                        .encrypted_share("share_2")
                        .build()
        );

        String json = converter.convertToDatabaseColumn(participants);

        assertNotNull(json);

        List<TranscriptAccessInquiryParticipant> deserialized = converter.convertToEntityAttribute(json);
        assertEquals(participants.size(), deserialized.size());

        for (int i = 0; i < participants.size(); i++) {
            TranscriptAccessInquiryParticipant original = participants.get(i);
            TranscriptAccessInquiryParticipant parsed = deserialized.get(i);

            assertEquals(original.getId(), parsed.getId());
            assertEquals(original.getPublic_key(), parsed.getPublic_key());
            assertEquals(original.getEncrypted_share(), parsed.getEncrypted_share());
        }
    }

    @Test
    void testConvertToDatabaseColumnWithNullEncryptedShare() {
        List<TranscriptAccessInquiryParticipant> participants = List.of(
                TranscriptAccessInquiryParticipant.builder()
                        .id("111222333444")
                        .public_key("public_key_1")
                        .encrypted_share(null)
                        .build(),
                TranscriptAccessInquiryParticipant.builder()
                        .id("555666777888")
                        .public_key("public_key_2")
                        .encrypted_share(null)
                        .build()
        );

        String json = converter.convertToDatabaseColumn(participants);

        assertNotNull(json);

        List<TranscriptAccessInquiryParticipant> deserialized = converter.convertToEntityAttribute(json);
        assertEquals(participants.size(), deserialized.size());

        for (int i = 0; i < participants.size(); i++) {
            TranscriptAccessInquiryParticipant original = participants.get(i);
            TranscriptAccessInquiryParticipant parsed = deserialized.get(i);

            assertEquals(original.getId(), parsed.getId());
            assertEquals(original.getPublic_key(), parsed.getPublic_key());
            assertNull(parsed.getEncrypted_share());
        }
    }


    @Test
    void testConvertToEntityAttribute() {
        String json = """
                [
                    {"id":"111222333444","public_key":"public_key_1","encrypted_share":"share_1"},
                    {"id":"555666777888","public_key":"public_key_2","encrypted_share":"share_2"}
                ]
                """;

        List<TranscriptAccessInquiryParticipant> participants = converter.convertToEntityAttribute(json);

        assertNotNull(participants);
        assertEquals(2, participants.size());

        TranscriptAccessInquiryParticipant first = participants.getFirst();
        assertEquals("111222333444", first.getId());
        assertEquals("public_key_1", first.getPublic_key());
        assertEquals("share_1", first.getEncrypted_share());

        TranscriptAccessInquiryParticipant second = participants.get(1);
        assertEquals("555666777888", second.getId());
        assertEquals("public_key_2", second.getPublic_key());
        assertEquals("share_2", second.getEncrypted_share());
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
