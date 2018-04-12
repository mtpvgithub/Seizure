package com.mtpv.seizureHelpers;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.mtpv.seizureInfo.FootPath_Vendor;
import com.mtpv.seizureInfo.IPsettings;
import com.mtpv.seizureInfo.MainActivity;
import com.mtpv.seizureInfo.Shop_vendor;

public class WebService extends Activity {

    public static String dyamic_Ip = IPsettings.test_service_url;
    public static String SOAP_ADDRESS = dyamic_Ip + "39BService/services/BServiceImpl?wsdl";

    //public final String SOAP_ADDRESS = "http://www.echallan.org/39BService/services/BServiceImpl?WSDL";
    //public final String SOAP_ADDRESS = "http://192.168.11.55:8080/39BService/services/BServiceImpl?WSDL";
    //public final String SOAP_ADDRESS = "http://192.168.11.10:8080/39BService/services/BServiceImpl?WSDL";
    //public final String SOAP_ADDRESS = "http://192.168.11.8/39BService/services/BServiceImpl?WSDL";
    //public final String SOAP_ADDRESS = "http://192.168.11.55:8080/SmokeService/services/SmokeServiceImpl?WSDL";

    public final String WSDL_TARGET_NAMESPACE = "http://service.mother.com";
    public final String SOAP_ACTION = "http://service.mother.com/authenticateUser";
    public final String AUTHENTICATE_USER = "authenticateUser";
    public final String PSNAMES = "getPSMaster";
    public final String PSPOINTNAMES = "getPointDetailsByPscode";
    public final String IDProofMaster = "getIDProofMaster";
    public final String DetainItemsMaster = "getDetainItemsMaster";
    public final String GenrateChallan = "generateChallan";
    public final String getDuplicatePrint = "getDuplicatePrint";
    public final String getDuplicatePrintByEticket = "getDuplicatePrintByEticket";
    public final String getReport = "getReport";
    public final String getViolation = "getSectionDetails";

    public static String Challan_response = "";


    public WebService() {

        Log.e("web service url after", "" + SOAP_ADDRESS);
    }

    public String AuthenticateUser(String pidCd, String password, String imei, String gpsLattitude, String gpsLongitude) {
        // TODO Auto-generated method stub

        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, AUTHENTICATE_USER);
        PropertyInfo pi = new PropertyInfo();
        pi.setName("pidCd");
        pi.setValue(pidCd);
        pi.setType(Integer.class);
        request.addProperty(pi);
        pi = new PropertyInfo();

        pi.setName("password");
        pi.setValue(password);
        pi.setType(Integer.class);
        request.addProperty(pi);

        pi.setName("imei");
        pi.setValue(imei);
        pi.setType(Integer.class);
        request.addProperty(pi);

        pi.setName("gpsLattitude");
        pi.setValue(gpsLattitude);
        pi.setType(Integer.class);
        request.addProperty(pi);

