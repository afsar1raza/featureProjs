//OExamResult.java (Entity class )
package com.nt.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="BATCH_EXAM_RESULT")
@Data
public class OExamResult {
	@Id
	private Integer id;
	private LocalDateTime dob;
	private Float percentage;
	private Integer semester;

}
