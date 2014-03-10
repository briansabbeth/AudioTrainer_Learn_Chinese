package com.MeadowEast.audiotest;
import android.view.GestureDetector;
import com.MeadowEast.R;
import com.MeadowEast.UpdateService.Alarm;
import com.MeadowEast.UpdateService.CheckUpdate;
import com.MeadowEast.UpdateService.DownloadService;
import com.MeadowEast.UpdateService.UnZip;

import com.MeadowEast.UpdateService.UnZipUtil;
import com.MeadowEast.dbOpenHelper.TingshuoDatasource;
import com.MeadowEast.dbOpenHelper.TingshuoHistDatasource;
import com.MeadowEast.model.HistoryModel;
import com.MeadowEast.model.Model;
import android.view.GestureDetector.OnGestureListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;
/**
 *@author bjsabbeth
 *@author Jonathan Kosar
 */
public class MainActivity extends Activity  implements OnClickListener,
		OnLongClickListener, OnGesturePerformedListener,  OnGestureListener  {

 
    private int turnCount;
	public static ProgressDialog mProgressDialog;
	private GestureLibrary gLibrary;
	private MediaPlayer mp;
	public static String[] cliplist;
	private File sample;
	private static File mainDir;
	private static File englishDir;
	public static File clipDir;
	private File zipFILE;
	private static File file;
	private Random rnd;
	private Handler clockHandler;
	private Runnable updateTimeTask;
	private boolean clockRunning;
	private boolean clockWasRunning;
	private Long elapsedMillis;
	private Long start;
	public static Map<String, String> hanzi;
	private static Map<String, String> instructions;
	private String key;
	static final String TAG = "MainActivity";
	public static String passkey;
	ShareActionProvider mShareActionProvider ;
	GestureDetector detector;
	public int index_getClip;
	public LinkedList<String> HistLList;
	private Handler mHandler;
	private int delayTime;
	
	public static final int progress_bar_type = 0;
	
	 	private String[] drawerListViewItems;
	 	public String[] testStringList;  
	 	
	    private DrawerLayout drawerLayout;
	    private ListView drawerListView;
	    private ActionBarDrawerToggle actionBarDrawerToggle;
	    
	    public static DownloadManager mgr;
		long lastDownload = -1L;

		// String Url="http://www.meadoweast.com/capstone/clips.zip";
		String Urltxt = "http://www.meadoweast.com/capstone/clipinfo.txt";
		String unzipLocation = Environment.getExternalStorageDirectory() + "/clips/";
		String StorezipFileLocation = Environment.getExternalStorageDirectory()	+ "/Clips.zip";
		String StoretxtFileLocation = Environment.getExternalStorageDirectory()	+ "/Android/data/com.MeadowEast.audiotest/files/clipinfo.txt";
		String DirectoryName = Environment.getExternalStorageDirectory() + "/Android/data/com.MeadowEast.audiotest/files/clips/";
		String DirectoryName1 = Environment.getExternalStorageDirectory() + "/Android/data/com.MeadowEast.audiotest/files/";
	
		public List<String> histlist = new ArrayList<String>();
//	
//	/********DATABASE VARIABLE*******************************/
	private static final String LOGTAG = "TINGSHOU_DB ";
	TingshuoDatasource datasource;
	private ArrayList<String> availableClips;
	private ArrayList<String> probabilityArray;
	String currentClip;
	String lastClip;
	boolean sameTurn;
	//a char representing  the previous command issued by the user
	//R = repeat, S = swipe(play next), B = back;
	//In the case of a repeat, we will know if we are in the same turn by 
	//checking what the last turn is.
	char lastTurn; 
	/********DATABASE VARIABLE*******************************/
 //	

	
	
	
	/********DATABASE VARIABLE*******************************/
////history table
TingshuoHistDatasource hist_datasource;

//////////////////////////////////////////////////////////
	


	
	
	private final Runnable mUpdateUI = new Runnable() 
	{
	    public void run()
	    {
	    	boolean alarmUp = (PendingIntent.getBroadcast(getBaseContext(), 0, 
	    	        new Intent("com.my.package.MY_UNIQUE_ACTION"), 
	    	        PendingIntent.FLAG_NO_CREATE) != null);

	    	if (alarmUp)
	    	{
	    		Log.d(TAG, "Alarm is already active");
	    	}
	    	
	    	
	    	if (!isNetworkAvailable())
	    	{
	    		mHandler.postDelayed(mUpdateUI, 21600000);
	    		Toast.makeText(MainActivity.this, "No internet for Daily update check, try again in little!", Toast.LENGTH_SHORT).show();
		    	
	    	}
	    	else if (!alarmUp)
		    	{
	    		
	    		Alarm alarm = new Alarm();
				alarm.SetAlarm(getBaseContext());
				Log.d(TAG, "Alarm was not active but now is!");
				//mHandler.postDelayed(mUpdateUI, 86400000);
				
				//Toast.makeText(MainActivity.this, "Daily update check!", Toast.LENGTH_LONG).show();
		    	}
	    	else
	    	{
	    		Log.d(TAG, "Alarm is already active and internet");
	    	}
	        
	    }
	    
	};
	
	
	
	
	/*private final Runnable mUpdateUi = new Runnable(){
	    public void run(){
	        check();
	    }

	};

	private void start(){
	    new Thread(
	        new Runnable(){
	            public void run(){
	            	Log.d(TAG, "inside start");
	                Looper.prepare();
	                mHandler = new Handler();
	                check();
	                Looper.loop();
	            }
	        }
	    ).run();
	}


	private void check(){
	    if (isNetworkAvailable()== true){
	        try {
	            new checkupdate().execute();
	            delayTime = 86400000;
	            Toast.makeText(MainActivity.this, "Daily update check!", Toast.LENGTH_SHORT).show();
	        } 
	        catch (IOException e) {
	            e.printStackTrace();
	            delayTime = 21600000;
	        }
	    }else{
	        delayTime = 21600000;
	        Toast.makeText(MainActivity.this, "No internet for Daily update check, try again in little!", Toast.LENGTH_SHORT).show();
	    }
	    reCheck();
	}

	private void reCheck(){
	    mHandler.postDelayed(mUpdateUi, delayTime);
	}*/
	
	
public static void readClipInfo() {

		hanzi = new HashMap<String, String>();
		instructions = new HashMap<String, String>();

		//SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

		//boolean pref = sharedPreferences.getBoolean("english mode", false);	
		boolean pref = false;
		
		Log.d(TAG, "beforeenglish" + pref);

		if (pref == true)
		{

		file = new File(englishDir, "clipinfo.txt");
		Log.d(TAG, "beforeenglish");
		try {
		FileReader fr = new FileReader(file);
		BufferedReader in = new BufferedReader(fr);
		String line;
		while ((line = in.readLine()) != null) {
		String fixedline = new String(line.getBytes());
		String[] fields = fixedline.split("\\t");
		if (fields.length == 3) {
		hanzi.put(fields[0], fields[1]);
		Log.e(TAG, "BJS HANZI FIELD " + fields[1] + " " + fields[0] );

		instructions.put(fields[0], fields[2]);
		} else {
		Log.d(TAG, "Bad line: " + fields.length + " elements");
		Log.d(TAG, fixedline);
		}
		}
		in.close();
		} catch (Exception e) {
		Log.d(TAG, "Problem reading clipinfo");
		}
		Log.d(TAG, "afterenglish");

		}


		else
		{

		file = new File(mainDir, "clipinfo.txt");
		Log.d(TAG, "before");

		try {
		FileReader fr = new FileReader(file);
		BufferedReader in = new BufferedReader(fr);
		String line;
		while ((line = in.readLine()) != null) {
		String fixedline = new String(line.getBytes(), "utf-8");
		String[] fields = fixedline.split("\\t");
		if (fields.length == 3) {
		hanzi.put(fields[0], fields[1]);
		instructions.put(fields[0], fields[2]);
		} else {
		Log.d(TAG, "Bad line: " + fields.length + " elements");
		Log.d(TAG, fixedline);
		}
		}
		in.close();
		}
		catch (Exception e) {
		Log.d(TAG, "Problem reading clipinfo");
		}

		Log.d(TAG, "after");

		}
		
	
	
}
	private String getInstruction(String key) {
		Log.i(LOGTAG, "BJS getInstructions(key), key =  "+  key);
		String instructionCodes = instructions.get(key);
		int n = instructionCodes.length();
		if (n == 0) {
			return "No instruction codes for " + key;
		}
		int index = rnd.nextInt(n);
		Log.i(LOGTAG, "BJS getInstructions().index "+  key);
		switch (instructionCodes.charAt(index)) {
		case 'C':
			return "continue the conversation";
		case 'A':
			return "answer the question";
		case 'R':
			return "repeat";
		case 'P':
			return "paraphrase";
		case 'Q':
			return "ask questions";
		case 'V':
			return "create variations";
		default:
			return "Bad instruction code " + instructionCodes.charAt(index)
					+ " for " + key;
		}
	}

	private void toggleClock() {
		if (clockRunning) {
			elapsedMillis += System.currentTimeMillis() - start;
			setHanzi("");
		} else
			start = System.currentTimeMillis();
		clockRunning = !clockRunning;
		clockHandler.removeCallbacks(updateTimeTask);
		if (clockRunning)
			clockHandler.postDelayed(updateTimeTask, 200);
	}

	private void showTime(Long totalMillis) {
		int seconds = (int) (totalMillis / 1000);
		int minutes = seconds / 60;
		seconds = seconds % 60;
		TextView t = (TextView) findViewById(R.id.timerTextView);
		if (seconds < 10)
			t.setText("" + minutes + ":0" + seconds);
		else
			t.setText("" + minutes + ":" + seconds);
	}

	private void createUpdateTimeTask() {
		updateTimeTask = new Runnable() {
			public void run() {
				Long totalMillis = elapsedMillis + System.currentTimeMillis()
						- start;
				showTime(totalMillis);
				clockHandler.postDelayed(this, 1000);
			}
		};
	}

	private void setHanzi(String s) {
		TextView t = (TextView) findViewById(R.id.hanziTextView);
		t.setText(s);
	}
	
	private String getHanzi(String s)
		{
		return hanzi.get(s);
		}


	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//Log.d(TAG, "testing only");
		// File filesDir = getFilesDir(); // Use on virtual device
		    
		
	
		
		
//////////////////////////////////Custom Gesutre Overlay//////////////////////////////////        
        
        detector=new GestureDetector(getBaseContext(), this);
		 
		gLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if (!gLibrary.load()) {
            finish();
       }  
        
        GestureOverlayView gOverlay = 
                (GestureOverlayView) findViewById(R.id.gestureOverlayView1);
            gOverlay.addOnGesturePerformedListener(this); 
            
           // gOverlay.setGestureStrokeType(GestureOverlayView.GESTURE_STROKE_TYPE_MULTIPLE);
            gOverlay.setGestureColor(Color.TRANSPARENT);
            gOverlay.setUncertainGestureColor(Color.TRANSPARENT);
            
  //////////////////////////////////Custom Gesutre Overlay////////////////////////////////// 
            
            
            
            //mHandler.post(mUpdateUI);
    		//start();
    		mgr = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
    		
    		registerReceiver(DownloadService.onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    		
    		 /**********************DATABASE CODE onCreate()******************************************/
    	     //////History Table
    	          Log.i(LOGTAG, "BJS ABOUT TO OPEN HISTDATASOURCE");
    	     hist_datasource = new TingshuoHistDatasource(this);
    	          hist_datasource.open();
    	          Log.i(LOGTAG, "BJS OPENED HISTDATASOURCE");
    	          outputHistModelList();


    	    
    	    
    	     //////Main Table
    	    
    	     lastTurn = '0';
    	     turnCount = 0;
    	          //sameTurn = false;
    	     availableClips = new ArrayList<String>();
    	          probabilityArray = new ArrayList<String>();
    	          Log.i(LOGTAG, "ABOUT TO OPEN DATASOURCE");
    	            
    	datasource = new TingshuoDatasource(this);
    	datasource.open();
    	List<Model> modelList = datasource.findAll();





    	   /************************END DATABASE CODE*****************************************/


		
	
		
	  
        

		File sdCard = Environment.getExternalStorageDirectory();
		File f = new File(sdCard.getAbsolutePath() + "/Android/data/com.MeadowEast.audiotest/files/");
		f.mkdirs();
		
		initializationcheck();
		
		mainDir = new File(sdCard.getAbsolutePath() + "/Android/data/com.MeadowEast.audiotest/files/");
		
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        
        boolean pref = sharedPreferences.getBoolean("english mode", false);
		
        
        englishDir = new File(sdCard.getAbsolutePath() + "/Android/data/com.MeadowEast.audiotest/files/english/");
        
        Log.i(LOGTAG,"Startup boolean value" + pref); 
        
        try {
		if (pref == true)
		{
			clipDir = new File(englishDir, "clips");
			cliplist = clipDir.list();
			//Toast.makeText(getApplicationContext(),"HELLO FROM PREF! ", Toast.LENGTH_SHORT).show();
			Log.i(LOGTAG,"Startup boolean value inside true" + pref); 
		}
		else
		{
		clipDir = new File(mainDir, "clips");
		cliplist = clipDir.list();
		Log.i(LOGTAG,"Startup boolean value inside false" + pref); 
	
		}
		
		readClipInfo();
		rnd = new Random();
		 /**********************DATABASE CODE******************************************/
		Log.i(LOGTAG, " BJS CLIPLISTLENGTH "+ cliplist.length + " initializingclips()");
		initializeAvailableClips();
		        modelList = datasource.findAll();
		        Log.i(LOGTAG,"BJS Outputting the modellist");
		        outputModelList(modelList);	
		        updateSlideHistList();
		  /*********************END DATABASE CODE***************************************************************/
        }
        catch (Exception e) 
		{
        	
		}
        
        
		
		/*findViewById(R.id.playButton).setOnClickListener(this);
		findViewById(R.id.repeatButton).setOnClickListener(this);
		findViewById(R.id.hanziButton).setOnClickListener(this);*/
		findViewById(R.id.timerTextView).setOnClickListener(this);
		findViewById(R.id.hanziTextView).setOnLongClickListener(this);
		
		
		clockHandler = new Handler();
		start = System.currentTimeMillis();
		elapsedMillis = 0L;
		clockRunning = false;
		createUpdateTimeTask();
	/*	findViewById(R.id.pauseButton).setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						toggleClock();
					}
				});*/
		if (savedInstanceState != null) {
			elapsedMillis = savedInstanceState.getLong("elapsedMillis");
			Log.d(TAG, "elapsedMillis restored to" + elapsedMillis);
			key = savedInstanceState.getString("key");
			String sampleName = savedInstanceState.getString("sample");
			if (sampleName.length() > 0)
				sample = new File(clipDir, sampleName);
			if (savedInstanceState.getBoolean("running"))
				toggleClock();
			else
				showTime(elapsedMillis);
			Log.d(TAG, "About to restore instruction");
			String instruction = savedInstanceState.getString("instruction");
			if (instruction.length() > 0) {
				Log.d(TAG, "Restoring instruction value of " + instruction);
				TextView t = (TextView) findViewById(R.id.instructionTextView);
				t.setText(instruction);
			}

		}
		
		
		
		
		//Slide Menu Stuff
		
		
		 // get list items from strings.xml
       drawerListViewItems = getResources().getStringArray(R.array.items);
       
       
       // get ListView defined in activity_main.xml
       drawerListView = (ListView) findViewById(R.id.left_drawer);

       // Set the adapter for the list view
       
       
     /*  if (testStringList == null)
       {	
       drawerListView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_listview_item, histlist));
       }
       else
       {
       drawerListView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_listview_item, testStringList));
       }*/
       
      drawerListView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_listview_item, histlist));
       // 2. App Icon
       drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

       // 2.1 create ActionBarDrawerToggle
       actionBarDrawerToggle = new ActionBarDrawerToggle(
               this,                  // host Activity 
               drawerLayout,         // DrawerLayout object 
               R.drawable.ic_launcher,  // nav drawer icon to replace 'Up' caret 
               R.string.drawer_open,  // "open drawer" description 
               R.string.drawer_close  // "close drawer" description 
               );

       // 2.2 Set actionBarDrawerToggle as the DrawerListener
       drawerLayout.setDrawerListener(actionBarDrawerToggle);

       // 2.3 enable and show "up" arrow
       getActionBar().setDisplayHomeAsUpEnabled(true);

       // just styling option
       drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

       drawerListView.setOnItemClickListener(new DrawerItemClickListener());
       
       drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

	/*Log.i(LOGTAG,"Before Alarm"); 
        	
		
	  	Intent i = new Intent(this, CheckUpdate.class);
	  	PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
	  	AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
	  	am.cancel(pi); // cancel any existing alarms
	  	am.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
	  	    SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_FIFTEEN_MINUTES,
	  	    AlarmManager.INTERVAL_FIFTEEN_MINUTES, pi);
	  	
	  	Alarm alarm = new Alarm();
	  	alarm.SetAlarm(getBaseContext());
		
	  	Log.i(LOGTAG,"After ALarm"); */
		
		
	}
	
	private class DrawerItemClickListener implements ListView.OnItemClickListener 
	{
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id)
        {
            Toast.makeText(MainActivity.this, ((TextView)view).getText(), Toast.LENGTH_LONG).show();
            drawerLayout.closeDrawer(drawerListView);
 
        }
    }
	
	public void onPause()
	{
		
		Log.d(TAG, "!!!! onPause is being run");
		clockWasRunning = clockRunning;
		if (clockRunning)
			toggleClock();
		datasource.close();
		unregisterReceiver(DownloadService.onComplete);
		//unregisterReceiver(BroadcastReceiver Alarm);
		super.onPause();
	}

	public void onSaveInstanceState(Bundle outState) 
	{
		super.onSaveInstanceState(outState);
		String sampleName = "";
		if (sample != null)
			sampleName = sample.getName();
		outState.putString("sample", sampleName);
		// onPause has stopped the clock if it was running, so we just save
		// elapsedMillis
		outState.putLong("elapsedMillis", elapsedMillis);
		TextView t = (TextView) findViewById(R.id.instructionTextView);
		outState.putString("instruction", t.getText().toString());
		outState.putString("key", key);
		outState.putBoolean("running", clockWasRunning);
	}

	public void reset() 
	{
		TextView t;
		if (clockRunning)
			toggleClock();
		start = 0L;
		elapsedMillis = 0L;
		sample = null;
		t = (TextView) findViewById(R.id.timerTextView);
		t.setText("0:00");
		setHanzi("");
		t = (TextView) findViewById(R.id.instructionTextView);
		t.setText("");
	}

	public boolean onLongClick(View v)
	{
		repeatHandler(key);
		switch (v.getId()) 
		{
		case R.id.hanziTextView:
			Toast.makeText(this, "Clip: " + key, Toast.LENGTH_LONG).show();
			Log.d(TAG, "Long clicked");
			break;
		}
		return true;
	}

	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
	
		case R.id.timerTextView:
			new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle(R.string.reset)
					.setMessage(R.string.reallyReset)
					.setPositiveButton(R.string.yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									MainActivity.this.reset();
								}
							}).setNegativeButton(R.string.no, null).show();
			break;
		}
	}


	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.d(TAG, "llkj");
			new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle(R.string.quit)
					.setMessage(R.string.reallyQuit)
					.setPositiveButton(R.string.yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									MainActivity.this.finish();
								}
							}).setNegativeButton(R.string.no, null).show();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
		
	}
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        // actionBarDrawerToggle.syncState();
        
       /* Log.i(TAG,"Before Alarm"); 
        	
		
	  
	  	
	  	Alarm alarm = new Alarm();
	  	alarm.SetAlarm(getBaseContext());
		
	  	Log.i(TAG,"After ALarm");*/
    }
 
	 @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

	@Override
