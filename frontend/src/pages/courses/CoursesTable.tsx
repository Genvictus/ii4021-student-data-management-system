import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table";
import type { Course } from "@/types/Course";

interface CorusesTableProps {
    courses: Course[];
}

export function CoursesTable(props: CorusesTableProps) {
    const { courses } = props;
    return (
        <Table>
            <TableHeader>
                <TableRow>
                    <TableHead className="w-[100px]">Code</TableHead>
                    <TableHead>Name</TableHead>
                    <TableHead>Credits</TableHead>
                    <TableHead>Department ID</TableHead>
                </TableRow>
            </TableHeader>
            <TableBody>
                {courses.length > 0 ? (
                    courses.map((course) => (
                        <TableRow key={course.courseId}>
                            <TableCell className="font-medium">
                                {course.code}
                            </TableCell>
                            <TableCell>{course.name}</TableCell>
                            <TableCell>{course.credits}</TableCell>
                            <TableCell>{course.departmentId}</TableCell>
                        </TableRow>
                    ))
                ) : (
                    <TableRow>
                        <TableCell
                            colSpan={4}
                            className="text-center text-muted-foreground"
                        >
                            No courses available
                        </TableCell>
                    </TableRow>
                )}
            </TableBody>
        </Table>
    );
}
