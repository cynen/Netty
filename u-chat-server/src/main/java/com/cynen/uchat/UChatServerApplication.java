package com.cynen.uchat;

import com.cynen.uchat.utils.IdWorker;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan(basePackages = "com.cynen.uchat.mapper")
public class UChatServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UChatServerApplication.class, args);
    }

    @Bean
    public IdWorker idWorker(){
        return  new IdWorker(0,0);
    }

}
