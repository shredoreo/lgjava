package com.shred.sc.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class TokenUtil {
    //进行MD5加密
    public static String toMD5(String password,String salt){
        return DigestUtils.md5Hex(password+salt);
    }
    public static String salt(){
        return StringUtils.replace(UUID.randomUUID().toString(),"-","");
    }

    /**
     * 使用邮箱+密码 生成token
     * @param email
     * @param password
     * @return
     */
    public static String geneToken(String email, String password){
        return toMD5(email+password, "xx");
    }
}
