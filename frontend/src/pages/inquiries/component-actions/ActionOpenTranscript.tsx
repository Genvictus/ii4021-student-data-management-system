import { Button } from "@/components/ui/button";
import {
    Dialog,
    DialogContent,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
} from "@/components/ui/dialog";
import { Separator } from "@/components/ui/separator";
import { TranscriptView } from "@/pages/student-transcripts/component-actions/TranscriptView";
import type { TranscriptAccessInquiry } from "@/types/TranscriptAccessInquiry";
import { type TranscriptWithStudent } from "@/types/TranscriptWithStudent";
import { FileText } from "lucide-react";
import { useEffect, useState } from "react";

interface ActionOpenTranscriptProps {
    inquiry: TranscriptAccessInquiry;
}

export function ActionOpenTranscript(props: ActionOpenTranscriptProps) {
    const { inquiry } = props;
    const [open, setOpen] = useState(false);
    const [transcript, setTranscript] = useState<TranscriptWithStudent | null>(
        null
    );

    useEffect(() => {
        setTranscript({
            transcriptId: "transcript-001",
            student: {
                userId: "user-123",
                email: "student1@example.com",
                fullName: "Alice Smith",
                role: "STUDENT",
                publicKey: "student1-public-key",
                department: null,
                departmentId: "dept-001",
                supervisorId: "hod-001",
            },
            studentId: "user-123",
            transcriptStatus: "PENDING",
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
            hodId: "hod-001",
            hod: null,
            hodDigitalSignature: "dummy-signature-123",
            encryptedKey: "encrypted-key-abc",
        });
    }, [open]);

    return (
        <div className="flex gap-2">
            <Dialog open={open} onOpenChange={setOpen}>
                <DialogTrigger asChild>
                    <Button
                        variant="ghost"
                        size="sm"
                        className="text-slate-400 bg-slate-400/30 hover:bg-slate-400/50"
                        disabled={inquiry.inquiryStatus !== "APPROVED"}
                    >
                        <FileText className="mr-1 h-4 w-4" />
                        Open Transcript
                    </Button>
                </DialogTrigger>
                <DialogContent className="!w-[60vw] !max-w-[60vw]">
                    <DialogHeader>
                        <DialogTitle className="text-2xl font-semibold">
                            Transcript Details
                        </DialogTitle>
                        <Separator className="mb-2" />
                        <div>
                            {transcript ? (
                                <TranscriptView
                                    transcript={transcript}
                                    cardScale={0.8}
                                />
                            ) : (
                                "loading"
                            )}
                        </div>
                    </DialogHeader>
                </DialogContent>
            </Dialog>
        </div>
    );
}
