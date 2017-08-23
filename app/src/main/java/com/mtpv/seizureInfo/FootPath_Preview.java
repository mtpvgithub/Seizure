package com.mtpv.seizureInfo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
//import org.apache.commons.codec.binary.Base64;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mtpv.seizure.R;
import com.mtpv.seizureHelpers.WebService;
import com.mtpv.seizureInfo.DisplayP.Async_task_submitDetails;

public class FootPath_Preview extends Activity{
	public static boolean FootPathFLG = false ;
	
	final int PROGRESS_DIALOG = 1;
	WebService WS = new WebService();
	
	TextView Fregnno_ET,location_ET,landmark_ET,rname_ET,rfname_ET,rage_ET,radderss_ET,rmobileno_ET,ridcardno_ET,w1name_ET,w1fname_ET,w1address_ET;
	TextView w2name_ET,w2fname_ET,w2address_ET;

	static TextView ditendItem_TV;

	TextView date_TV;
	
	TextView psname_Btn, pointname_Btn,ridcard_Btn, next_Btn,reset_Btn;
	
	LinearLayout detendL;
	public static String amount=null;
	
	String unitCode,unitName,bookedPsCode,bookedPsName,pointCode,pointName,operaterCd,operaterName,pidCd,pidName,password,cadreCD,cadre,
	onlineMode,imageEvidence,imgEncodedData,offenceDt,offenceTime,
	firmRegnNo,shopName,location = "",landmark,psCode,psName,respondantName,respondantFatherName,respondantAddress,respondantContactNo,respondantAge,IDCode,IDDetails,
	witness1Name,witness1FatherName,witness1Address,witness2Name,witness2FatherName,witness2Address;

	static String detainedItems;

	String simId;

	String imeiNo;

	String macId;

	String gpsLatitude;

	String gpsLongitude;

	String imgEncodedDataAfter; 
	
	//String image;
	Button edit_Btn, submit_Btn;
	//ImageButton camera_Btn;
	ImageView imgvd, imgvd2;
	
	static String A_PidCode =null;
	static String A_PidName =null;
	static String A_PsCode =null;
	static String A_PsName =null;
	static String A_CadreCode =null;
	static String A_Cadre =null;
	static String A_SecurityCd =null;
	
	static String pscd =null;
	static String pointCD =null;
	
	String simID,imeiID,macID;
	
	LinearLayout detainedITems_layout ;
	
	GPSTracker gps;
	double latitude = 0.0;
	double longitude = 0.0;
    static String detendItemsA = null ;
    static String detItems = null, image, tin_image ;
    
    LinearLayout id_card_layout, id_card_no_layout ;
    static String detendItemsSelected = null, detendItemsSelected_toSend = null ;
    static StringBuffer detendItems_Selected_tosend ;

	static StringBuffer detendItems_Selected_toDisplay;
    static String detItems_amount= null ;
    String[] detSplit ;
    
    String bussinessType; 
	
	String hawkerType; 
    
