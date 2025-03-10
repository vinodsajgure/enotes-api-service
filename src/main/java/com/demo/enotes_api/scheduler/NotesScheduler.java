package com.demo.enotes_api.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.demo.enotes_api.entity.Notes;
import com.demo.enotes_api.repository.NotesRepository;

@Component
public class NotesScheduler {
	
	@Autowired
	private NotesRepository notesRepository;

	@Scheduled(cron ="0 0 0 * * ?")
	public void deleteNotesFromRecyceBinAfterSevenDaysScheduler() {

		LocalDateTime cutOffDate = LocalDateTime.now().minusDays(7);
		List<Notes> deletedNotes = notesRepository.findAllByIsDeletedAndDeletedOnBefore(true,cutOffDate);
		notesRepository.deleteAll(deletedNotes);
	}

}
