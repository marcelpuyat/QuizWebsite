package customObjects;

import java.io.Serializable;

public class StringPair implements Serializable {

	private String first;
	private String second;
	
	public StringPair(String first, String second) {
		this.first = first;
		this.second = second;
	}
	
	/**
	 * @return the first
	 */
	public String getFirst() {
		return first;
	}

	/**
	 * @return the second
	 */
	public String getSecond() {
		return second;
	}
}
