export type TranscriptAccessInquiryStatus =
    | "WAITING_FOR_PARTICIPANTS"
    | "WAITING_FOR_REQUESTEE"
    | "APPROVED"
    | "CLOSED";

export interface TranscriptAccessInquiryParticipant {
    id: string;
    encryptedShare: string;
    publicKey?: string | null;
}

export interface TranscriptAccessInquiry {
    inquiryId: string;
    requesterId: string;
    requesteeId: string;
    inquiryStatus: TranscriptAccessInquiryStatus;
    participants: TranscriptAccessInquiryParticipant[];
    transcriptId: string;
}
