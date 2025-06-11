export type UserProfile = {
    userId: string;
    email: string;
    fullName: string;
    role: UserRole;
    departmentId: string;
    supervisorId: string | null;
};

export type UserRole = "STUDENT" | "SUPERVISOR" | "HOD";
