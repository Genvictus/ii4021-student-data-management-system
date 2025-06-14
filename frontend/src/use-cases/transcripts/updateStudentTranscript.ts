import axios from "axios";
import api from "@/axios";
import { getUserProfile } from "@/lib/getUserProfile";
import { getPrivateKey } from "@/private-key-store/opfs";
import type { TranscriptUpdatePayload } from "@/types/TranscriptPayload";
import type { TranscriptWithStudent } from "@/types/TranscriptWithStudent";
import type { ResponseFormat } from "@/use-cases/response";
import { toUpdatePayload } from "./util";

async function encryptDataAndKeys(transcript: TranscriptWithStudent): Promise<TranscriptUpdatePayload> {
    const selfKey = await getPrivateKey(getUserProfile()!.email);

    const payload = toUpdatePayload(transcript, selfKey!);

    return payload;
}

export async function UpdateStudentTranscript(transcript: TranscriptWithStudent): Promise<ResponseFormat<string[] | null>> {
    try {
        const processedPayload = encryptDataAndKeys(transcript);

        const response = await api.post("api/v1/transcripts", processedPayload);

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