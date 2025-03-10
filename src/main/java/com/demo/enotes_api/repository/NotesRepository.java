package com.demo.enotes_api.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.enotes_api.entity.Notes;

@Repository
public interface NotesRepository extends JpaRepository<Notes, Integer> {

	Page<Notes> findByCreatedByAndIsDeletedFalse(Integer userId, Pageable pageable);
	
	List<Notes> findByCreatedByAndIsDeletedTrue(Integer userId);

	List<Notes> findAllByIsDeletedAndDeletedOnBefore(boolean b, LocalDateTime cutOffDate);

	List<Notes> findAllByIsDeletedFalse();

	@Query("select n from Notes n where (lower(n.title) like lower(concat('%',:keyword,'%')) "
			+ "or lower(n.description) like lower(concat('%',:keyword,'%')) "
			+ "or lower(n.category.name) like lower(concat('%',:keyword,'%'))) "
			+ "and n.isDeleted=false "
			+ "and n.createdBy=:userId")
	Page<Notes> searchNotes(@Param("keyword")String keyword,@Param("userId")Integer userId, Pageable pageable);

	

}
