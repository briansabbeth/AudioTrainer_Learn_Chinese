package com.MeadowEast.audiotest;


import java.util.prefs.Preferences;
import com.MeadowEast.R;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class QuickPrefsActivity extends PreferenceActivity implements
OnSharedPreferenceChangeListener{
	CheckBoxPreference isReg;
	static final String TAG = "QuickPrefsActivity";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {    	
        super.onCreate(savedInstanceState);        
        addPreferencesFromResource(R.xml.preferences); 
        
        PreferenceManager.setDefaultValues(this,R.xml.preferences, false);
        
        CheckBoxPreference  night_checkbox = (CheckBoxPreference) findPreference("night_mode_key");        
       			
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	//menu.add(Menu.NONE, 0, 0, "Show current settings");
    	return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    		case 0:
    			startActivity(new Intent(this, ShowSettingsActivity.class));
    			return true;
    	}
    	return false;
    }
    @Override
    public void onStart(){
        super.onStart();
        SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(QuickPrefsActivity.this);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
    
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
	
    	SharedPreferences s = getSharedPreferences("MY_PREFS", 0);

        // Create a editor to edit the preferences:
        SharedPreferences.Editor editor = s.edit();

        // Let's do something a preference value changes
        if (key.equals("night_mode_key")) 
        {
            // Create a reference to the checkbox (in this case):
            CheckBoxPreference mHints = (CheckBoxPreference)getPreferenceScreen().findPreference("night_mode_key");
            
            //Lets change the summary so the user knows what will happen using a one line IF statement:
         //mHints.setSummary(mHints.isChecked() ? "Hints will popup." : "No hints will popup.");
            // Lets store the new preference:
            //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
          	boolean checkBoxValue = s.getBoolean("night_mode_key", false);
            		        
       if (checkBoxValue) 
	         {

    	   
	              Log.d(TAG, "true onshared" + checkBoxValue);
	            
	            	//setContentView(R.layout.activity_main);
	            	View v = findViewById(R.id.LinearLayout1);
	            	//View root = v.getRootView();
	            	
	            	v.setBackgroundColor(0xffffffff);
	              /*((TextView) findViewById(R.id.hanziTextView)).setTextColor(0xff000000);
	              ((TextView) findViewById(R.id.timerTextView)).setTextColor(0xff000000);
	              ((TextView) findViewById(R.id.instructionTextView)).setTextColor(0xff000000);*/
	            		   		
	         }
       else
             {
    	   /* findViewById(R.id.LinearLayout1).setBackgroundColor(0xff000000);
            	((TextView) findViewById(R.id.hanziTextView)).setTextColor(0xff444444);
                ((TextView) findViewById(R.id.timerTextView)).setTextColor(0xff444444);
                ((TextView) findViewById(R.id.instructionTextView)).setTextColor(0xff444444);*/
                Log.d(TAG, "false onshared" + checkBoxValue);
            }    		    	   
            		    	      		        	
         //checkBox.setChecked(false);
        
         editor.putBoolean("night_mode_key", mHints.isChecked());

        // Save the results:
        editor.commit();
   }

        if (key.equals("language_key")) {

       sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
       //ListPreference listPref = (ListPreference) sharedPreferences; 
       String entryvalue = sharedPreferences.getString( "language_key", "");
       Log.d(TAG, "Entryvalue " + entryvalue);
      
      
	    if (entryvalue.equals("EN"))
	    {
	    	Log.d(TAG, "EN " + entryvalue);
	    	Toast.makeText(getBaseContext(), "English Selected", Toast.LENGTH_SHORT).show();
	    }
	    else if (entryvalue.equals("CH"))
	    {
	    	Log.d(TAG, "CH " + entryvalue);
	    	Toast.makeText(getBaseContext(), "Chinese Selected", Toast.LENGTH_SHORT).show();
	    }
	    else
	    {
	    	Toast.makeText(getBaseContext(), "You may not go where no1 has gone before, pick again!", Toast.LENGTH_SHORT).show();
	    }
	    
       
     
	   }
	    
    }
 }