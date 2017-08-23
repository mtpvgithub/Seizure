package com.mtpv.seizureInfo;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.analogics.thermalAPI.Bluetooth_Printer_3inch_ThermalAPI;
import com.analogics.thermalprinter.AnalogicsThermalPrinter;
import com.mtpv.seizure.R;
import com.mtpv.seizureHelpers.DataBase;

public class PrintDispaly extends Activity {
	final int PROGRESS_DIALOG = 1;
	TextView Tv;
	Button back_Btn, print_Btn;
	String Pidcode , PidName, PsCode, PsName, cadreCD,cadre,password;
	String generateChallan;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.printdisplay);
	
		back_Btn = (Button)findViewById(R.id.back_Btn);
		print_Btn = (Button)findViewById(R.id.print_Btn);
		
		Tv = (TextView)findViewById(R.id.Tv);
		
		SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		final String requestfrom = sharedPreference.getString("printFrom", "");
		Log.i("requestfrom for previous history :::", ""+requestfrom);
		
		
		back_Btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (requestfrom.equals("shop")) {
					DisplayP.detItems = null ;
					DisplayP.ditendItem_TV.setText("");	
					DisplayP.detendItemsSelected = "" ;
					DisplayP.detainedItems = "" ;
					
					DisplayP.detendItems_Selected_toDisplay.setLength(0);
					DisplayP.detendItems_Selected_toDisplay.delete(0, Shop_vendor.detendItemsA.length());
					
					DisplayP.detendItems_Selected_tosend.setLength(0);
					DisplayP.detendItems_Selected_tosend.delete(0, Shop_vendor.detendItemsA.length());

					Shop_vendor.imgString = null ;
					Shop_vendor.imgString2 = null ;
					
					Shop_vendor.detendLinearLayout.removeAllViews();
					Shop_vendor.imgv.setImageDrawable(getResources().getDrawable(R.drawable.photo));
					Shop_vendor.imgv2.setImageDrawable(getResources().getDrawable(R.drawable.tin));
					
					DisplayP.detItems = null ;
					DisplayP.ditendItem_TV.setText("");	
					DisplayP.detendItemsSelected = "" ;
					DisplayP.detainedItems = "" ;
					
					DisplayP.detendItems_Selected_toDisplay.setLength(0);
					DisplayP.detendItems_Selected_toDisplay.delete(0, Shop_vendor.detendItemsA.length());
					
					DisplayP.detendItems_Selected_tosend.setLength(0);
					DisplayP.detendItems_Selected_tosend.delete(0, Shop_vendor.detendItemsA.length());
					
					Shop_vendor.detItems.setLength(0);
					Shop_vendor.Ditenditems.clear();
					Shop_vendor.detendItemsA.setLength(0);
					Shop_vendor.detendItemsA.delete(0, Shop_vendor.detendItemsA.length());
					
					Shop_vendor.Itemname_ET.setText("");
					Shop_vendor.qty_ET.setText("");
					Shop_vendor.amount_ET.setText("");
					
					Shop_vendor.items = "" ;
					
					Shop_vendor.detendLinearLayout.removeAllViews();
					Intent i = new Intent(PrintDispaly.this, Shop_vendor.class); 
					startActivity(i);
					
				}else if (requestfrom.equals("footpath")) {
					FootPath_Vendor.detItems.setLength(0);
					
					FootPath_Vendor.detItems.setLength(0);
					FootPath_Vendor.Ditenditems.clear();
					FootPath_Vendor.detendItemsA.setLength(0);
					FootPath_Vendor.detendItemsA.delete(0, FootPath_Vendor.detendItemsA.length());
					Log.i("FootPath_Vendor.detendItemsA   :::", ""+FootPath_Vendor.detendItemsA);
					
					FootPath_Vendor.items = "" ;
					FootPath_Vendor.imgv.setImageDrawable(getResources().getDrawable(R.drawable.photo));
					FootPath_Vendor.imgv2.setImageDrawable(getResources().getDrawable(R.drawable.tin));
					
					FootPath_Vendor.imgString = null ;
					FootPath_Vendor.imgString2 = null ;
					
					FootPath_Vendor.detendLinearLayout.removeAllViews();
					
					FootPath_Preview.detendItemsA = "" ;
					
					FootPath_Vendor.detItems.setLength(0);
					
					FootPath_Preview.detendItems_Selected_toDisplay.setLength(0);
					FootPath_Preview.detendItems_Selected_toDisplay.delete(0, Shop_vendor.detendItemsA.length());
					
					FootPath_Preview.detendItems_Selected_tosend.setLength(0);
					FootPath_Preview.detendItems_Selected_tosend.delete(0, Shop_vendor.detendItemsA.length());
					
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
					
					Intent i2 = new Intent(PrintDispaly.this, FootPath_Vendor.class); 
					startActivity(i2);
					
				}else if (requestfrom.equals("duplicatprint")) {
					Intent i3 = new Intent(PrintDispaly.this, DuplicatePrint.class); 
					startActivity(i3);
					
				}
				else {
					Intent i4 = new Intent(PrintDispaly.this, Dashboard.class); 
					finish();
					
				}
			}
		});
		
		
		
		
		print_Btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Async_Task_PrintData().execute();
				
			}
		});
		
		 	Intent intent = getIntent();
		 
		    generateChallan = intent.getStringExtra("generateChallan");
		     

		    Tv.setText(generateChallan);
		
	}
	
	public class Async_Task_PrintData extends AsyncTask<Void, Void, String>{
		//ProgressDialog progress = ProgressDialog.show(Reports.this, "Loading...!", "Please wait......Processing!!!");
		
		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showDialog(PROGRESS_DIALOG);
		}
		
		@SuppressWarnings("unused")
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String bt = "" ;
			DataBase helper= new DataBase(getApplicationContext());
			try {
				android.database.sqlite.SQLiteDatabase db = openOrCreateDatabase(DataBase.DATABASE_NAME,MODE_PRIVATE, null);
				String selectQuery = "SELECT  * FROM " + DataBase.Bluetooth;
			     //SQLiteDatabase db = this.getWritableDatabase();
			    Cursor cursor = db.rawQuery(selectQuery, null);
		       // looping through all rows and adding to list
			    
		        if (cursor.moveToFirst()) {
		            do {
		            	
		            	Log.i("1 :",""+ cursor.getString(0));
		            	bt = cursor.getString(0);
		           	 	
		           	 	//et_bt_address.setText(BLT_Name);
		           	 
	            		} while (cursor.moveToNext());
		        		}
						db.close(); 
					} catch (Exception e) {
						e.printStackTrace();
				
				}
			//Bluetooth_Printer_3inch_ThermalAPI   preparePrintData= new Bluetooth_Printer_3inch_ThermalAPI();
			try{
				//Toast.makeText(getApplicationContext(), "print BUTTON ", Toast.LENGTH_LONG).show();
			
			Bluetooth_Printer_3inch_ThermalAPI   preparePrintData= new Bluetooth_Printer_3inch_ThermalAPI();
			
				//Toast.makeText(getApplicationContext(), "print " +generateChallan, Toast.LENGTH_LONG).show();
			
			String printdata=preparePrintData.font_Courier_36(generateChallan);
			AnalogicsThermalPrinter printer=new AnalogicsThermalPrinter();

			printer.openBT(bt);
			printer.printData(printdata);
			Thread.sleep(5000);
			printer.closeBT();
			
			
				//Toast.makeText(getApplicationContext(), "print ", Toast.LENGTH_LONG).show();
			}catch(Exception e){
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						showToast("Turn on Bluetooth and Configure Bluetooth Settings ");
					}
				});
			}
			
			return null;
		}
		
		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			removeDialog(PROGRESS_DIALOG);
		}
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
	
	 protected Dialog onCreateDialog(int id) {
	        switch (id) {
		    case PROGRESS_DIALOG:
				ProgressDialog pd = ProgressDialog.show(this, "", "",	true);
				pd.setContentView(R.layout.custom_progress_dialog);
				pd.setCancelable(false); 
				
				return pd;
				
		    default:
				break;
		        }
				return null; 
	    }
	 
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		showToast("Please Click  on Back to go Back");
	}
}
