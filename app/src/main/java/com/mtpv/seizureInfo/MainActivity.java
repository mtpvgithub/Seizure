package com.mtpv.seizureInfo;

import java.util.ArrayList;
import java.util.HashMap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.*;
import android.support.design.widget.Snackbar;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mtpv.seizure.R;
import com.mtpv.seizureHelpers.DataBase;
import com.mtpv.seizureHelpers.ServiceHelper;
import com.mtpv.seizureHelpers.WebService;


/******************Login ResPonse***************/
/*PID code| officerName| PSCode| PsName| CadreCode| Cadre| Password*/
public class MainActivity extends Activity implements LocationListener,OnClickListener {

    final WebService WS = new WebService();
    HashMap<String, String> psMap;
    public static String SPSN = null;
    public static String SPSPN = null;
    public static String pscd = null;

    public static EditText userID_ET, password_ET;
    Button cancel_Btn, login_Btn;
    public static String Pid_code, Pid_Name, Ps_code, Ps_Name, CADRE_CODE, CADRE_NAME, SECURITY_CD;

    public static String PID_CODE1 = null, PID_NAME1 = null, PS_CODE1 = null, PS_NAME1 = null, CADRE_CODE1 = null, CADRE_NAME1 = null, SECURITY_CD1 = null,
            GHMC_AUTH = null,
            CONTACT_NO = null,
            AADHAAR_DATA_FLAG = null,
            TIN_FLAG = null,
            OTP_NO_FLAG = null,
            CASHLESS_FLAG = null,
            MOBILE_NO_FLAG = null,
            RTA_DATA_FLAG = null,
            DL_DATA_FLAG = null;

    SharedPreferences preference;
    SharedPreferences.Editor editor;

    public static String URL = "", URLOTPStatus = "";
    String service_type = "";
    public static String services_url = "";
    String ftps_url = "";
    private String url_to_fix = "/39BService/services/BServiceImpl?wsdl";
    private String url_to_fix_otpStatus = "/eTicketMobileHyd/services/MobileEticketServiceImpl?wsdl";
    public static String[] arr_logindetails;

    LinearLayout ip_settings;

    public static String PIdCode, userName, passWord;
    static String resp;
    static String uintCode = "23";
    static String uintName = "Hyderabad";

    final int SPLASH_DIALOG = 0;
    final int PROGRESS_DIALOG = 1;
    /* GPS VALUES */
    // flag for GPS status
    boolean isGPSEnabled = false;
    // flag for network status
    boolean isNetworkEnabled = false;
    boolean GetLocation = false;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    public static String PsCode = null, PsName = null;
    double latitude = 0.0;
    double longitude = 0.0;
    String provider = "";
    String IMEI = "";
    LocationManager m_locationlistner;
    android.location.Location location;
    public WebService web_service;

    GPSTracker gps;
    static String appVersion;

    ArrayList<String> my_ps_arr;

