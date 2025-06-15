import { Badge } from "./badge";

interface StatusBadgeProps {
    status: string;
}

export function StatusBadge(props: StatusBadgeProps) {
    const { status } = props;

    if (
        status === "REJECTED" ||
        status === "CLOSED" ||
        status === "UNVERIFIED"
    ) {
        return (
            <Badge className="text-red-500 bg-red-500/30 hover:bg-red-500/50">
                {status}
            </Badge>
        );
    }

    if (status === "APPROVED" || status === "VERIFIED") {
        return (
            <Badge className="text-green-400 bg-green-400/30 hover:bg-green-400/50">
                {status}
            </Badge>
        );
    }

    if (status === "WAITING_FOR_PARTICIPANT") {
        return (
            <Badge className="text-yellow-300 bg-yellow-300/30 hover:bg-yellow-300/50">
                {status}
            </Badge>
        );
    }

    if (status === "WAITING_FOR_REQUESTEE") {
        return (
            <Badge className="text-blue-400 bg-blue-400/30 hover:bg-blue-400/50">
                {status}
            </Badge>
        );
    }

    return (
        <Badge className="text-slate-300 bg-slate-300/20 hover:bg-slate-300/40">
            {status}
        </Badge>
    );
}
