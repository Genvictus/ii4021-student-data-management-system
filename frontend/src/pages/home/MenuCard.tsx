import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import type { UserRole } from "@/types/UserProfile";
import {
    Users,
    MessagesSquare,
    ScrollText,
    BookOpen,
    LogOut,
} from "lucide-react";
import { logout } from "@/use-cases/logout";
import { useNavigate } from "@tanstack/react-router";

interface MenuCardProps {
    userRole: UserRole;
}

export function MenuCard({ userRole }: MenuCardProps) {
    return (
        <Card className="w-80 h-80 shadow-lg">
            <CardHeader>
                <CardTitle>Menu</CardTitle>
            </CardHeader>

            {userRole === "STUDENT" ? (
                <StudentMenu />
            ) : userRole === "SUPERVISOR" ? (
                <SupervisorMenu />
            ) : (
                <HodMenu />
            )}
        </Card>
    );
}

function StudentMenu() {
    return (
        <CardContent className="flex flex-col gap-4 justify-center items-center">
            <ViewTranscriptMenuItem />
            <ViewCoursesMenuItem />
            <LogoutMenuItem />
        </CardContent>
    );
}

function SupervisorMenu() {
    return (
        <CardContent className="flex flex-col gap-4 justify-center items-center">
            <ViewStudentsMenuitem />
            <ViewInquiriesMenuItem />
            <ViewCoursesMenuItem />
            <LogoutMenuItem />
        </CardContent>
    );
}

function HodMenu() {
    return (
        <CardContent className="flex flex-col gap-4 justify-center items-center">
            <ViewStudentsMenuitem />
            <ViewCoursesMenuItem />
            <LogoutMenuItem />
        </CardContent>
    );
}

function ViewTranscriptMenuItem() {
    return (
        <Button className="w-full gap-2" size="lg">
            <ScrollText className="w-5 h-5" />
            <span className="text-xl">View Transcript</span>
        </Button>
    );
}

function ViewCoursesMenuItem() {
    return (
        <Button className="w-full gap-2" size="lg">
            <BookOpen className="w-5 h-5" />
            <span className="text-xl">View Courses</span>
        </Button>
    );
}

function ViewInquiriesMenuItem() {
    return (
        <Button className="w-full gap-2" size="lg">
            <MessagesSquare className="w-5 h-5" />
            <span className="text-xl">View Inquiries</span>
        </Button>
    );
}

function LogoutMenuItem() {
    const navigate = useNavigate();
    const handleLogout = () => {
        logout();
        navigate({ to: "/login" });
    };
    return (
        <Button className="w-full gap-2" size="lg" onClick={handleLogout}>
            <LogOut className="w-5 h-5" />
            <span className="text-xl">Log out</span>
        </Button>
    );
}

function ViewStudentsMenuitem() {
    return (
        <Button className="w-full gap-2" size="lg">
            <Users className="w-5 h-5" />
            <span className="text-xl">View Students</span>
        </Button>
    );
}
