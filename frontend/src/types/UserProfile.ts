import type { RsaPublicKey } from "@/lib/rsa";

export type UserProfile = {
    userId: string;
    email: string;
    fullName: string;
    role: UserRole;
    departmentId: string;
    supervisorId: string | null;
    publicKey: RsaPublicKey;
};

export type UserRole = "STUDENT" | "SUPERVISOR" | "HOD";
