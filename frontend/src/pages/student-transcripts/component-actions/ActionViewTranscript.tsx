import { useState } from "react";
import type { TranscriptActionsProps } from "../TranscriptActions";
import { Eye } from "lucide-react";
import {
    Dialog,
    DialogContent,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Separator } from "@/components/ui/separator";
import { TranscriptView } from "./TranscriptView";

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
                        <Separator className="mb-2" />
                        <div>
                            <TranscriptView
                                transcript={transcript}
                                cardScale={0.8}
                            />
                        </div>
                    </DialogHeader>
                </DialogContent>
            </Dialog>
        </div>
    );
}
