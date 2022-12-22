//BatchConfig.java
package com.nt.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import com.nt.listener.JobMonitoringListener;
import com.nt.model.ExamResult;
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
	private DataSource ds;
	@Autowired
	private ExamResultProcessor processor;

	/*@Bean(name="jcir")
	public JdbcCursorItemReader<ExamResult> createReader(){
		//create reader object
		JdbcCursorItemReader<ExamResult> reader=new JdbcCursorItemReader<ExamResult>();
		//specify Datasource
		reader.setDataSource(ds);
		reader.setSql("SELECT ID, DOB, SEMESTER, PERCENTAGE FROM EXAM_RESULT");
		//specify RowMapper
		reader.setRowMapper((rs,rowNumber)->{
			ExamResult result=new ExamResult();
			result.setId(rs.getInt(1));
			result.setDob(rs.getDate(2));
			result.setSemester(rs.getInt(3));
			result.setPercentage(rs.getFloat(4));
			return result;
		});
		return reader;
	}*/

	/*@Bean(name="jcir") //using anonymous sub class and instance block
	public JdbcCursorItemReader<ExamResult> createReader(){
		//create reader object
		JdbcCursorItemReader<ExamResult> reader=new JdbcCursorItemReader<ExamResult>() {{;
		//specify Datasource
		setDataSource(ds);
		setSql("SELECT ID, DOB, SEMESTER, PERCENTAGE FROM EXAM_RESULT");
		//specify RowMapper
		setRowMapper((rs,rowNumber)->{
			ExamResult result=new ExamResult();
			result.setId(rs.getInt(1));
			result.setDob(rs.getDate(2));
			result.setSemester(rs.getInt(3));
			result.setPercentage(rs.getFloat(4));
			return result;
		});
		}};
		return reader;
	}*/

	@Bean(name="jcir") //using builder
	public JdbcCursorItemReader<ExamResult> createReader(){
		return new JdbcCursorItemReaderBuilder<ExamResult>()
				.name("reader")
				.dataSource(ds)
				.sql("SELECT ID, DOB, SEMESTER, PERCENTAGE FROM EXAM_RESULT")
				.beanRowMapper(ExamResult.class)
				.build();
	}

	/*@Bean(name="ffiw")
	public FlatFileItemWriter<ExamResult> createWriter(){
		//create writer object
		FlatFileItemWriter<ExamResult> writer=new FlatFileItemWriter<ExamResult>();
		//specify the destination resource
		writer.setResource(new ClassPathResource("e:/TopStudents.csv"));
		//create FieldExtractor (To get fields from Model class object)
		BeanWrapperFieldExtractor<ExamResult> extractor=new BeanWrapperFieldExtractor<ExamResult>();
		//set names to the extracted field value
		extractor.setNames(new String[] {"id","dob","semester","percentage"});
		//create LineAggregator (combines everything into csv file lines)
		DelimitedLineAggregator<ExamResult> aggregator=new DelimitedLineAggregator<ExamResult>();
		aggregator.setDelimiter(",");
		aggregator.setFieldExtractor(extractor);

		return writer;
	}*/

	/*@Bean(name="ffiw")
	public FlatFileItemWriter<ExamResult> createWriter(){
		//create writer object
		FlatFileItemWriter<ExamResult> writer=new FlatFileItemWriter<ExamResult>() {{
			//specify the destination resource
			setResource(new ClassPathResource("e:/TopStudents.csv"));
			//create LineAggregator (combines everything into csv file lines)
			DelimitedLineAggregator<ExamResult> aggregator=new DelimitedLineAggregator<ExamResult>() {{;
			setDelimiter(",");
			setFieldExtractor(new BeanWrapperFieldExtractor<ExamResult>() {{
				//set names of extracted fields
				setNames(new String[] {"id","dob","semester","percentage"});
			}});
			}};
		}};
	
	return writer;
	}*/
	
	@Bean(name="ffiw")
	public FlatFileItemWriter<ExamResult> createWriter(){
		return new FlatFileItemWriterBuilder<ExamResult>()
				            .name("writer")
				            .resource(new FileSystemResource("d:/TopStudents.csv"))
				            .lineSeparator("\r\n")
				            .delimited().delimiter(",")
				            .names(new String[] {"id","dob","semester","percentage"})
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
	public Job creatJob1() {
		return jobFactory.get("job1")
				      .listener(listener)
				      .incrementer(new RunIdIncrementer())
				      .start(createStep1())
				      .build();
	}
}
