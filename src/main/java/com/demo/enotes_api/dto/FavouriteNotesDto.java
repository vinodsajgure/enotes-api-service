package com.demo.enotes_api.dto;

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
public class FavouriteNotesDto {

	private Integer id;
	private NotesDto note;
	private Integer userId;

}
