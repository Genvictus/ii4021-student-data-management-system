import { Button } from "@/components/ui/button";
import type { TranscriptAccessInquiry } from "@/types/TranscriptAccessInquiry";
import { FileText } from "lucide-react";

interface ActionOpenTranscriptProps {
    inquiry: TranscriptAccessInquiry;
}

export function ActionOpenTranscript(props: ActionOpenTranscriptProps) {
    const { inquiry } = props;

    return (
        <Button
            variant="ghost"
            size="sm"
            className="text-slate-400 bg-slate-400/30 hover:bg-slate-400/50"
            disabled={inquiry.inquiryStatus !== "APPROVED"}
        >
            <FileText className="mr-1 h-4 w-4" />
            Open Transcript
        </Button>
    );
}
