//ExamResultProcessor.java
package com.nt.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.nt.model.ExamResult;

@Component("erProcessor")
public class ExamResultProcessor implements ItemProcessor<ExamResult, ExamResult> {

	@Override
	public ExamResult process(ExamResult item) throws Exception {
		if(item.getPercentage()>90.0f)
			return item;
		else
			return null;
	}

}
