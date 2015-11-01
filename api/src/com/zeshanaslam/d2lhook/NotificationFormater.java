package com.zeshanaslam.d2lhook;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlPage;


public class NotificationFormater {

	String notificationText;

	public NotificationFormater(String notificationText) {
		this.notificationText = notificationText;

		this.notificationText = this.notificationText.split("\"Payload\"")[1]
				.split("\"Html\"")[1]
						.split(":\"")[1]
								.split("\",\"OnInit\"")[0]
										.replace("\\r", "")
										.replace("\\t", "")
										.replace("\\n", "")
										.replace("\\", "")
										.replace("&quot;", "")
										.replace("&amp;", "");
	}

	public List<?> getNotifications() {
		List<?> deList = null;
		try {
			StringWebResponse response = new StringWebResponse(this.notificationText, new URL(new D2LHook(null, null).URL));
			WebClient client = new WebClient();
			HtmlPage page = HTMLParser.parseHtml(response, client.getCurrentWindow());
			client.close();
			
			deList = page.getByXPath("//div[@class='d2l-datalist-item-content']");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return deList;
	}
}
