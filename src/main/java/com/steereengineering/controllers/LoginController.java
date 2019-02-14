package com.steereengineering.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.steereengineering.commands.EmailCommand;
import com.steereengineering.configuration.EmailDetails;
import com.steereengineering.converters.EmployeeToEmailCommand;
import com.steereengineering.model.Employee;
import com.steereengineering.services.EmailService;
import com.steereengineering.services.EmployeeService;
import com.steereengineering.utils.TokenGenerator;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
public class LoginController {

	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private EmployeeToEmailCommand employeeToEmailCommand;


	@RequestMapping(value={"/login"}, method = RequestMethod.GET)
	public ModelAndView login(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/login");
		return modelAndView;
	}

	@RequestMapping(value="/registration", method = RequestMethod.GET)
	public ModelAndView registration(){
		ModelAndView modelAndView = new ModelAndView();
		Employee employee = new Employee();
		modelAndView.addObject("employee", employee);
		modelAndView.setViewName("/registration");
		return modelAndView;
	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public ModelAndView createNewUser(@Valid Employee employee, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		Employee userExists = employeeService.findUserByEmail(employee.getEmail());
		if (userExists != null) {
			bindingResult
			.rejectValue("email", "error.user",
					"*There is already a user registered with the email provided");
		}
		if (employee.getConfirmpassword().compareTo(employee.getPassword()) != 0) {
			bindingResult
			.rejectValue("password", "error.user",
					"*The password and confirm password do not match.");
		}
		if (!bindingResult.hasErrors()) {
			String ePassword = bCryptPasswordEncoder.encode(employee.getPassword());
			employee.setPassword(ePassword);
			employee.setConfirmpassword(ePassword);
			employeeService.saveNewUser(employee);
			modelAndView.addObject("successMessage", "Employee has been registered successfully");
			modelAndView.addObject("employee", new Employee());

		}
		modelAndView.setViewName("/registration");
		return modelAndView;
	}

	@RequestMapping(value="/admin/home", method = RequestMethod.GET)
	public ModelAndView home(){
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Employee employee = employeeService.findUserByEmail(auth.getName());
		modelAndView.addObject("employeeName", "Welcome " + employee.getFirstname() + " " + employee.getLastname() + " (" + employee.getEmail() + ")");
		modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
		modelAndView.setViewName("/admin/home");
		return modelAndView;
	}

	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		//System.out.print("LOGOUT PARAMETER  = " + request.getParameter("logout"));
		if (auth != null){    
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login?login";//You can redirect wherever you want, but generally it's a good practice to show login screen again.
	}

	@RequestMapping(value="/password/reset/all", method = RequestMethod.GET)
	public String  resetAllPasswords(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentPrincipalName = authentication.getName();
		Employee user = employeeService.findUserByEmail(currentPrincipalName);
		if (user != null && user.getRoles().stream().anyMatch(r -> r.getRole().equals("Admin")) && user.getActive() == 1) {
			model.addAttribute("isadmin", "true");	
		} else {
			model.addAttribute("isadmin", "false");	
		}
		employeeService.resetAllPassword();
		//System.out.println("Current Principal Name  = " + currentPrincipalName);
		log.debug("Getting Index page");
		model.addAttribute("employee", employeeService.getPhoneList());
		return "/apps/phonelist/index";
	}

	@RequestMapping(value="/password/reset", method = RequestMethod.POST)
	public ModelAndView resetPage(@ModelAttribute EmailCommand emailCommand){
		ModelAndView modelAndView = new ModelAndView();
		Employee employee = new Employee();
		if(emailCommand.getEmail() != null) { 
			employee = employeeService.findUserByEmail(emailCommand.getEmail());
			if(employee == null) {
				String errorMessage = "Email address not registered or email address invalid.";
				modelAndView.addObject("errorMsg", errorMessage);
			} else {
				String resetToken = TokenGenerator.generateToken();
				employee.setResettoken(resetToken);
				if (employee.getPassword() == null) {
					employee.setPassword(employee.getEmail() + "123!");
					employee.setConfirmpassword(employee.getEmail() + "123!");
				}
				employeeService.saveUser(employee);
				EmailDetails emailDetails = new EmailDetails();
				emailDetails.setEmailSubject("Steere Intranet Password Reset");
				emailDetails.setEmailTo(employee.getEmail());
				StringBuffer body = new StringBuffer();
				body.append("Here is your Steere Intranet password reset link.\n\n");

				String link = "http://" + employeeService.getHost() + ":" + employeeService.getPort() + "/password/reset/" + resetToken + "/reset";
				body.append(link);
				emailDetails.setEmailBody(body.toString());
				try {
					emailService.processEmailRequest(emailDetails);
					String errorMessage = "Your password reset link was sent to mail address " + employee.getEmail() + ".";
					modelAndView.addObject("errorMsg", errorMessage);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} 
		modelAndView.setViewName("/apps/password/reset/forgotPassword");
		return modelAndView;
	}

	@RequestMapping(value="/password/reset", method = RequestMethod.GET)
	public ModelAndView showResetPage(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/apps/password/reset/forgotPassword");
		return modelAndView;
	}

	@RequestMapping(value="/password/reset/{resettoken}/reset", method = RequestMethod.GET)
	public ModelAndView resetPasswordGet(@PathVariable String resettoken){

		ModelAndView modelAndView = new ModelAndView();
		Employee employee = employeeService.findByResetToken(resettoken);
		EmailCommand emailCommand = employeeToEmailCommand.convert(employee);
		if(emailCommand == null) {
			String errorMessage = "Invalid Reset Link!";
			modelAndView.addObject("errorMsg", errorMessage);
		} else {	
			modelAndView.addObject("emailCommand", emailCommand);
			modelAndView.setViewName("/apps/password/reset/resetPassword");
		}
		modelAndView.addObject("action", "/password/reset/" + resettoken + "/reset");
		return modelAndView;
	}

	@RequestMapping(value="/error", method = RequestMethod.GET)
	public ModelAndView error() {
		ModelAndView mav = new ModelAndView();
		String errorMessage= "You are not authorized for the requested data.";
		mav.addObject("errorMsg", errorMessage);
		mav.setViewName("403");
		return mav;
	}	

	@RequestMapping(value="/password/reset/{resettoken}/reset", method = RequestMethod.POST)
	public ModelAndView submitPasswordReset(@PathVariable String resettoken, @Valid EmailCommand emailCommand, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		Employee employee = employeeService.findByResetToken(resettoken);
		if (employee == null) {
			modelAndView.addObject("successMessage", "Ivalid Reset Request");
			modelAndView.addObject("emailcommand", emailCommand);
			modelAndView.setViewName("/apps/password/reset/resetPassword");
			return modelAndView;
		}
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("/apps/password/reset/resetPassword");
		} else {
			String ePassword = bCryptPasswordEncoder.encode(emailCommand.getPassword());
			employee.setPassword(ePassword);
			employee.setConfirmpassword(ePassword);
			employeeService.saveUser(employee);;
			modelAndView.addObject("successMessage", "Your password was successfully changed.");
			modelAndView.addObject("emailcommand", emailCommand);
			modelAndView.setViewName("/apps/password/reset/resetPassword");
		}
		modelAndView.addObject("action", "/password/reset/" + resettoken + "/reset"); 
		return modelAndView;
	}

}