public boolean onOptionsItemSelected(MenuItem item) 
	{
		
		if (actionBarDrawerToggle.onOptionsItemSelected(item)) 
		{
            return true;
        }
		// Take appropriate action for each action item click
		switch (item.getItemId()) 
		{
		case R.id.action_settings:
			
			startActivity(new Intent(this, QuickPrefsActivity.class));
			// setContentView(R.layout.settings_activity);
			return true;
		case R.id.action_check_updates:

			if (isNetworkAvailable()== true)
	    	{
			try 
			{
				new CheckUpdate().execute();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	}
			else
			{
				Toast.makeText(MainActivity.this, "No internet for update check, try again in little!", Toast.LENGTH_SHORT).show();
			}

			return true;
		case R.id.action_trans:
			Intent x = new
			Intent(getApplicationContext(),DisplayDict.class);
			startActivity(x);
			return true;
		case R.id.share:
			getDefaultShareIntent();
	
			
		default:
			return super.onOptionsItemSelected(item);
		}

	}
	
////////////////////////CIRCLE IS REPEAT/////////////////////////
////    ////    ////    ////    ////    ////    ////    ////
	///    ////    ////    ////    ////    ////    ////
////   ////   ////    ////    ////    ////    ////    ////
public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        ArrayList<Prediction> predictions = gLibrary.recognize(gesture);
        //&& predictions.get(0).score > 1.0
        if (predictions.size() > 0 && predictions.get(0).score > 1.0 ) {
            
        	String action = predictions.get(0).name;
        	String hswipe = "hswipe";
        	String U = "u";
        	String circle = "circle";
        	String trig = "trig";
        	String lefthalf = "lefthalf";
        	String halfcir = "halfcir";
        	String threecir = "3cir";
        	String twolines = "twolines";
        	String bigcir = "bigcir";
        	String horovalcir= "horovalcir";
        	String pearcir = "pearcir";
        	String smallcir = "smallcir";
        	String squcir = "squcir";
        	String weirdcir = "weirdcir";
        		
        //if (action.equals( circle )|| action.equals( halfcir )|| action.equals( threecir ) || action.equals( weirdcir )|| action.equals( squcir )|| action.equals( smallcir )|| action.equals( pearcir )|| action.equals( horovalcir )|| action.equals( bigcir ))
   
 
            if ( action.equals( halfcir )|| action.equals( threecir ) )
            {
            
            	Log.i(TAG, " REPEAT "+  key);
            	
              Toast.makeText(this, "Repeat", Toast.LENGTH_SHORT).show();	
              
              repeatHandler(key);
              createAndInsertHistModel();/////inserts to the history
              
              repeatHandler(key);
              lastTurn = 'R';
			if (!clockRunning)
				toggleClock();
			if (sample != null) 
			{
				setHanzi("");
				if (mp != null) 
				{
					mp.stop();
					mp.release();
				}
				mp = new MediaPlayer();
				mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
				try 
				{
					mp.setDataSource(getApplicationContext(),
							Uri.fromFile(sample));
					mp.prepare();
					mp.start();
				} 
				catch (Exception e) 
				{
					Log.d(TAG, "Couldn't get mp3 file");
				}

			}
			
            }
            
            if (action.equals( twolines ))
            {
            	Log.i(LOGTAG, "BJS twolines "+  key);
            	//Toast.makeText(this, "Pause", Toast.LENGTH_SHORT).show();
            	toggleClock();
            }
        }
    }



