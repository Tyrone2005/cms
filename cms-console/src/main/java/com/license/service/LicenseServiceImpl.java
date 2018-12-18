package com.license.service;

import org.springframework.stereotype.Service;

import com.common.entity.RepJson;

@Service("licenseService")
public class LicenseServiceImpl implements LicenseService {



	@Override
	public RepJson create() {
		RepJson repJson = new RepJson();
		// TODO Auto-generated method stub
		return repJson;
	}

	@Override
	public RepJson downloadLicense() {
		RepJson repJson = new RepJson();
		// TODO Auto-generated method stub

		return repJson;
	}
}
