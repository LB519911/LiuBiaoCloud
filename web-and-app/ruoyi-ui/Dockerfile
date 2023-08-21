FROM nginx:latest
MAINTAINER 刘彪 "2691973894@qq.com"
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
COPY dist  /usr/share/nginx/html/
COPY default.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
# sudo docker buildx build --platform=linux/amd64  -f Dockerfile -t ruoyi-ui .

# sudo docker tag ruoyi-ui:latest registry.cn-hangzhou.aliyuncs.com/liubiao_dev/dev:ruoyi-ui

# sudo docker push registry.cn-hangzhou.aliyuncs.com/liubiao_dev/dev:ruoyi-ui

# sudo docker rmi registry.cn-hangzhou.aliyuncs.com/liubiao_dev/dev:ruoyi-ui

# sudo docker login --username=lb519911 registry.cn-hangzhou.aliyuncs.com


# sudo docker buildx build --platform=linux/amd64 -f Dockerfile -t ruoyi-ui .

# sudo docker tag ruoyi-ui:latest registry.apps.okd.ct-hs-1.xtj.zone/liubiao/course:ruoyi-ui

# sudo docker push registry.apps.okd.ct-hs-1.xtj.zone/liubiao/course:ruoyi-ui

# sudo docker rmi registry.apps.okd.ct-hs-1.xtj.zone/liubiao/course:ruoyi-ui

# sudo docker login --username=lb519911 registry.cn-hangzhou.aliyuncs.com

# docker login --username=liubiao registry.apps.okd.ct-hs-1.xtj.zone
