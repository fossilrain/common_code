#建库
create user tz identified by tzdatang;  
grant connect, resource to tz;
grant debug connect session to tz;
grant create public database link to tz;
grant drop public database link to tz;

grant create synonym to tz;

grant create view to tz;

#执行脚本

@F:\pro\script\dbscript\xxx.sql

#env:
jdbc:oracle:thin:@172.16.6.147:1521:orcl

jdbc:oracle:thin:@172.16.6.148:1521:orcl