package com.zeshanaslam.peeld2l;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class ApiHook {

	String username;
	String password;
	
	String URL = "https://pdsb.elearningontario.ca";
	WebClient webClient = new WebClient(BrowserVersion.FIREFOX_38);
	CookieManager cookieManager;
	HtmlPage page = null;
	
	public ApiHook(String username, String password) {
		this.username = username;
		this.password = password;
		
		// Disable logging
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 
	}
	
	public void loadPage() {
		try {
		// Store cookies
		cookieManager = webClient.getCookieManager();
		page = webClient.getPage(URL);
		
		HtmlForm form = page.getFormByName("processLogonForm");
		HtmlSubmitInput button = form.getInputByName("Login");
		HtmlTextInput user = form.getInputByName("userName");
		HtmlPasswordInput pass = form.getInputByName("password");
		
		user.setValueAttribute(username);
		pass.setValueAttribute(password);
		
		page = button.click();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void getCourses() {
		DomElement e = (DomElement) page.getByXPath("//a[@class='d2l-menuflyout-opener d2l-clickable']").iterator().next();
		try {
			page = e.click();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println(page.getWebResponse().getContentAsString());
	}
}