    private static final String[] requiredPermissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.CAMERA,
            Manifest.permission.INSTALL_SHORTCUT
    };

    private static final int REQUEST_PERMISSIONS = 20;
    private SparseIntArray mErrorString;

    @SuppressWarnings({"deprecation", "unused"})
    @SuppressLint("WorldReadableFiles")
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //getLocation();
        appVersion = getResources().getString(R.string.version);

        mErrorString = new SparseIntArray();

        if (Build.VERSION.SDK_INT > 22 && !hasPermissions(requiredPermissions)) {

            MainActivity.this.requestAppPermissions(new
                            String[]{Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.INTERNET,
                            Manifest.permission.CAMERA,
                            Manifest.permission.BLUETOOTH,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.INSTALL_SHORTCUT}, R.string
                            .runtime_permissions_txt
                    , REQUEST_PERMISSIONS);
        }


        m_locationlistner = null;

        my_ps_arr = new ArrayList<String>();

        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        preference = getSharedPreferences("preferences", MODE_PRIVATE);
        service_type = preference.getString("servicetype", "live");
        services_url = preference.getString("serviceurl", "url1");
        ftps_url = preference.getString("ftpurl", "url2");

        /**************Adding Shortcut of Application**************/
        SharedPreferences prefs = getSharedPreferences("ShortCutPrefs", MODE_PRIVATE);
        if (!prefs.getBoolean("isFirstTime", false)) {

            addShortcut();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isFirstTime", true);
            editor.commit();
        }
        /**************Adding Shortcut of Application**************/

        /*****************GPS FUNCTIONALITY******************/

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Toast.makeText(this, "GPS is Enabled in your device", Toast.LENGTH_SHORT).show();
        } else {
            showGPSDisabledAlertToUser();
        }
        /*****************GPS FUNCTIONALITY******************/


        ip_settings = (LinearLayout) findViewById(R.id.ip_settings);

        userID_ET = (EditText) findViewById(R.id.userID_ET);
        password_ET = (EditText) findViewById(R.id.password_ET);

        userID_ET.setText("23001004");
        password_ET.setText("WdSt48Pri");

        cancel_Btn = (Button) findViewById(R.id.cancel_Btn);
        login_Btn = (Button) findViewById(R.id.login_Btn);

        if ((!services_url.equals("url1") && (service_type.equals("live")))) {
            URL = "" + services_url + "" + url_to_fix;
            URLOTPStatus = "" + services_url +""+url_to_fix_otpStatus;

        } else if ((!services_url.equals("url1") && (service_type.equals("test")))) {
            URL = "" + services_url + "" + url_to_fix;
            URLOTPStatus = "" + services_url +""+url_to_fix_otpStatus;
        }

        cancel_Btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                TextView title = new TextView(MainActivity.this);
                title.setText("SEIZURE");
                title.setBackgroundColor(Color.RED);
                title.setGravity(Gravity.CENTER);
                title.setTextColor(Color.WHITE);
                title.setTextSize(26);
                title.setTypeface(title.getTypeface(), Typeface.BOLD);
                title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
                title.setPadding(20, 0, 20, 0);
                title.setHeight(70);

                String otp_message = "Are you sure, You want to Leave Application...!";

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                alertDialogBuilder.setCustomTitle(title);
                alertDialogBuilder.setIcon(R.drawable.dialog_logo);
                alertDialogBuilder.setMessage(otp_message);
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
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

                Button btn1 = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                btn1.setTextSize(22);
                btn1.setTextColor(Color.WHITE);
                btn1.setTypeface(btn1.getTypeface(), Typeface.BOLD);
                btn1.setBackgroundColor(Color.RED);


                Button btn2 = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                btn2.setTextSize(22);
                btn2.setTextColor(Color.WHITE);
                btn2.setTypeface(btn2.getTypeface(), Typeface.BOLD);
                btn2.setBackgroundColor(Color.RED);

            }
        });


        ip_settings.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                GPSTracker.locationManager = null;
                gps = new GPSTracker(MainActivity.this);

                // check if GPS enabled
                if (gps.getLocation() != null) {

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                } else {
                    latitude = 0.0;
                    longitude = 0.0;

//                    gps.showSettingsAlert();
                }
                Intent ipsetting = new Intent(getApplicationContext(), IPsettings.class);
                startActivity(ipsetting);
            }
        });

        login_Btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (userID_ET.getText().toString().trim().equals("")) {
                    userID_ET.setError(Html.fromHtml("<font color='black'>Enter PID</font>"));
                } else if (password_ET.getText().toString().trim().equals("")) {
                    password_ET.setError(Html.fromHtml("<font color='black'>Enter password</font>"));
                } else {
                    /*****************GPS FUNCTIONALITY******************/

                    LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                    /*****************GPS FUNCTIONALITY******************/
                    if (!isOnline()) {
                        // do something
                        showToast("Please check the Network!");
                    } else {
                        new Async_task_login().execute();
                        GPSTracker.locationManager = null;
                        gps = new GPSTracker(MainActivity.this);

                        // check if GPS enabled
                        if (gps.getLocation() != null) {

                            latitude = gps.getLatitude();
                            longitude = gps.getLongitude();

                            // \n is for new line
                            // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                        } else {
                            // can't get location
                            // GPS or Network is not enabled
                            // Ask user to enable GPS/network in settings
                            latitude = 0.0;
                            longitude = 0.0;
//                            gps.showSettingsAlert();
                        }

                    }
                }
            }
        });
    }




    public void requestAppPermissions(final String[] requestedPermissions,
                                      final int stringId, final int requestCode) {
        mErrorString.put(requestCode, stringId);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        boolean shouldShowRequestPermissionRationale = false;
        for (String permission : requestedPermissions) {
            permissionCheck = permissionCheck + MainActivity.this.checkSelfPermission(permission);
            shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale || MainActivity.this.shouldShowRequestPermissionRationale(permission);
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale) {
                Snackbar.make(findViewById(android.R.id.content), stringId,
                        Snackbar.LENGTH_INDEFINITE).setAction("GRANT",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MainActivity.this.requestPermissions(requestedPermissions, requestCode);
                            }
                        }).show();
            } else {
                MainActivity.this.requestPermissions(requestedPermissions, requestCode);
            }
        } else {
            onPermissionsGranted(requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int permission : grantResults) {
            permissionCheck = permissionCheck + permission;
        }
        if ((grantResults.length > 0) && permissionCheck == PackageManager.PERMISSION_GRANTED) {
            onPermissionsGranted(requestCode);
        } else {
            Snackbar.make(findViewById(android.R.id.content), mErrorString.get(requestCode),
                    Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            startActivity(intent);
                        }
                    }).show();
        }
    }

    public void onPermissionsGranted(final int requestCode) {
        Toast.makeText(this, "Permissions Received.", Toast.LENGTH_LONG).show();
    }

    public boolean hasPermissions(String... permissions) {
        for (String permission : permissions)
            if (PackageManager.PERMISSION_GRANTED != checkCallingOrSelfPermission(permission))
                return false;
        return true;
    }

    private void EnableGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("    GPS is unable to take Lattitude and Longitude Values \n            Please Turn OFF and Turn ON GPS?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
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
                .setCancelable(false)
                .setPositiveButton("Goto Settings",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @SuppressLint("WorldReadableFiles")
    @SuppressWarnings("deprecation")
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        preference = getSharedPreferences("preferences", MODE_PRIVATE);

        services_url = preference.getString("serviceurl", "url1");
        ftps_url = preference.getString("ftpurl", "url2");

        if (!services_url.equals("urls1")) {
            URL = "" + services_url + "" + url_to_fix;
        }
    }

    /***********************Network Check***************************/
    private boolean isOnline() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onClick(View v) {

    }

    /***********************Network Check***************************/

    public class Async_task_login extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            userName = userID_ET.getText().toString().trim();
            passWord = password_ET.getText().toString().trim();

            getLocation();
            String[] version_split = appVersion.split("\\-");
            ServiceHelper.login(userName, passWord, IMEI, "" + latitude, "" + longitude, "" + version_split[1]);
            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @SuppressWarnings({"deprecation", "unused"})
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            removeDialog(PROGRESS_DIALOG);
            try {
                if (ServiceHelper.login_response.trim().equals("0") || null == ServiceHelper.login_response) {
                    showToast("Login Failed!");
                } else if (ServiceHelper.login_response.trim().equals("1") || null == ServiceHelper.login_response) {
                    showToast("Invalid Login Details");
                } else if (ServiceHelper.login_response.trim().equals("2") || null == ServiceHelper.login_response) {
                    showToast("Unauthorised Device");
                } else if (ServiceHelper.login_response.trim().equals("3") || null == ServiceHelper.login_response) {
                    showToast("Contact eChallan Team");
                } else {
                    MainActivity.arr_logindetails = ServiceHelper.login_response.split(":");
                    for (String arr_logindetail : MainActivity.arr_logindetails) {
                        Ps_code = MainActivity.arr_logindetails[2];
                    }
                    PID_CODE1 = arr_logindetails[0];//
                    PID_NAME1 = arr_logindetails[1];//
                    PS_CODE1 = arr_logindetails[2];//
                    PS_NAME1 = arr_logindetails[3];//
                    CADRE_CODE1 = arr_logindetails[4];//
                    CADRE_NAME1 = arr_logindetails[5];//
                    SECURITY_CD1 = arr_logindetails[6];//
                    GHMC_AUTH = arr_logindetails[7];
                    CONTACT_NO = arr_logindetails[8];
                    AADHAAR_DATA_FLAG = arr_logindetails[9];
                    TIN_FLAG = arr_logindetails[10];
                    OTP_NO_FLAG = arr_logindetails[11];
                    CASHLESS_FLAG = arr_logindetails[12];
                    MOBILE_NO_FLAG = arr_logindetails[13];
                    RTA_DATA_FLAG = arr_logindetails[14];
                    DL_DATA_FLAG = arr_logindetails[15];

                    DataBase helper = new DataBase(getApplicationContext());

                    ContentValues values = new ContentValues();
                    values.put("PID_CODE", PID_CODE1);
                    values.put("PID_NAME", PID_NAME1);
                    values.put("PS_CODE", PS_CODE1);
                    values.put("PS_NAME", PS_NAME1);
                    values.put("CADRE_CODE", CADRE_CODE1);
                    values.put("CADRE_NAME", CADRE_NAME1);
                    values.put("SECURITY_CD", SECURITY_CD1);


                    SQLiteDatabase db = openOrCreateDatabase(DataBase.DATABASE_NAME, MODE_PRIVATE, null);
                    db.execSQL("DROP TABLE IF EXISTS " + DataBase.USER_TABLE);
                    db.execSQL(DataBase.CREATE_USER_TABLE);
                    db.insert(DataBase.USER_TABLE, null, values); // Inserting Row

                    SharedPreferences sharedPreferences = getSharedPreferences("loginValus", MODE_PRIVATE);
                    SharedPreferences.Editor editors = sharedPreferences.edit();
                    //23001004|TESTING PURPOSE|2300|TRAFFIC CELL|00|DEVP|7893816681|Y
                    String userName = "" + PID_NAME1;

                    //String pass_word = "" + arr_logindetails[5];
                    editors.putString("USER_NAME", userName);
                    editors.putString("PS_NAME", PS_NAME1);
                    editors.putString("PID_CODE", "" + PID_CODE1);
                    editors.putString("PID_NAME", "" + PID_NAME1);
                    editors.putString("PS_CODE", "" + PS_CODE1);
                    editors.putString("Ps_NAME", "" + PS_NAME1);
                    editors.putString("CADRE_CODE", "" + CADRE_CODE1);
                    editors.putString("CADRE_NAME", "" + CADRE_NAME1);
                    editors.putString("SECURITY_CD", "" + SECURITY_CD1);
                    editors.putString("GHMC_AUTH", "" + GHMC_AUTH);
                    editors.putString("CONTACT_NO", "" + CONTACT_NO);
                    editors.putString("AADHAAR_DATA_FLAG", "" + AADHAAR_DATA_FLAG);
                    editors.putString("TIN_FLAG", "" + TIN_FLAG);
                    editors.putString("OTP_NO_FLAG", "" + OTP_NO_FLAG);
                    editors.putString("CASHLESS_FLAG", "" + CASHLESS_FLAG);
                    editors.putString("MOBILE_NO_FLAG", "" + MOBILE_NO_FLAG);
                    editors.putString("RTA_DATA_FLAG", "" + RTA_DATA_FLAG);
                    editors.putString("DL_DATA_FLAG", "" + DL_DATA_FLAG);
                    editors.apply();
                    startActivity(new Intent(getApplicationContext(), Dashboard.class));
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
                showToast("Please check the Network and Try again!");
            }
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
                String[] psMaster_resp_array = new String[0];
                psMaster_resp_array = ServiceHelper.psMaster_resp.split("\\@");   //("\\@") this spits PS names

                for (int i = 0; i < ServiceHelper.psMaster_resp_array.length; i++) {
                    String allPSnames = ServiceHelper.psMaster_resp_array[i].split("\\:")[1];

                    Log.i("allPSnames", allPSnames);

                }
            } else {
                showToast("Please try Again");
            }

        }
    }

    public void getLocation() {
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
                this.GetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    m_locationlistner.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (m_locationlistner != null) {
                        location = m_locationlistner.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

							
							/*runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									Geocoder geocoder;
									List<Address> addresses;
									geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

									try {
										addresses = geocoder.getFromLocation(latitude, longitude, 1);
										
										String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
										String city = addresses.get(0).getLocality();
										String state = addresses.get(0).getAdminArea();
										String country = addresses.get(0).getCountryName();
										String postalCode = addresses.get(0).getPostalCode();
										String knownName = addresses.get(0).getFeatureName();
										
										Log.i("address", ""+address);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} // Here 1 represent max location result to returned, by documents it recommended 1 to 5

									
								}
							});*/
                        } else {
                            latitude = 0.0;
                            longitude = 0.0;
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        m_locationlistner.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (m_locationlistner != null) {
                            location = m_locationlistner.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
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
        IMEI = getDeviceID(telephonyManager);
    }


    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            latitude = (float) location.getLatitude();
            longitude = (float) location.getLongitude();
            // speed = location.getSpeed();
            //latitude = gps.getLatitude();
            //longitude = gps.getLongitude();
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

    String getDeviceID(TelephonyManager phonyManager) {

        String id = phonyManager.getDeviceId();
        if (id == null) {
            id = "not available";
        }

        int phoneType = phonyManager.getPhoneType();
        switch (phoneType) {
            case TelephonyManager.PHONE_TYPE_NONE:
                return id;

            case TelephonyManager.PHONE_TYPE_GSM:
                return id;

            case TelephonyManager.PHONE_TYPE_CDMA:
                return id;

            default:
                return "UNKNOWN:ID=" + id;
        }

    }

    /**************Adding Shortcut of Application**************/
    private void addShortcut() {
        Intent shortcutIntent = new Intent(getApplicationContext(), MainActivity.class);
        shortcutIntent.setAction(Intent.ACTION_MAIN);
        shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        int flags = Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT;
        shortcutIntent.addFlags(flags);

        Intent addIntent = new Intent();
        addIntent.putExtra("duplicate", false);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getResources().getString(R.string.app_name));
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource
                .fromContext(getApplicationContext(), R.drawable.logo));
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(addIntent);
    }

    /**************Adding Shortcut of Application**************/

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

        String otp_message = "Are you sure, You want to Leave Application...!";

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        alertDialogBuilder.setCustomTitle(title);
        alertDialogBuilder.setIcon(R.drawable.dialog_logo);
        alertDialogBuilder.setMessage(otp_message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

						/*Intent intent = new Intent(getApplicationContext(),	MainActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK	| Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
						finish();*/
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        alertDialog.getWindow().getAttributes();

        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        textView.setTextSize(28);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        textView.setGravity(Gravity.CENTER);

        Button btn1 = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        btn1.setTextSize(22);
        btn1.setTextColor(Color.WHITE);
        btn1.setTypeface(btn1.getTypeface(), Typeface.BOLD);
        btn1.setBackgroundColor(Color.RED);


        Button btn2 = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        btn2.setTextSize(22);
        btn2.setTextColor(Color.WHITE);
        btn2.setTypeface(btn2.getTypeface(), Typeface.BOLD);
        btn2.setBackgroundColor(Color.RED);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        switch (id) {
            case SPLASH_DIALOG:
			/*Dialog dg_splash = new Dialog(this,	android.R.style.Theme_Black_NoTitleBar_Fullscreen);
			dg_splash.setCancelable(false);
			dg_splash.setContentView(R.layout.splash);
			
			return dg_splash;*/

            case PROGRESS_DIALOG:
                ProgressDialog pd = ProgressDialog.show(this, "", "", true);
                pd.setContentView(R.layout.custom_progress_dialog);
                pd.setCancelable(false);

                return pd;

            default:
                break;
        }
        return super.onCreateDialog(id);
    }

    private void showToast(String msg) {
        Toast toast = Toast.makeText(getApplicationContext(), "" + msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        View toastView = toast.getView();
        ViewGroup group = (ViewGroup) toast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setTextSize(24);
        toastView.setBackgroundResource(R.drawable.toast_background);
        toast.show();
    }
}
