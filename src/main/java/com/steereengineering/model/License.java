package com.steereengineering.model;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


import lombok.Data;

@Data
@Entity(name = "License")
@Table(name = "license")

public class License {
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long license_id;
	private String licensename;
	private String licensetype;
	private String licensekey;
	private Date licensepurchaseddate;
	private Date licenseexpirationdate;
	private Integer licensedaystorenewal;

}
