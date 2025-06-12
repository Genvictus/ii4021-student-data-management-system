import { useLoaderData, useNavigate } from "@tanstack/react-router";
import { getUserProfile } from "@/lib/getUserProfile";
import { ProfileCard } from "./ProfileCard";
import { MenuCard } from "./MenuCard";

export async function homeLoader() {
    return getUserProfile();
}

export function Home() {
    const userProfile = useLoaderData({ from: "/home" });
    const navigate = useNavigate();

    if (!userProfile) {
        navigate({ to: "/login" });
        return null;
    }

    return (
        <div className="w-full h-full flex items-center justify-center gap-10">
            <ProfileCard userProfile={userProfile} />
            <MenuCard userRole={userProfile.role} />
        </div>
    );
}
