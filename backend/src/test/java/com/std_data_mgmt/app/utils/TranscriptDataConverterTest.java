package com.std_data_mgmt.app.utils;

import com.std_data_mgmt.app.entities.TranscriptEntry;
import com.std_data_mgmt.app.enums.Score;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TranscriptDataConverterTest {

    private final TranscriptDataConverter converter = new TranscriptDataConverter();

    @Test
    void testConvertToDatabaseColumn() {
        List<TranscriptEntry> entries = List.of(
                TranscriptEntry.builder()
                        .courseCode("CS101")
                        .credits(3)
                        .score(Score.A)
                        .build(),
                TranscriptEntry.builder()
                        .courseCode("MATH201")
                        .credits(4)
                        .score(Score.B)
                        .build()
        );

        String json = converter.convertToDatabaseColumn(entries);

        assertNotNull(json);

        List<TranscriptEntry> deserialized = converter.convertToEntityAttribute(json);
        assertEquals(entries.size(), deserialized.size());

        for (int i = 0; i < entries.size(); i++) {
            TranscriptEntry original = entries.get(i);
            TranscriptEntry parsed = deserialized.get(i);

            assertEquals(original.getCourseCode(), parsed.getCourseCode());
            assertEquals(original.getCredits(), parsed.getCredits());
            assertEquals(original.getScore(), parsed.getScore());
        }
    }


    @Test
    void testConvertToEntityAttribute() {
        String json = """
                [
                    {"courseCode":"CS101","credits":3,"score":"A"},
                    {"courseCode":"MATH201","credits":4,"score":"B"}
                ]
                """;

        List<TranscriptEntry> entries = converter.convertToEntityAttribute(json);

        assertNotNull(entries);
        assertEquals(2, entries.size());

        TranscriptEntry first = entries.getFirst();
        assertEquals("CS101", first.getCourseCode());
        assertEquals(3, first.getCredits());
        assertEquals(Score.A, first.getScore());

        TranscriptEntry second = entries.get(1);
        assertEquals("MATH201", second.getCourseCode());
        assertEquals(4, second.getCredits());
        assertEquals(Score.B, second.getScore());
    }

    @Test
    void testConvertEmptyOrNull() {
        assertNull(converter.convertToDatabaseColumn(null));

        List<TranscriptEntry> emptyList = converter.convertToEntityAttribute(null);
        assertNotNull(emptyList);
        assertTrue(emptyList.isEmpty());

        emptyList = converter.convertToEntityAttribute("");
        assertNotNull(emptyList);
        assertTrue(emptyList.isEmpty());
    }
}
