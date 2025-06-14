import api from "@/axios";
import { aesKeyToString, generateAesKey } from "@/lib/aes";
import { stringToBN } from "@/lib/converter";
import { getUserProfile } from "@/lib/getUserProfile";
import { stringToPublicKey, type RsaPublicKey } from "@/lib/rsa";
import type { TranscriptUpdatePayload } from "@/types/TranscriptPayload";
import type { ResponseFormat } from "@/use-cases/response";
import axios from "axios";
import { encryptKeys, encryptTranscriptEntries, toPayload } from "./util";
import type { TranscriptWithStudent } from "@/types/TranscriptWithStudent";
import type { UserProfile } from "@/types/UserProfile";

type TranscriptCreationPayload = Pick<
    TranscriptWithStudent,
    "studentId" | "transcriptData"
>

type UserResponse = Omit<UserProfile, "publicKey"> & { publicKey: string }

async function getHodPublicKey(departmentId: string): Promise<RsaPublicKey> {
    const response = await api.get<ResponseFormat<UserResponse[]>>("api/v1/users", {
        params: { departmentId: departmentId, role: "HOD" },
    });
    return stringToPublicKey(response.data.data![0].publicKey);
}

async function getStudentPublicKey(studentId: string): Promise<RsaPublicKey> {
    const response = await api.get<ResponseFormat<UserResponse>>(`api/v1/users/${studentId}`);
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
        transcript.transcriptData!,
        aesKey
    );
    payload.encryptedKeyHod = encryptKeys(aesKeyBN, hodPK);
    payload.encryptedKeySupervisor = encryptKeys(aesKeyBN, supervisorPK);
    payload.encryptedKeyStudent = encryptKeys(aesKeyBN, studentPK);

    return payload;
}

// TODO: @Genvictus
// 1. Ini bagusnya transcript type nya diganti jadi Pick<TranscriptWithStudent, "studentId" | "transcriptData">
// Karena transcriptId kan belum ada pas datanya dibikin, dan transcript.student juga gaperlu dimasukin (cuma perlu studentId).
// Please refer to src/pages/student-transcripts/component-actions/ActionCreateTranscript.tsx
// 2. Kenapa ini returnnya <ResponseFormat<string[] | null> ???
// Fix ASAP
export async function createStudentTranscript(
    transcript: TranscriptCreationPayload,
    encryptionKey: string
): Promise<ResponseFormat<string[] | null>> {
    try {
        const processedPayload = await encryptDataAndKeys(transcript, encryptionKey);

        console.log("transcript payload:", processedPayload);
        const response = await api.post("/api/v1/transcripts", processedPayload);

        console.log(response.data.data);

        return {
            success: true,
            message: response.data.message,
            data: null,
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
