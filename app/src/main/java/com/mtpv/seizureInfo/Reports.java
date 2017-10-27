package com.mtpv.seizureInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.analogics.thermalAPI.Bluetooth_Printer_3inch_ThermalAPI;
import com.analogics.thermalprinter.AnalogicsThermalPrinter;
import com.mtpv.seizure.R;
import com.mtpv.seizureHelpers.DataBase;
import com.mtpv.seizureHelpers.ServiceHelper;
import com.mtpv.seizureHelpers.WebService;


public class Reports extends Activity {

    EditText regnNo;
    final int PROGRESS_DIALOG = 1;

    Button date_Btn, get_reports_Btn, cancel2_Btn, print_Btn;
    TextView printTv;

    private int pYear;
    private int pMonth;
    private int pDay;
    private int mSecond;
    private int mHour;
    private int mMinute;
    static final int DATE_DIALOG_ID = 0;
    static final int TIME_DIALOG_ID = 1;
    StringBuilder timestamp;

    String PidCode = null; //com.tab39b.MainActivity.PIdCode;
    List<String> duplicateChallan = new ArrayList<String>();
    List<String> textdata = new ArrayList<String>();
    LinkedHashMap<String, String> duplicateChallanMap = new LinkedHashMap<String, String>();

    static String resp;
    String bluetoot_ID;
    String BT_Name;
    String ReportPrint;

    static boolean printFLG = false;

