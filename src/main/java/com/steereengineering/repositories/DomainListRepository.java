package com.steereengineering.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.steereengineering.model.DomainList;

public interface DomainListRepository extends JpaRepository<DomainList, Long> {
	@Query(value = "select distinct domainvalue from domainslist where domaintype = ?0 and active = 1  order by value", nativeQuery = true)
	 public List<String> getDomainList(String domaintype);
	@Query(value = "select distinct domaintype from domainslist order by type", nativeQuery = true)
	 public List<String> getDomainTypeList();
}
