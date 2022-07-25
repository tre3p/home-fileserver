import { useEffect, useState } from "react";
import filesService from "../services/files.service";

const FilesPage = () => {
    const [data, setData] = useState();
    const [file, setFile] = useState();

    async function uploadFile() {
        let formData = new FormData();
        formData.append("data", file);
        await filesService.post(formData).then((data) => console.log(data));
    }

    const handleFileChange = ({ target }) => {
        console.log(target.files[0]);
        setFile(target.files[0]);
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
            <button id="upload-buton" onClick={uploadFile}>
                Upload
            </button>
        </>
    );
};

export default FilesPage;