private Intent getDefaultShareIntent()
{
	
    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
    intent.setType("audio/mp3");
  
    intent.putExtra(Intent.EXTRA_SUBJECT, "SUBJECT");
    intent.putExtra(Intent.EXTRA_TEXT,hanzi.get(key));
    
    //startActivityForResult(Intent.createChooser(intent, "Send mail"), EMAIL_REQUEST);
   // File root = Environment.getExternalStorageDirectory();
    String name = clipDir.getName();
    Log.d(TAG, name);
        
    //File file = new File(clipDir, key);
    if (!sample.exists() || !sample.canRead())
    {
    	Toast.makeText(this, "Attachment Error", Toast.LENGTH_SHORT).show();
    	finish();
       //return;
    }
   Uri uri = Uri.parse("file://" + sample);
   intent.putExtra(Intent.EXTRA_STREAM, uri);
   startActivity(intent);
   return intent;
	
}



private boolean isNetworkAvailable() 
	{
	    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}


private boolean isEmptyDirectory(File file)
{
	if(file.isDirectory())
	{
		 
		if(file.list().length>0)
		{
 
			Log.i(LOGTAG, "Directory is not empty!");
			return false;
 
		}
		else
		{
 
			Log.i(LOGTAG, "Directory is empty!");
			return true;
		}
 
	}
	else
	{
 
		Log.i(LOGTAG, "This is not a directory");
		return false;
	}
	
	
}


