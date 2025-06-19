package org.libin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


//@EntityScan("org.libin.dao.models")
//@EnableJpaRepositories("org.libin.dao.repository")

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}