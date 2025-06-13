import api from "@/axios";
import type { ResponseFormat } from "../response";
import axios from "axios";

type RegisterPayload = {
    userId: string;
    email: string;
    password: string;
    fullName: string;
    role: string;
    publicKey: string;
    departmentId: string;
};

export async function register(
    payload: RegisterPayload
): Promise<ResponseFormat<string[] | null>> {
    try {
        const response = await api.post<ResponseFormat<null | string[]>>(
            "/api/v1/auth/register",
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
