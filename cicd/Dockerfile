FROM openjdk:8-jdk-alpine
MAINTAINER liubiao "2691973894@qq.com"
WORKDIR /jar
ENV PATH /usr/local/bin:$PATH
ENV ENV_IP=192.168.50.115
VOLUME /jar
RUN mkdir -p /jar/logs
COPY target/*.jar /jar/app.jar
EXPOSE 8888
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/jar/app.jar"]