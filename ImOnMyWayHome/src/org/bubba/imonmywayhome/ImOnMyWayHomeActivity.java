package org.bubba.imonmywayhome;

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
		String record = readFile() + " , , , ";
		
		StringTokenizer st = new StringTokenizer(record, ",");
		
		EditText textText = (EditText) findViewById(R.id.editTextMessage);
		textText.setText(st.nextToken().trim());

		EditText phoneNumberView = (EditText) findViewById(R.id.editTextPhoneNumber1);
		phoneNumberView.setText(st.nextToken().trim());
		
		phoneNumberView = (EditText) findViewById(R.id.editTextPhoneNumber2);
		phoneNumberView.setText(st.nextToken().trim());
		
		phoneNumberView = (EditText) findViewById(R.id.editTextPhoneNumber3);
		phoneNumberView.setText(st.nextToken().trim());
	}
    
    private final Button.OnClickListener sendButtonClickListener = new Button.OnClickListener() 
    {
		@Override
		public void onClick(View arg0)
		{
			EditText textText = (EditText) findViewById(R.id.editTextMessage);
			String textmsg = textText.getText().toString();

			EditText phoneNumberView1 = (EditText) findViewById(R.id.editTextPhoneNumber1);
			String phoneNumber1 = phoneNumberView1.getText().toString();
			
			EditText phoneNumberView2 = (EditText) findViewById(R.id.editTextPhoneNumber2);
			String phoneNumber2 = phoneNumberView2.getText().toString();
			
			EditText phoneNumberView3 = (EditText) findViewById(R.id.editTextPhoneNumber3);
			String phoneNumber3 = phoneNumberView3.getText().toString();

			sendTextMessage(textmsg, phoneNumber1);
			sendTextMessage(textmsg, phoneNumber2);
			sendTextMessage(textmsg, phoneNumber3);
			
			saveFile(textmsg + "," + phoneNumber1 + "," + phoneNumber2 + "," + phoneNumber3);
		}
    };
    
    private void sendTextMessage(String message, String phoneNumber)
    {
    	if(message == null || "".equals(message) 
    			|| phoneNumber == null || "".equals(phoneNumber))
		{
    		return;
		}
    	
    	SmsManager smsMgr = SmsManager.getDefault();
		ArrayList<String> msgs = smsMgr.divideMessage(message);
		
		for (String string : msgs)
		{
			smsMgr.sendTextMessage(phoneNumber, null, string, null, null);
		}
    }
    
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