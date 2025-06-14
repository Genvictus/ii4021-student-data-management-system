import api from "@/axios";
import { getUserProfile } from "@/lib/getUserProfile";
import type { RsaPrivateKey } from "@/lib/rsa";
import { getPrivateKey } from "@/private-key-store/opfs";
import type { ResponseFormat } from "@/use-cases/response";
import axios from "axios";

type LoginPayload = {
    email: string;
    password: string;
};

export async function login(
    payload: LoginPayload,
    privateKey?: RsaPrivateKey
): Promise<ResponseFormat<string[] | null>> {
    try {
        const pk = privateKey ?? (await getPrivateKey(getUserProfile()!.email))!
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
