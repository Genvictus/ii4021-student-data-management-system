import { Button } from "@/components/ui/button";
import type { TranscriptAccessInquiry } from "@/types/TranscriptAccessInquiry";
import {
    Dialog,
    DialogContent,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
} from "@/components/ui/dialog";
import { Separator } from "@/components/ui/separator";
import { User, Users } from "lucide-react";
import { useState } from "react";

interface ActionViewParticipantsProps {
    inquiry: TranscriptAccessInquiry;
}

export function ActionViewParticipants({
    inquiry,
}: ActionViewParticipantsProps) {
    const [open, setOpen] = useState(false);

    return (
        <div className="flex gap-2">
            <Dialog open={open} onOpenChange={setOpen}>
                <DialogTrigger asChild>
                    <Button
                        variant="ghost"
                        size="sm"
                        className="text-foreground border border-border bg-transparent hover:bg-border/20"
                    >
                        <Users className="mr-1 h-4 w-4" />
                        View Participants
                    </Button>
                </DialogTrigger>
                <DialogContent>
                    <DialogHeader>
                        <DialogHeader>
                            <DialogTitle className="flex gap-3 items-center">
                                <User />
                                Participants
                            </DialogTitle>
                        </DialogHeader>
                        <Separator className="mb-2" />
                        <ul className="space-y-2 pt-2">
                            {inquiry.participants.length > 0 ? (
                                inquiry.participants.map((p) => (
                                    <li
                                        key={p.id}
                                        className="rounded border border-border px-3 py-2 text-sm"
                                    >
                                        {p.id}
                                    </li>
                                ))
                            ) : (
                                <p className="text-sm text-muted-foreground">
                                    No participants yet.
                                </p>
                            )}
                        </ul>
                    </DialogHeader>
                </DialogContent>
            </Dialog>
        </div>
    );
}
