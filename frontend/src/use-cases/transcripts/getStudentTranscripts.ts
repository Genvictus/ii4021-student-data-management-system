import api from "@/axios";
import { aesKeyFromString } from "@/lib/aes";
import { BNToString } from "@/lib/converter";
import { getUserProfile } from "@/lib/getUserProfile";
import { getPrivateKey } from "@/private-key-store/opfs";
import type { TranscriptWithStudent } from "@/types/TranscriptWithStudent";
import type { ResponseFormat } from "../response";
import { decryptKeys, decryptTranscriptEntries } from "./util";
import axios from "axios";

async function parseAndDecryptTranscripts(transcripts: EncryptedTranscript[]): Promise<TranscriptWithStudent[]> {
    const selfKey = await getPrivateKey(getUserProfile()!.email);
    return transcripts.map(t => {
        const decryptKeyString = BNToString(decryptKeys(t.encryptedKey, selfKey!));
        const decryptKey = aesKeyFromString(decryptKeyString);
        return {
            ...t,
            transcriptData: decryptTranscriptEntries(t.encryptedTranscriptData, decryptKey)
        }
    })
}

type EncryptedTranscript = Omit<TranscriptWithStudent, "transcriptData"> & { encryptedTranscriptData: string }

export type GetStudentTranscriptsResponse = ResponseFormat<
    TranscriptWithStudent[]
>;

type GetEncryptedStudentTranscriptsResponse = ResponseFormat<
    EncryptedTranscript[]
>;

export async function getStudentTranscripts(): Promise<GetStudentTranscriptsResponse> {
    try {
        const response = await api.get<GetEncryptedStudentTranscriptsResponse>("api/v1/transcripts");
        const data = response.data.data;

        return {
            success: true,
            message: response.data.message,
            data: data ? await parseAndDecryptTranscripts(data) : null,
        };
    } catch (error) {
        console.error(error);

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
