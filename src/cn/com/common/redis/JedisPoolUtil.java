package cn.com.common.redis;

import redis.clients.jedis.Jedis;  
import redis.clients.jedis.JedisPool;  
import redis.clients.jedis.JedisPoolConfig;  
/* 
 * 如果Jedis在使用过程中出错，则也需要还给JedisPool
 * */  
public class JedisPoolUtil {  
    private static JedisPool pool = null;  
  
    //构建redis连接池 
    private static JedisPool getPool() {  
        if (pool == null) {  
            JedisPoolConfig config = new JedisPoolConfig();  
            //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；  
            //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。  
            config.setMaxTotal(500);  
            //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。  
            config.setMaxIdle(5);  
            //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；  
            config.setMaxWaitMillis(1000 * 100);  
            //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；  
            config.setTestOnBorrow(true);  
            pool = new JedisPool(config, "127.0.0.1",6379);  
        }
        return pool;  
    }  
  
    //从连接池中获取连接
    public static Jedis getResource(){
    	return getPool().getResource();
    }
    //归还连接资源
    public static void returnResource(Jedis jedis){
    	if(jedis != null){
    		jedis.close();
    	}
    }
    //销毁连接池
    public static void destory(){
    	if(pool != null){
    		pool.destroy();
    		System.out.println("销毁连接池...");
    	}
    }
    //测试
    public static void main(String[]args){
    	Jedis jedis=null;
    	try{
    		jedis=JedisPoolUtil.getResource();
//    		jedis.slaveof("127.0.0.1", 6379);
        	jedis.set("QQQ", "slaveof...");
        	String foobar = jedis.get("MMM");
        	System.out.println("...:"+foobar);
    	}finally{
    		JedisPoolUtil.returnResource(jedis);
    	}
    	JedisPoolUtil.destory();//应用/服务停止时销毁连接池
    }
}  

