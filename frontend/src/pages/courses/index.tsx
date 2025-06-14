import {
    getCourses,
    type GetCoursesResponse,
} from "@/use-cases/courses/getCourses";
import { useLoaderData } from "@tanstack/react-router";
import { CoursesTable } from "./CoursesTable";

export async function coursesLoader(): Promise<GetCoursesResponse> {
    return await getCourses();
}

export function Courses() {
    const coursesResponse = useLoaderData({ from: "/courses" });

    const courses =
        coursesResponse.success && coursesResponse.data
            ? coursesResponse.data
            : [];

    return (
        <div className="p-10">
            <h1 className="pb-3 text-2xl font-semibold">View Courses</h1>
            <CoursesTable courses={courses} />
        </div>
    );
}
