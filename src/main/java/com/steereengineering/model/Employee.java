package com.steereengineering.model;


import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import  javax.validation.constraints.Email;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotEmpty;
import org.springframework.data.annotation.Transient;



//@Id
//@GeneratedValue(strategy = GenerationType.IDENTITY)
@Data
@Entity
@Table(name = "employee")
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "employee_id")
	private Long employee_id;
	@Column(name = "email")
	@Email(message = "*Please provide a valid Email")
	@NotEmpty(message = "*Please provide an email")
	private String email;
	@Column(name = "password")
	@Length(min = 8, message = "*Your password must have at least 8 characters")
	@Transient
	private String password;
	@Column(name = "confirmpassword")
	@Length(min = 8, message = "*Your password must have at least 8 characters")
	@Transient
	private String confirmpassword;
	@Column(name = "firstname")
	@NotEmpty(message = "*Please provide your first name")
	private String firstname;
	@Column(name = "lastname")
	@NotEmpty(message = "*Please provide your last name")
	private String lastname;
	@Column(name = "active")
	private int active;
	@Column(name = "extension")
	private String extension;
	@Column(name = "cellphone")
	@NotEmpty(message = "*Please provide your cell phone number")
	private String cellphone;
	@Column(name = "resettoken")
	private String resettoken;
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "employee_role", joinColumns = @JoinColumn(name = "employee_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;

}



