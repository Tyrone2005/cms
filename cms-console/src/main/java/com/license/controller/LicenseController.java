package com.license.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.common.utils.LicenseMake;
import com.license.entity.License;


@Controller
@RequestMapping("/LicenseController")
public class LicenseController {

	
	@RequestMapping("/create")
	public String create(License lic,HttpServletRequest rq,HttpServletResponse rp) {
		
		LicenseMake lm = new LicenseMake("/licenseMakeConf.properties",true);
		try {
			lm.create(lic);
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		return "success";
	}
	
	
	@RequestMapping("/create")
	public void remoteCreate(HttpServletRequest rq,HttpServletResponse rp) {
		
		
	}
}
