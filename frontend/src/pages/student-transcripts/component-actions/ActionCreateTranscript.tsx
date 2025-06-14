import { useState } from "react";
import { Plus, PlusCircle, XCircle } from "lucide-react";
import {
    Dialog,
    DialogContent,
    DialogFooter,
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
import { createStudentTranscript } from "@/use-cases/transcripts/createStudentTranscript";
import type { ResponseFormat } from "@/use-cases/response";
import type { EncryptedTranscriptWithStudent } from "@/types/EncryptedTranscriptWithStudent";
import { toast } from "sonner";

type TranscriptInput = Pick<
    TranscriptWithStudent,
    "studentId" | "transcriptData"
>;
const initialTranscript: TranscriptInput = {
    studentId: "",
    transcriptData: [],
};

export function ActionCreateTranscript() {
    const [open, setOpen] = useState(false);
    const [transcript, setTranscript] = useState(
        structuredClone(initialTranscript)
    );
    const [encryptionKey, setEncryptionKey] = useState<string>("");

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

    const handleConfirm = async () => {
        if (!encryptionKey) return;
        const response = await createStudentTranscript(
            transcript,
            encryptionKey
        );

        if (response.success) {
            toast.success("Successfully approved inquiry");
        } else {
            let errorMsg = "Failed to approve inquiry";
            toast.error(errorMsg, {
                description: response.message ?? undefined,
            });
        }

        setOpen(false);
    };

    const handleCancel = () => {
        setTranscript(structuredClone(initialTranscript));
        setOpen(false);
    };

    const confirmDisabled =
        !transcript.studentId.trim() ||
        !encryptionKey.trim() ||
        transcript.transcriptData.length === 0;

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
                        <DialogTitle className="flex gap-3 items-center text-2xl font-semibold">
                            <PlusCircle className="w-4 h-4 mr-1" />
                            Create Transcript
                        </DialogTitle>
                        <Separator />
                    </DialogHeader>

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
                            <strong>Encryption Key:</strong>
                            <Input
                                className="mt-1 w-1/2"
                                value={encryptionKey}
                                onChange={(e) => {
                                    setEncryptionKey(e.target.value);
                                }}
                            />
                        </div>
                        <div>
                            <strong>GPA:</strong>{" "}
                            {calculateGpa(transcript.transcriptData).gpa}
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
                    </div>
                    <DialogFooter>
                        <div className="flex justify-end gap-2 pt-4">
                            <Button variant="outline" onClick={handleCancel}>
                                <XCircle />
                                Cancel
                            </Button>
                            <Button
                                variant="default"
                                onClick={handleConfirm}
                                disabled={confirmDisabled}
                            >
                                Confirm
                            </Button>
                        </div>
                    </DialogFooter>
                </DialogContent>
            </Dialog>
        </div>
    );
}
