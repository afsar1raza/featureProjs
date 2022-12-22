//OExamResult.java (Document class )
package com.nt.document;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class OExamResult {
	private Integer id;
	private LocalDateTime dob;
	private Float percentage;
	private Integer semester;

}
