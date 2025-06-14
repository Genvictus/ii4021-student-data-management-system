import type { TranscriptAccessInquiry } from "@/types/TranscriptAccessInquiry";
import type { ResponseFormat } from "@/use-cases/response";

export async function approveTranscriptAccessInquiry(
    inquiry: TranscriptAccessInquiry
): Promise<ResponseFormat<null>> {
    // TODO: @Genvictus
    return {
        success: true,
        message: "OK",
        data: null,
    };
}
