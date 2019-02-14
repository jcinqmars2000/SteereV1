package com.steereengineering.converters;


import com.steereengineering.commands.EmployeeCommand;
import com.steereengineering.model.Employee;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class EmployeeToEmployeeCommand implements Converter<Employee, EmployeeCommand> {
	@Synchronized
	@Nullable
	@Override
	public EmployeeCommand convert(Employee employee) {

		if (employee != null) {
			final EmployeeCommand ec = new EmployeeCommand();
			ec.setEmployee_id(employee.getEmployee_id());
			ec.setFirstname(employee.getFirstname());
			ec.setLastname(employee.getLastname());
			ec.setEmail(employee.getEmail());
			ec.setExtension(employee.getExtension());
			ec.setCellphone(employee.getCellphone());
			ec.setPassword(employee.getPassword());
			return ec;
		}
		return null;
	}
}

