package com.bonusCaluculator;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Scanner;


@SpringBootApplication
@EnableProcessApplication
@EnableEurekaClient
@EnableDiscoveryClient
@RestController
public class Application {

  @Autowired
  private DiscoveryClient discoveryClient;
  private static Application instance;


  public static void main(String[] args) {
    ConfigurableApplicationContext applicationContext = SpringApplication.run(Application.class);

    Scanner keyboard = new Scanner(System.in);
    String input="";
    while (!input.equals("close")) {
      System.out.println("Write 'close' to shutdown service.");
      input = keyboard.next();
    }
    applicationContext.close();

  }

  public Application(){
    instance = this;
  }


  public static String getServiceURL(String serviceID) throws Exception {
    DiscoveryClient discoveryClient = Application.instance.discoveryClient;
    ArrayList<ServiceInstance> instances = (ArrayList<ServiceInstance>) discoveryClient.getInstances(serviceID);
    if(instances.size()==0)
      throw new Exception("No Eureka instance with service ID: "+ serviceID +" found.");
    String url= instances.get(0).getUri().toString();
    return url;
  }

  @RequestMapping("/")
  public String home(){
    return "Hello World";
  }

}