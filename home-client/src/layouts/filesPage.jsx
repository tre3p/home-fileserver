import { useEffect, useState } from "react";
import configFile from "../config.json";
import filesService from "../services/files.service";

const FilesPage = () => {
    const [data, setData] = useState();
    const [file, setFile] = useState();

    useEffect(() => {
        filesService.get().then((data) => setData(data));
    }, []);

    const handleFileUpload = async () => {
        let formData = new FormData();
        formData.append("data", file);
        await filesService.post(formData).then((data) => console.log(data));
        await filesService.get().then((data) => setData(data));
    };

    const handleFileDelete = async (fileId) => {
        await filesService.deleteFile(fileId);
        setData(data.filter((file) => file.id !== fileId));
    };

    const handleFileChange = ({ target }) => {
        console.log(target.files[0]);
        setFile(target.files[0]);
    };

    const createDownloadLink = (hash) => {
        const link = document.createElement("a");
        link.href = `${configFile.apiEndpoint}file/${hash}`;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    };

    return (
        <>
            <h1>Test</h1>
            <input
                type="file"
                id="fileupload"
                name="fileupload"
                onChange={handleFileChange}
            />
            <button id="upload-buton" onClick={handleFileUpload}>
                Upload
            </button>
            {data && (
                <div>
                    {data.map((file) => (
                        <div key={file.id}>
                            <h2>{file.originalFileName}</h2>
                            <button
                                onClick={() => createDownloadLink(file.hash)}
                            >
                                Download
                            </button>
                            <button onClick={() => handleFileDelete(file.id)}>
                                Delete
                            </button>
                        </div>
                    ))}
                </div>
            )}
        </>
    );
};

export default FilesPage;
