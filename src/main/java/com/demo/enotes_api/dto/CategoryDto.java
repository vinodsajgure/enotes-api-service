package com.demo.enotes_api.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

	private Integer id;
	private String name;
	private String description;
	private Boolean isActive;
	private Integer createdBy;
	private Date createdDate;
	private Integer updatedBy;
	private Date updatedOn;
}
