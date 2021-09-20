package com.shred.sc.service.impl;

import com.shred.sc.service.IMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService implements IMailService {

    @Autowired
    private JavaMailSender jms;

    @Value("${spring.mail.username}")
    private String username;

    @Override
    public boolean sendMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(username); // 邮件发送者
        message.setTo(to); // 邮件接受者
        message.setSubject(subject); // 主题
        message.setText(content); // 内容

        try {
            jms.send(message);

        } catch (Exception e) {

            return false;
        }
        return true;
    }
}
