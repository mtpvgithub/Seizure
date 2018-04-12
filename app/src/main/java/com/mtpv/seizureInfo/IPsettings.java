package com.mtpv.seizureInfo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.mtpv.seizure.R;

public class IPsettings extends Activity implements OnClickListener {

    EditText et_service_url;
    EditText et_ftp_url;
    Button btn_back_ip;
    Button btn_save;
//	Button btn_cancel;

    RadioGroup rg_live_test;
    RadioButton rbtn_liveTest;
    RadioButton rbtn_live;// just for pref enabling
    RadioButton rbtn_test;// just for pref enabling

    SharedPreferences preference;
    SharedPreferences.Editor editor;

    String SERVICE_URL_PREF = "";
    String FTP_URL_PREF = "";
    String SERVICE_TYPE_PREf = "";


    /* TO SET FIRST TIME SERVICE & FTP DETAILS START */
    /*private String test_service_url = "http://192.168.11.4/eTicketMobileTest";*/
    /*-----below is commented just to use the wifi : 22-01-2015-----*/
    //public static String test_service_url = "http://192.168.11.55:8080/";
    public static String test_service_url = "http://192.168.11.10:8080/";//125.16.1.70:8080
    // public static String test_service_url = "http://192.168.11.4/";


    @SuppressWarnings("unused")
    private String test_service_url3 = "http://192.168.11.4";
    @SuppressWarnings("unused")
    private String test_service_url4 = "http://192.168.11.4";
    //private String test_service_url = "http://192.168.11.8/39BService";

    //private String live_service_url = "http://192.168.11.4";

    //private String ftp_fix = "192.168.11.9:99";
    String service_type = "live";

    private String live_service_url = "https://www.echallan.org/";

    public static String open_ftp_fix = "125.16.1.69:99";
    public static String ftp_fix = "192.168.11.9:99";


    @SuppressWarnings("deprecation")
    @SuppressLint("WorldReadableFiles")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ipsettings);
        LoadUIComponents();

        preference = getSharedPreferences("preferences", MODE_PRIVATE);
        editor = preference.edit();
        SERVICE_TYPE_PREf = preference.getString("servicetype", "live");
        SERVICE_URL_PREF = preference.getString("serviceurl", "url1");
        FTP_URL_PREF = preference.getString("ftpurl", "url2");

        //Log.i("IP SETTINGS", "****vals****");
		/*---------SERVICE TYPE-------------*/

		/*---------SERVICE URL-------------*/
        rbtn_live = (RadioButton) findViewById(R.id.radioButton_live);
        rbtn_test = (RadioButton) findViewById(R.id.radioButton_test);

//		if (!SERVICE_URL_PREF.equals("url1")) {
//			et_service_url.setText(SERVICE_URL_PREF);
//		} 

        if (SERVICE_TYPE_PREf.equals("live")) {
            rbtn_live.setChecked(true);
            et_service_url.setText("" + live_service_url);
        } else {
            et_service_url.setText("" + test_service_url);
            rbtn_test.setChecked(true);
        }
		

		/*---------FTP URL-------------*/
        if (!FTP_URL_PREF.equals("url2")) {
            et_ftp_url.setText(FTP_URL_PREF);
        } else {

            editor.putString("ftpurl", "" + ftp_fix);
            et_ftp_url.setText("" + ftp_fix);
        }

        editor.commit();
    }

    private void LoadUIComponents() {
        // TODO Auto-generated method stub
        et_service_url = (EditText) findViewById(R.id.edt_service_ipsettings_xml);
        et_ftp_url = (EditText) findViewById(R.id.edt_ftpurl_xml);
        btn_save = (Button) findViewById(R.id.btnsubmit_ipsettings_xml);
//		btn_cancel = (Button) findViewById(R.id.btncancel_ipsettings_xml);
        btn_back_ip = (Button) findViewById(R.id.btnback_ipsettings_xml);

        rg_live_test = (RadioGroup) findViewById(R.id.radioGroup_live_test);


        btn_save.setOnClickListener(this);
//		btn_cancel.setOnClickListener(this);
        btn_back_ip.setOnClickListener(this);

        rg_live_test.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                switch (checkedId) {
                    case R.id.radioButton_live:
                        service_type = "live";
                        et_service_url.setText(live_service_url);
                        et_ftp_url.setText(open_ftp_fix);
                        break;

                    case R.id.radioButton_test:
                        service_type = "test";
                        et_service_url.setText(test_service_url);
                        et_ftp_url.setText(ftp_fix);
                        break;

                    default:
                        break;
                }
            }
        });
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("WorldReadableFiles")
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btnsubmit_ipsettings_xml:

                if (et_service_url.getText().toString().trim().equals("")) {
                    showError(et_service_url, "Enter Service URL");
                } else if (et_ftp_url.getText().toString().trim().equals("")) {
                    showError(et_ftp_url, "Enter FTP URL");
                } else {
                    preference = getSharedPreferences("preferences", MODE_PRIVATE);
                    editor = preference.edit();
                    // preference.edit().clear().commit();
                    if (preference.contains("serviceurl")) {
                        // editor = preference.edit();
                        editor.remove("serviceurl");
                        editor.commit();
                    }

				/* TO CLEAR ONLY FTP URL */
                    if (preference.contains("ftpurl")) {
                        // editor = preference.edit();
                        editor.remove("ftpurl");
                        editor.commit();
                    }
				
				
				/* TO CLEAR ONLY SERVICETYPE URL */
                    if (preference.contains("servicetype")) {
                        // editor = preference.edit();
                        editor.remove("servicetype");
                        editor.commit();
                    }

                    String[] ftp_split = et_ftp_url.getText().toString().trim()
                            .split("\\:");

                    if ((ftp_split != null) && (ftp_split.length == 2)) {
                        editor.putString("serviceurl", "" + et_service_url.getText().toString().trim());

                        editor.putString("ftpurl", "" + et_ftp_url.getText().toString().trim());

                        editor.putString("servicetype", "" + service_type);
                        editor.commit();
                        showToast("Successfully Saved!");
                        finish();
                    } else {
                        showToast("Enter Proper FTP URL");
                    }

                }

                break;
            case R.id.btnback_ipsettings_xml:
                startActivity(new Intent(this, MainActivity.class));
                break;

            default:
                break;
        }
    }

    @SuppressWarnings("unused")
    private void clearFields() {
        // TODO Auto-generated method stub
        preference.edit().clear().commit();
        et_service_url.setText("");
        et_ftp_url.setText("");

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

    private void showError(EditText et, String msg) {
        et.setError("" + msg);
    }

}
