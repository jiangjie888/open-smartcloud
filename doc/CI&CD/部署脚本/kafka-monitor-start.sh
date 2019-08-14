java -Xms512M -Xmx512M -Xss1024K -XX:PermSize=256m -XX:MaxPermSize=512m  -cp KafkaOffsetMonitor-assembly-0.2.0.jar com.quantifind.kafka.offsetapp.OffsetGetterWeb \
--port 9900 \
--zk 172.16.4.130:2181,172.16.4.131:2181,172.16.4.121:2181 \
--refresh 5.minutes \
--retain 1.day >/dev/null 2>&1;