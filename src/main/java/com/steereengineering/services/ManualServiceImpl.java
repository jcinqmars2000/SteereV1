package com.steereengineering.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.steereengineering.model.Manual;
import com.steereengineering.repositories.ManualRepository;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ManualServiceImpl implements ManualService {
	private final ManualRepository manualRepository;

	@Value("${application.port}")
	private String port;
	@Value("${application.host}")
	private String host;

	public ManualServiceImpl(ManualRepository manualRepository) {
		this.manualRepository = manualRepository;
	}

	@Override
	public Manual getManualByName(String name) {
		
		Optional<Manual> manual = manualRepository.getManualByName(name);

		if (!manual.isPresent()) {
			throw new RuntimeException("Manual Not Found!");
		}
		log.debug( "manual = "  + manual.get().getManual_id());
		log.debug( "manual = "  + manual.get().getName());
		log.debug( "manual = "  + manual.get().getPath());
		log.debug( "manual = "  + manual.get().getType());
		return manual.get();
	}



}
