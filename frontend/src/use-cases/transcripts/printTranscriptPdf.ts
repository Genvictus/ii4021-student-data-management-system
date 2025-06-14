import { jsPDF } from "jspdf";
import autoTable from "jspdf-autotable";
import type { TranscriptWithStudent } from "@/types/TranscriptWithStudent";

export function printTranscriptPdf(data: TranscriptWithStudent): void {
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

    const tableData = data.transcriptData.map((entry) => [
        entry.courseCode,
        entry.credits,
        entry.score,
    ]);

    autoTable(doc, {
        startY: 75,
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
    doc.text(
        `Signed by Head of Department (ID: ${data.hodId})`,
        leftMargin,
        finalY
    );

    const boxX = leftMargin;
    const boxY = finalY + 5;
    const boxWidth = usableWidth;
    const boxHeight = 30;

    doc.setDrawColor(0);
    doc.setLineWidth(0.3);
    doc.rect(boxX, boxY, boxWidth, boxHeight);

    doc.setFontSize(10);
    doc.setFont("courier", "bold");
    doc.text(data.hodDigitalSignature, boxX + 5, boxY + 5);

    doc.save(`transcript_${data.studentId}.pdf`);
}
