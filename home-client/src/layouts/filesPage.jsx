import { useEffect, useState } from "react";
import filesService from "../services/files.service";

const FilesPage = () => {
    const [data, setData] = useState();
    useEffect(() => {
        filesService.get().then((data) => setData(data));
    }, []);
    useEffect(() => {
        console.log(data);
    }, [data]);
    return <h1>Test</h1>;
};

export default FilesPage;
