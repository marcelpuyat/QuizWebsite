package customObjects;

import java.io.Serializable;

public class StringBooleanPair implements Serializable {

	private String str;
	private boolean bool;
	
	public StringBooleanPair(String str, boolean bool) {
		this.str = str;
		this.bool = bool;
	}

	/**
	 * @return the str
	 */
	public String getStr() {
		return str;
	}
	
	/**
	 * @return the bool
	 */
	public boolean getBool() {
		return this.bool;
	}
	
}
