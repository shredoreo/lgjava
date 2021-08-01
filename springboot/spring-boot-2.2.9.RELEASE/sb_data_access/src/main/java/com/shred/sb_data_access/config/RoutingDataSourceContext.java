package com.shred.sb_data_access.config;

public class RoutingDataSourceContext {

	public static final String MASTER = "master";
	public static final String SLAVE = "slave";
	static ThreadLocal<String> threadLocal = new ThreadLocal<>();

	// 指定数据源的类型 master slave
	public RoutingDataSourceContext(String key) {
		threadLocal.set(key);
	}


	public static String getDataSourceRoutingKey(){
		String key = threadLocal.get();
		return key == null? MASTER : key;
	}


	public void close(){
		threadLocal.remove();
	}
}
