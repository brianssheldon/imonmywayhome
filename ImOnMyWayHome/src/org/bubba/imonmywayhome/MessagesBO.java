package org.bubba.imonmywayhome;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MessagesBO implements Serializable
{
	private static final long serialVersionUID = 666L;
	List<String> msgs;
	int selectedMsgNbr;
	String phoneNumber1;
	String phoneNumber2;
	String phoneNumber3;
	boolean phoneNumberChecked1;
	boolean phoneNumberChecked2;
	boolean phoneNumberChecked3;
	
	public MessagesBO()
	{
		msgs = new ArrayList<String>();
		phoneNumber1 = "";
		phoneNumber2 = "";
		phoneNumber3 = "";
	}

	public List<String> getMsgs()
	{
		return msgs;
	}

	public void setMsgs(List<String> msgs)
	{
		this.msgs = msgs;
	}

	public int getSelectedMsgNbr()
	{
		return selectedMsgNbr;
	}

	public void setSelectedMsgNbr(int selectedMsgNbr)
	{
		this.selectedMsgNbr = selectedMsgNbr;
	}

	public String getPhoneNumber1()
	{
		return phoneNumber1;
	}

	public void setPhoneNumber1(String phoneNumber1)
	{
		this.phoneNumber1 = phoneNumber1;
	}

	public String getPhoneNumber2()
	{
		return phoneNumber2;
	}

	public void setPhoneNumber2(String phoneNumber2)
	{
		this.phoneNumber2 = phoneNumber2;
	}

	public String getPhoneNumber3()
	{
		return phoneNumber3;
	}

	public void setPhoneNumber3(String phoneNumber3)
	{
		this.phoneNumber3 = phoneNumber3;
	}

	public boolean isPhoneNumberChecked1()
	{
		return phoneNumberChecked1;
	}

	public void setPhoneNumberChecked1(boolean phoneNumberChecked1)
	{
		this.phoneNumberChecked1 = phoneNumberChecked1;
	}

	public boolean isPhoneNumberChecked2()
	{
		return phoneNumberChecked2;
	}

	public void setPhoneNumberChecked2(boolean phoneNumberChecked2)
	{
		this.phoneNumberChecked2 = phoneNumberChecked2;
	}

	public boolean isPhoneNumberChecked3()
	{
		return phoneNumberChecked3;
	}

	public void setPhoneNumberChecked3(boolean phoneNumberChecked3)
	{
		this.phoneNumberChecked3 = phoneNumberChecked3;
	}
}