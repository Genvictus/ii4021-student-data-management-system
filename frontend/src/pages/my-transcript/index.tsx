import { Button } from "@/components/ui/button";
import type { TranscriptWithStudent } from "@/types/TranscriptWithStudent";
import { printTranscriptPdf } from "@/use-cases/transcripts/printTranscriptPdf";
import { useLoaderData } from "@tanstack/react-router";
import { Printer } from "lucide-react";
import { TranscriptView } from "../student-transcripts/component-actions/TranscriptView";

export async function myTranscriptLoader() {
    const mockData: TranscriptWithStudent = {
        transcriptId: "tx123",
        student: {
            userId: "u1",
            email: "student@example.com",
            fullName: "John Doe",
            role: "STUDENT",
            publicKey: "abc123",
            department: null,
            departmentId: "dep1",
            supervisorId: "s1",
        },
        studentId: "stu123",
        transcriptStatus: "APPROVED",
        transcriptData: [
            { courseCode: "CS101", credits: 3, score: "A" },
            { courseCode: "MA101", credits: 4, score: "B" },
        ],
        hodId: "hod456",
        hod: null,
        hodDigitalSignature: "signature123",
        encryptedKey: "xyz",
    };
    return mockData;
}

export function MyTranscript() {
    const transcript = useLoaderData({ from: "/my-transcript" });
    const handlePrintTranscript = () => {
        printTranscriptPdf(transcript);
    };

    return (
        <div className="px-10 py-10">
            <TranscriptView transcript={transcript} />
            <Button
                variant="outline"
                size="sm"
                className="mt-5"
                onClick={handlePrintTranscript}
            >
                <Printer className="w-4 h-4 mr-1" />
                Print PDF
            </Button>
        </div>
    );
}
