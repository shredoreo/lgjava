package com.shred.sb_data_access.mapper;

import com.shred.sb_data_access.pojo.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductMapper {

	@Select("select * from product")
	public List<Product> findAllProductM();

	@Select("select * from product")
	public List<Product> findAllProductS();
}
