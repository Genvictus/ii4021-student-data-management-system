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
import type { TranscriptEntry } from "@/types/TranscriptWithStudent";

interface TranscriptCourseEditTableProps {
    data: TranscriptEntry[];
    updateEntry: (
        index: number,
        field: keyof TranscriptEntry,
        value: string | number
    ) => void;
    removeEntry: (index: number) => void;
}

export function TranscriptCourseEditTable(
    props: TranscriptCourseEditTableProps
) {
    const { data, updateEntry, removeEntry } = props;
    return (
        <Table className="mt-2">
            <TableHeader>
                <TableRow className="text-center">
                    <TableHead className="uppercase font-bold tracking-wide">
                        Course Code
                    </TableHead>
                    <TableHead className="uppercase font-bold tracking-wide">
                        Credits
                    </TableHead>
                    <TableHead className="uppercase font-bold tracking-wide">
                        Score
                    </TableHead>
                    <TableHead className="uppercase font-bold tracking-wide">
                        Action
                    </TableHead>
                </TableRow>
            </TableHeader>
            <TableBody>
                {data.map((entry, index) => (
                    <TableRow key={index} className="text-center">
                        <TableCell>
                            <Input
                                value={entry.courseCode}
                                onChange={(e) =>
                                    updateEntry(
                                        index,
                                        "courseCode",
                                        e.target.value
                                    )
                                }
                                className="text-center"
                            />
                        </TableCell>
                        <TableCell>
                            <Input
                                type="number"
                                min={0}
                                value={entry.credits}
                                onChange={(e) =>
                                    updateEntry(
                                        index,
                                        "credits",
                                        +e.target.value
                                    )
                                }
                                className="text-center"
                            />
                        </TableCell>
                        <TableCell>
                            <Select
                                value={entry.score}
                                onValueChange={(value) =>
                                    updateEntry(index, "score", value)
                                }
                            >
                                <SelectTrigger className="w-[100px] mx-auto">
                                    <SelectValue placeholder="Score" />
                                </SelectTrigger>
                                <SelectContent>
                                    {["A", "AB", "B", "BC", "C", "D", "E"].map(
                                        (grade) => (
                                            <SelectItem
                                                key={grade}
                                                value={grade}
                                            >
                                                {grade}
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
                                onClick={() => removeEntry(index)}
                            >
                                <Trash2 className="w-4 h-4" />
                            </Button>
                        </TableCell>
                    </TableRow>
                ))}
            </TableBody>
        </Table>
    );
}
