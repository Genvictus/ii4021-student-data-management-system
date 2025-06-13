import { Button } from "@/components/ui/button";
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table";
import type { Course } from "@/types/Course";

interface InquiriesTableProps {
    inquiries: Course[];
}

export function InquiriesTable(props: InquiriesTableProps) {
    const { inquiries } = props;
    return (
        <Table>
            <TableHeader>
                <TableRow>
                    <TableHead className="w-[100px]">Code</TableHead>
                    <TableHead>Name</TableHead>
                    <TableHead>Credits</TableHead>
                    <TableHead>Department ID</TableHead>
                    <TableHead>Actions</TableHead>
                </TableRow>
            </TableHeader>
            <TableBody>
                {inquiries.length > 0 ? (
                    inquiries.map((inquiry) => (
                        <TableRow key={inquiry.courseId}>
                            <TableCell className="font-medium">
                                {inquiry.code}
                            </TableCell>
                            <TableCell>{inquiry.name}</TableCell>
                            <TableCell>{inquiry.credits}</TableCell>
                            <TableCell>{inquiry.departmentId}</TableCell>
                            <TableCell>
                                <InquiryActions inquiry={inquiry} />
                            </TableCell>
                        </TableRow>
                    ))
                ) : (
                    <TableRow>
                        <TableCell
                            colSpan={4}
                            className="text-center text-muted-foreground"
                        >
                            No inquiries available
                        </TableCell>
                    </TableRow>
                )}
            </TableBody>
        </Table>
    );
}

interface InquiryActionsProps {
    inquiry: Course;
}

function InquiryActions(props: InquiryActionsProps) {
    return (
        <div className="flex gap-2">
            <Button variant="outline" size="sm">
                View
            </Button>
            <Button variant="secondary" size="sm">
                Edit
            </Button>
            <Button variant="destructive" size="sm">
                Delete
            </Button>
        </div>
    );
}
