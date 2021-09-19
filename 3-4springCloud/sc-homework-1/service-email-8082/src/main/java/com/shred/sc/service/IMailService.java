package com.shred.sc.service;

public interface IMailService {
    boolean sendMail(String to, String subject, String content);
}
