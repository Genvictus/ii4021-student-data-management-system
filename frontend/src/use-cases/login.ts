import api from "@/axios";
import type { ResponseFormat } from "./response-format";
import axios from "axios";

type LoginPayload = {
    email: string;
    password: string;
};

export async function login(
    payload: LoginPayload
): Promise<ResponseFormat<string[] | null>> {
    try {
        const response = await api.post("api/v1/auth/login", payload);

        console.log(response.data.data);
        const data = response.data.data;

        localStorage.setItem("token", data.token);
        localStorage.setItem("user", JSON.stringify(data.user));

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
