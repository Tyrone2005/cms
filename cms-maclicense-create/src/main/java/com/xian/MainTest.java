package com.xian;


public class MainTest {
	
	public static void main(String[] args) {
		System.out.println("开始创建证书");
		LicenseMake clicense=new LicenseMake("licenseMakeConf.properties");  
        clicense.create();
	}
}
