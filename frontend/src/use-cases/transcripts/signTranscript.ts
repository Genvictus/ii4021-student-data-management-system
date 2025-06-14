import type { TranscriptWithStudent } from "@/types/TranscriptWithStudent";
import type { ResponseFormat } from "../response";

type SignTranscriptResponse = ResponseFormat<null>;
export async function signTranscript(
    transcript: TranscriptWithStudent
): Promise<SignTranscriptResponse> {
    // TODO: @Genvictus
    return {
        success: true,
        message: "OK",
        data: null,
    };
}
