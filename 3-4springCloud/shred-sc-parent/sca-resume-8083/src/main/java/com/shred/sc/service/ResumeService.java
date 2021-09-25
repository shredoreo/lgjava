package com.shred.sc.service;

import com.shred.sc.pojo.Resume;

public interface ResumeService {
    Resume findDefaultByUserId(Long userId);
}
