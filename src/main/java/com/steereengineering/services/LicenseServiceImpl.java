package com.steereengineering.services;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.steereengineering.model.Employee;
import com.steereengineering.model.License;
import com.steereengineering.repositories.LicenseRepository;

/**
 * @author jcinq
 *
 */
@Service
public class LicenseServiceImpl implements LicenseService {
	@Value("${application.port}")
	private String port;
	@Value("${application.host}")
	private String host;
	
	public LicenseServiceImpl(LicenseRepository licenseRespository) {
		this.licenseRespository = licenseRespository;
	}

	private final LicenseRepository licenseRespository;
		

	public String getHost() {
		return host;
	}

	public String getPort() {
		return port;
	}
	

	@Override
	public Set<License> getVendorLicenses(Long vendor_id) {
		Set<License> licenseSet = new LinkedHashSet<License>();
		licenseRespository.findAllById(vendor_id).iterator().forEachRemaining(licenseSet::add);
		 return licenseSet;
	}

	@Override
	public void deleteById(Long idToDelete) {
		// TODO Auto-generated method stub
		
	}

}
