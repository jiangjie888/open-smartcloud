#基于 java:v8 来构建我们的镜像
FROM 172.16.4.130/library/javatom:v8.0

#维护者
MAINTAINER app="smardcloud.com river-leader" by="jiangjie"
#LABEL app="smardcloud.com river-leader" version="1.0.0" by="jiangjie"

#声明变量 在docker build创建镜像时 使用--build-arg 传入参数source
#ARG source


#设置工作目录为文件夹，即容器启动默认的文件夹
#RUN mkdir -p /opt/webapps
#WORKDIR /opt/webapps

#拷贝项目到docker容器中,将宿主机的APPlication拷贝到容器的工作目录下
RUN rm -rf /opt/webapps/zuul-gateway*
ADD ./target/zuul-gateway.jar /opt/webapps/zuul-gateway.jar

#设置Docker容器对外暴露端口
EXPOSE 9000

#使用来运行应用程序
ENTRYPOINT ["java", "-jar", "/opt/webapps/zuul-gateway.jar"]
#,"--server.port=9000"
#CMD ["--spring.profiles.active=test"]
#CMD ["java", "-jar", "bootshiro.jar","--spring.profiles.active=prod"]