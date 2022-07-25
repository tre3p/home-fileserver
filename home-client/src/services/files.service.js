import httpService from "./http.service";

const filesEndpoint = "files/";

const filesService = {
    get: async () => {
        const { data } = await httpService.get(filesEndpoint);
        return data;
    },
    post: async (payload) => {
        const { data } = await httpService.post(filesEndpoint, payload);
        return data;
    },
    getHash: async (fileId) => {
        const { data } = await httpService.get(filesEndpoint + fileId);
        return data;
    },
    deleteFile: async (fileId) => {
        const { data } = await httpService.delete(filesEndpoint + fileId);
        return data;
    }
};

export default filesService;
