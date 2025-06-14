import type { TranscriptAccessInquiry } from "@/types/TranscriptAccessInquiry";
import type { ResponseFormat } from "@/use-cases/response";

export const dummyData: TranscriptAccessInquiry[] = [
    {
        inquiryId: "inq-001",
        requesterId: "supervisor-123",
        requesteeId: "supervisor-456",
        inquiryStatus: "WAITING_FOR_PARTICIPANTS",
        transcriptId: "transcript-789",
        participants: [
            {
                id: "supervisor-123",
                encryptedShare: "enc-share-1",
            },
            {
                id: "supervisor-888",
                encryptedShare: "enc-share-2",
            },
        ],
    },
    {
        inquiryId: "inq-002",
        requesterId: "supervisor-789",
        requesteeId: "supervisor-999",
        inquiryStatus: "WAITING_FOR_REQUESTEE",
        transcriptId: "transcript-456",
        participants: [
            {
                id: "supervisor-789",
                encryptedShare: "enc-share-3",
            },
            {
                id: "7482749817491278472",
                encryptedShare: "enc-share-4",
            },
        ],
    },
    {
        inquiryId: "inq-003",
        requesterId: "supervisor-321",
        requesteeId: "7482749817491278472",
        inquiryStatus: "APPROVED",
        transcriptId: "transcript-123",
        participants: [
            {
                id: "supervisor-321",
                encryptedShare: "enc-share-5",
            },
            {
                id: "supervisor-111",
                encryptedShare: "enc-share-6",
            },
        ],
    },
    {
        inquiryId: "inq-004",
        requesterId: "7482749817491278472",
        requesteeId: "supervisor-222",
        inquiryStatus: "CLOSED",
        transcriptId: "transcript-999",
        participants: [
            {
                id: "supervisor-111",
                encryptedShare: "enc-share-7",
            },
            {
                id: "supervisor-333",
                encryptedShare: "enc-share-8",
            },
        ],
    },
];

export type GetTranscriptAccessInquiryResponse = ResponseFormat<
    TranscriptAccessInquiry[] | null
>;
export async function getTranscriptAccessInquiries(): Promise<GetTranscriptAccessInquiryResponse> {
    return {
        success: true,
        message: "OK",
        data: dummyData,
    };
}
