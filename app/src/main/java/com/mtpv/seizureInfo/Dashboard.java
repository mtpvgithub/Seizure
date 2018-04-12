package com.mtpv.seizureInfo;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mtpv.seizure.BuildConfig;
import com.mtpv.seizure.R;
import com.mtpv.seizureHelpers.DataBase;
import com.mtpv.seizureHelpers.ServiceHelper;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

public class Dashboard extends Activity {

    LinearLayout footpath_vendor, report, settings, duplicate_print, release_doc, images_layout, previous_his;
    ImageView logout, image_to_capture;
    TextView usernameTV;

    final int PROGRESS_DIALOG = 1;


    String server = "192.168.11.9";
    int port = 99;
    String username = "ftpuser";
    String password = "Dk0r$l1qMp6";
    String filename = "Version-1.5.1.apk";

    @SuppressWarnings("unused")
    private static final int BUFFER_SIZE = 4096;
    ProgressBar progress;
    Dialog dialog;
    int downloadedSize = 0;
    int totalSize = 0;
    TextView cur_val, ffd;

    DataBase db;

    public static String class_clarify = null, OtpStatus = "", OtpResponseDelayTime;
    public static String[] otp_Master;

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dashboard);

        //shop_vendor = (LinearLayout)findViewById(R.id.shop_vendor);
        footpath_vendor = (LinearLayout) findViewById(R.id.footpath_vendor);
        report = (LinearLayout) findViewById(R.id.report);
        settings = (LinearLayout) findViewById(R.id.settings);
        duplicate_print = (LinearLayout) findViewById(R.id.duplicate_print);
        previous_his = (LinearLayout) findViewById(R.id.previous_his);

        release_doc = (LinearLayout) findViewById(R.id.release_doc);

        logout = (ImageView) findViewById(R.id.logout);

        image_to_capture = (ImageView) findViewById(R.id.image_to_capture);

        usernameTV = (TextView) findViewById(R.id.usernameTV);

        SharedPreferences sharedPreferences = getSharedPreferences("loginValus", MODE_PRIVATE);
        String UserName = sharedPreferences.getString("USER_NAME", "");
        if (UserName != null) {
            usernameTV.setText("Welcome : " + UserName);
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

                String otp_message = "Are You Sure,\nDo You Want To Exit?";

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Dashboard.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                alertDialogBuilder.setCustomTitle(title);
                alertDialogBuilder.setIcon(R.drawable.dialog_logo);
                alertDialogBuilder.setMessage(otp_message);
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Cancel",
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

        if (MainActivity.SECURITY_CD1.equals("N")) {

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

            String otp_message = "\nPlease Update your Application...! \n";

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Dashboard.this,
                    AlertDialog.THEME_HOLO_LIGHT);
            alertDialogBuilder.setCustomTitle(title);
            alertDialogBuilder.setIcon(R.drawable.dialog_logo);
            alertDialogBuilder.setMessage(otp_message);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    new Async_UpdateApk().execute();
                    MainActivity.SECURITY_CD1 = "Y";
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

        }

        footpath_vendor.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                if (isOnline()) {
                    Async_getOtpStatusNTime OtpStatusNTime = new Async_getOtpStatusNTime();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
                        OtpStatusNTime.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        OtpStatusNTime.execute();
                    }
                } else {
                    showToast("Pleae check the network!");
                }
                // TODO Auto-generated method stub


            }
        });

        report.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (isOnline()) {

                    Intent report = new Intent(getApplicationContext(), Reports.class);
                    startActivity(report);
                } else {
                    showToast("Pleae check the network!");
                }
            }
        });

        duplicate_print.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (isOnline()) {
                    Intent duplicatePrint = new Intent(getApplicationContext(), DuplicatePrint.class);
                    startActivity(duplicatePrint);
                } else {
                    showToast("Pleae check the network!");
                }
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (isOnline()) {
                    Intent settings = new Intent(getApplicationContext(), Settings.class);
                    startActivity(settings);
                } else {
                    showToast("Pleae check the network!");
                }
            }
        });

        previous_his.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (isOnline()) {
                    Intent i = new Intent(Dashboard.this, PreviousHistory.class);
                    startActivity(i);
                    Log.i("URL *******", "" + MainActivity.URL);
                } else {
                    showToast("Pleae check the network!");
                }
            }
        });
        release_doc.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (isOnline()) {
                    Intent release = new Intent(getApplicationContext(), ReleaseDocument.class);
                    startActivity(release);
                } else {
                    showToast("Pleae check the network!");
                }
            }
        });

        image_to_capture.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (isOnline()) {

                    Intent images = new Intent(getApplicationContext(), CaptureImages.class);
                    startActivity(images);

                } else {
                    showToast("Pleae check the network!");
                }
            }
        });
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

    public class Async_getOtpStatusNTime extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {

            String unit_code = MainActivity.PID_CODE1.substring(0, 2);
            String rtaApproverResponse = ServiceHelper.getOtpStatusNTime(MainActivity.PID_CODE1.substring(0, 2));

            return rtaApproverResponse;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);
        }

        @Override
        protected void onPostExecute(String result) {

            removeDialog(PROGRESS_DIALOG);

            if (result != null && !result.equalsIgnoreCase("") && !result.equalsIgnoreCase("NA|NA")) {


                try {

                    otp_Master = new String[0];

                    otp_Master = result.split("\\|");

                    OtpStatus = otp_Master[0].toString() != null ? otp_Master[0].toString().trim() : "N";
                    OtpResponseDelayTime = otp_Master[1].toString() != null ? otp_Master[1].toString().trim() : "0";

                    LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        Intent footpath_vendor = new Intent(getApplicationContext(), FootPath_Vendor.class);
                        startActivity(footpath_vendor);
                    } else {
                        showGPSDisabledAlertToUser();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    OtpStatus = "N";
                    OtpResponseDelayTime = "0";

                }

            } else {
                showToast("something went Wrong Please try Again!");
            }

        }

    }

    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case PROGRESS_DIALOG:
                ProgressDialog pd = ProgressDialog.show(this, "", "", true);
                pd.setContentView(R.layout.custom_progress_dialog);
                pd.setCancelable(false);

                return pd;

        }
        return null;
    }

    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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


        String otp_message = "Are you sure, You want to Exit Application...!";

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

    class Async_UpdateApk extends AsyncTask<Void, Void, String> {

        @SuppressWarnings("deprecation")
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG);

        }

        @SuppressLint("SdCardPath")
        @Override
        protected String doInBackground(Void... arg0) {

            FTPClient ftpClient = new FTPClient();

            if (null != MainActivity.services_url
                    && MainActivity.services_url.equals("https://www.echallan.org/")) {
                server = "125.16.1.69";
            } else {
                server = "192.168.11.9";
            }

            try {
                ftpClient.connect(server, port);
                ftpClient.login(username, password);
                ftpClient.enterLocalPassiveMode();
                ftpClient.setBufferSize(1024 * 1024);
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                File downloadFile1 = new File("/mnt/sdcard/Download/39BHYD.apk");
                String remoteFile1 = "/23/TabAPK" + "/39BHYD.apk";
                OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadFile1));
                boolean success = ftpClient.retrieveFile(remoteFile1, outputStream);
                FileOutputStream fileOutput = new FileOutputStream(downloadFile1);
                InputStream inputStream = ftpClient.retrieveFileStream(remoteFile1);
                if (inputStream == null || ftpClient.getReplyCode() == 550) {
                    fileOutput.close();
                    outputStream.close();
                    runOnUiThread(new Runnable() {
                        @SuppressWarnings("deprecation")
                        @Override
                        public void run() {
                            removeDialog(PROGRESS_DIALOG);
                            TextView title = new TextView(Dashboard.this);
                            title.setText("SEIZURE");
                            title.setBackgroundColor(Color.RED);
                            title.setGravity(Gravity.CENTER);
                            title.setTextColor(Color.WHITE);
                            title.setTextSize(26);
                            title.setTypeface(title.getTypeface(), Typeface.BOLD);
                            title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0,
                                    R.drawable.dialog_logo, 0);
                            title.setPadding(20, 0, 20, 0);
                            title.setHeight(70);
                            String otp_message = "\n Your Application is Upto Date \n No Need to Update \n";
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Dashboard.this,
                                    AlertDialog.THEME_HOLO_LIGHT);
                            alertDialogBuilder.setCustomTitle(title);
                            alertDialogBuilder.setIcon(R.drawable.dialog_logo);
                            alertDialogBuilder.setMessage(otp_message);
                            alertDialogBuilder.setCancelable(false);
                            alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

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
                        }
                    });
                } else {

                    totalSize = remoteFile1.length();

                    runOnUiThread(new Runnable() {
                        @SuppressWarnings("deprecation")
                        public void run() {
                            removeDialog(PROGRESS_DIALOG);
                            showProgress(server);
                            progress.setMax(totalSize);
                        }
                    });
                    byte[] buffer = new byte[1024];
                    int bufferLength = 0;
                    while ((bufferLength = inputStream.read(buffer)) > 0) {
                        fileOutput.write(buffer, 0, bufferLength);
                        downloadedSize += bufferLength;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                progress.setProgress(downloadedSize);
                                float per = ((float) downloadedSize / totalSize) * 100;
                                cur_val.setText((int) per / 215000 + "%");
                            }
                        });
                    }
                    fileOutput.close();
                    outputStream.close();
                    if (success) {
                        dialog.dismiss();
                        ftpClient.logout();
                        ftpClient.disconnect();
                        finish();
                        if (Build.VERSION.SDK_INT <= 23) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(
                                    Uri.fromFile(new File("/mnt/sdcard/Download/39BHYD.apk")),
                                    "application/vnd.android.package-archive");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            Uri apkUri = FileProvider.getUriForFile(Dashboard.this, BuildConfig.APPLICATION_ID +
                                    ".fileProvider", new File("/mnt/sdcard/Download/39BHYD.apk"));
                            Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                            intent.setData(apkUri);
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity(intent);
                        }


                    }
                }

            } catch (SocketException | FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // removeDialog(PROGRESS_DIALOG);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }

    @SuppressWarnings("deprecation")
    private void showProgress(String server) {
        dialog = new Dialog(Dashboard.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.myprogressdialog);
        dialog.setTitle("Download Progress");
        dialog.setCancelable(false);
        TextView text = (TextView) dialog.findViewById(R.id.tv1);
        text.setText("Downloading file ... ");
        cur_val = (TextView) dialog.findViewById(R.id.cur_pg_tv);
        cur_val.setText("It may Take Few Minutes.....");
        dialog.show();
        progress = (ProgressBar) dialog.findViewById(R.id.progress_bar);
        progress.setProgress(0);
        progress.setMax(100);
        progress.setIndeterminate(true);
        progress.setProgressDrawable(getResources().getDrawable(R.drawable.green_progress));
    }

}