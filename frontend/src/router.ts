import {
    createRootRoute,
    createRoute,
    createRouter,
} from "@tanstack/react-router";

import App from "./App";
import { Login } from "@/pages/login";
import { Register } from "@/pages/register";
import { Home, homeLoader } from "@/pages/home";

const rootRoute = createRootRoute({
    component: App,
});

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
    loader: homeLoader,
});

const routeTree = rootRoute.addChildren([loginRoute, registerRoute, homeRoute]);

export const router = createRouter({ routeTree });

declare module "@tanstack/react-router" {
    interface Register {
        router: typeof router;
    }
}
