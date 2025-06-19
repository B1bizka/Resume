package org.libin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan("org.libin.dao.models")
@EnableJpaRepositories("org.libin.dao.repository")
@SpringBootApplication
public class CatApplication {
    public static void main(String[] args) {
        SpringApplication.run(CatApplication.class, args);
    }
}