/**
#是否以后台进程运行
daemonize yes
  
#指定后台进程的pid文件写入位置
pidfile /var/run/redis.pid
  
#监听端口，默认为6379
port 6379
  
#只接受以下绑定的IP请求
bind 127.0.0.1
  
#设置unix套接字，默认为空，及不通过unix套接字来监听
# unixsocket /tmp/redis.sock
# unixsocketperm 755
  
#客户端空闲多长时间，关闭链接。0表示不关闭
timeout 5
  
# TCP keepalive.
# 如果是非零值，当失去链接时，会使用SO_KEEPALIVE发送TCP ACKs 到客户端。
# 这个参数有两个作用:
# 1.检测断点。
# 2.从网络中间设备来看，就是保持链接
# 在Linux上，设定的时间就是发送ACKs的周期。
# 注意：达到双倍的设定时间才会关闭链接。在其他内核上，周期依赖于内核设置。
# 一个比较合理的值为60s
tcp-keepalive 0
  
# 指定日志级别，以下记录信息依次递减
# debug用于开发/测试
# verbose没debug那么详细
# notice适用于生产线# warning只记录非常重要的信息
loglevel notice
  
#日志文件名称，如果为stdout则输出到标准输出端，如果是以后台进程运行则不产生日志
logfile C:/Users/michael/Desktop/file/work/data/redis/logs/redis.log
  
# 要想启用系统日志记录器，设置一下选项为yes
# syslog-enabled no
  
# 指明syslog身份
# syslog-ident redis
  
# 指明syslog设备。必须是一个用户或者是local0 ~ local7之一
# syslog-facility local0
  
#设置数据库数目，第一个数据库编号为：0
databases 16
  
##############快照#################
  
#在什么条件下保存数据库到磁盘，条件可以有很多个，满足任何一个条件都会精心快照
#在900秒之内有一次key的变化
save 900 1
#在300秒之内，有10个key的变化
save 300 10
#在60秒之内有10000个key变化
save 60 10000
  
#当持久化失败的时候，是否继续提供服务
stop-writes-on-bgsave-error yes
  
#当写入磁盘时，是否使用LZF算法压缩数据，默认为yes
rdbcompression yes
  
#是否添加CRC64校验到每个文件末尾--花费时间保证安全
rdbchecksum yes
  
#磁盘上数据库的保存名称
dbfilename dump.rdb
  
# Redis工作目录，以上数据库保存文件和AOF日志都会写入此目录
dir C:/Users/michael/Desktop/file/work/data/redis/01/
  
##############同步#################
  
#主从复制，当本机是slave时配置
# slaveof <masterip> <masterport>
  
#当主机需要密码验证时候配置
# masterauth <master-password>
  
# 当slave和master丢失链接，或正处于同步过程中。是否响应客户端请求
# 设置为yes表示响应
# 设置为no，直接返回"SYNC with master in progress"（正在和主服务器同步中）
slave-serve-stale-data yes
  
# 设置slave是否为只读。
# 注意：即使slave设置为只读，也不能令其暴露在不受信任的网络环境中
slave-read-only yes
  
# 设置slave给master发送ping的时间间隔
# repl-ping-slave-period 10
  
# 设置数据传输I/O，主机数据、ping响应超时时间，默认60s
# 这个时间一定要比repl-ping-slave-period大，否则会不断检测到超时
# repl-timeout 60
  
# 是否在SYNC后slave socket上禁用TCP_NODELAY？
# 如果你设置为yes，Redis会使用少量TCP报文和少量带宽发送数据给slave。
# 但是这样会在slave端出现延迟。如果使用Linux内核的默认设置，大概40毫秒。
# 如果你设置为no，那么在slave端研究就会减少但是同步带宽要增加。
# 默认我们是为低延迟优化的。
# 但是如果流量特别大或者主从服务器相距比较远，设置为yes比较合理。
repl-disable-tcp-nodelay no
  
# 设置slave优先级，默认为100
# 当主服务器不能正确工作的时候，数字低的首先被提升为主服务器，但是0是禁用选择
slave-priority 100
  
##############安全#################
  
# 设置客户端连接密码，因为Redis响应速度可以达到每秒100w次，所以密码要特别复杂
# requirepass foobared
  
# 命令重新命名，或者禁用。
# 重命名命令为空字符串可以禁用一些危险命令比如：FLUSHALL删除所有数据
# 需要注意的是，写入AOF文件或传送给slave的命令别名也许会引起一些问题
# rename-command CONFIG ""
  
##############限制#################
  
# 设置最多链接客户端数量，默认为10000。
# 实际可以接受的请求数目为设置值减去32，这32是Redis为内部文件描述符保留的
# maxclients 10000
  
# 设置最大使用内存数量，在把Redis当作LRU缓存时特别有用。
# 设置的值要比系统能使用的值要小
# 因为当启用删除算法时，slave输出缓存也要占用内存
# maxmemory <bytes>
  
#达到最大内存限制时，使用何种删除算法
# volatile-lru  使用LRU算法移除带有过期标致的key
# allkeys-lru -> 使用LRU算法移除任何key
# volatile-random -> 随机移除一个带有过期标致的key
# allkeys-random ->  随机移除一个key
# volatile-ttl -> 移除最近要过期的key
# noeviction -> 不删除key，当有写请求时，返回错误
#默认设置为volatile-lru
# maxmemory-policy volatile-lru
  
# LRU和最小TTL算法没有精确的实现
# 为了节省内存只在一个样本范围内选择一个最近最少使用的key，可以设置这个样本大小
# maxmemory-samples 3
  
##############AO模式#################
  
# AOF和RDB持久化可以同时启用
# Redis启动时候会读取AOF文件，AOF文件有更好的持久化保证
appendonly no
  
# AOF的保存名称，默认为appendonly.aof
# appendfilename appendonly.aof
  
# 设置何时写入追加日志，又三种模式
# no：表示由操作系统决定何时写入。性能最好，但可靠性最低
# everysec：表示每秒执行一次写入。折中方案，推荐
# always：表示每次都写入磁盘。性能最差，比上面的安全一些
appendfsync everysec
  
# 当AOF同步策略设定为alway或everysec
# 当后台存储进程（后台存储或者AOF日志后台写入）会产生很多磁盘开销
# 某些Linux配置会使Redis因为fsync()调用产生阻塞很久
# 现在还没有修复补丁，甚至使用不同线程进行fsync都会阻塞我们的同步write(2)调用。
# 为了缓解这个问题，使用以下选项在一个BGSAVE或BGREWRITEAOF运行的时候
# 可以阻止fsync()在主程序中被调用，
no-appendfsync-on-rewrite no
  
# AOF自动重写（合并命令，减少日志大小）
# 当AOF日志大小增加到一个特定比率，Redis调用BGREWRITEAOF自动重写日志文件
# 原理：Redis 会记录上次重写后AOF文件的文件大小。
# 如果刚启动，则记录启动时AOF大小
# 这个基本大小会用来和当前大小比较。如果当前大小比特定比率大，就会触发重写。
# 你也需要指定一个AOF需要被重写的最小值，这样会避免达到了比率。
# 但是AOF文件还很小的情况下重写AOF文件。
# 设置为0禁用自动重写
auto-aof-rewrite-percentage 100
auto-aof-rewrite-min-size 64mb
  
##############LUA脚本#################
# Lua脚本的最大执行时间，单位毫秒
# 超时后会报错，并且计入日志
# 当一个脚本运行时间超过了最大执行时间
# 只有SCRIPT KILL和 SHUTDOWN NOSAVE两个命令可以使用。
# SCRIPT KILL用于停止没有调用写命令的脚本。
# SHUTDOWN NOSAVE是唯一的一个，在脚本的写命令正在执行
# 用户又不想等待脚本的正常结束的情况下，关闭服务器的方法。
# 以下选项设置为0或负数就会取消脚本执行时间限制
lua-time-limit 5000
  
##############慢查询#################
  
# Redis慢查询日志记录超过设定时间的查询，且只记录执行命令的时间
# 不记录I/O操作，比如：和客户端交互，发送回复等。
# 时间单位为微妙，1000000微妙 = 1 秒
# 设置为负数会禁用慢查询日志，设置为0会记录所有查询命令
slowlog-log-slower-than 10000
  
# 日志长度没有限制，但是会消耗内存。超过日志长度后，最旧的记录会被移除
# 使用SLOWLOG RESET命令可以回收内存
slowlog-max-len 128
  
##############高级设置###############
  
# 当有少量条目的时候，哈希使用高效内存数据结构。最大的条目也不能超过设定的阈值。# “少量”定义如下：
hash-max-ziplist-entries 512
hash-max-ziplist-value 64
  
# 和哈希编码一样，少量列表也以特殊方式编码节省内存。“少量”设定如下：
list-max-ziplist-entries 512
list-max-ziplist-value 64
  
# 集合只在以下情况下使用特殊编码来节省内存
# -->集合全部由64位带符号10进制整数构成的字符串组成
# 下面的选项设置这个特殊集合的大小。
set-max-intset-entries 512
  
# 当有序集合的长度和元素设定为以下数字时，又特殊编码节省内存
zset-max-ziplist-entries 128
zset-max-ziplist-value 64
  
# 哈希刷新使用每100个CPU毫秒中的1毫秒来帮助刷新主哈希表（顶级键值映射表）。
#  Redis哈希表使用延迟刷新机制，越多操作，越多刷新。
# 如果服务器空闲，刷新操作就不会进行，更多内存会被哈希表占用
# 默认每秒进行10次主字典刷新，释放内存。
# 如果你有硬性延迟需求，偶尔2毫秒的延迟无法忍受的话。设置为no
# 否则设置为yes
activerehashing yes
  
# 客户端输出缓存限制强迫断开读取速度比较慢的客户端
# 有三种类型的限制
# normal -> 正茶馆你客户端
# slave  -> slave和 MONITOR
# pubsub -> 客户端至少订阅了一个频道或者模式
# 客户端输出缓存限制语法如下（时间单位：秒）
# client-output-buffer-limit <类别> <强制限制> <软性限制> <软性时间>
# 达到强制限制缓存大小，立刻断开链接。
# 达到软性限制，仍然会有软性时间大小的链接时间
# 默认正常客户端无限制，只有请求后，异步客户端数据请求速度快于它能读取数据的速度
# 订阅模式和主从客户端又默认限制，因为它们都接受推送。
# 强制限制和软性限制都可以设置为0来禁用这个特性
client-output-buffer-limit normal 0 0 0
client-output-buffer-limit slave 256mb 64mb 60
client-output-buffer-limit pubsub 32mb 8mb 60
  
# 设置Redis后台任务执行频率，比如清除过期键任务。
# 设置范围为1到500，默认为10.越大CPU消耗越大，延迟越小。
# 建议不要超过100
hz 10
  
# 当子进程重写AOF文件，以下选项开启时，AOF文件会每产生32M数据同步一次。
# 这有助于更快写入文件到磁盘避免延迟
# aof-rewrite-incremental-fsync yes
  
##############包含#################
  
#引入标准模板
# include /path/to/other.conf
 */
