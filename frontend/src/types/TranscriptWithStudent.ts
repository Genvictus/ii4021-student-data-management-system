export type TranscriptWithStudent = {
    transcriptId: string;
    student: {
        userId: string;
        email: string;
        fullName: string;
        role: "STUDENT" | "HOD" | "ADMIN";
        publicKey: string;
        department: null;
        departmentId: string;
        supervisorId: string;
    };
    studentId: string;
    transcriptStatus: "PENDING" | "APPROVED" | "REJECTED";
    transcriptData: TranscriptEntry[];
    hodId: string;
    hod: null;
    hodDigitalSignature: string;
    encryptedKey: string;
};

export type TranscriptEntry = {
    courseCode: string;
    credits: number;
    score: "A" | "AB" | "B" | "BC" | "C" | "D" | "E";
};

export type EncryptedTranscriptWithStudent = Omit<
    TranscriptWithStudent, "transcriptData"
> & {
    encryptedTranscriptData: string;
};

