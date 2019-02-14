package com.steereengineering.commands;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

@NoArgsConstructor

public class EmployeeCommand {
	private Long employee_id;
	@Size(min=1, max = 255)
	private String firstname;
	@Size(min=1, max = 255)
	private String lastname;
	@Size(min=3, max = 255)
	@Email
	private String email;
	private String extension;
	private String cellphone;
	@Size(min=8, max = 255)
	private String password;
	@Size(min=8, max = 255)
	private String confirmpassword;
}
