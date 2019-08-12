FROM ubuntu:16.04
#MAINTAINER jiangjie tomcat-8.5.37
LABEL app="tomcat-8.5.37" version="1.0.0" by="jiangjie"

#把java与tomcat添加到容器中
ADD apache-tomcat-8.5.37.tar.gz /usr/local/

#配置java与tomcat环境变量
ENV CATALINA_HOME /usr/local/apache-tomcat-8.5.37
ENV CATALINA_BASE /usr/local/apache-tomcat-8.5.37
ENV PATH $PATH:$CATALINA_HOME/lib:$CATALINA_HOME/bin

#容器运行时监听的端口
EXPOSE  8080
CMD /usr/local/apache-tomcat-8.5.37/bin/catalina.sh run

#chmod 777 Dockerfile