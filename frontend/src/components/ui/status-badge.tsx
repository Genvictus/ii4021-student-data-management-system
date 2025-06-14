import { Badge } from "./badge";

interface StatusBadgeProps {
    status: string;
}

export function StatusBadge(props: StatusBadgeProps) {
    const { status } = props;
    return (
        <Badge
            variant={
                status === "APPROVED"
                    ? "default"
                    : status === "REJECTED"
                    ? "destructive"
                    : "secondary"
            }
        >
            {status}
        </Badge>
    );
}
