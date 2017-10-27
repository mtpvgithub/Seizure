package com.mtpv.seizureInfo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mtpv.seizure.BuildConfig;
import com.mtpv.seizure.R;
import com.mtpv.seizureHelpers.DataBase;
import com.mtpv.seizureHelpers.ServiceHelper;
import com.mtpv.seizureHelpers.WebService;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

@SuppressWarnings("deprecation")
@SuppressLint({"NewApi", "SimpleDateFormat", "DefaultLocale"})
public class FootPath_Vendor extends Activity implements LocationListener {
    public static String NAMESPACE = "http://service.mother.com";
    public static String SOAP_ACTION_ID = NAMESPACE + "getDetailsByAADHAR", challanResponse;
    public static String challanGenresp = "";
    public static byte[] image1ByteArray = null;

    public static Button sent_otp;
    public static String gpslattitude = null, gpslongitude = null;
    public static String Opdata_Chalana = null;
    public static String[] aadhar_details;
    private static String METHOD_NAME = "getAADHARData";
    public static String SOAP_ACTION = NAMESPACE + METHOD_NAME;

    public static HashMap<String, String> pointNameMap = new HashMap<String, String>();

    public static HashMap<String, String> psNameMap = new HashMap<String, String>();

    /* DATE & TIME START */
    SimpleDateFormat date_format, date_format2;

    public static StringBuilder strReturnedAddress;
    Calendar calendar;
    int present_date;
    int present_month;
    int present_year;

    int present_hour;
    int present_minutes;

    String present_date_toSend = "";
    StringBuffer present_time_toSend;

    final int PROGRESS_DIALOG = 1;
    String[][] psname_name_code_arr;
    ArrayList<String> ps_codes_fr_names_arr;
    ArrayList<String> ps_names_arr;

    public static EditText Fregnno_ET, shopn_ET, shopl_ET, rname_ET, rfname_ET, rage_ET, radderss_ET, rmobileno_ET,
            ridcardno_ET, w1name_ET, w1fname_ET, w1address_ET;
    public static EditText w2name_ET, w2fname_ET, w2address_ET, shopOWN_ET, shopA_ET, Itemname_ET, qty_ET, amount_ET;
    public static EditText et_location, et_landmark;
    public static Button psname_Btn, pointname_Btn, ridcard_Btn, datepicker_Btn, next_Btn, reset_Btn, add_Btn;
    public static LinearLayout respondent_aadhaar_details_layout, aadhaar_layout;
    public static RadioGroup adr_group;
    public static RadioButton adr_yes, adr_no;
    public static String GHMC_AUTH = null, CONTACT_NO = null, AADHAAR_DATA_FLAG = null, TIN_FLAG = null,
            OTP_NO_FLAG = null, CASHLESS_FLAG = null, MOBILE_NO_FLAG = null, RTA_DATA_FLAG = null, DL_DATA_FLAG = null;
    AutoCompleteTextView autoCompleteTextView;
    Uri imageUri;
    Bitmap photo;
    Bitmap photo1;
    String imageDir = "/storage/emulated/0/DCIM/footpath";
    String imageFileName;
    public static String otp_verify_status = "N";

    static LinearLayout detendLinearLayout;
    LinearLayout HHH;

    ImageButton camera_Btn, off_Btn, gallary_Btn;
    public static ImageView imgv;
    public static ImageView imgv2;

    WebService WS = new WebService();

    HashMap<String, String> psMap;
    HashMap<String, String> ViolationMap;
    HashMap<String, String> AmountMap;
    HashMap<String, String> psPointNameMap;
    private HashMap<String, String> IdProofCardsMap;
    @SuppressWarnings("unused")
    private HashMap<String, String> DitendItemsMap;

    ArrayList<String> selectChB = new ArrayList<String>();
    static ArrayList<String> Ditenditems = new ArrayList<String>();
    static StringBuffer detItems = new StringBuffer();
    static StringBuffer detendItemsA = new StringBuffer();
    SharedPreferences PID_info;
    SharedPreferences EnterDetails;
    SharedPreferences selectedBtn;

    byte[] byteArray;
    public static String imgString, imgString2;
    String orgImageStr;

    @SuppressWarnings("unused")
    private String d, m, y;

    String SPSN = null;
    String SPSPN = null;
    String pscd = null;
    String pointCD = null;
    String IDproofCD = null;
    private String SIDC = null;

    String A_PidCode;
    String A_PidName;
    String A_PsCode;
    String A_PsName;
    String A_CadreCode;
    String A_Cadre;
    String A_SecurityCd;

    // static String PID_Details;
    /* GPS VALUES */
    // flag for GPS status
    boolean isGPSEnabled = false;
    // flag for network status
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    public static String PsCode = null, PsName = null;
    double latitude = 0.0;
    double longitude = 0.0;
    String provider = "";
    LocationManager m_locationlistner;
    android.location.Location location;
    public static String Current_Date;
    static String date;

    private int pYear;
    private int pMonth;
    private int pDay;
    private int mSecond;
    private int mHour;
    private int mMinute;
    static final int DATE_DIALOG_ID = 0;
    static final int TIME_DIALOG_ID = 1;

    StringBuilder timestamp;

    FootPath_Vendor CameraActivity = null;
    @SuppressWarnings("unused")
    private static final int CAMERA_REQUEST = 2500;
    @SuppressWarnings("unused")
    private static final int PICK_IMAGE_REQUEST = 1;
    LinearLayout ll;

    final int PLACES = 0;
    final int PLACES_DETAILS = 1;

    LinearLayout fine_amnt_layout;
    public static EditText fine_amnt_et;

    Button get_details, get_location_details;
    static String items = null;

    public static int fineAmnt = 0, selected_val = 300, multiplied_val = 0;

    private static String SelPicId = "";
    public static boolean imgFLG = false;

    public static String offenceDate, firm_regno, respondentName, shopName, shopOwner, shopAddress, location_tosend,
            idProofCode, idProofNo;

    public static String fineAmount = "300", business_Type = "FOOTPATH";
    CountDownTimer newtimer;

    RadioButton movable, fixed;
    public static String hawkerType = "MOVABLE";

    ArrayList<String> my_points_arr;
    ArrayList<String> my_ps_arr;
    ArrayList<String> my_psCode_arr;
    DataBase db;

    GPSTracker gps;

    String selectedPs_code = null;

    @SuppressWarnings({"unused"})
    @SuppressLint({"NewApi", "SimpleDateFormat", "InflateParams"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_foot_path__vendor);
        ll = new LinearLayout(this);

        latitude = 0.0;
        longitude = 0.0;
        GPSTracker.locationManager = null;

        my_points_arr = new ArrayList<String>();
        my_ps_arr = new ArrayList<String>();
        my_psCode_arr = new ArrayList<String>();

        Drawable edittext_style = (Drawable) getResources().getDrawable(R.drawable.edittext_style);

        et_location = (EditText) findViewById(R.id.et_location);
        et_landmark = (EditText) findViewById(R.id.et_landmark);

        fixed = (RadioButton) findViewById(R.id.fixed);
        movable = (RadioButton) findViewById(R.id.movable);

        et_location.requestFocus();

        get_details = (Button) findViewById(R.id.get_details);

        fine_amnt_layout = (LinearLayout) findViewById(R.id.fine_amnt_layout);

        fine_amnt_layout.setVisibility(View.VISIBLE);

        fine_amnt_et = (EditText) findViewById(R.id.fine_amnt_et);
        fine_amnt_et.setText("" + selected_val);

        get_location_details = (Button) findViewById(R.id.get_location_details);


        newtimer = new CountDownTimer(1000000000, 50) {

            public void onTick(long millisUntilFinished) {
                date = (DateFormat.format("dd/MM/yyyy hh:mm:ss", new java.util.Date()).toString());
                Calendar c1 = Calendar.getInstance();
                int mSec = c1.get(Calendar.MILLISECOND);
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String strdate1 = sdf1.format(c1.getTime());
                date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                // id_date.setText(strdate1);
                Current_Date = strdate1;
            }

            public void onFinish() {

            }
        };
        newtimer.start();

        synchronized (this) {
            getLocation();
        }

        GPSTracker.locationManager = null;
        gps = new GPSTracker(FootPath_Vendor.this);

        // check if GPS enabled
        if (gps.getLocation() != null) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

        } else {

            gps.showSettingsAlert();
        }
        rname_ET = (EditText) findViewById(R.id.rname_ET);
        rfname_ET = (EditText) findViewById(R.id.rfname_ET);
        radderss_ET = (EditText) findViewById(R.id.radderss_ET);
        rmobileno_ET = (EditText) findViewById(R.id.rmobileno_ET);
        ridcardno_ET = (EditText) findViewById(R.id.ridcardno_ET);

        rage_ET = (EditText) findViewById(R.id.rage_ET);
        shopOWN_ET = (EditText) findViewById(R.id.shopOWN_ET);
        // shopA_ET = (EditText)findViewById(R.id.shopA_ET);

        // off_Btn =(ImageButton)findViewById(R.id.off_Btn);

        detendLinearLayout = (LinearLayout) findViewById(R.id.detendL);

        // camera_Btn = (ImageButton)findViewById(R.id.camera_btn);
        imgv = (ImageView) findViewById(R.id.imgv);
        imgv2 = (ImageView) findViewById(R.id.imgv2);
        // gallary_Btn = (ImageButton)findViewById(R.id.gallary_Btn);

        Itemname_ET = (EditText) findViewById(R.id.Itemname_ET);
        qty_ET = (EditText) findViewById(R.id.qty_ET);
        amount_ET = (EditText) findViewById(R.id.amount_ET);
        add_Btn = (Button) findViewById(R.id.add_Btn);

        psname_Btn = (Button) findViewById(R.id.psname_Btn);
        pointname_Btn = (Button) findViewById(R.id.pointname_Btn);
        ridcard_Btn = (Button) findViewById(R.id.ridcard_Btn);
        datepicker_Btn = (Button) findViewById(R.id.datepicker_Btn);

        respondent_aadhaar_details_layout = (LinearLayout) findViewById(R.id.respondent_aadhaar_details_layout);
        aadhaar_layout = (LinearLayout) findViewById(R.id.aadhaar_layout);
        adr_group = (RadioGroup) findViewById(R.id.adr_group);
        adr_yes = (RadioButton) findViewById(R.id.adr_yes);
        adr_no = (RadioButton) findViewById(R.id.adr_no);

        pointname_Btn.setText("Select Point Name");

        respondent_aadhaar_details_layout.setVisibility(View.GONE);

