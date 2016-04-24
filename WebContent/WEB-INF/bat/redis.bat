#该脚本为启动两个redis服务与对应的两个redis客户端
start "6379-server" /D "D:\pf\redis\redis6379\64bit" /I redis-server.exe redis.conf
start "6479-server" /D "D:\pf\redis\redis6479\64bit" /I redis-server.exe redis.conf
start "6379-cli" /D "D:\pf\redis\redis6379\64bit" /I redis-cli.exe -h 127.0.0.1 -p 6379
start "6479-cli" /D "D:\pf\redis\redis6479\64bit" /I redis-cli.exe -h 127.0.0.1 -p 6479