package org.cms.genkeypair;

import java.io.IOException;

public class GenkeypairTool {

	public static void main(String[] args) throws IOException {

		Runtime runtime = Runtime.getRuntime();
		runtime.exec("cmd /c start Genkeypair.bat");

	}
}