        new Async_task_GetPsName().execute();

        SharedPreferences sharedPreferences = getSharedPreferences("loginValus", MODE_PRIVATE);
        // 23001004|TESTING PURPOSE|2300|TRAFFIC CELL|00|DEVP|7893816681|Y
        String userName = sharedPreferences.getString("PS_NAME", "");

        A_PidCode = sharedPreferences.getString("PID_CODE", "");
        A_PidName = sharedPreferences.getString("PID_NAME", "");
        A_PsCode = sharedPreferences.getString("PS_CODE", "");
        A_PsName = sharedPreferences.getString("Ps_NAME", "");
        A_CadreCode = sharedPreferences.getString("CADRE_CODE", "");
        A_Cadre = sharedPreferences.getString("CADRE_NAME", "");
        A_SecurityCd = sharedPreferences.getString("SECURITY_CD", "");
        GHMC_AUTH = sharedPreferences.getString("GHMC_AUTH", "");
        CONTACT_NO = sharedPreferences.getString("CONTACT_NO", "");
        AADHAAR_DATA_FLAG = sharedPreferences.getString("AADHAAR_DATA_FLAG", "");
        TIN_FLAG = sharedPreferences.getString("TIN_FLAG", "");
        OTP_NO_FLAG = sharedPreferences.getString("OTP_NO_FLAG", "");
        CASHLESS_FLAG = sharedPreferences.getString("CASHLESS_FLAG", "");
        MOBILE_NO_FLAG = sharedPreferences.getString("MOBILE_NO_FLAG", "");
        RTA_DATA_FLAG = sharedPreferences.getString("RTA_DATA_FLAG", "");
        DL_DATA_FLAG = sharedPreferences.getString("DL_DATA_FLAG", "");

        adr_yes.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if ("Y".equals(Dashboard.OtpStatus)) {

                    sent_otp.setVisibility(View.VISIBLE);
                } else if ("N".equals(Dashboard.OtpStatus)) {
                    sent_otp.setVisibility(View.GONE);
                }

                gps = new GPSTracker(FootPath_Vendor.this);

                // check if GPS enabled
                if (gps.getLocation() != null) {

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                } else {

                    gps.showSettingsAlert();

                }

                aadhaar_layout.setVisibility(View.VISIBLE);
                respondent_aadhaar_details_layout.setVisibility(View.GONE);

