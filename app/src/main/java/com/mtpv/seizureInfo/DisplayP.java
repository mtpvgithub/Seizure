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

public class DisplayP extends Activity{
	
	public static boolean shopFLG = false ;
	
	final int PROGRESS_DIALOG = 1;
	WebService WS = new WebService();
	
	TextView Fregnno_ET,shopn_ET,shopl_ET,rname_ET,rfname_ET,rage_ET,radderss_ET,rmobileno_ET,ridcardno_ET,w1name_ET,w1fname_ET,w1address_ET;
	TextView w2name_ET,w2fname_ET,w2address_ET;

	static TextView ditendItem_TV;

	TextView date_TV;
	
	TextView psname_Btn, pointname_Btn,ridcard_Btn, next_Btn,reset_Btn;
	
	LinearLayout detendL;
	
	String unitCode,unitName,bookedPsCode,bookedPsName,pointCode,pointName,operaterCd,operaterName,pidCd,pidName,password,cadreCD,cadre,
	onlineMode,imageEvidence,imgEncodedData,offenceDt,offenceTime,
	firmRegnNo,shopName,location,psCode,psName,respondantName,respondantFatherName,respondantAddress,respondantContactNo,respondantAge,IDCode,IDDetails,
	witness1Name,witness1FatherName,witness1Address,witness2Name,witness2FatherName,witness2Address;
	public static String amount=null;
	static String detainedItems;

	String simId;

	String imeiNo;

	String macId;

	String gpsLatitude;

	String gpsLongitude;

	String imgEncodedDataAfter; 
	
	String bussinessType; 
	
	String hawkerType; 
	
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
	
	LinearLayout witness_layout, witness_layout2, detainedITems_layout ;
	
	GPSTracker gps;
	double latitude = 0.0;
	double longitude = 0.0;
    static String detItems = null, image, tin_image ;
    
    static String detendItemsSelected = null, detendItemsSelected_toSend = null ;
    static StringBuffer detendItems_Selected_tosend ;

	static StringBuffer detendItems_Selected_toDisplay;
    static String detItems_amount= null ;
    String[] detSplit ;
    
    LinearLayout id_card_layout, id_card_no_layout ;
    
    StringBuffer detendItems_Selected ;
	
