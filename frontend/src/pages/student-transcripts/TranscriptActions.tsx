import type { TranscriptWithStudent } from "@/types/TranscriptWithStudent";
import { ActionViewTranscript } from "./component-actions/ActionViewTranscript";
import { ActionUpdateTranscript } from "./component-actions/ActionUpdateTranscript";
import { getUserProfile } from "@/lib/getUserProfile";
import { ActionSignTranscript } from "./component-actions/ActionSignTranscript";

export interface TranscriptActionsProps {
    transcript: TranscriptWithStudent;
}

export function TranscriptActions({ transcript }: TranscriptActionsProps) {
    const userProfile = getUserProfile();
    if (!userProfile) return;

    const actions =
        userProfile.role === "HOD"
            ? [
                  <ActionViewTranscript transcript={transcript} />,
                  <ActionSignTranscript transcript={transcript} />,
              ]
            : userProfile.role === "SUPERVISOR"
            ? [
                  <ActionViewTranscript transcript={transcript} />,
                  <ActionUpdateTranscript transcript={transcript} />,
              ]
            : [];

    return <div className="flex gap-3">{actions}</div>;
}