	@SuppressWarnings("unused")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_foot_path__preview);
		
		String date = (DateFormat.format("dd/MM/yyyy hh:mm:ss", new java.util.Date()).toString());
		//Log.i("date ::::",""+date);
		
		imgvd=(ImageView)findViewById(R.id.imgvd);
		imgvd2=(ImageView)findViewById(R.id.imgvd2);
		
		Fregnno_ET = (TextView)findViewById(R.id.fregnno_ET);
		location_ET = (TextView)findViewById(R.id.location_ET);
		landmark_ET = (TextView)findViewById(R.id.landmark_ET);
		
		detainedITems_layout = (LinearLayout)findViewById(R.id.detainedITems_layout);
		detainedITems_layout.setVisibility(View.GONE);
		
		date_TV=(TextView)findViewById(R.id.date_TV);
		
		psname_Btn = (TextView)findViewById(R.id.psname_Btn);
		pointname_Btn=(TextView)findViewById(R.id.pointname_Btn);
		
		rname_ET = (TextView)findViewById(R.id.rname_ET);
		rfname_ET = (TextView)findViewById(R.id.rfname_ET);
		rage_ET = (TextView)findViewById(R.id.rage_ET);
		radderss_ET = (TextView)findViewById(R.id.radderss_ET);
		rmobileno_ET = (TextView)findViewById(R.id.rmobileno_ET);
		
		
		ridcard_Btn = (TextView)findViewById(R.id.ridcard_Btn);
		ridcardno_ET = (TextView)findViewById(R.id.ridcardno_ET);
		
		w1name_ET = (TextView)findViewById(R.id.w1name_ET);
		w1fname_ET = (TextView)findViewById(R.id.w1fname_ET);
		w1address_ET = (TextView)findViewById(R.id.w1address_ET);
		w2name_ET = (TextView)findViewById(R.id.w2name_ET);
		w2fname_ET = (TextView)findViewById(R.id.w2fname_ET);
		w2address_ET = (TextView)findViewById(R.id.w2address_ET);
		ditendItem_TV = (TextView)findViewById(R.id.ditendItem_TV);
		
		id_card_layout = (LinearLayout)findViewById(R.id.id_card_layout);
		id_card_no_layout = (LinearLayout)findViewById(R.id.id_card_no_layout);
		
		edit_Btn = (Button)findViewById(R.id.edit_Btn);
		submit_Btn = (Button)findViewById(R.id.submit_Btn);
		
		gps = new GPSTracker(FootPath_Preview.this);
		if(gps.getLocation()!=null){
            
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
             
        }else{
            
            gps.showSettingsAlert();
            
           
        }
		
		/****************************************************************************************************************************/
		
		/*gps = new GPSTracker(FootPath_Preview.this);
		if(gps.canGetLocation()){
			 latitude = gps.getLatitude()+"";
             longitude = gps.getLongitude()+"";
             
             
             
          //  Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
		}else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }*/
		
	
		
		/****************************************************************************************************************************/
		
		 Intent intent = getIntent();
		 	
		 //	String ImageView = intent.getStringExtra("imgv");
		 
		    String Fregnno = intent.getStringExtra("Fregnno");
			String Landmark = intent.getStringExtra("Landmark");
			String Location = intent.getStringExtra("Location");
			String Date = intent.getStringExtra("Date");
			String PSName = intent.getStringExtra("PSName" );
			String PsPointName = intent.getStringExtra("PsPointName" );
			String RespName = intent.getStringExtra("RespName" );
			String RespFName = intent.getStringExtra("RespFName" );
			String RespAge = intent.getStringExtra("RespAge");
			String Respaddress = intent.getStringExtra("Respaddress" );
			String RespMobileNo = intent.getStringExtra("RespMobileNo" );
			String RespIdCard = intent.getStringExtra("RespIdCard" );
			String RespIdcarvalue = intent.getStringExtra("RespIdcarvalue" );
			String WitnessName1 = intent.getStringExtra("" );
			String WitnessFName1 = intent.getStringExtra("" );
			String WitnessAddress1 = intent.getStringExtra("" );
			String WitnessName2 = intent.getStringExtra("");
			String WitnessFName2 = intent.getStringExtra("");
			String WitnessAddress2 = intent.getStringExtra("" );
			detendItemsA = intent.getStringExtra("detendItemsA" );
			detItems = intent.getStringExtra("detItems");
			final String IDproofCD = intent.getStringExtra("IDproofCD");
