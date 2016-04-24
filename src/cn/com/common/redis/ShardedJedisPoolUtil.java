package cn.com.common.redis;

import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * 主从模式
 */
public class ShardedJedisPoolUtil {

	private static ShardedJedisPool pool=null;
	
	//构建redis连接池 
	private static ShardedJedisPool getPool(){
		if (pool == null) {
			JedisPoolConfig config = new JedisPoolConfig();  
            //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；  
            //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。  
            config.setMaxTotal(500);  
            //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。  
            config.setMaxIdle(1000 * 60);  
            //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；  
            config.setMaxWaitMillis(1000 * 100);  
            //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；  
            config.setTestOnBorrow(true); 
            
            List<JedisShardInfo> jdsInfoList =new ArrayList<JedisShardInfo>(2);
            JedisShardInfo infoA = new JedisShardInfo("127.0.0.1", "6479");
//            infoA.setPassword("admin");
            JedisShardInfo infoB = new JedisShardInfo("127.0.0.1", "6379");
//            infoB.setPassword("admin");
            jdsInfoList.add(infoA);
            jdsInfoList.add(infoB);
            pool =new ShardedJedisPool(config, jdsInfoList);
		}
		return pool;
	}
	 //从连接池中获取连接
    public static ShardedJedis getResource(){
    	return getPool().getResource();
    }
    //归还连接资源
    public static void returnResource(ShardedJedis jedis){
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
    	ShardedJedis jedis=null;
    	try{
    		jedis=ShardedJedisPoolUtil.getResource();
        	jedis.set("III", "III");
        	String foobar = jedis.get("III");
        	System.out.println("...:"+foobar);
    	}finally{
    		ShardedJedisPoolUtil.returnResource(jedis);
    	}
    	ShardedJedisPoolUtil.destory();//应用/服务停止时销毁连接池
    }
}
