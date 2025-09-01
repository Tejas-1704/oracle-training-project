package com.oracle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ProjectCoreApplication {

        public static void main(String[] args) {
                SpringApplication.run(ProjectCoreApplication.class, args);
        }

}
