"use client";

import { useState } from "react";
import type { TranscriptActionsProps } from "../TranscriptActions";
import { Pencil, Trash2, Plus } from "lucide-react";
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { calculateGpa } from "@/lib/calculateGpa";
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table";
import { Separator } from "@/components/ui/separator";
import { Input } from "@/components/ui/input";
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from "@/components/ui/select";

export function ActionUpdateTranscript(props: TranscriptActionsProps) {
    const { transcript } = props;
    const [open, setOpen] = useState(false);
    const [data, setData] = useState(
        structuredClone(transcript.transcriptData)
    );

    const removeEntry = (index: number) => {
        setData((prev) => prev.filter((_, i) => i !== index));
    };

    function addEntry() {
        setData((prev) => [
            ...prev,
            {
                courseCode: "",
                credits: 0,
                score: "A" as const,
            },
        ]);
    }

    const updateEntry = (
        index: number,
        field: keyof (typeof data)[0],
        value: string | number
    ) => {
        setData((prev) =>
            prev.map((entry, i) =>
                i === index ? { ...entry, [field]: value } : entry
            )
        );
    };

    const handleConfirm = () => {
        console.log("Updated transcriptData:", data);
        setOpen(false);
    };

    const handleCancel = () => {
        setData(structuredClone(transcript.transcriptData));
        setOpen(false);
    };

    return (
        <div className="flex gap-2">
            <Dialog open={open} onOpenChange={setOpen}>
                <DialogTrigger asChild>
                    <Button variant="default" size="sm">
                        <Pencil className="w-4 h-4 mr-2" />
                        Update
                    </Button>
                </DialogTrigger>
                <DialogContent className="!w-[60vw] !max-w-[60vw]">
                    <DialogHeader>
                        <DialogTitle className="text-2xl">
                            Update Transcript
                        </DialogTitle>
                        <Separator />
                        <DialogDescription>
                            <div className="space-y-4 text-base">
                                <div>
                                    <strong>Student ID:</strong>{" "}
                                    {transcript.student.userId}
                                </div>
                                <div>
                                    <strong>Student:</strong>{" "}
                                    {transcript.student.fullName}
                                </div>
                                <div>
                                    <strong>Status:</strong>{" "}
                                    {transcript.transcriptStatus}
                                </div>
                                <div>
                                    <strong>GPA:</strong> {calculateGpa(data)}
                                </div>

                                <div>
                                    <strong>Courses:</strong>
                                    <Table className="mt-2">
                                        <TableHeader>
                                            <TableRow className="text-center">
                                                <TableHead>
                                                    Course Code
                                                </TableHead>
                                                <TableHead>Credits</TableHead>
                                                <TableHead>Score</TableHead>
                                                <TableHead>Action</TableHead>
                                            </TableRow>
                                        </TableHeader>
                                        <TableBody>
                                            {data.map((entry, index) => (
                                                <TableRow
                                                    key={index}
                                                    className="text-center"
                                                >
                                                    <TableCell>
                                                        <Input
                                                            value={
                                                                entry.courseCode
                                                            }
                                                            onChange={(e) =>
                                                                updateEntry(
                                                                    index,
                                                                    "courseCode",
                                                                    e.target
                                                                        .value
                                                                )
                                                            }
                                                            className="text-center"
                                                        />
                                                    </TableCell>
                                                    <TableCell>
                                                        <Input
                                                            type="number"
                                                            min={0}
                                                            value={
                                                                entry.credits
                                                            }
                                                            onChange={(e) =>
                                                                updateEntry(
                                                                    index,
                                                                    "credits",
                                                                    +e.target
                                                                        .value
                                                                )
                                                            }
                                                            className="text-center"
                                                        />
                                                    </TableCell>
                                                    <TableCell>
                                                        <Select
                                                            value={entry.score}
                                                            onValueChange={(
                                                                value
                                                            ) =>
                                                                updateEntry(
                                                                    index,
                                                                    "score",
                                                                    value
                                                                )
                                                            }
                                                        >
                                                            <SelectTrigger className="w-[100px] mx-auto">
                                                                <SelectValue placeholder="Score" />
                                                            </SelectTrigger>
                                                            <SelectContent>
                                                                {[
                                                                    "A",
                                                                    "AB",
                                                                    "B",
                                                                    "BC",
                                                                    "C",
                                                                    "D",
                                                                    "E",
                                                                ].map(
                                                                    (grade) => (
                                                                        <SelectItem
                                                                            key={
                                                                                grade
                                                                            }
                                                                            value={
                                                                                grade
                                                                            }
                                                                        >
                                                                            {
                                                                                grade
                                                                            }
                                                                        </SelectItem>
                                                                    )
                                                                )}
                                                            </SelectContent>
                                                        </Select>
                                                    </TableCell>
                                                    <TableCell>
                                                        <Button
                                                            variant="destructive"
                                                            size="icon"
                                                            onClick={() =>
                                                                removeEntry(
                                                                    index
                                                                )
                                                            }
                                                        >
                                                            <Trash2 className="w-4 h-4" />
                                                        </Button>
                                                    </TableCell>
                                                </TableRow>
                                            ))}
                                        </TableBody>
                                    </Table>
                                    <div className="flex justify-end mt-4">
                                        <Button
                                            variant="outline"
                                            size="sm"
                                            onClick={addEntry}
                                        >
                                            <Plus className="w-4 h-4 mr-1" />{" "}
                                            Add Course
                                        </Button>
                                    </div>
                                </div>

                                <div className="flex justify-end gap-2 pt-4">
                                    <Button
                                        variant="outline"
                                        onClick={handleCancel}
                                    >
                                        Cancel
                                    </Button>
                                    <Button
                                        variant="default"
                                        onClick={handleConfirm}
                                    >
                                        Confirm Update
                                    </Button>
                                </div>
                            </div>
                        </DialogDescription>
                    </DialogHeader>
                </DialogContent>
            </Dialog>
        </div>
    );
}
