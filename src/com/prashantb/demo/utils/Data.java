package com.prashantb.demo.utils;

/**
 * @author ashraf
 *
 */
public class Data {
	
	private String Name;
	private String Number;
	private String Email;

	public Data(String Name, String Number, String Email) {
		super();
		this.Name = Name;
		this.Number = Number;
		this.Email = Email;
	}

	/**
	 * @return the Name
	 */
	public String getName() {
		return Name;
	}
	/**
	 * @param Name the Name to set
	 */
	public void setName(String Name) {
		this.Name = Name;
	}
	/**
	 * @return the lastName
	 */
	public String getNumber() {
		return Number;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setNumber(String Number) {
		this.Number = Number;
	}
	/**
	 * @return the Email
	 */
	public String getEmail() {
		return Email;
	}
	/**
	 * @param Email the Email to set
	 */
	public void setEmail(String Email) {
		this.Email = Email;
	}
	
	@Override
	public String toString() {
		return "Student [Name=" + Name
				+ ", Number=" + Number + ", Email=" + Email +"]";
	}
}
