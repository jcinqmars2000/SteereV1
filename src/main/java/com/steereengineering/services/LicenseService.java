package com.steereengineering.services;

import java.util.Set;

import org.springframework.stereotype.Service;
import com.steereengineering.model.License;
@Service
public interface LicenseService {
	
    	public String getHost();
		public String getPort();
		Set<License> getVendorLicenses(Long vender_id);
		void deleteById(Long idToDelete);

}
