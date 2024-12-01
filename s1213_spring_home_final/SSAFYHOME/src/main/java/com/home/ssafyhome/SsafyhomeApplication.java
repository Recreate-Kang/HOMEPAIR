package com.home.ssafyhome;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({
        "com.home.ssafyhome.**.mapper",
})
public class SsafyhomeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsafyhomeApplication.class, args);
    }

}