void initializationcheck()
	{
	 
	String englishclipsPath = Environment.getExternalStorageDirectory() + "/Android/data/com.MeadowEast.audiotest/files/english/";
	String englishzipPath = Environment.getExternalStorageDirectory() + "/Android/data/com.MeadowEast.audiotest/files/english.zip";
	
	
	String clipsPath = Environment.getExternalStorageDirectory() + "/Android/data/com.MeadowEast.audiotest/files/clips/";
	String clipszipPath = Environment.getExternalStorageDirectory() + "/Android/data/com.MeadowEast.audiotest/files/clips.zip";
	
	File englishclipDir = new File(englishclipsPath, "clips");
	File clipDir = new File(clipsPath);
	
	File englishfilezip = new File(englishzipPath);
	
	
	File clipszip = new File(clipszipPath);
	
	
	
	try 
	{
	
		if (clipszip.exists() && englishfilezip.exists() )
		{	
			new CheckUpdate().execute();
		}
		
	
			if (!clipszip.exists() || !englishfilezip.exists() )
			{
				Log.d(TAG, "Inside of initializationcheck for if zip exist");
				try 
				{
					new CheckUpdate().execute();
				} 
				catch (IOException e1) 
				{
					
					e1.printStackTrace();
				}
			}
			
			else if (isEmptyDirectory(clipDir) || isEmptyDirectory(englishclipDir))
			{
				
				Log.d(TAG, "Inside of initializationcheck for isEmptyDirectory ");	
				
				if(isEmptyDirectory(clipDir))
				{
					
				
				Log.d(TAG, "zipPath" + clipszipPath);
		
				UnZip.startzip(clipszipPath, clipsPath, MainActivity.this);
				}
				
				if(isEmptyDirectory(englishclipDir))
				{
				Log.d(TAG, "englishzipPath " + englishzipPath);
				
				UnZip.startzip(englishzipPath, clipsPath, MainActivity.this);
				}
				
			}
			
			if (( clipszip.exists() && englishfilezip.exists() ) && ( isEmptyDirectory(clipDir) || isEmptyDirectory(englishclipDir) )  )
			{
				Log.d(TAG, "Inside of initializationcheck for the last if statement");		
				
				Log.d(TAG, "zipPath" + clipszipPath);
		
				UnZip.startzip(clipszipPath, clipsPath, MainActivity.this);
				
				Log.d(TAG, "englishzipPath " + englishzipPath);
				
				UnZip.startzip(englishzipPath, clipsPath, MainActivity.this);
				
			}
			
		/*	else
			{
				Log.i(LOGTAG, "Nothing to do on initializationcheck");
			}*/
		
	} 
	catch (Exception e) 
	{
		Log.i(LOGTAG, "Inside of initializationcheck catach exception", e);
		
		e.printStackTrace();
	}
	
	}




