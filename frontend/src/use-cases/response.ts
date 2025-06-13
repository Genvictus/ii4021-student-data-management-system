export type ResponseFormat<T> = {
    success: boolean;
    message: string | null;
    data: T | null;
};
