package com.shred.sc.service;

import org.springframework.stereotype.Service;

public interface IUserService {
    boolean register(String email, String password,String code);
    boolean isRegisterd(String email);
    String login(String email, String password);
    String getInfoByToken(String token);
}
