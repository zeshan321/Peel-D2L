package com.zeshanaslam.d2lhook;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;

import Exceptions.InvaildCourseException;
import Objects.ContentObject;
import Objects.CourseObject;
import Objects.LockerObject;
import Objects.NotificationObject;

public class D2LHook {

	String username;
	String password;
	String URL = "https://pdsb.elearningontario.ca";
	
	boolean loginSuc = true;

	WebClient webClient = new WebClient(BrowserVersion.FIREFOX_38);
	CookieManager cookieManager;
	HtmlPage page = null;


	public D2LHook(String username, String password) {
		this.username = username;
		this.password = password;

		// Disable logging
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 

		// Disable CSS
		webClient.getOptions().setCssEnabled(false);
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
			if (page.asXml().contains("*  Please try again.")) {
				loginSuc = false;
				return;
			}
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
				courses.add(new CourseObject(de.getAttribute("href").replace("/d2l/lp/ouHome/home.d2l?ou=", ""), de.getAttribute("title").replace("Enter ", "")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return courses;
	}

	public List<ContentObject> getCourseContent(String ID) throws InvaildCourseException {
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
			throw new InvaildCourseException("Invaild Course ID");
		}
		return list;
	}

	public List<LockerObject> getLocker(boolean previewLink) {
		List<LockerObject> list = new ArrayList<>();
		try {
			WebClient webClient = new WebClient(BrowserVersion.FIREFOX_38);
			webClient.setCookieManager(cookieManager);

			HtmlPage page = webClient.getPage("https://pdsb.elearningontario.ca/d2l/lms/locker/locker.d2l?ou=8340");

			for (Object object : page.getByXPath("//a")) {
				DomElement de = (DomElement) object;
				if (de.asXml().toString().contains("/d2l/common/viewFile.d2lfile/Database/")) {
					if (!previewLink) {
						list.add(new LockerObject(de.getAttribute("title").replace("Open ", ""),  "https://pdsb.elearningontario.ca" + de.getAttribute("href").replace("&display=1", "")));
					} else {
						list.add(new LockerObject(de.getAttribute("title").replace("Open ", ""),  "https://pdsb.elearningontario.ca" + de.getAttribute("href")));

					}
				}
			}

			webClient.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<NotificationObject> getNotifications() {
		List<NotificationObject> list = new ArrayList<>();
		try {
			WebClient webClient = new WebClient(BrowserVersion.FIREFOX_38);
			webClient.setCookieManager(cookieManager);

			UnexpectedPage page = webClient.getPage("https://pdsb.elearningontario.ca/d2l/MiniBar/6594660/ActivityFeed/GetAlerts?Category=1&_d2l_prc%24headingLevel=2&_d2l_prc%24scope=&_d2l_prc%24hasActiveForm=false&isXhr=true&requestId=2");
			NotificationFormater notificationFormater = new NotificationFormater(page.getWebResponse().getContentAsString());
			
			webClient.close();
			
			for (Object object: notificationFormater.getNotifications()) {
				DomElement de = (DomElement) object;
				String[] split = de.asText().split("\\n");
				
				list.add(new NotificationObject(split[0].trim(), split[1].trim(), split[2].trim()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	public void debugData(File file) {
		try {
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			writer.println(page.asXml());
			writer.close();
			System.out.println("Log updated!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean loginStatus() {
		return loginSuc;
	}
}
