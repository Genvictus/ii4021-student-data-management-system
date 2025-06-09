import { Outlet } from "@tanstack/react-router";
import { Toaster } from "@/components/ui/sonner";
import "./App.css";

function App() {
    return (
        <div className="w-full h-[100vh]">
            <Toaster richColors />
            <Outlet />
        </div>
    );
}

export default App;
