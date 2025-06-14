import { useRouterState } from "@tanstack/react-router";

export function ViewPdf() {
    const { location } = useRouterState();
    // @ts-ignore
    const pdfUrl = location.state?.pdfUrl;

    if (!pdfUrl) {
        return (
            <div className="p-10 text-center text-red-500">
                No PDF file provided.
            </div>
        );
    }

    return (
        <div className="p-10">
            <h1 className="py-5 text-2xl">PDF Viewer</h1>
            <iframe
                src={pdfUrl}
                width="100%"
                height="800px"
                className="w-full border rounded-md shadow"
            />
        </div>
    );
}
