package com.home.ssafyhome.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan({"com.home.ssafyhome.auth.mapper"})
public class MybatisConfig {
}
