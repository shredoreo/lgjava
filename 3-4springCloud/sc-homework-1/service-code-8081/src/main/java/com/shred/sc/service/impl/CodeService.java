package com.shred.sc.service.impl;

import com.shred.sc.dao.AuthCodeDao;
import com.shred.sc.pojo.AuthCode;
import com.shred.sc.service.ICodeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class CodeService implements ICodeService {

    @Autowired
    private AuthCodeDao authCodeDao;

    /**
     * 创建验证码，并发送
     * @param email
     * @return
     */
    @Override
    public AuthCode create(String email) {
        AuthCode authCode1 = new AuthCode();
        authCode1.setEmail(email);
        Optional<AuthCode> one = authCodeDao.findOne(Example.of(authCode1));
        //存在则使用，更新对应的字段
        AuthCode authCode2 = one.orElse(authCode1);


//        authCode2.setEmail(email);
        authCode2.setCode(""+(int)((Math.random()*9+1)*100000));
        authCode2.setCreateTime(new Date());
        authCode2.setExpireTime(
                Date.from(
                        LocalDateTime.now().plusMinutes(10).atZone(ZoneId.systemDefault())
                                .toInstant()
                )
        );

        System.out.println("保存验证码");
        authCodeDao.save(authCode2);

        return authCode2;

    }

    @Override
    public int validate(String email, String code) {
        AuthCode authCode = new AuthCode();
        authCode.setCode(code);
        authCode.setEmail(email);
        Optional<AuthCode> one = authCodeDao.findOne(Example.of(authCode));

        if (one.isPresent()) {
            AuthCode result = one.get();
            if (StringUtils.equals(result.getCode(), code)) {
                return result.getExpireTime().after(new Date()) ? 0 : 2;
            } else {
                return 1;
            }
        } else {
            return 1;
        }


    }
}
