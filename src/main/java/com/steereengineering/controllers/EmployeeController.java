package com.steereengineering.controllers;

import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.steereengineering.commands.EmployeeCommand;
import com.steereengineering.model.Employee;
import com.steereengineering.model.Role;
import com.steereengineering.services.EmployeeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class EmployeeController {
	
	private static final String PHONELIST_PHONELISTFORM_URL = "/apps/phonelist/phonelistform";
	public final EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@GetMapping
	@RequestMapping("/apps/phonelist/{id}/show")
	public String showById(@PathVariable String id, Model model){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	String currentPrincipalName = authentication.getName();
        Employee user = employeeService.findUserByEmail(currentPrincipalName);
        if (user != null && user.getRoles().stream().anyMatch(r -> r.getRole().equals("Admin")) && user.getActive() == 1) {
          model.addAttribute("isadmin", "true");	
        } else {
          model.addAttribute("isadmin", "false");	
        }
        Employee employee = employeeService.findById(new Long(id));
        Set<Role> roles = employee.getRoles();
        boolean is_admin = (employee.getRoles().stream().anyMatch(r -> r.getRole().equals("Admin")) ?  true : false);
        boolean is_user = (employee.getRoles().stream().anyMatch(r -> r.getRole().equals("User")) ?   true : false);
        boolean is_superuser = (employee.getRoles().stream().anyMatch(r -> r.getRole().equals("SuperUser")) ?   true : false);
        model.addAttribute("is_admin", is_admin);
        model.addAttribute("is_superuser",is_superuser);
        model.addAttribute("is_user", is_user);
        model.addAttribute("employee", employee);
        model.addAttribute("roles", roles);
       
		return "/apps/phonelist/show";
	}
	@PostMapping("/apps/phonelist")
    public String saveOrUpdate(@Validated @ModelAttribute("employee") EmployeeCommand command, BindingResult bindingResult){
		System.out.println("inside saveOrUpdate");
		log.debug("Binding Results has errors = " + bindingResult.hasErrors());
		if(bindingResult.hasErrors()){
			System.out.println("inside saveOrUpdate -> ErrorCount = " + bindingResult.getErrorCount());
			bindingResult.getAllErrors().forEach(objectError-> {
			System.out.println(objectError.toString());
		});
		return 	PHONELIST_PHONELISTFORM_URL;
		}	
		
        EmployeeCommand savedCommand = employeeService.savePhoneListCommand(command);
        savedCommand.getEmployee_id();
     /*   return "redirect:/phonelist/" + savedCommand.getId() + "/show";*/
        return "redirect:/apps/phonelist/index";

    }

	@GetMapping
	@RequestMapping("/apps/phonelist/new")
	public String newPhoneList(Model model){
		model.addAttribute("employee", new EmployeeCommand());
		return PHONELIST_PHONELISTFORM_URL;
	}

	@GetMapping
	@RequestMapping("/apps/phonelist/{id}/update")
	public String updatePhoneList(@PathVariable String id, Model model){
		model.addAttribute("employee", employeeService.findCommandById(Long.valueOf(id)));
		return PHONELIST_PHONELISTFORM_URL;
	}
	@GetMapping
	@RequestMapping("/apps/phonelist/{id}/delete")
	public String deleteById(@PathVariable String id){

		log.debug("Deleting id: " + id);

		employeeService.deleteById(Long.valueOf(id));
		return "redirect:/apps/phonelist/index";
	}
	
	/*@GetMapping
	@RequestMapping("/error")
	public String error(Model Model){
		return "redirect:/intranet";
	}*/
}