/*@Override
protected void onDestroy()
{
	unregisterReceiver(DownloadService.onComplete);
	
    super.onDestroy();
}
*/


public boolean onDown(MotionEvent arg0) {
	Log.i(LOGTAG, "BJS on down ");
	//Toast.makeText(getApplicationContext(), "Single Tap Gesture", 100).show();
	return true;
}

private static final int SWIPE_THRESHOLD = 100;
private static final int SWIPE_VELOCITY_THRESHOLD = 100;

public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
	///Log.i(LOGTAG, "BJS onFling");

	boolean result = false;
    try {
        float diffY = e2.getY() - e1.getY();
        float diffX = e2.getX() - e1.getX();
        if (Math.abs(diffX) > Math.abs(diffY)) {
            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffX > 0) {
                    onSwipeRight();
                } else {
                    onSwipeLeft();
                }
            }
        } else {
            if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffY > 0) {
                    onSwipeBottom();
                } else {
                    onSwipeTop();
                }
            }
        }
    } catch (Exception exception) {
        exception.printStackTrace();
    }
    return result;
}


public void onSwipeRight() {
	Log.d(TAG, "right");
	
}
//////////////  bjsbjsbjsbjsbjs   swipe calls get instructions  bjsbjsbjsbjsbjs  \\\\\\\\\\\\\\\\\
//////////////  bjsbjsbjsbjsbjs   swipe calls get instructions  bjsbjsbjsbjsbjs  \\\\\\\\\\\\\\\\\\
//////////////  bjsbjsbjsbjsbjs   swipe calls get instructions  bjsbjsbjsbjsbjs  \\\\\\\\\\\\\\\\\\\

