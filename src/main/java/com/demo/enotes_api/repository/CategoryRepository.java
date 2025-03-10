package com.demo.enotes_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.enotes_api.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

	public Optional<Category> findByIdAndIsDeletedFalse(Integer id);

	public List<Category> findByIsDeletedFalse();

	public List<Category> findByIsActiveTrueAndIsDeletedFalse();

	public Boolean existsByName(String name);

}






