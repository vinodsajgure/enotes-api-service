package com.demo.enotes_api.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.demo.enotes_api.dto.NotesDto;
import com.demo.enotes_api.dto.NotesResponse;
import com.demo.enotes_api.entity.FileDetails;

public interface NotesService{

	public Boolean saveNotes(String notes, MultipartFile file) throws Exception;
	
	public List<NotesDto> getAllNotes();

	public byte[] downloadFile(FileDetails fileDetails) throws Exception;

	public FileDetails getFileDetails(Integer id);

	public NotesResponse getAllNotesByUser(Integer pageNo, Integer pageSize);

	public void softDeleteNotes(Integer id);

	public void restoreDeletedNotes(Integer id);

	public List<NotesDto> getUserRecycleBinNotes(Integer userId);

	public void hardDeleteNotes(Integer id);

	public void emptyUserRecycleBin();

	public Boolean copyNotes(Integer notesId);
	
	public NotesResponse getNotesByUserSearch(String keyword,Integer pageNo, Integer pageSize);
	
	
	
}
