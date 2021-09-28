package com.shred.sc.service.impl;

import com.shred.sc.dao.ResumeDao;
import com.shred.sc.pojo.Resume;
import com.shred.sc.service.ResumeService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;

@Service
public class ResumeServiceImpl implements ResumeService {
    @Autowired
    private ResumeDao resumeDao;

    @Override
    public Integer findDefaultByUserId(Long userId) {
        Resume resume = new Resume();
        resume.setUserId(userId);

        resume.setIsDefault(1);
        Example<Resume> resumeExample = Example.of(resume);

        return resumeDao.findOne(resumeExample).get().getIsOpenResume();
    }
}
