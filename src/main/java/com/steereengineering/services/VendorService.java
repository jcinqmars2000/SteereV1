package com.steereengineering.services;

import java.util.Optional;
import java.util.Set;

import com.steereengineering.model.Vendor;

public interface VendorService {
	Set<Vendor> getVendorList();

	Optional<Vendor> findById(Long valueOf);

}
