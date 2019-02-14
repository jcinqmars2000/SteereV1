package com.steereengineering.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.steereengineering.model.License;


public interface LicenseRepository extends CrudRepository<License, Long> {

	@Query(value = "select * from license where vendor_id = ?1", nativeQuery = true)
	 public Set<License> findAllById(Long vendor_id);

	}