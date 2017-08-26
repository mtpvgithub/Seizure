package com.mtpv.seizureInfo;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import com.mtpv.seizure.R;
import com.mtpv.seizureHelpers.DataBase;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Settings extends Activity {
    private Button mActivateBtn;
    private Button mPairedBtn;
    private Button mScanBtn;

    Button save, cancel_btn, scan_bluetooth;
    public static ListView bluetooth_list;
    public static EditText et_bt_address;

    final int PROGRESS_DIALOG = 2;
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

    BluetoothAdapter bluetoothAdapter;
    static final UUID MY_UUID = UUID.randomUUID();
    ArrayAdapter<String> btArrayAdapter;
    @SuppressWarnings("unused")
    private static final int REQUEST_ENABLE_BT = 1;
    String address = "";

    private ProgressDialog mProgressDlg;

    private ArrayList<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();

    public static BluetoothAdapter mBluetoothAdapter;

    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static String BLT_Name = "";

    ImageView update_apk;

    public static boolean bluetoothFLG = false, pinpadFLG = false;

    public static String BTprinter_Adress = "", BTprinter_Name = "";

    @SuppressWarnings("unused")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_settings);

        ffd = (TextView) findViewById(R.id.ffd);

        save = (Button) findViewById(R.id.save);
        cancel_btn = (Button) findViewById(R.id.cancel_Btn);
        scan_bluetooth = (Button) findViewById(R.id.scan_btn);

        mActivateBtn = (Button) findViewById(R.id.btn_enable);
        mPairedBtn = (Button) findViewById(R.id.btn_view_paired);
        mScanBtn = (Button) findViewById(R.id.btn_scan);

        et_bt_address = (EditText) findViewById(R.id.buletooth_text);

        update_apk = (ImageView) findViewById(R.id.update_apk);

        if (et_bt_address.getText().toString().equals("")) {
            DataBase helper = new DataBase(getApplicationContext());
            try {
                android.database.sqlite.SQLiteDatabase db = openOrCreateDatabase(DataBase.DATABASE_NAME, MODE_PRIVATE,
                        null);
                String selectQuery = "SELECT  * FROM " + DataBase.Bluetooth;
                // SQLiteDatabase db = this.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);
                // looping through all rows and adding to list

                if (cursor.moveToFirst()) {
                    do {

                        // Log.i("1 :",""+ cursor.getString(0));
                        BLT_Name = cursor.getString(0);

                        et_bt_address.setText(BLT_Name);

                    } while (cursor.moveToNext());
                }
                db.close();
            } catch (Exception e) {
                e.printStackTrace();

            }

        }

        save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!et_bt_address.getText().toString().trim().equals("")) {
                    showToast("Blutooth Device Saved Successfully..!");
                    String n = et_bt_address.getText().toString();

                    SharedPreferences.Editor editor = getSharedPreferences(mypreference, MODE_PRIVATE).edit();
                    editor.putString("BLT_Name", n);
                    editor.commit();

                    DataBase helper = new DataBase(getApplicationContext());

                    ContentValues values = new ContentValues();
                    values.put("BT_Name", n);
                    SQLiteDatabase db = openOrCreateDatabase(DataBase.DATABASE_NAME, MODE_PRIVATE, null);
                    // db.execSQL("delete from " + DataBase.USER_TABLE);
                    db.execSQL("DROP TABLE IF EXISTS " + DataBase.Bluetooth);
                    db.execSQL(DataBase.CREATE_Bluetooth);
                    db.insert(DataBase.Bluetooth, null, values);
                    System.out.println(
                            "*********************OFFICER TABLE Insertion Successfully **********************************");
                    finish();
                } else {
                    showToast("Please Scan Bluetooth Device");
                    et_bt_address.setError(Html.fromHtml("<font color='black'>Please Enter Bluetooth Address</font>"));
                    et_bt_address.requestFocus();
                }

            }
        });

        cancel_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        update_apk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new Async_UpdateApk().execute();
            }
        });

        bluetooth_list = (ListView) findViewById(R.id.listview_devicesfound);

		/* GETTING BLUETOOTH ADDRESS */

	
		/* BLUETOOTH CONNECTIVITY */
        // if (et_bt_address.getText().toString().trim().equals("")) {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        CheckBlueToothState();
        registerReceiver(ActionFoundReceiver, new IntentFilter(
                BluetoothDevice.ACTION_FOUND));
        btArrayAdapter = new ArrayAdapter<String>(Settings.this,
                android.R.layout.simple_list_item_1);
        bluetooth_list.setAdapter(btArrayAdapter);

        bluetooth_list.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.i("bluetoothFLG and pinpadFLG********", "" + bluetoothFLG
                        + " And " + pinpadFLG);

                if (bluetoothFLG) {
                    Log.i("bluetoothFLG Bluetooth********", "" + bluetoothFLG);
                    /**
                     * Toast.makeText(getApplicationContext(),
                     * ""+listDevicesFound.getCount(),
                     * Toast.LENGTH_SHORT).show();
                     */
                    String selection = (String) (bluetooth_list
                            .getItemAtPosition(position));

                    Log.i("selection Bluetooth********", "" + selection);

                    Toast.makeText(getApplicationContext(),
                            "BLUETOOTH ADDRESS IS SAVED SUCCESSFULLY",
                            Toast.LENGTH_SHORT).show();
                    address = "";
                    address = selection.substring(0, 17);
                    et_bt_address.setText(address);

                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    Alertmessage();
                } else if (pinpadFLG) {
                    Log.i("pinpadFLG pinpadFLG********", "" + pinpadFLG);
                    /**
                     * Toast.makeText(getApplicationContext(),
                     * ""+listDevicesFound.getCount(),
                     * Toast.LENGTH_SHORT).show();
                     */
                    String selection = (String) (bluetooth_list
                            .getItemAtPosition(position));

                    Log.i("selection pinpadFLG********", "" + selection);

                    Toast.makeText(getApplicationContext(),
                            "PINPAD ADDRESS IS SAVED SUCCESSFULLY",
                            Toast.LENGTH_SHORT).show();
                    address = "";
                    address = selection.substring(0, 17);
                    //et_pinpad.setText(address);

                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    Alertmessage();
                }

            }
        });
		
		
		
		
