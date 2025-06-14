import api from "@/axios";
import { aesKeyToString, generateAesKey } from "@/lib/aes";
import { stringToBN } from "@/lib/converter";
import { getUserProfile } from "@/lib/getUserProfile";
import { stringToPublicKey, type RsaPublicKey } from "@/lib/rsa";
import type { TranscriptUpdatePayload } from "@/types/TranscriptPayload";
import type { ResponseFormat } from "@/use-cases/response";
import axios from "axios";
import { encryptKeys, encryptTranscriptEntries, toUpdatePayload } from "./util";
import type { TranscriptWithStudent } from "@/types/TranscriptWithStudent";

async function getHodPublicKey(departmentId: string): Promise<RsaPublicKey> {
    const response = await api.get("/api/v1/users", {
        params: { departmentId: departmentId, role: "HOD" },
    });
    return stringToPublicKey(response.data.publicKey);
}

async function getStudentPublicKey(studentId: string): Promise<RsaPublicKey> {
    const response = await api.get(`api/v1/users/${studentId}`);
    return stringToPublicKey(response.data.publicKey);
}

async function encryptDataAndKeys(
    transcript: TranscriptWithStudent,
    encryptionKey: string
): Promise<TranscriptUpdatePayload> {
    const hodPK = await getHodPublicKey(getUserProfile()!.departmentId);
    const studentPK = await getStudentPublicKey(transcript.studentId);
    const supervisorPK = getUserProfile()!.publicKey;

    const aesKey = generateAesKey(encryptionKey);
    const aesKeyBN = stringToBN(aesKeyToString(aesKey));

    const payload = toUpdatePayload(transcript);

    payload.encryptedTranscriptData = encryptTranscriptEntries(
        transcript.transcriptData!,
        aesKey
    );
    payload.encryptedKeyHod = encryptKeys(aesKeyBN, hodPK);
    payload.encryptedKeySupervisor = encryptKeys(aesKeyBN, supervisorPK);
    payload.encryptedKeyStudent = encryptKeys(aesKeyBN, studentPK);

    return payload;
}

export async function createStudentTranscript(
    transcript: TranscriptWithStudent,
    encryptionKey: string
): Promise<ResponseFormat<string[] | null>> {
    try {
        const processedPayload = encryptDataAndKeys(transcript, encryptionKey);

        const response = await api.post(
            "/api/v1/transcripts",
            processedPayload
        );

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
