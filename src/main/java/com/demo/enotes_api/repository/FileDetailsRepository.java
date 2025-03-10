package com.demo.enotes_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.enotes_api.entity.FileDetails;

public interface FileDetailsRepository extends JpaRepository<FileDetails, Integer> {

}
