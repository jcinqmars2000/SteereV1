package com.steereengineering.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
@Entity
@Table(name = "domainlist")
public class DomainList {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "domain_id")
	private Long domain_id;	
	@Column(name = "domaintype")
	@Length(min = 1, message = "*The type must have at least 1 characters")
	private String domaintype;
	@Column(name = "domainvalue")
	@Length(min = 1, message = "*Your vaule must have at least 1 characters")
	private String domainvalue;
	@Column(name = "active")
	private int active;
}
