package com.shred.sb_data_access.service;

import com.shred.sb_data_access.mapper.ProductMapper;
import com.shred.sb_data_access.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
	@Autowired
	private ProductMapper productMapper;

	public void findAllProductM() {
		List<Product> allProductM = productMapper.findAllProductM();
		System.out.println(allProductM);
	}

	public void findAllProductS() {
		List<Product> allProductS = productMapper.findAllProductS();
		System.out.println(allProductS);
	}

}
