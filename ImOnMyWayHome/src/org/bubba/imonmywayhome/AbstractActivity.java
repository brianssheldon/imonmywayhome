package org.bubba.imonmywayhome;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class AbstractActivity extends Activity
{
	public static final String MSGS_FILE_NAME = "imonmywayhomemessagesfile.txt";
	public static int selectedMsgNbr = 0;

	public AbstractActivity()
	{
		super();
	}

	MessagesBO readMsgsBO()
	{
		MessagesBO msgsBO;
		
		try
		{
			FileInputStream fis = openFileInput(MSGS_FILE_NAME);
			ObjectInputStream in = new ObjectInputStream(fis);
			msgsBO =  (MessagesBO)in.readObject();
			in.close();
			fis.close();
		} 
		catch (Exception e)
		{
			try
			{
				msgsBO = getNewMsgsBO();
				saveMsgsBO(msgsBO);
			} 
			catch (Exception e2)
			{
				e2.printStackTrace();
				msgsBO = getNewMsgsBO();
				Toast.makeText(getBaseContext(), "error creating msgs file\n" + e2.toString(), Toast.LENGTH_LONG).show();
			}
		}
		
		return msgsBO;
	}
	
	void saveMsgsBO(MessagesBO msgsBO)
	{
		try
		{
			FileOutputStream fos = openFileOutput(MSGS_FILE_NAME, Context.MODE_PRIVATE);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			out.writeObject(msgsBO);
			out.close();
			fos.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Toast.makeText(getBaseContext(), "error saving msgs file\n" + e.toString(), Toast.LENGTH_LONG).show();
		}
	}

	private MessagesBO getNewMsgsBO()
	{
		MessagesBO msgsBO;
		msgsBO = new MessagesBO();
		msgsBO.getMsgs().add("I'm on my way home.");
		msgsBO.getMsgs().add("I'll be home after I stop by the store.");
		msgsBO.getMsgs().add("");
		msgsBO.getMsgs().add("42");
		msgsBO.getMsgs().add("");
		msgsBO.getMsgs().add("");
		msgsBO.getMsgs().add("What is the last thing that goes through a bugs mind when it hits your windshield?");
		msgsBO.getMsgs().add("It's butt");
		msgsBO.setSelectedMsgNbr(0);
		msgsBO.setPhoneNumber1("");
		msgsBO.setPhoneNumber2("");
		msgsBO.setPhoneNumber3("");
		return msgsBO;
	}
}