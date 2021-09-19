package com.shred.sc.dao;

import com.shred.sc.pojo.AuthCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

public interface AuthCodeDao extends JpaRepository<AuthCode, Long> {
}
