"use client";
import { Button } from "@/components/ui/button";
import {
    Card,
    CardContent,
    CardDescription,
    CardFooter,
    CardHeader,
    CardTitle,
} from "@/components/ui/card";
import {
    Select,
    SelectContent,
    SelectGroup,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from "@/components/ui/select";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
    type ChangeEventHandler,
    type FormEventHandler,
    useState,
} from "react";
import { generateKeyPair, publicKeyToString } from "@/lib/rsa";
import { generatePrime } from "@/lib/random";
import { setPrivateKey } from "@/private-key-store/opfs";
import { register } from "@/use-cases/auth/register";
import { toast } from "sonner";
import { useNavigate } from "@tanstack/react-router";
import { Separator } from "@/components/ui/separator";

export function RegisterForm() {
    const [formData, setFormData] = useState({
        userId: "",
        fullName: "",
        email: "",
        password: "",
        confirmPassword: "",
        role: "",
        departmentId: "",
        supervisorId: "",
    });
    const [isLoading, setIsLoading] = useState(false);

    const formUnfilled =
        !formData.userId ||
        !formData.fullName ||
        !formData.email ||
        !formData.password ||
        !formData.confirmPassword ||
        !formData.role ||
        !formData.departmentId ||
        (formData.role === "STUDENT" && !formData.supervisorId);

    const navigate = useNavigate();

    const handleFieldChange = (id: string, value: string) => {
        setFormData((prev) => ({
            ...prev,
            [id]: value,
        }));
    };

    const handleChange: ChangeEventHandler<HTMLInputElement> = (e) => {
        const { id, value } = e.target;
        handleFieldChange(id, value);
    };

    const handleSubmit: FormEventHandler = async (e) => {
        if (formUnfilled) return;

        setIsLoading(true);

        e.preventDefault();
        const [p, q] = await Promise.all([
            generatePrime(1024),
            generatePrime(1024),
        ]);
        const { publicKey, privateKey } = generateKeyPair(p, q);
        setPrivateKey(formData.email, privateKey);
        const publicKeyString = publicKeyToString(publicKey);
        const response = await register({
            ...formData,
            publicKey: publicKeyString,
        });

        if (response.success) {
            toast.success("Register Successful", {
                description: response.message ?? undefined,
            });

            setTimeout(() => {
                navigate({ to: "/login" });
            }, 500);
        } else {
            toast.error("Register Failed", {
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
            <Card className="w-[60vw]">
                <CardHeader>
                    <CardTitle>Register to TransCrypt</CardTitle>
                    <CardDescription>Your transcript encrypted</CardDescription>
                </CardHeader>

                <CardContent>
                    <div className="flex gap-6">
                        {/* LEFT SIDE */}
                        <div className="flex-1 space-y-4">
                            <div className="flex flex-col space-y-1.5">
                                <Label htmlFor="userId">User ID</Label>
                                <Input
                                    id="userId"
                                    placeholder="Your NIM/NIP"
                                    onChange={handleChange}
                                />
                            </div>
                            <div className="flex flex-col space-y-1.5">
                                <Label htmlFor="fullName">Full Name</Label>
                                <Input
                                    id="fullName"
                                    placeholder="Your Full Name"
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
                            <div className="flex flex-col space-y-1.5">
                                <Label htmlFor="confirmPassword">
                                    Confirm Password
                                </Label>
                                <Input
                                    id="confirmPassword"
                                    type="password"
                                    placeholder="Your password"
                                    onChange={handleChange}
                                />
                            </div>
                        </div>

                        {/* VERTICAL SEPARATOR */}
                        <Separator orientation="vertical" className="h-auto" />

                        {/* RIGHT SIDE */}
                        <div className="flex-1 space-y-4">
                            <div className="flex flex-col space-y-1.5">
                                <Label htmlFor="email">Email</Label>
                                <Input
                                    id="email"
                                    placeholder="Your email"
                                    onChange={handleChange}
                                />
                            </div>
                            <div className="flex flex-col space-y-1.5">
                                <Label htmlFor="role">Role</Label>
                                <Select
                                    onValueChange={(value) =>
                                        handleFieldChange("role", value)
                                    }
                                >
                                    <SelectTrigger className="w-full">
                                        <SelectValue placeholder="Your role" />
                                    </SelectTrigger>
                                    <SelectContent>
                                        <SelectGroup>
                                            <SelectItem value="STUDENT">
                                                Student
                                            </SelectItem>
                                            <SelectItem value="SUPERVISOR">
                                                Supervisor
                                            </SelectItem>
                                            <SelectItem value="HOD">
                                                Head of Department
                                            </SelectItem>
                                        </SelectGroup>
                                    </SelectContent>
                                </Select>
                            </div>

                            {formData.role === "STUDENT" && (
                                <div className="flex flex-col space-y-1.5">
                                    <Label htmlFor="supervisorId">
                                        Supervisor ID
                                    </Label>
                                    <Input
                                        id="supervisorId"
                                        placeholder="Your supervisor ID"
                                        onChange={handleChange}
                                    />
                                </div>
                            )}

                            <div className="flex flex-col space-y-1.5">
                                <Label htmlFor="departmentId">
                                    Department ID
                                </Label>
                                <Select
                                    onValueChange={(value) =>
                                        handleFieldChange("departmentId", value)
                                    }
                                >
                                    <SelectTrigger className="w-full">
                                        <SelectValue placeholder="Your department ID" />
                                    </SelectTrigger>
                                    <SelectContent>
                                        <SelectGroup>
                                            <SelectItem value="135">
                                                135
                                            </SelectItem>
                                            <SelectItem value="182">
                                                182
                                            </SelectItem>
                                        </SelectGroup>
                                    </SelectContent>
                                </Select>
                            </div>

                            <div className="text-xs mt-4">
                                Already have an account?{" "}
                                <a
                                    href="/login"
                                    className="text-blue-500 underline"
                                >
                                    Login
                                </a>
                            </div>
                        </div>
                    </div>
                </CardContent>

                <CardFooter>
                    <Button
                        className="w-full"
                        type="submit"
                        disabled={formUnfilled || isLoading}
                    >
                        Register
                    </Button>
                </CardFooter>
            </Card>
        </form>
    );
}
