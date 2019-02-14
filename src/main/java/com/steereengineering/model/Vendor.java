package com.steereengineering.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.URL;

import lombok.Data;

@Data
@Entity
@Table(name = "vendor")
public class Vendor {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "vendor_id", unique = true)
	private Long vendor_id;
	@Column(name = "vendorname" , unique = true)
	@NotEmpty(message = "*Please provide a Vendor name")
	private String vendorname;
	@Column(name = "vendortype")
	@NotEmpty(message = "*Please select a Vendor Type")
	private String vendortype;
	@URL(protocol = "http")
	private String vendorurl;
	
}
