import { useLoaderData } from "@tanstack/react-router";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { TriangleAlert } from "lucide-react";

import { TranscriptView } from "../student-transcripts/component-actions/TranscriptView";
import { ActionPrintTranscriptPdf } from "./component-actions/ActionPrintTranscriptPdf";
import { ActionViewTranscriptPdf } from "./component-actions/ActionViewTranscriptPdf";
import {
    getStudentTranscripts,
    type GetStudentTranscriptsResponse,
} from "@/use-cases/transcripts/getStudentTranscripts";

export async function myTranscriptLoader(): Promise<GetStudentTranscriptsResponse> {
    return getStudentTranscripts();
}

export function MyTranscript() {
    const response = useLoaderData({ from: "/my-transcript" });

    return (
        <div className="px-10 py-10">
            <h1 className="text-2xl font-semibold py-5">My Transcript</h1>

            {response.success && response.data && response.data.length > 0 ? (
                <>
                    <TranscriptView transcript={response.data[0]} />
                    <div className="space-x-3">
                        <ActionPrintTranscriptPdf
                            transcript={response.data[0]}
                        />
                        <ActionViewTranscriptPdf />
                    </div>
                </>
            ) : response.success && response.data?.length === 0 ? (
                <Alert variant="default">
                    <TriangleAlert className="h-4 w-4" />
                    <AlertTitle>No Transcript Found</AlertTitle>
                    <AlertDescription>
                        You don't have any transcript data yet.
                    </AlertDescription>
                </Alert>
            ) : (
                <Alert variant="destructive">
                    <TriangleAlert className="h-4 w-4" />
                    <AlertTitle>Failed to Load Transcript</AlertTitle>
                    <AlertDescription>
                        {response.message ?? "An unknown error occurred."}
                    </AlertDescription>
                </Alert>
            )}
        </div>
    );
}
