package com.ny.archive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // BaseEntity 자동 시간 생성
@SpringBootApplication
public class NyArchiveBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(NyArchiveBeApplication.class, args);
    }

}
