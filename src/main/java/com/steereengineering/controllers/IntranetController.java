package com.steereengineering.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletResponse;

import com.esotericsoftware.minlog.Log;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.steereengineering.model.Employee;
import com.steereengineering.model.Manual;
import com.steereengineering.services.EmployeeService;
import com.steereengineering.services.ManualService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller


public class IntranetController {

	private EmployeeService employeeService;
	private ManualService manualService;

	public IntranetController(EmployeeService employeeService, ManualService manualService) {
		this.employeeService = employeeService;
		this.manualService = manualService;
	}

	@RequestMapping(value={"/", "/intranet", "/intranet/"}, method = RequestMethod.GET)
	public String intranet(Model model){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		//Log.debug("currentPrincipalName = " + currentPrincipalName);
		if (currentPrincipalName =="anonymousUser") {
			model.addAttribute("isloggedin","false");
		} else {
			model.addAttribute("isloggedin","true");
		}
		Employee user = employeeService.findUserByEmail(currentPrincipalName);
		if (user != null && user.getRoles().stream().anyMatch(r -> r.getRole().equals("Admin")) && user.getActive() == 1) {
			model.addAttribute("isadmin", "true");	
		} else {
			model.addAttribute("isadmin", "false");	
		}
		//Log.debug("Current Principal Name  = " + currentPrincipalName);
		return "/intranet";
	}
	@RequestMapping(value={"/public/employeelist/phonelist"}, method = RequestMethod.GET)
	public String getPhoneList(Model model) {
		Log.debug("Inside of getPhoneList Method" );
		model.addAttribute("isadmin", "false");	
		log.debug("Getting Public Index page");
		model.addAttribute("employee", employeeService.getPhoneList());
		return "/public/employeelist/phonelist";
	}
	@RequestMapping(value = "/emergency", method = RequestMethod.GET)
	public String getEmergencyInfo() {
		return "/static/emergency";
	}
	@RequestMapping(value = "/manuals", method = RequestMethod.GET)
	public String getManuals() {
		return "/static/manuals";
	}

	@GetMapping(value = "/manual/{id}")
	public StreamingResponseBody getSteamingFile(@PathVariable String id, HttpServletResponse response) throws IOException,  URISyntaxException {
		Manual manual =  manualService.getManualByName(id);
		Log.debug(manual.getManual_id().toString());
		Log.debug(manual.getName());
		Log.debug(manual.getPath());
		Log.debug(manual.getType());
		
		//File file = new File(getClass().getClassLoader().getResource("employee_manual_2018.pdf").toURI());
		//FileSystemResource fsr = new FileSystemResource("C:/workspace-sts-3.9.4.RELEASE/Steere Documents/employee_manual_2018.pdf");
		FileSystemResource fsr = new FileSystemResource(manual.getPath());
		File file = fsr.getFile();
		Log.debug("File Name = " + file.getName());
		//viewing in web browser
		//response.setContentType("application/pdf");
		response.setContentType(manual.getType());
		//for downloading the file directly if viewing is not possible
		response.setHeader("Content-Disposition", "inline; filename=" + file.getName());
		file = null;

		// put the directory architecture according to your target directory
		// generated during compilation in maven spring boot
		InputStream inputStream = new FileInputStream(manual.getPath());
		return outputStream -> {
			int nRead;
			byte[] data = new byte[1024];
			while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
				outputStream.write(data, 0, nRead);
			}
			inputStream.close();
		};
	}

}
