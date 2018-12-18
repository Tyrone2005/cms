package com.license.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.common.entity.RepJson;
import com.common.utils.LicenseMake;
import com.license.entity.License;
import com.license.service.LicenseService;

@Controller
@RequestMapping("/LicenseController")
public class LicenseController {

	
	@Autowired
	private LicenseService licenseService;
	
	
	@RequestMapping("/create")
	@ResponseBody
	public RepJson create(License lic,HttpServletRequest rq, HttpServletResponse rp) {

		RepJson repJson = new RepJson();
		
		LicenseMake lm = new LicenseMake("/licenseMakeConf.properties", true);
		try {
			lm.create(lic);
		} catch (Exception e) {
			e.printStackTrace();
			repJson.setSuccess(false);
			repJson.setMessage(e.getMessage());
			return repJson;
		}
		repJson.setSuccess(true);
		return repJson;
	}

	@RequestMapping(value = "/remoteCreate", method = { RequestMethod.POST })
	public void remoteCreate(HttpServletRequest rq, HttpServletResponse rp) {

	}
}
