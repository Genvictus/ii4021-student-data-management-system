import type { UserProfile } from "@/types/UserProfile";

export function getUserProfile(): UserProfile | null {
    const userProfile = localStorage.getItem("user");

    if (!userProfile) {
        return null;
    }

    const parsedUserProfile = JSON.parse(userProfile);

    return {
        userId: parsedUserProfile.userId,
        email: parsedUserProfile.email,
        fullName: parsedUserProfile.fullName,
        role: parsedUserProfile.role,
        departmentId: parsedUserProfile.departmentId,
        supervisorId: parsedUserProfile.supervisorId || null,
    };
}
