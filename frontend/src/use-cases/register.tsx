import type { ResponseFormat } from "./response-format";
import axios from "axios";

interface RegisterPayload {
    userId: string;
    email: string;
    password: string;
    fullName: string;
    role: string;
    publicKey: string;
    departmentId: string;
}

export async function register(
    payload: RegisterPayload
): Promise<ResponseFormat<string[] | null>> {
    const baseUrl = import.meta.env.VITE_BE_BASE_URL;

    if (!baseUrl) {
        throw new Error("BASE_URL is not defined in environment variables");
    }

    const url = `${baseUrl}/api/v1/auth/register`;

    try {
        const response = await axios.post<ResponseFormat<null | string[]>>(
            url,
            payload
        );
        return response.data;
    } catch (error) {
        console.error(error);
        if (axios.isAxiosError(error) && error.response) {
            return error.response.data as ResponseFormat<null | string[]>;
        }

        return {
            success: false,
            message: "An unexpected error occurred",
            data: null,
        };
    }
}
