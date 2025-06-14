import { StatusBadge } from "@/components/ui/status-badge";
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table";
import type { TranscriptAccessInquiry } from "@/types/TranscriptAccessInquiry";
import { getUserProfile } from "@/lib/getUserProfile";
import { ActionViewParticipants } from "./component-actions/ActionViewParticipants";
import { ActionApproveInquiry } from "./component-actions/ActionApproveInquiry";
import { ActionOpenTranscript } from "./component-actions/ActionOpenTranscript";
import { ActionRejectInquiry } from "./component-actions/ActionRejectInquiry";
import { ActionJoinInquiry } from "./component-actions/ActionJoinInquiry";

interface InquiriesTableProps {
    inquiries: TranscriptAccessInquiry[];
}

export function InquiriesTable(props: InquiriesTableProps) {
    const { inquiries } = props;

    return (
        <Table>
            <TableHeader>
                <TableRow className="bg-slate-800 text-slate-100">
                    <TableHead className="uppercase font-bold tracking-wide">
                        Inquiry ID
                    </TableHead>
                    <TableHead className="uppercase font-bold tracking-wide">
                        Transcript ID
                    </TableHead>
                    <TableHead className="uppercase font-bold tracking-wide">
                        Requester ID
                    </TableHead>
                    <TableHead className="uppercase font-bold tracking-wide">
                        Requestee ID
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
                {inquiries.length > 0 ? (
                    inquiries.map((inquiry) => (
                        <TableRow key={inquiry.inquiryId}>
                            <TableCell>{inquiry.inquiryId}</TableCell>
                            <TableCell>{inquiry.transcriptId}</TableCell>
                            <TableCell>{inquiry.requesterId}</TableCell>
                            <TableCell>{inquiry.requesteeId}</TableCell>
                            <TableCell>
                                <StatusBadge status={inquiry.inquiryStatus} />
                            </TableCell>
                            <TableCell>
                                <InquiryActions inquiry={inquiry} />
                            </TableCell>
                        </TableRow>
                    ))
                ) : (
                    <TableRow>
                        <TableCell
                            colSpan={6}
                            className="text-center text-muted-foreground"
                        >
                            No inquiries available
                        </TableCell>
                    </TableRow>
                )}
            </TableBody>
        </Table>
    );
}

interface InquiryActionsProps {
    inquiry: TranscriptAccessInquiry;
}

export function InquiryActions({ inquiry }: InquiryActionsProps) {
    const userProfile = getUserProfile();
    if (!userProfile) return null;

    const isRequester = userProfile.userId === inquiry.requesterId;
    const isRequestee = userProfile.userId === inquiry.requesteeId;
    const isParticipant = inquiry.participants.some(
        (el) => el.id === userProfile.userId
    );

    let Actions: React.ReactNode[] = [];

    if (isRequester || isParticipant) {
        Actions = [
            <ActionViewParticipants key="view" inquiry={inquiry} />,
            <ActionOpenTranscript key="open" inquiry={inquiry} />,
        ];
    } else if (isRequestee) {
        Actions = [
            <ActionApproveInquiry key="approve" inquiry={inquiry} />,
            <ActionRejectInquiry key="reject" inquiry={inquiry} />,
        ];
    } else {
        Actions = [
            <ActionViewParticipants key="view" inquiry={inquiry} />,
            <ActionJoinInquiry key="join" inquiry={inquiry} />,
        ];
    }

    return <div className="flex gap-2">{Actions}</div>;
}
