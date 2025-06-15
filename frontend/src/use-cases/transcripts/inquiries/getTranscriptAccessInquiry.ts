import api from "@/axios";
import type { TranscriptAccessInquiry } from "@/types/TranscriptAccessInquiry";
import type { ResponseFormat } from "@/use-cases/response";
import axios from "axios";

export type GetTranscriptAccessInquiryResponse =
    ResponseFormat<TranscriptAccessInquiry | null>;
export async function getTranscriptAccessInquiries(
    inquiryId: string
): Promise<GetTranscriptAccessInquiryResponse> {
    try {
        const response = await api.get<GetTranscriptAccessInquiryResponse>(
            `/api/v1/transcripts/access-inquiries/${inquiryId}`
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
