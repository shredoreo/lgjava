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
@Slf4j
public class UserService implements IUserService {
    @Autowired
    private TokenDao tokenDao;
    @Autowired
    private CodeServiceFeignClient codeFeign;

    @Override
    public boolean register(String email, String password, String code) {
        //校验注册
        if (isRegisterd(email)) {
            log.info("已注册");
            return false;
        }
        //校验验证码
        Integer validateRes = codeFeign.verifyCode(email, code);

        Token token = new Token();
        token.setToken(TokenUtil.geneToken(email, password));
        token.setEmail(email);

        if (validateRes.compareTo(0) == 0) {
            //保存用户
            tokenDao.save(token);
            return true;
        } else {
            return false;
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
        Optional<Token> one = tokenDao.findOne(Example.of(token));
        return one.get().getEmail();
    }

    @Override
    public String getInfoByToken(String token) {
        Token token1 = new Token();
        token1.setToken(token);
        Optional<Token> one = tokenDao.findOne(Example.of(token1));

        return one.get().getEmail();
    }


}
