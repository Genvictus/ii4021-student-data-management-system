import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table";
import type { TranscriptWithStudent } from "@/types/TranscriptWithStudent";
import { TranscriptActions } from "./TranscriptActions";
import { Badge } from "@/components/ui/badge";
import { StatusBadge } from "@/components/ui/status-badge";

interface TranscriptsTableProps {
    transcripts: TranscriptWithStudent[];
}

export function TranscriptsTable(props: TranscriptsTableProps) {
    const { transcripts } = props;
    return (
        <Table>
            <TableHeader>
                <TableRow className="bg-slate-800 text-slate-100">
                    <TableHead className="uppercase font-bold tracking-wide">
                        Student ID
                    </TableHead>
                    <TableHead className="uppercase font-bold tracking-wide">
                        Student Name
                    </TableHead>
                    <TableHead className="uppercase font-bold tracking-wide">
                        Department ID
                    </TableHead>
                    <TableHead className="uppercase font-bold tracking-wide">
                        Status
                    </TableHead>
                    <TableHead className="uppercase font-bold tracking-wide">
                        Actions
                    </TableHead>
                </TableRow>
            </TableHeader>

            <TableBody>
                {transcripts.length > 0 ? (
                    transcripts.map((transcript) => (
                        <TableRow key={transcript.transcriptId}>
                            <TableCell className="font-medium">
                                {transcript.studentId}
                            </TableCell>
                            <TableCell>{transcript.student.fullName}</TableCell>
                            <TableCell>
                                {transcript.student.departmentId}
                            </TableCell>
                            <TableCell>
                                <StatusBadge
                                    status={transcript.transcriptStatus}
                                />
                            </TableCell>
                            <TableCell>
                                <TranscriptActions transcript={transcript} />
                            </TableCell>
                        </TableRow>
                    ))
                ) : (
                    <TableRow>
                        <TableCell
                            colSpan={6}
                            className="text-center text-muted-foreground"
                        >
                            No transcripts available
                        </TableCell>
                    </TableRow>
                )}
            </TableBody>
        </Table>
    );
}
