package com.steereengineering.services;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.steereengineering.model.Vendor;
import com.steereengineering.repositories.VendorRepository;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service

public class VendorServiceImpl implements VendorService {
    private final VendorRepository vendorRepository;
   
    public VendorServiceImpl(VendorRepository vendorRepository) {
    	this.vendorRepository = vendorRepository;
    }
      
	@Override
	public Set<Vendor> getVendorList() {
         Set<Vendor> vendorList =  new LinkedHashSet<Vendor>();
          vendorRepository.getOrderByVendorName().iterator().forEachRemaining(vendorList::add); 
		return vendorList;
	}

	@Override
	public Optional<Vendor> findById(Long id) {
		return vendorRepository.findById(id);
	}
}
