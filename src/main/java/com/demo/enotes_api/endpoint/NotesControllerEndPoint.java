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
import static com.demo.enotes_api.util.Constants.DEFAULT_PAGENO;
import static com.demo.enotes_api.util.Constants.DEFAULT_PAGESIZE;;

@RequestMapping("/api/v1/notes")
public interface NotesControllerEndPoint {

	@PostMapping()
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> saveNotes(@RequestParam String notes, @RequestParam(required = false) MultipartFile file)throws Exception;
	
	@GetMapping("/download/{id}")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<?> downloadNotesFile(@PathVariable Integer id) throws Exception;
	
	@GetMapping()
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<?> getAllNotes();
	
	@GetMapping("/user-notes")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getAllNotesByUser(@RequestParam(name = "pageNo", defaultValue = DEFAULT_PAGENO) Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = DEFAULT_PAGESIZE) Integer pageSize);
	
	@GetMapping("/soft-delete/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> softDeleteNotes(@PathVariable Integer id);
	
	@GetMapping("/restore/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> restoreNotes(@PathVariable Integer id);
	
	@GetMapping("/recycle-bin")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getUserRecycleBinNotes(); 
	
	@DeleteMapping("/hard-delete/{id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> hardDeleteNotes(@PathVariable Integer id);
	
	@DeleteMapping("/empty-recyclebin")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> emptyUserRecycleBin();
	
	@GetMapping("/favNotes/{notesId}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> favouriteNotes(@PathVariable Integer notesId);
	
	@DeleteMapping("/unFavNotes/{favouriteNotesId}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> unFavouriteNotes(@PathVariable Integer favouriteNotesId);
	
	@GetMapping("/favouriteNotes")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getUserfavouriteNotes();
	
	@GetMapping("/copy/{notesId}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> copyNotes(@PathVariable Integer notesId);
	
	@GetMapping("/search-notes")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> searchNotesByUser(@RequestParam(name="keyword",defaultValue = "")String keyword,
			@RequestParam(name = "pageNo", defaultValue = DEFAULT_PAGENO) Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = DEFAULT_PAGESIZE) Integer pageSize);
}
