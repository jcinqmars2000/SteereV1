package com.steereengineering.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.steereengineering.model.Status;

public interface StatusRepository extends JpaRepository<Status, Long> {
	@Query(value = "select status from status order by status", nativeQuery = true)
	public List<String> getStatusValues();
}
