export type UserProfile = {
    userId: string;
    email: string;
    fullName: string;
    role: UserRole;
    departmentId: string;
    supervisorId: string | null;
    publicKey: string;
};

export type UserRole = "STUDENT" | "SUPERVISOR" | "HOD";
