package com.steereengineering.controllers;


import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.steereengineering.model.Employee;
import com.steereengineering.services.EmployeeService;


@Slf4j
@Controller
public class IndexController {

    private final EmployeeService employeeService;

    public IndexController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @RequestMapping({ "/apps/phonelist/index"})
    public String getIndexPage(Model model) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	String currentPrincipalName = authentication.getName();
        Employee user = employeeService.findUserByEmail(currentPrincipalName);
        if (user != null && user.getRoles().stream().anyMatch(r -> r.getRole().equals("Admin")) && user.getActive() == 1) {
          model.addAttribute("isadmin", "true");	
        } else {
          model.addAttribute("isadmin", "false");	
        }
          	//System.out.println("Current Principal Name  = " + currentPrincipalName);
        log.debug("Getting Index page");
        model.addAttribute("employee", employeeService.getPhoneList());
        return "/apps/phonelist/index";
    }
    
    @RequestMapping({ "/index"})
    public String getIndexPageAtRoot(Model model) {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	String currentPrincipalName = authentication.getName();
        Employee user = employeeService.findUserByEmail(currentPrincipalName);
        if (user != null && user.getRoles().stream().anyMatch(r -> r.getRole().equals("Admin")) && user.getActive() == 1) {
          model.addAttribute("isadmin", "true");	
        } else {
          model.addAttribute("isadmin", "false");	
        }
    	//System.out.println("Current Principal Name  = " + currentPrincipalName);
        log.debug("Getting Index page");
        model.addAttribute("employee", employeeService.getPhoneList());
        return "/index";
    }
    
}