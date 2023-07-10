package com.demo.springbatch.steps;

import org.springframework.batch.item.ItemReader;

import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import com.demo.springbatch.entity.Employee;


public class EmployeeReader implements ItemReader<Employee> {

	private ItemReader<Employee> delegate;
	
	@Override
	public Employee read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		if (delegate == null) {
			delegate = readerFlatFileItemReader();
		}
		return delegate.read();
	}


	private FlatFileItemReader<Employee> readerFlatFileItemReader() {
	        FlatFileItemReader<Employee> itemReader = new FlatFileItemReader<>();
	        itemReader.setResource(new FileSystemResource("src/main/resources/employee.csv")); // setting resource for reading
	        itemReader.setName("employeeReader"); //setting reader component name
	        itemReader.setLinesToSkip(1); // how many lines to skip from top
	        itemReader.setLineMapper(lineMapper());

	        return itemReader;
	    }

	    private LineMapper<Employee> lineMapper() {
	        DefaultLineMapper<Employee> lineMapper = new DefaultLineMapper<>();

	        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
	        lineTokenizer.setDelimiter(","); // setting delimiter used in csv
	        lineTokenizer.setStrict(false); // setting this false means - if few entries were missing for some rows, then it can read that as well
	        lineTokenizer.setNames("id", "name", "username", "gender", "salary");

	        BeanWrapperFieldSetMapper<Employee> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
	        fieldSetMapper.setTargetType(Employee.class); // mapping with the entity - remember that Entity variables are same as CSV header names

	        lineMapper.setLineTokenizer(lineTokenizer);
	        lineMapper.setFieldSetMapper(fieldSetMapper);

	        return lineMapper;

	    }


}
