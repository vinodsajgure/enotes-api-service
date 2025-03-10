package com.demo.enotes_api.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.demo.enotes_api.dto.NotesDto;
import com.demo.enotes_api.dto.NotesDto.FileDto;
import com.demo.enotes_api.dto.NotesResponse;
import com.demo.enotes_api.entity.FileDetails;
import com.demo.enotes_api.entity.Notes;
import com.demo.enotes_api.exception.ResourceNotFoundException;
import com.demo.enotes_api.repository.CategoryRepository;
import com.demo.enotes_api.repository.FileDetailsRepository;
import com.demo.enotes_api.repository.NotesRepository;
import com.demo.enotes_api.service.NotesService;
import com.demo.enotes_api.util.CommonUtil;
import com.demo.enotes_api.util.Validations;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class NotesServiceImpl implements NotesService {

	@Autowired
	private NotesRepository notesRepository;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private Validations validations;

	@Value("${file.upload.path}")
	private String uploadPath;

	@Autowired
	private FileDetailsRepository fileDetailsRepository;

	@Override
	public Boolean saveNotes(@RequestParam String notes, @RequestParam MultipartFile file) throws Exception {
//		converting the json value coming from user in the form of string into a notes dto object using objectMapper.
		ObjectMapper ob = new ObjectMapper();
		NotesDto notesDto = ob.readValue(notes, NotesDto.class);
		notesDto.setIsDeleted(false);
		notesDto.setDeletedOn(null);

		// checking if id is present in request for update api
		if (!ObjectUtils.isEmpty(notesDto.getId())) {
			updateNotes(notesDto, file);
		}

		// Notes validation
		validations.notesValidations(notesDto);

		// category id exists or not validation.
		checkCategoryIdExists(notesDto.getCategory().getId());

		Notes notesMap = mapper.map(notesDto, Notes.class);

		FileDetails fileDtls = saveFileDetails(file);
		if (!ObjectUtils.isEmpty(fileDtls)) {
			notesMap.setFileDetails(fileDtls);
		} else {
			if (ObjectUtils.isEmpty(notesDto.getId())) {
				notesMap.setFileDetails(null);
			}
		}

		Notes savedNotes = notesRepository.save(notesMap);

		if (!ObjectUtils.isEmpty(savedNotes)) {
			return true;
		}
		return false;
	}

	private void updateNotes(NotesDto notesDto, MultipartFile file) {
		Notes existsNotes = notesRepository.findById(notesDto.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Invalid notes id"));

		if (ObjectUtils.isEmpty(file)) {
			notesDto.setFileDetails(mapper.map(existsNotes.getFileDetails(), FileDto.class));
		}
	}

	private FileDetails saveFileDetails(MultipartFile file) throws IOException {

		if (!ObjectUtils.isEmpty(file) && !file.isEmpty()) {
			String originalFilename = file.getOriginalFilename();
			String extension = FilenameUtils.getExtension(originalFilename);

			List<String> allowedExtensions = Arrays.asList("pdf", "xlsx", "png", "jpg");

			if (!allowedExtensions.contains(extension)) {
				throw new IllegalArgumentException("Inavalid file format.Upload only .pdf , .png, .jpg , .xlsx");
			}

			FileDetails fileDtls = new FileDetails();

			fileDtls.setOriginalFileName(originalFilename);
			fileDtls.setDisplayFileName(getDisplayName(originalFilename));

			String randomStr = UUID.randomUUID().toString();

			String uploadFileName = randomStr + "." + extension;

			fileDtls.setUploadFileName(uploadFileName);
			fileDtls.setFielSize(file.getSize());

			File saveFile = new File(uploadPath);
			if (!saveFile.exists()) {
				saveFile.mkdir();
			}

			String storePath = uploadPath.concat(uploadFileName);
			fileDtls.setPath(storePath);

//			upload file and save file details

			long uploadedFile = Files.copy(file.getInputStream(), Paths.get(storePath));
			if (uploadedFile != 0) {
				FileDetails savedFileDetails = fileDetailsRepository.save(fileDtls);
				return savedFileDetails;
			}

		}
		return null;
	}

	private String getDisplayName(String originalFilename) {
		String extension = FilenameUtils.getExtension(originalFilename);
		String fileName = FilenameUtils.removeExtension(originalFilename);

		if (fileName.length() > 8) {
			fileName = fileName.substring(0, 7);
		}

		fileName = fileName + "." + extension;
		return fileName;
	}

	private void checkCategoryIdExists(Integer id) throws ResourceNotFoundException {
		categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category id does not exists."));
	}

	@Override
	public List<NotesDto> getAllNotes() {
		List<NotesDto> notesList = notesRepository.findAllByIsDeletedFalse().stream()
				.map(note -> mapper.map(note, NotesDto.class)).toList();
		return notesList;
	}

	@Override
	public byte[] downloadFile(FileDetails fileDetails) throws Exception {

		InputStream io = new FileInputStream(fileDetails.getPath());

		byte[] byteArrayData = StreamUtils.copyToByteArray(io);
		return byteArrayData;
	}

	@Override
	public FileDetails getFileDetails(Integer id) {
		FileDetails fileDetails = fileDetailsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("File is not available."));

		return fileDetails;
	}

	@Override
	public NotesResponse getAllNotesByUser(Integer pageNo, Integer pageSize) {
		Integer userId = CommonUtil.getLoggedInUser().getId();
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<Notes> pageNotes = notesRepository.findByCreatedByAndIsDeletedFalse(userId, pageable);

		List<NotesDto> notesDto = pageNotes.get().map(n -> mapper.map(n, NotesDto.class)).toList();

		NotesResponse notes = new NotesResponse().builder().notes(notesDto).pageNo(pageNotes.getNumber())
				.pageSize(pageNotes.getSize()).totalElements(pageNotes.getTotalElements())
				.totalPages(pageNotes.getTotalPages()).isFirst(pageNotes.isFirst()).isLast(pageNotes.isLast()).build();

		return notes;
	}

	@Override
	public void softDeleteNotes(Integer id) {
		Notes notes = notesRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Invalid notes id"));
		notes.setIsDeleted(true);
		notes.setDeletedOn(LocalDateTime.now());
		notesRepository.save(notes);
	}

	@Override
	public void restoreDeletedNotes(Integer id) {
		Notes notes = notesRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Invalid notes id"));
		notes.setIsDeleted(false);
		notes.setDeletedOn(null);
		notesRepository.save(notes);

	}

	@Override
	public List<NotesDto> getUserRecycleBinNotes(Integer userId) {
		List<Notes> recycleBinNotesList = notesRepository.findByCreatedByAndIsDeletedTrue(userId);
		List<NotesDto> notesDtoRecycleBinlist = recycleBinNotesList.stream()
				.map(note -> mapper.map(note, NotesDto.class)).toList();
		return notesDtoRecycleBinlist;
	}

	@Override
	public void hardDeleteNotes(Integer id) {

		Notes notes = notesRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Notes not availavle"));

		if (notes.getIsDeleted()) {
			notesRepository.delete(notes);
		} else {
			throw new IllegalArgumentException("Invalid Request, You can not hard delete directly.");
		}

	}

	@Override
	public void emptyUserRecycleBin() {
		Integer userId = CommonUtil.getLoggedInUser().getId();
		List<Notes> recycleBinNotesList = notesRepository.findByCreatedByAndIsDeletedTrue(userId);
		if (!CollectionUtils.isEmpty(recycleBinNotesList)) {
			notesRepository.deleteAll(recycleBinNotesList);
		}

	}
	
	@Override
	public Boolean copyNotes(Integer notesId) {
		Notes notes = notesRepository.findById(notesId).orElseThrow(() -> new ResourceNotFoundException("Invalid notes id"));
		
//		Notes copyNotes = new Notes();      ----->  Doing this using builder pattern                
//		copyNotes.setCategory(notes.getCategory());
		
		
		Notes copyNotes = Notes.builder()
				.title(notes.getTitle())
				.description(notes.getDescription())
				.category(notes.getCategory())
				.isDeleted(false)
				.fileDetails(null)
				.build();
		
		Notes copiedNotes = notesRepository.save(copyNotes);
		
		if(!ObjectUtils.isEmpty(copiedNotes)) {
			return true;
		}
		
		return false;
		
	}

	@Override
	public NotesResponse getNotesByUserSearch(String keyword,Integer pageNo, Integer pageSize) {
		
		Integer userId = CommonUtil.getLoggedInUser().getId();
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<Notes> pageNotes = notesRepository.searchNotes(keyword,userId, pageable);

		List<NotesDto> notesDto = pageNotes.get().map(n -> mapper.map(n, NotesDto.class)).toList();

		NotesResponse notes = new NotesResponse().builder().notes(notesDto).pageNo(pageNotes.getNumber())
				.pageSize(pageNotes.getSize()).totalElements(pageNotes.getTotalElements())
				.totalPages(pageNotes.getTotalPages()).isFirst(pageNotes.isFirst()).isLast(pageNotes.isLast()).build();

		return notes;
	}
	

}
