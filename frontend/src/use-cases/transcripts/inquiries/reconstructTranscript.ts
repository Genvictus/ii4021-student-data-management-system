import type { TranscriptAccessInquiry } from "@/types/TranscriptAccessInquiry";
import type { TranscriptEntry } from "@/types/TranscriptWithStudent";

export function reconstructTranscript(
    inquiry: TranscriptAccessInquiry
): TranscriptEntry[] {
    // TODO: @Genvictus

    return [
        {
            courseCode: "CS101",
            credits: 3,
            score: "A",
        },
        {
            courseCode: "MA201",
            credits: 4,
            score: "B",
        },
    ];
}
