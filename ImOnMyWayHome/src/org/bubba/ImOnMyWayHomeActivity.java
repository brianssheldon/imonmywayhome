package org.bubba;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ImOnMyWayHomeActivity extends Activity
{
	public static final String FILE_NAME = "imonmywayhomefile.txt";
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button theSendButton = (Button) findViewById(R.id.sendButton);
		theSendButton.setOnClickListener(sendButtonClickListener);
		
		populateScreen();
    }

	void populateScreen()
	{
		String record = readFile();
		
		StringTokenizer st = new StringTokenizer(record, ",");

		if(st.countTokens() < 1) st = new StringTokenizer(" , ", ",");
		if(st.countTokens() < 2) st = new StringTokenizer(record, ",");
		
		EditText textText = (EditText) findViewById(R.id.editTextMessage);
		textText.setText(st.nextToken().trim());
		
		EditText phoneNumberView = (EditText) findViewById(R.id.editTextPhoneNumber);
		phoneNumberView.setText(st.nextToken().trim());
	}
    
    private final Button.OnClickListener sendButtonClickListener = new Button.OnClickListener() 
    {
		@Override
		public void onClick(View arg0)
		{
			EditText textText = (EditText) findViewById(R.id.editTextMessage);
			String textmsg = textText.getText().toString();
			
			EditText phoneNumberView = (EditText) findViewById(R.id.editTextPhoneNumber);
			String phoneNumber = phoneNumberView.getText().toString();
			
			if(textmsg == null || "".equals(textmsg) 
					|| phoneNumber == null || "".equals(phoneNumber))
			{
				return;
			}
			
			SmsManager smsMgr = SmsManager.getDefault();
			ArrayList<String> msgs = smsMgr.divideMessage(textmsg);
			for (String string : msgs)
			{
				smsMgr.sendTextMessage(phoneNumber, null, string, null, null);
			}
			
			saveFile(textmsg + "," + phoneNumber);
		}
    };

	private void saveFile(String msgAndNumber)
	{
		try
		{
			FileOutputStream fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
			fos.write(msgAndNumber.getBytes());
			fos.close();
		}
		catch(Exception e)
		{
			Log.getStackTraceString(e);
		}
	}
	
	private String readFile()
	{
		StringBuffer record = new StringBuffer();
		
		try
		{
	    	int ch = 0;
			FileInputStream fis = openFileInput(FILE_NAME);
			while( (ch = fis.read()) != -1)
	        {
	        	record.append((char)ch);
	        }
	    	fis.close();
		}
		catch (FileNotFoundException e)
		{
			saveFile(",");
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		if(record.length() ==0)
		{
			record.append(",");
		}
		
    	return record.toString();
	}
}