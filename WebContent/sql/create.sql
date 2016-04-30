1：创建临时表空间
create temporary tablespace BIGOA_TEMP 
tempfile 'F:\oracle\product\10.2.0\oradata\orcl\bigoa_temp1.dbf' 
size 500m  
autoextend on  
next 500m maxsize 20480m 
extent management local;  
 
2：创建数据表空间
create tablespace BIGOA
logging  
datafile 'F:\oracle\product\10.2.0\oradata\orcl\BIGOA.dbf' 
size 500m  
autoextend on  
next 500m maxsize 20480m  
extent management local;  
 
第3步：创建用户并指定表空间
create user fstb identified by fstb 
default tablespace BIGOA  
temporary tablespace BIGOA_TEMP;  
 
第4步：给用户授予权限
grant connect,resource,dba to fstb;

drop tablespace BIGOA offline;
drop tablespace fstb_temp including contents and datafile; 

drop tablespace BIGOA including contents and datafiles;

drop user fstb  cascade;
alter user system account unlock
