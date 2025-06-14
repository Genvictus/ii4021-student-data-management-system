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
import { getStudentTranscriptById } from "@/use-cases/transcripts/getStudentTranscriptById";
import { reconstructTranscript } from "@/use-cases/transcripts/inquiries/reconstructTranscript";
import { FileText, AlertTriangle } from "lucide-react";
import { useEffect, useState } from "react";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { Skeleton } from "@/components/ui/skeleton";

interface ActionOpenTranscriptProps {
    inquiry: TranscriptAccessInquiry;
}

export function ActionOpenTranscript(props: ActionOpenTranscriptProps) {
    const { inquiry } = props;
    const [open, setOpen] = useState(false);
    const [transcript, setTranscript] = useState<TranscriptWithStudent | null>(
        null
    );
    const [errorMessage, setErrorMessage] = useState<string | null>(null);

    const prepareTranscript = async () => {
        setTranscript(null);
        setErrorMessage(null);

        const reconstructedTranscriptData = reconstructTranscript(inquiry);
        const response = await getStudentTranscriptById(inquiry.transcriptId);

        if (!response.success || !response.data) {
            let errorMsg = "Failed to open student transcript";
            if (response.message) {
                errorMsg += `: ${response.message}`;
            }
            setErrorMessage(errorMsg);
            return;
        }

        setTranscript({
            ...response.data,
            transcriptData: reconstructedTranscriptData,
        });
    };

    useEffect(() => {
        if (open) {
            prepareTranscript();
        }
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
                    </DialogHeader>
                    {errorMessage && (
                        <Alert variant="destructive" className="mb-4">
                            <AlertTriangle className="h-4 w-4" />
                            <AlertTitle>Error</AlertTitle>
                            <AlertDescription>{errorMessage}</AlertDescription>
                        </Alert>
                    )}
                    {transcript ? (
                        <TranscriptView
                            transcript={transcript}
                            cardScale={0.8}
                        />
                    ) : !errorMessage ? (
                        <div className="space-y-4">
                            <Skeleton className="h-6 w-1/2" />
                            <Skeleton className="h-96 w-full" />
                        </div>
                    ) : null}
                </DialogContent>
            </Dialog>
        </div>
    );
}
