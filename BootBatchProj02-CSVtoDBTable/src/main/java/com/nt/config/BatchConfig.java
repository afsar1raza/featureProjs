//BatchConfig.java
package com.nt.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;

import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.nt.listener.JobMonitoringListener;
import com.nt.model.Employee;
import com.nt.processor.EmployeeInfoItemProcessor;

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
	private EmployeeInfoItemProcessor processor;
	@Autowired
	private DataSource ds;

	
	@Bean(name="ffiReader") //using ReadyMade Builder class
	public FlatFileItemReader<Employee> createFFIReader(){
		return new FlatFileItemReaderBuilder<Employee>()
				             .name("file-Reader")
				             .resource(new ClassPathResource("EmployeeInfo.csv"))
				             .delimited().delimiter(",")
				             .names("empno","ename","eadd","salary")
				             .targetType(Employee.class)
				             .build();
	}  
	
	/*//by using anonymous sub class and instance block
	@Bean("ffiReader")
	public FlatFileItemReader<Employee> createFFIReader(){
		//create reader object
		FlatFileItemReader<Employee> reader=new FlatFileItemReader<Employee>();
		//set csv file as resource
		reader.setResource(new ClassPathResource("EmployeeInfo.csv"));
		//set LineMapper object
		reader.setLineMapper(new DefaultLineMapper<Employee>() {{
			//set DelimitedTokenizer object
			setLineTokenizer(new DelimitedLineTokenizer() {{
				setDelimiter(",");
				setNames("empno","ename","eadd","salary");
			}});
			
			//set fieldSetMapper object
			setFieldSetMapper(new BeanWrapperFieldSetMapper<Employee>() {{
				setTargetType(Employee.class);
			}});
			
		}});
		return reader;		
	}		*/
				
				
	/*@Bean("ffiReader")
	public FlatFileItemReader<Employee> createFFIReader(){
		//create reader object
		FlatFileItemReader<Employee> reader=new FlatFileItemReader<Employee>();
		//set csv file as resource
		reader.setResource(new ClassPathResource("EmployeeInfo.csv"));
		//create LineMapper object (To get each line from csv file)
		DefaultLineMapper<Employee> lineMapper=new DefaultLineMapper<Employee>();
		//create LineTokenizer (To get tokens from lines)
		DelimitedLineTokenizer tokenizer=new DelimitedLineTokenizer();
		tokenizer.setDelimiter(",");
		tokenizer.setNames("empno","ename","eadd","salary");
		//create FieldSetMapper (To set the tokens to Model class Object properties)
		BeanWrapperFieldSetMapper<Employee> fieldSetMapper=new BeanWrapperFieldSetMapper<Employee>();
		fieldSetMapper.setTargetType(Employee.class);
		
		//set Tokenizer, FieldSetMapper object to LineMapper
		lineMapper.setFieldSetMapper(fieldSetMapper);
		lineMapper.setLineTokenizer(tokenizer);
		
		//set LineMapper to reader object
		reader.setLineMapper(lineMapper);
	
		return reader;
	}*/
	
	@Bean(name="writer")//using Builder class
	public JdbcBatchItemWriter<Employee> createJBIWriter(){
	return new JdbcBatchItemWriterBuilder<Employee>()
			            .dataSource(ds)
			            .sql("INSERT INTO BATCH_EMPLOYEEINFO VALUES(:empno,:ename,:eadd,:salary,:grossSalary,:netSalary)")
			            .beanMapped()  //Creates a BeanPropertyItemSqlParameterSourceProvider to be used as your ItemSqlParameterSourceProvider
			            .build();
	}
	
	
	/*//by using anonymous sub class and instance block
	@Bean("jbiw")
	public JdbcBatchItemWriter<Employee> createJBIWriter(){
		//create JdbcBatchItemWriter
		JdbcBatchItemWriter<Employee> writer=new JdbcBatchItemWriter<Employee>() {{
			//set DataSource
		      setDataSource(ds);
		      //set INSERT SQL query with named params
		      setSql("INSERT INTO BATCH_EMPLOYEEINFO VALUES(:empno,:ename,:eadd,:salary,:grossSalary,:netSalary)");
		      //set sourceProvider
		      setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Employee>());
		}};
	
		return writer;
	}*/
	
	/*@Bean("jbiw")
	public JdbcBatchItemWriter<Employee> createJBIWriter(){
		//create JdbcBatchItemWriter
		JdbcBatchItemWriter<Employee> writer=new JdbcBatchItemWriter<Employee>();
		//set Datasource
		writer.setDataSource(ds);
		//set INSERT SQL query with named params
		writer.setSql("INSERT INTO BATCH_EMPLOYEEINFO VALUES(:empno,:ename,:eadd,:salary,:grossSalary,:netSalary)");
		//create BeanPropertyItemSqlParameterSourceProvider object
		BeanPropertyItemSqlParameterSourceProvider<Employee> sourceProvider=new BeanPropertyItemSqlParameterSourceProvider<Employee>();
		//set sourceProvider to writer object
		writer.setItemSqlParameterSourceProvider(sourceProvider);
		
		return writer;
	}*/
	
	@Bean(name="step1")
	public Step createStep1() {
		return stepFactory.get("step1")
				      .<Employee,Employee>chunk(5)
				      .reader(createFFIReader())
				      .processor(processor)
				      .writer(createJBIWriter())
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
