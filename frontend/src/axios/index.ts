import axios from "axios";

const baseURL = import.meta.env.VITE_BE_BASE_URL;

if (!baseURL) {
    throw new Error("BASE_URL is not defined in environment variables");
}

const api = axios.create({
    baseURL,
    headers: {
        "Content-Type": "application/json",
    },
});

api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem("token");
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

export default api;
