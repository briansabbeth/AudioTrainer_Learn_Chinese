package com.MeadowEast.audiotest;

import com.MeadowEast.R;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;


/**@author Jonathan Kosar
 * This Activity inflates when you select it from onOptionsItemSelected(MenuItem). 
 * This fulfills the Internet dictionary lookup for Hanzi inside a WebView.
 * It grabs the Hanzi through global variable passkey and attaches to the end of the search web address.
*/
public class DisplayDict extends Activity
{
	private static final String TAG = "DisplayDict";
	private WebView webView;
	private String urlstring;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_dict);
		
		//openAlert();
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		String pref2 = sharedPreferences.getString( "language_key", "");
		Log.d(TAG, "DisplayDict item is  " + MainActivity.globalitem );
		if (pref2.equals("EN"))
		{
			
			if (MainActivity.globalitem == 2)
			{
				urlstring = "http://www.spanishdict.com/translate/" + MainActivity.hanzi.get(MainActivity.passkey);
			}
			else if (MainActivity.globalitem == 0)
			{
				urlstring = "http://i.word.com/idictionary/" + MainActivity.hanzi.get(MainActivity.passkey);
			}
			else
			{
				urlstring = "http://www.mdbg.net/chindict/chindict.php?page=worddict&wdrst=1&wdqb="+ MainActivity.hanzi.get(MainActivity.passkey);
			}
			
		}
		else if (pref2.equals("CH")	)
		{
			if (MainActivity.globalitem == 2)
			{
				urlstring = "http://translation.babylon.com/chinese%20(s)/to-spanish/" + MainActivity.hanzi.get(MainActivity.passkey);
			}
			else if (MainActivity.globalitem == 1)
			{
				urlstring = "http://www.mdbg.net/chindict/chindict.php?page=worddict&wdrst=1&wdqb=" + MainActivity.hanzi.get(MainActivity.passkey);
			}
			else
			{
				urlstring = "http://www.mdbg.net/chindict/chindict.php?page=worddict&wdrst=1&wdqb="+ MainActivity.hanzi.get(MainActivity.passkey);
			}
		}
		else
		{
			if (MainActivity.globalitem == 1)
			{
				urlstring = "http://translate.google.com/#zh-CN/zh-CN/" + MainActivity.hanzi.get(MainActivity.passkey);
			}
			else 
			{
				urlstring = "http://www.spanishdict.com/translate/" + MainActivity.hanzi.get(MainActivity.passkey);
			}
			
		}
		webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setSupportZoom(true);  
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setDisplayZoomControls(false);
		//webViewwv.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
		//webView.addView(iv);
		webView.getSettings().setJavaScriptEnabled(true);
		
		webView.loadUrl(urlstring);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_dict, menu);
		return true;
	}
	
	private void openAlert() {
		 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DisplayDict.this);
	     
		 alertDialogBuilder.setTitle(this.getTitle()+ "Choice");
		 alertDialogBuilder.setMessage("Are you sure?");
		 // set positive button: Yes message
		 alertDialogBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// go to a new activity of the app
					Intent positveActivity = new Intent(getApplicationContext(),
                           DisplayDict.class);
		            startActivity(positveActivity);	
				}
			  });
		 // set negative button: No message
		 alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// cancel the alert box and put a Toast to the user
					dialog.cancel();
					Toast.makeText(getApplicationContext(), "You chose a negative answer", 
							Toast.LENGTH_LONG).show();
				}
			});
		 // set neutral button: Exit the app message
		 alertDialogBuilder.setNeutralButton("Exit the app",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					// exit the app and go to the HOME
					DisplayDict.this.finish();
				}
			});
		 
		 AlertDialog alertDialog = alertDialogBuilder.create();
		 // show alert
		 alertDialog.show();
	}

	
}