public void onSwipeLeft() {
	lastTurn = 'S';
	Toast.makeText(this, "Play", Toast.LENGTH_SHORT).show();
	Log.i(TAG, "BJS onSwipeLeft() " + lastTurn);
	
	
	try{

	//PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    
	SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
	boolean pref = sharedPreferences.getBoolean("english mode", false);
	Log.e(TAG, "pref boolean value is "  + pref);
	File sdCard = Environment.getExternalStorageDirectory();
	mainDir = new File(sdCard.getAbsolutePath() + "/Android/data/com.MeadowEast.audiotest/files/"); 
	englishDir = new File(sdCard.getAbsolutePath() + "/Android/data/com.MeadowEast.audiotest/files/english/");
	
	if (pref == true)
		{
			clipDir = new File(englishDir, "clips");
			cliplist = clipDir.list();
			Log.e(TAG, "inside True ON Play "  + pref);
			//Toast.makeText(getApplicationContext(),"inside True ON Play", Toast.LENGTH_SHORT).show();
		}
	else
		{
			clipDir = new File(mainDir, "clips");
			cliplist = clipDir.list();
			Log.e(TAG, "inside False ON Play "  + pref);
			//Toast.makeText(getApplicationContext(),"inside False ON Play ", Toast.LENGTH_SHORT).show();
			
		}
	
	 datasource = new TingshuoDatasource(this);
     datasource.open();
 	  

	readClipInfo();
	
	String test = Integer.toString(cliplist.length);
	
	/*initializeAvailableClips();
    modelList = datasource.findAll();
    Log.i(LOGTAG,"BJS Outputting the modellist"); 
    outputModelList(modelList);	
	String test = Integer.toString(cliplist.length);
	
	Log.e(TAG, test);
	//try {
*/		
	
	Log.e(TAG, test);

	//BJS CHANGES
	/////
	// key = sample.getName();
	// key = key.substring(0, key.length() - 4);
	String test_clip = getClip();
	Log.e(TAG, "BJS onSwipeLeft().testClip() " + test_clip);
	key = test_clip;

         
     databaseSwipeHandler();
     createAndInsertHistModel();
    
     updateSlideHistList();
        
      sample = new File(clipDir, cliplist[index_getClip]);
      Log.e(TAG, "BJS This is inedex_getclip inside of play " + index_getClip);
      Log.e(TAG, "BJS This is key inside of play " + key);
      Log.e(TAG, "BJS This is key inside of play " + sample);
         
        
      turnCount++;
		//Log.e(TAG, "BJS onSwipeLeft().turnCount = " + turnCount);
		//Log.i(TAG, "BJS onSwipeLeft().key - 4 " + key);
		
	TextView t = (TextView) findViewById(R.id.instructionTextView);
		t.setText(getInstruction(key));
		Log.e(TAG, "BJS TEXTVIEW " + t);
		
		
		
		
		if (!clockRunning)
			toggleClock();
		
		if (sample != null) {
			setHanzi("");
			if (mp != null) {
				mp.stop();
				mp.release();
			}
			mp = new MediaPlayer();
			mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
			try {
				mp.setDataSource(getApplicationContext(),
						Uri.fromFile(sample));
				mp.prepare();
				mp.start();
			} catch (Exception e) {
				Log.d(TAG, "Couldn't get mp3 file");
			}
		} 
	}
	catch (Exception e) 
	{
		initializationcheck();
	}
	
    Log.i(LOGTAG, "BJS ABOUT TO OPEN HISTDATASOURCE");
    hist_datasource = new TingshuoHistDatasource(this);
         hist_datasource.open();
         Log.i(LOGTAG, "BJS OPENED HISTDATASOURCE");
         outputHistModelList();
}

public void onSwipeTop() {
}

public void onSwipeBottom() {
	//Toast.makeText(this, "Pause", Toast.LENGTH_SHORT).show();
	toggleClock();
}
public void onLongPress(MotionEvent arg0) {
	// TODO Auto-generated method stub
	
}
@Override
public boolean dispatchTouchEvent(MotionEvent e)
{
    detector.onTouchEvent(e);

    return super.dispatchTouchEvent(e);
}

public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
		float arg3) {
	// TODO Auto-generated method stub
	return false;
}

public void onShowPress(MotionEvent arg0) {
	// TODO Auto-generated method stub
	Log.i(LOGTAG, "testsing4");
	//Toast.makeText(getApplicationContext(), "Show Press gesture", 100).show();
}

