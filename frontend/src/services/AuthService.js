import backendClient from "../api/axios";
import {AxiosError} from "axios";

export const login = async (email, password) => {
    try {
        const response = await backendClient.post(`/users/login`, {email, password});
        return response.data;

    } catch (error) {
        if (error instanceof AxiosError) {
            throw new Error(error?.response?.data?.message);
        }

        throw error;
    }
};

export const register = async (userData) => {
    try {
        const response = await backendClient.post(`/users/registration`, userData);
        if (response.status < 200 || response.status >= 300) {
            throw new Error(response?.data?.message);
        }
        return response.data;
    } catch (error) {
        if (error instanceof AxiosError) {
            throw new Error(error?.response?.data?.message);
        }

        throw error;
    }
};
