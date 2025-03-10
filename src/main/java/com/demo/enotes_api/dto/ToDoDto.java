package com.demo.enotes_api.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ToDoDto {

	private Integer id;
	private String title;
	private StatusDto status;
	private Integer createdBy;
	private Date createdDate;
	private Integer updatedBy;
	private Date updatedOn;
	
	@AllArgsConstructor
	@NoArgsConstructor
	@Getter
	@Setter
	@Builder
	public static class StatusDto{
		private Integer id;
		private String name;
	}
}
