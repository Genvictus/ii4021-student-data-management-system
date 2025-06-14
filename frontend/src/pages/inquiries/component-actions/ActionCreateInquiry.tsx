import { Button } from "@/components/ui/button";
import { DialogFooter, DialogHeader } from "@/components/ui/dialog";
import { Separator } from "@/components/ui/separator";
import {
    Dialog,
    DialogContent,
    DialogTitle,
    DialogTrigger,
} from "@/components/ui/dialog";
import { CheckCircle2, PlusCircle, XCircle } from "lucide-react";
import { useState } from "react";

// interface ActionCreateInquiryProps {}

export function ActionCreateInquiry() {
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
                    <Button variant="default" size="sm">
                        <PlusCircle className="w-4 h-4 mr-1" />
                        Create Inquiry
                    </Button>
                </DialogTrigger>
                <DialogContent>
                    <DialogHeader>
                        <DialogTitle className="flex gap-3 items-center text-2xl font-semibold">
                            <PlusCircle />
                            Create Inquiry
                        </DialogTitle>
                        <Separator className="mb-2" />
                    </DialogHeader>
                    <div>Form</div>
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
