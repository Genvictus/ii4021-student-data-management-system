import api from "@/axios";
import { getUserProfile } from "@/lib/getUserProfile";
import { sha3Digest, sign } from "@/lib/rsa";
import { getPrivateKey } from "@/private-key-store/opfs";
import type { TranscriptSignPayload } from "@/types/TranscriptPayload";
import type { ResponseFormat } from "@/use-cases/response";
import axios from "axios";
import { encryptTranscriptEntriesFromKeyString } from "./util";

export async function signTranscript(
    transcript: TranscriptSignPayload
): Promise<ResponseFormat<string[] | null>> {
    try {
        const selfPK = (await getPrivateKey(getUserProfile()!.email))!;

        const transcriptEntryString = JSON.stringify(transcript.transcriptData);
        const digest = sha3Digest(transcriptEntryString);
        const signature = sign(digest, selfPK);
        const signatureString = signature.toString(16);

        // TODO
        const response = await api.patch(`/api/v1/transcripts/${transcript.transcriptId}/signature`, signatureString);

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
