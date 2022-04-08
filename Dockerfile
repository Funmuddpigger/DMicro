#指定基础镜像
FROM java:8-alpine
#拷贝到容器路径
COPY ./artService.jar /tmp/artService.jar
#暴露端口
EXPOSE 7070
#入口,jar包启动命令
ENTRYPOINT java -jar /tmp/artService.jar

#指定基础镜像
FROM java:8-alpine
#拷贝到容器路径
COPY ./usrService.jar /tmp/usrService.jar
#暴露端口
EXPOSE 5050
#入口,jar包启动命令
ENTRYPOINT java -jar /tmp/usrService.jar

#指定基础镜像
FROM java:8-alpine
#拷贝到容器路径
COPY ./topicService.jar /tmp/topicService.jar
#暴露端口
EXPOSE 10010
#入口,jar包启动命令
ENTRYPOINT java -jar /tmp/topicService.jar

#指定基础镜像
FROM java:8-alpine
#拷贝到容器路径
COPY ./comService.jar /tmp/comService.jar
#暴露端口
EXPOSE 4040
#入口,jar包启动命令
ENTRYPOINT java -jar /tmp/comService.jar

#指定基础镜像
FROM java:8-alpine
#拷贝到容器路径
COPY ./gateway.jar /tmp/gateway.jar
#暴露端口
EXPOSE 21000
#入口,jar包启动命令
ENTRYPOINT java -jar /tmp/gateway.jar



#run
docker run --name art-service -p 7070:7070  -d art-service:1.0
docker run --name usr-service -p 5050:5050 -d usr-service:1.0
docker run --name topic-service -p 10010:10010 -d topic-service:1.0
docker run --name comment-service -p 4040:4040 -d comment-service:1.0

docker run --name usr-service -p 5050:5050 -d usr-service:1.0 -v /data/images:/data/images
docker run --name art-service -p 7070:7070 -d art-service:1.0 -v /data/images:/data/images
docker run --name topic-service -p 10010:10010 -d topic-service:1.0 -v /data/images:/data/images
docker run --name comment-service -p 4040:4040 -d comment-service:1.0 -v /data/images:/data/images
docker run --name good-service -p 9090:9090 -d good-service:1.0 -v /data/images:/data/images
docker run --name gateway -p 21000:21000 -d gateway:1.0