package org.bubba.imonmywayhome;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class ImOnMyWayHomeActivity extends Activity
{
	public static final String FILE_NAME = "imonmywayhomefile.txt";
	private boolean sendClicked = false;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button theSendButton = (Button) findViewById(R.id.sendButton);
		theSendButton.setOnClickListener(sendButtonClickListener);

		Button theSaveButton = (Button) findViewById(R.id.saveButton);
		theSaveButton.setOnClickListener(saveButtonClickListener);

		populateScreen();
	}

	void populateScreen()
	{
		String record = readFile() + " , , , , , , ";

		StringTokenizer st = new StringTokenizer(record, ",");
		
		while (st.hasMoreElements())
		{
			System.err.println("*-*" + st.nextToken() + "*-*");
		}
		
		
		st = new StringTokenizer(record, ",");

		EditText textText = (EditText) findViewById(R.id.editTextMessage);
		textText.setText(st.nextToken().trim());

		EditText phoneNumberView = (EditText) findViewById(R.id.editTextPhoneNumber1);
		phoneNumberView.setText(st.nextToken().trim());

		phoneNumberView = (EditText) findViewById(R.id.editTextPhoneNumber2);
		phoneNumberView.setText(st.nextToken().trim());

		phoneNumberView = (EditText) findViewById(R.id.editTextPhoneNumber3);
		phoneNumberView.setText(st.nextToken().trim());
		
		if("Y".equals(st.nextToken().trim())) ((CheckBox)findViewById(R.id.checkBox1)).setChecked(true);
		if("Y".equals(st.nextToken().trim())) ((CheckBox)findViewById(R.id.checkBox2)).setChecked(true);
		if("Y".equals(st.nextToken().trim())) ((CheckBox)findViewById(R.id.checkBox3)).setChecked(true);
	}

	private final Button.OnClickListener sendButtonClickListener = new Button.OnClickListener()
	{
		@Override
		public void onClick(View arg0)
		{
			sendClicked = true;
			saveFile();
		}
	};

	private String cbToString(CheckBox cb)
	{
		if(cb.isChecked()) return "Y";
		return "N";
	}
	
	private final Button.OnClickListener saveButtonClickListener = new Button.OnClickListener()
	{
		@Override
		public void onClick(View arg0)
		{
			saveFile();
		}
	};

	private void sendTextMessage(String message, String phoneNumber)
	{
		if (message == null || "".equals(message) || phoneNumber == null
				|| "".equals(phoneNumber))
		{
			return;
		}

		SmsManager smsMgr = SmsManager.getDefault();
		ArrayList<String> msgs = smsMgr.divideMessage(message);

		for (String string : msgs)
		{
			PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent("sent"), 0);
	        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent("delivered"), 0);
	        
			createSentReceiver();
			createDeliveredReceiver();
			
			smsMgr.sendTextMessage(phoneNumber, null, string, sentPI, deliveredPI);
		}
	}

	void createDeliveredReceiver()
	{
		registerReceiver(new BroadcastReceiver(){
		    @Override
		    public void onReceive(Context arg0, Intent arg1) {
		        switch (getResultCode())
		        {
		            case Activity.RESULT_OK:
		                Toast.makeText(getBaseContext(), "message delivered", 
		                        Toast.LENGTH_SHORT).show();
		                break;
		            case Activity.RESULT_CANCELED:
		                Toast.makeText(getBaseContext(), "message not delivered", 
		                        Toast.LENGTH_SHORT).show();
		                break;                        
		        }
		    }
		}, new IntentFilter("delivered"));
	}

	void createSentReceiver()
	{
		registerReceiver(new BroadcastReceiver()
		{
			@Override
			public void onReceive(Context arg0, Intent arg1)
			{
				switch (getResultCode())
				{
				case Activity.RESULT_OK:
					Toast.makeText(getBaseContext(), "message sent", Toast.LENGTH_SHORT).show();
					break;
					
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(getBaseContext(), "Generic failure", Toast.LENGTH_SHORT).show();
					break;
					
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Toast.makeText(getBaseContext(), "No service", Toast.LENGTH_SHORT).show();
					break;
					
				case SmsManager.RESULT_ERROR_NULL_PDU:
					Toast.makeText(getBaseContext(), "Null PDU", Toast.LENGTH_SHORT).show();
					break;
					
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					Toast.makeText(getBaseContext(), "Radio off", Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter("sent"));
	}

	private void saveFile()
	{
		EditText textText = (EditText) findViewById(R.id.editTextMessage);
		String textmsg = textText.getText().toString();

		EditText phoneNumberView1 = (EditText) findViewById(R.id.editTextPhoneNumber1);
		String phoneNumber1 = phoneNumberView1.getText().toString();

		EditText phoneNumberView2 = (EditText) findViewById(R.id.editTextPhoneNumber2);
		String phoneNumber2 = phoneNumberView2.getText().toString();

		EditText phoneNumberView3 = (EditText) findViewById(R.id.editTextPhoneNumber3);
		String phoneNumber3 = phoneNumberView3.getText().toString();

		CheckBox cb1 = (CheckBox)findViewById(R.id.checkBox1);
		CheckBox cb2 = (CheckBox)findViewById(R.id.checkBox2);
		CheckBox cb3 = (CheckBox)findViewById(R.id.checkBox3);
		
		if(sendClicked)
		{
			if(cb1.isChecked())	sendTextMessage(textmsg, phoneNumber1);
			if(cb2.isChecked())	sendTextMessage(textmsg, phoneNumber2);
			if(cb3.isChecked())	sendTextMessage(textmsg, phoneNumber3);
			sendClicked = false;
		}
		
		saveFile(textmsg + " ," + phoneNumber1 + " ," + phoneNumber2 + " ," + phoneNumber3
			+ " ," + cbToString(cb1)+ " ," + cbToString(cb2)+ " ," + cbToString(cb3) + " ");
	}
	
	private void saveFile(String msgAndNumber)
	{
		try
		{
			FileOutputStream fos = openFileOutput(FILE_NAME,
					Context.MODE_PRIVATE);
			fos.write(msgAndNumber.getBytes());
			fos.close();
		}
		catch (Exception e)
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
			while ((ch = fis.read()) != -1)
			{
				record.append((char) ch);
			}
			fis.close();
		}
		catch (FileNotFoundException e)
		{
			record.append(" , , , , , , , , , , , , , , , ");
			saveFile(record.toString());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		if (record.length() == 0)
		{
			record.append(",");
		}

		return record.toString();
	}
}