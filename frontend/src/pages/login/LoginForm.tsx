import { Button } from "@/components/ui/button";
import {
    Card,
    CardContent,
    CardDescription,
    CardFooter,
    CardHeader,
    CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { login } from "@/use-cases/login";
import { useNavigate } from "@tanstack/react-router";
import {
    type ChangeEventHandler,
    type FormEventHandler,
    useState,
} from "react";
import { toast } from "sonner";

export function LoginForm() {
    const [formData, setFormData] = useState({
        email: "",
        password: "",
    });
    const [isLoading, setIsLoading] = useState(false);
    const navigate = useNavigate();

    const formUnfilled = !formData.email || !formData.password;

    const handleChange: ChangeEventHandler<HTMLInputElement> = (e) => {
        const { id, value } = e.target;
        setFormData((prev) => ({
            ...prev,
            [id]: value,
        }));
    };

    const handleSubmit: FormEventHandler = async (e) => {
        if (formUnfilled) return;

        setIsLoading(true);
        e.preventDefault();
        const response = await login(formData);

        if (response.success) {
            toast.success("Login Successful", {
                description: response.message ?? undefined,
            });

            setTimeout(() => {
                navigate({ to: "/home" });
            }, 500);
        } else {
            toast.error("Login Failed", {
                description: Array.isArray(response.data) ? (
                    <div className="space-y-1">
                        {response.data.map((msg, idx) => (
                            <div key={idx}>{msg}</div>
                        ))}
                    </div>
                ) : (
                    response.message ?? "An error occurred"
                ),
            });
        }

        setIsLoading(false);
    };

    return (
        <form onSubmit={handleSubmit}>
            <Card className="w-[350px]">
                <CardHeader>
                    <CardTitle>Login to TransCrypt</CardTitle>
                    <CardDescription>Your transcript encrypted</CardDescription>
                </CardHeader>
                <CardContent>
                    <div className="grid w-full items-center gap-4">
                        <div className="flex flex-col space-y-1.5">
                            <Label htmlFor="email">Email</Label>
                            <Input
                                id="email"
                                placeholder="Your email"
                                onChange={handleChange}
                            />
                        </div>
                        <div className="flex flex-col space-y-1.5">
                            <Label htmlFor="password">Password</Label>
                            <Input
                                id="password"
                                type="password"
                                placeholder="Your password"
                                onChange={handleChange}
                            />
                        </div>
                    </div>
                    <div className="mt-2 text-xs">
                        Don't have an account?{" "}
                        <a href="/register" className="text-blue-400 underline">
                            Register
                        </a>
                    </div>
                </CardContent>
                <CardFooter>
                    <Button
                        className="w-full"
                        disabled={formUnfilled || isLoading}
                    >
                        Login
                    </Button>
                </CardFooter>
            </Card>
        </form>
    );
}
