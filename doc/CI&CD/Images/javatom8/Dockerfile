FROM ubuntu:16.04
MAINTAINER create by jiangjie for jdk1.8.0_181 and tomcat-8.5.37
#LABEL app="jdk1.8.0_181 and tomcat-8.5.37" version="1.0.0" by="jiangjie"

#把java与tomcat添加到容器中
ADD jdk-8u181-linux-x64.tar.gz /usr/local/
ADD apache-tomcat-8.5.37.tar.gz /usr/local/

#配置java与tomcat环境变量
ENV JAVA_HOME /usr/local/jdk1.8.0_181 
ENV CLASSPATH $JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar 
ENV CATALINA_HOME /usr/local/apache-tomcat-8.5.37
ENV CATALINA_BASE /usr/local/apache-tomcat-8.5.37
ENV PATH $PATH:$JAVA_HOME/bin:$CATALINA_HOME/lib:$CATALINA_HOME/bin

#容器运行时监听的端口
EXPOSE  8080
ENTRYPOINT ["java","-version"]

#FROM CentOS:latest
#MAINTAINER john
# now add java and tomcat support in the container 
#ADD jdk-8u181-linux-x64.tar.gz  /usr/local/ 
#ADD apache-tomcat-8.5.32.tar.gz /usr/local/

# configuration of java and tomcat ENV 
#ENV JAVA_HOME /usr/local/jdk1.8.0_181 
#ENV CLASSPATH $JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar 
#ENV CATALINA_HOME /usr/local/apache-tomcat-8.5.32
#ENV PATH $PATH:$JAVA_HOME/bin:$CATALINA_HOME/lib:$CATALINA_HOME/bin

# container listener port 
#EXPOSE 8080
# startup web application services by self 
#CMD /usr/local/apache-tomcat-8.5.32/bin/catalina.sh run

#chmod 777 Dockerfile