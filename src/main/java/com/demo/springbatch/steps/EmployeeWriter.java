package com.demo.springbatch.steps;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo.springbatch.entity.Employee;
import com.demo.springbatch.repository.EmployeeRepository;

@Component
public class EmployeeWriter implements ItemWriter<Employee> {

    @Autowired
    private EmployeeRepository employeeRepository;

	@Override
	public void write(List<? extends Employee> items) throws Exception {
		System.out.println("Thread Name: " + Thread.currentThread().getName());
		employeeRepository.saveAll(items);
		
	}
}
