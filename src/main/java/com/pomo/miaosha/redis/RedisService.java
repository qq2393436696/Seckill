package com.pomo.miaosha.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.ArrayList;
import java.util.List;

@Service
public class RedisService {

    @Autowired
    JedisPool jedisPool;

    /**
     * 获取单个对象
     */
    /*
    * 1、建立连接
    * 2、生成真正的key，并且根据key查询str
    * 3、String转化成Bean
    * 4、返回Bean类
    *
    *
    *
    * */
    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();   //获取一个连接
            //生成真正的key
            String realKey = prefix.getPrefix() + key;
            String str = jedis.get(realKey);  //根据真正的key查询str
            T t = stringToBean(str, clazz); //String转化成Bean
            return t;
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 设置对象
     */

    /*
    *
    * 1、获取一个连接
    * 2、将value转化成str
    * 3、以类名+id生成真正的key
    * 4、获取存活时间expireSeconds
    * 5、判断存活时间，如果为零，则创建一个，如果不为零，则替换
    * */
    public <T> Boolean set(KeyPrefix prefix, String key, T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource(); //获取一个连接
            String str = beanToString(value);  //把value转化成str
            if(str == null || str.length() <= 0){
                return false;
            }

            String realKey = prefix.getPrefix() + key;//生成真正的key
            int seconds = prefix.expireSeconds();
            if(seconds <= 0) {
                jedis.set(realKey, str);
            } else {
                jedis.setex(realKey, seconds, str);//Redis Setex 命令为指定的 key 设置值及其过期时间。如果 key 已经存在， SETEX 命令将会替换旧的值。
            }
            return true;
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 判断key是否存在
     */
    public <T> Boolean exists(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey = prefix.getPrefix() + key;
            return jedis.exists(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 删除
     */
    /*
    * 1、建立连接
    * 2、生成真正的key
    * 3、删除
    * 4、返回值
    * */
    public <T> Boolean delete(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource(); //建立连接
            //生成真正的key
            String realKey = prefix.getPrefix() + key;
            long ret = jedis.del(realKey);
            return ret > 0;
        } finally {
            returnToPool(jedis);
        }
    }

    public boolean delete(KeyPrefix prefix) {
        if(prefix == null) {
            return false;
        }
        List<String> keys = scanKeys(prefix.getPrefix());
        if(keys==null || keys.size() <= 0) {
            return true;
        }
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.del(keys.toArray(new String[0]));
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    public List<String> scanKeys(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            List<String> keys = new ArrayList<String>();
            String cursor = "0";
            ScanParams sp = new ScanParams();
            sp.match("*"+key+"*");
            sp.count(100);
            do{
                ScanResult<String> ret = jedis.scan(cursor, sp);
                List<String> result = ret.getResult();
                if(result!=null && result.size() > 0){
                    keys.addAll(result);
                }
                //再处理cursor
                cursor = ret.getStringCursor();
            }while(!cursor.equals("0"));
            return keys;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 增加值
     */
    public <T> Long incr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey = prefix.getPrefix() + key;
            return jedis.incr(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 减少值
     */
    /*
    * 1、建立连接
    * 2、生成真正的key
    * 3、减少值
    * 4、返回值
    * */
    public <T> Long decr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource(); //获取连接
            //生成真正的key
            String realKey = prefix.getPrefix() + key;
            return jedis.decr(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    public static <T> String beanToString(T value) {
        if(value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if(clazz == int.class || clazz == Integer.class) {
            return ""+value;
        } else if (clazz == String.class) {
            return (String)value;
        } else if (clazz == long.class || clazz == Long.class) {
            return ""+value;
        } else {
            return JSON.toJSONString(value);
        }
    }

    @SuppressWarnings("unchecked")
    public static  <T> T stringToBean(String str, Class<T> clazz) {
        if(str == null || str.length() <= 0 || clazz == null) {
            return null;
        }
        if(clazz == int.class || clazz == Integer.class) {
            return (T)Integer.valueOf(str);
        } else if (clazz == String.class) {
            return (T)str;
        } else if (clazz == long.class || clazz == Long.class) {
            return (T)Long.valueOf(str);
        } else {
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }

    private void returnToPool(Jedis jedis) {
        if(jedis != null) {
            jedis.close();
        }
    }
}