    final WebService WS = new WebService();
    DataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_reports);

        date_Btn = (Button) findViewById(R.id.report_date_btn);
        get_reports_Btn = (Button) findViewById(R.id.get_report_print);
        cancel2_Btn = (Button) findViewById(R.id.cancel_report_print);
        print_Btn = (Button) findViewById(R.id.print_Btn);

        printTv = (TextView) findViewById(R.id.report_text);
        printTv.setText("");
        printFLG = false;
        ReportPrint = null;

        db = new DataBase(getApplicationContext());

        //android.database.sqlite.SQLiteDatabase db = openOrCreateDatabase(DataBase.DATABASE_NAME,MODE_PRIVATE, null);
        //String select_Query = "SELECT  * FROM " + DataBase.Bluetooth;
        /*Cursor res = DBH.Eq(bluetoothQuery);
		res.moveToFirst();
		 while(res.isAfterLast() == false){
			 BT_Name = res.getString(res.getColumnIndex("BT_Name"));		
	         res.moveToNext();
		 }
		 
		 try{
				bluetoot_ID = BT_Name.split("\\r?\\n")[1];
			}catch(Exception e){
				showToast("Please set Bluetooth in setting");
			}*/
        //Cursor cursr = DataBase.db.rawQuery(select_Query, null);
        // looping through all rows and adding to list
		    /*try {
				//db.open();
			
	        if (cursr.moveToFirst()) {
	            do {
	            	
	            	Log.i("1 :",""+ cursr.getString(0));
	            	BT_Name = cursr.getString(0);
	           	 	
	           	 	//et_bt_address.setText(BLT_Name);
	           	 
         		} while (cursr.moveToNext());
	        		}
					db.close(); 
				} catch (Exception e) {
					e.printStackTrace();
			
			}*/


        try {
            android.database.sqlite.SQLiteDatabase db = openOrCreateDatabase(DataBase.DATABASE_NAME, MODE_PRIVATE, null);
            String selectQuery = "SELECT  * FROM " + DataBase.USER_TABLE;
            String select_Query = "SELECT  * FROM " + DataBase.Bluetooth;
            //SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            Cursor cursr = DataBase.db.rawQuery(select_Query, null);
            // looping through all rows and adding to list
			    
			    
		      /*  if (cursor.moveToFirst()) {
		            do {
		            	
		            	//Log.i("1 :",""+ cursor.getString(0));
		           	 	Log.i("2 :",""+cursor.getString(1));
		           	 	Log.i("3 :",""+cursor.getString(2));
		           	 	Log.i("4 :",""+cursor.getString(3));
		           	 	Log.i("5 :",""+cursor.getString(4));
		           	 	Log.i("6 :",""+cursor.getString(5));
		           	 PidCode = cursor.getString(0);
		           	 
		            	 pidCode = cursor.getString(0);
		            	 pidName = cursor.getString(1);
		            	 psCode = cursor.getString(2);
		            	 psName = cursor.getString(3);
		            	 cadreCd = cursor.getString(4);
		            	 cadreName = cursor.getString(5);
		            	 unitCd = cursor.getString(6);
		            	 unitName = cursor.getString(7);
		            	 
		            		} while (cursor.moveToNext());
		        		}*/

            if (cursr.moveToFirst()) {
                do {

                    Log.i("1 :", "" + cursr.getString(0));
                    BT_Name = cursr.getString(0);

                    //et_bt_address.setText(BLT_Name);

                } while (cursr.moveToNext());
            }

            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            showToast("Please check the network");

        }

        final Calendar cal = Calendar.getInstance();
        pYear = cal.get(Calendar.YEAR);
        pMonth = cal.get(Calendar.MONTH);
        pDay = cal.get(Calendar.DAY_OF_MONTH);
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);
        mSecond = cal.get(Calendar.SECOND);
        updateDisplay();

        date_Btn.setOnClickListener(new View.OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                showDialog(DATE_DIALOG_ID);
                //showDialog(TIME_DIALOG_ID);
            }
        });

        get_reports_Btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                printFLG = false;
                new Async_task_getDetails().execute();
            }
        });

        cancel2_Btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        print_Btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (ReportPrint != null && printFLG && !printTv.getText().toString().trim().equals("")) {
                    new Async_Task_PrintData().execute();
                } else {
                    showToast("Please Click on Get Button to Get Reports");
                }

            }
        });
    }

    public class Async_Task_PrintData extends AsyncTask<Void, Void, String> {
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
            String bt = "";
            DataBase helper = new DataBase(getApplicationContext());
            try {
                android.database.sqlite.SQLiteDatabase db = openOrCreateDatabase(DataBase.DATABASE_NAME, MODE_PRIVATE, null);
                String selectQuery = "SELECT  * FROM " + DataBase.Bluetooth;
                //SQLiteDatabase db = this.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);
                // looping through all rows and adding to list

                if (cursor.moveToFirst()) {
                    do {

                        //Log.i("1 :",""+ cursor.getString(0));
                        bt = cursor.getString(0);

                        //et_bt_address.setText(BLT_Name);

                    } while (cursor.moveToNext());
                }
                db.close();
            } catch (Exception e) {
                e.printStackTrace();

            }

            try {
                Bluetooth_Printer_3inch_ThermalAPI preparePrintData = new Bluetooth_Printer_3inch_ThermalAPI();
                String printdata = preparePrintData.font_Courier_36(ReportPrint);
                AnalogicsThermalPrinter printer = new AnalogicsThermalPrinter();

                printer.openBT(bt);
                printer.printData(printdata);
                Thread.sleep(5000);
                printer.closeBT();


                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        showToast("Successfully Printed");
                    }
                });

            } catch (Exception e) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        showToast("Turn on Bluetooth ");
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


    public class Async_task_getDetails extends AsyncTask<Void, Void, String> {
        @SuppressWarnings("deprecation")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub

            final Button Btn = new Button(Reports.this);
            Btn.setText("");

            printFLG = true;

            String offenceDate = date_Btn.getText().toString();

            SharedPreferences sharedPreferences = getSharedPreferences("loginValus", MODE_PRIVATE);
            //23001004|TESTING PURPOSE|2300|TRAFFIC CELL|00|DEVP|7893816681|Y
            PidCode = sharedPreferences.getString("PID_CODE", "");
            //String pidCode = PidCode;

            //Log.i("offenceDate :::", ""+offenceDate);

            //ReportPrint = WS.getReport(offenceDate,pidCode);
            ServiceHelper.reports(offenceDate, PidCode);


            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);
            ReportPrint = ServiceHelper.report_resp;
            if (ServiceHelper.report_resp.contains("NO DATA")&& ServiceHelper.report_resp.equals("NA")&& null==ServiceHelper.report_resp) {
                showToast("No Data Found ......!!");
            } else {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        if (ReportPrint != null) {
                            printFLG = true;
                            printTv.setVisibility(View.VISIBLE);
                            printTv.setText(ReportPrint);
                        }


                    }
                });
            }
        }
    }

    private DatePickerDialog.OnDateSetListener pDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {

                    pYear = year;
                    pMonth = monthOfYear;
                    pDay = dayOfMonth;
                    updateDisplay();
                    // displayToast();
                }

            };

    @SuppressWarnings("unused")
    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay,
                                      int minute) {
                    // TODO Auto-generated method stub
                    mHour = hourOfDay;
                    mMinute = minute;
                    updateDisplay();
                }
            };


    /**
     * Updates the date in the TextView
     */

    @SuppressLint("SimpleDateFormat")
    private void updateDisplay() {

        date_Btn.setText(new StringBuilder()
                .append(pDay).append("/")
                .append(pMonth + 1).append("/")
                .append(pYear).append(" "));

        timestamp = new StringBuilder().append(pDay).append("/")
                .append(pMonth + 1).append("/")
                .append(pYear).append("/")
                .append(mHour).append(":")
                .append(mMinute).append(":")
                .append(mSecond).append(" ");

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
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        pDateSetListener,
                        pYear, pMonth, pDay);

            case PROGRESS_DIALOG:
                ProgressDialog pd = ProgressDialog.show(this, "", "", true);
                pd.setContentView(R.layout.custom_progress_dialog);
                pd.setCancelable(false);

                return pd;

            default:
                break;
        }
        return null;
    }
}

