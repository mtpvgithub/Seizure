package com.mtpv.seizureHelpers;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

import com.mtpv.seizureInfo.MainActivity;

import java.util.Objects;

public class ServiceHelper {
	private static String NAMESPACE = "http://service.mother.com";
	private static String NAMESPACE_OtpStatus = "http://service.et.mtpv.com";
	private static String METHOD_NAME = "authenticateUser";
	public static String SOAP_ACTION = NAMESPACE + METHOD_NAME ;
	
	public static String SEND_OTP_TO_MOBILE_METHOD_NAME = "sendOTP";
	public static String VERIFY_OTP_FROM_MOBILE_METHOD_NAME = "verifyOTP";
	
	//getLocation(String gpsLatti, String gpsLongi);
	public static String Get_Address_By_LAT_LONG = "getLocation" ;
	
	 //public String authenticateUser(String pidCd, String password,String imei,String gpsLattitude,String gpsLongitude);
	public static String AUTHENTICATION = "authenticateUser" ; 
     //public String getDetainItemsMaster(String unitCode);
	public static String DETAINED_ITEMS = "getDetainItemsMaster" ;     
	    //public String getPSMaster(String unitCode);
	public static String PS_MASTER = "getPSMaster" ;    
	    //public String getPointDetailsByPscode(String pscode);
	public static String POINT_DETAILS_BY_PsCODE = "getPointDetailsByPscode" ;
	 /*   public String generateChallan(String unitCode,String unitName, String bookedPsCode,String bookedPsName,String pointCode, String pointName,
	            String operaterCd,String operaterName,    String pidCd, String pidName,String password,String cadreCD,String cadre,   
	            String onlineMode, String imageEvidence,String imgEncodedData,String offenceDt, String offenceTime,        
	            String firmRegnNo, String shopName,String shopOwnerName,String shopAddress , String location,String psCode,
	            String psName, String respondantName, String respondantFatherName,
	            String respondantAddress,     String respondantContactNo,String respondantAge, String IDCode, String IDDetails,            
	            String witness1Name, String witness1FatherName, String witness1Address,
	            String witness2Name, String witness2FatherName, String witness2Address,
	            String detainedItems,            
	            String simId, String imeiNo,String macId, String gpsLatitude, String gpsLongitude);*/
	public static String FINAL_CHALLAN = "generateChallan" ;
/*	    public String getDuplicatePrint(String offenceDate,String pidCode,String shopName,String 
	            firmRegnNo,String responsdentName);*/	
	public static String DUPLICATE_PRINT = "getDuplicatePrint" ;
//	    public String getPendingChallans(String firmRegnNo, String shopName,String respondantName );
	public static String PENDING_CHALLANS = "getPendingChallans" ; 
	    //public String getDuplicatePrintByEticket(String eticketNo);
	public static String DUPLICATE_PRINT_BY_ETICKET = "getDuplicatePrintByEticket" ;
	    //public String getReport(String offenceDate,String pidCode);
	public static String GET_REPORT = "getReport" ;
	    //public String  previousHistory(String idCode,String idValue,String firmRegnNo);
	public static String PREVIOUS_HISTORY = "previousHistory" ;
	
	public static String GET_DETAINED_ITEMS_BY_CHALLAN_NO = "",otpStatusnTime ;
	
	public static String GET_DETAINED_ITEMS_BY_AADHAAR = "" ; 
	
	public static String  login_response, detained_resp, psMaster_resp, point_by_ps_resp,
		challan_gen_resp, duplicatePrint_resp, pendingChallan_resp, dupByEticket_resp, report_resp, prevHistry_resp;
	
	public static String[] PointNamesBypsNames_master, psMaster_resp_array ; 
	
	public static String allPSnames,allPointnames, otp_Send_resp, otp_verify_resp, detainedBy_challanNo_resp, detainedByAadhaarNo_resp, ReleaseDocSubmit_resp, tinDetails_resp= null, get_lat_long_Detail_resp ;
	
	public static String[] pending_challans_master;
	public static String[][] pending_challans_details;
	
	public static String[] detainedItems_master;
	
