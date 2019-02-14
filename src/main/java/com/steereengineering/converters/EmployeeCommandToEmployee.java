package com.steereengineering.converters;

import org.springframework.stereotype.Component;
import com.steereengineering.commands.EmployeeCommand;
import com.steereengineering.model.Employee;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

@Component
public class EmployeeCommandToEmployee implements Converter<EmployeeCommand, Employee> {
	@Synchronized
	@Nullable
	@Override
	public Employee convert(EmployeeCommand source) {
		if (source == null) {
			return null;
		}

		final Employee el = new Employee();
		el.setEmployee_id(source.getEmployee_id());
		el.setFirstname(source.getFirstname());
		el.setLastname(source.getLastname());
		el.setExtension(source.getExtension());
		el.setEmail(source.getEmail());
		el.setCellphone(source.getCellphone());
		el.setPassword(source.getPassword());
		return el;
	}

}
