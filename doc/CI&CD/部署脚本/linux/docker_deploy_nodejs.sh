#!/bin/bash
# $# 是传给脚本的参数个数
# $0 是脚本本身的名字
# $1 是传递给该shell脚本的第一个参数
# $2 是传递给该shell脚本的第二个参数
# $@ 是传给脚本的所有参数的列表
# $* 是以一个单字符串显示所有向脚本传递的参数，与位置变量不同，参数可超过9个
# $$ 是脚本运行的当前进程ID号
# $? 是显示最后命令的退出状态，0表示没有错误，其他表示有错误

# 定义变量
MYAPPS_DIR="/opt/docker/webapps"
PRJECT_MARSTER=$1
PRJECT_NAME=$2
API_VERSION='1.0.0'
API_PORT=$3
SOURCE_PORT=$4
PROJECT_ENV=$5
PACKAGE=$PRJECT_NAME".rar"
#$BUILD_NUMBER
LAST_TAG=$6
#VIRTUAL_DIR=$7
PRE_TAG=$((LAST_TAG-1))
HARBOR_HOST=172.16.4.130
IMAGE_NAME=$HARBOR_HOST/$PRJECT_MARSTER/$PRJECT_NAME
CONTAINER_NAME=$PRJECT_MARSTER-$PRJECT_NAME-$API_VERSION
echo "================begin deploy==================="
#检测参数
if [ $# < 6 ]; then
	echo "The number of input parameters is insufficient"
	exit 1
else
	echo "parameters ok"
fi
#输出参数
echo "-------start print parameters----------"
for key in "$@"
do
    echo '$@' $key
done
echo "-------end print parameters------"
#检查目录文件
if [ ! -f $MYAPPS_DIR/$PRJECT_MARSTER/$PRJECT_NAME/Dockerfile ]; then
    echo $MYAPPS_DIR/$PRJECT_MARSTER/$PRJECT_NAME/Dockerfile" is not exist"
    exit 1
fi

#
chmod 777 $MYAPPS_DIR/$PRJECT_MARSTER/$PRJECT_NAME/static
cp $MYAPPS_DIR/$PRJECT_MARSTER/$PRJECT_NAME/appConfig.js $MYAPPS_DIR/$PRJECT_MARSTER/$PRJECT_NAME/static/

#删除同名docker容器
echo "delete docker container "$CONTAINER_NAME
cid=$(docker ps -a | grep -w "$CONTAINER_NAME" | awk '{print $1}')
echo $cid
if [ "$cid" != "" ]; then
   docker stop $cid
   docker rm -f $cid
fi

#删除同名docker镜像
echo "delete docker images "$IMAGE_NAME:$PRE_TAG
imageid=$(docker images -a | grep -w "$IMAGE_NAME" | awk '{print $3}')
echo $imageid
if [ "$cid" != "" ]; then
   docker rmi -f $imageid
fi
#docker rmi -f $IMAGE_NAME:$PRE_TAG
#docker image prune -a -f

#删除 Dockerfile 文件
#rm -f Dockerfile

#生成docker 镜像和容器
echo "-------start create docker image and container------"
echo $MYAPPS_DIR/$PRJECT_MARSTER/$PRJECT_NAME
cd $MYAPPS_DIR/$PRJECT_MARSTER/$PRJECT_NAME/
#创建镜像
echo "create docker image "$IMAGE_NAME:$LAST_TAG
docker build -t $IMAGE_NAME:$LAST_TAG .
imageid=$(docker images | grep -w "$IMAGE_NAME" | awk '{print $3}')
if [ "$imageid" = "" ]; then
   echo "create docker image "$IMAGE_NAME:$LAST_TAG" failed"
   exit 1
fi


#启动docker 容器
echo "start docker container "$CONTAINER_NAME
docker run -d -p $API_PORT:$SOURCE_PORT --name $CONTAINER_NAME $IMAGE_NAME:$LAST_TAG
cid=$(docker ps | grep -w "$CONTAINER_NAME" | awk '{print $1}')
if [ "$cid" = "" ]; then
   echo "start docker container "$CONTAINER_NAME" failed"
   exit 1
fi

echo "-------end create docker image and container------"
#推送docker image 到 Harbor
#echo $PROJECT_ENV
if [ "$PROJECT_ENV" = "pod" ]; then
	echo "-------start push docker image to Harbor------"
	#is_contaioner=${curl -i -s -k http://$HARBOR_HOST}
	#if [ $is_contaioner -ne 200 ]; then
		#docker tag $IMAGE_NAME $HARBOR_HOST/$IMAGE_NAME
		docker login -u admin -p 123456 http://$HARBOR_HOST
		docker push $IMAGE_NAME:$LAST_TAG
		#docker rmi $IMAGE_NAME
	#else
		#echo "Harbor image is exist,do not upload"
	#fi
	echo "-------end push docker image to Harbor------"
fi
echo deploy ok
echo "================end deploy==================="