	@SuppressWarnings("unused")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.display);
		
		String date = (DateFormat.format("dd/MM/yyyy hh:mm:ss", new java.util.Date()).toString());
		
		imgvd=(ImageView)findViewById(R.id.imgvd);
		imgvd2=(ImageView)findViewById(R.id.imgvd2);
		
		Fregnno_ET = (TextView)findViewById(R.id.fregnno_ET);
		shopn_ET = (TextView)findViewById(R.id.shopn_ET);
		shopl_ET = (TextView)findViewById(R.id.shopl_ET);
		
		date_TV=(TextView)findViewById(R.id.date_TV);
		
		id_card_layout = (LinearLayout)findViewById(R.id.id_card_layout);
		id_card_no_layout = (LinearLayout)findViewById(R.id.id_card_no_layout);
		
		psname_Btn = (TextView)findViewById(R.id.psname_Btn);
		pointname_Btn=(TextView)findViewById(R.id.pointname_Btn);
		
		witness_layout = (LinearLayout)findViewById(R.id.witness_layout);
		witness_layout2 = (LinearLayout)findViewById(R.id.witness_layout2);
		detainedITems_layout = (LinearLayout)findViewById(R.id.detainedITems_layout);
		
		witness_layout.setVisibility(View.GONE);
		witness_layout2.setVisibility(View.GONE);
		detainedITems_layout.setVisibility(View.GONE);
		
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
		
		edit_Btn = (Button)findViewById(R.id.edit_Btn);
		submit_Btn = (Button)findViewById(R.id.submit_Btn);
		
		gps = new GPSTracker(DisplayP.this);
		if(gps.getLocation()!=null){
            
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
             
           
			
            // \n is for new line
            //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();    
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
            
           
        }
		
		 Intent intent = getIntent();
		 	
		 //	String ImageView = intent.getStringExtra("imgv");
		 
		    String Fregnno = intent.getStringExtra("Fregnno");
			String ShopName = intent.getStringExtra("ShopName");
			String ShopLocation = intent.getStringExtra("ShopLocation");
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
			String WitnessName1 = intent.getStringExtra("WitnessName1" );
			String WitnessFName1 = intent.getStringExtra("WitnessFName1" );
			String WitnessAddress1 = intent.getStringExtra("WitnessAddress1" );
			String WitnessName2 = intent.getStringExtra("WitnessName2");
			String WitnessFName2 = intent.getStringExtra("WitnessFName2");
			String WitnessAddress2 = intent.getStringExtra("WitnessAddress2" );
			detendItemsSelected = intent.getStringExtra("detendItemsA" );
			detItems = intent.getStringExtra("detItems");
			final String IDproofCD = intent.getStringExtra("IDproofCD");
			
			bussinessType = intent.getStringExtra("BussinessType");
			hawkerType = intent.getStringExtra("hawkerType");
			
			detSplit = detItems.split("\\@");
			
			int amont_length = detSplit.length;
			
			 amount = Shop_vendor.fine_amnt_et.getText().toString().trim();
			int amount_share = Integer.parseInt(amount);
			int final_share = amount_share/amont_length ;
			
			Log.i("amount ::::::::", ""+amount);
			Log.i("Shop amount ::::",""+Shop_vendor.fineAmnt);
			
			detendItems_Selected_tosend = new StringBuffer();
			detendItems_Selected_toDisplay = new StringBuffer();
			
			for (int i = 0; i < detSplit.length; i++) {
				detendItemsSelected = detSplit[i];
				detendItemsSelected_toSend = detSplit[i]+":"+final_share;
				
				detendItems_Selected_tosend.append(detendItemsSelected_toSend+"@");
				
				detendItems_Selected_toDisplay.append(detendItemsSelected+"\n");
			}
			
			SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			image = sharedPreference.getString("picture","");
			tin_image = sharedPreference.getString("tinPicture","");
			String timestamp = intent.getStringExtra("timestamp");
			
			//Toast.makeText(getApplicationContext(), "timestamp" +timestamp, Toast.LENGTH_LONG).show();
			if (image!=null) {
				try{
					byte[] decodedString = Base64.decode(image,Base64.NO_WRAP);
					InputStream inputStream  = new ByteArrayInputStream(decodedString);
					Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
					imgvd.setImageBitmap(bitmap);
					}catch(Exception e){
					showToast("Image Not Selected");
					}	
			}
			
			
			if (tin_image!=null) {
				try{
					byte[] decodedString = Base64.decode(tin_image,Base64.NO_WRAP);
					InputStream inputStream  = new ByteArrayInputStream(decodedString);
					Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
					imgvd2.setImageBitmap(bitmap);
					}catch(Exception e){
					showToast("Image Not Selected");
				}
					
			}
						
			//String DitendItems = detItems.replace("@", ",");
			//Log.e("ditendItem :,:", DitendItems);
			
			
			  pscd=intent.getStringExtra("pscd");
			  pointCD=intent.getStringExtra("pointCD");
			
			  
			  SharedPreferences sharedPreferences = getSharedPreferences("loginValus", MODE_PRIVATE);
				//23001004|TESTING PURPOSE|2300|TRAFFIC CELL|00|DEVP|7893816681|Y
				String userName = sharedPreferences.getString("PS_NAME", "");
				
				 A_PidCode = sharedPreferences.getString("PID_CODE","");
				 A_PidName = sharedPreferences.getString("PID_NAME","");
				 A_PsCode = sharedPreferences.getString("PS_CODE","");
				 A_PsName = sharedPreferences.getString("Ps_NAME","");
				 A_CadreCode = sharedPreferences.getString("CADRE_CODE","");
				 A_Cadre = sharedPreferences.getString("CADRE_NAME","");
				 A_SecurityCd = sharedPreferences.getString("SECURITY_CD","");
			/*  A_PidCode = intent.getStringExtra("A_PidCode");
			  A_PidName = intent.getStringExtra("A_PidName");
			  A_PsCode = intent.getStringExtra("A_PsCode");
			  A_PsName = intent.getStringExtra("A_PsName");
			  A_CadreCode = intent.getStringExtra("A_CadreCode");
			  A_Cadre = intent.getStringExtra("A_Cadre");
			  A_SecurityCd = intent.getStringExtra("A_SecurityCd"); 
			*/
			Fregnno_ET.setText(Fregnno);
			shopn_ET.setText(ShopName);
			shopl_ET.setText(ShopLocation);
			date_TV.setText(Date);
			psname_Btn.setText(PSName);
			pointname_Btn.setText(PsPointName);
			rname_ET.setText(RespName);
			rfname_ET.setText(RespFName);
			rage_ET.setText(RespAge);
			radderss_ET.setText(Respaddress);
			rmobileno_ET.setText(RespMobileNo);
			//ridcard_Btn.setText(RespIdCard);
			if (!Shop_vendor.ridcardno_ET.getText().toString().trim().equals("")) {
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
			
			
			if (Shop_vendor.witnessFLG && WitnessName1.trim()!=null && WitnessName1.trim().length()>2) {
				witness_layout.setVisibility(View.VISIBLE);
				
				w1name_ET.setText(WitnessName1);
				w1fname_ET.setText(WitnessFName1); 
				w1address_ET.setText(WitnessAddress1);
				
			}
			
			if (Shop_vendor.witnessFLG2 && WitnessName2.trim()!=null && WitnessName2.trim().length()>2) {
				witness_layout2.setVisibility(View.VISIBLE);
				
				w2name_ET.setText(WitnessName2);
				w2fname_ET.setText(WitnessFName2);
				w2address_ET.setText(WitnessAddress2);
				
			}
			
				ditendItem_TV.setText(detendItems_Selected_toDisplay);	
				
				if (ditendItem_TV.getText().toString().trim()!=null && ditendItem_TV.getText().toString().trim().length()>0) {
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
				 bookedPsName = Shop_vendor.psname_Btn.getText().toString().trim();
				 
				 Log.i("bookedPsName :::", ""+bookedPsName);
				 Log.i("bookedPsCode :::", ""+bookedPsCode);
				 Log.i("operaterName :::", ""+operaterName);
				 Log.i("pidName :::", ""+pidName);
				 Log.i("operaterCd :::", ""+operaterCd);
				 cadreCD = A_CadreCode;
				 cadre = A_Cadre;
				password = A_SecurityCd;
				
				
				 onlineMode = "1";
				
				 imageEvidence = "1";
				 imgEncodedData = image;
				 
				 imgEncodedDataAfter = tin_image ;
				 
				 
				
				 offenceDt = timestamp.trim();
				 offenceTime = time.toString();
				
			
				 firmRegnNo = Fregnno_ET.getText().toString().trim();
				 shopName = shopn_ET.getText().toString().trim();
				 location = "";
				
				
				
				 psCode = pscd;
				 psName = psname_Btn.getText().toString().trim();
				 pointCode = pointCD;
				 pointName = pointname_Btn.getText().toString().trim();
				
				 respondantName = rname_ET.getText().toString().trim();
				 respondantFatherName = rfname_ET.getText().toString().trim();
				 respondantAge = rage_ET.getText().toString().trim();
				 respondantAddress =radderss_ET.getText().toString().trim();
				 respondantContactNo =rmobileno_ET.getText().toString().trim();
				
				 //IDCode = IDproofCD;
				 IDCode = "1";
				// IDCode = ridcard_Btn.getText().toString();
				 IDDetails = ridcardno_ET.getText().toString().trim();
				
				 witness1Name = w1name_ET.getText().toString().trim();
				 witness1FatherName =w1fname_ET.getText().toString().trim();
				 witness1Address= w1address_ET.getText().toString().trim();
				 witness2Name = w2name_ET.getText().toString().trim();
				 witness2FatherName = w2fname_ET.getText().toString().trim();
				 witness2Address = w2address_ET.getText().toString().trim();
				 detainedItems = detItems;
				
				 simId = simID;
				 imeiNo = imeiID;
				 macId = macID;
				
				 gpsLatitude = ""+latitude;
				 gpsLongitude = ""+longitude;
			 
			submit_Btn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					showToast("Please Wait...!");
					if (!isNetworkAvailable()) {
					    // do something
				        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DisplayP.this);
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
				/*WebService WS = new WebService();*/
				
					
					/*String generateChallan = WS.generateChallan(unitCode,unitName,bookedPsCode,bookedPsName,pointCode,pointName,operaterCd,operaterName,pidCd,pidName,password,cadreCD,cadre,
							onlineMode,imageEvidence,imgEncodedData,offenceDt,offenceTime,
							firmRegnNo,shopName,location,psCode,psName,respondantName,respondantFatherName,respondantAddress,respondantContactNo,respondantAge,IDCode,IDDetails,
							witness1Name,witness1FatherName,witness1Address,witness2Name,witness2FatherName,witness2Address,detainedItems,
							simId,imeiNo,macId,gpsLatitude,gpsLongitude);
					
					Log.e("generateChallan Respons ::", generateChallan);
					
					Toast.makeText(getApplicationContext(), "Successfully Updated", Toast.LENGTH_LONG).show();
					
					Intent i = new Intent(DisplayP.this, PrintDispaly.class);
					i.putExtra("generateChallan", generateChallan);
					startActivity(i);
					 
					finish();*/

				}
			});
			
			edit_Btn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//ditendItem_TV.setText("");
					detItems = null ;
					ditendItem_TV.setText("");	
					DisplayP.detendItemsSelected = "" ;
					detainedItems = "" ;
					
					finish();
					detendItems_Selected_toDisplay.setLength(0);
					detendItems_Selected_toDisplay.delete(0, Shop_vendor.detendItemsA.length());
					
					detendItems_Selected_tosend.setLength(0);
					detendItems_Selected_tosend.delete(0, Shop_vendor.detendItemsA.length());
					
					Shop_vendor.detItems.setLength(0);
					Shop_vendor.Ditenditems.clear();
					Shop_vendor.detendItemsA.setLength(0);
					Shop_vendor.detendItemsA.delete(0, Shop_vendor.detendItemsA.length());
					
					Shop_vendor.Itemname_ET.setText("");
					Shop_vendor.qty_ET.setText("");
					Shop_vendor.amount_ET.setText("");
					
					Shop_vendor.items = "" ;
					
					Shop_vendor.detendLinearLayout.removeAllViews();
					
					
					
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

		//ProgressDialog progress = ProgressDialog.show(DisplayP.this, "Loading...!", "Please wait......Processing!!!");
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
			
		String 	respondantName = Shop_vendor.rname_ET.getText().toString();
		String 	respondantAddress = Shop_vendor.radderss_ET.getText().toString();
		for (String bookdpsName : Shop_vendor.psNameMap.keySet()) {
			if (bookdpsName.trim().equals(bookedPsName)) {
				bookedPsCode = Shop_vendor.psNameMap.get(bookdpsName);
			}
		}
		
		for (String pointName : Shop_vendor.pointNameMap.keySet()) {
			if (pointName.trim().equals(pointName)) {
				pointCD = Shop_vendor.pointNameMap.get(pointName);
			}
		}
		
		/*gps = new GPSTracker(DisplayP.this);
		if(gps.getLocation()!=null){
            
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
             
           
			
            
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();    
        }else{
          
            gps.showSettingsAlert();
            
           
        }*/
		generateChallan = WS.generateChallan(unitCode, unitName, bookedPsCode, bookedPsName, pointCD, pointName, operaterCd, operaterName, 
				pidCd, pidName, password, cadreCD, cadre, onlineMode, imageEvidence, imgEncodedData, offenceDt, offenceTime, firmRegnNo, shopName, 
				""+Shop_vendor.shopOWN_ET.getText().toString().trim(), ""+Shop_vendor.shopl_ET.getText().toString().trim(), location, psCode, psName, respondantName, respondantFatherName, respondantAddress, respondantContactNo, respondantAge, 
				IDCode, IDDetails, witness1Name, witness1FatherName, witness1Address, witness2Name, witness2FatherName, witness2Address, ""+detendItems_Selected_tosend, 
				simId, imeiNo, macId, gpsLatitude, gpsLongitude, imgEncodedDataAfter, amount, bussinessType, hawkerType );
		
/*		String unitCode,String unitName, String bookedPsCode,String bookedPsName,String pointCode, String pointName,
        String operaterCd,String operaterName,    String pidCd, String pidName,String password,String cadreCD,String cadre,   
        String onlineMode, String imageEvidence,String imgEncodedData,String offenceDt, String offenceTime,        
        String firmRegnNo, String shopName,String shopOwnerName,String shopAddress , String location,String psCode,
        String psName, String respondantName, String respondantFatherName,
        String respondantAddress,     String respondantContactNo,String respondantAge, String IDCode, String IDDetails,            
        String witness1Name, String witness1FatherName, String witness1Address,
        String witness2Name, String witness2FatherName, String witness2Address,
        String detainedItems,            
        String simId, String imeiNo,String macId, String gpsLatitude, String gpsLongitude*/		
			
			
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
					}else if(!WebService.Challan_response.equals("0^NA^NA") ) {
						generateChallan = WebService.Challan_response ;
						shopFLG = true ;

						SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
						SharedPreferences.Editor edit = sharedPreference.edit();
						edit.putString("printFrom", "shop");
						edit.commit();
						
						
						Intent i = new Intent(DisplayP.this, PrintDispaly.class);
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
		showToast("Please Click  on Back to go Back");
	}

}
