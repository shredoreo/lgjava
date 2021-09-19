package com.shred.sc.utils;

import java.util.UUID;

public class TokenUtil {
    public static String geneToken(String email, String password){
        return UUID.fromString(email+  password).toString();
    }
}
