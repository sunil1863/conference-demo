package com.pluralsight.conferencedemo.services;

import redis.clients.jedis.JedisPubSub;

import java.time.LocalDateTime;

public class RedisSubscriber extends JedisPubSub {
    @Override
    public void onMessage(String channel, String message) {
        System.out.println(String.format("Received Message: %s on Channel: %s at: %s", message, channel, LocalDateTime.now()));
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        //super.onSubscribe(channel, subscribedChannels);
        System.out.println("from onSubscribe");
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        System.out.println("from onUnsubscribe");
    }
}
