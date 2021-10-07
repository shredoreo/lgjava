package com.shred.sc;


public interface EmailService {
    Boolean sendCode(String to, String subject, String content);
}
