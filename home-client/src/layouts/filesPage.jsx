import { useEffect, useState } from "react";
import filesService from "../services/files.service";
// import { useLocation } from "react-router-dom";

const FilesPage = () => {
    const [data, setData] = useState();
    const [file, setFile] = useState();

    // const location = useLocation();

    const handleFileUpload = async () => {
        let formData = new FormData();
        formData.append("data", file);
        await filesService.post(formData).then((data) => console.log(data));
        await filesService.get().then((data) => setData(data));
    };

    const handleFileChange = ({ target }) => {
        console.log(target.files[0]);
        setFile(target.files[0]);
    };

    const handleFileDownload = async (fileId) => {
        await filesService.getHash(fileId).then((fileHash) => {
            console.log(fileHash);
        });
    };
    useEffect(() => {
        filesService.get().then((data) => setData(data));
    }, []);
    useEffect(() => {
        console.log(file);
    }, [file]);
    useEffect(() => {
        console.log(data);
    }, [data]);
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
                            <form
                                action={`http://localhost:5050/file/${file.hash}`}
                            >
                                <button type="submit">Download</button>
                            </form>
                        </div>
                    ))}
                </div>
            )}
        </>
    );
};

export default FilesPage;
