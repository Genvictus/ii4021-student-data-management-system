import { Button } from "@/components/ui/button";
import { DialogFooter, DialogHeader } from "@/components/ui/dialog";
import { Separator } from "@/components/ui/separator";
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogTitle,
    DialogTrigger,
} from "@/components/ui/dialog";
import { CheckCircle2, Pen, ShieldCheck, XCircle } from "lucide-react";
import { useState } from "react";
import { toast } from "sonner";
import type { TranscriptWithStudent } from "@/types/TranscriptWithStudent";
import { signTranscript } from "@/use-cases/transcripts/signTranscript";

interface ActionSignTranscriptProps {
    transcript: TranscriptWithStudent;
}

export function ActionSignTranscript({
    transcript,
}: ActionSignTranscriptProps) {
    const [open, setOpen] = useState(false);

    const handleConfirm = async () => {
        const response = await signTranscript(transcript);
        if (response.success) {
            toast.success("Successfully signed transcript");
        } else {
            let errorMsg = "Failed to sign transcript";
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
                    <Button
                        variant="ghost"
                        size="sm"
                        className="text-green-400 bg-green-400/30 hover:bg-green-400/50"
                        disabled={transcript.transcriptStatus !== "PENDING"}
                    >
                        <Pen className="mr-1 h-4 w-4" />
                        Sign
                    </Button>
                </DialogTrigger>
                <DialogContent>
                    <DialogHeader>
                        <DialogTitle className="flex gap-3 items-center">
                            <ShieldCheck />
                            Sign Transcript
                        </DialogTitle>
                        <Separator className="mb-2" />
                        <DialogDescription>
                            {`Are you sure you want to approve this transcript (ID: ${transcript.transcriptId})?`}
                        </DialogDescription>
                    </DialogHeader>
                    <DialogFooter>
                        <Button onClick={handleConfirm}>
                            <CheckCircle2 className="mr-2 h-4 w-4" />
                            Confirm
                        </Button>
                        <Button
                            variant="outline"
                            className="text-muted-foreground"
                            onClick={handleCancel}
                        >
                            <XCircle className="mr-2 h-4 w-4" />
                            Cancel
                        </Button>
                    </DialogFooter>
                </DialogContent>
            </Dialog>
        </div>
    );
}
