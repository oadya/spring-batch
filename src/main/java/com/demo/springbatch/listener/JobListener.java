package com.demo.springbatch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class JobListener implements JobExecutionListener {


	private static final Logger LOGGER = LoggerFactory
			.getLogger(JobListener.class);
	
	@Override
	public void beforeJob(JobExecution jobExecution) {
		LOGGER.info("BEGIN JOB : {} ", jobExecution.getJobInstance().getJobName());

	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		LOGGER.info("END  JOB execution with status : {} ", jobExecution.getExitStatus());
	}


	

}
