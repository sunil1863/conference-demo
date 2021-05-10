package com.pluralsight.conferencedemo;

//import org.springframework.boot.ApplicationRunner;
import com.pluralsight.conferencedemo.services.RedisListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
//import org.springframework.context.annotation.Bean;
//import org.springframework.core.env.Environment;

@SpringBootApplication
public class ConferenceDemoApplication implements CommandLineRunner {

	@Autowired
	private YAMLConfig myConfig;

	@Value("${redishost}")
	String redishosturl;

	@Value("${redisport}")
	private Integer redisPort;

	public static RedisListener getRedisListener() {
		return redisListener;
	}

	public static Thread getListenerThread() {
		return listenerThread;
	}

	static RedisListener redisListener;
	static Thread listenerThread;

	public static ConfigurableApplicationContext getAppctx() {
		return appctx;
	}

	public static void setAppctx(ConfigurableApplicationContext appctx) {
		ConferenceDemoApplication.appctx = appctx;
	}

	static ConfigurableApplicationContext appctx;

	public static void main(String[] args)
	{
		//SpringApplication.run(ConferenceDemoApplication.class, args);

		SpringApplication app = new SpringApplication(ConferenceDemoApplication.class);
		appctx = app.run();
		System.out.println("SpringApplication has started.");
	}

	public void run(String... args) throws Exception {
		System.out.println("using environment: " + myConfig.getEnvironment());
		System.out.println("name: " + myConfig.getName());
		System.out.println("enabled:" + myConfig.isEnabled());
		System.out.println("servers: " + myConfig.getServers());

		redisListener = new RedisListener(redishosturl, redisPort);
		listenerThread = new Thread(redisListener);
		listenerThread.start();
		Thread.sleep(1000);
	}
}
