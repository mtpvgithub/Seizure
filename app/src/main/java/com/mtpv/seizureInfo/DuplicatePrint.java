package com.mtpv.seizureInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mtpv.seizure.R;
import com.mtpv.seizureHelpers.DataBase;
import com.mtpv.seizureHelpers.WebService;

@SuppressWarnings("unused")
public class DuplicatePrint extends Activity {
	public static boolean dupprintFLG = false ;
	
	final int PROGRESS_DIALOG = 1;
	EditText regnNo;
	Button date_Btn,get_Btn,cancel2_Btn, back_Btn;
	TextView printTv;
	Button Btn;
	LinearLayout ll;
	LinearLayout layout;
	LinearLayout.LayoutParams params;
	String []DuplicatePrint1;
	//ArrayList<String> arr_fav_list = new ArrayList<String>();
	
		private int pYear;
	    private int pMonth;
	    private int pDay;
	    private int mSecond;
	    private int mHour;
	    private int mMinute;
	    static final int DATE_DIALOG_ID = 0;
	    static final int TIME_DIALOG_ID=1;
	    StringBuilder timestamp ;
	    
	    String PidCode =null;//= com.tab39b.MainActivity.PIdCode;
	    List<String> duplicateChallan=new ArrayList<String>();
	    //List<String> DuplicatePrint1=new ArrayList<String>();
	    List<String> textdata=new ArrayList<String>();
	    HashMap<String,String> duplicateChallanMap=new HashMap<String,String>();

	static String resp, DuplicatePrint;
	final WebService WS = new WebService();
	
	public static String offenceDate = null ;
	
