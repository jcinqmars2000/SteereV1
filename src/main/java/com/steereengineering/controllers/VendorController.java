package com.steereengineering.controllers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.steereengineering.model.Employee;
import com.steereengineering.model.License;
import com.steereengineering.model.Vendor;
import com.steereengineering.services.EmployeeService;
import com.steereengineering.services.VendorService;
import com.steereengineering.services.LicenseService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller

public class VendorController {
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

	private final EmployeeService employeeService;
	private final VendorService vendorService;
	private final LicenseService licenseService;

	public VendorController(EmployeeService employeeService, VendorService vendorService, LicenseService licenseService) {
		this.employeeService = employeeService;
		this.vendorService = vendorService;
		this.licenseService = licenseService; 
	}

	@RequestMapping({ "/apps/vendor/index"})
	public String getIndexPageAtRoot(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		Employee user = employeeService.findUserByEmail(currentPrincipalName);
		if (user != null && user.getRoles().stream().anyMatch(r -> r.getRole().equals("Admin")) && user.getActive() == 1) {
			model.addAttribute("isadmin", "true");	
		} else {
			model.addAttribute("isadmin", "false");	
		}
		System.out.println("Current Principal Name  = " + currentPrincipalName);
		log.debug("Getting Index page");
		model.addAttribute("vendor", vendorService.getVendorList());
		return "/apps/vendor/index";
	}
	@GetMapping
	@RequestMapping("/apps/vendor/{id}/show")
	public String showById(@PathVariable String id, Model model){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		Employee user = employeeService.findUserByEmail(currentPrincipalName);
		if (user != null && user.getRoles().stream().anyMatch(r -> r.getRole().equals("Admin")) && user.getActive() == 1) {
			model.addAttribute("isadmin", "true");	
		} else {
			model.addAttribute("isadmin", "false");	
		}
		System.out.println("ID = " + id);
		Vendor vendor = vendorService.findById(Long.valueOf(id)).get();
		Set<License> licenses = licenseService.getVendorLicenses(Long.valueOf(id));
		model.addAttribute("vendor", vendor);
        System.out.println("number of Licenses = " + licenses.size());
        model.addAttribute("license", licenses);
		return "/apps/vendor/show";
	}
	@GetMapping
	@RequestMapping("/apps/vendor/{id}/delete")
	public String deleteById(@PathVariable String id){

		log.debug("Deleting id: " + id);

		//vendorService.deleteById(Long.valueOf(id));
		return "redirect:/apps/vendor/index";
	}
}