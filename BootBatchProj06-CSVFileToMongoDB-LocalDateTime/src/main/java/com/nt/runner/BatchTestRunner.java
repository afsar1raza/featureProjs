//BatchTestRunner.java
package com.nt.runner;

import java.util.Random;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BatchTestRunner implements CommandLineRunner {
	@Autowired
	private JobLauncher launcher;
	
	@Autowired
	private Job job;

	@Override
	public void run(String... args) throws Exception {
		System.out.println("BatchTestRunner.run()");
		// create JobParameters
		JobParameters params=new JobParametersBuilder().addLong("run.id",new Random().nextLong(1000)).toJobParameters();

		//run the job
		//JobExecution execution= launcher.run(job,params);
		//System.out.println("launch method is called");
		//System.out.println("Job Execution status: "+execution.getStatus());
	}

}
