import { useEffect, useState } from "react";
import filesService from "../services/files.service";

const FilesPage = () => {
    const [data, setData] = useState();
    const [file, setFile] = useState();

    async function uploadFile() {
        let formData = new FormData();
        formData.append("file", file);
        await filesService.post(formData).then((data) => console.log(data));
    }
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
                onChange={(file) => {
                    console.log(file);
                    setFile(file.target.files[0]);
                }}
            />
            <button id="upload-buton" onClick={uploadFile}>
                Upload
            </button>
        </>
    );
};

export default FilesPage;
