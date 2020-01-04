package com.database;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.WebRequest;

/**
 * This class contains all of our endpoints for communication.
 * 
 * @author Thane Storley, Nickolas Mitchell
 *
 */
@SpringBootApplication
public class DatabaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(DatabaseApplication.class, args);
	}

	@RestController
	class ApplicationController implements ErrorController {
		@Autowired
		private ErrorAttributes errorAttributes;

		@Override
		public String getErrorPath() {
			return "/error";
		}

		@RequestMapping("/error")
		public String error(HttpServletRequest servletRequest, Model model) {
			Map<String, Object> attrs = errorAttributes
					.getErrorAttributes((WebRequest) new ServletRequestAttributes(servletRequest), false);
			model.addAttribute("attrs", attrs);
			return "error: " + attrs.toString();
		}
	}
}
