package com.shred.sc;

import org.springframework.web.bind.annotation.PathVariable;

public interface CodeService {
    String create(String email);

    Integer verifyCode(String email, String code);
}
