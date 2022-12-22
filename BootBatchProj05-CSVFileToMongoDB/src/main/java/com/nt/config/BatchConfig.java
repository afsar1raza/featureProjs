//BatchConfig.java
package com.nt.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.nt.document.ExamResult;
import com.nt.listener.JobMonitoringListener;
import com.nt.processor.ExamResultProcessor;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
	@Autowired
	private JobBuilderFactory jobFactory;
	@Autowired
	private StepBuilderFactory stepFactory;
	@Autowired
	private JobMonitoringListener listener;
	@Autowired
	private ExamResultProcessor processor;
	@Autowired
	private MongoTemplate template;

	@Bean(name="ffir")
	public FlatFileItemReader<ExamResult> createReader(){
		return new FlatFileItemReaderBuilder<ExamResult>()
				            .name("file-reader")
				            .resource(new ClassPathResource("Students.csv"))
				            .delimited().delimiter(",")
				            .names("id","dob","percentage","semester")
				            .targetType(ExamResult.class)
				            .build();
				            
	}
	
	@Bean(name="miw")
	public MongoItemWriter<ExamResult> createWriter(){
		return new MongoItemWriterBuilder<ExamResult>()
				            .collection("SuperBrains")
				            .template(template)
				            .build();
	}
	
	@Bean(name="step1")
	public Step createStep1() {
		return stepFactory.get("step1")
				                        .<ExamResult,ExamResult>chunk(5)
				                        .reader(createReader())
				                        .processor(processor)
				                        .writer(createWriter())
				                        .build();
	}
	
	@Bean(name="job1")
	public Job createJob1() {
		return jobFactory.get("job1")
				.incrementer(new RunIdIncrementer())
				.listener(listener)
				.start(createStep1())
				.build();
	}
}
