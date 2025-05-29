import backendClient from "../api/axios";
import { AxiosError } from "axios";

export const createGroupClass = async (groupClassData) => {
    try {
        const response = await backendClient.post('/group-classes', groupClassData);
        return response.data;
    } catch (error) {
        if (error instanceof AxiosError) {
            throw new Error(error?.response?.data?.message);
        }
        throw error;
    }
};

export const getAllGroupClasses = async () => {
    try {
        const response = await backendClient.get('/group-classes');
        return response.data;
    } catch (error) {
        if (error instanceof AxiosError) {
            throw new Error(error?.response?.data?.message);
        }
        throw error;
    }
};

export const getGroupClassById = async (id) => {
    try {
        const response = await backendClient.get(`/group-classes/${id}`);
        return response.data;
    } catch (error) {
        if (error instanceof AxiosError) {
            throw new Error(error?.response?.data?.message);
        }
        throw error;
    }
};

export const updateGroupClass = async (id, groupClassData) => {
    try {
        const response = await backendClient.put(`/group-classes/${id}`, groupClassData);
        return response.data;
    } catch (error) {
        if (error instanceof AxiosError) {
            throw new Error(error?.response?.data?.message);
        }
        throw error;
    }
};

export const deleteGroupClass = async (id) => {
    try {
        await backendClient.delete(`/group-classes/${id}`);
    } catch (error) {
        if (error instanceof AxiosError) {
            throw new Error(error?.response?.data?.message);
        }
        throw error;
    }
};

export const addParticipant = async (id) => {
    try {
        const response = await backendClient.post(`/group-classes/${id}/participants`);
        return response.data;
    } catch (error) {
        if (error instanceof AxiosError) {
            throw new Error(error?.response?.data?.message);
        }
        throw error;
    }
};

export const removeParticipant = async (id) => {
    try {
        const response = await backendClient.delete(`/group-classes/${id}/participants`);
        return response.data;
    } catch (error) {
        if (error instanceof AxiosError) {
            throw new Error(error?.response?.data?.message);
        }
        throw error;
    }
};