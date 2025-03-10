package com.demo.enotes_api.service;

import java.util.List;

import com.demo.enotes_api.dto.FavouriteNotesDto;

public interface FavouriteNotesService {

	public void favouriteNotes(Integer notesId);
	
	public void unFavouriteNotes(Integer notesId);
	
	public List<FavouriteNotesDto> getUserFavouriteNotes();
}
