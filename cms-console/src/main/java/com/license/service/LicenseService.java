package com.license.service;

import com.common.entity.RepJson;

public interface LicenseService {

	RepJson downloadLicense();
	
	RepJson create();
}
