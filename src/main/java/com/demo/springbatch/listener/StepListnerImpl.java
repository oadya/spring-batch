package com.demo.springbatch.listener;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class StepListnerImpl implements StepExecutionListener, SkipListener<Object, Object>  {
	
	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(StepListnerImpl.class);
	
	
	@Override
	public void beforeStep(StepExecution stepExecution) {
		
		LOGGER.info("BEGIN step : {} with job parametres {} ", stepExecution.getStepName(),stepExecution.getJobParameters());
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		LOGGER.info("END step : {} with status : {} and  job parametres {} ", stepExecution.getStepName(), stepExecution.getExitStatus().getExitCode(),stepExecution.getJobParameters());
		return stepExecution.getExitStatus();
	}

	@Override
	public void onSkipInRead(Throwable t) {
		LOGGER.error("Exception suivante skipée : ", t);
	}

	@Override
	public void onSkipInWrite(Object item, Throwable t) {
		LOGGER.error("Exception suivante skipée : ", t);
	}

	@Override
	public void onSkipInProcess(Object item, Throwable t) {
		LOGGER.error("Exception suivante skipée : ", t);
	}
}