@Override
public boolean onTouchEvent(MotionEvent event) {
	Log.i(LOGTAG, "testsing3");
    return detector.onTouchEvent(event);
}

public boolean onSingleTapUp(MotionEvent e) {
	
	//Toast.makeText(this, "ShowHanziTap", Toast.LENGTH_SHORT).show();
	Log.e(TAG, "BJS tap up");
	passkey = key;
	
	Log.e(TAG, "This is key inside of Single Tap " + passkey);
	if (!clockRunning)
		toggleClock();
	if (sample != null)
		setHanzi(hanzi.get(key)); // Should add default value: error
									// message if no hanzi for key
   // Toast.makeText(getApplicationContext(), "Single Tap Gesture", 100).show();
    Log.i(LOGTAG, "testsing2");
    return true;
}

public boolean onSingleTapConfirmed(MotionEvent e) { 

	//Toast.makeText(getApplicationContext(), "Single Tap Gesture", 100).show();
	Log.i(LOGTAG, "testsing1");
    return true; 
}

/**
* Called by Android.
* When program is resumed, open the database.
*/
@Override
protected void onResume() {
// TODO Auto-generated method stub
super.onResume();
datasource.open();
registerReceiver(DownloadService.onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
}

public void updateSlideHistList()
{
	List<HistoryModel> histModelList = hist_datasource.findAll();
	
	histlist.clear();
	
	for (int i = 0; i < histModelList.size(); i++)
	{
	
	String tempconstr = histModelList.get(i).get_clip_id() + " " + histModelList.get(i).get_short_hanzi();

	histlist.add(new String (tempconstr) );
	
	Log.i(LOGTAG, "THIS what  has " + histlist);
    
	
	}
	
	drawerListView.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_listview_item, histlist));
}

/********************************
* DATABASE STUFF
*******************************/

/* setAvailableClips():
* 0)Clear availableClips and probability array.
* 1)Get a substring of each entry in the cliplist (clipString -.mp3)
* 2)Put each substring into the availableClips arrayList.
* 3)Look into the database, if the data is not there enter it, the probability = 1
* 3.5) If the data is there, find the probability,
* 4)Enter it into the probabilty array the appropriate number of times
* 5)??????Use the probabilityArray to randomly pick the next clip
*
* NOTE: This can be done on every update as well as every time
* we rotate ten (ten go out and ten come in).
*
*/



private void initializeAvailableClips()
{
    Log.i(LOGTAG, "BJS InINITIALIZE");

availableClips.clear();
probabilityArray.clear();


String tempKey;	
Model tempModel = new Model();


//get the data and create the probaility array with that
//clip number the appropriate amount of times.
for(int i = 0; i< cliplist.length; ++i)
{
tempKey = cliplist[i];
tempKey = tempKey.substring(0, tempKey.length() - 4);
availableClips.add(i, tempKey);
/*
* If the dats is already in the database:
* 1)get the probabilty of the data by adding the turn fields
* 2)add it to the probability
* If data is not in the database:
* 1)create and insert using Model class;
* 2)add the clip number one time to the probabilityArray.
*/
if (datasource.isDataInDatabase(tempKey))
{
tempModel = datasource.findModel(tempKey);
//if ((tempModel.getTurnZero() + tempModel.getTurnOne()+tempModel.getTurnTwo)>3())
///
///
for(int j = 0; j<tempModel.getProbability(); ++j)
{	
probabilityArray.add(tempKey);
Log.i(LOGTAG, "IN DBASE " + tempModel.getClipTxtNumber() + " PROB "+ tempModel.getProbability());	
}

}
else//put it in the databases add it to the probabilityArray as 1.
{
createAndInsertModel(tempKey);
probabilityArray.add(tempKey);

}

}

}

/*
* Make a model to be inserted into the database;
* Initially sets all turns(repeats) to zero
* and sets the probability to one.
*/
private Model createAndInsertModel(String clip_txt_number)
{
Model model = new Model(); //declare a new object only once
// and reuse objects below
model.setClipTxtNumber(clip_txt_number);
model.setProbability(1);
model.setTurnZero(0);
model.setTurnOne(0);
model.setTurnTwo(0);
model = datasource.createModel(model);
return model;
}










/*1)Set the boolean sameTurn to false;
*2)Set the turnNumber to 0;
*3)Get a randomindex from the array.
*4)Return the array index
*
*/
private String getClip()
{
 // sameTurn = false;
 
  rnd = new Random();
  int index = rnd.nextInt(probabilityArray.size());
  index_getClip = index;
  return probabilityArray.get(index);

}

/**
* Example:
* |Orig | |Shift | |Repeat | |Repeat | |Shift |
* [1][2][0] ->[0][1][2] ->[1][1][2] ->[2][1][2]->[0][2][1]
* update_data(String clip_id_number, int new_probability,
int turn_zero,int turn_one,int turn_two){
*/

