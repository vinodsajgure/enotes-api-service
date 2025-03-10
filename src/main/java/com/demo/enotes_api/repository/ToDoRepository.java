package com.demo.enotes_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.enotes_api.entity.ToDo;

public interface ToDoRepository extends JpaRepository<ToDo, Integer>{

	List<ToDo> findByCreatedBy(Integer userId);

}
