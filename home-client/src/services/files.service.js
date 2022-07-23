import httpService from "./http.service";

const filesEndpoint = "files/";

const filesService = {
    get: async () => {
        const { data } = await httpService.get(filesEndpoint);
        return data;
    }
};

export default filesService;
