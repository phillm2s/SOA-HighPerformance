package com.HighPerformance.WebInterface;

import com.HighPerformance.WebInterface.REST.camunda.Test;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.Scanner;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
public class WebInterfaceApplication {

	@Autowired
	private EurekaClient eurekaClient;
	private static WebInterfaceApplication instance;

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(WebInterfaceApplication.class, args);

		Scanner keyboard = new Scanner(System.in);
		String input="";
		while (!input.equals("close")) {
			System.out.println("Write 'close' to shutdown service.");
			input = keyboard.next();
		}
		applicationContext.close();
	}


	WebInterfaceApplication(){
		instance = this;
	}


	public static String getServiceURL(String serviceID) throws Exception {
		EurekaClient eurekaClient = WebInterfaceApplication.instance.eurekaClient;
		InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka(serviceID, false);
		String url = instanceInfo.getHomePageUrl();
		return url;
	}
}
