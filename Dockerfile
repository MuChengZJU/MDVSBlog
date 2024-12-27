# 使用 Java 17 基础镜像
FROM openjdk:17-jdk-slim

# 设置工作目录
WORKDIR /app

# 复制 Maven 构建的 jar 文件到容器中
COPY target/Blog-0.0.1-SNAPSHOT.jar /app/blog-backend.jar

# 暴露服务端口
EXPOSE 9090

# 启动服务
CMD ["java", "-jar", "/app/blog-backend.jar"]
