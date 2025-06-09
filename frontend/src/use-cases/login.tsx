import type { ResponseFormat } from "./response-format";
import axios from "axios";

interface LoginPayload {
    email: string;
    password: string;
}

export async function login(
    payload: LoginPayload
): Promise<ResponseFormat<string[] | null>> {
    const baseUrl = import.meta.env.VITE_BE_BASE_URL;

    if (!baseUrl) {
        throw new Error("BASE_URL is not defined in environment variables");
    }

    const url = `${baseUrl}/api/v1/auth/login`;

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
