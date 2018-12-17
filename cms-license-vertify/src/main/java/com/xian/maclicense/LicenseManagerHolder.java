package com.xian.maclicense;

import mlicense.LicenseManager;
import mlicense.LicenseParam;

/**
 * 
 * @ClassName:  LicenseManagerHolder   
 * @Description:
 * @author: Tyrone 
 * @date:   2018年12月17日 下午3:13:08   
 *     
 * @Copyright: 2018 www.hengyunsoft.com Inc. All rights reserved.
 */
public class LicenseManagerHolder
{
	private static LicenseManager licenseManager;
	private LicenseManagerHolder(){}
	public static synchronized  LicenseManager getLicenseManager(LicenseParam param)
	{
		if(licenseManager==null)
		{
			licenseManager=new LicenseManager(param);
		}
		return licenseManager;
	}
}

