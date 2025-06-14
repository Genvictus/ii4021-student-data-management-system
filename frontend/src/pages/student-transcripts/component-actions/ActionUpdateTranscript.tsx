import { useEffect, useState } from "react";
import type { TranscriptActionsProps } from "../TranscriptActions";
import { CheckCircle2, Pencil, Plus, XCircle } from "lucide-react";
import {
    Dialog,
    DialogContent,
    DialogFooter,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Separator } from "@/components/ui/separator";
import { TranscriptCourseEditTable } from "./TranscriptCourseEditTable";
import { TranscriptViewCard } from "./TranscriptView";
import { UpdateStudentTranscript } from "@/use-cases/transcripts/updateStudentTranscript";
import { toast } from "sonner";

export function ActionUpdateTranscript(props: TranscriptActionsProps) {
    const { transcript } = props;
    const [open, setOpen] = useState(false);
    const [data, setData] = useState(
        structuredClone(transcript.transcriptData)
    );

    useEffect(() => {
        if (open) {
            structuredClone(transcript.transcriptData);
        }
    }, [open]);

    const removeEntry = (index: number) => {
        setData((prev) => prev.filter((_, i) => i !== index));
    };

    const addEntry = () => {
        setData((prev) => [
            ...prev,
            {
                courseCode: "",
                credits: 0,
                score: "A" as const,
            },
        ]);
    };

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

    const handleConfirm = async () => {
        const response = await UpdateStudentTranscript(transcript);

        if (response.success) {
            toast.success("Successfully updated transcript");
        } else {
            let errorMsg = "Failed to update transcript";
            toast.error(errorMsg, {
                description: response.message ?? undefined,
            });
        }

        setOpen(false);
    };

    const handleCancel = () => {
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
                        <DialogTitle className="text-2xl font-semibold">
                            Update Transcript
                        </DialogTitle>
                        <Separator />
                    </DialogHeader>

                    {/* Scrollable content */}
                    <div className="space-y-4 text-base max-h-[70vh] overflow-y-auto pr-2">
                        <div className="space-y-4 text-base">
                            <div
                                style={{
                                    transform: `scale(0.8)`,
                                    transformOrigin: "top left",
                                }}
                            >
                                <TranscriptViewCard transcript={transcript} />
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
                        </div>
                    </div>

                    <DialogFooter>
                        <div className="flex justify-end gap-2 pt-4">
                            <Button variant="outline" onClick={handleCancel}>
                                <XCircle />
                                Cancel
                            </Button>
                            <Button variant="default" onClick={handleConfirm}>
                                <CheckCircle2 />
                                Confirm Update
                            </Button>
                        </div>
                    </DialogFooter>
                </DialogContent>
            </Dialog>
        </div>
    );
}
