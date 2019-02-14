package com.steereengineering.services;

import java.util.Set;

import com.steereengineering.commands.EmployeeCommand;
import com.steereengineering.model.Employee;

public interface EmployeeService {

      void updatePassword(String password, Long userId);
	  public String getHost();
	  public String getPort();
	  Set<Employee> getPhoneList();
      Employee findById(Long l);
      EmployeeCommand findCommandById(Long l);
      EmployeeCommand savePhoneListCommand(EmployeeCommand command);
      void deleteById(Long idToDelete);
      public Employee findUserByEmail(String email);
      public void saveUser(Employee employee);
      public void saveNewUser(Employee employee);
      public Employee findByResetToken(String resettoken);
      public void resetAllPassword();
      public void resetBlankPasswords();
      public void resetPasswordById(Long l);
}
