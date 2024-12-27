# 使用 Nginx 作为前端服务器
FROM nginx:alpine

# 复制前端的静态文件到 Nginx 默认路径
COPY dist /usr/share/nginx/html

# 暴露端口
EXPOSE 9091

# 启动 Nginx 服务
CMD ["nginx", "-g", "daemon off;"]
