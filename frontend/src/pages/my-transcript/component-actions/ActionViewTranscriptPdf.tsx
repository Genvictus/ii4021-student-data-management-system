import { useEffect, useState } from "react";
import { File, Eye } from "lucide-react";
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
import { useNavigate } from "@tanstack/react-router";

function createPdfUrl(file: File, decrypt: boolean, key: string | null) {
    console.log("Showing PDF", { file, decrypt, key });
    // You can handle decryption logic here if needed
    return URL.createObjectURL(file);
}

export function ActionViewTranscriptPdf() {
    const [open, setOpen] = useState(false);
    const [decryptPdf, setDecryptPdf] = useState(false);
    const [decryptionKey, setDecryptionKey] = useState("");
    const [pdfFile, setPdfFile] = useState<File | null>(null);
    const [pdfUrl, setPdfUrl] = useState<string | null>(null);

    const navigate = useNavigate();

    useEffect(() => {
        if (open) {
            setDecryptPdf(false);
            setDecryptionKey("");
            setPdfFile(null);
            setPdfUrl(null);
        }
    }, [open]);

    const handleFileChange: React.ChangeEventHandler<HTMLInputElement> = (
        e
    ) => {
        const file = e.target.files?.[0] || null;
        setPdfFile(file);
        setPdfUrl(null);
    };

    const handleViewPdf = () => {
        if (!pdfFile) return;

        const url = createPdfUrl(
            pdfFile,
            decryptPdf,
            decryptPdf ? decryptionKey : null
        );

        navigate({
            to: "/my-transcript/view-pdf",
            // @ts-ignore
            state: { pdfUrl: url },
        });

        setOpen(false);
    };

    return (
        <Dialog open={open} onOpenChange={setOpen}>
            <DialogTrigger asChild>
                <Button variant="secondary" className="mt-5" size="lg">
                    <File className="w-4 h-4 mr-1" />
                    View PDF
                </Button>
            </DialogTrigger>
            <DialogContent className="max-w-3xl">
                <DialogHeader>
                    <DialogTitle>View Transcript PDF</DialogTitle>
                </DialogHeader>

                <div className="space-y-5">
                    <div className="space-y-2">
                        <Label htmlFor="pdf-file">PDF File</Label>
                        <Input
                            id="pdf-file"
                            type="file"
                            accept="application/pdf"
                            onChange={handleFileChange}
                        />
                    </div>

                    <div className="flex items-center space-x-2">
                        <Switch
                            id="decrypt-pdf"
                            checked={decryptPdf}
                            onCheckedChange={(val) => {
                                setDecryptPdf(val);
                                setDecryptionKey("");
                            }}
                        />
                        <Label htmlFor="decrypt-pdf">Decrypt PDF</Label>
                    </div>

                    {decryptPdf && (
                        <div className="space-y-2">
                            <Label htmlFor="decryption-key">
                                Decryption Key
                            </Label>
                            <Input
                                id="decryption-key"
                                value={decryptionKey}
                                onChange={(e) =>
                                    setDecryptionKey(e.target.value)
                                }
                            />
                        </div>
                    )}

                    <div className="flex gap-3">
                        <Button
                            variant="default"
                            size="lg"
                            onClick={handleViewPdf}
                            disabled={!pdfFile}
                        >
                            <Eye className="w-4 h-4 mr-1" />
                            View PDF
                        </Button>
                    </div>

                    {pdfUrl && (
                        <div className="border rounded-md overflow-hidden">
                            <iframe
                                src={pdfUrl}
                                width="100%"
                                height="500px"
                                className="w-full rounded-md border"
                            />
                        </div>
                    )}
                </div>
            </DialogContent>
        </Dialog>
    );
}
