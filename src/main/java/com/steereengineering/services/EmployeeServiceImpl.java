package com.steereengineering.services;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.steereengineering.repositories.EmployeeRepository;
import com.steereengineering.repositories.RoleRepository;
import lombok.extern.slf4j.Slf4j;

import com.steereengineering.commands.EmployeeCommand;
import com.steereengineering.converters.EmployeeCommandToEmployee;
import com.steereengineering.converters.EmployeeToEmployeeCommand;
import com.steereengineering.model.*;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final static String pwSuffix = "123!";
    private final EmployeeRepository employeeRepository;
    private final EmployeeCommandToEmployee  employeeCommandToEmployee;
    private final EmployeeToEmployeeCommand  employeeToEmployeeCommand;
    @Autowired
    private RoleRepository roleRepository;
    @Value("${application.port}")
    private String port;
    @Value("${application.host}")
    private String host;

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,EmployeeCommandToEmployee  employeeCommandToEmployee, EmployeeToEmployeeCommand  employeeToEmployeeCommand) {
        this.employeeRepository = employeeRepository;
        this.employeeCommandToEmployee = employeeCommandToEmployee;
        this.employeeToEmployeeCommand = employeeToEmployeeCommand;
    }

    @Override
    public Set<Employee> getPhoneList() {

        Set<Employee> phoneListSet = new LinkedHashSet<Employee>();

        employeeRepository.getOrderByFirstname().iterator().forEachRemaining(phoneListSet::add);
		/*for(Employee phone : phoneListSet) {
			System.out.println(phone.toString());
		}*/

        return phoneListSet;
    }

    @Override
    public Employee findById(Long l) {

        Optional<Employee> phoneListOptional = employeeRepository.findById(l);

        if (!phoneListOptional.isPresent()) {
            throw new RuntimeException("Name Not Found!");
        }

        return phoneListOptional.get();
    }

    @Override
    @Transactional
    public EmployeeCommand savePhoneListCommand(EmployeeCommand command) {
        Employee detachedPhoneList = employeeCommandToEmployee.convert(command);

        Employee savePhoneList = employeeRepository.save(detachedPhoneList);
        log.debug("Saved RecipeID:" + savePhoneList.getEmployee_id());
        return employeeToEmployeeCommand.convert(savePhoneList);
    }

    @Override
    @Transactional
    public EmployeeCommand findCommandById(Long l) {
        return employeeToEmployeeCommand.convert(findById(l));
    }

    @Override
    public void deleteById(Long idToDelete) {
        employeeRepository.deleteById(idToDelete);
    }

    @Override
    public Employee findUserByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }


    @Override
    public void saveUser(Employee employee) {
        employee.setActive(1);
        employeeRepository.save(employee);
    }

    @Override
    public void saveNewUser(Employee employee) {
        employee.setActive(1);
        Role userRole = roleRepository.findByRole("User");
        employee.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        employeeRepository.save(employee);
    }



    @Override
    public Employee findByResetToken(String resettoken) {
        return  employeeRepository.findByResetToken(resettoken);

    }


    @Override
    public void updatePassword(String password, Long userId) {
        employeeRepository.updatePassword(password, userId);

    }

    @Override
    public void resetAllPassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        List<Employee> allEmployees =   employeeRepository.getOrderByFirstname();
        allEmployees = employeeRepository.getOrderByFirstname();
        Iterator<Employee> employees = allEmployees.iterator();
        while(employees.hasNext()) {
            Employee employee = employees.next();
            String newPasswd = encoder.encode(employee.getEmail()+pwSuffix);
            employee.setPassword(newPasswd);
            employee.setConfirmpassword(newPasswd);
            employeeRepository.save(employee);
        }

    }

    @Override
    public void resetBlankPasswords() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        List<Employee> allEmployees =   employeeRepository.getOrderByFirstname();
        allEmployees = employeeRepository.getOrderByFirstname();
        Iterator<Employee> employees = allEmployees.iterator();
        while(employees.hasNext()) {
            Employee employee = employees.next();
            if (employee.getPassword()==null || employee.getPassword().length()==0) {
                String newPasswd = encoder.encode(employee.getEmail()+pwSuffix);
                employee.setPassword(newPasswd);
                employee.setConfirmpassword(newPasswd);
                employeeRepository.save(employee);
            }

        }
    }

    @Override
    public void resetPasswordById(Long l) {
        // TODO Auto-generated method stub

    }


}

