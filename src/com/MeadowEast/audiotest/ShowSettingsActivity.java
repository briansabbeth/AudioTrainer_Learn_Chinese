package com.MeadowEast.audiotest;




import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import com.MeadowEast.R;

public class ShowSettingsActivity extends Activity {
	static final String TAG = "CAT";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_settings_layout);

		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		StringBuilder builder = new StringBuilder();

		builder.append("\n" + sharedPrefs.getBoolean("night_mode_key", true));
		builder.append("\n" + sharedPrefs.getString("time_usage_key", "-1"));
		builder.append("\n" + sharedPrefs.getString("language_key", "-1"));

		//builder.append("\n" + sharedPrefs.getBoolean("time_history_key", true));

		TextView settingsTextView = (TextView) findViewById(R.id.settings_text_view);
		settingsTextView.setText(builder.toString());
		
		
		
		

	}

}
