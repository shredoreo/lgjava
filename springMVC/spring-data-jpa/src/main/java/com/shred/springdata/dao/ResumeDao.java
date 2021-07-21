package com.shred.springdata.dao;

import com.shred.springdata.pojo.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
/**
 * 一个符合SpringDataJpa要求的Dao层接口是需要继承JpaRepository和
 * JpaSpecificationExecutor
 * JpaRepository <操作的实体类类型,主键类型> 封装了基本的CRUD操作
 *
 * JpaSpecificationExecutor<操作的实体类类型> 封装了复杂的查询(分⻚、排序等)
 */
public interface ResumeDao extends JpaRepository<Resume,Long>,
        JpaSpecificationExecutor<Resume>  {


}
