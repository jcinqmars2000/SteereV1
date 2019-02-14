package com.steereengineering.commands;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import com.steereengineering.constraint.FieldMatch;

import lombok.Builder;
import lombok.Data;

@Data
@FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match")
public class EmailCommand {
	private  long employee_id;
	@Size(min=8, max = 255)
	private String confirmpassword;
	@Size(min=8, max = 255)
	private String password;
	@Email
	private String email;
}
