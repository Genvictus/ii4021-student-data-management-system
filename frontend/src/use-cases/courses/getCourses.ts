import api from "@/axios";
import type { Course } from "@/types/Course";
import type { ResponseFormat } from "@/use-cases/response";
import axios from "axios";

export type GetCoursesResponse = ResponseFormat<Course[] | null>;

export async function getCourses(): Promise<GetCoursesResponse> {
    try {
        const res = await api.get<ResponseFormat<Course[] | null>>(
            "/api/v1/courses"
        );

        return res.data;
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
