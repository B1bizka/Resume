package org.libin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("org.libin.dao.models")
@EnableJpaRepositories("org.libin.dao.repository")
public class OwnerApplication {
    public static void main(String[] args){ SpringApplication.run(OwnerApplication.class,args);}
}