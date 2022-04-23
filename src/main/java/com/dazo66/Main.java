package com.dazo66;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication(scanBasePackages = {"com.dazo66", "com.aye10032"})
public class Main {

    public static void main(String[] args) throws IOException {
        System.load(System.getProperty("user.dir") + "\\data\\cv\\opencv_java430.dll");
        SpringApplication.run(Main.class, args);
    }


}
