import {
    getStudentTranscripts,
    type GetStudentTranscriptsResponse,
} from "@/use-cases/transcripts/getStudentTranscripts";
import { useLoaderData } from "@tanstack/react-router";
import { TranscriptsTable } from "./TranscriptTable";
import { ActionCreateTranscript } from "./component-actions/ActionCreateTranscript";
import { Alert, AlertTitle, AlertDescription } from "@/components/ui/alert";
import { TriangleAlert } from "lucide-react";

export async function studentTranscriptLoader(): Promise<GetStudentTranscriptsResponse> {
    return getStudentTranscripts();
}

export function StudentTranscript() {
    const response = useLoaderData({ from: "/student-transcripts" });

    return (
        <div className="p-10">
            <div className="flex items-center justify-between pb-5">
                <h1 className="text-2xl font-semibold">
                    View Student Transcripts
                </h1>
                <div className="flex gap-2">
                    <ActionCreateTranscript />
                </div>
            </div>

            {response.success && response.data && response.data.length > 0 ? (
                <TranscriptsTable transcripts={response.data} />
            ) : response.success && response.data?.length === 0 ? (
                <Alert variant="default">
                    <TriangleAlert className="h-4 w-4" />
                    <AlertTitle>No Transcripts Found</AlertTitle>
                    <AlertDescription>
                        You don't have any transcript records yet.
                    </AlertDescription>
                </Alert>
            ) : (
                <Alert variant="destructive">
                    <TriangleAlert className="h-4 w-4" />
                    <AlertTitle>Failed to Load Transcripts</AlertTitle>
                    <AlertDescription>
                        {response.message ??
                            "An unknown error occurred while fetching transcripts."}
                    </AlertDescription>
                </Alert>
            )}
        </div>
    );
}
