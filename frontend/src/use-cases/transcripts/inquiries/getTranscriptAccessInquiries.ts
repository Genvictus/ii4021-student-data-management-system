import type { TranscriptAccessInquiry } from "@/types/TranscriptAccessInquiry";
import type { ResponseFormat } from "@/use-cases/response";

export const dummyData: TranscriptAccessInquiry[] = [
    // WAITING_FOR_PARTICIPANTS
    {
        inquiryId: "inq-001",
        requesterId: "7482749817491278472",
        requesteeId: "supervisor-001",
        inquiryStatus: "WAITING_FOR_PARTICIPANTS",
        transcriptId: "transcript-001",
        participants: [
            { id: "7482749817491278472", encryptedShare: "enc-001" },
        ],
    },
    {
        inquiryId: "inq-002",
        requesterId: "supervisor-002",
        requesteeId: "7482749817491278472",
        inquiryStatus: "WAITING_FOR_PARTICIPANTS",
        transcriptId: "transcript-002",
        participants: [{ id: "supervisor-002", encryptedShare: "enc-003" }],
    },
    {
        inquiryId: "inq-003",
        requesterId: "supervisor-003",
        requesteeId: "supervisor-004",
        inquiryStatus: "WAITING_FOR_PARTICIPANTS",
        transcriptId: "transcript-003",
        participants: [{ id: "supervisor-003", encryptedShare: "enc-005" }],
    },
    {
        inquiryId: "inq-004",
        requesterId: "supervisor-005",
        requesteeId: "supervisor-006",
        inquiryStatus: "WAITING_FOR_PARTICIPANTS",
        transcriptId: "transcript-004",
        participants: [{ id: "supervisor-005", encryptedShare: "enc-007" }],
    },

    // WAITING_FOR_REQUESTEE
    {
        inquiryId: "inq-005",
        requesterId: "7482749817491278472",
        requesteeId: "supervisor-011",
        inquiryStatus: "WAITING_FOR_REQUESTEE",
        transcriptId: "transcript-005",
        participants: [
            { id: "7482749817491278472", encryptedShare: "enc-009" },
            { id: "supervisor-010", encryptedShare: "enc-010" },
        ],
    },
    {
        inquiryId: "inq-006",
        requesterId: "supervisor-012",
        requesteeId: "7482749817491278472",
        inquiryStatus: "WAITING_FOR_REQUESTEE",
        transcriptId: "transcript-006",
        participants: [
            { id: "supervisor-012", encryptedShare: "enc-011" },
            { id: "supervisor-013", encryptedShare: "enc-012" },
        ],
    },
    {
        inquiryId: "inq-007",
        requesterId: "supervisor-014",
        requesteeId: "supervisor-015",
        inquiryStatus: "WAITING_FOR_REQUESTEE",
        transcriptId: "transcript-007",
        participants: [
            { id: "7482749817491278472", encryptedShare: "enc-013" },
            { id: "supervisor-014", encryptedShare: "enc-014" },
        ],
    },
    {
        inquiryId: "inq-008",
        requesterId: "supervisor-016",
        requesteeId: "supervisor-017",
        inquiryStatus: "WAITING_FOR_REQUESTEE",
        transcriptId: "transcript-008",
        participants: [
            { id: "supervisor-016", encryptedShare: "enc-015" },
            { id: "supervisor-019", encryptedShare: "enc-016" },
        ],
    },

    // APPROVED
    {
        inquiryId: "inq-009",
        requesterId: "7482749817491278472",
        requesteeId: "supervisor-021",
        inquiryStatus: "APPROVED",
        transcriptId: "transcript-009",
        participants: [
            { id: "7482749817491278472", encryptedShare: "enc-017" },
            { id: "supervisor-020", encryptedShare: "enc-018" },
        ],
    },
    {
        inquiryId: "inq-010",
        requesterId: "supervisor-022",
        requesteeId: "7482749817491278472",
        inquiryStatus: "APPROVED",
        transcriptId: "transcript-010",
        participants: [
            { id: "supervisor-022", encryptedShare: "enc-019" },
            { id: "supervisor-023", encryptedShare: "enc-020" },
        ],
    },
    {
        inquiryId: "inq-011",
        requesterId: "supervisor-024",
        requesteeId: "supervisor-025",
        inquiryStatus: "APPROVED",
        transcriptId: "transcript-011",
        participants: [
            { id: "7482749817491278472", encryptedShare: "enc-021" },
            { id: "supervisor-024", encryptedShare: "enc-022" },
        ],
    },
    {
        inquiryId: "inq-012",
        requesterId: "supervisor-026",
        requesteeId: "supervisor-027",
        inquiryStatus: "APPROVED",
        transcriptId: "transcript-012",
        participants: [
            { id: "supervisor-026", encryptedShare: "enc-023" },
            { id: "supervisor-029", encryptedShare: "enc-024" },
        ],
    },

    // CLOSED
    {
        inquiryId: "inq-013",
        requesterId: "7482749817491278472",
        requesteeId: "supervisor-031",
        inquiryStatus: "CLOSED",
        transcriptId: "transcript-013",
        participants: [
            { id: "supervisor-030", encryptedShare: "enc-025" },
            { id: "7482749817491278472", encryptedShare: "enc-026" },
        ],
    },
    {
        inquiryId: "inq-014",
        requesterId: "supervisor-032",
        requesteeId: "7482749817491278472",
        inquiryStatus: "CLOSED",
        transcriptId: "transcript-014",
        participants: [
            { id: "supervisor-032", encryptedShare: "enc-027" },
            { id: "supervisor-033", encryptedShare: "enc-028" },
        ],
    },
    {
        inquiryId: "inq-015",
        requesterId: "supervisor-034",
        requesteeId: "supervisor-035",
        inquiryStatus: "CLOSED",
        transcriptId: "transcript-015",
        participants: [{ id: "supervisor-034", encryptedShare: "enc-030" }],
    },
    {
        inquiryId: "inq-016",
        requesterId: "supervisor-037",
        requesteeId: "supervisor-038",
        inquiryStatus: "CLOSED",
        transcriptId: "transcript-016",
        participants: [{ id: "supervisor-037", encryptedShare: "enc-031" }],
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
