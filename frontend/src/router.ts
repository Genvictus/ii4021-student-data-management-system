import {
    createRootRoute,
    createRoute,
    createRouter,
} from "@tanstack/react-router";

import App from "./App";
import { Login } from "@/pages/login";
import { Register } from "@/pages/register";
import { Home } from "@/pages/home";

// Root route
const rootRoute = createRootRoute({
    component: App,
});

// Child routes
const loginRoute = createRoute({
    getParentRoute: () => rootRoute,
    path: "/login",
    component: Login,
});

const registerRoute = createRoute({
    getParentRoute: () => rootRoute,
    path: "/register",
    component: Register,
});

const homeRoute = createRoute({
    getParentRoute: () => rootRoute,
    path: "/home",
    component: Home,
});

// Combine routes
const routeTree = rootRoute.addChildren([loginRoute, registerRoute, homeRoute]);

export const router = createRouter({ routeTree });

// For type safety
declare module "@tanstack/react-router" {
    interface Register {
        router: typeof router;
    }
}
