package com.demo.springbatch;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBatchApplication implements CommandLineRunner {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SpringBatchApplication.class);
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	@Qualifier("employeeJob")
	private Job job;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBatchApplication.class, args);
	}

	
	@Override
	public void run(String... args) throws Exception {
		
		JobExecution execution = null;

		try {

			JobParameters jobParameters = new JobParametersBuilder()
					.addString("JobId", String.valueOf(System.currentTimeMillis()))
					.addDate("date", new Date())
					.addLong("time", System.currentTimeMillis()).toJobParameters();

			execution = jobLauncher.run(job, jobParameters);

		} catch (Exception e) {
			LOGGER.error("END  Batch emplyee with error {} ", e);
		}

		LOGGER.info(" END  -- Batch emplyee ");
	}

}

