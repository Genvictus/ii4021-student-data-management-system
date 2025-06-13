import {
    getCourses,
    type GetCoursesResponse,
} from "@/use-cases/courses/getCourses";
import { useLoaderData } from "@tanstack/react-router";
import { InquiriesTable } from "./InquiriesTable";

export async function inquiriesLoader(): Promise<GetCoursesResponse> {
    return await getCourses();
}

export function Inquiries() {
    const inquiriesResponse = useLoaderData({ from: "/inquiries" });

    const inquiries =
        inquiriesResponse.success && inquiriesResponse.data
            ? inquiriesResponse.data
            : [];

    return (
        <div className="p-10">
            <h1 className="pb-3 text-2xl">View Inquiries</h1>
            <InquiriesTable inquiries={inquiries} />
        </div>
    );
}
