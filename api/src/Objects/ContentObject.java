package Objects;

import java.util.List;

public class ContentObject {

	public String name;
	public List<String> subContent;
	
	public ContentObject(String name, List<String> subContent) {
		this.name = name;
		this.subContent = subContent;
	}
}
