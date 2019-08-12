#基于构建我们的镜像
FROM 172.16.4.130/library/microsoft/dotnet:2.2-aspnetcore-runtime

MAINTAINER app="smardcloud.com workflow" by="jiangjie"
#声明变量 在docker build创建镜像时 使用--build-arg 传入参数source
#ARG source

#设置工作目录为 `/publish` 文件夹，即容器启动默认的文件夹
WORKDIR /webapps


#复制指令 如果source创建是没有赋值，默认路径为obj/Docker/publish
#COPY ${source:-obj/Docker/publish} .

#拷贝项目publish文件夹中的所有文件到 docker容器中的publish文件夹中  
COPY . /webapps

#设置Docker容器对外暴露60000端口
EXPOSE 5000

#使用`dotnet WebApp.Web.Host.dll`来运行应用程序
#CMD ["dotnet", "WebApp.Web.Host.dll", "--server.urls", "http://*:5000"]
ENTRYPOINT ["dotnet", "WebApp.Web.Host.dll"]