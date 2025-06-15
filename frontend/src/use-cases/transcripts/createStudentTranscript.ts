import api from "@/axios";
import { aesKeyToString, generateAesKey } from "@/lib/aes";
import { stringToBN } from "@/lib/converter";
import { getUserProfile } from "@/lib/getUserProfile";
import { stringToPublicKey, type RsaPublicKey } from "@/lib/rsa";
import type { GetEncryptedStudentTranscriptResponse, TranscriptUpdatePayload } from "@/types/TranscriptPayload";
import type { TranscriptWithStudent } from "@/types/TranscriptWithStudent";
import type { UserProfile } from "@/types/UserProfile";
import type { ResponseFormat } from "@/use-cases/response";
import axios from "axios";
import { encryptKeys, encryptTranscriptEntries, toPayload } from "./util";

type TranscriptCreationPayload = Pick<
    TranscriptWithStudent,
    "studentId" | "transcriptData"
>;

type UserResponse = Omit<UserProfile, "publicKey"> & { publicKey: string };

async function getHodPublicKey(departmentId: string): Promise<RsaPublicKey> {
    const response = await api.get<ResponseFormat<UserResponse[]>>(
        "api/v1/users",
        {
            params: { departmentId: departmentId, role: "HOD" },
        }
    );
    return stringToPublicKey(response.data.data![0].publicKey);
}

async function getStudentPublicKey(studentId: string): Promise<RsaPublicKey> {
    const response = await api.get<ResponseFormat<UserResponse>>(
        `api/v1/users/${studentId}`
    );
    return stringToPublicKey(response.data.data!.publicKey);
}

async function encryptDataAndKeys(
    transcript: TranscriptCreationPayload,
    encryptionKey: string
): Promise<TranscriptUpdatePayload> {
    const hodPK = await getHodPublicKey(getUserProfile()!.departmentId);
    const studentPK = await getStudentPublicKey(transcript.studentId);
    const supervisorPK = getUserProfile()!.publicKey;

    const aesKey = generateAesKey(encryptionKey);
    const aesKeyBN = stringToBN(aesKeyToString(aesKey));

    const payload = toPayload({ ...transcript, encryptedKey: "" });

    payload.encryptedTranscriptData = encryptTranscriptEntries(
        transcript.transcriptData,
        aesKey
    );
    payload.encryptedKeyHod = encryptKeys(aesKeyBN, hodPK);
    payload.encryptedKeySupervisor = encryptKeys(aesKeyBN, supervisorPK);
    payload.encryptedKeyStudent = encryptKeys(aesKeyBN, studentPK);

    return payload;
}

export async function createStudentTranscript(
    transcript: TranscriptCreationPayload,
    encryptionKey: string
): Promise<ResponseFormat<TranscriptWithStudent>> {
    try {
        const processedPayload = await encryptDataAndKeys(
            transcript,
            encryptionKey
        );

        console.log("transcript payload:", processedPayload);
        const response = await api.post<GetEncryptedStudentTranscriptResponse>("/api/v1/transcripts", processedPayload);

        const createdTranscript = response.data.data!

        return {
            success: true,
            message: response.data.message,
            data: {
                ...createdTranscript,
                transcriptData: transcript.transcriptData
            },
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