private void repeatHandler(String key)
{
Log.i(LOGTAG, " BJS update_data ");
Log.i(LOGTAG, " BJS ProbArray.size == "+ probabilityArray.size() + "Last Turn = " + lastTurn);


    int turn0,turn1, turn2, tempProb;
    
Model tempModel = getModel(key);
turn0 = tempModel.getTurnZero();
turn1 = tempModel.getTurnOne();
turn2 = tempModel.getTurnTwo();	
    tempProb = tempModel.getProbability();
Log.i(LOGTAG, " BJS update_data.tempProb = " + tempModel.getProbability());	
  
   ///if it is the same turn as a previous repeat
   ///do not shift the turns.
   ///increment turn0;
   ///if the combined turns are greater than 3: double the probability;
   if (lastTurn == 'R' && tempProb <2)
   {
Log.i(LOGTAG, " BJS update_data. LastTurn == R = " + tempModel.getProbability());	

turn0 ++;
Log.i(LOGTAG, " BJS update_data.turn0 = " + turn0);	
/// If the combined turns = 4 or anything greater mod 4
/// then the probability should be doubled. *I am considering 1 repeat = sort of equal to a turn
if (((turn0+turn1+turn2)>3 ))
{
Log.i(LOGTAG, " BJS update_data: probabilityArray was size: " + probabilityArray.size());	
datasource.update_data(key, 2, turn0, turn1, turn2);
probabilityArray.add(key);	
Log.i(LOGTAG, " BJS update_data: probabilityArray is now size: " + probabilityArray.size());	
Log.i(LOGTAG, " BJS update_data(combined turns =4): "+ key+ " " + turn0 + " " + turn1+ " " + turn2 + " " +getModel(key).getProbability());	
}
//increment the data
datasource.update_data(key, tempProb,turn0, turn1, turn2);
Log.i(LOGTAG, " BJS update_data "+ key+ " " + turn0 + " " + turn1+ " " + turn2 + " " + tempProb);	

    }
   
   else if (lastTurn == 'R' && tempProb > 1)
    {
    if (((turn0+turn1+turn2)<4 ))
    {
    datasource.update_data(key, 1,turn0, turn1, turn2);	
}
    }
   ///
   ////Move to its own function.
   else if (lastTurn == 'S')
   {

   }
  
   //Log.i(LOGTAG, " BJS update_data "+ key+ " " + turn0 + " " + turn1+ " " + turn2 + " " + tempProb);

   lastTurn = 'R';
}

private void databaseSwipeHandler()
{
int turn0, turn1,turn2,tempProb;
Model tempModel = getModel(key);
turn0 = tempModel.getTurnZero();
turn1 = tempModel.getTurnOne();
turn2 = tempModel.getTurnTwo();
Log.i(LOGTAG, "BJS DBSWIPEHN() PRIOR TO UPDATE "+ key+ " " + turn0 + " " + turn1+ " " + turn2 + " ");	


datasource.update_data(key, 1, 0, turn0, turn1);

tempModel = getModel(key);
turn0 = tempModel.getTurnZero();
turn1 = tempModel.getTurnOne();
turn2 = tempModel.getTurnTwo();
datasource.update_data(key, 1, 0, turn0, turn1);


tempProb = tempModel.getProbability();

Log.i(LOGTAG, " BJS DBSWIPEHN() POST UPDATE "+ key+ " " + turn0 + " " + turn1+ " " + turn2 + " ");	
      
}

private Model getModel(String clip_txt_number)
{
Model model = datasource.findModel(clip_txt_number);
return model;
}



////Just to test consistancies
//
//private void outputAvailableClips()
//{
// for(int i = 0; i< cliplist.length; ++i)
// {
// Log.i(LOGTAG, " AVAILABLECLIP "+ availableClips.get(i));
// }
//}


/*
* LOGCAT out the modellist or the model. Just for testing
*/
public void outputModelList(List<Model> modelList)
{
 
for (int i = 0; i < modelList.size(); i++)
{
Log.i(LOGTAG, "BJS CLIPNUM " +modelList.get(i).getClipTxtNumber() + " PROB "+ modelList.get(i).getProbability());

}
}

public void outputModel(Model model){
    Log.i(LOGTAG,"COLUMN_ID = "+ model.getColumnId() + "MODEL TXT = "+ model.getClipTxtNumber() + "Probability = " + model.getProbability() +
     "TURN 0 = " + model.getTurnZero() + "TURNONE = " + model.getTurnOne() + "TURNTWO = " + model.getTurnTwo());

}


////////////////
////////////////
///Hist Table///
////////////////
////////////////
//
/**
* Returns the in order list for the history list that is to be displayed
* Calls private function histListForOutput which sets the propper ordering
* JON this is the one you call to get the list of historyModel objects
* @return
*/
public List<HistoryModel> getHistory()
{
List<HistoryModel> histModelList = hist_datasource.findAll();
     return histListForOutput(histModelList);
}

/*
* Take a List of history models, reverse it and return the reversed list
*/
private List<HistoryModel> histListForOutput (List<HistoryModel> histModelList)
{

List<HistoryModel> temp = new ArrayList<HistoryModel>();

for (int i = histModelList.size()-1; i >=0; i--)
{
//Log.i(LOGTAG, "BJS IN THE REVERSE THING= ");
temp.add(histModelList.get(i));
}
    outputHistModelList();
    return temp;
}

/*
* outputs the histmodellist
* for purposes of output testing
*/
public void outputHistModelList()
{
List<HistoryModel> histModelList = hist_datasource.findAll();

for (int i = 0; i < histModelList.size(); i++)
{
	
Log.i(LOGTAG, "BJS HISTCLIP " +histModelList.get(i).getId() + " PROB "+ histModelList.get(i).get_clip_id() + " " + histModelList.get(i).get_short_hanzi());



}

}



private HistoryModel createAndInsertHistModel()
{
HistoryModel hmodel = new HistoryModel();
hmodel.set_clip_id(key);
hmodel.set_short_hanzi("TEST");	///////CHANGE THIS TO WHATEVER VARIABLE YOU WANT
///////CONTAINING HANZI
hmodel = hist_datasource.createModel(hmodel);
Log.i(LOGTAG,"BJS CreateAndInsertHist.." + hmodel.get_clip_id() + " " + hmodel.get_short_hanzi());
outputHistModelList();

return hmodel;

}



/********************************
* End DATABASE STUFF
*******************************/




}