	public static String shopName = null;
	public static String firmRegnNo = null;
	public static String responsdentName= null, button_response = "";
	//DataBase db;
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_duplicate_print);
		
		date_Btn = (Button)findViewById(R.id.duplicate_date_btn);
		get_Btn = (Button)findViewById(R.id.get_dup_print);
		cancel2_Btn = (Button)findViewById(R.id.cancel_dup_print);
		//back_Btn = (Button)findViewById(R.id.back_Btn);
		
		printTv = (TextView)findViewById(R.id.dup_text);
	
		try {
			android.database.sqlite.SQLiteDatabase db = openOrCreateDatabase(DataBase.DATABASE_NAME,MODE_PRIVATE, null);
			String selectQuery = "SELECT  * FROM " + DataBase.USER_TABLE;
		     //SQLiteDatabase db = this.getWritableDatabase();
		    Cursor cursor = db.rawQuery(selectQuery, null);
	       // looping through all rows and adding to list
		    
	        if (cursor.moveToFirst()) {
	            do {
	            	
	           	 PidCode = cursor.getString(0);
	           	 
	            	/* pidCode = cursor.getString(0);
	            	 pidName = cursor.getString(1);
	            	 psCode = cursor.getString(2);
	            	 psName = cursor.getString(3);
	            	 cadreCd = cursor.getString(4);
	            	 cadreName = cursor.getString(5);
	            	 unitCd = cursor.getString(6);
	            	 unitName = cursor.getString(7);
	            	 */
	            		} while (cursor.moveToNext());
	        		}
					db.close(); 
				} catch (Exception e) {
					e.printStackTrace();
			
				}
		
		
		
		final Calendar cal = Calendar.getInstance();
		   pYear = cal.get(Calendar.YEAR);
	       pMonth = cal.get(Calendar.MONTH);
	       pDay = cal.get(Calendar.DAY_OF_MONTH);
	       mHour = cal.get(Calendar.HOUR_OF_DAY);
	       mMinute = cal.get(Calendar.MINUTE);
	       mSecond=cal.get(Calendar.SECOND);
	       updateDisplay();
	       
	       
	       
		
		date_Btn.setOnClickListener(new View.OnClickListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				showDialog(DATE_DIALOG_ID);
				//Toast.makeText(getApplicationContext(), "Date Button", Toast.LENGTH_SHORT).show();
				
			}
		});
		
		layout = (LinearLayout ) findViewById(R.id.h1);
		params = new LinearLayout.LayoutParams(
        		LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		
		get_Btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Async_task_getDetails().execute();
				
				
			}
		
		});
		
		
		cancel2_Btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent back = new Intent(DuplicatePrint.this, Dashboard.class);
				startActivity(back);
				
			}
		});
		
	}
	
	public class Async_task_getDetails extends AsyncTask<Void, Void, String>{
		
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
			
			offenceDate = date_Btn.getText().toString();
			
			DuplicatePrint = WS.getDuplicatePrint(offenceDate,PidCode,"","","");		
	    	 Log.e("DuplicatePrint ::::", DuplicatePrint);

			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					 layout.removeAllViews();
					 //ll.removeAllViews();
			    	 //String pidCode = PidCode;
			    	 
			    	 Log.e("offenceDate ::::", offenceDate);
			    	 Log.e("pidCode ::::", PidCode);

			    		try{
							//((LinearLayout)ll.getParent()).removeView(ll);
							
							if (DuplicatePrint!=null&&DuplicatePrint.length()>0){	
								DuplicatePrint1 = DuplicatePrint.split("\\|");
								
								for (int j=0;j<DuplicatePrint1.length;j++) {	
									String []Dprint = DuplicatePrint1[j].split("\\@");
									String Eticket_No = Dprint[0];
									String ShopName = Dprint[1];
									String respondantName = Dprint[2];
									
									duplicateChallan.add(Eticket_No+"\t\t"+ShopName+"\t\t\t"+respondantName);
									textdata.add(""+ShopName+" "+respondantName);
									duplicateChallanMap.put(Eticket_No+"\t\t"+ShopName+"\t\t\t"+respondantName, Eticket_No);
													
									ll = new LinearLayout(DuplicatePrint.this,null);
									ll.setOrientation(LinearLayout.HORIZONTAL);
									ll.setGravity(Gravity.CENTER);
					         
									Btn = new Button(DuplicatePrint.this);
									Btn.setId(j);
									Btn.setWidth(550);
									Btn.setText(Eticket_No+"\t\t"+ShopName+"\t\t\t"+respondantName);
									Btn.setBackgroundResource(R.drawable.navi_blue_btn_style);
									Btn.setTextColor(Color.WHITE);
									Btn.setGravity(Gravity.CENTER);
									Btn.setTypeface(null,Typeface.BOLD);
									
									Btn.setOnClickListener(new View.OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										CharSequence selectedButtonValue = ((Button) v).getText();
										
										for(String buttonText:duplicateChallanMap.keySet()){
											if(buttonText.equals(selectedButtonValue.toString())){
												
												button_response = duplicateChallanMap.get(buttonText);
												dupprintFLG = false ;
												new Async_task_eTicket().execute();
												/*String getDuplicatePrintByEticket = WS.getDuplicatePrintByEticket(""+duplicateChallanMap.get(buttonText));
												Log.e("getDuplicatePrintByEticket", getDuplicatePrintByEticket);*/
												
												}
											}
										}
									});
						    	ll.addView(Btn);
						    	layout.addView(ll);
								}//end of for loop
							}
							else{
								//Btn.setText("");//end validation
								//Toast.makeText(getApplicationContext(), "Data Not Found",Toast.LENGTH_LONG).show();
							}
						}catch(Exception error){
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									/*Toast toast = Toast.makeText(getApplicationContext(), "Data Not Found", Toast.LENGTH_SHORT);
									toast.setGravity(Gravity.CENTER, 0, 0);
									View toastView = toast.getView();
								    toast.show();*/
									showToast("Data Not Found");
								}
							});
						}
				}
			});
			
			return null;
		}
		
		
		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			//progress.dismiss();
			removeDialog(PROGRESS_DIALOG);
		}
	}
	/*************************************************************************Date methos********************************************/
	
	public class Async_task_eTicket extends AsyncTask<Void, Void, String> {
		String getDuplicatePrintByEticket = "" ;
		@Override
		protected String doInBackground(Void... params) {
			
			//button_response
		    getDuplicatePrintByEticket = WS.getDuplicatePrintByEticket(button_response);
			return null;
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showDialog(PROGRESS_DIALOG);
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			removeDialog(PROGRESS_DIALOG);
			
			if (getDuplicatePrintByEticket.equals("0^NA^NA")) {
				dupprintFLG = false ;
			}else {
				dupprintFLG = true ;
				
				SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				SharedPreferences.Editor edit = sharedPreference.edit();
				edit.putString("printFrom", "duplicatprint");
				edit.commit();
				
				Intent i = new Intent(DuplicatePrint.this, PrintDispaly.class);
				i.putExtra("generateChallan", WebService.Challan_response);
				startActivity(i);
			}
		}
	}
	
	 private DatePickerDialog.OnDateSetListener pDateSetListener =
	            new DatePickerDialog.OnDateSetListener() {
	 
	                public void onDateSet(DatePicker view, int year, 
	                                      int monthOfYear, int dayOfMonth) {
	                	
	                	
	                	pYear =year;
	                    pMonth = monthOfYear;
	                    pDay = dayOfMonth;
	                    updateDisplay();
	                }

	            };
	     
	            
	       // Register  TimePickerDialog listener                
               private TimePickerDialog.OnTimeSetListener mTimeSetListener =
                   new TimePickerDialog.OnTimeSetListener() {
            // the callback received when the user "sets" the TimePickerDialog in the dialog
                       /*public void onTimeSet(TimePicker view, int hourOfDay, int min) {
                           hour = hourOfDay;
                           minute = min;
                           // Set the Selected Date in Select date Button
                           btnSelectTime.setText("Time selected :"+hour+"-"+minute);
                         }
*/
						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							// TODO Auto-generated method stub
							mHour = hourOfDay;
                           mMinute = minute;
                           updateDisplay();
   	                  //  displayToast();
						}
                   };
	            
	            
	    /** Updates the date in the TextView */
	    
		@SuppressLint("SimpleDateFormat")
		private void updateDisplay() {
			
			/*datepicker_Btn.setText(new StringBuilder()
           // Month is 0 based so add 1
	    				.append(d).append("/")
	    				.append(m + 1).append("/")
	    				.append(y).append(" "));*/
			
			date_Btn.setText(new StringBuilder()
           // Month is 0 based so add 1
	    				.append(pDay).append("/")
	    				.append(pMonth + 1).append("/")
	    				.append(pYear).append(" "));
			
	    	
	    			timestamp = new StringBuilder().append(pDay).append("/")
	    											.append(pMonth + 1).append("/")
	    											.append(pYear).append("/")
	    											.append(mHour).append(":")
	    											.append(mMinute).append(":")
	    											.append(mSecond).append(" ");
	    			
	    			
			
	    			
	    	//Toast.makeText(getApplicationContext(), "timeStamp" +timestamp, Toast.LENGTH_LONG).show();
			
	    }
	     
	 	    
	    protected Dialog onCreateDialog(int id) {
	        switch (id) {
	        case DATE_DIALOG_ID:
	            return new DatePickerDialog(this, 
	                        pDateSetListener,
	                        pYear, pMonth, pDay);
	        
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
	    	super.onBackPressed();
	    	
	    	Intent back = new Intent(getApplicationContext(), Dashboard.class);
	    	startActivity(back);
	    }

	    
	    
}
