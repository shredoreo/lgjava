package com.shred.sc.service.impl;

import com.shred.sc.service.IMessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;

//Source.class 输出通道的定义，SpringCloud Stream内值的通道
@EnableBinding(Source.class)//
public class MessageProducerImpl implements IMessageProducer {

    //将MessageChannel的封装对象Source注入到这里使用
    @Autowired
    private Source source;

    @Override
    public void sendMessage(String content) {
        //向mq发送消息，并非直接操作mq，而是操作sc stream
        //使用通道向外发出消息，指的是source里的output通道
        source.output().send(MessageBuilder.withPayload(content).build());
    }

}
