import { calculateGpa } from "@/lib/calculateGpa";
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table";
import type { TranscriptWithStudent } from "@/types/TranscriptWithStudent";
import { Card } from "@/components/ui/card";
import { ProfileItem } from "@/pages/home/ProfileItem";
import { User, GraduationCap, BadgeCheck, IdCard } from "lucide-react";
import { StatusBadge } from "@/components/ui/status-badge";
import { verifyTranscript } from "@/use-cases/transcripts/verifyTranscript";
import { useEffect, useState } from "react";

interface TranscriptViewProps {
    transcript: TranscriptWithStudent;
    cardScale?: number;
}

export function TranscriptView(props: TranscriptViewProps) {
    const { transcript, cardScale } = props;
    return (
        <div className="space-y-4 text-base">
            <div
                style={{
                    transform: cardScale ? `scale(${cardScale})` : "none",
                    transformOrigin: "top left",
                }}
            >
                <TranscriptViewCard transcript={transcript} />
            </div>
            <div>
                <strong className="text-xl">Courses:</strong>
                <TranscriptViewTable transcript={transcript} />
            </div>
        </div>
    );
}

interface TranscriptViewCardProps {
    transcript: TranscriptWithStudent;
}

export function TranscriptViewCard(props: TranscriptViewCardProps) {
    const { transcript } = props;
    const [verificationStatus, setVerifivationStatus] = useState<
        "VERIFIED" | "UNVERIFIED"
    >("UNVERIFIED");

    const getTranscriptVerificationStatus = async () => {
        const response = await verifyTranscript(transcript);

        if (response.success && response.data) {
            setVerifivationStatus("VERIFIED");
        }
    };

    useEffect(() => {
        getTranscriptVerificationStatus();
    }, [transcript]);

    return (
        <Card className="flex-1 shadow-xl border p-4 w-sm">
            <ProfileItem
                icon={IdCard}
                label="Student ID"
                value={transcript.studentId}
            />
            <ProfileItem
                icon={User}
                label="Student Name"
                value={transcript.student.fullName}
            />
            <ProfileItem
                icon={GraduationCap}
                label="GPA"
                value={calculateGpa(transcript.transcriptData).gpa}
            />
            <ProfileItem
                icon={BadgeCheck}
                label="Transcript Status"
                value={<StatusBadge status={transcript.transcriptStatus} />}
            />
            <ProfileItem
                icon={BadgeCheck}
                label="Verification Status"
                value={<StatusBadge status={verificationStatus} />}
            />
        </Card>
    );
}

interface TranscriptViewTableProps {
    transcript: TranscriptWithStudent;
}
function TranscriptViewTable(props: TranscriptViewTableProps) {
    const { transcript } = props;

    return (
        <Table>
            <TableHeader>
                <TableRow>
                    <TableHead className="w-[150px] text-center py-3">
                        Course Code
                    </TableHead>
                    <TableHead className="text-center py-3">Credits</TableHead>
                    <TableHead className="text-center py-3">Score</TableHead>
                </TableRow>
            </TableHeader>
            <TableBody>
                {transcript.transcriptData.map((entry, index) => (
                    <TableRow key={index} className="text-center">
                        <TableCell className="py-3">
                            {entry.courseCode}
                        </TableCell>
                        <TableCell className="py-3">{entry.credits}</TableCell>
                        <TableCell className="py-3">{entry.score}</TableCell>
                    </TableRow>
                ))}
            </TableBody>
        </Table>
    );
}
