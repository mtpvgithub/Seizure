package com.mtpv.seizureInfo;

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
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.mtpv.seizure.R;
import com.mtpv.seizureHelpers.ServiceHelper;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("SimpleDateFormat")
public class ReleaseDocument extends Activity implements LocationListener {
	
	EditText et_adhaar_no, et_challan ;
	Button get_release_doc, get_release_docBYChallan,  back_btn, submit_btn ;
	
	LinearLayout challan_layout, adhaar_layout ;
	RadioButton challan_based, aadhaar_based ;
	
	public static String aadhaar_String = null, challanNo = null ;
	final int PROGRESS_DIALOG = 1;
	
	public static ArrayList<String> sb_selected_penlist;
	public static ArrayList<String> ALL_selected_penlist;
	public static StringBuffer ALL_selected_penlist_toSend; 
	public static ArrayList<String> sb_selected_penlist_positions;
	public static StringBuffer sb_selected_penlist_send  ;
	
	int pos;
	int total_amount = 0;
	CheckBox[] cb;
	TextView[] titckt_txt ;
	LinearLayout[] ll;
	ArrayList<Boolean> detained_items_status;
	
	int total_pendingChallan=0, total_pendingAmount=0;
	public static double total_amount_selected_challans = 0.0;
	
	LinearLayout ll_vhle_hstry_pending_challans, full_pending_layout, image_layout;
	ImageView release_imgv ;
	
	public static String radiobtn_value = "challan" , id_no = "1", date_tosend;
	
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
    LocationManager m_locationlistner;
	android.location.Location location;

	public static double latitude = 0.0;
	public static double longitude = 0.0;
	
	public static boolean imageFLG = false, aadhaarFLG = false, TicketFLG = false;
	
	byte[] byteArray;
	String imgString;
	
	RelativeLayout rl_vhlehstry_root_pchallans_xml ;
	
