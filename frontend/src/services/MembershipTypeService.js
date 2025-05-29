import backendClient from "../api/axios";
import { AxiosError } from "axios";

export const createMembershipType = async (membershipTypeData) => {
    try {
        const response = await backendClient.post('/membership-types', membershipTypeData);
        return response.data;
    } catch (error) {
        if (error instanceof AxiosError) {
            throw new Error(error?.response?.data?.message);
        }
        throw error;
    }
};

export const getAllMembershipTypes = async () => {
    try {
        const response = await backendClient.get('/membership-types');
        return response.data;
    } catch (error) {
        if (error instanceof AxiosError) {
            throw new Error(error?.response?.data?.message);
        }
        throw error;
    }
};

export const getMembershipTypeById = async (id) => {
    try {
        const response = await backendClient.get(`/membership-types/${id}`);
        return response.data;
    } catch (error) {
        if (error instanceof AxiosError) {
            throw new Error(error?.response?.data?.message);
        }
        throw error;
    }
};

export const updateMembershipType = async (id, membershipTypeData) => {
    try {
        const response = await backendClient.put(`/membership-types/${id}`, membershipTypeData);
        return response.data;
    } catch (error) {
        if (error instanceof AxiosError) {
            throw new Error(error?.response?.data?.message);
        }
        throw error;
    }
};

export const deleteMembershipType = async (id) => {
    try {
        await backendClient.delete(`/membership-types/${id}`);
    } catch (error) {
        if (error instanceof AxiosError) {
            throw new Error(error?.response?.data?.message);
        }
        throw error;
    }
};