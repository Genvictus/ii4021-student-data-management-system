import { useState } from "react";
import type { TranscriptActionsProps } from "../TranscriptActions";
import { Pencil, Plus } from "lucide-react";
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
import { Separator } from "@/components/ui/separator";
import { TranscriptCourseEditTable } from "./TranscriptCourseEditTable";
import { Badge } from "@/components/ui/badge";
import { StatusBadge } from "@/components/ui/status-badge";

export function ActionUpdateTranscript(props: TranscriptActionsProps) {
    const { transcript } = props;
    const [open, setOpen] = useState(false);
    const [data, setData] = useState(
        structuredClone(transcript.transcriptData)
    );

    const removeEntry = (index: number) => {
        setData((prev) => prev.filter((_, i) => i !== index));
    };

    function addEntry() {
        setData((prev) => [
            ...prev,
            {
                courseCode: "",
                credits: 0,
                score: "A" as const,
            },
        ]);
    }

    const updateEntry = (
        index: number,
        field: keyof (typeof data)[0],
        value: string | number
    ) => {
        setData((prev) =>
            prev.map((entry, i) =>
                i === index ? { ...entry, [field]: value } : entry
            )
        );
    };

    const handleConfirm = () => {
        console.log("Updated transcriptData:", data);
        setOpen(false);
    };

    const handleCancel = () => {
        setData(structuredClone(transcript.transcriptData));
        setOpen(false);
    };

    return (
        <div className="flex gap-2">
            <Dialog open={open} onOpenChange={setOpen}>
                <DialogTrigger asChild>
                    <Button variant="default" size="sm">
                        <Pencil className="w-4 h-4 mr-2" />
                        Update
                    </Button>
                </DialogTrigger>
                <DialogContent className="!w-[60vw] !max-w-[60vw]">
                    <DialogHeader>
                        <DialogTitle className="text-2xl">
                            Update Transcript
                        </DialogTitle>
                        <Separator />
                    </DialogHeader>

                    {/* Scrollable content */}
                    <div className="space-y-4 text-base max-h-[70vh] overflow-y-auto pr-2">
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
                                <StatusBadge
                                    status={transcript.transcriptStatus}
                                />
                            </div>
                            <div>
                                <strong>GPA:</strong> {calculateGpa(data)}
                            </div>

                            <div>
                                <strong>Courses:</strong>
                                <TranscriptCourseEditTable
                                    data={data}
                                    updateEntry={updateEntry}
                                    removeEntry={removeEntry}
                                />
                                <div className="flex justify-end mt-4">
                                    <Button
                                        variant="outline"
                                        size="sm"
                                        onClick={addEntry}
                                    >
                                        <Plus className="w-4 h-4 mr-1" /> Add
                                        Course
                                    </Button>
                                </div>
                            </div>

                            <div className="flex justify-end gap-2 pt-4">
                                <Button
                                    variant="outline"
                                    onClick={handleCancel}
                                >
                                    Cancel
                                </Button>
                                <Button
                                    variant="default"
                                    onClick={handleConfirm}
                                >
                                    Confirm Update
                                </Button>
                            </div>
                        </div>
                    </div>
                </DialogContent>
            </Dialog>
        </div>
    );
}