	static String date, date_tosave;
	public static String strdate1;
	private static String SelPicId="" ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_release_document);
		
		TicketFLG = false;
		imageFLG = false ;
		aadhaarFLG = false ;
		
		getLocation() ;
		
		sb_selected_penlist_positions = new ArrayList<String>();
		 sb_selected_penlist_send = new StringBuffer("");
		 ALL_selected_penlist_toSend = new StringBuffer("");
		
		ll_vhle_hstry_pending_challans = (LinearLayout) findViewById(R.id.ll_vhle_hstry_pchallans_xml);
		image_layout = (LinearLayout)findViewById(R.id.image_layout);
		
		rl_vhlehstry_root_pchallans_xml = (RelativeLayout)findViewById(R.id.rl_vhlehstry_root_pchallans_xml);
		rl_vhlehstry_root_pchallans_xml.setVisibility(View.GONE);
		
		release_imgv = (ImageView)findViewById(R.id.release_imgv);
		
		challan_layout = (LinearLayout)findViewById(R.id.challan_layout);
		adhaar_layout = (LinearLayout)findViewById(R.id.adhaar_layout);
		adhaar_layout.setVisibility(View.GONE);
		
		et_adhaar_no = (EditText)findViewById(R.id.et_aadhaar);
		et_challan = (EditText)findViewById(R.id.et_challan);
		
		get_release_doc = (Button)findViewById(R.id.get_release_doc);
		get_release_docBYChallan = (Button)findViewById(R.id.get_release_docBYChallan);
		
		back_btn = (Button)findViewById(R.id.back_btn);
		submit_btn = (Button)findViewById(R.id.submit_btn);
		
		challan_based = (RadioButton)findViewById(R.id.challan_based);
		aadhaar_based = (RadioButton)findViewById(R.id.aadhaar_based);

		date = (DateFormat.format("dd/MM/yyyy hh:mm:ss", new java.util.Date()).toString());
		date = (DateFormat.format("dd/MM/yyyy hh:mm:ss", new java.util.Date()).toString());
		Calendar c1 = Calendar.getInstance();
		int mSec = c1.get(Calendar.MILLISECOND);
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String strdate1 = sdf1.format(c1.getTime());
		date_tosave = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
		//id_date.setText(strdate1);
		//Current_Date=strdate1;
		
		//et_challan.setText("HYD003B151000078");
		
		challan_based.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				radiobtn_value = "challan" ;
				id_no = "1";
				
				et_challan.setHint("Enter E-Ticket Number");
				/*et_challan.setText("");*/
				et_challan.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
				
				et_challan.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
				et_challan.setInputType(InputType.TYPE_CLASS_TEXT);
				et_challan.requestFocus();
			}
		});
		
		
		release_imgv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ReleaseDocument.SelPicId = "1";
				selectImage();

			}
		});
		
		aadhaar_based.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				radiobtn_value = "aadhaar" ;
				id_no = "2";
				
				et_challan.setHint("Enter Aadhaar Number");
				//et_challan.setText("754065178809");
				et_challan.setText("");
				int maxLength = 12 ;
				et_challan.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
				et_challan.setInputType( InputType.TYPE_CLASS_NUMBER);
				et_challan.requestFocus();
			}
		});
		
		get_release_docBYChallan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				VerhoeffCheckDigit ver=new VerhoeffCheckDigit();
				
				if (radiobtn_value.equals("challan")) {
					challanNo = et_challan.getText().toString().trim();
					
					if (et_challan.getText().toString().trim().equals("")) {
						et_challan.setError(Html.fromHtml("<font color='black'>Please Enter E-Ticket Number</font>"));
						et_challan.requestFocus();
					}else {
						TicketFLG = false;
						new Async_GetDetainedbyChallan().execute();
					}
				}else if (radiobtn_value.equals("aadhaar")) {
					challanNo = et_challan.getText().toString().trim();

					if (challanNo==null && challanNo.equals("")) {
						et_challan.setError(Html.fromHtml("<font color='black'>Please Enter Aadhaar Number</font>"));
						et_challan.requestFocus();
					}else if (challanNo!=null && challanNo.length()!=12) {
						et_challan.setError(Html.fromHtml("<font color='black'>Please Enter Valid Aadhaar Number</font>"));
						et_challan.requestFocus();
						
					}else if (challanNo!=null && challanNo.length()==12 && !ver.isValid(challanNo)) {
						et_challan.setError(Html.fromHtml("<font color='black'>Please Enter Valid Aadhaar Number</font>"));
						et_challan.requestFocus();
						
					}else if (challanNo!=null && challanNo.length()==12 && ver.isValid(challanNo)) {
						
						new Async_GetDetainedbyChallan().execute();
						
					}
				}
				
			}
		});
		
		get_release_doc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				aadhaar_String = et_adhaar_no.getText().toString().trim();
				
				
			}
		});
		
		
		back_btn.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					finish();	
				}
			});

		submit_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				VerhoeffCheckDigit ver=new VerhoeffCheckDigit();
				
				if (!et_challan.getText().toString().trim().equals("") && TicketFLG) {
				
					if (imageFLG && imgString == null
							&& image_layout.getVisibility() == View.VISIBLE) {
						showToast("Please Capture Encroachment Image !!!");
					} else if (aadhaarFLG && adhaar_layout.getVisibility() == View.VISIBLE) {
						if (et_adhaar_no.getText().toString().trim().equals("")) {
							et_adhaar_no.setError(Html
									.fromHtml("<font color='black'>Please Enter Aadhaar Number</font>"));
							et_adhaar_no.requestFocus();
						} else if (et_adhaar_no.getText().toString().trim() != null
								&& et_adhaar_no.getText().toString().trim()
										.length() != 12) {
							et_adhaar_no.setError(Html
									.fromHtml("<font color='black'>Please Enter Valid Aadhaar Number</font>"));
							et_adhaar_no.requestFocus();
						} else if (et_adhaar_no.getText().toString().trim() != null
								&& et_adhaar_no.getText().toString().trim()
										.length() == 12
								&& !ver.isValid(et_adhaar_no.getText()
										.toString().trim())) {
							et_adhaar_no.setError(Html
									.fromHtml("<font color='black'>Please Enter Valid Aadhaar Number</font>"));
							et_adhaar_no.requestFocus();
						} else if (et_adhaar_no.getText().toString().trim() != null
								&& et_adhaar_no.getText().toString().trim()
										.length() == 12
								&& ver.isValid(et_adhaar_no.getText()
										.toString().trim())) {

							new Async_Submit().execute();
						}
					} else if (!aadhaarFLG && !imageFLG) {
						new Async_Submit().execute();
					}else if (!aadhaarFLG && imageFLG) {
						new Async_Submit().execute();
					}else if (aadhaarFLG && !imageFLG) {
						new Async_Submit().execute();
					}
				}else {
					showToast("Please Enter E- Ticket Number and Click on Get Button");
				}
			}
		});
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
					m_locationlistner.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
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
						m_locationlistner.requestLocationUpdates(
								LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
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

	protected void selectImage() {

		if (ReleaseDocument.SelPicId.equals("1")){
		// TODO Auto-generated method stub
		final CharSequence[] options = { "Open Camera", "Choose from Gallery", "Cancel" };
		AlertDialog.Builder builder = new AlertDialog.Builder(ReleaseDocument.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {

        	
        	@Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Open Camera"))
                {
                   Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                   File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                   intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                   startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                	
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                 else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
        	
            }
        });
        builder.show();
		}
    
	}
	
	@SuppressWarnings("unused")
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
        	String picturePath="";
        	if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());

                for (File temp : f.listFiles()) {

                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
	                	String current_date = date_tosave;
	            		Calendar c1 = Calendar.getInstance();
	            		int mSec = c1.get(Calendar.MILLISECOND);
	            		SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	            		String strdate1 = sdf1.format(c1.getTime());
	            		String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
	            		//id_date.setText(strdate1);
	            		
	                    Bitmap bitmap;
	                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
	                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
	                            bitmapOptions);
	                    
	                    String path = android.os.Environment
	                            .getExternalStorageDirectory()
	                            + File.separator
	                            + "Tab39B" + File.separator + "ReleaseDocuments" + File.separator + current_date;
	                    File camerapath = new File(path);
                    
                    if(!camerapath.exists()){
                    	camerapath.mkdirs();
                    }
	                    f.delete();
	                    OutputStream outFile = null;
	                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        
                    	outFile = new FileOutputStream(file);
                        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                    	Canvas canvas = new Canvas(mutableBitmap); //bmp is the bitmap to dwaw into

                    	Paint paint= new Paint();
                    	paint.setColor(Color.RED);
                    	paint.setTextSize(80);
                    	paint.setTextAlign(Paint.Align.CENTER);
                    	
                    	int xPos = (canvas.getWidth() / 2);
                    	int yPos = (int) ((canvas.getHeight() / 2) - ((paint
                    	.descent() + paint.ascent()) / 2));

                    	canvas.drawText("Date & Time: " + strdate1, xPos,
                    	yPos + 400, paint);
                    	canvas.drawText("Lat :" + latitude , xPos, yPos + 500, paint);
                    	canvas.drawText("Long :"+ longitude, xPos, yPos + 600, paint);
                    	//canvas.drawText("Date & Time: "+Current_Date+"\n"+" Lat :"+latitude+ " Long :"+longitude,1250, 1500, paint);
                    
                        mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if("1".equals(ReleaseDocument.SelPicId)  && bitmap!=null){
                    	Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                    	Canvas canvas = new Canvas(mutableBitmap); //bmp is the bitmap to dwaw into

                    	Paint paint= new Paint();
                    	paint.setColor(Color.RED);
                    	paint.setTextSize(80);
                    	paint.setTextAlign(Paint.Align.CENTER);
                    	int xPos = (canvas.getWidth() / 2);
                    	int yPos = (int) ((canvas.getHeight() / 2) - ((paint
                    	.descent() + paint.ascent()) / 2));

                    	canvas.drawText("Date & Time: " + strdate1, xPos,
                    	yPos + 400, paint);
                    	canvas.drawText("Lat :" + latitude , xPos, yPos + 500, paint);
                    	canvas.drawText("Long :"+ longitude, xPos, yPos + 600, paint);
                    	//canvas.drawText("Date & Time: "+Current_Date+"\n"+" Lat :"+latitude+ " Long :"+longitude,1250, 1500, paint);
                    
                    	release_imgv.setImageBitmap(mutableBitmap);
                    	//picture1.setRotation(90);
                    	
                    	ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    	mutableBitmap.compress(Bitmap.CompressFormat.JPEG,20, bytes);
                        
                        byteArray = bytes.toByteArray();
                    	
                    	imgString = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                    }	
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } 
        	else if (requestCode == 2) {
            	Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                picturePath = c.getString(columnIndex);
                c.close();
                
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.w("path of image from gallery......******************.........", picturePath+"");
                
                if("1".equals(ReleaseDocument.SelPicId) && thumbnail!=null){
                	Bitmap mutableBitmap = thumbnail.copy(Bitmap.Config.ARGB_8888, true);
                //	Canvas canvas = new Canvas(mutableBitmap); //bmp is the bitmap to dwaw into
//                	canvas.drawText("Date & Time: " + Current_Date, xPos,
//                	yPos + 800, paint);
//                	canvas.drawText("Lat :" + latitude , xPos, yPos + 900, paint);
//                	canvas.drawText("Long :"+ longitude, xPos, yPos + 1000, paint);
                	
                	release_imgv.setImageBitmap(mutableBitmap);
                	Log.i("image selected :::", ""+mutableBitmap);
                	
                	//picture1.setRotation(90);
                	ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                	mutableBitmap.compress(Bitmap.CompressFormat.JPEG,20, bytes);
                    
                    byteArray = bytes.toByteArray();
                	
                	imgString = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                	
                }
	        }
        }
	    } 

	private class Async_GetDetainedbyChallan extends AsyncTask<Void, Void, String>{

		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showDialog(PROGRESS_DIALOG);
		}
		
		@Override
		protected String doInBackground(Void... params) {
			
    		ServiceHelper.getDetainedItems(""+MainActivity.uintCode,""+challanNo);
			
			return null;
		}
		
		
		@SuppressWarnings({ "deprecation", "unused" })
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			removeDialog(PROGRESS_DIALOG);
			if (null!=ServiceHelper.detainedBy_challanNo_resp){

			if (!ServiceHelper.detainedBy_challanNo_resp.equals("0") &&
					!ServiceHelper.detainedBy_challanNo_resp.equals("anyType{}") &&
					!ServiceHelper.detainedBy_challanNo_resp.equals("NA")
					) {

				TicketFLG = true;
				rl_vhlehstry_root_pchallans_xml.setVisibility(View.VISIBLE);

				// HYD003B161000053!20/12/2016!Y!N!GGGG,HHHH@


				date_tosend = "" + ServiceHelper.pending_challans_master[1];

				if (ServiceHelper.pending_challans_master[2].equals("N")) {
					aadhaarFLG = true;
					adhaar_layout.setVisibility(View.VISIBLE);
					aadhaar_String = et_adhaar_no.getText().toString().trim();
				} else {
					aadhaarFLG = false;
					adhaar_layout.setVisibility(View.GONE);
				}

				if (ServiceHelper.pending_challans_master[3].equals("N")) {
					imageFLG = true;
					image_layout.setVisibility(View.VISIBLE);
				} else {
					imageFLG = false;
					image_layout.setVisibility(View.GONE);
				}

				sb_selected_penlist = new ArrayList<String>();
				ALL_selected_penlist = new ArrayList<String>();

				sb_selected_penlist.clear();
				ALL_selected_penlist.clear();
				sb_selected_penlist_positions.clear();
				sb_selected_penlist_send.delete(0, sb_selected_penlist_send.length());
				ALL_selected_penlist_toSend.delete(0, ALL_selected_penlist_toSend.length());


				detained_items_status = new ArrayList<Boolean>();

				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				StringBuffer myId = new StringBuffer();

				params.setMargins(0, 0, 0, 10);
				myId.delete(0, myId.length());

				//cb = new CheckBox[ServiceHelper.pending_challans_master.length];
				titckt_txt = new TextView[ServiceHelper.pending_challans_master.length];

				ll_vhle_hstry_pending_challans.removeAllViews();
				ll = new LinearLayout[ServiceHelper.pending_challans_master.length];

				for (int i = 0; i < 1; i++) {
					total_pendingChallan++;

					ll[i] = new LinearLayout(getApplicationContext());
					ll[i].setId(i);
					ll[i].setLayoutParams(params);
					ll[i].setOrientation(LinearLayout.HORIZONTAL);

					titckt_txt[i] = new TextView(getApplicationContext());
					android.widget.LinearLayout.LayoutParams params1 = new android.widget.LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f);
					int identifier = getResources().getIdentifier(getApplicationContext().getPackageName() + ":drawable/custom_chec_box", null, null);
					titckt_txt[i].setText("\t\t\t\t" + ServiceHelper.pending_challans_master[0].toString().trim() + "\t\t\t\t\t\t" + ServiceHelper.pending_challans_master[4].toString().trim());

					//total_pendingAmount = total_pendingAmount+Integer.parseInt(ServiceHelper.pending_challans_master[i][7].toString().trim());

					//titckt_txt[i].setButtonDrawable(identifier);
					titckt_txt[i].setTextAppearance(getApplicationContext(), R.style.navi_text_style);
					titckt_txt[i].setId(i);

					ll[i].addView(titckt_txt[i]);
					detained_items_status.add(true);

					ALL_selected_penlist.add("" + ServiceHelper.pending_challans_master[0].toString().trim() + "@" +
							ServiceHelper.pending_challans_master[1].toString().trim() + "@" +
							ServiceHelper.pending_challans_master[2].toString().trim() + "@" +
							ServiceHelper.pending_challans_master[3].toString().trim() + "@" +
							ServiceHelper.pending_challans_master[4].toString().trim() + "$");


					ll_vhle_hstry_pending_challans.addView(ll[i]);
				}

				//Log.i("total_pendingChallan  ::::::", ""+total_pendingChallan+" and "+total_pendingAmount);

			} else {
				TicketFLG = false;
				showToast("No Detained Items Found !!!");
			}
		}else{
				showToast("Please check the Network and Try Again!");
			}
	  }
	}
	
	
	private class Async_Submit extends AsyncTask<Void, Void, String>{

		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			showDialog(PROGRESS_DIALOG);
		}
		
		@Override
		protected String doInBackground(Void... params) {
			/*String currentDate = (DateFormat.format("dd/MM/yyyy hh:mm:ss", new java.util.Date()).toString());
    		Calendar c1 = Calendar.getInstance();
    		int mSec = c1.get(Calendar.MILLISECOND);
    		SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    		String strdate1 = sdf1.format(c1.getTime());
    		String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());*/
    		
			ServiceHelper.getReleaseItemsSubmit(""+MainActivity.uintCode, challanNo, aadhaar_String, date_tosend, MainActivity.PID_CODE1, 
					MainActivity.PID_NAME1, imgString);
			return null;
		}
		
		
		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			removeDialog(PROGRESS_DIALOG);
			if(ServiceHelper.ReleaseDocSubmit_resp==null){
				showToast("Releasing Detained Items Failed !!!");
			}else if (ServiceHelper.ReleaseDocSubmit_resp.equals("SUCCESS")) {
				TextView title = new TextView(ReleaseDocument.this);
				title.setText("SEIZURE");
				title.setBackgroundColor(Color.RED);
				title.setGravity(Gravity.CENTER);
				title.setTextColor(Color.WHITE);
				title.setTextSize(26);
				title.setTypeface(title.getTypeface(), Typeface.BOLD);
				title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.dialog_logo, 0, R.drawable.dialog_logo, 0);
				title.setPadding(20, 0, 20, 0);
				title.setHeight(70);
				
				String otp_message = "Detained Items \n Released Successfully" ;
				
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReleaseDocument.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
				alertDialogBuilder.setCustomTitle(title);
				alertDialogBuilder.setIcon(R.drawable.dialog_logo);
				alertDialogBuilder.setMessage(otp_message);
				alertDialogBuilder.setCancelable(false);
				alertDialogBuilder.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								imageFLG = false ;
								aadhaarFLG = false ;
								TicketFLG = false ;
								
								Intent intent = new Intent(ReleaseDocument.this, Dashboard.class);
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
			
			}else if(ServiceHelper.ReleaseDocSubmit_resp.equals("FAIL")){
				showToast("Releasing Detained Items Failed !!!");
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
	 protected Dialog onCreateDialog(int id) {
	        switch (id) {
	        case PROGRESS_DIALOG:
				ProgressDialog pd = ProgressDialog.show(this, "", "",	true);
				pd.setContentView(R.layout.custom_progress_dialog);
				pd.setCancelable(false); 
				
				return pd;
	        }
	        return null;
	    }

	 @Override
		public void onLocationChanged(Location location) {
			if (location != null) {
				latitude = (float) location.getLatitude();
				longitude = (float) location.getLongitude();
				// speed = location.getSpeed();
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
