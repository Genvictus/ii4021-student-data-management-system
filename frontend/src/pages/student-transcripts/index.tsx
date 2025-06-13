import { PlusCircle, Printer } from "lucide-react";
import { Button } from "@/components/ui/button";
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

    const handleCreateTranscript = () => {};

    const handlePrintTranscript = () => {};

    return (
        <div className="p-10">
            <div className="flex items-center justify-between pb-3">
                <h1 className="text-2xl font-semibold">
                    View Student Transcripts
                </h1>
                <div className="flex gap-2">
                    <Button
                        variant="outline"
                        size="sm"
                        onClick={handlePrintTranscript}
                    >
                        <Printer className="w-4 h-4 mr-1" />
                        Print PDF
                    </Button>
                    <Button
                        variant="default"
                        size="sm"
                        onClick={handleCreateTranscript}
                    >
                        <PlusCircle className="w-4 h-4 mr-1" />
                        Create Transcript
                    </Button>
                </div>
            </div>
            <TranscriptsTable transcripts={transcripts} />
        </div>
    );
}
