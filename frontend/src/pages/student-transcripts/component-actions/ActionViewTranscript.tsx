import { useState } from "react";
import type { TranscriptActionsProps } from "../TranscriptActions";
import { Eye } from "lucide-react";
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { calculateGpa } from "@/lib/calculateGpa";
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table";
import { Separator } from "@/components/ui/separator";

export function ActionViewTranscript(props: TranscriptActionsProps) {
    const { transcript } = props;
    const [open, setOpen] = useState(false);

    return (
        <div className="flex gap-2">
            <Dialog open={open} onOpenChange={setOpen}>
                <DialogTrigger asChild>
                    <Button variant="secondary" size="sm">
                        <Eye className="w-4 h-4 mr-2" />
                        View
                    </Button>
                </DialogTrigger>
                <DialogContent className="!w-[60vw] !max-w-[60vw]">
                    <DialogHeader>
                        <DialogTitle className="text-2xl">
                            Transcript Details
                        </DialogTitle>
                        <Separator />
                        <DialogDescription>
                            <div className="space-y-4 text-base">
                                <div>
                                    <strong>Student ID:</strong>{" "}
                                    {transcript.student.userId}
                                </div>
                                <div>
                                    <strong>Student:</strong>{" "}
                                    {transcript.student.fullName}
                                </div>
                                <div>
                                    <strong>Status:</strong>{" "}
                                    {transcript.transcriptStatus}
                                </div>
                                <div>
                                    <strong>GPA:</strong>{" "}
                                    {calculateGpa(transcript.transcriptData)}
                                </div>
                                <div>
                                    <strong>Courses:</strong>
                                    <Table className="mt-2">
                                        <TableHeader>
                                            <TableRow>
                                                <TableHead className="w-[150px] text-center">
                                                    Course Code
                                                </TableHead>
                                                <TableHead className="text-center">
                                                    Credits
                                                </TableHead>
                                                <TableHead className="text-center">
                                                    Score
                                                </TableHead>
                                            </TableRow>
                                        </TableHeader>
                                        <TableBody>
                                            {transcript.transcriptData.map(
                                                (entry, index) => (
                                                    <TableRow
                                                        key={index}
                                                        className="text-center"
                                                    >
                                                        <TableCell>
                                                            {entry.courseCode}
                                                        </TableCell>
                                                        <TableCell>
                                                            {entry.credits}
                                                        </TableCell>
                                                        <TableCell>
                                                            {entry.score}
                                                        </TableCell>
                                                    </TableRow>
                                                )
                                            )}
                                        </TableBody>
                                    </Table>
                                </div>
                            </div>
                        </DialogDescription>
                    </DialogHeader>
                </DialogContent>
            </Dialog>
        </div>
    );
}
