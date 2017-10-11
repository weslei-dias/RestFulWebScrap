package com.example.travel.controller;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

public class teste {

	
	public static void main(String[] args) {
		DesiredCapabilities dcaps = new DesiredCapabilities();
    	dcaps.setJavascriptEnabled(true);
    	dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
    			"M:\\TRAVEL\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
    	WebDriver driver = new PhantomJSDriver(dcaps);
	}
}
