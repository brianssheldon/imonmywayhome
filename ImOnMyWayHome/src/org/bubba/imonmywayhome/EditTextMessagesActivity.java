package org.bubba.imonmywayhome;

import android.os.Bundle;
import android.widget.EditText;

public class EditTextMessagesActivity  extends AbstractActivity
{
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edittextmessages);
		
        populateScreen();
    }

	private void populateScreen()
	{
		MessagesBO msgsBO = readMsgsBO();
        int count = 1;
        for (String msg : msgsBO.getMsgs())
		{
        	((EditText) findViewById(getIdFromString(count))).setText(msg.trim());
        	count += 1;
		}
	}
	
	int getIdFromString(int count)
	{
        String fieldId = "editText" + count;
        int id = getResources().getIdentifier (fieldId, "id", getPackageName());
        return id;
	}
	
	void saveMsgsFile()
	{
		MessagesBO msgsBO = new MessagesBO();
		
		for (int i = 1; i < 9; i++)
		{
			msgsBO.getMsgs().add( ((EditText) findViewById(getIdFromString(i))).getText().toString().trim());	
		}
		
		msgsBO.setSelectedMsgNbr(selectedMsgNbr);
				
		saveMsgsBO(msgsBO);
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) 
	{
		saveMsgsFile();
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onPause()
	{
		saveMsgsFile();
		super.onPause();
	}
}