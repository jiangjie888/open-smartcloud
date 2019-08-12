#基于 来构建我们的镜像
FROM 172.16.4.130/library/tomcat:8.5.40-alpine

MAINTAINER app="smardcloud.com chengguan-pc" by="jiangjie"
#声明变量 在docker build创建镜像时 使用--build-arg 传入参数source
#ARG source


#设置工作目录为 `/publish` 文件夹，即容器启动默认的文件夹
WORKDIR /usr/local/tomcat/webapps


#拷贝项目publish文件夹中的所有文件到 docker容器中的publish文件夹中  
COPY . /usr/local/tomcat/webapps/chengguanpc

#设置Docker容器对外暴露端口
EXPOSE 8080

#程序启动脚本
#CMD ["pm2","start", "pm2.json","--no-daemon"]
#CMD ["npm", "start"]
#ENTRYPOINT