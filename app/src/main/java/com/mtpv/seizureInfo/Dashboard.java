package com.mtpv.seizureInfo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mtpv.seizure.R;
import com.mtpv.seizureHelpers.DataBase;

public class Dashboard extends Activity{
	
	LinearLayout footpath_vendor, report, settings, duplicate_print, release_doc, images_layout,previous_his ;
	ImageView logout, image_to_capture ;
	TextView usernameTV;
	
	DataBase db;
	
	public static String class_clarify = null ;
	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_dashboard);
		
		//shop_vendor = (LinearLayout)findViewById(R.id.shop_vendor);
		footpath_vendor = (LinearLayout)findViewById(R.id.footpath_vendor);
		report = (LinearLayout)findViewById(R.id.report);
		settings = (LinearLayout)findViewById(R.id.settings);
		duplicate_print = (LinearLayout)findViewById(R.id.duplicate_print);
		previous_his = (LinearLayout)findViewById(R.id.previous_his);
		
		release_doc = (LinearLayout)findViewById(R.id.release_doc);
		
		logout = (ImageView)findViewById(R.id.logout);
		
		image_to_capture = (ImageView)findViewById(R.id.image_to_capture);
		
		usernameTV = (TextView)findViewById(R.id.usernameTV);
		
		SharedPreferences sharedPreferences = getSharedPreferences("loginValus", MODE_PRIVATE);
		String UserName = sharedPreferences.getString("USER_NAME", "");
		if (UserName!=null) {
			usernameTV.setText("Welcome : "+ UserName);
		}
		db = new DataBase(getApplicationContext());
		
		logout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TextView title = new TextView(Dashboard.this);
				title.setText("SEIZURE");
				title.setBackgroundColor(Color.RED);
				title.setGravity(Gravity.CENTER);
				title.setTextColor(Color.WHITE);
				title.setTextSize(26);
				title.setTypeface(title.getTypeface(), Typeface.BOLD);
				title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
				title.setPadding(20, 0, 20, 0);
				title.setHeight(70);
				
				String otp_message = "Are You Sure,\nDo You Want To Exit?" ;
				
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Dashboard.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
				alertDialogBuilder.setCustomTitle(title);
				alertDialogBuilder.setIcon(R.drawable.dialog_logo);
				alertDialogBuilder.setMessage(otp_message);
				alertDialogBuilder.setCancelable(false);
				alertDialogBuilder.setPositiveButton( "Cancel",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					});

				alertDialogBuilder.setNegativeButton("Ok",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				        	startActivity(intent);
						}
					});
				
			        AlertDialog alertDialog = alertDialogBuilder.create();
			        alertDialog.show();
			      
			        alertDialog.getWindow().getAttributes();

			        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
			        textView.setTextSize(28);
			        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
			        textView.setGravity(Gravity.CENTER);
			        
			        Button btn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
			        btn.setTextSize(22);
			        btn.setTextColor(Color.WHITE);
			        btn.setTypeface(btn.getTypeface(), Typeface.BOLD);
			        btn.setBackgroundColor(Color.RED);
			        
			        Button btn2 = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
			        btn2.setTextSize(22);
			        btn2.setTextColor(Color.WHITE);
			        btn2.setTypeface(btn2.getTypeface(), Typeface.BOLD);
			        btn2.setBackgroundColor(Color.RED);
			}
		});
		
		/*shop_vendor.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				
				LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
				if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
					Intent shop_vendor = new Intent(getApplicationContext(), Shop_vendor.class);
					startActivity(shop_vendor);
				}else {
					showGPSDisabledAlertToUser();
				}
				
			}
		});*/
		
		footpath_vendor.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
				if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
					Intent footpath_vendor = new Intent(getApplicationContext(), FootPath_Vendor.class);
					startActivity(footpath_vendor);
				}else {
					showGPSDisabledAlertToUser();
				}
				
			}
		});
		
		report.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent report = new Intent(getApplicationContext(), Reports.class);
				startActivity(report);
			}
		});
		
		duplicate_print.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent duplicatePrint = new Intent(getApplicationContext(), DuplicatePrint.class);
				startActivity(duplicatePrint);
			}
		});
		
		settings.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent settings = new Intent(getApplicationContext(), Settings.class);
				startActivity(settings);
			}
		});
		
		previous_his.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Dashboard.this,PreviousHistory.class);
				startActivity(i);
				Log.i("URL *******", ""+MainActivity.URL);
			}
		});
		release_doc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent release = new Intent(getApplicationContext(), ReleaseDocument.class);
				startActivity(release);
			}
		});
		
		image_to_capture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent images = new Intent(getApplicationContext(), CaptureImages.class);
				startActivity(images);
			}
		});
	}
	
	
	private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("    GPS is Disabled in your Device \n            Please Enable GPS?")
        .setCancelable(false)
        .setPositiveButton("Goto Settings",
                new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                Intent callGPSSettingIntent = new Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(callGPSSettingIntent);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		TextView title = new TextView(this);
		title.setText("SEIZURE");
		title.setBackgroundColor(Color.RED);
		title.setGravity(Gravity.CENTER);
		title.setTextColor(Color.WHITE);
		title.setTextSize(26);
		title.setTypeface(title.getTypeface(), Typeface.BOLD);
		title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
		title.setPadding(20, 0, 20, 0);
		title.setHeight(70);

	      
      	String otp_message = "Are you sure, You want to Exit Application...!" ;
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Dashboard.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		alertDialogBuilder.setCustomTitle(title);
		alertDialogBuilder.setIcon(R.drawable.dialog_logo);
		alertDialogBuilder.setMessage(otp_message);
		alertDialogBuilder.setCancelable(false);
			alertDialogBuilder.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							 Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				        	 startActivity(intent);
						}
					});

			alertDialogBuilder.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					});
			
		      AlertDialog alertDialog = alertDialogBuilder.create();
		      alertDialog.show();
		      
		      alertDialog.getWindow().getAttributes();

		        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
		        textView.setTextSize(28);
		        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
		        textView.setGravity(Gravity.CENTER);

		        
		        Button btn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
		        btn.setTextSize(22);
		        btn.setTextColor(Color.WHITE);
		        btn.setTypeface(btn.getTypeface(), Typeface.BOLD);
		        btn.setBackgroundColor(Color.RED); 
	      
		        
		        Button btn2 = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
		        btn2.setTextSize(22);
		        btn2.setTextColor(Color.WHITE);
		        btn2.setTypeface(btn2.getTypeface(), Typeface.BOLD);
		        btn2.setBackgroundColor(Color.RED);
	      
	}
}