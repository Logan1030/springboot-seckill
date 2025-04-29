# 使用JDK 8作为基础镜像
FROM openjdk:8-jdk-alpine

# 设置工作目录
WORKDIR /app

# 复制Maven构建的jar包到容器中
COPY target/*.jar app.jar

# 暴露应用端口
EXPOSE 8082

# 设置时区
ENV TZ=Asia/Shanghai

# 启动命令
ENTRYPOINT ["java","-jar","app.jar"] 