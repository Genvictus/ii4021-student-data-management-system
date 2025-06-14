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
import { User, Badge, GraduationCap, BadgeCheck } from "lucide-react";
import { StatusBadge } from "@/components/ui/status-badge";

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
                <strong>Courses:</strong>
                <TranscriptViewTable transcript={transcript} />
            </div>
        </div>
    );
}

interface TranscriptViewCardProps {
    transcript: TranscriptWithStudent;
}

function TranscriptViewCard(props: TranscriptViewCardProps) {
    const { transcript } = props;

    return (
        <Card className="flex-1 shadow-xl border p-4 w-sm">
            <ProfileItem
                icon={Badge}
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
                value={calculateGpa(transcript.transcriptData)}
            />
            <ProfileItem
                icon={BadgeCheck}
                label="Transcript Status"
                value={<StatusBadge status={transcript.transcriptStatus} />}
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
                <TableRow className="p-2">
                    <TableHead className="w-[150px] text-center">
                        Course Code
                    </TableHead>
                    <TableHead className="text-center">Credits</TableHead>
                    <TableHead className="text-center">Score</TableHead>
                </TableRow>
            </TableHeader>
            <TableBody>
                {transcript.transcriptData.map((entry, index) => (
                    <TableRow key={index} className="text-center">
                        <TableCell>{entry.courseCode}</TableCell>
                        <TableCell>{entry.credits}</TableCell>
                        <TableCell>{entry.score}</TableCell>
                    </TableRow>
                ))}
            </TableBody>
        </Table>
    );
}