/*
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		mProgressDlg = new ProgressDialog(this);

		mProgressDlg.setMessage("Please Wait Bluetooth is Scanning...");
		mProgressDlg.setCancelable(false);
		mProgressDlg.setInverseBackgroundForced(false);
		mProgressDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

				mBluetoothAdapter.cancelDiscovery();
			}
		});

		if (mBluetoothAdapter == null) {
			showUnsupported();
		} else {
			mPairedBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

					if (pairedDevices == null || pairedDevices.size() == 0) {
						showToast("No Paired Devices Found");
					} else {
						ArrayList<BluetoothDevice> list = new ArrayList<BluetoothDevice>();

						list.addAll(pairedDevices);

						Intent intent = new Intent(Settings.this, DeviceListActivity.class);

						intent.putParcelableArrayListExtra("device.list", list);

						startActivity(intent);
					}
				}
			});*/

        scan_bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //mBluetoothAdapter.startDiscovery();


                // 21-Aug-17
                bluetoothFLG = true;
                pinpadFLG = false;

                ffd.setVisibility(View.VISIBLE);
                final ProgressDialog progressDialog = new ProgressDialog(
                        Settings.this);
                progressDialog
                        .setMessage("Please wait BlueTooth Scan is in Process!!!");
                progressDialog.setCancelable(false);
                progressDialog.show();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btArrayAdapter.clear();
                        bluetoothAdapter.cancelDiscovery();
                        bluetoothAdapter.startDiscovery();
                    }
                });

                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Thread.sleep(6000);
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

            }
        });

        mActivateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBluetoothAdapter.isEnabled()) {
                    mBluetoothAdapter.disable();

                    showDisabled();
                } else {
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

                    startActivityForResult(intent, 1000);
                }
            }
        });

    }

    private void showEnabled() {
        showToast("Bluetooth is On");

        mActivateBtn.setText("Disable");
        mActivateBtn.setEnabled(false);

        mPairedBtn.setEnabled(false);
        mScanBtn.setEnabled(false);
    }

    private void showDisabled() {
        showToast("Bluetooth is Off");

        mActivateBtn.setText("Enable");
        mActivateBtn.setEnabled(false);

        mPairedBtn.setEnabled(false);
        mScanBtn.setEnabled(false);
    }

    private void showUnsupported() {
        showToast("Bluetooth is unsupported by this device");

        mActivateBtn.setText("Enable");
        mActivateBtn.setEnabled(false);

        mPairedBtn.setEnabled(false);
        mScanBtn.setEnabled(false);
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
            // TODO Auto-generated method stub

            FTPClient ftpClient = new FTPClient();

            if (null != MainActivity.services_url
                    && MainActivity.services_url.equals("https://www.echallan.org/")) {
                server = IPsettings.open_ftp_fix;
            } else {
                server = IPsettings.ftp_fix;
            }

            try {
                ftpClient.connect(server, port);
                ftpClient.login(username, password);
                ftpClient.enterLocalPassiveMode();
                ftpClient.setBufferSize(1024 * 1024);
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                File downloadFile1 = new File("/sdcard/Download/39BHYD.apk");
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
                            // TODO Auto-generated method stub
                            removeDialog(PROGRESS_DIALOG);

                            // showToast("your is Upto Date");
                            TextView title = new TextView(Settings.this);
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

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Settings.this,
                                    AlertDialog.THEME_HOLO_LIGHT);
                            alertDialogBuilder.setCustomTitle(title);
                            alertDialogBuilder.setIcon(R.drawable.dialog_logo);
                            alertDialogBuilder.setMessage(otp_message);
                            alertDialogBuilder.setCancelable(false);
                            alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

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
                                cur_val.setText((int) per / 1500000 + "%");
                            }
                        });
                    }
                    fileOutput.close();
                    outputStream.close();
                    if (success) {
                        ftpClient.logout();
                        ftpClient.disconnect();
                        finish();
                        System.out.println("File #1 has been downloaded successfully.");
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(
                                Uri.fromFile(new File(
                                        Environment.getExternalStorageDirectory() + "/download/" + "39BHYD.apk")),
                                "application/vnd.android.package-archive");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }

            } catch (SocketException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            // removeDialog(PROGRESS_DIALOG);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
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

    @SuppressWarnings("deprecation")
    private void showProgress(String server) {
        // TODO Auto-generated method stub
        dialog = new Dialog(Settings.this);
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
        progress.setProgress(0);// initially progress is 0
        progress.setMax(100);
        progress.setIndeterminate(true);
        progress.setProgressDrawable(getResources().getDrawable(R.drawable.green_progress));
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                if (state == BluetoothAdapter.STATE_ON) {
                    showToast("Enabled");

                    showEnabled();
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                mDeviceList = new ArrayList<BluetoothDevice>();

                mProgressDlg.show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                mProgressDlg.dismiss();

                Intent newIntent = new Intent(Settings.this, DeviceListActivity.class);
                newIntent.putParcelableArrayListExtra("device.list", mDeviceList);
                startActivity(newIntent);

            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                mDeviceList.add(device);

                showToast("Found device " + device.getName());
            }
        }
    };

    /* BLUETOOTH CONNECTIVITY */
    private void CheckBlueToothState() {
        // TODO Auto-generated method stub
        if (bluetoothAdapter == null) {
            ffd.setText("Bluetooth NOT support");
        } else {
            if (bluetoothAdapter.isEnabled()) {
                if (bluetoothAdapter.isDiscovering()) {
                    ffd.setText("Bluetooth is currently in device discovery process");
                } else {
                    ffd.setText("Bluetooth is Enabled");
                    scan_bluetooth.setEnabled(true);
                }
            } else {
                ffd.setText("Bluetooth is NOT Enabled!");
                Intent enableBtIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }


    /* BLUETOOTH CONNECTIVITY */
    private final BroadcastReceiver ActionFoundReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                btArrayAdapter.add(device.getAddress() + "\n"
                        + device.getName());
                btArrayAdapter.notifyDataSetChanged();

                BTprinter_Adress = device.getAddress();
                BTprinter_Name = device.getName();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode == REQUEST_ENABLE_BT) {
            CheckBlueToothState();
        }
    }

    public void Alertmessage() {

        if (mBluetoothAdapter == null) {
            showToast("Bluetooth is not available");
            return;
        }
        if (!mBluetoothAdapter.isEnabled()) {
            showToast("Please enable your BT and re-run this program");
            finish();
            return;
        }
    }

}
