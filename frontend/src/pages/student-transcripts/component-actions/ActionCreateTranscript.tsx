import { useState } from "react";
import { Plus, PlusCircle } from "lucide-react";
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { calculateGpa } from "@/lib/calculateGpa";
import { Separator } from "@/components/ui/separator";
import { type TranscriptWithStudent } from "@/types/TranscriptWithStudent";
import { TranscriptCourseEditTable } from "./TranscriptCourseEditTable";

const dummyTranscript: Pick<
    TranscriptWithStudent,
    "studentId" | "transcriptData"
> = {
    studentId: "user-123",
    transcriptData: [
        {
            courseCode: "CS101",
            credits: 3,
            score: "A",
        },
        {
            courseCode: "MA201",
            credits: 4,
            score: "B",
        },
    ],
};

export function ActionCreateTranscript() {
    const [open, setOpen] = useState(false);
    const [transcript, setTranscript] = useState(
        structuredClone(dummyTranscript)
    );

    const updateEntry = (
        index: number,
        field: keyof (typeof transcript.transcriptData)[0],
        value: string | number
    ) => {
        const newData = transcript.transcriptData.map((entry, i) =>
            i === index ? { ...entry, [field]: value } : entry
        );
        setTranscript((prev) => ({
            ...prev,
            transcriptData: newData,
        }));
    };

    const removeEntry = (index: number) => {
        const newData = transcript.transcriptData.filter((_, i) => i !== index);
        setTranscript((prev) => ({
            ...prev,
            transcriptData: newData,
        }));
    };

    const addEntry = () => {
        setTranscript((prev) => ({
            ...prev,
            transcriptData: [
                ...prev.transcriptData,
                { courseCode: "", credits: 0, score: "A" },
            ],
        }));
    };

    const handleConfirm = () => {
        console.log("Created transcript:", transcript);
        setOpen(false);
    };

    const handleCancel = () => {
        setTranscript(structuredClone(dummyTranscript));
        setOpen(false);
    };

    return (
        <div className="flex gap-2">
            <Dialog open={open} onOpenChange={setOpen}>
                <DialogTrigger asChild>
                    <Button variant="default" size="sm">
                        <PlusCircle className="w-4 h-4 mr-1" />
                        Create Transcript
                    </Button>
                </DialogTrigger>
                <DialogContent className="!w-[60vw] !max-w-[60vw]">
                    <DialogHeader>
                        <DialogTitle className="text-2xl">
                            Create Transcript
                        </DialogTitle>
                        <Separator />
                    </DialogHeader>

                    {/* Scrollable content */}
                    <div className="space-y-4 text-base max-h-[70vh] overflow-y-auto pr-2">
                        <div>
                            <strong>Student ID:</strong>
                            <Input
                                className="mt-1 w-1/2"
                                value={transcript.studentId}
                                onChange={(e) =>
                                    setTranscript((prev) => ({
                                        ...prev,
                                        studentId: e.target.value,
                                    }))
                                }
                            />
                        </div>

                        <div>
                            <strong>GPA:</strong>{" "}
                            {calculateGpa(transcript.transcriptData)}
                        </div>

                        <div>
                            <strong>Courses:</strong>
                            <TranscriptCourseEditTable
                                data={transcript.transcriptData}
                                updateEntry={updateEntry}
                                removeEntry={removeEntry}
                            />
                            <div className="flex justify-end mt-4">
                                <Button
                                    variant="outline"
                                    size="sm"
                                    onClick={addEntry}
                                >
                                    <Plus className="w-4 h-4 mr-1" />
                                    Add Course
                                </Button>
                            </div>
                        </div>

                        <div className="flex justify-end gap-2 pt-4">
                            <Button variant="outline" onClick={handleCancel}>
                                Cancel
                            </Button>
                            <Button variant="default" onClick={handleConfirm}>
                                Confirm
                            </Button>
                        </div>
                    </div>
                </DialogContent>
            </Dialog>
        </div>
    );
}
