export type TranscriptUpdatePayload = {
    studentId: string,
    encryptedTranscriptData: string,
    hodDigitalSignature: string,
    encryptedKeyStudent: string,
    encryptedKeySupervisor: string,
    encryptedKeyHod: string,
}