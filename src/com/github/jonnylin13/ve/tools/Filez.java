package com.github.jonnylin13.ve.tools;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Filez {
	
	public static String readFile(File file, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
		return new String(encoded, encoding);
	}

}
