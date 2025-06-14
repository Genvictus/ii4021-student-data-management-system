import type { TranscriptWithStudent } from "@/types/TranscriptWithStudent";
import { useLoaderData } from "@tanstack/react-router";
import { TranscriptView } from "../student-transcripts/component-actions/TranscriptView";
import { ActionPrintTranscriptPdf } from "./component-actions/ActionPrintTranscriptPdf";
import { ActionViewTranscriptPdf } from "./component-actions/ActionViewTranscriptPdf";

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

    return (
        <div className="px-10 py-10">
            <TranscriptView transcript={transcript} />
            <div className="space-x-3">
                <ActionPrintTranscriptPdf transcript={transcript} />
                <ActionViewTranscriptPdf />
            </div>
        </div>
    );
}
