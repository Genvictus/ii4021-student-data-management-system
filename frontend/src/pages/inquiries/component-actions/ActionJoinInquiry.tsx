import { Button } from "@/components/ui/button";
import { DialogFooter, DialogHeader } from "@/components/ui/dialog";
import { Separator } from "@/components/ui/separator";
import type { TranscriptAccessInquiry } from "@/types/TranscriptAccessInquiry";
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogTitle,
    DialogTrigger,
} from "@/components/ui/dialog";
import { CheckCircle2, LogIn, User, XCircle } from "lucide-react";
import { useState } from "react";

interface ActionJoinInquiryProps {
    inquiry: TranscriptAccessInquiry;
}

export function ActionJoinInquiry(props: ActionJoinInquiryProps) {
    const { inquiry } = props;
    const [open, setOpen] = useState(false);

    const handleConfirm = () => {
        // Will implement later
    };

    const handleCancel = () => {
        setOpen(false);
    };

    return (
        <div className="flex gap-2">
            <Dialog open={open} onOpenChange={setOpen}>
                <DialogTrigger asChild>
                    <Button
                        variant="ghost"
                        size="sm"
                        className="text-blue-400 bg-blue-400/30 hover:bg-blue-400/50"
                    >
                        <LogIn className="mr-1 h-4 w-4" />
                        Join
                    </Button>
                </DialogTrigger>
                <DialogContent>
                    <DialogHeader>
                        <DialogTitle className="flex gap-3 items-center">
                            <User />
                            Participants
                        </DialogTitle>
                        <Separator className="mb-2" />
                        <DialogDescription>
                            {`Are you sure you want to join this inquiry (ID: ${inquiry.inquiryId})?`}
                        </DialogDescription>
                    </DialogHeader>
                    <DialogFooter>
                        <Button onClick={handleConfirm}>
                            <CheckCircle2 className="mr-2 h-4 w-4" />
                            Confirm
                        </Button>
                        <Button
                            variant="outline"
                            className="text-muted-foreground"
                            onClick={handleCancel}
                        >
                            <XCircle className="mr-2 h-4 w-4" />
                            Cancel
                        </Button>
                    </DialogFooter>
                </DialogContent>
            </Dialog>
        </div>
    );
}
