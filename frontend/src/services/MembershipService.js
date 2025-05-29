import backendClient from "../api/axios";
import { AxiosError } from "axios";

export const createMembership = async (membershipData) => {
    try {
        const response = await backendClient.post('/memberships', membershipData);
        return response.data;
    } catch (error) {
        if (error instanceof AxiosError) {
            throw new Error(error?.response?.data?.message);
        }
        throw error;
    }
};

export const getAllMemberships = async () => {
    try {
        const response = await backendClient.get('/memberships');
        return response.data;
    } catch (error) {
        if (error instanceof AxiosError) {
            throw new Error(error?.response?.data?.message);
        }
        throw error;
    }
};

export const getUserMemberships = async () => {
    try {
        const response = await backendClient.get('/memberships/user');
        return response.data;
    } catch (error) {
        if (error instanceof AxiosError) {
            throw new Error(error?.response?.data?.message);
        }
        throw error;
    }
};

export const getActiveUserMembership = async () => {
    try {
        const response = await backendClient.get('/memberships/user/active');
        return response.data;
    } catch (error) {
        if (error instanceof AxiosError) {
            throw new Error(error?.response?.data?.message);
        }
        throw error;
    }
};

export const getMembershipById = async (id) => {
    try {
        const response = await backendClient.get(`/memberships/${id}`);
        return response.data;
    } catch (error) {
        if (error instanceof AxiosError) {
            throw new Error(error?.response?.data?.message);
        }
        throw error;
    }
};

export const updateMembership = async (id, membershipData) => {
    try {
        const response = await backendClient.put(`/memberships/${id}`, membershipData);
        return response.data;
    } catch (error) {
        if (error instanceof AxiosError) {
            throw new Error(error?.response?.data?.message);
        }
        throw error;
    }
};

export const deleteMembership = async (id) => {
    try {
        await backendClient.delete(`/memberships/${id}`);
    } catch (error) {
        if (error instanceof AxiosError) {
            throw new Error(error?.response?.data?.message);
        }
        throw error;
    }
};