import type { TranscriptEntry } from "@/types/TranscriptWithStudent";

const ScoreConversion = {
    A: 4,
    AB: 3.5,
    B: 3,
    BC: 2.5,
    C: 2,
    D: 1,
    E: 0,
};

type CalculateGpaResult = {
    gpa: number;
    totalCredits: number;
};

export function calculateGpa(
    transcriptEntries: TranscriptEntry[]
): CalculateGpaResult {
    let totalCredits = 0;
    let totalConvertedScore = 0;

    for (let entry of transcriptEntries) {
        const convertedScore = ScoreConversion[entry.score] * entry.credits;
        totalCredits += entry.credits;
        totalConvertedScore += convertedScore;
    }

    if (totalCredits === 0) {
        return { gpa: 0, totalCredits: 0 };
    }

    const gpa = totalConvertedScore / totalCredits;
    return {
        gpa: parseFloat(gpa.toFixed(2)),
        totalCredits,
    };
}
