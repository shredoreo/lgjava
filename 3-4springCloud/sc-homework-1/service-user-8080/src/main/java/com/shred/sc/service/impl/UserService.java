package com.shred.sc.service.impl;

import com.shred.sc.dao.TokenDao;
import com.shred.sc.feign.CodeServiceFeignClient;
import com.shred.sc.pojo.Token;
import com.shred.sc.service.IUserService;
import com.shred.sc.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j(topic = "用户服务")
public class UserService implements IUserService {
    @Autowired
    private TokenDao tokenDao;
    @Autowired
    private CodeServiceFeignClient codeFeign;

    @Override
    public int register(String email, String password, String code) {

        //校验验证码
        Integer validateRes = codeFeign.verifyCode(email, code);

        Token token = new Token();
        token.setToken(TokenUtil.geneToken(email, password));
        token.setEmail(email);

        if (validateRes.compareTo(0) == 0) {
            //校验注册
            if (isRegisterd(email)) {
                log.info("用户存在，已注册");
                return 3;
            }
            log.warn("注册时保存用户");
            //保存用户
            tokenDao.save(token);
            return 0;
        } else if (validateRes ==2){
            log.warn("验证码超时");
            return 2;
        } else {
            log.warn("验证码错误");
            return 1;
        }
    }

    @Override
    public boolean isRegisterd(String email) {
        Token token = new Token();
        token.setEmail(email);

        return tokenDao.exists(Example.of(token));
    }

    @Override
    public String login(String email, String password) {
        Token token = new Token();
        token.setToken(TokenUtil.geneToken(email, password));
        token.setEmail(email);
        log.warn(token.toString());

        Optional<Token> one = tokenDao.findOne(Example.of(token));

        return one.get().getEmail();
    }

    @Override
    public Token findToken(String email, String password) {
        Token token = new Token();
        token.setToken(TokenUtil.geneToken(email, password));
        token.setEmail(email);
        Optional<Token> one = tokenDao.findOne(Example.of(token));


        return one.get();
    }

    @Override
    public String getInfoByToken(String token) {
        Token token1 = new Token();
        token1.setToken(token);
        Optional<Token> one = tokenDao.findOne(Example.of(token1));

        return one.get().getEmail();
    }


}
