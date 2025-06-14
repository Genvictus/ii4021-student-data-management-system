import { useEffect, useState } from "react";
import { Button } from "@/components/ui/button";
import { DialogFooter, DialogHeader } from "@/components/ui/dialog";
import { Separator } from "@/components/ui/separator";
import {
    Dialog,
    DialogContent,
    DialogTitle,
    DialogTrigger,
} from "@/components/ui/dialog";
import { CheckCircle2, PlusCircle, Search, XCircle } from "lucide-react";
import { Label } from "@radix-ui/react-label";
import { Input } from "@/components/ui/input";

export function ActionCreateInquiry() {
    const [open, setOpen] = useState(false);
    const [studentId, setStudentId] = useState("");
    const [transcriptId, setTranscriptId] = useState<string | null>(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (open) {
            setStudentId("");
            setTranscriptId(null);
            setLoading(false);
        }
    }, [open]);

    const handleStudentIdChange: React.ChangeEventHandler<HTMLInputElement> = (
        e
    ) => {
        setStudentId(e.target.value);
    };

    const handleSearchClick = async () => {
        setLoading(true);
        setError(null);
        setTranscriptId(null);

        await new Promise((resolve) => setTimeout(resolve, 1000));

        const mockTranscriptId = `T-${studentId.toUpperCase()}`;
        setTranscriptId(mockTranscriptId);
        setLoading(false);
    };

    const handleConfirm = () => {
        if (!transcriptId) return;
        console.log(`Creating inquiry for transcript: ${transcriptId}`);
        setOpen(false);
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

                    <div>
                        <Label htmlFor="student-id">
                            Look up student's transcript ID
                        </Label>
                        <div className="flex gap-3 items-center mt-3">
                            <Input
                                id="student-id"
                                placeholder="Student ID"
                                onChange={handleStudentIdChange}
                                value={studentId}
                            />
                            <Button
                                type="button"
                                onClick={handleSearchClick}
                                disabled={loading}
                            >
                                <Search className="mr-1 h-4 w-4" />
                                {loading ? "Searching..." : "Search"}
                            </Button>
                        </div>

                        {loading && (
                            <p className="text-sm text-muted-foreground mt-2">
                                Searching...
                            </p>
                        )}
                        {error && (
                            <p className="text-sm text-red-500 mt-2">{error}</p>
                        )}
                        {transcriptId && (
                            <p className="text-sm text-green-600 mt-2">
                                Found transcript ID:{" "}
                                <span className="font-semibold">
                                    {transcriptId}
                                </span>
                            </p>
                        )}
                    </div>

                    {transcriptId && (
                        <div>
                            Proceed to create inquiry to access this transcript?
                        </div>
                    )}

                    <DialogFooter>
                        <Button
                            onClick={handleConfirm}
                            disabled={!transcriptId}
                        >
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
