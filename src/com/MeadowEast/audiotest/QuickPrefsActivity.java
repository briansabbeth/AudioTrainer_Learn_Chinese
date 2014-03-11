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
import android.widget.Toast;

public class QuickPrefsActivity extends PreferenceActivity implements
OnSharedPreferenceChangeListener{
	CheckBoxPreference isReg;
	static final String TAG = "CAT";
    @Override
    public void onCreate(Bundle savedInstanceState) {    	
        super.onCreate(savedInstanceState);        
        addPreferencesFromResource(R.xml.preferences); 
        
        PreferenceManager.setDefaultValues(this,R.xml.preferences, false);
        CheckBoxPreference  pref = (CheckBoxPreference) findPreference("night_mode");        
       
/*        pref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
        	 
        	public boolean onPreferenceClick(Preference preference, Object newValue) {

        	    Toast.makeText(getBaseContext(), "Some text", Toast.LENGTH_SHORT).show();
        	    boolean blnIsReg = Boolean.getBoolean(newValue.toString());
        	    
        	    if (blnIsReg == true)
        	    {
        	    	Toast.makeText(getBaseContext(), "true", Toast.LENGTH_SHORT).show();
        	    }
        	    else
        	    {
        	    	Toast.makeText(getBaseContext(), "false", Toast.LENGTH_SHORT).show();
        	    	}
        	    
        	    
        	    
        	    return true; 
        	}

			public boolean onPreferenceClick(Preference preference) {
				// TODO Auto-generated method stub
				return false;
			}	     
        	});*/



			
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
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
    
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
	
    	SharedPreferences s = getSharedPreferences("MY_PREFS", 0);

        // Create a editor to edit the preferences:
        SharedPreferences.Editor editor = s.edit();

        // Let's do something a preference value changes
        if (key.equals("night_mode")) 
        {
            // Create a reference to the checkbox (in this case):
            CheckBoxPreference mHints = (CheckBoxPreference)getPreferenceScreen().findPreference("english mode");
            //Lets change the summary so the user knows what will happen using a one line IF statement:
         //mHints.setSummary(mHints.isChecked() ? "Hints will popup." : "No hints will popup.");
            // Lets store the new preference:
            /*SharedPreferences sharedPreferences = PreferenceManager
            		                .getDefaultSharedPreferences(this);*/
            		        boolean checkBoxValue = s.getBoolean("night_mode", false);
            		        
            		       if (checkBoxValue) {
            		            //checkBox.setChecked(true);
            		    	   
            		    	   
            		    	   
            		    	   Log.d(TAG, "true onshared" + checkBoxValue);
            		        } 
            		       else
            		        {
            		        	Log.d(TAG, "false onshared" + checkBoxValue);
            			            //checkBox.setChecked(false);
            			        }
            
         editor.putBoolean("night_mode", mHints.isChecked());
         
         
        
        /**
         * You could perform several else if statements, or probably better use a switch block.
         */

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