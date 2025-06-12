package com.eh.digitalpathology.dicompurge;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DicomPurgeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DicomPurgeApplication.class, args);
    }


}

