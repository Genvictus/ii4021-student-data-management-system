import { useEffect, useState } from "react";
import { Printer } from "lucide-react";
import { Button } from "@/components/ui/button";
import {
    Dialog,
    DialogTrigger,
    DialogContent,
    DialogHeader,
    DialogTitle,
} from "@/components/ui/dialog";
import { Switch } from "@/components/ui/switch";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
import { toast } from "sonner";
import { printTranscriptPdf } from "@/use-cases/transcripts/printTranscriptPdf";
import type { TranscriptWithStudent } from "@/types/TranscriptWithStudent";

interface ActionPrintTranscriptPdfProps {
    transcript: TranscriptWithStudent;
}

export function ActionPrintTranscriptPdf(props: ActionPrintTranscriptPdfProps) {
    const { transcript } = props;
    const [open, setOpen] = useState(false);
    const [encryptPdf, setEncryptPdf] = useState(true);
    const [encryptionKey, setEncryptionKey] = useState<string>("");

    useEffect(() => {
        if (open) {
            setEncryptPdf(false);
            setEncryptionKey("");
        }
    }, [open]);

    const handleSwitchChange = () => {
        setEncryptPdf((prev) => !prev);
        setEncryptionKey("");
    };

    const handleEncryptionKeyChange: React.ChangeEventHandler<
        HTMLInputElement
    > = (e) => {
        setEncryptionKey(e.target.value);
    };

    const handlePrintPdf = () => {
        console.log(`printing pdf with encryption key ${encryptionKey}`);
        printTranscriptPdf(transcript, encryptionKey);
        setOpen(false);
        toast.success("Successfully print transcript PDF");
    };

    return (
        <Dialog open={open} onOpenChange={setOpen}>
            <DialogTrigger asChild>
                <Button variant="default" className="mt-5" size="lg">
                    <Printer className="w-4 h-4 mr-1" />
                    Print PDF
                </Button>
            </DialogTrigger>
            <DialogContent>
                <DialogHeader>
                    <DialogTitle>Print Transcript PDF</DialogTitle>
                </DialogHeader>
                <div className="space-y-5">
                    <div className="flex items-center space-x-2">
                        <Switch
                            id="encrypt-pdf"
                            checked={encryptPdf}
                            onCheckedChange={handleSwitchChange}
                        />
                        <Label htmlFor="encrypt-pdf">Encrypt PDF</Label>
                    </div>
                    {encryptPdf && (
                        <div className="space-y-2">
                            <Label htmlFor="encryption-key">
                                Encryption Key
                            </Label>
                            <Input
                                id="encryption-key"
                                value={encryptionKey}
                                onChange={handleEncryptionKeyChange}
                            />
                        </div>
                    )}
                    <div className="flex gap-3">
                        <Button
                            variant="default"
                            size="lg"
                            onClick={handlePrintPdf}
                        >
                            <Printer className="w-4 h-4 mr-1" />
                            Print PDF
                        </Button>
                    </div>
                </div>
            </DialogContent>
        </Dialog>
    );
}
