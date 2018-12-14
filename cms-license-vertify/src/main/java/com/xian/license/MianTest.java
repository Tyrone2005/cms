package com.xian.license;

public class MianTest {

	public static void main(String[] args) throws Exception
	{
		LicenseVertify vlicense=new LicenseVertify("newVerification"); // 项目唯一识别码，对应生成配置文件的subject
		vlicense.install("c:");
		vlicense.vertify();
	}

}
