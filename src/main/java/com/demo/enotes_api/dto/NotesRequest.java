package com.demo.enotes_api.dto;

import com.demo.enotes_api.dto.NotesDto.CategoryDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotesRequest {

	private Integer id;
	private String title;
	private String description;
	private CategoryDto category;
}
