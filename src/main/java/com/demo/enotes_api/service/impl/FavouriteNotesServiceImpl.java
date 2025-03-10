package com.demo.enotes_api.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.demo.enotes_api.dto.FavouriteNotesDto;
import com.demo.enotes_api.entity.FavouriteNotes;
import com.demo.enotes_api.entity.Notes;
import com.demo.enotes_api.exception.ResourceNotFoundException;
import com.demo.enotes_api.repository.FavouriteNotesRepository;
import com.demo.enotes_api.repository.NotesRepository;
import com.demo.enotes_api.service.FavouriteNotesService;
import com.demo.enotes_api.util.CommonUtil;

@Service
public class FavouriteNotesServiceImpl implements FavouriteNotesService {

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private NotesRepository notesRepository;

	@Autowired
	private FavouriteNotesRepository favouriteNotesRepository;

	@Override
	public void favouriteNotes(Integer notesId) {
		Integer userId = CommonUtil.getLoggedInUser().getId();
		Notes notes = notesRepository.findById(notesId)
				.orElseThrow(() -> new ResourceNotFoundException("Invalid notes id"));

		FavouriteNotes favouriteNotes = FavouriteNotes.builder().note(notes).userId(userId).build();

		favouriteNotesRepository.save(favouriteNotes);
	}

	@Override
	public void unFavouriteNotes(Integer FavouriteNotesId) {
		FavouriteNotes FavouriteNotes = favouriteNotesRepository.findById(FavouriteNotesId)
				.orElseThrow(() -> new ResourceNotFoundException("Favourite Notes id not found"));

		favouriteNotesRepository.delete(FavouriteNotes);

	}

	@Override
	public List<FavouriteNotesDto> getUserFavouriteNotes() {
		Integer userId = CommonUtil.getLoggedInUser().getId();
		List<FavouriteNotes> favouriteNotesList = favouriteNotesRepository.findByUserId(userId);

		return favouriteNotesList.stream().map(notes -> mapper.map(notes, FavouriteNotesDto.class)).toList();
	}

}
