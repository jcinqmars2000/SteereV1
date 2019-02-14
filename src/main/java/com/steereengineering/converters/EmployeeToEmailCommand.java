package com.steereengineering.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import com.steereengineering.commands.EmailCommand;
import com.steereengineering.model.Employee;

import lombok.Synchronized;

@Component

public class EmployeeToEmailCommand implements  Converter<Employee, EmailCommand> {
	@Synchronized
	@Nullable
	@Override
	public EmailCommand convert(Employee employee) {

		if (employee != null) {
			final EmailCommand ec = new EmailCommand();
			ec.setEmployee_id(employee.getEmployee_id());
			ec.setPassword(employee.getPassword());
			ec.setConfirmpassword(employee.getConfirmpassword());
			return ec;
		}
		return null;
	}

}