	public static String  ghmc_hisrty_resp =null,print_resp=null,prv_hisrty_resp =null,prv_hisrty_resp_firm =null,prv_hisrty_resp_tin =null, location_masters_resp =null;
	
	public static void login(String pid, String pidpwd, String mob_imei,
			String lat, String log,String appVersion) {
		try {

			//2319130006
			//0006
			SoapObject request = new SoapObject(NAMESPACE, "" + AUTHENTICATION);
			request.addProperty("pidCd", pid);
			request.addProperty("password", pidpwd);
			request.addProperty("imei", mob_imei);
			request.addProperty("gpsLattitude", lat);
			request.addProperty("gpsLongitude", log);
			request.addProperty("appVersion", appVersion);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();
			try {
				login_response = result.toString();
				if (Objects.equals(login_response, "0") || login_response.equals("NA") || login_response.equals("anyTpe{}")) {
					login_response = "0";
				} else {
					login_response = login_response.replace("|", ":");
					MainActivity.arr_logindetails = login_response.split(":");

				}
			}catch (Exception e){
				e.printStackTrace();
				login_response = "0";
			}

		} catch (Exception E) {
			E.printStackTrace();
			login_response = "0";
		}
	}



	public static  String  getOtpStatusNTime(String unitcode)
	{
		try {
			SoapObject request = new SoapObject(NAMESPACE, "getOtpStatusNTime");

			request.addProperty("unitcCode", unitcode);



			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);


			HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();

			try {
				if(result!=null) {
					otpStatusnTime = result.toString().trim();
				}else
				{
					otpStatusnTime = "NA|NA";
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				otpStatusnTime = "NA|NA";
			}
		} catch (SoapFault fault) {

			fault.printStackTrace();
			otpStatusnTime = "NA|NA";

		} catch (Exception E) {
			E.printStackTrace();
			otpStatusnTime = "NA|NA";
		}

		return otpStatusnTime;
	}



	//previousHistory(String idCode,String idValue,String firmName, String tinNumber)
		public static void getPreviousHstryTin(String idCode, String idValue,String firmName, String tinNumber) {
			// TODO Auto-generated method stub
				try {
					SoapObject request = null;
							
							request = new SoapObject(NAMESPACE, "previousHistory");

							request.addProperty("IDCode", "" + idCode);
							request.addProperty("IdValue", "" + idValue);
							request.addProperty("ShopName", "" + firmName);
							request.addProperty("GHMCTIN", "" + tinNumber);
							

							SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
							envelope.dotNet = true;
							envelope.setOutputSoapObject(request);
							HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL);
							httpTransportSE.call(SOAP_ACTION, envelope);
							Object result = envelope.getResponse();
							
							prv_hisrty_resp_tin = result.toString();
							
							if (prv_hisrty_resp_tin.trim()==null || prv_hisrty_resp_tin.trim().equals("NA")
									|| prv_hisrty_resp_tin.trim().equals("anyType{}")|| prv_hisrty_resp_tin.trim().equals("0^NA^NA")) {
								prv_hisrty_resp_tin = null;
								
							}else {
								prv_hisrty_resp_tin = result.toString();
							}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
		}
		
