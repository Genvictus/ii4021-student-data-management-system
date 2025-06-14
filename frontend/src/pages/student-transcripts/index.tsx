import {
    getStudentTranscripts,
    type GetStudentTranscriptsResponse,
} from "@/use-cases/transcripts/getStudentTranscripts";
import { useLoaderData } from "@tanstack/react-router";
import { TranscriptsTable } from "./TranscriptTable";
import { ActionCreateTranscript } from "./component-actions/ActionCreateTranscript";

export async function studentTranscriptLoader(): Promise<GetStudentTranscriptsResponse> {
    return getStudentTranscripts();
}

export function StudentTranscript() {
    const transcriptsResponse = useLoaderData({ from: "/student-transcripts" });

    const transcripts =
        transcriptsResponse.success && transcriptsResponse.data
            ? transcriptsResponse.data
            : [];

    return (
        <div className="p-10">
            <div className="flex items-center justify-between pb-3">
                <h1 className="text-2xl font-semibold">
                    View Student Transcripts
                </h1>
                <div className="flex gap-2">
                    <ActionCreateTranscript />
                </div>
            </div>
            <TranscriptsTable transcripts={transcripts} />
        </div>
    );
}
