package com.DataBaseRestApi.DataBaseRestApi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Scanner;

@SpringBootApplication
@EnableEurekaClient
@RestController
public class DataBaseRestApiApplication {

	public static void main(String[] args) {
		DataBaseConnection.getInstance().connect();
		ConfigurableApplicationContext applicationContext = SpringApplication.run(DataBaseRestApiApplication.class, args);


		Scanner keyboard = new Scanner(System.in);
		String input="";
		while (!input.equals("close")) {
			System.out.println("Write 'close' to shutdown service.");
			input = keyboard.next();
		}
		applicationContext.close();
		DataBaseConnection.getInstance().close();
	}




}
