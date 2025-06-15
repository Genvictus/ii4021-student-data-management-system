import api from "@/axios";
import type { ResponseFormat } from "@/use-cases/response";
import axios from "axios";

type GetStudentTranscriptIdResponse = ResponseFormat<string | null>;

export async function getStudentTranscriptId(
    studentId: string
): Promise<GetStudentTranscriptIdResponse> {
    try {
        const response = await api.get<GetStudentTranscriptIdResponse>(
            `/api/v1/transcripts/student/${studentId}`
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
