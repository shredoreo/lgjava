package com.shred.sc.dao;

import com.shred.sc.pojo.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenDao extends JpaRepository<Token, Long> {
}
