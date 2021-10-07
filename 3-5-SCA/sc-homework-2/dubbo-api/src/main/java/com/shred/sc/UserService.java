package com.shred.sc;

public interface UserService {
    int register(String email, String password,String code);
    boolean isRegisterd(String email);
    String login(String email, String password);
    String findToken(String email, String password);
    String getInfoByToken(String token);
    String getInfo(String token);
}
