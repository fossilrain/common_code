--所有drop脚本、create脚本依次执行。该脚本将清除DB当前已有的所有数据--

set define off;
@ droptable.sql;
@ dropproc.sql;
@ createtable.sql;
@ createproc.sql;
@ insertdata.sql;
set define on;


