import { jsPDF } from "jspdf";
import autoTable from "jspdf-autotable";
import type { TranscriptWithStudent } from "@/types/TranscriptWithStudent";
import { calculateGpa } from "@/lib/calculateGpa";
import { RC4 } from "@/lib/rc4";

export function printTranscriptPdf(
    data: TranscriptWithStudent,
    encryptionKey?: string
): void {
    const { gpa, totalCredits } = calculateGpa(data.transcriptData);

    const doc = new jsPDF();

    const leftMargin = 20;
    const rightMargin = 20;
    const pageWidth = doc.internal.pageSize.getWidth();
    const usableWidth = pageWidth - leftMargin - rightMargin;

    doc.setFontSize(14);
    doc.text("Transcript Report", pageWidth / 2, 20, { align: "center" });

    doc.setFontSize(11);
    doc.text(`Student ID: ${data.studentId}`, leftMargin, 40);
    doc.text(`Full Name: ${data.student.fullName}`, leftMargin, 50);
    doc.text(`Transcript Status: ${data.transcriptStatus}`, leftMargin, 60);
    doc.text(`Total Credits: ${totalCredits}`, leftMargin, 70);
    doc.text(`GPA: ${gpa}`, leftMargin, 80);

    const tableData = data.transcriptData.map((entry) => [
        entry.courseCode,
        entry.credits,
        entry.score,
    ]);

    autoTable(doc, {
        startY: 95,
        head: [["Course Code", "Credits", "Score"]],
        body: tableData,
        theme: "grid",
        margin: { left: leftMargin, right: rightMargin },
        tableWidth: usableWidth,
        styles: {
            halign: "center",
            cellPadding: 2,
        },
        headStyles: {
            fillColor: [22, 160, 133],
            textColor: 255,
            fontStyle: "bold",
        },
    });

    const finalY = (doc as any).lastAutoTable.finalY + 10;

    doc.setFontSize(11);
    if (data.hodDigitalSignature) {
        doc.text(
            `Signed by Head of Department (ID: ${data.hodId})`,
            leftMargin,
            finalY
        );
    }

    const boxX = leftMargin;
    const boxY = finalY + 5;
    const boxWidth = usableWidth;
    const boxHeight = 35;

    doc.setDrawColor(0);
    doc.setLineWidth(0.3);
    doc.rect(boxX, boxY, boxWidth, boxHeight);

    doc.setFontSize(10);
    doc.setFont("courier", "bold");
    if (data.hodDigitalSignature) {
        const wrappedSignature = doc.splitTextToSize(
            data.hodDigitalSignature,
            boxWidth - 10
        );

        doc.text(wrappedSignature, boxX + 5, boxY + 5);
    }

    if (encryptionKey && encryptionKey !== "") {
        console.log("Print encrypted version of pdf");
        const bytesBuffer = new Uint8Array(doc.output("arraybuffer"));
        const encryptedBuffer = new RC4(encryptionKey).encrypt(bytesBuffer);

        const blob = new Blob([encryptedBuffer]);
        const link = document.createElement("a");
        link.href = URL.createObjectURL(blob);
        link.download = `transcript_${data.studentId}.pdf`;
        link.click();
    } else {
        console.log("Print unencrypted version of pdf");
        doc.save(`transcript_${data.studentId}.pdf`);
    }
}
