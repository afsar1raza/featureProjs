//Item Processor
package com.nt.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.nt.model.Employee;

@Component("processor")
public class EmployeeInfoItemProcessor implements ItemProcessor<Employee, Employee> {

	@Override
	public Employee process(Employee item) throws Exception {
		if(item.getSalary()>=50000) {
			item.setGrossSalary(item.getSalary()+(item.getSalary()*0.4f));
			item.setNetSalary(item.getGrossSalary()-(item.getGrossSalary()*0.2f));
			return item;
		}
			return null;
	}

}
