package com.pluralsight.conferencedemo.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.auditing.CurrentDateTimeProvider;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.time.LocalDateTime;

public class RedisListener implements Runnable {
    final String channelName = "testchannel";

    String redishosturl;
    Integer redisPort;

    Jedis subscriber;

    RedisSubscriber redisSubscriber;

    public RedisListener(String redishot, Integer redisPort){
        redishosturl = redishot;
        this.redisPort = redisPort;
    }

    @Override
    public void run() {
        redisSubscriber = new RedisSubscriber();
        //Jedis subscriber = new Jedis("localhost", 6379);
        System.out.println(redishosturl);
        System.out.println(redisPort);
        Jedis subscriber = new Jedis(redishosturl, redisPort);
        subscriber.subscribe(redisSubscriber, channelName);
    }

    public void Unsubscribe(){
        redisSubscriber.unsubscribe(channelName);
    }
}
