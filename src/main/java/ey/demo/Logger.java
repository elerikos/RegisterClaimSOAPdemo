package ey.demo;

import javax.swing.JTextArea;

public  class  Logger {
	
	 static JTextArea area;
	
	public static void log(String messaage) {
		area.append(messaage+"\r\n");
		System.out.println(messaage);
		
	}

}
