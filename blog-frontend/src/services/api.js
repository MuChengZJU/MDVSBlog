
import axios from 'axios';

// 获取当前主机名和协议
const protocol = window.location.protocol; // 'http:' 或 'https:'
const hostname = window.location.hostname; // 当前主机名或IP
const port = 9090; // 后端Spring Boot的端口

const apiClient = axios.create({
    baseURL: `${protocol}//${hostname}:${port}/api`, // 动态构建API基础URL
    headers: {
        'Content-Type': 'application/json',
    },
});

// 在每个请求中添加 Authorization 头
apiClient.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
});

// 全局错误处理：跳转到登录页面
apiClient.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response && error.response.status === 401) {
            localStorage.removeItem('token');
            window.location.href = '/login'; // 重定向到登录页面
        }
        return Promise.reject(error);
    }
);

console.log('apiClient loaded'); // 添加调试日志
export default apiClient;