                rname_ET.setText("");
                ridcardno_ET.setText("");
                rfname_ET.setText("");
                radderss_ET.setText("");
                rmobileno_ET.setText("");
                rage_ET.setText("");
            }
        });

        adr_no.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if ("Y".equals(Dashboard.OtpStatus)) {
                    sent_otp.setVisibility(View.VISIBLE);
                } else if ("N".equals(Dashboard.OtpStatus)) {
                    sent_otp.setVisibility(View.GONE);
                }

                gps = new GPSTracker(FootPath_Vendor.this);

                if (gps.getLocation() != null) {

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                } else {
                    gps.showSettingsAlert();

                }
                respondent_aadhaar_details_layout.setVisibility(View.VISIBLE);
                aadhaar_layout.setVisibility(View.GONE);

                ridcardno_ET.setText("");
                rname_ET.setText("");
                rfname_ET.setText("");
                radderss_ET.setText("");
                rmobileno_ET.setText("");
                rage_ET.setText("");

                ridcardno_ET.setEnabled(true);
                rname_ET.setEnabled(true);
                rfname_ET.setEnabled(true);
                radderss_ET.setEnabled(true);
                rmobileno_ET.setEnabled(true);
                rage_ET.setEnabled(true);
            }
        });

        psname_Btn.setText("" + userName);

        reset_Btn = (Button) findViewById(R.id.reset_Btn);
        next_Btn = (Button) findViewById(R.id.print_Btn);

        sent_otp = (Button) findViewById(R.id.send_otp);

        if ("Y".equals(Dashboard.OtpStatus)) {

            sent_otp.setVisibility(View.VISIBLE);
        } else if ("N".equals(Dashboard.OtpStatus)) {
            sent_otp.setVisibility(View.GONE);
        }

        fixed.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                gps = new GPSTracker(FootPath_Vendor.this);

                // check if GPS enabled
                if (gps.getLocation() != null) {

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                } else {
                    gps.showSettingsAlert();

                }
                // TODO Auto-generated method stub
                hawkerType = "FIXED";
            }
        });

        movable.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                gps = new GPSTracker(FootPath_Vendor.this);

                // check if GPS enabled
                if (gps.getLocation() != null) {

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                } else {
                    gps.showSettingsAlert();

                }
                hawkerType = "MOVABLE";
            }
        });

        rname_ET.setEnabled(true);
        rfname_ET.setEnabled(true);
        radderss_ET.setEnabled(true);
        rmobileno_ET.setEnabled(true);
        rage_ET.setEnabled(true);

        imgv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                gps = new GPSTracker(FootPath_Vendor.this);

                // check if GPS enabled
                if (gps.getLocation() != null && latitude != 0.0 && longitude != 0.0) {

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                    FootPath_Vendor.SelPicId = "1";
                    selectImage();

                } else {
                    gps.showSettingsAlert();

                }
                /*
                 * FootPath_Vendor.SelPicId = "1"; selectImage();
				 */

            }
        });

        imgv2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                gps = new GPSTracker(FootPath_Vendor.this);

                // check if GPS enabled
                if (gps.getLocation() != null && latitude != 0.0 && longitude != 0.0) {

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                    FootPath_Vendor.SelPicId = "2";
                    selectImage();
                } else {

                    gps.showSettingsAlert();

                }

            }
        });


        /***************** GPS FUNCTIONALITY ******************/

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Toast.makeText(this, "GPS is Enabled in your devide",
            // Toast.LENGTH_SHORT).show();
        } else {
            showGPSDisabledAlertToUser();
        }
        /***************** GPS FUNCTIONALITY ******************/

        get_location_details.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                et_location.setText("");
                et_landmark.setText("");
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    new Async_GetGPS_Address().execute();
                } else {
                    showGPSDisabledAlertToUser();
                }

            }
        });

        get_details.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                VerhoeffCheckDigit ver = new VerhoeffCheckDigit();
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    if (!isOnline()) {
                        showToast("Please check the network connection!");
                    } else {
                        if (ridcardno_ET.getText().toString().trim() == null
                                && ridcardno_ET.getText().toString().trim().length() > 4
                                && ridcardno_ET.getText().toString().trim().length() != 12
                                && !ver.isValid(ridcardno_ET.getText().toString().trim())) {
                            ridcardno_ET
                                    .setError(Html.fromHtml("<font color='white'>Please Enter Valid Aadhaar</font>"));
                            ridcardno_ET.requestFocus();
                        } else if (ridcardno_ET.getText().toString().trim() != null
                                && ridcardno_ET.getText().toString().trim().length() > 4
                                && ridcardno_ET.getText().toString().trim().length() == 12
                                && !ver.isValid(ridcardno_ET.getText().toString().trim())) {
                            ridcardno_ET
                                    .setError(Html.fromHtml("<font color='white'>Please Enter Valid Aadhaar</font>"));
                            ridcardno_ET.requestFocus();
                        } else if (ridcardno_ET.getText().toString().trim() != null
                                && ridcardno_ET.getText().toString().trim().length() != 12
                                && !ver.isValid(ridcardno_ET.getText().toString().trim())) {

                            ridcardno_ET
                                    .setError(Html.fromHtml("<font color='white'>Please Enter Valid Aadhaar</font>"));
                            ridcardno_ET.requestFocus();

                        } else if (ridcardno_ET.getText().toString().trim() != null
                                && ridcardno_ET.getText().toString().trim().length() == 12
                                && ver.isValid(ridcardno_ET.getText().toString().trim())) {

                            // new Async_GetGPS_Address().execute();
                            if ("Y".equals(AADHAAR_DATA_FLAG)) {
                                new Async_getDetails().execute();
                            } else {
                                respondent_aadhaar_details_layout.setVisibility(View.VISIBLE);
                            }

                            if (isOnline()) {

                                rname_ET.setEnabled(true);
                                rfname_ET.setEnabled(true);
                                rage_ET.setEnabled(true);
                                radderss_ET.setEnabled(true);
                                rmobileno_ET.setEnabled(true);

                                rname_ET.setText("");
                                rfname_ET.setText("");
                                rage_ET.setText("");
                                radderss_ET.setText("");
                                rmobileno_ET.setText("");

                                if ("Y".equals(AADHAAR_DATA_FLAG)) {
                                    new AsyncGetIDDetails().execute();
                                } else {
                                    respondent_aadhaar_details_layout.setVisibility(View.VISIBLE);
                                }

                            } else {
                                showToast("Please Enable Data Connection!!");
                            }

                        }
                    }
                } else {
                    showGPSDisabledAlertToUser();
                }
            }
        });

        sent_otp.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String tempContactNumber = rmobileno_ET.getText().toString();

                if (tempContactNumber.equals("")) {
                    rmobileno_ET
                            .setError(Html.fromHtml("<font color='black'>Enter mobile number to send OTP!!</font>"));
                    rmobileno_ET.requestFocus();
                } else if (tempContactNumber.trim() != null && tempContactNumber.trim().length() > 1
                        && tempContactNumber.trim().length() != 10) {
                    rmobileno_ET.setError(Html.fromHtml("<font color='black'>Enter Valid mobile number!!</font>"));
                    rmobileno_ET.requestFocus();
                } else if (tempContactNumber.length() == 10) {
                    if ((tempContactNumber.charAt(0) == '7') || (tempContactNumber.charAt(0) == '8')
                            || (tempContactNumber.charAt(0) == '9')) {
                        if (isNetworkEnabled) {
                            // otp_status = "send";

                            new Async_sendOTP_to_mobile().execute();

                        } else {
                            showToast("Please check your network connection!");
                        }
                    } else {
                        rmobileno_ET.setError(Html.fromHtml("<font color='black'>Check Contact No.!!</font>"));
                        rmobileno_ET.requestFocus();
                    }
                } else if (tempContactNumber.length() == 11) {
                    if (tempContactNumber.charAt(0) == '0') {
                        if (isNetworkEnabled) {
                            new Async_sendOTP_to_mobile().execute();
                        } else {
                            showToast("Please check your network connection!");
                        }
                    } else {
                        rmobileno_ET.setError(Html.fromHtml("<font color='black'>Check Contact No.!!</font>"));
                        rmobileno_ET.requestFocus();
                    }
                }

            }
        });
        /***************************************************************
         * Gallary Button
         *********************************************************/
		/*
		 * gallary_Btn.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub
		 * 
		 * Intent intent = new Intent(); // Show only images, no videos or
		 * anything else intent.setType("image/*");
		 * intent.setAction(Intent.ACTION_GET_CONTENT); // Always show the
		 * chooser (if there are multiple options available)
		 * startActivityForResult(Intent.createChooser(intent,
		 * "Select Picture"), PICK_IMAGE_REQUEST);
		 * 
		 * //Toast.makeText(getApplicationContext(), "Gallary Button",
		 * Toast.LENGTH_LONG).show(); } });
		 */

        /***************************************************************
         * Camera Button
         *********************************************************/

		/*
		 * camera_Btn.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub
		 * 
		 * Intent cameraIntent = new
		 * Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); File myDir
		 * = new File(imageDir); if(!myDir.exists()) myDir.mkdirs(); Random
		 * generator = new Random(); int n = 100000; n = generator.nextInt(n);
		 * 
		 * imageFileName = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new
		 * Date())+"_Image-"+n+".jpg"; Uri imgStUri = Uri.fromFile(new
		 * File(myDir.getAbsolutePath()+"/"+imageFileName));
		 * 
		 * //Toast.makeText(getApplicationContext(),
		 * "File Storage : "+myDir.getAbsolutePath(), Toast.LENGTH_LONG).show();
		 * 
		 * cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgStUri);
		 * startActivityForResult(cameraIntent, CAMERA_REQUEST);
		 * 
		 * } });
		 */

        /*************************************************************************
         * DATE Picker Button
         ************************************/
        final Calendar cal = Calendar.getInstance();

        pYear = cal.get(Calendar.YEAR);
        pMonth = cal.get(Calendar.MONTH);
        pDay = cal.get(Calendar.DAY_OF_MONTH);
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);
        mSecond = cal.get(Calendar.SECOND);
        updateDisplay();

        datepicker_Btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDialog(DATE_DIALOG_ID);

            }
        });

        String PsNameMaster = "select * from PsNameMaster";
        System.out.println("query:" + PsNameMaster);
        final ArrayList<String> PsNameMasterList = new ArrayList<String>();

        psMap = new HashMap<String, String>();

		/*
		 * DBH = new DataBase(getApplicationContext()); try { Cursor res1 =
		 * DBH.Eq(PsNameMaster); res1.moveToFirst(); while(res1.isAfterLast() ==
		 * false){
		 * PsNameMasterList.add(res1.getString(res1.getColumnIndex("PsName")));
		 * 
		 * psMap.put(res1.getString(res1.getColumnIndex("PsName")),
		 * res1.getString(res1.getColumnIndex("PsCode")));
		 * 
		 * res1.moveToNext(); } } catch (Exception e) { // TODO: handle
		 * exception e.printStackTrace(); }
		 */

        psname_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView title = new TextView(FootPath_Vendor.this);
                title.setText("Select PS Name");
                title.setBackgroundColor(Color.parseColor("#007300"));
                title.setGravity(Gravity.CENTER);
                title.setTextColor(Color.WHITE);
                title.setTextSize(26);
                title.setTypeface(title.getTypeface(), Typeface.BOLD);
                title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
                title.setPadding(20, 0, 20, 0);
                title.setHeight(70);

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(FootPath_Vendor.this);
                builderSingle.setCustomTitle(title);
                builderSingle.setItems(my_ps_arr.toArray(new CharSequence[my_ps_arr.size()]),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                psname_Btn.setText(my_ps_arr.get(which));
                                SPSN = psname_Btn.getText().toString();
                                pointname_Btn.setText("Select Point Name");
                                // Log.e("slected ps name :: ",SPSN);

                            }

                        });

                builderSingle.show().getWindow().setLayout(500, 600);
            }
        });

        pointname_Btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new Async_task_GetPointNames().execute();

            }
        });

        ridcard_Btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // idBtnFLG = true ;
				/*
				 * rname_ET.setText(""); rfname_ET.setText("");
				 * radderss_ET.setText(""); rmobileno_ET.setText("");
				 * rage_ET.setText("");
				 */

                ridcardno_ET.requestFocus();
                if (!isOnline()) {
                    // do something
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FootPath_Vendor.this);
                    alertDialogBuilder.setMessage("Please Enable Internet Connection").setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    AlertDialog alert = alertDialogBuilder.create();
                    alert.show();
                } else {
                    VerhoeffCheckDigit ver = new VerhoeffCheckDigit();

                    if (ridcardno_ET.getText() != null && ridcardno_ET.getText().toString().trim().length() >= 1
                            && (!ver.isValid(ridcardno_ET.getText().toString()))) {
                        showToast("Please Enter Valid Adhaar Number");
                    } else {
                        new Asyn_task_idCard().execute();
                    }
                }

            }
        });

        add_Btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (Itemname_ET.getText().toString().equals("")) {
                    Itemname_ET.setError(Html.fromHtml("<font color='black'>Please Enter Item Name</font>"));
                    Itemname_ET.requestFocus();
                } else if (qty_ET.getText().toString().equals("")) {
                    qty_ET.setError(Html.fromHtml("<font color='black'>Please Enetr Quantity</font>"));
                    qty_ET.requestFocus();
                } /*
					 * else if (amount_ET.getText().toString().equals("")) {
					 * amount_ET.setError(Html.
					 * fromHtml("<font color='black'>Please Enetr Amount</font>"
					 * )); amount_ET.requestFocus(); }
					 */ else {

                    LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View addView = layoutInflater.inflate(R.layout.row, null);
                    final TextView textOut = (TextView) addView.findViewById(R.id.textout);
                    final TextView qty = (TextView) addView.findViewById(R.id.qty);
                    final TextView amount = (TextView) addView.findViewById(R.id.amnt);
                    textOut.setText(Itemname_ET.getText().toString().toUpperCase());
                    qty.setText(qty_ET.getText().toString());
                    // amount.setText(""+selected_val);
                    // items = textOut.getText().toString()
                    // +":"+qty.getText().toString()+":"+amount.getText().toString();
                    items = textOut.getText().toString().toUpperCase() + ":" + qty.getText().toString();
                    Ditenditems.add(items);
                    Log.e("DitendItems", items);
                    Itemname_ET.setText("");
                    Itemname_ET.requestFocus();
                    qty_ET.setText("");
                    // amount_ET.setText("");

                    detendLinearLayout.addView(addView);

                    Button buttonRemove = (Button) addView.findViewById(R.id.remove);

                    buttonRemove.setOnClickListener(new OnClickListener() {

                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            ((LinearLayout) addView.getParent()).removeView(addView);
                            Ditenditems.remove(items);
                        }
                    });
                }
            }
        });

        next_Btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                gps = new GPSTracker(FootPath_Vendor.this);

                // check if GPS enabled
                if (gps.getLocation() != null) {

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                } else {
                    gps.showSettingsAlert();

                }

                String tempContactNumber = rmobileno_ET.getText().toString().trim();

                if (psname_Btn.getText().toString().trim() == null
                        || psname_Btn.getText().toString().equals("Select PS Name")) {
                    showToast("Please Select PS Name");
                } else if (pointname_Btn.getText().toString().trim() == null
                        || pointname_Btn.getText().toString().equals("Select Point Name")) {
                    showToast("Please Select Point Name");
                } else if (rname_ET.getText().toString().trim() == null
                        || rname_ET.getText().toString().trim().equals("")) {
                    rname_ET.setError(Html.fromHtml("<font color='white'>Please Enter Respondant Name</font>"));
                    rname_ET.requestFocus();
                } else if (rfname_ET.getText().toString().trim() == null
                        || rfname_ET.getText().toString().trim().equals("")) {
                    rfname_ET.setError(Html.fromHtml("<font color='white'>Please Enter Respondant Father Name</font>"));
                    rfname_ET.requestFocus();
                } else if (rage_ET.getText().toString().trim() == null
                        || rage_ET.getText().toString().trim().equals("")) {
                    rage_ET.setError(Html.fromHtml("<font color='white'>Please Enter Respondent Age</font>"));
                    rage_ET.requestFocus();
                } else if (radderss_ET.getText().toString().trim() == null
                        || radderss_ET.getText().toString().trim().equals("")) {
                    radderss_ET.setError(Html.fromHtml("<font color='white'>Please Enter Respondent Address</font>"));
                    radderss_ET.requestFocus();
                } /*
					 * else if (ridcardno_ET.getText().toString().trim()==null
					 * || ridcardno_ET.getText().toString().trim().equals("")) {
					 * ridcardno_ET.setError(Html.
					 * fromHtml("<font color='black'>Please Enter Respondent ID Proof</font>"
					 * )); ridcardno_ET.requestFocus(); }
					 */ else if (rmobileno_ET.getText().toString().trim() == null
                        || rmobileno_ET.getText().toString().trim().equals("")) {
                    rmobileno_ET
                            .setError(Html.fromHtml("<font color='white'>Please Enter Respondent Mobile No</font>"));
                    rmobileno_ET.requestFocus();
                } else if (imgString == null && imgv.getDrawable().getConstantState() == getResources()
                        .getDrawable(R.drawable.photo).getConstantState()) {
                    showToast("Please Select Encroachment Image");
                } else if (imgString2 == null && imgv2.getDrawable().getConstantState() == getResources()
                        .getDrawable(R.drawable.photo).getConstantState()) {
                    showToast("Please Select After Removal Encroachment Image");
                } /*
					 * else if (imgv2.getDrawable().getConstantState() ==
					 * getResources().getDrawable(R.drawable.photo).
					 * getConstantState()) {
					 * showToast("Please Select After Removal Encroachment Image"
					 * ); }
					 */ else if (Ditenditems.isEmpty()) {
                    showToast("Please Click Add to Detain Items");
                } else if (tempContactNumber.trim() != null && tempContactNumber.trim().length() > 1
                        && tempContactNumber.trim().length() != 10) {
                    rmobileno_ET.setError(Html.fromHtml("<font color='white'>Enter Valid mobile number!!</font>"));
                    rmobileno_ET.requestFocus();
                } else if (tempContactNumber.length() == 10) {
                    if ((tempContactNumber.charAt(0) == '7') || (tempContactNumber.charAt(0) == '8')
                            || (tempContactNumber.charAt(0) == '9')) {

                        if (Dashboard.OtpStatus.equalsIgnoreCase("Y") && !FootPath_Vendor.otp_verify_status.equalsIgnoreCase("Y")) {


                            showToast("Please Verify Otp");
                        } else {
                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            String strDate = sdf.format(c.getTime());

                            // Log.i("strDate ", ""+ strDate);

                            String Fregnno = "";
                            String ShopName = "";
                            String ShopLocation = "";
                            String ShopOwnerName = "";
                            String ShopAddress = "";
                            String Location = et_location.getText().toString();
                            String Landmark = et_landmark.getText().toString();
                            String Date = datepicker_Btn.getText().toString();
                            String PSName = psname_Btn.getText().toString();
                            String PsPointName = pointname_Btn.getText().toString();
                            String RespName = rname_ET.getText().toString();
                            String RespFName = rfname_ET.getText().toString();
                            String RespAge = rage_ET.getText().toString();
                            String Respaddress = radderss_ET.getText().toString();
                            String RespMobileNo = rmobileno_ET.getText().toString();

                            String RespIdCard = ridcard_Btn.getText().toString();
                            String RespIdcarvalue = ridcardno_ET.getText().toString();
                            String WitnessName1 = "";
                            String WitnessFName1 = "";
                            String WitnessAddress1 = "";
                            String WitnessName2 = "";
                            String WitnessFName2 = "";
                            String WitnessAddress2 = "";

                            ArrayList<String> Items = Ditenditems;

                            for (int i = 0; i < Items.size(); i++) {
                                detendItemsA.append(Items.get(i));
                                detendItemsA.append(", ");
                            }
                            System.out.println("Ditenditems:" + Ditenditems);

                            for (int i = 0; i < Items.size(); i++) {
                                detItems.append(Items.get(i));
                                detItems.append("@");
                            }

                            System.out.println("checkedlist:" + selectChB);
                            Intent dis = new Intent(FootPath_Vendor.this, FootPath_Preview.class);//

                            dis.putExtra("Fregnno", "");
                            dis.putExtra("ShopName", "");
                            dis.putExtra("ShopLocation", "");
                            dis.putExtra("Location", Location);
                            dis.putExtra("Landmark", Landmark);

                            dis.putExtra("Date", Date);
                            dis.putExtra("Date", Date);
                            dis.putExtra("PSName", PSName);
                            dis.putExtra("PsPointName", PsPointName);
                            dis.putExtra("RespName", RespName);
                            dis.putExtra("RespFName", RespFName);
                            dis.putExtra("RespAge", RespAge);
                            dis.putExtra("Respaddress", Respaddress);
                            dis.putExtra("RespMobileNo", RespMobileNo);
                            dis.putExtra("RespIdCard", RespIdCard);
                            dis.putExtra("RespIdcarvalue", RespIdcarvalue);
                            dis.putExtra("WitnessName1", "");
                            dis.putExtra("WitnessFName1", "");
                            dis.putExtra("WitnessAddress1", "");
                            dis.putExtra("WitnessName2", "");
                            dis.putExtra("WitnessFName2", "");
                            dis.putExtra("WitnessAddress2", "");
                            dis.putExtra("detItems", detItems.toString());
                            dis.putExtra("detendItemsA", detendItemsA.toString());

                            dis.putExtra("hawkerType", hawkerType);
                            dis.putExtra("BussinessType", business_Type);


                            SharedPreferences sharedPreference = PreferenceManager
                                    .getDefaultSharedPreferences(getApplicationContext());
                            SharedPreferences.Editor edit = sharedPreference.edit();

                            try {
                                edit.putString("picture", imgString);
                            } catch (Exception e) {
                                System.out.println("imgString ::" + e);
                            }

                            try {
                                edit.putString("tinPicture", imgString2);
                            } catch (Exception e) {
                                System.out.println("imgString2 ::" + e);
                            }

                            edit.apply();
                            dis.putExtra("pscd", selectedPs_code);
                            dis.putExtra("pointCD", pointCD);
                            dis.putExtra("IDproofCD", IDproofCD);

                            dis.putExtra("A_PidCode", A_PidCode);
                            dis.putExtra("A_PidName", A_PidName);
                            dis.putExtra("A_PsCode", A_PsCode);
                            dis.putExtra("A_PsName", A_PsName);
                            dis.putExtra("A_CadreCode", A_CadreCode);
                            dis.putExtra("A_Cadre", A_Cadre);
                            dis.putExtra("A_SecurityCd", A_SecurityCd);

                            dis.putExtra("timestamp", strDate);

                            startActivity(dis);

                            Items.clear();
                        }

                    } /*
						 * else if (Shop_vendor.otp_verify_status == "N") {
						 * showToast("Please Verify OTP !!!"); } }
						 */ else {
                        rmobileno_ET.setError(Html.fromHtml("<font color='white'>Check Contact No.!!</font>"));
                        rmobileno_ET.requestFocus();
                    }

                } else {
                    rmobileno_ET.setError(Html.fromHtml("<font color='white'>Check Contact No.!!</font>"));
                    rmobileno_ET.requestFocus();

                }
                    /*{
                    Shop_vendor.otp_verify_status = "Y";
                    if (Shop_vendor.otp_verify_status == "Y") {
                        // if (Shop_vendor.otp_verify_status == "Y") {

                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        String strDate = sdf.format(c.getTime());

                        // Log.i("strDate ", ""+ strDate);

                        String Fregnno = "";
                        String ShopName = "";
                        String ShopLocation = "";
                        String ShopOwnerName = "";
                        String ShopAddress = "";
                        String Location = et_location.getText().toString();
                        String Landmark = et_landmark.getText().toString();
                        String Date = datepicker_Btn.getText().toString();
                        String PSName = psname_Btn.getText().toString();
                        String PsPointName = pointname_Btn.getText().toString();
                        String RespName = rname_ET.getText().toString();
                        String RespFName = rfname_ET.getText().toString();
                        String RespAge = rage_ET.getText().toString();
                        String Respaddress = radderss_ET.getText().toString();
                        String RespMobileNo = rmobileno_ET.getText().toString();

                        String RespIdCard = ridcard_Btn.getText().toString();
                        String RespIdcarvalue = ridcardno_ET.getText().toString();
                        String WitnessName1 = "";
                        String WitnessFName1 = "";
                        String WitnessAddress1 = "";
                        String WitnessName2 = "";
                        String WitnessFName2 = "";
                        String WitnessAddress2 = "";

                        ArrayList<String> Items = Ditenditems;

                        for (int i = 0; i < Items.size(); i++) {
                            detendItemsA.append(Items.get(i));
                            detendItemsA.append(", ");
                        }
                        System.out.println("Ditenditems:" + Ditenditems);

                        for (int i = 0; i < Items.size(); i++) {
                            detItems.append(Items.get(i));
                            detItems.append("@");
                        }

                        System.out.println("checkedlist:" + selectChB);

                        // Toast.makeText(getApplicationContext(), "Please
                        // wait", Toast.LENGTH_LONG).show();
                        showToast("Please wait...!");

                        Log.e("detItem test", detItems.toString());

                        Intent dis = new Intent(FootPath_Vendor.this, FootPath_Preview.class);//

                        dis.putExtra("Fregnno", "");
                        dis.putExtra("ShopName", "");
                        dis.putExtra("ShopLocation", "");
                        dis.putExtra("Location", Location);
                        dis.putExtra("Landmark", Landmark);

                        dis.putExtra("Date", Date);
                        dis.putExtra("Date", Date);
                        dis.putExtra("PSName", PSName);
                        dis.putExtra("PsPointName", PsPointName);
                        dis.putExtra("RespName", RespName);
                        dis.putExtra("RespFName", RespFName);
                        dis.putExtra("RespAge", RespAge);
                        dis.putExtra("Respaddress", Respaddress);
                        dis.putExtra("RespMobileNo", RespMobileNo);
                        dis.putExtra("RespIdCard", RespIdCard);
                        dis.putExtra("RespIdcarvalue", RespIdcarvalue);
                        dis.putExtra("WitnessName1", "");
                        dis.putExtra("WitnessFName1", "");
                        dis.putExtra("WitnessAddress1", "");
                        dis.putExtra("WitnessName2", "");
                        dis.putExtra("WitnessFName2", "");
                        dis.putExtra("WitnessAddress2", "");
                        dis.putExtra("detItems", detItems.toString());
                        dis.putExtra("detendItemsA", detendItemsA.toString());

                        dis.putExtra("hawkerType", hawkerType);
                        dis.putExtra("BussinessType", business_Type);

                        Log.e("detItem test", detItems.toString());

                        SharedPreferences sharedPreference = PreferenceManager
                                .getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor edit = sharedPreference.edit();

                        try {
                            edit.putString("picture", imgString);
                        } catch (Exception e) {
                            System.out.println("imgString ::" + e);
                        }

                        try {
                            edit.putString("tinPicture", imgString2);
                        } catch (Exception e) {
                            System.out.println("imgString2 ::" + e);
                        }

                        edit.commit();
                        dis.putExtra("pscd", selectedPs_code);
                        dis.putExtra("pointCD", pointCD);
                        dis.putExtra("IDproofCD", IDproofCD);

                        dis.putExtra("A_PidCode", A_PidCode);
                        dis.putExtra("A_PidName", A_PidName);
                        dis.putExtra("A_PsCode", A_PsCode);
                        dis.putExtra("A_PsName", A_PsName);
                        dis.putExtra("A_CadreCode", A_CadreCode);
                        dis.putExtra("A_Cadre", A_Cadre);
                        dis.putExtra("A_SecurityCd", A_SecurityCd);

                        dis.putExtra("timestamp", strDate);

                        startActivity(dis);

                        Items.clear();

                    } else if (Shop_vendor.otp_verify_status == "N") {
                        showToast("Please Verify OTP !!!");
                    }
                }*/
            }
        });

        /*************************************************************************
         * reset Button
         ***********************/

        reset_Btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                gps = new GPSTracker(FootPath_Vendor.this);

                // check if GPS enabled
                if (gps.getLocation() != null) {

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                } else {
                    gps.showSettingsAlert();

                }

                imgv.setImageDrawable(getResources().getDrawable(R.drawable.photo));
                imgv2.setImageDrawable(getResources().getDrawable(R.drawable.tin));

                imgString = null;
                imgString2 = null;

                et_landmark.setText("");
                et_landmark.requestFocus();
                et_location.requestFocus();

                et_location.setText("");

                ridcard_Btn.setText("");
                rname_ET.setText("");
                rfname_ET.setText("");
                radderss_ET.setText("");
                rmobileno_ET.setText("");
                ridcardno_ET.setText("");
                rage_ET.setText("");

                fine_amnt_layout.setVisibility(View.VISIBLE);
                fine_amnt_et.setText("RS. " + selected_val);

                // psname_Btn.setText("");
                pointname_Btn.setText("");

                rname_ET.setEnabled(true);
                rfname_ET.setEnabled(true);
                radderss_ET.setEnabled(true);
                rmobileno_ET.setEnabled(true);
                rage_ET.setEnabled(true);

                FootPath_Vendor.detItems.setLength(0);

                FootPath_Vendor.detItems.setLength(0);
                FootPath_Vendor.Ditenditems.clear();
                FootPath_Vendor.detendItemsA.setLength(0);
                FootPath_Vendor.detendItemsA.delete(0, FootPath_Vendor.detendItemsA.length());
                // Log.i("FootPath_Vendor.detendItemsA :::",
                // ""+FootPath_Vendor.detendItemsA);

                FootPath_Vendor.Itemname_ET.setText("");
                FootPath_Vendor.qty_ET.setText("");
                FootPath_Vendor.amount_ET.setText("");

                FootPath_Vendor.items = "";

                FootPath_Vendor.detendLinearLayout.removeAllViews();
                pointname_Btn.setText("Select Point Name");
            }
        });
    }


    private void EnableGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setMessage(
                        "    GPS is unable to take Lattitude and Longitude Values \n            Please Turn OFF and Turn ON GPS?")
                .setCancelable(false).setPositiveButton("Goto Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent = new Intent(
                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(callGPSSettingIntent);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("    GPS is Disabled in your Device \n            Please Enable GPS?")
                .setCancelable(false).setPositiveButton("Goto Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent = new Intent(
                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(callGPSSettingIntent);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @SuppressWarnings("unused")
    private void getLocation() {
        try {
            m_locationlistner = (LocationManager) this.getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = m_locationlistner.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = m_locationlistner.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                latitude = 0.0;
                longitude = 0.0;
            } else {
                this.canGetLocation = true;
                String current_time;
                String current_date = null;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    m_locationlistner.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (m_locationlistner != null) {
                        location = m_locationlistner.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                            long time = location.getTime();
                            Date date = new Date(time);

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String gps_Date = sdf.format(date);
                            System.out.println(gps_Date);

                        } else {
                            latitude = 0.0;
                            longitude = 0.0;
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        m_locationlistner.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (m_locationlistner != null) {
                            location = m_locationlistner.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();

                                long time = location.getTime();
                                Date date = new Date(time);

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String gps_Date = sdf.format(date);
                                System.out.println(gps_Date);

                                // new Async_GetGPS_Address().execute();

                            } else {
                                latitude = 0.0;
                                longitude = 0.0;
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

    }

    public static String getDate() {
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        System.out.println(today.month);
        return today.monthDay + "-" + getMonthName(today.month) + "-" + today.year;
    }

    public static String getMonthName(int month) {
        switch (month + 1) {
            case 1:
                return "Jan";

            case 2:
                return "Feb";

            case 3:
                return "Mar";

            case 4:
                return "Apr";

            case 5:
                return "May";

            case 6:
                return "Jun";

            case 7:
                return "Jul";

            case 8:
                return "Aug";

            case 9:
                return "Sep";

            case 10:
                return "Oct";

            case 11:
                return "Nov";

            case 12:
                return "Dec";
        }

        return "";
    }

    public class Async_sendOTP_to_mobile extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub

            date_format2 = new SimpleDateFormat("dd-MMM-yyyy");

            // Log.i("date_format2", ""+ date_format2);
            // Log.i("getDate().toUpperCase()", ""+ getDate().toUpperCase());

            present_date_toSend = date_format2.format(new Date(present_year - 1900, present_month, present_date));

            Dashboard.class_clarify = "footpathclass";

            ServiceHelper.sendOTPtoMobile(rmobileno_ET.getText().toString().trim(), "" + getDate().toUpperCase());

            return null;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);

            if (ServiceHelper.otp_Send_resp != null) {
                if ((!ServiceHelper.otp_Send_resp.equals("0") || !ServiceHelper.otp_Send_resp.equals("")
                        || !ServiceHelper.otp_Send_resp.equals("NA")
                        || !ServiceHelper.otp_Send_resp.equals("anyType{}"))) {

                    SharedPreferences sharedPreference = PreferenceManager
                            .getDefaultSharedPreferences(getApplicationContext());

                    SharedPreferences.Editor editor = sharedPreference.edit();

                    editor.putString("OTP_Num", "" + ServiceHelper.otp_Send_resp);
                    editor.putString("OTP_DATE", "" + getDate().toUpperCase());
                    editor.putString("MobileNo", "" + rmobileno_ET.getText().toString().trim());

                    editor.apply();

                    Intent verify = new Intent(FootPath_Vendor.this, OTP_input.class);
                    startActivity(verify);

                }
            } else {
                showToast("Please try Again !!!");
            }
        }
    }

    protected void selectImage() {
        // TODO Auto-generated method stub
        if (FootPath_Vendor.SelPicId.equals("1")) {
            final CharSequence[] options = {"Open Camera", "Choose from Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(FootPath_Vendor.this);
            builder.setTitle("Add Photo!");
            builder.setItems(options, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int item) {
                   /* FileProvider.getUriForFile(SpotChallan.this,
                            BuildConfig.APPLICATION_ID + ".fileProvider"*/

                    if (options[item].equals("Open Camera")) {
                        if (Build.VERSION.SDK_INT <= 23) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            startActivityForResult(intent, 1);
                        } else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(FootPath_Vendor.this,
                                    BuildConfig.APPLICATION_ID + ".fileProvider", f));
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 1);
                        }
                    } else if (options[item].equals("Choose from Gallery")) {
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);
                    } else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                        imgFLG = false;
                    }
                }
            });
            builder.show();
        } else if (FootPath_Vendor.SelPicId.equals("2")) {
            final CharSequence[] options = {"Open Camera", "Choose from Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(FootPath_Vendor.this);
            builder.setTitle("Add Photo!");
            builder.setItems(options, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals("Open Camera")) {
                        if (Build.VERSION.SDK_INT <= 23) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                            startActivityForResult(intent, 1);
                        } else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(FootPath_Vendor.this,
                                    BuildConfig.APPLICATION_ID + ".fileProvider", f));
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(intent, 1);
                        }
                    } else if (options[item].equals("Choose from Gallery")) {

                        Intent intent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 2);
                    } else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                        imgFLG = false;
                    }
                }
            });
            builder.show();

        }
    }

    public class Async_task_GetPsName extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub

            ServiceHelper.getPsNames("" + MainActivity.uintCode);
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

            if (ServiceHelper.psMaster_resp != null) {

                try {

                    String[] psMaster_resp_array = new String[0];
                    psMaster_resp_array = ServiceHelper.psMaster_resp.split("\\@"); // ("\\@")
                    my_ps_arr.clear();
                    db = new DataBase(getApplicationContext());
                    for (String locationDet : psMaster_resp_array) {
                        String[] cirleDet = locationDet.split("\\:");
                        try {
                            db.open();
                            db.insertPsNameDetails("" + cirleDet[0], "" + cirleDet[1]);

                            psNameMap.put(cirleDet[1].trim(), cirleDet[0].trim());
                            // Log.i("All Values :::",""+cirleDet[0]+"\n
                            // gfbhbb"+cirleDet[1]);
                        } catch (Exception e) {
                            // TODO: handle exception
                            if (db != null) {
                                db.close();
                            }
                        }
                    }

                    for (int i = 0; i < ServiceHelper.psMaster_resp_array.length; i++) {
                        String allPSnames = ServiceHelper.psMaster_resp_array[i].split("\\:")[1];
                        my_ps_arr.add(allPSnames);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("Please check the internet and try again!");
                }
            } else {
                showToast("Please check the internet and try again!");
            }

        }
    }

    public class Asyn_task_idCard extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {
                if (null != WS.GetIDProofMaster()) {


                    String IdProofCards = WS.GetIDProofMaster();
                    String IdProofCards1[] = IdProofCards.split("\\@");

                    // HashMap<String , String>
                    IdProofCardsMap = new HashMap<String, String>();
                    final ArrayList<String> IdProofCardsList = new ArrayList<String>();

                    for (int i = 0; i < IdProofCards1.length; i++) {
                        String codevalue[] = IdProofCards1[i].split("\\:");// 2300:trafficell
                        IdProofCardsMap.put(codevalue[0], codevalue[1]);
                        IdProofCardsList.add(codevalue[1]);

                    }
                    // System.out.println("psmap"+psMap);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            TextView title = new TextView(FootPath_Vendor.this);
                            title.setText("Select ID Proof Card:");
                            title.setBackgroundColor(Color.parseColor("#007300"));
                            title.setGravity(Gravity.CENTER);
                            title.setTextColor(Color.WHITE);
                            title.setTextSize(26);
                            title.setTypeface(title.getTypeface(), Typeface.BOLD);
                            title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
                            title.setPadding(20, 0, 20, 0);
                            title.setHeight(70);

                            AlertDialog.Builder builderSingle = new AlertDialog.Builder(FootPath_Vendor.this);
                            builderSingle.setCustomTitle(title);
                            builderSingle.setItems(IdProofCardsList.toArray(new CharSequence[IdProofCardsList.size()]),
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // TODO Auto-generated method stub
                                            ridcard_Btn.setText(IdProofCardsList.get(which));
                                            SIDC = ridcard_Btn.getText().toString();
                                            // Log.i("slected ID Card :: ",SIDC);

                                            if (ridcard_Btn.getText().toString().equals("AADHAAR")) {
                                                // Log.i("id service called",
                                                // "AADHAAR");
                                                // AADHAR
										/*
										 * rname_ET.setText("");
										 * rfname_ET.setText("");
										 * radderss_ET.setText("");
										 * rmobileno_ET.setText("");
										 * rage_ET.setText("");
										 * ridcardno_ET.setText("");
										 */

                                                int maxLength = 12;
                                                ridcardno_ET.setFilters(
                                                        new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
                                                ridcardno_ET.setInputType(InputType.TYPE_CLASS_NUMBER);
                                                ridcardno_ET.requestFocus();

                                            } else if (ridcard_Btn.getText().toString().equals("DRIVING LICENCE")) {
                                                // Log.i("id service called", "DRIVING
                                                // LICENCE");
                                                // DRIVING LICENSE
										/*
										 * rname_ET.setText("");
										 * rfname_ET.setText("");
										 * radderss_ET.setText("");
										 * rmobileno_ET.setText("");
										 * rage_ET.setText("");
										 * ridcardno_ET.setText("");
										 */

                                                ridcardno_ET.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
                                                new AsyncGetDLDetails().execute();
                                            } else if (ridcard_Btn.getText().toString().equals("VEHICLE REGN_NO")) {
                                                // Log.i("id service called", "VEHICLE
                                                // REGN_NO");
                                                // VEHICLLE RC
										/*
										 * rname_ET.setText("");
										 * rfname_ET.setText("");
										 * radderss_ET.setText("");
										 * rmobileno_ET.setText("");
										 * rage_ET.setText("");
										 * ridcardno_ET.setText("");
										 */

                                                ridcardno_ET.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
                                                new AsyncGetRCDetails().execute();
                                            }

                                            for (String idproof1 : IdProofCardsMap.keySet()) {
                                                String idproof1det = IdProofCardsMap.get(idproof1);
                                                if (SIDC.equals(idproof1det)) {
                                                    IDproofCD = idproof1;
                                                    // Log.e("pointCD", pointCD);
                                                }
                                            }
                                        }

                                    });
                            builderSingle.show().getWindow().setLayout(500, 600);
                        }
                    });
                } else {
                    showToast("Please check network and try again!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showToast("Please check network and try again!");
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            // progress.dismiss();
            removeDialog(PROGRESS_DIALOG);

        }
    }

    /*********************** Network Check ***************************/
    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /*********************** Network Check ***************************/

    @SuppressWarnings("unused")
    private class Async_GetGPS_Address extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            ServiceHelper.get_Location("" + latitude, "" + longitude);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);

            // 3-6-489, Hyderguda Road, Avanti Nagar, Basheer Bagh, Hyderabad,
            // Telangana 500029, India

            if (!ServiceHelper.get_lat_long_Detail_resp.equals("NA")
                    && ServiceHelper.get_lat_long_Detail_resp != null) {
                String detail_resp = ServiceHelper.get_lat_long_Detail_resp;
                String[] location_detail = detail_resp.split("\\,");

                for (int i = 0; i < location_detail.length; i++) {
                    if (i < 2) {
                        et_landmark.setText("" + location_detail[0] + ",\n" + location_detail[1]);
                    } else if (i > 2) {
                        et_location.setText(
                                "" + location_detail[2] + ",\n" + location_detail[3] + "," + location_detail[4]);
                    }
                }

                if (!et_location.getText().toString().trim().equals("")) {
                    et_location.setEnabled(false);
                    et_location.setTextColor(Color.BLACK);
                } else {
                    et_location.setEnabled(true);
                }

                if (!et_landmark.getText().toString().trim().equals("")) {
                    et_landmark.setEnabled(false);
                    et_landmark.setTextColor(Color.BLACK);
                } else {
                    et_landmark.setEnabled(true);
                }
            } else {
                et_location.setEnabled(true);
                et_location.requestFocus();
                et_landmark.setEnabled(true);
            }
        }

    }

    public class Async_getDetails extends AsyncTask<Void, Void, String> {
        VerhoeffCheckDigit ver = new VerhoeffCheckDigit();

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            ServiceHelper.preViousHistry("1", "" + ridcardno_ET.getText().toString().trim(),
                    "" + ridcardno_ET.getText().toString().trim(), business_Type);

            // ServiceHelper.get_Location(""+latitude, ""+longitude);

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);

            if (ServiceHelper.prevHistry_resp != null && !ServiceHelper.prevHistry_resp.equals("NA")) {

                if (isNetworkEnabled) {

                    if (ServiceHelper.prevHistry_resp.equals("0")) {
                        showToast("No Previous Data Found !!!");
                        selected_val = 300;
                    } else if (ServiceHelper.prevHistry_resp.equals("NA")) {
                        showToast("No Previous Data Found !!!");
                        selected_val = 300;
                    } else if (ServiceHelper.prevHistry_resp.equals("anyType{}")) {
                        showToast("No Previous Data Found !!!");
                        selected_val = 300;
                    } else if (ServiceHelper.prevHistry_resp.length() > 20) {

                        String strJson = ServiceHelper.prevHistry_resp;

                        // new AsyncGetIDDetails().execute();
                        respondent_aadhaar_details_layout.setVisibility(View.VISIBLE);
                        try {
                            JSONObject jsonRootObject = new JSONObject(strJson);// array
                            JSONArray jsonArray = jsonRootObject.optJSONArray("SHOP_DETAILS");// array
                            if (jsonArray.length() < 1) {
                                showToast("No Previous Data Found !!!");
                                selected_val = 300;

                                fineAmount = jsonRootObject.optString("FINE_AMOUNT").toString();
                                // Log.i("fineAmount :::", ""+fineAmount);
                                fine_amnt_et.setText("Rs. " + fineAmount);

                                if (fine_amnt_et.getText().toString() != null
                                        && fine_amnt_et.getText().toString().trim().length() > 1) {
                                    fine_amnt_et.setEnabled(false);
                                    fine_amnt_et.setTextColor(Color.BLACK);
                                } else {
                                    fine_amnt_et.setEnabled(true);
                                }
                            } else {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    offenceDate = jsonObject.optString("OFFENCE_DT");
                                    firm_regno = jsonObject.optString("FIRM_REGN_NO");
                                    respondentName = jsonObject.optString("RESPONDENT_NAME");
                                    shopName = jsonObject.optString("SHOP_NAME");
                                    shopOwner = jsonObject.optString("SHOP_O_NAME");
                                    location_tosend = jsonObject.optString("LOCATION");
                                    shopAddress = jsonObject.optString("SHOP_ADDR");
                                    idProofCode = jsonObject.optString("ID_PROOF_CD");
                                    idProofNo = jsonObject.optString("ID_PROOF_DET");

                                }
                                fineAmount = jsonRootObject.optString("FINE_AMOUNT");
                                // Log.i("fineAmount :::", ""+fineAmount);
                                fine_amnt_et.setText("Rs. " + fineAmount);

                                if (fine_amnt_et.getText().toString().trim() != null
                                        && fine_amnt_et.getText().toString().trim().length() > 1) {
                                    fine_amnt_et.setEnabled(false);
                                    fine_amnt_et.setTextColor(Color.BLACK);
                                } else {
                                    fine_amnt_et.setEnabled(true);
                                }
                            }
                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    } else {
                        showToast("No Previous Data Found !!!");

                        if (ridcardno_ET.getText() != null && ridcardno_ET.getText().toString().trim().length() > 10
                                && ridcardno_ET.getText().toString().trim().length() == 12
                                && (!ver.isValid(ridcardno_ET.getText().toString()))) {
                            showToast("Please Enter Valid Adhaar Number");
                        } else if (ridcardno_ET.getText() != null
                                && ridcardno_ET.getText().toString().trim().length() != 12
                                && (!ver.isValid(ridcardno_ET.getText().toString()))) {
                            showToast("Please Enter Valid Adhaar Number");
                        } else if (ridcardno_ET.getText() != null
                                && ridcardno_ET.getText().toString().trim().length() == 12
                                && (ver.isValid(ridcardno_ET.getText().toString()))) {

                            if ("Y".equals(AADHAAR_DATA_FLAG)) {
                                if (isOnline()) {
                                    new AsyncGetIDDetails().execute();
                                } else {
                                    showToast("Please Check Internet Connection");
                                }
                            }

                        }
                    }
                }
            } else {
                showToast("No data Found!");
            }

        }

    }

    /********************
     * Get Details BY AADHAAR NO Class AsyncGetIDDetails Starts
     ****************************/

    class AsyncGetIDDetails extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            try {
                SoapObject request = new SoapObject(NAMESPACE, "getAADHARData");
                request.addProperty("uid", "" + ridcardno_ET.getText().toString().trim());
                request.addProperty("eid", "" + ridcardno_ET.getText().toString().trim());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL);
                Log.i("**MainActivity.URL ***", "" + MainActivity.URL);

                httpTransportSE.call(SOAP_ACTION, envelope);
                Object result = envelope.getResponse();
                if (null != result) {
                    Opdata_Chalana = "";
                    Opdata_Chalana = result.toString();
                } else {
                    Opdata_Chalana = "0";
                    aadhar_details = new String[0];
                }


            } catch (SoapFault fault) {
                Opdata_Chalana = "0";
                aadhar_details = new String[0];

            } catch (Exception e) {
                // TODO: handle exception
                Opdata_Chalana = "0";
                aadhar_details = new String[0];
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);
            if (!isOnline()) {
                showToast("Please Enable the Data Connection!");
            } else {

                if (Opdata_Chalana.toString().trim().equals("0") || Opdata_Chalana.toString().trim().equals("NA")
                        || Opdata_Chalana.toString().trim().contains("anyTypr{}")) {
                    aadhar_details = new String[0];
                    showToast("No Aadhaar Details Found !!!");
                    rname_ET.setEnabled(true);
                    rfname_ET.setEnabled(true);
                    radderss_ET.setEnabled(true);
                    rmobileno_ET.setEnabled(true);
                    rage_ET.setEnabled(true);
                } else {
                    try {
                        aadhar_details = new String[0];
                        aadhar_details = Opdata_Chalana.split("\\@");
                        respondent_aadhaar_details_layout.setVisibility(View.VISIBLE);
                        rname_ET.setText("" + aadhar_details[0]);

                        rfname_ET.setText("" + aadhar_details[1]);

                        radderss_ET.setText("" + aadhar_details[2] + "," + aadhar_details[3] + "," + aadhar_details[4] + ","
                                + aadhar_details[5] + "," + aadhar_details[6] + "," + aadhar_details[7]);

                        if (aadhar_details[8].trim().length() == 10) {
                            rmobileno_ET.setText("" + aadhar_details[8]);
                        } else {
                            rmobileno_ET.setText("");
                            rmobileno_ET.setEnabled(true);
                            rmobileno_ET.setTextColor(Color.BLACK);
                        }

                        String dob_age = "" + aadhar_details[10];
                        if (dob_age != null && dob_age.length() > 2) {

                            String validate = dob_age.substring(0, 3);
                            Log.i("validate ::::::::", "" + validate);

                            if (validate.contains("-")) {
                                String[] split_dob = dob_age.split("\\-");
                                String service_year = "" + split_dob[2];

                                int final_age = pYear - Integer.parseInt(service_year);
                                Log.i("final_age ::::::::", "" + final_age);

                                rage_ET.setText("" + final_age);
                            } else if (validate.contains("/")) {
                                String[] split_dob = dob_age.split("\\/");
                                String service_year = "" + split_dob[2];

                                int final_age = pYear - Integer.parseInt(service_year);
                                Log.i("final_age ::::::::", "" + final_age);

                                rage_ET.setText("" + final_age);
                            }
                        }

                        if (rname_ET.getText().toString().trim() != null
                                && rname_ET.getText().toString().trim().length() > 1) {
                            rname_ET.setEnabled(false);
                            rname_ET.setTextColor(Color.BLACK);
                        } else {
                            rname_ET.setEnabled(true);
                        }

                        if (rfname_ET.getText().toString().trim() != null
                                && rfname_ET.getText().toString().trim().length() > 1) {
                            rfname_ET.setEnabled(false);
                            rfname_ET.setTextColor(Color.BLACK);
                        } else {
                            rfname_ET.setEnabled(true);
                        }

                        if (radderss_ET.getText().toString().trim() != null
                                && radderss_ET.getText().toString().trim().length() > 1) {
                            radderss_ET.setEnabled(true);
                            radderss_ET.setTextColor(Color.BLACK);
                        } else {
                            radderss_ET.setEnabled(true);
                        }

                        if (rmobileno_ET.getText().toString().trim() != null
                                && rmobileno_ET.getText().toString().trim().length() > 1) {
                            rmobileno_ET.setEnabled(true);
                            rmobileno_ET.setTextColor(Color.BLACK);
                        } else if (rmobileno_ET.getText().toString().trim().equals("0")) {
                            rmobileno_ET.setEnabled(true);
                            rmobileno_ET.setTextColor(Color.BLACK);
                        } else {
                            rmobileno_ET.setEnabled(true);
                        }

                        if (rage_ET.getText().toString().trim() != null
                                && rage_ET.getText().toString().trim().length() > 1) {
                            rage_ET.setEnabled(false);
                            rage_ET.setTextColor(Color.BLACK);
                        } else {
                            rage_ET.setEnabled(true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        showToast("Please check the Network and try again!");
                    }
                }
            }
        }
    }

    /********************
     * Get Details BY RC Class AsyncGetIDDetails Ends
     ****************************/

    /********************
     * Get Details BY Driving License Class AsyncGetDLDetails Starts
     ****************************/

    class AsyncGetDLDetails extends AsyncTask<Void, Void, String> {

        // ProgressDialog progress = ProgressDialog.show(FootPath_Vendor.this,
        // "Loading...!", "Please wait......Processing!!!");

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {
                String ip = "";
                SQLiteDatabase db = openOrCreateDatabase(DataBase.DATABASE_NAME, MODE_PRIVATE, null);
                String selectQuery = "SELECT  * FROM " + DataBase.IP_TABLE;
                Cursor cursor = db.rawQuery(selectQuery, null);
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        Log.i("DATABASE   IP VALUE :", "" + cursor.getString(0));
                        ip = cursor.getString(0);
                    } while (cursor.moveToNext());
                }
                db.close();
                // INSERT ID PROOF DETAILS START
                SoapObject request = new SoapObject(NAMESPACE, "getDetailsByDrivingLicence");
                request.addProperty("dl_no", ridcardno_ET.getText().toString().toUpperCase());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                Log.i("request", "" + request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(
                        ip + "/HydPettyCaseService/services/PettyCaseServiceImpl?wsdl");
                Log.i("androidHttpTransport", "" + androidHttpTransport);
                androidHttpTransport.call(SOAP_ACTION_ID, envelope);
                Object result = envelope.getResponse();

                if (null != result) {

                    challanGenresp = result.toString();

                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (challanGenresp != null && challanGenresp.length() > 25) {
                                try {
                                    JSONObject jsonObj = new JSONObject(challanGenresp);
                                    // Getting JSON Array node
                                    JSONArray contacts = jsonObj.getJSONArray("LICENSE_DETAILS");
                                    // looping through All Contacts
                                    JSONObject c = contacts.getJSONObject(0);

                                    rname_ET.setText(c.getString("NAME"));
                                    rfname_ET.setText(c.getString("FNAME"));
                                    rage_ET.setText(c.getString("DOB").toString());

                                    String completeAddress = c.getString("HNO") + c.getString("STREET")
                                            + c.getString("VILLAGE") + c.getString("MANDAL") + c.getString("DISTRICT")
                                            + c.getString("STATE") + c.getString("PIN_CODE");
                                    radderss_ET.setText(completeAddress);
                                    // rmobileno_ET.setText(c.getString("PHONE_NO").toString());

                                    if (c.getString("PHONE_NO").toString().trim().length() == 10) {
                                        rmobileno_ET.setText(c.getString("PHONE_NO").toString());
                                    } else {
                                        rmobileno_ET.setText("");
                                    }

                                    fineAmount = jsonObj.optString("FINE_AMOUNT").toString();
                                    Log.i("fineAmount :::", "" + fineAmount);
                                    fine_amnt_et.setText("Rs. " + fineAmount);

                                    if (fine_amnt_et.getText().toString().trim() != null
                                            && fine_amnt_et.getText().toString().trim().length() > 1) {
                                        fine_amnt_et.setEnabled(false);
                                        fine_amnt_et.setTextColor(Color.BLACK);
                                    } else {
                                        fine_amnt_et.setEnabled(true);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    showToast("Please check the network and try again!");
                                }
                            } else {
                                showToast("No data Found");
                            }
                        }
                    });
                } else {
                    showToast("Please check the network and try again!");
                }

                // // INSERT ID PROOF DETAILS END
            } catch (Exception e) {
                e.printStackTrace();
                showToast("Please check the network and try again!");
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            // progress.dismiss();
            removeDialog(PROGRESS_DIALOG);
            if (!isOnline()) {
                // do something
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FootPath_Vendor.this);
                alertDialogBuilder.setMessage("").setCancelable(false).setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();
            }
        }
    }

    /********************
     * Get Details BY Driving License Class AsyncGetDLDetails Ends
     ****************************/

    /********************
     * Get Details BY RC Class AsyncGetRCDetails Starts
     ****************************/

    class AsyncGetRCDetails extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                String ip = "";
                SQLiteDatabase db = openOrCreateDatabase(DataBase.DATABASE_NAME, MODE_PRIVATE, null);
                String selectQuery = "SELECT  * FROM " + DataBase.IP_TABLE;
                // SQLiteDatabase db = this.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        Log.i("DATABASE   IP VALUE :", "" + cursor.getString(0));
                        ip = cursor.getString(0);
                    } while (cursor.moveToNext());
                }
                db.close();
                // INSERT ID PROOF DETAILS START
                SoapObject request = new SoapObject(NAMESPACE, "getDetailsByRC");
                request.addProperty("regn_no", ridcardno_ET.getText().toString());
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                Log.i("request", "" + request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(
                        ip + "/HydPettyCaseService/services/PettyCaseServiceImpl?wsdl");
                androidHttpTransport.call(SOAP_ACTION_ID, envelope);
                Object result = envelope.getResponse();
                if (null != result) {

                    challanGenresp = result.toString();
                    runOnUiThread(new Runnable() {
                        @SuppressWarnings("unused")
                        public void run() {
                            if (challanGenresp != null && challanGenresp.length() > 25) {
                                try {
                                    JSONObject jsonObj = new JSONObject(challanGenresp);
                                    // Getting JSON Array node
                                    JSONArray contacts = jsonObj.getJSONArray("RC_DETAILS");
                                    // looping through All Contacts
                                    JSONObject c = contacts.getJSONObject(0);
                                    rname_ET.setText(c.getString("NAME"));
                                    rfname_ET.setText(c.getString("FNAME"));
                                    rage_ET.setText(c.getString("DOB").toString());

                                    String completeAddress = c.getString("HNO") + c.getString("STREET")
                                            + c.getString("VILLAGE") + c.getString("MANDAL") + c.getString("DISTRICT")
                                            + c.getString("STATE") + c.getString("PIN_CODE");
                                    radderss_ET.setText(completeAddress);
                                    // rmobileno_ET.setText(c.getString("PHONE_NO").toString());

                                    if (c.getString("PHONE_NO").toString().trim().length() == 10) {
                                        rmobileno_ET.setText(c.getString("PHONE_NO").toString());
                                    } else {
                                        rmobileno_ET.setText("");
                                    }
                                    // city_ET.setText(c.getString("DISTRICT").toString());
                                    // pincode_ET.setText(c.getString("PIN_CODE").toString());

                                    // imgv.setImageResource(R.drawable.photo);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else {
                                Log.i("no data from service", "no ");
                                Toast toast = Toast.makeText(getApplicationContext(), "No data Found", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, -200);
                                View toastView = toast.getView();
                                toast.show();
                            }
                        }
                    });
                }else{
                    showToast("Please check the network and Try again!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showToast("Please check the network and Try again!");
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            // progress.dismiss();
            removeDialog(PROGRESS_DIALOG);
            if (!isOnline()) {
                // do something
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FootPath_Vendor.this);
                alertDialogBuilder.setMessage("Please Enable Internet Connection").setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = alertDialogBuilder.create();
                alert.show();
            }
        }
    }

    /********************
     * Get Details BY RC Class AsyncGetRCDetails Ends
     ****************************/

    public class Async_task_GetPointNames extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            String query = "select PS_CODE from " + DataBase.psName_table + " where PS_NAME='"
                    + psname_Btn.getText().toString().trim() + "'";
            // selectedPs_code
            my_psCode_arr.clear();

            try {
                db.open();
                Cursor loc_cursor = DataBase.db.rawQuery(query, null);

                if (loc_cursor.moveToNext()) {
                    do {
                        selectedPs_code = "" + loc_cursor.getString(0);

                    } while (loc_cursor.moveToNext());
                }

                loc_cursor.close();

            } catch (Exception e) {
                e.printStackTrace();
                if (db != null) {
                    db.close();
                }
            }

            ServiceHelper.getPointDetailsByPscode("" + selectedPs_code);

            // }
            return null;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);

            if (ServiceHelper.point_by_ps_resp != null) {
                try {
                    String[] point_array = ServiceHelper.point_by_ps_resp.split("\\@");
                    my_points_arr.clear();
                    for (String locationDet : point_array) {
                        String[] cirleDet = locationDet.split("\\:");
                        try {
                            db.open();
                            db.insertPointNamesDetails("" + cirleDet[0], "" + cirleDet[1]);
                            pointNameMap.put(cirleDet[1].trim(), cirleDet[0].trim());
                            Log.i("All Points Values :::", "" + cirleDet[0] + "\n " + cirleDet[1]);
                        } catch (Exception e) {
                            // TODO: handle exception
                            if (db != null) {
                                db.close();
                            }
                        }
                    }

                    for (int i = 0; i < ServiceHelper.PointNamesBypsNames_master.length; i++) {
                        String allPoints = ServiceHelper.PointNamesBypsNames_master[i].split("\\:")[1];

                        Log.i("allPSnames", allPoints);
                        my_points_arr.add(allPoints);
                    }

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            TextView title = new TextView(FootPath_Vendor.this);
                            title.setText("Select Point Name:");
                            title.setBackgroundColor(Color.parseColor("#007300"));
                            title.setGravity(Gravity.CENTER);
                            title.setTextColor(Color.WHITE);
                            title.setTextSize(26);
                            title.setTypeface(title.getTypeface(), Typeface.BOLD);
                            title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
                            title.setPadding(20, 0, 20, 0);
                            title.setHeight(70);

                            AlertDialog.Builder builderSingle = new AlertDialog.Builder(FootPath_Vendor.this);
                            builderSingle.setCustomTitle(title);
                            builderSingle.setItems(my_points_arr.toArray(new CharSequence[my_points_arr.size()]),
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // TODO Auto-generated method stub
                                            pointname_Btn.setText(my_points_arr.get(which));
                                            SPSPN = pointname_Btn.getText().toString();

                                        }

                                    });

                            builderSingle.show().getWindow().setLayout(500, 600);

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("Please check the network and Try again!");
                }
            } else {
                showToast("Please check the network and Try again!");
            }


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

	/*
	 * protected void onActivityResult(int requestCode, int resultCode, Intent
	 * data) { if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
	 * 
	 * 
	 * File imagFile = new File(imageDir, imageFileName); try { photo =
	 * BitmapFactory.decodeStream(new FileInputStream(imagFile)); } catch
	 * (FileNotFoundException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } imgv.setImageBitmap(photo); } if (requestCode ==
	 * PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null &&
	 * data.getData() != null) {
	 * 
	 * Uri uri = data.getData();
	 * 
	 * try { photo = MediaStore.Images.Media.getBitmap(getContentResolver(),
	 * uri);
	 * 
	 * imgv.setImageBitmap(photo); } catch (IOException e) {
	 * e.printStackTrace(); }
	 * 
	 * }
	 * 
	 * ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	 * photo.compress(Bitmap.CompressFormat.JPEG,20, bytes);
	 * 
	 * byteArray = bytes.toByteArray();
	 * 
	 * imgString = Base64.encodeToString(byteArray, Base64.NO_WRAP); }
	 */

    @SuppressWarnings("unused")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            String picturePath = "";
            if (requestCode == 1) {

                File f = new File(Environment.getExternalStorageDirectory().toString());

                for (File temp : f.listFiles()) {

                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    String current_date = FootPath_Vendor.date;
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);

                    String path = android.os.Environment.getExternalStorageDirectory() + File.separator + "Tab39B"
                            + File.separator + "FootPath" + File.separator + current_date;
                    File camerapath = new File(path);

                    if (!camerapath.exists()) {
                        camerapath.mkdirs();
                    }
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {

                        outFile = new FileOutputStream(file);
                        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                        Canvas canvas = new Canvas(mutableBitmap); // bmp is the
                        // bitmap to
                        // dwaw into

                        Paint paint = new Paint();
                        paint.setColor(Color.RED);
                        paint.setTextSize(80);
                        paint.setTextAlign(Paint.Align.CENTER);

                        getLocation();
                        int xPos = (canvas.getWidth() / 2);
                        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));

                        canvas.drawText("Date & Time: " + Current_Date, xPos, yPos + 300, paint);
                        canvas.drawText("Lat :" + latitude, xPos, yPos + 400, paint);
                        canvas.drawText("Long :" + longitude, xPos, yPos + 500, paint);
                        mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outFile);
                        outFile.flush();
                        outFile.close();

                        new SingleMediaScanner(this, file);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if ("1".equals(FootPath_Vendor.SelPicId) && bitmap != null) {
                        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                        Canvas canvas = new Canvas(mutableBitmap); // bmp is the
                        // bitmap to
                        // dwaw into

                        Paint paint = new Paint();
                        paint.setColor(Color.RED);
                        paint.setTextSize(80);
                        paint.setTextAlign(Paint.Align.CENTER);
                        int xPos = (canvas.getWidth() / 2);
                        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));

                        canvas.drawText("Date & Time: " + Current_Date, xPos, yPos + 300, paint);
                        canvas.drawText("Lat :" + latitude, xPos, yPos + 400, paint);
                        canvas.drawText("Long :" + longitude, xPos, yPos + 500, paint);
                        // canvas.drawText("Date & Time: "+Current_Date+"\n"+"
                        // Lat :"+latitude+ " Long :"+longitude,1250, 1500,
                        // paint);

                        imgv.setImageBitmap(mutableBitmap);
                        // picture1.setRotation(90);

                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 20, bytes);

                        byteArray = bytes.toByteArray();

                        imgString = Base64.encodeToString(byteArray, Base64.NO_WRAP);

                        Log.i("imgString 1::", "" + imgString);
                    } else if ("2".equals(FootPath_Vendor.SelPicId) && bitmap != null) {
                        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                        Canvas canvas = new Canvas(mutableBitmap); // bmp is the
                        // bitmap to
                        // dwaw into

                        Paint paint = new Paint();
                        paint.setColor(Color.RED);
                        paint.setTextSize(80);
                        paint.setTextAlign(Paint.Align.CENTER);
                        int xPos = (canvas.getWidth() / 2);
                        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));

                        canvas.drawText("Date & Time: " + Current_Date, xPos, yPos + 300, paint);
                        canvas.drawText("Lat :" + latitude, xPos, yPos + 400, paint);
                        canvas.drawText("Long :" + longitude, xPos, yPos + 500, paint);

                        imgv2.setImageBitmap(mutableBitmap);
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 20, bytes);

                        byteArray = bytes.toByteArray();

                        imgString2 = Base64.encodeToString(byteArray, Base64.NO_WRAP);

                        Log.i("imgString 2::", "" + imgString);
                    } else if (bitmap == null) {
                        showToast("Image Cannot be Loaded !");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                picturePath = c.getString(columnIndex);
                c.close();

                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));

                if ("1".equals(FootPath_Vendor.SelPicId) && thumbnail != null) {
                    Bitmap mutableBitmap = thumbnail.copy(Bitmap.Config.ARGB_8888, true);
                    Canvas canvas = new Canvas(mutableBitmap); // bmp is the
                    // bitmap to
                    // dwaw into

                    Paint paint = new Paint();
                    paint.setColor(Color.RED);
                    paint.setTextSize(100);
                    paint.setTextAlign(Paint.Align.CENTER);
                    // canvas.drawText("Date & Time: "+Current_Date+"\n"+" Lat
                    // :"+latitude+ " Long :"+longitude,1250, 1500, paint);

                    int xPos = (canvas.getWidth() / 2);
                    int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));

                    // canvas.drawText("Date & Time: " + Current_Date, xPos,
                    // yPos + 800, paint);
                    // canvas.drawText("Lat :" + latitude , xPos, yPos + 900,
                    // paint);
                    // canvas.drawText("Long :"+ longitude, xPos, yPos + 1000,
                    // paint);

                    imgv.setImageBitmap(mutableBitmap);
                    // picture1.setRotation(90);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 20, bytes);

                    byteArray = bytes.toByteArray();

                    imgString = Base64.encodeToString(byteArray, Base64.NO_WRAP);

                } else if ("2".equals(FootPath_Vendor.SelPicId) && thumbnail != null) {
                    Bitmap mutableBitmap = thumbnail.copy(Bitmap.Config.ARGB_8888, true);
                    Canvas canvas = new Canvas(mutableBitmap); // bmp is the
                    // bitmap to
                    // dwaw into

                    Paint paint = new Paint();
                    paint.setColor(Color.RED);
                    paint.setTextSize(100);
                    paint.setTextAlign(Paint.Align.CENTER);
                    int xPos = (canvas.getWidth() / 2);
                    int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));


                    imgv2.setImageBitmap(mutableBitmap);
                    // picture2.setRotation(90);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 20, bytes);

                    byteArray = bytes.toByteArray();

                    imgString2 = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                } else if (thumbnail == null) {
                    showToast("Image Cannot be Loaded !");
                }
            }
        }
    }

    /*************************************************************************
     * Camera Method
     *******************************************/

    /*************************************************************************
     * Date methos
     ********************************************/

    private DatePickerDialog.OnDateSetListener pDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            pYear = year;
            pMonth = monthOfYear;
            pDay = dayOfMonth;
            updateDisplay();
            displayToast();
        }

    };

    @SuppressWarnings("unused")
    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // TODO Auto-generated method stub
            mHour = hourOfDay;
            mMinute = minute;
            updateDisplay();
            displayToast();
        }
    };

    /**
     * Updates the date in the TextView
     */

    @SuppressLint("SimpleDateFormat")
    private void updateDisplay() {

        datepicker_Btn.setText(new StringBuilder()
                // Month is 0 based so add 1
                .append(pDay).append("/").append(pMonth + 1).append("/").append(pYear).append(" "));

        timestamp = new StringBuilder().append(pDay).append("/").append(pMonth + 1).append("/").append(pYear)
                .append("/").append(mHour).append(":").append(mMinute).append(":").append(mSecond).append(" ");

        // Toast.makeText(getApplicationContext(), "timeStamp" +timestamp,
        // Toast.LENGTH_LONG).show();

    }

    private void displayToast() {
        // Toast.makeText(this, new StringBuilder().append("Date choosen is
        // ").append(datepicker_Btn.getText()), Toast.LENGTH_SHORT).show();

    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case PROGRESS_DIALOG:
                ProgressDialog pd = ProgressDialog.show(this, "", "", true);
                pd.setContentView(R.layout.custom_progress_dialog);
                pd.setCancelable(false);

                return pd;
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, pDateSetListener, pYear, pMonth, pDay);
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        newtimer.cancel();
        Intent back = new Intent(getApplicationContext(), Dashboard.class);
        startActivity(back);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            latitude = (float) location.getLatitude();
            longitude = (float) location.getLongitude();
            // speed = location.getSpeed();

            // new Async_GetGPS_Address().execute();
        } else {
            latitude = 0.0;
            longitude = 0.0;
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

}
