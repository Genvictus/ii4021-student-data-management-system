import type { TranscriptWithStudent } from "@/types/TranscriptWithStudent";
import type { ResponseFormat } from "../response";

export type GetStudentTranscriptsResponse = ResponseFormat<
    TranscriptWithStudent[] | null
>;

export async function getStudentTranscripts(): Promise<GetStudentTranscriptsResponse> {
    return {
        success: true,
        message: "Successfully get student transcripts",
        data: dummyData,
    };
}

const dummyData: TranscriptWithStudent[] = [
    {
        transcriptId: "transcript-001",
        student: {
            userId: "user-123",
            email: "student1@example.com",
            fullName: "Alice Smith",
            role: "STUDENT",
            publicKey: "student1-public-key",
            department: null,
            departmentId: "dept-001",
            supervisorId: "hod-001",
        },
        studentId: "user-123",
        transcriptStatus: "PENDING",
        transcriptData: [
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
        ],
        hodId: "hod-001",
        hod: null,
        hodDigitalSignature: "dummy-signature-123",
        encryptedKey: "encrypted-key-abc",
    },
    {
        transcriptId: "transcript-002",
        student: {
            userId: "user-456",
            email: "student2@example.com",
            fullName: "Bob Johnson",
            role: "STUDENT",
            publicKey: "student2-public-key",
            department: null,
            departmentId: "dept-002",
            supervisorId: "hod-002",
        },
        studentId: "user-456",
        transcriptStatus: "APPROVED",
        transcriptData: [
            {
                courseCode: "PH101",
                credits: 2,
                score: "AB",
            },
            {
                courseCode: "EN102",
                credits: 3,
                score: "BC",
            },
        ],
        hodId: "hod-002",
        hod: null,
        hodDigitalSignature: "dummy-signature-456",
        encryptedKey: "encrypted-key-def",
    },
];
