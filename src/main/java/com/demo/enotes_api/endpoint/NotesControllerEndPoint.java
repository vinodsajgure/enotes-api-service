package com.demo.enotes_api.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.demo.enotes_api.dto.NotesDto;
import com.demo.enotes_api.dto.NotesRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import static com.demo.enotes_api.util.Constants.DEFAULT_PAGENO;
import static com.demo.enotes_api.util.Constants.DEFAULT_PAGESIZE;;

@Tag(name="Notes", description="All Notes Operations APIs")
@RequestMapping("/api/v1/notes")
public interface NotesControllerEndPoint {

	@Operation(summary = "Save Notes Endpoint",tags = {"Notes","User"},description = "User Save Notes")
	@PostMapping(consumes="multipart/form-data")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<?> saveNotes(@RequestParam
			@Parameter(description = "Json String Notes",required=true,content = @Content(schema = @Schema(implementation = NotesRequest.class))) String notes, 
			@RequestParam(required = false) MultipartFile file)throws Exception;
	
	@Operation(summary = "Download Notes Endpoint",tags = {"Notes","User"},description = "User Can Download the Saved Notes")
	@GetMapping("/download/{id}")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<?> downloadNotesFile(@PathVariable Integer id) throws Exception;
	
	@Operation(summary = "Get All Notes Endpoint",tags = {"Notes","User"},description = "Admin,User Can Get All Notes")
	@GetMapping()
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<?> getAllNotes();
	
	@Operation(summary = "Get All Notes By User Endpoint",tags = {"Notes","User"},description = "User Can Get His All Notes")
	@GetMapping("/user-notes")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getAllNotesByUser(@RequestParam(name = "pageNo", defaultValue = DEFAULT_PAGENO) Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = DEFAULT_PAGESIZE) Integer pageSize);
	
	
	@Operation(summary = "Soft Delete Notes By Id Endpoint",tags = {"Notes","User"},description = "User Can Soft Delete His Notes")
	@GetMapping("/soft-delete/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> softDeleteNotes(@PathVariable Integer id);
	
	
	@Operation(summary = "Restore Notes By Id Endpoint",tags = {"Notes","User"},description = "User Can Restore The Deleted Notes")
	@GetMapping("/restore/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> restoreNotes(@PathVariable Integer id);
	
	@Operation(summary = "Get Recycle Bin Notes Endpoint",tags = {"Notes","User"},description = "User Can Get All Recycle Bin Notes")
	@GetMapping("/recycle-bin")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getUserRecycleBinNotes(); 
	
	@Operation(summary = "Hard Delete Notes Endpoint",tags = {"Notes","User"},description = "User Can Delete the Notes Permenently")
	@DeleteMapping("/hard-delete/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> hardDeleteNotes(@PathVariable Integer id);
	
	
	@Operation(summary = "Empty Recycle Bin Notes Endpoint",tags = {"Notes","User"},description = "User Can Remove All Recycle Bin Notes")
	@DeleteMapping("/empty-recyclebin")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> emptyUserRecycleBin();
	
	
	@Operation(summary = "Favourite Notes Endpoint",tags = {"Notes","User"},description = "User Can Make Favourite Notes")
	@GetMapping("/favNotes/{notesId}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> favouriteNotes(@PathVariable Integer notesId);
	
	@Operation(summary = "Unfavourite Notes Endpoint",tags = {"Notes","User"},description = "User Can Make Unfavoutite Notes")
	@DeleteMapping("/unFavNotes/{favouriteNotesId}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> unFavouriteNotes(@PathVariable Integer favouriteNotesId);
	
	
	@Operation(summary = "Get User Favourite Notes Endpoint",tags = {"Notes","User"},description = "User Can Get All Favourite Notes")
	@GetMapping("/favouriteNotes")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getUserfavouriteNotes();
	
	
	@Operation(summary = "Copy Notes By Id Endpoint",tags = {"Notes","User"},description = "User Can Copy Notes")
	@GetMapping("/copy/{notesId}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> copyNotes(@PathVariable Integer notesId);
	
	
	@Operation(summary = "Search Notes Endpoint",tags = {"Notes","User"},description = "User Can Search Notes By Name")
	@GetMapping("/search-notes")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> searchNotesByUser(@RequestParam(name="keyword",defaultValue = "")String keyword,
			@RequestParam(name = "pageNo", defaultValue = DEFAULT_PAGENO) Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = DEFAULT_PAGESIZE) Integer pageSize);
}
