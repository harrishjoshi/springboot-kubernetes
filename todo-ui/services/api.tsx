import axios from "axios"
import getConfig from 'next/config'
import { Todo, TodosResponse } from "./models";
const { serverRuntimeConfig, publicRuntimeConfig } = getConfig()

const getApiUrl = () => {
    return `${serverRuntimeConfig.API_BASE_URL || publicRuntimeConfig.API_BASE_URL}/api/v1/todo`;
}

export const fetchTodos = async (page: number, query: string): Promise<TodosResponse> => {
    let url = `${getApiUrl()}?page=${page}`

    if (query) {
        url += `&query=${query}`;
    }

    try {
        const response = await axios.get<TodosResponse>(url);

        return response.data;
    } catch (error: any) {
          // Returning an empty TodosResponse
        return Promise.resolve({
            data: [],
            currentPage: 1,
            totalPages: 1,
            hasNext: false,
            hasPrevious: false,
            totalElements: 0,
            isFirst: true,
            isLast: true
        });
    }
}

export const saveTodo = async (todo: { title: string, description: string }) => {
    try {
        const response = await axios.post(`${getApiUrl()}`, todo);
        return response.data;
    } catch (error: any) {
        return { errorMessage: error.response?.data?.detail || `Failed to save todo. Please try again.`, errorCode: error.status };
    }
}

export const fetchTodoById = async (id: any) => {
    try {
        const response = await axios.get(`${getApiUrl()}/${id}`);
        return response.data;
    } catch (error: any) {
        return { errorMessage: error.response?.data?.detail || `Todo with [${id}] not found.`, errorCode: error.status };
    }
};

export const updateTodo = async (id: any, todo: any) => {
    try {
        const response = await axios.put(`${getApiUrl()}/${id}`, todo);
        return response.data;
    } catch (error: any) {
        return { errorMessage: error.response?.data?.detail || `Failed to update todo. Please try again.`, errorCode: error.status };
    }
};

export const deleteTodo = async (id: any) => {
    try {
        await axios.delete(`${getApiUrl()}/${id}`);
    } catch (error: any) {
        return { errorMessage: error.response?.data?.detail || `Failed to delete todo. Please try again.`, errorCode: error.status };
    }
};