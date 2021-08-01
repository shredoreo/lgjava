package com.shred.sb_data_access.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class RoutingDataSource extends AbstractRoutingDataSource {
	@Override
	protected Object determineCurrentLookupKey() {
		return RoutingDataSourceContext.getDataSourceRoutingKey();
	}

}
