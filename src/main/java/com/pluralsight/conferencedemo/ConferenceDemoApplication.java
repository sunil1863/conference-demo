package com.pluralsight.conferencedemo;

//import org.springframework.boot.ApplicationRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;
//import org.springframework.core.env.Environment;

@SpringBootApplication
public class ConferenceDemoApplication implements CommandLineRunner {

	@Autowired
	private YAMLConfig myConfig;

	public static void main(String[] args)
	{
		//SpringApplication.run(ConferenceDemoApplication.class, args);

		SpringApplication app = new SpringApplication(ConferenceDemoApplication.class);
		app.run();
	}

	public void run(String... args) throws Exception {
		System.out.println("using environment: " + myConfig.getEnvironment());
		System.out.println("name: " + myConfig.getName());
		System.out.println("enabled:" + myConfig.isEnabled());
		System.out.println("servers: " + myConfig.getServers());
	}
}
