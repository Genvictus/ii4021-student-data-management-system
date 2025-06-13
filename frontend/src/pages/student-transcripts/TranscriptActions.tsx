import type { TranscriptWithStudent } from "@/types/TranscriptWithStudent";
import { ActionViewTranscript } from "./component-actions/ActionViewTranscript";
import { ActionUpdateTranscript } from "./component-actions/ActionUpdateTranscript";

export interface TranscriptActionsProps {
    transcript: TranscriptWithStudent;
}

export function TranscriptActions({ transcript }: TranscriptActionsProps) {
    return (
        <div className="flex gap-3">
            <ActionViewTranscript transcript={transcript} />
            <ActionUpdateTranscript transcript={transcript} />
        </div>
    );
}
