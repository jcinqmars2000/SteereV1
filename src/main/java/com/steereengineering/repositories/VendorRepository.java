package com.steereengineering.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.steereengineering.model.Vendor;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
	@Query(value = "select * from vendor order by vendorname", nativeQuery = true)
	 public List<Vendor> getOrderByVendorName();

}
