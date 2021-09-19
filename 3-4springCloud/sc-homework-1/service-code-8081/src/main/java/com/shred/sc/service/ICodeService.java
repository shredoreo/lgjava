package com.shred.sc.service;

import com.shred.sc.pojo.AuthCode;

public interface ICodeService {
    AuthCode create(String email);

    int validate(String email, String code);
}
