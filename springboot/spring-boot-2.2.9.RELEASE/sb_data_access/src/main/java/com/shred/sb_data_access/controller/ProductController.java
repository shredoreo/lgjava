package com.shred.sb_data_access.controller;

import com.shred.sb_data_access.config.RoutingDataSourceContext;
import com.shred.sb_data_access.config.RoutingWith;
import com.shred.sb_data_access.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.shred.sb_data_access.config.RoutingDataSourceContext.MASTER;
import static com.shred.sb_data_access.config.RoutingDataSourceContext.SLAVE;

@RestController
public class ProductController {

	@Autowired
	private ProductService productService;

	@RoutingWith(MASTER)
	@RequestMapping("/findAllProductM")
	public String findAllProductM() {
//		RoutingDataSourceContext dsContext = new RoutingDataSourceContext(MASTER);
		productService.findAllProductM();
		return "master";
	}

	@RoutingWith(SLAVE)
	@RequestMapping("/findAllProductS")
	public String findAllProductS() {
//		RoutingDataSourceContext dsContext = new RoutingDataSourceContext(RoutingDataSourceContext.SLAVE);

		productService.findAllProductS();
		return "slave";
	}

}
