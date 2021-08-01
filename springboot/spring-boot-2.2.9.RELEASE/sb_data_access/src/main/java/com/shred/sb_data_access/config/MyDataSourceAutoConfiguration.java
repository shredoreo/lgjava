package com.shred.sb_data_access.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;

import static com.shred.sb_data_access.config.RoutingDataSourceContext.MASTER;
import static com.shred.sb_data_access.config.RoutingDataSourceContext.SLAVE;

@Configuration
public class MyDataSourceAutoConfiguration {

	@Bean
	@ConfigurationProperties("spring.druid.datasource.master")
	public DataSource masterDataSource(){
		System.out.println("创建 master ds");
		return DataSourceBuilder.create().build();
	}

	@Bean
	@ConfigurationProperties("spring.druid.datasource.slave")
	public DataSource slaveDataSource(){
		System.out.println("创建 slave ds");
		return DataSourceBuilder.create().build();
	}

	@Bean
	@Primary//设置为主数据源
	public DataSource primaryDataSource(@Autowired@Qualifier("masterDataSource") DataSource masterDs,
										@Autowired@Qualifier("slaveDataSource")DataSource slaveDs){
		RoutingDataSource routingDataSource = new RoutingDataSource();
		var map = new HashMap<>();
		map.put(MASTER, masterDs);
		map.put(SLAVE, slaveDs);
		routingDataSource.setTargetDataSources(map);
		return routingDataSource;
	}
}
