package com.pluralsight.conferencedemo.controllers;

import com.pluralsight.conferencedemo.ConferenceDemoApplication;
import com.pluralsight.conferencedemo.models.DataMessage;
import com.pluralsight.conferencedemo.utils.RedisPool;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.*;

@RestController
@Log4j2
public class HomeController {

    //Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Value("${custom.app.version}")
    private String appVersion;

    @Value("${redishost}")
    private String redisHost;

    @Value("${redisport}")
    private Integer reidsPort;

    @GetMapping
    @RequestMapping("/")
    public Map getStatus() {
        log.info("getStatus Called");
        Map map = new HashMap<String, String>();
        map.put("app.version", appVersion);
        log.info("getStatus Completed");
        return map;
    }

    //Study - https://www.baeldung.com/jedis-java-redis-client-library
    @GetMapping
    @RequestMapping("/cache")
    public String Cache() {
        try {
            //Jedis jedis = new Jedis("localhost", 6379);
            Jedis jedis = new Jedis(redisHost, reidsPort);
            jedis.set("hellokey", "helloworld");
            String resp1 = jedis.get("hellokey");
            return "Cache provided response for hellokey: " + resp1;
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    @GetMapping
    @RequestMapping("/cacheset")
    public String CacheSet() {
        try {
            //Jedis jedis = new Jedis("localhost", 6379);
            Jedis jedis = new Jedis(redisHost, reidsPort);
            jedis.sadd("Item_A", "Ord1");
            jedis.sadd("Item_A", "Ord2");
            jedis.sadd("Item_A", "Ord3", "Ord4");
            jedis.sadd("Item_A", "Ord5");

            jedis.sadd("Item_B", "Ord2");
            jedis.sadd("Item_B", "Ord3");

            jedis.sadd("State_CL", "Ord1");
            jedis.sadd("State_CL", "Ord2");
            jedis.sadd("State_CA", "Ord3");
            jedis.sadd("State_MA", "Ord4");
            jedis.sadd("State_WA", "Ord5");

            //find orders of Item A from state California
            Set<String> itemACaliforniaOrders = jedis.sinter("Item_A", "State_CA");

            String orders = itemACaliforniaOrders.stream().reduce("", (String result, String str) -> result + "," + str);
            return orders;
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    @GetMapping
    @RequestMapping("/cachehash")
    public String CacheHash() {
        try {
            //Jedis jedis = new Jedis("localhost", 6379);
            Jedis jedis = new Jedis(redisHost, reidsPort);
            jedis.hset("Ord1", "OrdNumber", "1001");
            jedis.hset("Ord1", "OrdDate", "12/25/2020");
            jedis.hset("Ord1", "OrdValue", "100");

            String ordVal = jedis.hget("Ord1", "OrdValue");

            Map<String, String> ord1Fields = jedis.hgetAll("Ord1");

            StringBuffer buffer = new StringBuffer();
            ord1Fields.forEach((key, value) -> {
                        buffer.append(key);
                        buffer.append(":");
                        buffer.append(value);
                        buffer.append(",");
                    }
            );

            return buffer.toString();
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    @GetMapping
    @RequestMapping("/cacheexpire")
    public String CacheExpire() {
        try {
            //Jedis jedis = new Jedis("localhost", 6379);
            Jedis jedis = new Jedis(redisHost, reidsPort);
            jedis.set("hellokey", "helloworld");
            jedis.expire("hellokey", 2);
            String resp1 = jedis.get("hellokey");

            Thread.sleep(2000);
            Boolean exists = jedis.exists("hellokey");

            return "Cache provided response for hellokey: " + resp1 + " - expiry check after 2 seconds: " + exists.toString();
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    @GetMapping
    @RequestMapping("/cachescore")
    public String CacheScore() {
        try {
            //Jedis jedis = new Jedis("localhost", 6379);
            Jedis jedis = new Jedis(redisHost, reidsPort);
            jedis.hset("Prd1", "Name", "Dining Table");
            jedis.hset("Prd1", "Type", "Glass");
            jedis.hset("Prd1", "Capacity", "6");
            jedis.hset("Prd1", "Price", "40000");

            jedis.hset("Prd2", "Name", "Sofa");
            jedis.hset("Prd2", "Type", "L Shape");
            jedis.hset("Prd2", "Capacity", "7");
            jedis.hset("Prd2", "Price", "60000");

            jedis.hset("Prd3", "Name", "Bed");
            jedis.hset("Prd3", "Type", "King Size");
            jedis.hset("Prd3", "Capacity", "2");
            jedis.hset("Prd3", "Price", "48000");

            jedis.zadd("PrdSet", 40000, "Prd1");
            jedis.zadd("PrdSet", 60000, "Prd2");
            jedis.zadd("PrdSet", 48000, "Prd3");

            StringBuffer prdResBuffer = new StringBuffer();
            Set<String> prdset1 = jedis.zrangeByScore("PrdSet", 40000, 48000);
            prdset1.stream().forEach(x -> {
                Map<String, String> prdFields = jedis.hgetAll(x);
                StringBuffer prdBuffer = new StringBuffer();
                prdFields.forEach((k, v) -> {
                    prdBuffer.append(k);
                    prdBuffer.append(":");
                    prdBuffer.append(v);
                    prdBuffer.append(" - ");
                });
                prdResBuffer.append("\r\n");
                prdResBuffer.append(prdBuffer.toString());
            });

            return "ZRange Product Result: " + prdResBuffer.toString();

            //Set<Tuple> setElements = jedis.zrangeByScoreWithScores("testsorted", -1.0, 1.0);
            //setElements.forEach(x -> {String str = x.getElement() + ": " + x.getScore();});
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    @GetMapping
    @RequestMapping("/cachetrans")
    public String CacheTransaction() {
        try {
            //Jedis jedis = new Jedis("localhost", 6379);
            Jedis jedis = new Jedis(redisHost, reidsPort);

            Transaction trans = jedis.multi();
            trans.hset("Prd1", "Name", "Dining Table");
            trans.hset("Prd1", "Type", "Glass");

            //Created error delibrately - transaction will discard this command only
            trans.lpop("PrdSet");

            trans.hset("Prd1", "Capacity", "6");
            trans.hset("Prd1", "Price", "40000");
            trans.zadd("PrdSet", 40000, "Prd1");

            trans.exec();

            Map<String, String> prdFields = jedis.hgetAll("Prd1");
            StringBuffer prdBuffer = new StringBuffer();
            prdFields.forEach((k, v) -> {
                prdBuffer.append(k);
                prdBuffer.append(":");
                prdBuffer.append(v);
                prdBuffer.append(" - ");
            });

            //if any command fails to queue then None of the commands execute inside transaction
            //and all commands queued are discarded at the time of exec

            return prdBuffer.toString();
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    @GetMapping
    @RequestMapping("/cachepipeline")
    public String CachePipeline() {
        try {
            //Jedis jedis = new Jedis("localhost", 6379);
            Jedis jedis = new Jedis(redisHost, reidsPort);

            Pipeline pipe = jedis.pipelined();

            pipe.hset("Prd1", "Name", "Dining Table");
            pipe.hset("Prd1", "Type", "Glass");
            pipe.hset("Prd1", "Capacity", "6");
            pipe.hset("Prd1", "Price", "40000");

            pipe.hset("Prd2", "Name", "Sofa");
            pipe.hset("Prd2", "Type", "L Shape");
            pipe.hset("Prd2", "Capacity", "7");
            pipe.hset("Prd2", "Price", "60000");

            pipe.hset("Prd3", "Name", "Bed");
            pipe.hset("Prd3", "Type", "King Size");
            pipe.hset("Prd3", "Capacity", "2");
            pipe.hset("Prd3", "Price", "48000");

            pipe.zadd("PrdSet", 40000, "Prd1");
            pipe.zadd("PrdSet", 60000, "Prd2");
            pipe.zadd("PrdSet", 48000, "Prd3");

            Response<Set<String>> prdset1Resp = pipe.zrangeByScore("PrdSet", 50000, 100000);
            pipe.sync();

            StringBuffer prdResBuffer = new StringBuffer();
            Set<String> prdset1 = prdset1Resp.get();
            prdset1.stream().forEach(x -> {
                Map<String, String> prdFields = jedis.hgetAll(x);
                StringBuffer prdBuffer = new StringBuffer();
                prdFields.forEach((k, v) -> {
                    prdBuffer.append(k);
                    prdBuffer.append(":");
                    prdBuffer.append(v);
                    prdBuffer.append(" - ");
                });
                prdResBuffer.append("\r\n");
                prdResBuffer.append(prdBuffer.toString());
            });

            return "ZRange Product Result: " + prdResBuffer.toString();

            //Set<Tuple> setElements = jedis.zrangeByScoreWithScores("testsorted", -1.0, 1.0);
            //setElements.forEach(x -> {String str = x.getElement() + ": " + x.getScore();});
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    @GetMapping
    @RequestMapping("/cachelist")
    public String CacheList() {
        try {
            //Jedis jedis = new Jedis("localhost", 6379);
            Jedis jedis = new Jedis(redisHost, reidsPort);
            jedis.lpush("tweets", "one");
            jedis.lpush("tweets", "two");
            jedis.lpush("tweets", "three");
            jedis.lpush("tweets", "four");
            jedis.lpush("tweets", "five");

            Long listlength = jedis.llen("tweets");
            String val1 = jedis.rpop("tweets");
            String validx = jedis.lindex("tweets", 1);
            Long listlength2 = jedis.llen("tweets");

            return "Length1: " + listlength + " - rpopval: " + val1 + " - lindexval: " + validx + " - Length2: " + listlength2;
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    //Study - https://www.baeldung.com/jedis-java-redis-client-library
    @GetMapping
    @RequestMapping("/cachepool")
    public String CachePool() {
        try (Jedis jedis = RedisPool.getInstance().getResource()){
            //Jedis jedis = new Jedis("localhost", 6379);

            jedis.set("hellopoolkey", "helloworld from pooled connection");
            String resp1 = jedis.get("hellopoolkey");
            return "Cache provided response for hellopoolkey: " + resp1;

            //Jedis Cluster - Requires a Redis Cluster setup first
            //JedisCluster jedisCluster = new JedisCluster(new HostAndPort("localhost", 6379));
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    @PostMapping("/cachepublish")
    public String CachePublish(@RequestBody DataMessage bodyMessage){
        try {
            System.out.println("In CachePublish for message id: " + bodyMessage.getId());
            Jedis jedis = new Jedis(redisHost, reidsPort);
            Long result = jedis.publish("testchannel", bodyMessage.getMessage());
            return "Published message id: " + bodyMessage.getId() + " with result: " + result;
        }
        catch (Exception ex) {
            return ex.getMessage();
        }
    }

    @GetMapping("/stop")
    public String Stop(){
        SpringApplication.exit(ConferenceDemoApplication.getAppctx(), new ExitCodeGenerator() {
            @Override
            public int getExitCode() {
                return 0;
            }
        });

        return "exited application";
    }
}