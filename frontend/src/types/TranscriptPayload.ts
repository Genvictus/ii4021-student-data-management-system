import type { ResponseFormat } from "@/use-cases/response";
import type { EncryptedTranscriptWithStudent, TranscriptWithStudent } from "./TranscriptWithStudent";

export type TranscriptCreationPayload = Pick<
    TranscriptWithStudent, "studentId" | "transcriptData"
>;

export type TranscriptUpdatePayload = {
    studentId: string,
    encryptedTranscriptData: string,
    hodDigitalSignature?: string,
    encryptedKeyStudent: string,
    encryptedKeySupervisor: string,
    encryptedKeyHod: string,
}

export type TranscriptSignPayload = Pick<
    TranscriptWithStudent, "transcriptId" | "studentId" | "transcriptData" | "encryptedKey"
>;

export type GetEncryptedStudentTranscriptsResponse = ResponseFormat<
    EncryptedTranscriptWithStudent[]
>;

export type GetEncryptedStudentTranscriptResponse = ResponseFormat<
    EncryptedTranscriptWithStudent
>;

export type SignaturePayload = {
    signature: string
}
