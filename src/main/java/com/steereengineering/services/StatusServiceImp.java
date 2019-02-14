package com.steereengineering.services;

import java.util.List;

import org.springframework.stereotype.Service;


import com.steereengineering.repositories.StatusRepository;

@Service
public class StatusServiceImp {
	private final StatusRepository statusRepository;
	public StatusServiceImp(StatusRepository statusRepository) {
		this.statusRepository = statusRepository;
	}
	
	public List<String> getStatusValues() {
		return statusRepository.getStatusValues();
	}
}
