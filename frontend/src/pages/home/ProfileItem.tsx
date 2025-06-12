import { type LucideIcon } from "lucide-react";

export function ProfileItem({
    icon: Icon,
    label,
    value,
}: {
    icon: LucideIcon;
    label: string;
    value: React.ReactNode;
}) {
    return (
        <div className="flex items-center justify-between">
            <div className="flex items-center gap-2 text-muted-foreground font-medium">
                <Icon className="w-4 h-4" />
                <span>{label}</span>
            </div>
            <div className="text-right">{value}</div>
        </div>
    );
}
