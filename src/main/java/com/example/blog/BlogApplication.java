package com.example.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class BlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}

	@PostConstruct
	public void init() {
		// 设置默认时区为 Asia/Shanghai
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
	}
}