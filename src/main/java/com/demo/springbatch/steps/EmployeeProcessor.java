package com.demo.springbatch.steps;


import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.demo.springbatch.entity.Employee;

@Component
public class EmployeeProcessor implements ItemProcessor<Employee, Employee> {
    @Override
    public Employee process(Employee employee) throws Exception {
        
    	/*long salary = employee.getSalary();

        if(salary >= 50000) {
            return employee;
        }*/

        return employee;
    }


}
