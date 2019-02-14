package com.steereengineering.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.steereengineering.repositories.DomainListRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DomainListServiceImpl implements DomainListService {
	private final DomainListRepository domainListRepository;
	
	public DomainListServiceImpl(DomainListRepository domainListRepository) {
		this.domainListRepository = domainListRepository;
	}
	
	@Override
	public List<String> getDomainList(String domaintype) {
		 return domainListRepository.getDomainList(domaintype);
	}

	@Override
	public List<String> getDomainTypeList() {
		return domainListRepository.getDomainTypeList();
	}

}
