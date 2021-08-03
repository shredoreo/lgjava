package com.shred.cache.controller;

import com.shred.cache.pojo.Employee;
import com.shred.cache.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
/*
http://localhost:8080/emp/1
http://localhost:8080/updateEmp/1
http://localhost:8080/delEmp/1
http://localhost:8080/addEmp
 */
@RestController
public class EmployeeController {
	@Autowired
	EmployeeService employeeService;

	@GetMapping("/emp/{id}")
	public Employee getEmp(@PathVariable("id") Integer id) {
		return employeeService.getEmpById(id);
	}

	@GetMapping("/updateEmp/{id}")
	public Employee updateEmp(@PathVariable("id") Integer id) {
		Employee empById = employeeService.getEmpById(id);
		empById.setLastName("update");
		return employeeService.updateEmp(empById);
	}


	@GetMapping("/delEmp/{id}")
	public Employee delEmp(@PathVariable("id") Integer id) {
		return employeeService.delEmp(id);
	}

	@GetMapping("/addEmp")
	public Employee addEmp() {
		Employee employee = new Employee();
		employee.setId(1);
		employee.setGender(1);
		employee.setLastName("auto");
		employee.setDId(1);
		employee.setEmail("autoInsert@qq.com");
		return employeeService.insertEmp(employee);
	}

}