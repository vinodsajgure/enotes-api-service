package com.demo.enotes_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.demo.enotes_api.dto.FavouriteNotesDto;
import com.demo.enotes_api.dto.NotesDto;
import com.demo.enotes_api.dto.NotesResponse;
import com.demo.enotes_api.endpoint.NotesControllerEndPoint;
import com.demo.enotes_api.entity.FileDetails;
import com.demo.enotes_api.entity.User;
import com.demo.enotes_api.service.FavouriteNotesService;
import com.demo.enotes_api.service.NotesService;
import com.demo.enotes_api.util.CommonUtil;

@RestController
public class NotesController implements NotesControllerEndPoint {
	
	@Autowired
	private FavouriteNotesService favouriteNotesService;

	@Autowired
	private NotesService notesService;


	@Override
	public ResponseEntity<?> saveNotes(String notes, MultipartFile file)
			throws Exception {

		Boolean saveNotes = notesService.saveNotes(notes, file);

		if (saveNotes) {
//			return new ResponseEntity<>("Notes saved successfully.",HttpStatus.CREATED);
			return CommonUtil.createBuildResponseMessage(HttpStatus.CREATED, "Notes saved successfully.");
		}
//			return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
		return CommonUtil.createBuildErrorResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Somthing went wrong");

	}


	@Override
	public ResponseEntity<?> downloadNotesFile(Integer id) throws Exception {

		FileDetails fileDetails = notesService.getFileDetails(id);
		byte[] data = notesService.downloadFile(fileDetails);

		HttpHeaders headers = new HttpHeaders();
		String contentType = CommonUtil.getContentType(fileDetails.getOriginalFileName());
		headers.setContentType(MediaType.parseMediaType(contentType));
		headers.setContentDispositionFormData("attachment", fileDetails.getOriginalFileName());

		return ResponseEntity.ok().headers(headers).body(data);
	}


	@Override
	public ResponseEntity<?> getAllNotes() {
		List<NotesDto> allNotes = notesService.getAllNotes();
		if (!CollectionUtils.isEmpty(allNotes)) {
			return CommonUtil.createBuildResponse(HttpStatus.OK, allNotes);
		} else {
			return ResponseEntity.noContent().build();
		}
	}


	@Override
	public ResponseEntity<?> getAllNotesByUser(Integer pageNo,Integer pageSize) {
		NotesResponse allNotes = notesService.getAllNotesByUser(pageNo, pageSize);
		if (!ObjectUtils.isEmpty(allNotes)) {
			return CommonUtil.createBuildResponse(HttpStatus.OK, allNotes);
		} else {
			return ResponseEntity.noContent().build();
		}
	}


	@Override
	public ResponseEntity<?> softDeleteNotes(Integer id) {
		notesService.softDeleteNotes(id);
		return CommonUtil.createBuildResponseMessage(HttpStatus.OK, "Notes deleted successfully");
	}
	

	@Override
	public ResponseEntity<?> restoreNotes(Integer id) {
		notesService.restoreDeletedNotes(id);
		return CommonUtil.createBuildResponseMessage(HttpStatus.OK, "Notes restored successfully");
	}
	

	@Override
	public ResponseEntity<?> getUserRecycleBinNotes() {
		Integer userId = CommonUtil.getLoggedInUser().getId();
		List<NotesDto> notes = notesService.getUserRecycleBinNotes(userId);
		if(CollectionUtils.isEmpty(notes)) {
			return CommonUtil.createBuildResponseMessage(HttpStatus.NOT_FOUND, "Notes not available in Recycle Bin");
		}
		return CommonUtil.createBuildResponse(HttpStatus.OK, notes);
	}
	
	

	@Override
	public ResponseEntity<?> hardDeleteNotes(Integer id) {
		notesService.hardDeleteNotes(id);
		return CommonUtil.createBuildResponseMessage(HttpStatus.OK, "Notes deleted successfully");
	}
	

	@Override
	public ResponseEntity<?> emptyUserRecycleBin() {
		notesService.emptyUserRecycleBin();
		return CommonUtil.createBuildResponseMessage(HttpStatus.OK, "Notes deleted successfully");
	}
	
	

	@Override
	public ResponseEntity<?> favouriteNotes(Integer notesId) {
		favouriteNotesService.favouriteNotes(notesId);
		return CommonUtil.createBuildResponseMessage(HttpStatus.CREATED, "Notes added to Favourites successfully");
	}
	

	@Override
	public ResponseEntity<?> unFavouriteNotes(Integer favouriteNotesId) {
		favouriteNotesService.unFavouriteNotes(favouriteNotesId);
		return CommonUtil.createBuildResponseMessage(HttpStatus.OK , "Notes removed from Favourites successfully");
	}
	

	@Override
	public ResponseEntity<?> getUserfavouriteNotes() {
		List<FavouriteNotesDto> userFavouriteNotes = favouriteNotesService.getUserFavouriteNotes();
		if(CollectionUtils.isEmpty(userFavouriteNotes)) {
			return CommonUtil.createBuildResponseMessage(HttpStatus.NOT_FOUND, "Favourite Notes are not available");
		}
		return CommonUtil.createBuildResponse(HttpStatus.OK, userFavouriteNotes);
	}
	

	@Override
	public ResponseEntity<?> copyNotes(Integer notesId) {
		Boolean copyNotes = notesService.copyNotes(notesId);
		if(copyNotes) {
			return CommonUtil.createBuildResponseMessage(HttpStatus.CREATED, "Notes copied successfully");
		}
		return CommonUtil.createBuildErrorResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Somthing went wrong , copy failed.");
	}
	

	@Override
	public ResponseEntity<?> searchNotesByUser(String keyword,Integer pageNo,Integer pageSize) {
		NotesResponse allNotes = notesService.getNotesByUserSearch(keyword,pageNo, pageSize);
		if (!ObjectUtils.isEmpty(allNotes)) {
			return CommonUtil.createBuildResponse(HttpStatus.OK, allNotes);
		} else {
			return ResponseEntity.noContent().build();
		}
	}


}
