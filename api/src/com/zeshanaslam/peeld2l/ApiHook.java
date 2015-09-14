package com.zeshanaslam.peeld2l;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.gargoylesoftware.htmlunit.AjaxController;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;

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

	public List<CourseObject> getCourses() {
		List<CourseObject> courses = new ArrayList<>();
		try {
			// Wait for javascript to load
			JavaScriptJobManager manager = page.getEnclosingWindow().getJobManager();
			while (manager.getJobCount() > 5) {}

			for (Object object : page.getByXPath("//a[@class='vui-link d2l-link d2l-left']")) {
				DomElement de = (DomElement) object;
				courses.add(new CourseObject(de.getAttribute("href").replace("/d2l/lp/ouHome/home.d2l?ou=", ""), de.getAttribute("title").replace("Enter", "")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return courses;
	}

	public List<ContentObject> getCourseContent(String ID) {
		List<ContentObject> list = new ArrayList<>();
		try {
			WebClient webClient = new WebClient(BrowserVersion.FIREFOX_38);
			webClient.setCookieManager(cookieManager);

			HtmlPage page = webClient.getPage("https://pdsb.elearningontario.ca/d2l/le/content/" + ID + "/Home");

			for (Object object : page.getByXPath("//li[@class='d2l-le-TreeAccordionItem d2l-le-TreeAccordionItem-Root']")) {
				DomElement de = (DomElement) object;
				if (de.getId().contains("D2L_LE_Content_TreeBrowser_D2L.LE.Content.ContentObject.ModuleCO-")) {
					String[] split = de.asText().split("\\n");
					List<String> ob = new ArrayList<>();
					
					for (int i = 1; i < split.length; i++) {
						ob.add(split[i]);
					}
					
					list.add(new ContentObject(split[0], ob));
				}
			}
			webClient.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public void logData(String text) {
		try {
			PrintWriter writer = new PrintWriter("log-d2l.txt", "UTF-8");
			writer.println(text);
			writer.close();
			System.out.println("Log updated!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