//			final String image = intent.getStringExtra("picture");
			//String timestamp = intent.getStringExtra("timestamp");
			bussinessType = intent.getStringExtra("BussinessType");
			hawkerType = intent.getStringExtra("hawkerType");
			
			
			SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			image = sharedPreference.getString("picture","");
			tin_image = sharedPreference.getString("tinPicture","");
			String timestamp = intent.getStringExtra("timestamp");
			
			Log.e("timestamp",timestamp);


			detSplit = detItems.split("\\@");
			
			int amont_length = detSplit.length;
			//Log.i("amont_length :::", ""+amont_length);
			amount = FootPath_Vendor.fine_amnt_et.getText().toString().trim();
			int amount_share = Integer.parseInt(amount);
			//Log.i("amount_share :::", ""+amount_share);
			
			int final_share = amount_share/amont_length ;
			
			//Log.i("final_share :::", ""+final_share);
			
			detendItems_Selected_tosend = new StringBuffer();
			detendItems_Selected_toDisplay = new StringBuffer();
			
			for (int i = 0; i < detSplit.length; i++) {
				detendItemsSelected = detSplit[i];
				detendItemsSelected_toSend = detSplit[i]+":"+final_share;
				
				detendItems_Selected_tosend.append(detendItemsSelected_toSend+"@");
				
				detendItems_Selected_toDisplay.append(detendItemsSelected+"\n");
				//Log.i("detendItems_Selected_tosend :::", ""+detendItems_Selected_tosend);
			}
			
			
			try{
			byte[] decodedString = Base64.decode(image,Base64.NO_WRAP);
			InputStream inputStream  = new ByteArrayInputStream(decodedString);
			Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
			imgvd.setImageBitmap(bitmap);
			}catch(Exception e){
			//Toast.makeText(getApplicationContext(), "Image Not Selected", Toast.LENGTH_LONG).show();
			showToast("Image Not Selected");
			}
			
			
			try{
				byte[] decodedString = Base64.decode(tin_image,Base64.NO_WRAP);
				InputStream inputStream  = new ByteArrayInputStream(decodedString);
				Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
				imgvd2.setImageBitmap(bitmap);
				}catch(Exception e){
				//Toast.makeText(getApplicationContext(), "Image Not Selected", Toast.LENGTH_LONG).show();
				showToast("Image Not Selected");
				}
		
		
			String DitendItems = detItems.replace("@", ",");
			//Log.e("ditendItem :,:", DitendItems);
			
			
			  pscd=intent.getStringExtra("pscd");
			  pointCD=intent.getStringExtra("pointCD");
			
			  A_PidCode = intent.getStringExtra("A_PidCode");
			  A_PidName = intent.getStringExtra("A_PidName");
			  A_PsCode = intent.getStringExtra("A_PsCode");
			  A_PsName = intent.getStringExtra("A_PsName");
			  A_CadreCode = intent.getStringExtra("A_CadreCode");
			  A_Cadre = intent.getStringExtra("A_Cadre");
			  A_SecurityCd = intent.getStringExtra("A_SecurityCd"); 
			
			Fregnno_ET.setText(Fregnno);
			location_ET.setText(Location);
			landmark_ET.setText(Landmark);
			date_TV.setText(Date);
			psname_Btn.setText(PSName);
			pointname_Btn.setText(PsPointName);
			rname_ET.setText(RespName);
			rfname_ET.setText(RespFName);
			rage_ET.setText(RespAge);
			radderss_ET.setText(Respaddress);
			rmobileno_ET.setText(RespMobileNo);
			
			if (!FootPath_Vendor.ridcardno_ET.getText().toString().trim().equals("")) {
				id_card_no_layout.setVisibility(View.VISIBLE);
				id_card_layout.setVisibility(View.VISIBLE);
				
				ridcard_Btn.setText("Aadhaar");
				ridcardno_ET.setText(RespIdcarvalue);
			}else {
				id_card_no_layout.setVisibility(View.GONE);
				id_card_layout.setVisibility(View.GONE);
				
				ridcard_Btn.setText("");
				ridcardno_ET.setText(RespIdcarvalue);
			}
			
			
			w1name_ET.setText(WitnessName1);
			w1fname_ET.setText(WitnessFName1); 
			w1address_ET.setText(WitnessAddress1);
			w2name_ET.setText(WitnessName2);
			w2fname_ET.setText(WitnessFName2);
			w2address_ET.setText(WitnessAddress2);
			ditendItem_TV.setText(detendItems_Selected_toDisplay);
			
			if (ditendItem_TV.getText().toString().trim()!=null && !ditendItem_TV.getText().toString().trim().equals("")) {
				detainedITems_layout.setVisibility(View.VISIBLE);
			}else {
				detainedITems_layout.setVisibility(View.GONE);
			}
			
			TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
			
		     imeiID = tm.getDeviceId(); //International Mobile Station Equipment Identity
		     simID = tm.getSimSerialNumber();//subscriber identity module
			
		    WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = manager.getConnectionInfo();
			 macID = info.getMacAddress();
			 
