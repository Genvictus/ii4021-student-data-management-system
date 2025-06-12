import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { User, Mail, BadgeInfo, IdCard, Building2, Users } from "lucide-react";
import { ProfileItem } from "./ProfileItem";
import type { UserProfile } from "@/types/UserProfile";

interface ProfileCardProps {
    userProfile: UserProfile;
}

export function ProfileCard(props: ProfileCardProps) {
    const { userProfile } = props;
    return (
        <Card className="flex-1 h-80 max-w-md shadow-xl border">
            <CardHeader>
                <CardTitle className="text-xl">Your Profile</CardTitle>
            </CardHeader>
            <CardContent className="space-y-4 text-sm">
                <ProfileItem
                    icon={User}
                    label="Full Name"
                    value={userProfile.fullName}
                />
                <ProfileItem
                    icon={Mail}
                    label="Email"
                    value={userProfile.email}
                />
                <ProfileItem
                    icon={BadgeInfo}
                    label="Role"
                    value={userProfile.role}
                />
                <ProfileItem
                    icon={IdCard}
                    label="User ID"
                    value={userProfile.userId}
                />
                <ProfileItem
                    icon={Building2}
                    label="Department ID"
                    value={userProfile.departmentId}
                />
                <ProfileItem
                    icon={Users}
                    label="Supervisor ID"
                    value={
                        userProfile.supervisorId ?? (
                            <span className="italic text-muted-foreground">
                                None
                            </span>
                        )
                    }
                />
            </CardContent>
        </Card>
    );
}
