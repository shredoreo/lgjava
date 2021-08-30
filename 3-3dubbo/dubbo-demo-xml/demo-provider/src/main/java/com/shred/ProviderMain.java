package com.shred;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class ProviderMain {
    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new
                ClassPathXmlApplicationContext("classpath:dubbo-provider.xml");
        context.start();
        System.in.read();
    }
}
