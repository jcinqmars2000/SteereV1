package com.steereengineering.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.steereengineering.model.Manual;

public interface ManualRepository extends JpaRepository<Manual, Long > {
	@Query(value = "select * from manual where name = ?1", nativeQuery = true)
	public Optional<Manual> getManualByName(String name);
	
}
