package com.shred.cache.service;

import com.shred.cache.mappers.EmployeeMapper;
import com.shred.cache.pojo.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "emp")
public class EmployeeService {
	@Autowired
	EmployeeMapper employeeMapper;

	@CachePut(key = "#emp.id")
	public Employee insertEmp(Employee emp) {
		employeeMapper.insertEmp(emp);
		return emp;
	}

	@Cacheable(key = "#id")
	public Employee getEmpById(Integer id) {
		Employee emp = employeeMapper.getEmpById(id);
		return emp;
	}

	@CachePut(key = "#employee.id")
	public Employee updateEmp(Employee employee) {
		employeeMapper.updateEmp(employee);
		return employee;
	}

	@CacheEvict(key = "#id", beforeInvocation = true)
	public Employee delEmp(Integer id) {
		employeeMapper.deleteEmpById(id);
		return null;
	}
}