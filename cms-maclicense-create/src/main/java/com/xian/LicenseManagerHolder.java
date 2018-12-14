package com.xian;

import mlicense.LicenseManager;
import mlicense.LicenseParam;

/**
 * 
 * @ClassName:  LicenseManagerHolder   
 * @Description:证书管理（单类）
 * @author: Tyrone 
 * @date:   2018年12月12日 下午2:40:59   
 *     
 * @Copyright: 2018 www.hengyunsoft.com Inc. All rights reserved.
 */
public class LicenseManagerHolder {
	private static LicenseManager licenseManager;
	private LicenseManagerHolder(){}
	public static synchronized LicenseManager getLicenseManager(LicenseParam param){
		if(licenseManager == null){
			licenseManager = new LicenseManager(param);
		}
		return licenseManager;
	}
}
