import api from "@/axios";
import type { ResponseFormat } from "../response";
import type { EncryptedTranscriptWithStudent } from "@/types/TranscriptWithStudent";
import axios from "axios";

export type GetStudentTranscriptByIdResponse =
    ResponseFormat<EncryptedTranscriptWithStudent>;

export async function getStudentTranscriptById(
    id: string
): Promise<GetStudentTranscriptByIdResponse> {
    try {
        const response = await api.get<GetStudentTranscriptByIdResponse>(
            `/api/v1/transcripts/${id}`
        );
        const data = response.data;

        return data;
    } catch (error) {
        if (axios.isAxiosError(error) && error.response) {
            return error.response.data;
        }

        return {
            success: false,
            message: "An unexpected error occurred",
            data: null,
        };
    }
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
