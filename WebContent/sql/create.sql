1��������ʱ��ռ�
create temporary tablespace BIGOA_TEMP 
tempfile 'F:\oracle\product\10.2.0\oradata\orcl\bigoa_temp1.dbf' 
size 500m  
autoextend on  
next 500m maxsize 20480m 
extent management local;  
 
2���������ݱ�ռ�
create tablespace BIGOA
logging  
datafile 'F:\oracle\product\10.2.0\oradata\orcl\BIGOA.dbf' 
size 500m  
autoextend on  
next 500m maxsize 20480m  
extent management local;  
 
��3���������û���ָ����ռ�
create user fstb identified by fstb 
default tablespace BIGOA  
temporary tablespace BIGOA_TEMP;  
 
��4�������û�����Ȩ��
grant connect,resource,dba to fstb;

drop tablespace BIGOA offline;
drop tablespace fstb_temp including contents and datafile; 

drop tablespace BIGOA including contents and datafiles;

drop user fstb  cascade;
alter user system account unlock
