package com.shred.sc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;

@EnableBinding(Sink.class)
public class MessageConsumerService {

    //引入输入通道
    @StreamListener(Sink.INPUT)
    public void receiveMessages(Message<String> message) {
        System.out.println("====接收到的消息" + message);
    }
}
