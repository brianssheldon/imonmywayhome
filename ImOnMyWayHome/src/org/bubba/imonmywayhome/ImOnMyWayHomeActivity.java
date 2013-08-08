package org.bubba.imonmywayhome;

import java.util.ArrayList;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ImOnMyWayHomeActivity extends AbstractActivity
{
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
		MessagesBO msgsBO = readMsgsBO();
		EditText phoneNumberView = (EditText) findViewById(R.id.editTextPhoneNumber1);
		phoneNumberView.setText(msgsBO.getPhoneNumber1().trim());

		phoneNumberView = (EditText) findViewById(R.id.editTextPhoneNumber2);
		phoneNumberView.setText(msgsBO.getPhoneNumber2().trim());

		phoneNumberView = (EditText) findViewById(R.id.editTextPhoneNumber3);
		phoneNumberView.setText(msgsBO.getPhoneNumber3().trim());
		
		((CheckBox)findViewById(R.id.checkBox1)).setChecked(msgsBO.isPhoneNumberChecked1());
		((CheckBox)findViewById(R.id.checkBox2)).setChecked(msgsBO.isPhoneNumberChecked2());
		((CheckBox)findViewById(R.id.checkBox3)).setChecked(msgsBO.isPhoneNumberChecked3());
		
		populateMessagesSpinner(msgsBO);
	}

	private void populateMessagesSpinner(MessagesBO msgsBO)
	{
		selectedMsgNbr = msgsBO.getSelectedMsgNbr();
	
	    ArrayAdapter dataAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, msgsBO.getMsgs());
	    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    
	    Spinner spinner = (Spinner) findViewById(R.id.spinner1);
	    spinner.setAdapter(dataAdapter);
	    spinner.setSelection(selectedMsgNbr);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id)
			{
				selectedMsgNbr = position;
				updateSpinnerSelection();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView)
			{
				// your code here
			}

		});
	}
	
	public void editTextMessage(View view)
	{
        Intent myIntentx = new Intent(view.getContext(), EditTextMessagesActivity.class);
//        myIntentx.putExtra("itemSelected", getSelectedSpinnerItemPosition());
        startActivityForResult(myIntentx, 100);
	}
	
	public void onCheckboxClicked(View view)
	{
	    boolean checked = ((CheckBox) view).isChecked();
	    boolean showMsg = false;
	    
	    switch(view.getId()) 
	    {
	        case R.id.checkBox1:
	            if (checked)
	            {
	            	if(isEditTextBlank(R.id.editTextPhoneNumber1))
	        		{
	        			showMsg = true;
	        		}
	            }
	            break;
	
	        case R.id.checkBox2:
	            if (checked)
	            {
	            	if(isEditTextBlank(R.id.editTextPhoneNumber2))
	        		{
	        			showMsg = true;
	        		}
	            }
	            break;
	
	        case R.id.checkBox3:
	            if (checked)
	            {
	            	if(isEditTextBlank(R.id.editTextPhoneNumber3))
	        		{
	        			showMsg = true;
	        		}
	            }
	            break;
	       }
	    	
	    	if(showMsg)
	    	{
	    		Toast.makeText(getBaseContext(), "Please enter a phone number\nbefore checking this box",
    					Toast.LENGTH_SHORT).show();
    			((CheckBox)view).setChecked(false);
	    	}
	}
	
	private void updateSpinnerSelection()
	{
		MessagesBO msgsBO = readMsgsBO();
		msgsBO.setSelectedMsgNbr(selectedMsgNbr);
		saveMsgsBO(msgsBO);
	}
	
	private boolean isEditTextBlank(int id)
	{
    	EditText phoneNumberView = (EditText) findViewById(id);
		String phoneNumber = phoneNumberView.getText().toString();
		
		if(phoneNumber == null ||"".equals(phoneNumber))
		{
			return true;
		}
		return false;
	}

	private final Button.OnClickListener sendButtonClickListener = new Button.OnClickListener()
	{
		@Override
		public void onClick(View arg0)
		{

		    Spinner spinner = (Spinner) findViewById(R.id.spinner1);
			String textmsg = (String) spinner.getSelectedItem();
			
			if(textmsg == null || textmsg.length() < 1)
			{
				Toast.makeText(getBaseContext(), "message is blank\nno message sent", 
                        Toast.LENGTH_SHORT).show();
			}
			else if(isANumberChecked())
			{
				sendClicked = true;
			}
			else
			{
				Toast.makeText(getBaseContext(), "no phone numbers selected\nno message sent", 
                        Toast.LENGTH_SHORT).show();
			}
			
			saveFile();
		}

		private boolean isANumberChecked()
		{
			if(isEditTextBlank(R.id.editTextPhoneNumber1))
			{
				((CheckBox)findViewById(R.id.checkBox1)).setChecked(false);
			}

			if(isEditTextBlank(R.id.editTextPhoneNumber2))
			{
				((CheckBox)findViewById(R.id.checkBox2)).setChecked(false);
			}

			if(isEditTextBlank(R.id.editTextPhoneNumber3))
			{
				((CheckBox)findViewById(R.id.checkBox3)).setChecked(false);
			}
			
			return ((CheckBox)findViewById(R.id.checkBox1)).isChecked()
				|| ((CheckBox)findViewById(R.id.checkBox2)).isChecked()
				|| ((CheckBox)findViewById(R.id.checkBox3)).isChecked();
		}
	};
	
	private final Button.OnClickListener saveButtonClickListener = new Button.OnClickListener()
	{
		@Override
		public void onClick(View arg0)
		{
//			updateSpinnerSelection();
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
		MessagesBO msgsBO = readMsgsBO(); 

		EditText phoneNumberView1 = (EditText) findViewById(R.id.editTextPhoneNumber1);
		msgsBO.setPhoneNumber1(phoneNumberView1.getText().toString());

		EditText phoneNumberView2 = (EditText) findViewById(R.id.editTextPhoneNumber2);
		msgsBO.setPhoneNumber2(phoneNumberView2.getText().toString());

		EditText phoneNumberView3 = (EditText) findViewById(R.id.editTextPhoneNumber3);
		msgsBO.setPhoneNumber3(phoneNumberView3.getText().toString());

		msgsBO.setPhoneNumberChecked1(((CheckBox)findViewById(R.id.checkBox1)).isChecked());
		msgsBO.setPhoneNumberChecked2(((CheckBox)findViewById(R.id.checkBox2)).isChecked());
		msgsBO.setPhoneNumberChecked3(((CheckBox)findViewById(R.id.checkBox3)).isChecked());
		
		if(sendClicked)
		{
		    Spinner spinner = (Spinner) findViewById(R.id.spinner1);
			String textmsg = (String) spinner.getSelectedItem();
			if(msgsBO.isPhoneNumberChecked1())	sendTextMessage(textmsg, msgsBO.getPhoneNumber1());
			if(msgsBO.isPhoneNumberChecked2())	sendTextMessage(textmsg, msgsBO.getPhoneNumber2());
			if(msgsBO.isPhoneNumberChecked3())	sendTextMessage(textmsg, msgsBO.getPhoneNumber3());
			sendClicked = false;
		}
		
		saveMsgsBO(msgsBO);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == 100)
		{	// back from EditTextMessagesActivity
			populateMessagesSpinner(readMsgsBO());
		}
	}
}