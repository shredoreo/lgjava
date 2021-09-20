package com.shred.sc.service;

import com.shred.sc.pojo.Token;
import org.springframework.stereotype.Service;

public interface IUserService {
    int register(String email, String password,String code);
    boolean isRegisterd(String email);
    String login(String email, String password);
    Token findToken(String email, String password);
    String getInfoByToken(String token);
}
