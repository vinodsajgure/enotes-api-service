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
import com.demo.enotes_api.entity.FileDetails;
import com.demo.enotes_api.entity.User;
import com.demo.enotes_api.service.FavouriteNotesService;
import com.demo.enotes_api.service.NotesService;
import com.demo.enotes_api.util.CommonUtil;

@RestController
@RequestMapping("/api/v1/notes")
public class NotesController {
	
	@Autowired
	private FavouriteNotesService favouriteNotesService;

	@Autowired
	private NotesService notesService;

	@PostMapping()
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> saveNotes(@RequestParam String notes, @RequestParam(required = false) MultipartFile file)
			throws Exception {

		Boolean saveNotes = notesService.saveNotes(notes, file);

		if (saveNotes) {
//			return new ResponseEntity<>("Notes saved successfully.",HttpStatus.CREATED);
			return CommonUtil.createBuildResponseMessage(HttpStatus.CREATED, "Notes saved successfully.");
		}
//			return new ResponseEntity<>("Something went wrong",HttpStatus.INTERNAL_SERVER_ERROR);
		return CommonUtil.createBuildErrorResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Somthing went wrong");

	}

	@GetMapping("/download/{id}")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<?> downloadNotesFile(@PathVariable Integer id) throws Exception {

		FileDetails fileDetails = notesService.getFileDetails(id);
		byte[] data = notesService.downloadFile(fileDetails);

		HttpHeaders headers = new HttpHeaders();
		String contentType = CommonUtil.getContentType(fileDetails.getOriginalFileName());
		headers.setContentType(MediaType.parseMediaType(contentType));
		headers.setContentDispositionFormData("attachment", fileDetails.getOriginalFileName());

		return ResponseEntity.ok().headers(headers).body(data);
	}

	@GetMapping()
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<?> getAllNotes() {
		List<NotesDto> allNotes = notesService.getAllNotes();
		if (!CollectionUtils.isEmpty(allNotes)) {
			return CommonUtil.createBuildResponse(HttpStatus.OK, allNotes);
		} else {
			return ResponseEntity.noContent().build();
		}
	}

	@GetMapping("/user-notes")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getAllNotesByUser(@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize) {
		NotesResponse allNotes = notesService.getAllNotesByUser(pageNo, pageSize);
		if (!ObjectUtils.isEmpty(allNotes)) {
			return CommonUtil.createBuildResponse(HttpStatus.OK, allNotes);
		} else {
			return ResponseEntity.noContent().build();
		}
	}

	@GetMapping("/soft-delete/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> softDeleteNotes(@PathVariable Integer id) {
		notesService.softDeleteNotes(id);
		return CommonUtil.createBuildResponseMessage(HttpStatus.OK, "Notes deleted successfully");
	}
	
	@GetMapping("/restore/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> restoreNotes(@PathVariable Integer id) {
		notesService.restoreDeletedNotes(id);
		return CommonUtil.createBuildResponseMessage(HttpStatus.OK, "Notes restored successfully");
	}
	
	@GetMapping("/recycle-bin")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getUserRecycleBinNotes() {
		Integer userId = CommonUtil.getLoggedInUser().getId();
		List<NotesDto> notes = notesService.getUserRecycleBinNotes(userId);
		if(CollectionUtils.isEmpty(notes)) {
			return CommonUtil.createBuildResponseMessage(HttpStatus.NOT_FOUND, "Notes not available in Recycle Bin");
		}
		return CommonUtil.createBuildResponse(HttpStatus.OK, notes);
	}
	
	
	@DeleteMapping("/hard-delete/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> hardDeleteNotes(@PathVariable Integer id) {
		notesService.hardDeleteNotes(id);
		return CommonUtil.createBuildResponseMessage(HttpStatus.OK, "Notes deleted successfully");
	}
	
	@DeleteMapping("/empty-recyclebin")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> emptyUserRecycleBin() {
		notesService.emptyUserRecycleBin();
		return CommonUtil.createBuildResponseMessage(HttpStatus.OK, "Notes deleted successfully");
	}
	
	
	@GetMapping("/favNotes/{notesId}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> favouriteNotes(@PathVariable Integer notesId) {
		favouriteNotesService.favouriteNotes(notesId);
		return CommonUtil.createBuildResponseMessage(HttpStatus.CREATED, "Notes added to Favourites successfully");
	}
	
	@DeleteMapping("/unFavNotes/{favouriteNotesId}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> unFavouriteNotes(@PathVariable Integer favouriteNotesId) {
		favouriteNotesService.unFavouriteNotes(favouriteNotesId);
		return CommonUtil.createBuildResponseMessage(HttpStatus.OK , "Notes removed from Favourites successfully");
	}
	
	@GetMapping("/favouriteNotes")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getUserfavouriteNotes() {
		List<FavouriteNotesDto> userFavouriteNotes = favouriteNotesService.getUserFavouriteNotes();
		if(CollectionUtils.isEmpty(userFavouriteNotes)) {
			return CommonUtil.createBuildResponseMessage(HttpStatus.NOT_FOUND, "Favourite Notes are not available");
		}
		return CommonUtil.createBuildResponse(HttpStatus.OK, userFavouriteNotes);
	}
	
	@GetMapping("/copy/{notesId}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> copyNotes(@PathVariable Integer notesId) {
		Boolean copyNotes = notesService.copyNotes(notesId);
		if(copyNotes) {
			return CommonUtil.createBuildResponseMessage(HttpStatus.CREATED, "Notes copied successfully");
		}
		return CommonUtil.createBuildErrorResponseMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Somthing went wrong , copy failed.");
	}
	
	@GetMapping("/search-notes")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> searchNotesByUser(@RequestParam(name="keyword",defaultValue = "")String keyword,
			@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize) {
		NotesResponse allNotes = notesService.getNotesByUserSearch(keyword,pageNo, pageSize);
		if (!ObjectUtils.isEmpty(allNotes)) {
			return CommonUtil.createBuildResponse(HttpStatus.OK, allNotes);
		} else {
			return ResponseEntity.noContent().build();
		}
	}


}
