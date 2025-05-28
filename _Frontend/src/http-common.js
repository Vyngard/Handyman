import axios from "axios";

const http = axios.create({
    baseURL : "http://localhost:8080",
    headers:{
        "Content-type": "application/json"
    }
});

// Add a request interceptor to include the JWT token
http.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// Add a response interceptor to handle authentication errors
http.interceptors.response.use(
    (response) => {
        return response;
    },
    (error) => {
        if (error.response && error.response.status === 401) {
            // Token expired or invalid, redirect to login
            localStorage.removeItem('token');
            localStorage.removeItem('userId');
            localStorage.removeItem('workerId');
            localStorage.removeItem('userType');
            localStorage.removeItem('fullName');
            window.location.href = '/login';
        }
        return Promise.reject(error);
    }
);

export default http;