        pi.setName("gpsLongitude");
        pi.setValue(gpsLongitude);
        pi.setType(Integer.class);
        request.addProperty(pi);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);

        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response = null;

        try {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();

            Challan_response = response.toString();

            if (Challan_response == "0") {
                Toast.makeText(getApplicationContext(), "No Response for Server", Toast.LENGTH_LONG).show();

            } else {
                Challan_response = Challan_response.replace("|", ":");
                MainActivity.arr_logindetails = Challan_response.split(":");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            response = exception.toString();
            Challan_response = "0";
        }

        return response.toString();
    }


    public String GetPsNames(String unitCode) {
        // TODO Auto-generated method stub
        Log.i("unitCode :::", "" + unitCode);
        /*SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,PSNAMES);
        PropertyInfo pi=new PropertyInfo();
			
			pi.setName("unitCode");
			pi.setValue(unitCode);
			pi.setType(Integer.class);
			request.addProperty(pi);
			
		 SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		 envelope.dotNet = true;
		 envelope.setOutputSoapObject(request);
		 HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
		 Object response=null;*/

        try {
            DataBase DBH = new DataBase(getApplicationContext());

            SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, PSNAMES);
            request.addProperty("unitCode", unitCode);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("request", "" + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(SOAP_ADDRESS);

            androidHttpTransport.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();

            Log.i("result ::", "" + result.toString());


        } catch (Exception e) {
            e.printStackTrace();
        }
        return unitCode;


    }

    public String GetPsPointNames(String sPSN) {
        // TODO Auto-generated method stub
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, PSPOINTNAMES);
        PropertyInfo pi = new PropertyInfo();
        pi.setName("pscode");
        pi.setValue(sPSN);
        pi.setType(Integer.class);
        request.addProperty(pi);
        pi = new PropertyInfo();

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response = null;

        try {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
        } catch (Exception exception) {
            response = exception.toString();
        }

        return response.toString();

    }


    public String GetIDProofMaster() {
        // TODO Auto-generated method stub
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, IDProofMaster);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response = null;

        try {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
        } catch (Exception exception) {
            response = exception.toString();
        }

        return response.toString();
    }


    public String GETDetainItemsMaster() {
        // TODO Auto-generated method stub
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, DetainItemsMaster);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response = null;

        try {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
        } catch (Exception exception) {
            response = exception.toString();
        }

        return response.toString();

    }


    public String generateChallan(String unitCd, String unitNm, String bookdPsCd, String bookdPsNm, String pointCd, String pointNm,
                                  String operatrCd, String operatrName, String pidCod, String pidNme, String passwrd, String cadreCd, String cadr,
                                  String onlineMd, String imageEvidnce, String imgEncoddData, String offnceDt, String offncTime,
                                  String firmRgnNo, String shopNam, String shopOwnrName, String shopAddrss, String locatn, String psCd,
                                  String psNm, String respndntName, String respndntFathrName,
                                  String respndntAddrss, String respondntCntctNo, String respdantAge, String IDCd, String IDDtails,
                                  String witnss1Name, String witnss1FatherName, String witnss1Address,
                                  String witnss2Name, String witnss2FatherName, String witnss2Address,
                                  String detaindItems,
                                  String simid, String imei_No, String mac_Id, String gps_Latitude, String gps_Longitude, String imgEncodedDataAfter, String totalFine,
                                  String business_Type, String hawker_Type) {

        try {
            SoapObject request = null;
            request = new SoapObject(WSDL_TARGET_NAMESPACE, "" + GenrateChallan);
            request.addProperty("unitCode", "" + unitCd);
            request.addProperty("unitName", "" + unitNm);
            request.addProperty("bookedPsCode", "" + bookdPsCd);
            request.addProperty("bookedPsName", "" + bookdPsNm);
            request.addProperty("pointCode", "" + pointCd);
            request.addProperty("pointName", "" + pointNm);
            request.addProperty("operaterCd", "" + operatrCd);
            request.addProperty("operaterName", "" + operatrName);
            request.addProperty("pidCd", "" + pidCod);
            request.addProperty("pidName", "" + pidNme);
            request.addProperty("password", "" + passwrd);
            request.addProperty("cadreCD", "" + cadreCd);
            request.addProperty("cadre", "" + cadr);
            request.addProperty("onlineMode", "" + onlineMd);
            request.addProperty("imageEvidence", "" + imageEvidnce);
            request.addProperty("imgEncodedData", "" + imgEncoddData);
            request.addProperty("offenceDt", "" + offnceDt);
            request.addProperty("offenceTime", "" + offncTime);
            request.addProperty("firmRegnNo", "" + firmRgnNo);
            request.addProperty("shopName", "" + shopNam);
            request.addProperty("shopOwnerName", "" + shopOwnrName);
            request.addProperty("shopAddress", "" + shopAddrss);
            request.addProperty("location", "" + locatn);
            request.addProperty("psCode", "" + psCd);
            request.addProperty("psName", "" + psNm);
            request.addProperty("respondantName", "" + respndntName);
            request.addProperty("respondantFatherName", "" + respndntFathrName);
            request.addProperty("respondantAddress", "" + respndntAddrss);
            request.addProperty("respondantContactNo", "" + respondntCntctNo);
            request.addProperty("respondantAge", "" + respdantAge);
            request.addProperty("IDCode", "" + IDCd);
            request.addProperty("IDDetails", "" + IDDtails);
            request.addProperty("witness1Name", "" + witnss1Name);
            request.addProperty("witness1FatherName", "" + witnss1FatherName);
            request.addProperty("witness1Address", "" + witnss1Address);
            request.addProperty("witness2Name", "" + witnss2Name);
            request.addProperty("witness2FatherName", "" + witnss2FatherName);
            request.addProperty("witness2Address", "" + witnss2Address);
            request.addProperty("detainedItems", "" + detaindItems);
            request.addProperty("simId", "" + simid);
            request.addProperty("imeiNo", "" + imei_No);
            request.addProperty("macId", "" + mac_Id);
            request.addProperty("gpsLatitude", "" + gps_Latitude);
            request.addProperty("gpsLongitude", "" + gps_Longitude);
            request.addProperty("imgEncodedDataAfter", "" + imgEncodedDataAfter);
            request.addProperty("totalFine", "" + totalFine);//

            request.addProperty("businessType", "" + business_Type);
            request.addProperty("hawkerType", "" + hawker_Type);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();

            if (null != result) {
                Challan_response = result.toString();
            }

            FootPath_Vendor.fineAmount = "";
            Shop_vendor.fineAmount = "";

        } catch (Exception e) {
            e.printStackTrace();
            Challan_response = "0^NA^NA";

        }
        return gps_Longitude;
    }


    public String generateChallanfootpath(String unitCd, String unitNm, String bookdPsCd, String bookdPsNm, String pointCd, String pointNm,
                                          String operatrCd, String operatrName, String pidCod, String pidNme, String passwrd, String cadreCd, String cadr,
                                          String onlineMd, String imageEvidnce, String imgEncoddData, String offnceDt, String offncTime,
                                          String firmRgnNo, String shopNam, String shopOwnrName, String shopAddrss, String locatn, String psCd,
                                          String psNm, String respndntName, String respndntFathrName,
                                          String respndntAddrss, String respondntCntctNo, String respdantAge, String IDCd, String IDDtails,
                                          String witnss1Name, String witnss1FatherName, String witnss1Address,
                                          String witnss2Name, String witnss2FatherName, String witnss2Address,
                                          String detaindItems,
                                          String simid, String imei_No, String mac_Id, String gps_Latitude, String gps_Longitude,
                                          String business_Type, String hawker_Type) {
        try {
            SoapObject request = null;

            request = new SoapObject(WSDL_TARGET_NAMESPACE, "" + GenrateChallan);

            request.addProperty("unitCode", "" + unitCd);
            request.addProperty("unitName", "" + unitNm);
            request.addProperty("bookedPsCode", "" + bookdPsCd);
            request.addProperty("bookedPsName", "" + bookdPsNm);
            request.addProperty("pointCode", "" + pointCd);
            request.addProperty("pointName", "" + pointNm);
            request.addProperty("operaterCd", "" + operatrCd);
            request.addProperty("operaterName", "" + operatrName);
            request.addProperty("pidCd", "" + pidCod);
            request.addProperty("pidName", "" + pidNme);
            request.addProperty("password", "" + passwrd);
            request.addProperty("cadreCD", "" + cadreCd);
            request.addProperty("cadre", "" + cadr);
            request.addProperty("onlineMode", "" + onlineMd);
            request.addProperty("imageEvidence", "" + imageEvidnce);
            request.addProperty("imgEncodedData", "" + imgEncoddData);
            request.addProperty("offenceDt", "" + offnceDt);
            request.addProperty("offenceTime", "" + offncTime);
            request.addProperty("firmRegnNo", "" + firmRgnNo);
            request.addProperty("shopName", "" + shopNam);
            request.addProperty("shopOwnerName", "" + shopOwnrName);
            request.addProperty("shopAddress", "" + shopAddrss);
            request.addProperty("location", "" + locatn);
            request.addProperty("psCode", "" + psCd);
            request.addProperty("psName", "" + psNm);
            request.addProperty("respondantName", "" + respndntName);
            request.addProperty("respondantFatherName", "" + respndntFathrName);
            request.addProperty("respondantAddress", "" + respndntAddrss);
            request.addProperty("respondantContactNo", "" + respondntCntctNo);
            request.addProperty("respondantAge", "" + respdantAge);
            request.addProperty("IDCode", "" + IDCd);
            request.addProperty("IDDetails", "" + IDDtails);
            request.addProperty("witness1Name", "" + witnss1Name);
            request.addProperty("witness1FatherName", "" + witnss1FatherName);
            request.addProperty("witness1Address", "" + witnss1Address);
            request.addProperty("witness2Name", "" + witnss2Name);
            request.addProperty("witness2FatherName", "" + witnss2FatherName);
            request.addProperty("witness2Address", "" + witnss2Address);
            request.addProperty("detainedItems", "" + detaindItems);
            request.addProperty("simId", "" + simid);
            request.addProperty("imeiNo", "" + imei_No);
            request.addProperty("macId", "" + mac_Id);
            request.addProperty("gpsLatitude", "" + gps_Latitude);
            request.addProperty("gpsLongitude", "" + gps_Longitude);

            request.addProperty("businessType", "" + business_Type);
            request.addProperty("hawkerType", "" + hawker_Type);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();

            Challan_response = result.toString();

        } catch (Exception e) {
            e.printStackTrace();

        }
        return gps_Longitude;
    }


    public String getDuplicatePrint(String offenceDate, String pidCode, String shopName, String
            firmRegnNo, String responsdentName) {
        // TODO Auto-generated method stub
        try {
            SoapObject request = null;

            request = new SoapObject(WSDL_TARGET_NAMESPACE, "" + getDuplicatePrint);

            request.addProperty("offenceDate", "" + offenceDate);
            request.addProperty("pidCode", "" + pidCode);
            request.addProperty("shopName", "" + shopName);
            request.addProperty("firmRegnNo", "" + firmRegnNo);
            request.addProperty("responsdentName", "" + responsdentName);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            if (null != result) {

                Challan_response = result.toString();

            } else {
                Challan_response = "NA";
            }
        } catch (Exception e) {
            e.printStackTrace();
            Challan_response = "NA";
        }
        return Challan_response;
    }


    public String getDuplicatePrintByEticket(String Eticket) {
        // TODO Auto-generated method stub
		/*SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,getDuplicatePrintByEticket);
		PropertyInfo pi=new PropertyInfo();
		pi.setName("Eticket");
			pi.setValue(Eticket);
			pi.setType(Integer.class);
			request.addProperty(pi);
		
	        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		    envelope.dotNet = true;
		    
		    envelope.setOutputSoapObject(request);

		    HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
		    Object response=null;
		    
		    try
		    {
		    httpTransport.call(SOAP_ACTION, envelope);
		    response = envelope.getResponse();
		    }
		    catch (Exception exception)
		    {
		    response=exception.toString();
		    }
			
			return  response.toString();*/
        try {
            SoapObject request = null;

            request = new SoapObject(WSDL_TARGET_NAMESPACE, "" + getDuplicatePrintByEticket);

            request.addProperty("Eticket", "" + Eticket);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();
            if (null != result) {
                Challan_response = "";
                Challan_response = result.toString();

            } else {
                Challan_response = "NA";
            }


        } catch (Exception e) {
            e.printStackTrace();
            Challan_response = "NA";
        }
        return Eticket;
    }


    public String getReport(String offenceDate, String pidCode) {
        // TODO Auto-generated method stub
		/*SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE,getReport);
	        request.addProperty("offenceDate", offenceDate);
	        request.addProperty("pidCode", pidCode);
		
	        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		    envelope.dotNet = true;
		    
		    envelope.setOutputSoapObject(request);

		    HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
		    Object response=null;
		    
		    try
		    {
		    httpTransport.call(SOAP_ACTION, envelope);
		    response = envelope.getResponse();
		    
		    }*/


        try {
            SoapObject request = null;

            request = new SoapObject(WSDL_TARGET_NAMESPACE, getReport);
            request.addProperty("offenceDate", offenceDate);
            request.addProperty("pidCode", pidCode);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransportSE = new HttpTransportSE(MainActivity.URL);
            httpTransportSE.call(SOAP_ACTION, envelope);
            Object result = envelope.getResponse();

            Challan_response = result.toString();

        } catch (Exception exception) {
            exception.printStackTrace();
//		    response=exception.toString();
        }
        return pidCode;

    }

    public String getViolation() {
        // TODO Auto-generated method stub
        SoapObject request = new SoapObject(WSDL_TARGET_NAMESPACE, getViolation);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE httpTransport = new HttpTransportSE(SOAP_ADDRESS);
        Object response = null;

        try {
            httpTransport.call(SOAP_ACTION, envelope);
            response = envelope.getResponse();
        } catch (Exception exception) {
            response = exception.toString();
        }

        return response.toString();
    }

}
