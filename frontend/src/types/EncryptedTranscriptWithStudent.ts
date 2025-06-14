import type { TranscriptWithStudent } from "./TranscriptWithStudent";

export type EncryptedTranscriptWithStudent = Omit<
    TranscriptWithStudent,
    "transcriptData"
> & {
    encryptedTranscriptData: string;
};
