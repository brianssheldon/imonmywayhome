package org.bubba.imonmywayhome;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class EditTextMessagesActivity  extends Activity
{
	public static final String FILE_NAME = "imonmywayhomemessagesfile.txt";
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edittextmessages);
        
        String theFile = readFile();
        
        StringTokenizer st = new StringTokenizer(theFile, ",");
        
        if(st.countTokens() > 7)
        {
        	((EditText) findViewById(R.id.editText1)).setText(st.nextToken().trim());
        	((EditText) findViewById(R.id.editText2)).setText(st.nextToken().trim());
        	((EditText) findViewById(R.id.editText3)).setText(st.nextToken().trim());
        	((EditText) findViewById(R.id.editText4)).setText(st.nextToken().trim());
        	((EditText) findViewById(R.id.editText5)).setText(st.nextToken().trim());
        	((EditText) findViewById(R.id.editText6)).setText(st.nextToken().trim());
        	((EditText) findViewById(R.id.editText7)).setText(st.nextToken().trim());
        	((EditText) findViewById(R.id.editText8)).setText(st.nextToken().trim());
        }
    }
	
	String readFile()
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
	
	private void saveFile()
	{
		StringBuffer sb = new StringBuffer();

    	sb.append(((EditText) findViewById(R.id.editText1)).getText() + " , ");
    	sb.append(((EditText) findViewById(R.id.editText2)).getText() + " , ");
    	sb.append(((EditText) findViewById(R.id.editText3)).getText() + " , ");
    	sb.append(((EditText) findViewById(R.id.editText4)).getText() + " , ");
    	sb.append(((EditText) findViewById(R.id.editText5)).getText() + " , ");
    	sb.append(((EditText) findViewById(R.id.editText6)).getText() + " , ");
    	sb.append(((EditText) findViewById(R.id.editText7)).getText() + " , ");
    	sb.append(((EditText) findViewById(R.id.editText8)).getText() + " , ");
				
		saveFile(sb.toString());
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

	@Override
	protected void onSaveInstanceState(Bundle outState) 
	{
		saveFile();
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onPause()
	{
		saveFile();
		super.onPause();
	}
}