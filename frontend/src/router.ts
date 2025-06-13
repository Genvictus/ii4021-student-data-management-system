import {
    createRootRoute,
    createRoute,
    createRouter,
} from "@tanstack/react-router";

import App from "./App";
import { Login } from "@/pages/login";
import { Register } from "@/pages/register";
import { Home, homeLoader } from "@/pages/home";
import { Index } from "./pages";
import { Courses, coursesLoader } from "./pages/courses";
import { Inquiries, inquiriesLoader } from "./pages/inquiries";
import {
    StudentTranscript,
    studentTranscriptLoader,
} from "./pages/student-transcripts";
import { MyTranscript, myTranscriptLoader } from "./pages/my-transcript";

const rootRoute = createRootRoute({
    component: App,
});

const indexRoute = createRoute({
    getParentRoute: () => rootRoute,
    path: "/",
    component: Index,
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

const coursesRoute = createRoute({
    getParentRoute: () => rootRoute,
    path: "/courses",
    component: Courses,
    loader: coursesLoader,
});

const inquiriesRoute = createRoute({
    getParentRoute: () => rootRoute,
    path: "/inquiries",
    component: Inquiries,
    loader: inquiriesLoader,
});

const studentTranscript = createRoute({
    getParentRoute: () => rootRoute,
    path: "/student-transcripts",
    component: StudentTranscript,
    loader: studentTranscriptLoader,
});

const myTranscriptRoute = createRoute({
    getParentRoute: () => rootRoute,
    path: "/transcript",
    component: MyTranscript,
    loader: myTranscriptLoader,
});

const routeTree = rootRoute.addChildren([
    indexRoute,
    loginRoute,
    registerRoute,
    homeRoute,
    coursesRoute,
    inquiriesRoute,
    studentTranscript,
    myTranscriptRoute,
]);

export const router = createRouter({ routeTree });

declare module "@tanstack/react-router" {
    interface Register {
        router: typeof router;
    }
}
