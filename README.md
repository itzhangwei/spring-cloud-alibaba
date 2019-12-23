***安装nacos，docker启动命令***

```
docker run -dt 
 --name nacos-server \
 -e PREFER_HOST_MODE=hostname \
 -e MODE=standalone \
 -e SPRING_DATASOURCE_PLATFORM=mysql \
 -e MYSQL_MASTER_SERVICE_HOST=106.12.45.222 \
 -e MYSQL_MASTER_SERVICE_PORT=3306 \
 -e MYSQL_MASTER_SERVICE_USER=root \
 -e MYSQL_MASTER_SERVICE_PASSWORD=root_mysql \
 -e MYSQL_MASTER_SERVICE_DB_NAME=tools    \
 -e MYSQL_SLAVE_SERVICE_HOST=106.12.45.222 \
 -e MYSQL_SLAVE_SERVICE_PORT=3306 \
 -p 8848:8848 \
 容器ID
 ```
*****

**启动前需要创建nacos需要的数据表**

[github mysql文件](https://github.com/alibaba/nacos/blob/develop/distribution/conf/nacos-mysql.sql)