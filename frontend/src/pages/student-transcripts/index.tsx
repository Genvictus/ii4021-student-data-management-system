import {
    getStudentTranscripts,
    type GetStudentTranscriptsResponse,
} from "@/use-cases/transcripts/getStudentTranscripts";
import { useLoaderData } from "@tanstack/react-router";
import { TranscriptsTable } from "./TranscriptTable";

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
            <h1 className="pb-3 text-2xl">View Student Transcripts</h1>
            <TranscriptsTable transcripts={transcripts} />
        </div>
    );
}