		public static void getDuplicatePrintByEticket(String eticketNo) {
			// TODO Auto-generated method stub
			try {
				SoapObject request = null;
						
						request = new SoapObject(NAMESPACE, "getDuplicatePrintByEticket");

						request.addProperty("Ticket", "" + eticketNo);
						
						

						SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
						envelope.dotNet = true;
						envelope.setOutputSoapObject(request);
						Log.i("ghmc_ ::::::", ""+request);
						HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL);
						httpTransportSE.call(SOAP_ACTION, envelope);
						Object result = envelope.getResponse();
						
						print_resp = result.toString();
						
						if (print_resp.trim()==null || print_resp.trim().equals("NA")
								|| print_resp.trim().equals("anyType{}")|| print_resp.trim().equals("0^NA^NA")) {
							print_resp = null;
							
						}else {
							print_resp = result.toString();
							Log.i("ghmc_::", ""+print_resp);
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
		}
	
	/* TO GET PS_NAME */
	public static void getPsNames(String unitCd) {
		try {
			//String pidCd, String password,String imei,String gpsLattitude,String gpsLongitude
			SoapObject request = new SoapObject(NAMESPACE, "" + PS_MASTER);
			request.addProperty("unitCode", unitCd);
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();
			if (null!=result) {
				psMaster_resp = result.toString();
				if (psMaster_resp.trim() == "0" || psMaster_resp.trim().equals("NA") || psMaster_resp.trim().equals("anyTpe{}")) {
					psMaster_resp = null;
				} else {
					psMaster_resp_array = new String[0];
					psMaster_resp_array = psMaster_resp.split("\\@");   //("\\@") this spits PS names

					for (int i = 0; i < ServiceHelper.psMaster_resp_array.length; i++) {
						allPSnames = ServiceHelper.psMaster_resp_array[i].split("\\:")[1];

					}

				}
			}else{
				psMaster_resp_array = new String[0];
				psMaster_resp = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			psMaster_resp_array = new String[0];
			psMaster_resp = null;
		}
	}
	
	public static void getPreviousHstryFirm(String idCode, String idValue,String firmName, String tinNumber) {
		// TODO Auto-generated method stub
		try {
			SoapObject request = null;
			
			request = new SoapObject(NAMESPACE, "previousHistory");

			request.addProperty("IDCode", "" + idCode);
			request.addProperty("IdValue", "" + idValue);
			request.addProperty("ShopName", "" + firmName);
			request.addProperty("GHMCTIN", "" + tinNumber);
			

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("ghm:", ""+request);
			HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL);
			httpTransportSE.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();
			
			prv_hisrty_resp_firm ="";
			if (result.toString()==null || result.toString().equals("NA")
					|| result.toString().trim().equals("anyType{}")|| result.toString().trim().equals("0^NA^NA")) {
				prv_hisrty_resp_firm = "NA";
				
			}else {
				prv_hisrty_resp_firm = result.toString();
				prv_hisrty_resp_firm = "NA";

			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			prv_hisrty_resp_firm = "NA";
		}
	}
	public static void getPreviousHstry(String idCode, String idValue,String firmName, String tinNumber) {
		// TODO Auto-generated method stub
		
		try {
			SoapObject request = null;
			
			request = new SoapObject(NAMESPACE, "previousHistory");

			request.addProperty("IDCode", "" + idCode);
			request.addProperty("IdValue", "" + idValue);
			request.addProperty("ShopName", "" + firmName);
			request.addProperty("GHMCTIN", "" + tinNumber);
			

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("ghmc_::", ""+request);
			HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL);
			httpTransportSE.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();
			
			prv_hisrty_resp = result.toString();
			
			if (prv_hisrty_resp.trim()==null || prv_hisrty_resp.trim().equals("NA")
					|| prv_hisrty_resp.trim().equals("anyType{}")|| prv_hisrty_resp.trim().equals("0^NA^NA")) {
				prv_hisrty_resp = null;
				
			}else {
				prv_hisrty_resp = result.toString();
				Log.i("ghmc_p::", ""+prv_hisrty_resp);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	/* TO GET POINT_NAME */
	public static void getPointDetailsByPscode(String psCode) {
		try {
			//String pidCd, String password,String imei,String gpsLattitude,String gpsLongitude
			SoapObject request = new SoapObject(NAMESPACE, "" + POINT_DETAILS_BY_PsCODE);
			request.addProperty("pscode", psCode);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();
			if (null!=result){
			point_by_ps_resp = result.toString();

			if (point_by_ps_resp != null) {
				PointNamesBypsNames_master = new String[0];
				PointNamesBypsNames_master = point_by_ps_resp.split("\\@");   //("\\@") this spits PS names

				for (int i = 0; i < ServiceHelper.PointNamesBypsNames_master.length; i++) {
					allPointnames = ServiceHelper.PointNamesBypsNames_master[i].split("\\:")[1];

				}
			} else if (point_by_ps_resp == null || point_by_ps_resp.equals("NA") || point_by_ps_resp.equals("anyType{}")) {
				point_by_ps_resp = null;
			} else {
				point_by_ps_resp = null;
			}
		}else{
				PointNamesBypsNames_master = new String[0];
				point_by_ps_resp = null ;
			}
		} catch (Exception e) {
			e.printStackTrace();
			PointNamesBypsNames_master = new String[0];
			point_by_ps_resp = null ;
		}
	}
	
	
	/* TO GET DUPLICATE PRINT */
	public static void getDupPrint(String offceDate,String pidCd,String shpNme,String 
            firmRegNo,String responsdntName) {
		try {
			//String pidCd, String password,String imei,String gpsLattitude,String gpsLongitude
			SoapObject request = new SoapObject(NAMESPACE, "" + DUPLICATE_PRINT);
			request.addProperty("offenceDate", offceDate);
			request.addProperty("pidCode", pidCd);
			request.addProperty("shopName", shpNme);
			request.addProperty("firmRegnNo", firmRegNo);
			request.addProperty("responsdentName", responsdntName);
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();
			
			dupByEticket_resp = result.toString();
			if (dupByEticket_resp.trim().equals("NA") || dupByEticket_resp.trim().equals("anyType{}")) {
				dupByEticket_resp = null ;
			}else {
				dupByEticket_resp = result.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/* TO GET PENDING CHALLANS */
	public static void getPendngChallans(String firmRegNo, String shpName,String respndantName) {
		try {
			//String pidCd, String password,String imei,String gpsLattitude,String gpsLongitude
			SoapObject request = new SoapObject(NAMESPACE, "" + PENDING_CHALLANS);
			request.addProperty("firmRegnNo", firmRegNo);
			request.addProperty("shopName", shpName);
			request.addProperty("respondantName", respndantName);
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();
			
			pendingChallan_resp = result.toString();
			
			if (pendingChallan_resp.trim()==null || pendingChallan_resp.trim().equals("NA") || pendingChallan_resp.trim().equals("anyType{}")) {
				pendingChallan_resp = null;
			}else {
				pendingChallan_resp = result.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/* TO GET PENDING CHALLANS */
	public static void duplicatePrintByEticket(String etcketNo) {
		try {
			//String pidCd, String password,String imei,String gpsLattitude,String gpsLongitude
			SoapObject request = new SoapObject(NAMESPACE, "" + DUPLICATE_PRINT_BY_ETICKET);
			request.addProperty("eticketNo", etcketNo);
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();
			
			dupByEticket_resp = result.toString();
			if (dupByEticket_resp.trim()==null || dupByEticket_resp.trim().equals("NA") || dupByEticket_resp.trim().equals("anyType{}")) {
				dupByEticket_resp = null;
			}else {
				dupByEticket_resp = result.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* TO GET REPORTS */
	public static void reports(String offnceDate,String pidCd) {
		try {
			//String pidCd, String password,String imei,String gpsLattitude,String gpsLongitude
			SoapObject request = new SoapObject(NAMESPACE, "" + GET_REPORT);
			request.addProperty("offenceDate", offnceDate);
			request.addProperty("pidCode", pidCd);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			Log.i("request :::", ""+request);
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();
			
			report_resp = result.toString();
			if (report_resp.trim()==null || report_resp.trim().equals("NA") || report_resp.trim().equals("anyType{}")) {
				report_resp = null;
			}else {
				report_resp = result.toString();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			report_resp="NA";
		}
	}
	
	
	/* TO GET PENDING CHALLANS */
	public static void preViousHistry(String idCd,String idVal,String firmRegNo, String module_Type) {
		try {
			//String pidCd, String password,String imei,String gpsLattitude,String gpsLongitude
			SoapObject request = new SoapObject(NAMESPACE, "" + PREVIOUS_HISTORY);
			request.addProperty("idCode", idCd);
			request.addProperty("idValue", idVal);
			request.addProperty("firmRegnNo", firmRegNo);
			request.addProperty("moduleType", module_Type);//moduleType
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();
			try {

				prevHistry_resp = result.toString();
				if (prevHistry_resp.trim() == null || prevHistry_resp.trim().equals("NA") || prevHistry_resp.trim().equals("anyType{}")) {
					prevHistry_resp = null;
				} else {
					prevHistry_resp = result.toString();
				}
			}catch (Exception e){
				e.printStackTrace();
				prevHistry_resp="NA";
			}
		} catch (Exception e) {
			e.printStackTrace();
			prevHistry_resp="NA";
		}
	}
	
	

	public static void sendOTPtoMobile(String mobileNo,String date) {
		try {

			SoapObject request = new SoapObject(NAMESPACE, SEND_OTP_TO_MOBILE_METHOD_NAME);
			request.addProperty("mobileNo", mobileNo);
			request.addProperty("date", date);
			
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();

			if (null!=result) {

				otp_Send_resp = result.toString();
				if (otp_Send_resp.trim() == null || otp_Send_resp.trim().equals("NA") || otp_Send_resp.trim().equals("anyType{}")) {
					otp_Send_resp = null;
				} else {
					otp_Send_resp = result.toString();
				}
			}else{
				otp_Send_resp = "NA";
			}


		} catch (SoapFault fault) {
			otp_Send_resp = "NA";
        } 
		catch (Exception E) {
			E.printStackTrace();
			otp_Send_resp = "NA";
		}
	}
	
	public static void verifyOTP(String mobileNo,String date, String otp,String verify_status) {
		try {

			SoapObject request = new SoapObject(NAMESPACE, VERIFY_OTP_FROM_MOBILE_METHOD_NAME);
				request.addProperty("mobileNo", mobileNo);
				request.addProperty("date", date);
				request.addProperty("otp", otp);
				request.addProperty("verify_status", verify_status);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL);
			androidHttpTransport.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();

			otp_verify_resp = result.toString();
			if (otp_verify_resp.trim()==null || otp_verify_resp.trim().equals("NA") || otp_verify_resp.trim().equals("anyType{}")) {
				otp_verify_resp = null;
			}else {
				otp_verify_resp = result.toString();
			}
		} catch (SoapFault fault) {
			otp_verify_resp = "NA";

        }  catch (Exception E) {
			E.printStackTrace();
			otp_verify_resp = "NA";
		}
	}

	//getDetainedItems(unitcode,eticketno,aadharNo)
	public static void getDetainedItems(String unit_code, String ticketNo) {
		try {
			SoapObject request = new SoapObject(NAMESPACE, "getDetainedItems");

			request.addProperty("unitcode", "" + unit_code);
			request.addProperty("eticketno", "" + ticketNo);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL);
			httpTransportSE.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();
			if (null!=result) {

				detainedBy_challanNo_resp = result.toString();

				if (detainedBy_challanNo_resp.trim().equals("0") || detainedBy_challanNo_resp.trim().equals("NA")
						|| detainedBy_challanNo_resp.trim().equals("anyType{}")) {
					detainedBy_challanNo_resp = "0";
					pending_challans_master = new String[0];
					pending_challans_details = new String[0][0];
				} else {
					pending_challans_master = new String[0];
					pending_challans_master = detainedBy_challanNo_resp.split("\\@")[0].split("\\!");
				}
			}else{
				detainedBy_challanNo_resp = null;
			}
		}  catch (SoapFault fault) {
			fault.printStackTrace();
			detainedBy_challanNo_resp = null;

        } catch (Exception e) {
        	e.printStackTrace();
        	detainedBy_challanNo_resp = null;
		}
	}
	
	
	public static void getDetainedItemsByAadhaar(String regno,
			String drvr_lcno, String ownr_lcnce_no, String pid, String pidname,
			String pwd, String simid, String imei, String lat, String logn,
			String ip_val, String unit_code) {

		try {
			SoapObject request = new SoapObject(NAMESPACE, ""+ GET_DETAINED_ITEMS_BY_AADHAAR);

			request.addProperty("regnNo", "" + regno);
			request.addProperty("drvierLicNo", "" + drvr_lcno);
			request.addProperty("ownerLicNo", "" + ownr_lcnce_no);
			request.addProperty("pidCd", "" + pid);
			request.addProperty("pidName", "" + pidname);
			request.addProperty("password", "" + pwd);
			request.addProperty("simId", "" + simid);
			request.addProperty("imeiNo", "" + imei);
			request.addProperty("latitude", "" + lat);
			request.addProperty("longitude", "" + logn);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL);
			httpTransportSE.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();
			
			detainedByAadhaarNo_resp = result.toString();
			if (detainedByAadhaarNo_resp.trim()==null || detainedByAadhaarNo_resp.trim().equals("NA")
				|| detainedByAadhaarNo_resp.trim().equals("NA")) {
				
				detainedByAadhaarNo_resp = null ;
			}else {
				detainedByAadhaarNo_resp = result.toString();
			}
		}  catch (SoapFault fault) {
			detainedByAadhaarNo_resp = null;
        } catch (Exception e) {
        	e.printStackTrace();
			detainedByAadhaarNo_resp = null;
		}
	}
	
	
	public static void getReleaseItemsSubmit(String unit_cd, String eticket_No,String aadhaarNo,String off_Date,String pid_Cd,
			String pid_Name,String img_After) {
//public String releaseDocs(String unitCode, String eticketNo,String aadhaar,String offDate,String pidCd,String pidName,String imgAfter);

		try {
			SoapObject request = new SoapObject(NAMESPACE, "releaseDocs" );
			
			request.addProperty("unitCode", unit_cd);
			request.addProperty("eticketNo", eticket_No);
			request.addProperty("aadhaar", aadhaarNo);
			request.addProperty("offDate", off_Date);
			request.addProperty("pidCd",pid_Cd);
			request.addProperty("pidName",pid_Name);
			request.addProperty("imgAfter",img_After);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL);
			httpTransportSE.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();
			
			ReleaseDocSubmit_resp = result.toString();
			if (ReleaseDocSubmit_resp.trim()==null || ReleaseDocSubmit_resp.trim().equals("NA") || ReleaseDocSubmit_resp.trim().equals("anyType{}")) {
				ReleaseDocSubmit_resp = null;
			}else {
				ReleaseDocSubmit_resp = result.toString();
			}
		}  catch (SoapFault fault) {
			ReleaseDocSubmit_resp = null;
        } catch (Exception e) {
        	e.printStackTrace();
			ReleaseDocSubmit_resp = null;
		}
	}
	
	//getLocation(String gpsLatti, String gpsLongi);
	public static void get_Location(String gps_Latti, String gps_Longi) {
//public String releaseDocs(String unitCode, String eticketNo,String aadhaar,String offDate,String pidCd,String pidName,String imgAfter);

		try {
			SoapObject request = new SoapObject(NAMESPACE, "getLocation" );
			
			request.addProperty("gpsLatti", gps_Latti);
			request.addProperty("gpsLongi", gps_Longi);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL);
			httpTransportSE.call(SOAP_ACTION, envelope);
			Object result = envelope.getResponse();
			
			get_lat_long_Detail_resp = result.toString();
			
			if (get_lat_long_Detail_resp.trim()==null || get_lat_long_Detail_resp.trim().equals("NA") || get_lat_long_Detail_resp.trim().equals("anyType{}")) {
				get_lat_long_Detail_resp = null;
			}else {
				get_lat_long_Detail_resp = result.toString();
			}
		}  catch (SoapFault fault) {
			get_lat_long_Detail_resp = null;
        } catch (Exception e) {
        	e.printStackTrace();
			get_lat_long_Detail_resp = null;
		}
	}
	
	// getTINRData(String tin);
		public static void getTinDetails(String tinNo) {
			try {
				//String pidCd, String password,String imei,String gpsLattitude,String gpsLongitude
				SoapObject request = new SoapObject(NAMESPACE, "getTINRData");
				request.addProperty("tin", tinNo);
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(MainActivity.URL);
				androidHttpTransport.call(SOAP_ACTION, envelope);
				Object result = envelope.getResponse();
				
				tinDetails_resp = result.toString();
				if (tinDetails_resp.trim()==null || tinDetails_resp.trim().equals("NA") || tinDetails_resp.trim().equals("anyType{}")) {
					tinDetails_resp = null;
				}else {
					tinDetails_resp = result.toString();
				}
			}  catch (SoapFault fault) {
				tinDetails_resp = null ;
	        } catch (Exception e) {
				e.printStackTrace();
				tinDetails_resp = null ;
			}
		}
	

}