/**************************************************************************************************************************************/
			 
			 
			    Calendar cal = Calendar.getInstance(); 
				int hourofday = cal.get(Calendar.HOUR_OF_DAY);
				int minute = cal.get(Calendar.MINUTE);
				int second = cal.get(Calendar.SECOND);
				
				StringBuilder time = new StringBuilder();
				time.append(hourofday).append(":").append(minute).append(":").append(second);

				 unitCode = "23";
				 unitName = "Hyderabad";
				
				 pidCd = A_PidCode;
				 operaterCd = A_PidCode;
				 pidName = A_PidName;
				 operaterName = A_PidName;
				 bookedPsCode = A_PsCode;
				 bookedPsName = FootPath_Vendor.psname_Btn.getText().toString().trim();
				 cadreCD = A_CadreCode;
				 cadre = A_Cadre;
				 password = A_SecurityCd;
				
				
				 onlineMode = "1";
				
				 imageEvidence = "1";
				 imgEncodedData = image;
				 
				 imgEncodedDataAfter = tin_image ;
				
				 offenceDt = timestamp.trim();
				// Log.i("offenceDt :::", ""+offenceDt);
				 
				 offenceTime = time.toString();
				
			
				 firmRegnNo = Fregnno_ET.getText().toString();
				 location = location_ET.getText().toString();
				 landmark = landmark_ET.getText().toString();
				
				 psCode = pscd;
				 psName = psname_Btn.getText().toString();
				 pointCode = pointCD;
				 pointName = pointname_Btn.getText().toString();
				
				 respondantName = rname_ET.getText().toString();
				 respondantFatherName = rfname_ET.getText().toString();
				 respondantAge = rage_ET.getText().toString();
				 respondantAddress =radderss_ET.getText().toString();
				 respondantContactNo =rmobileno_ET.getText().toString();
				
				// IDCode = IDproofCD;
				 IDCode = "1";
				// IDCode = ridcard_Btn.getText().toString();
				 IDDetails = ridcardno_ET.getText().toString();
				
				 witness1Name = w1name_ET.getText().toString();
				 witness1FatherName =w1fname_ET.getText().toString();
				 witness1Address= w1address_ET.getText().toString();
				 witness2Name = w2name_ET.getText().toString();
				 witness2FatherName = w2fname_ET.getText().toString();
				 witness2Address = w2address_ET.getText().toString();
				 detainedItems = detItems;
				
				 simId = simID;
				 imeiNo = imeiID;
				 macId = macID;
				
				 gpsLatitude = ""+latitude;
				 gpsLongitude =""+longitude;
			 
	/***********************************************************************************************************************************/		

			 
			 
			submit_Btn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					//Toast.makeText(getApplicationContext(), "Please wait", Toast.LENGTH_LONG).show();
					showToast("Please wait...!");
					
					if (!isNetworkAvailable()) {
					    // do something
				        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FootPath_Preview.this);
				        alertDialogBuilder.setMessage("Please Enable Internet Connection")
				        .setCancelable(false)
				        .setPositiveButton("Ok",
				            new DialogInterface.OnClickListener(){
				            public void onClick(DialogInterface dialog, int id){
				            }
				        });
				        AlertDialog alert = alertDialogBuilder.create();
				        alert.show();
					}else{
						LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
						if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
							new Async_task_submitDetails().execute();
						}else {
							showGPSDisabledAlertToUser();
						}
						
					}
					
				}
			});
			
			edit_Btn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//ditendItem_TV.setText("");
					finish();
					FootPath_Vendor.detItems.setLength(0);
					
					detendItems_Selected_toDisplay.setLength(0);
					detendItems_Selected_toDisplay.delete(0, Shop_vendor.detendItemsA.length());
					
					detendItems_Selected_tosend.setLength(0);
					detendItems_Selected_tosend.delete(0, Shop_vendor.detendItemsA.length());
					
					FootPath_Vendor.detItems.setLength(0);
					FootPath_Vendor.Ditenditems.clear();
					FootPath_Vendor.detendItemsA.setLength(0);
					FootPath_Vendor.detendItemsA.delete(0, FootPath_Vendor.detendItemsA.length());
					//Log.i("FootPath_Vendor.detendItemsA   :::", ""+FootPath_Vendor.detendItemsA);
					
					FootPath_Vendor.Itemname_ET.setText("");
					FootPath_Vendor.qty_ET.setText("");
					FootPath_Vendor.amount_ET.setText("");
					
					FootPath_Vendor.items = "" ;
					
					FootPath_Vendor.detendLinearLayout.removeAllViews();
					
					detItems = null ;
					ditendItem_TV.setText("");	
					FootPath_Preview.detendItemsA = "" ;
					detainedItems = "" ;
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
	
	/***********************Network Check***************************/
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	/***********************Network Check***************************/
	
	public class Async_task_submitDetails extends AsyncTask<Void, Void, String>{

		//ProgressDialog progress = ProgressDialog.show(FootPath_Preview.this, "Loading...!", "Please wait......Processing!!!");
		String generateChallan = null ;
		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showDialog(PROGRESS_DIALOG);
		}
		
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			
		String 	respondantName = FootPath_Vendor.rname_ET.getText().toString();
		String 	respondantAddress = FootPath_Vendor.radderss_ET.getText().toString();
		
		for (String bookdpsName : FootPath_Vendor.psNameMap.keySet()) {
			if (bookdpsName.trim().equals(bookedPsName)) {
				bookedPsCode = FootPath_Vendor.psNameMap.get(bookdpsName);
			}
		}
		//Log.i("detendItems_Selected_tosend @ service::::", ""+detendItems_Selected_tosend);
		for (String pointName : FootPath_Vendor.pointNameMap.keySet()) {
			if (pointName.trim().equals(pointName)) {
				pointCD = FootPath_Vendor.pointNameMap.get(pointName);
			}
		}
		/*gps = new GPSTracker(FootPath_Preview.this);
		if(gps.canGetLocation()){
			 latitude = gps.getLatitude()+"";
             longitude = gps.getLongitude()+"";
             
             
             
          // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
		}else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }*/
		generateChallan = WS.generateChallan(unitCode, unitName, bookedPsCode, bookedPsName, pointCD, pointName, operaterCd, operaterName, 
				pidCd, pidName, password, cadreCD, cadre, onlineMode, imageEvidence, imgEncodedData, offenceDt, offenceTime, "", "", 
				"", "", location, psCode, psName, respondantName, respondantFatherName, respondantAddress, respondantContactNo, respondantAge, 
				IDCode, IDDetails, witness1Name, witness1FatherName, witness1Address, witness2Name, witness2FatherName, witness2Address, ""+detendItems_Selected_tosend, 
				simId, imeiNo, macId, gpsLatitude, gpsLongitude,  imgEncodedDataAfter, amount, bussinessType, hawkerType);
		
			return null;
         }
          
		
		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			removeDialog(PROGRESS_DIALOG);
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					
					if(WebService.Challan_response.equals("0^NA^NA")){
						
						showToast("Challan Generation Failed");
					}else {
						generateChallan = WebService.Challan_response ;
						
						SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
						SharedPreferences.Editor edit = sharedPreference.edit();
						edit.putString("printFrom", "footpath");
						edit.commit();
						
						Intent i = new Intent(FootPath_Preview.this, PrintDispaly.class);
						FootPathFLG = true ;
						i.putExtra("generateChallan", generateChallan);
						startActivity(i);			 
						finish();
						showToast("Challan Generated Successfully");
					}
				}
			});
		}
	}
	
	
	protected Dialog onCreateDialog(int id) {
        switch (id) {
        case PROGRESS_DIALOG:
			ProgressDialog pd = ProgressDialog.show(this, "", "",	true);
			pd.setContentView(R.layout.custom_progress_dialog);
			pd.setCancelable(false); 
			
			return pd;
       /* case DATE_DIALOG_ID:
            return new DatePickerDialog(this, 
                        pDateSetListener,
                        pYear, pMonth, pDay);*/
        }
        return null;
    }
	
	private void showToast(String msg) {
		// TODO Auto-generated method stub
		Toast toast = Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		View toastView = toast.getView();
		
		ViewGroup group = (ViewGroup) toast.getView();
	    TextView messageTextView = (TextView) group.getChildAt(0);
	    messageTextView.setTextSize(24);
		
    	toastView.setBackgroundResource(R.drawable.toast_background);
	    toast.show();
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		showToast("Please Click  on Edit to go Back");
	}

}
