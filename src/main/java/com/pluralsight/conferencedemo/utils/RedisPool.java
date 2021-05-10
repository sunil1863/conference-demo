package com.pluralsight.conferencedemo.utils;

import redis.clients.jedis.*;

import java.time.Duration;

public class RedisPool {
    static JedisPool jedisPool = null;

    private RedisPool(){

    }

    public static synchronized JedisPool getInstance(){
        if(jedisPool == null){
            jedisPool = new JedisPool(CreatePoolConfig(), "localhost");
        }

        return jedisPool;
    }

    private static JedisPoolConfig CreatePoolConfig(){
        final JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(128);
        jedisPoolConfig.setMaxIdle(128);
        jedisPoolConfig.setMinIdle(16);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnReturn(true);
        jedisPoolConfig.setTestWhileIdle(true);
        jedisPoolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
        jedisPoolConfig.setNumTestsPerEvictionRun(3);
        jedisPoolConfig.setBlockWhenExhausted(true);
        return jedisPoolConfig;
    }
}
