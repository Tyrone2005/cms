package com.xian.maclicense;


public class Mian {

	public static void main(String[] args) throws Exception
	{
		// 项目唯一识别码，对应生成配置文件的subject
		LicenseVertify vlicense=new LicenseVertify("newVerification"); 
		
		//license安装
//		vlicense.install( "c:");
		
		//license验证
		vlicense.vertify();
		
	}

}
