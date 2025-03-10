package com.demo.enotes_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.enotes_api.entity.FavouriteNotes;

public interface FavouriteNotesRepository extends JpaRepository<FavouriteNotes, Integer>{

	List<FavouriteNotes> findByUserId(int userId);

}
