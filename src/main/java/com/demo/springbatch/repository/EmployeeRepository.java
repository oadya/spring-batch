package com.demo.springbatch.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.springbatch.entity.Employee;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Integer> {
}