import { useLoaderData } from "@tanstack/react-router";
import { InquiriesTable } from "./InquiriesTable";
import {
    getTranscriptAccessInquiries,
    type GetTranscriptAccessInquiryResponse,
} from "@/use-cases/transcripts/inquiries/getTranscriptAccessInquiries";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { TriangleAlert } from "lucide-react";
import { ActionCreateInquiry } from "./component-actions/ActionCreateInquiry";

export async function inquiriesLoader(): Promise<GetTranscriptAccessInquiryResponse> {
    return await getTranscriptAccessInquiries();
}

export function Inquiries() {
    const response = useLoaderData({ from: "/inquiries" });

    return (
        <div className="p-10">
            <div className="flex items-center justify-between pb-5">
                <h1 className="pb-3 text-2xl font-semibold">View Inquiries</h1>
                <div className="flex gap-2">
                    <ActionCreateInquiry />
                </div>
            </div>

            {response.success && response.data && response.data.length > 0 ? (
                <InquiriesTable inquiries={response.data} />
            ) : response.success && response.data?.length === 0 ? (
                <Alert variant="default">
                    <TriangleAlert className="h-4 w-4" />
                    <AlertTitle>No Inquiries</AlertTitle>
                    <AlertDescription>
                        You don't have any inquiries yet.
                    </AlertDescription>
                </Alert>
            ) : (
                <Alert variant="destructive">
                    <TriangleAlert className="h-4 w-4" />
                    <AlertTitle>Failed to Load Inquiries</AlertTitle>
                    <AlertDescription>
                        {response.message ??
                            "An unknown error occurred while fetching inquiries."}
                    </AlertDescription>
                </Alert>
            )}
        </div>
    );
}
