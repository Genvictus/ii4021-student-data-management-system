import type { ResponseFormat } from "../response";
import type { EncryptedTranscriptWithStudent } from "@/types/EncryptedTranscriptWithStudent";

export type GetStudentTranscriptByIdResponse =
    ResponseFormat<EncryptedTranscriptWithStudent>;

export async function getStudentTranscriptById(
    id: string
): Promise<GetStudentTranscriptByIdResponse> {
    // TODO: @Genvictus
    return {
        success: true,
        message: "Successfully get student transcript by ID",
        data: dummyData,
    };
}

const dummyData: EncryptedTranscriptWithStudent = {
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
    encryptedTranscriptData: "adfafasdfasdf",
    hodId: "hod-001",
    hod: null,
    hodDigitalSignature: "dummy-signature-123",
    encryptedKey: "encrypted-key-abc",
};
