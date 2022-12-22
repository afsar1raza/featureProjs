//BatchTestRunner.java
package com.nt.runner;

import java.util.Random;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
public class BatchTestService {
	@Autowired
	private JobLauncher launcher;
	
	@Autowired
	private Job job;

	//@Scheduled(cron="0 11/2 * * * *")
	//@Scheduled(cron="${cron.exp}")
	public void run() throws Exception {
		System.out.println("BatchTestRunner.run()");
		// create JobParameters
		JobParameters params=new JobParametersBuilder().addLong("run.id",new Random().nextLong(1000)).toJobParameters();

		//run the job
		JobExecution execution= launcher.run(job,params);
		System.out.println("launch method is called");
		System.out.println("Job Execution status: "+execution.getStatus());
	}

}
