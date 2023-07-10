package com.demo.springbatch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.demo.springbatch.entity.Employee;
import com.demo.springbatch.listener.StepListnerImpl;
import com.demo.springbatch.partition.CustomPartitioner;
import com.demo.springbatch.steps.EmployeeProcessor;
import com.demo.springbatch.steps.EmployeeWriter;

//@Configuration
//@EnableBatchProcessing
public class JobPartitionConfiguration {

	@Autowired
    private JobBuilderFactory jobBuilderFactory;

	@Autowired
    private StepBuilderFactory stepBuilderFactory;
 
	@Autowired
	private StepListnerImpl stepListner;
    
	@Bean
	public Job employeeJobPartition() {
		return jobBuilderFactory.get("employeeJobPartition")
				.start(masterStep())
				.build();
	}
	
	@Bean
	public Step employeeStep() {
		return stepBuilderFactory.get("employeeStep")
				.<Employee, Employee>chunk(250)
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.listener(stepListner)
				.build();
	}
    
	@Bean
    public Step masterStep() {
        return stepBuilderFactory.get("masterStep")
        		.partitioner(employeeStep().getName(), partitioner())
                .partitionHandler(partitionHandler())
                .build();
    }

	    
	@Bean
    public PartitionHandler partitionHandler() {
        TaskExecutorPartitionHandler taskExecutorPartitionHandler = new TaskExecutorPartitionHandler();
        taskExecutorPartitionHandler.setGridSize(4);
        taskExecutorPartitionHandler.setTaskExecutor(taskExecutor());
        taskExecutorPartitionHandler.setStep(employeeStep());
        return taskExecutorPartitionHandler;
    }


    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(4);
        taskExecutor.setCorePoolSize(4);
        taskExecutor.setQueueCapacity(4);
        return taskExecutor;
    }
    

	
    @Bean
    public FlatFileItemReader<Employee> reader() {
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

	
	/*@Bean
	@StepScope
	public EmployeeReader reader() {
		return new EmployeeReader();
	}*/
    
	@Bean
	@StepScope
	public EmployeeProcessor processor() {
		return new EmployeeProcessor();
	}

	@Bean
	@StepScope
	public EmployeeWriter writer() {
		return new EmployeeWriter();
	}

	@Bean
	public CustomPartitioner partitioner() {
		return new CustomPartitioner();
	}
        
	
}
