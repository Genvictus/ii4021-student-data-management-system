import api from "@/axios";
import type { ResponseFormat } from "@/use-cases/response";
import axios from "axios";

export async function createTranscriptAccessInquiry(
    transcriptId: string
): Promise<ResponseFormat<void>> {
    try {
        const response = await api.post<ResponseFormat<null>>(
            "/api/v1/transcripts/access-inquiries",
            transcriptId
        );

        return response.data;
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
