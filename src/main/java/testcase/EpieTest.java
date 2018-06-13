package testcase;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;

import dao.UserDetailDao;
import dao.UserDetailDaoImpl;
import mybatis.Users;

import org.testng.Reporter;
import org.testng.SkipException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.apache.commons.io.IOUtils;

import utils.HttpClientUtils;
import utils.Util;
/**
 * @author cenjing
 *
 */
public class EpieTest {
	

	private static Log logger = LogFactory.getLog(Util.class);
	private static String url_address = "./config/url.properties";
	private static String test_environment = Util.getProperties(url_address, "environment");
	private static String test_prefix = test_environment+"_url_prefix";
	private static final String account_PATH= "./config/testAccount.properties";
	
	  @BeforeMethod
	  public void beforeMethod() {
	  }

	  @AfterMethod
	  public void afterMethod() {
	  }
	  
	  @BeforeSuite
	  public void beforeSuite() {
	  }
	  
	  @Test(dataProvider = "loginData", dataProviderClass = staticProvider.class, groups = { "loginTest", "checkintest" },enabled =true)
	  public void loginTest(String caseDescription, String loginURL,String gcmd,String fh, String credential, String password, String valicateCode, String status, String isRun) 
	  {
		   Reporter.log("======================="+caseDescription+"===================================");
		   
		   String regex ="bizcode|desc";
		   if(loginURL.isEmpty()) {
			   loginURL =Util.getProperties(url_address, test_environment+"_login_address");
			   //System.out.println(loginURL);
		   }
		    try {	
		    	if(isRun.equals("Yes")) {
		    	URIBuilder builder = new URIBuilder();
		        URI uri = null;
		        if(credential.equals("unregistered")) {
		        	credential = Tools.checkMobileUseful(url_address,test_environment);
		        }
		        String body ="{\"credential\":\""+credential+"\",\"password\":\""+password+"\"}";
		        
		        List<NameValuePair> nvps = new ArrayList<>();
		    	nvps.add(new BasicNameValuePair("gcmd", gcmd));
		    	nvps.add(new BasicNameValuePair("fh", fh));
		    	nvps.add(new BasicNameValuePair("bd", body));
		    	uri= builder.setPath(loginURL).setParameters(nvps).build();
		    	logger.info("=====URI: "+uri);
		    	Reporter.log(uri.toString());
		    	
		    	String output = HttpClientUtils.httpJsonPost(uri.toString());
		    	
		    	Reporter.log(output);
		        // Examine the response status        
		        String bizcode = output.split(regex)[1].split("\"")[2];
		       	     
		        Assert.assertEquals(bizcode, status);
		    	}
		    	else {
		    		 throw new SkipException("skip the test");
		    	}
		    }
		    catch(Exception e) {
		    	e.printStackTrace();
		    }
		   
	  }
	  
	  @BeforeTest
	  public void beforeTest() {
	  }
	  
	  @Test(dataProvider = "registerData", dataProviderClass = staticProvider.class, groups = { "Registertest", "checkintest" },enabled =true)
	  public void registerTest(String caseDescription,String reqURL,String mobile,String checkCode, String loginPassword, String loginPasswordSure, String recommendMobile, String actCode,String returnCode, String isRun) throws Exception
	  {
		  
		  Reporter.log("======================="+caseDescription+"===================================");
		  String register_url =Util.getProperties(url_address, test_prefix)+ Util.getProperties(url_address, "register_suffix");
		 if(isRun.equals("Yes")) {
		  if(reqURL.isEmpty()) {
			  reqURL= register_url;
		  }
		  //use test mobile number
		  if(mobile.isEmpty()) {
			  //create random mobile number and query in DB until found one not exist,and use it for register
			  mobile =Tools.checkMobileUseful(url_address,test_environment);
			  //mobile= Util.getProperties("./config/testAccount.properties","register_mobile");
			  }
		  if(mobile.equals("empty")) {
			  mobile = "";
		  }
		  
		  if(checkCode.equals("empty")) {
			  checkCode = "";
		  }
		  if(recommendMobile.isEmpty()) {
			  recommendMobile = Tools.checkMobileUseful(url_address, test_environment);
		  }
		  
		  //fetch smsValidateCode if not inputed from test case
		  if(checkCode.isEmpty()) {
			  checkCode =Tools.SmsCodeGenerate(url_address,test_environment,mobile);  
		  }
		  
		  Map<String, String> parameterMap = new HashMap<String, String>();
		  parameterMap.put("mobile", mobile);
		  parameterMap.put("checkCode", checkCode);
		  parameterMap.put("loginPassword",loginPassword);
		  parameterMap.put("loginPasswordSure",loginPasswordSure);
		  parameterMap.put("recommendMobile",recommendMobile);
		  parameterMap.put("actCode",actCode);
		  
		  String output = HttpClientUtils.httpJsonPost(register_url, parameterMap);
		  Reporter.log("============================= Response ==========================");
		  Reporter.log(output);
		
		  //query from DB 
		  UserDetailDao userDetail = new UserDetailDaoImpl();
		  String  status  = userDetail.getUserStatusByMobile(mobile,test_environment);
		  List<Users> user = userDetail.getUserDetailInfoByMobile(mobile, test_environment);
		  Reporter.log("UserStatus: "+status);
		  if(user.size()!=0) {
			  //print user info
			  for(int i =0; i<user.size();i++) {
				  Reporter.log("Id= "+user.get(i).getId() +"; Name= "+user.get(i).getName()
				+"; Mobile="+user.get(i).getMobile()+"; Account= "+user.get(i).getAccount()
				+";Type= "+user.get(i).getType() +"; Status= "+user.get(i).getStatus());
			  }
		  }
		  
	      String retCode = output.split("returnCode|'")[1].split("\"")[2];;
		  logger.info(retCode);
		  //set register mobile into property
		  if(retCode.equals("0000")) {
			 Util.setProperties(account_PATH, "register_mobile", mobile);
		  }
		  
		  Assert.assertEquals(retCode,returnCode);
		 }
		 else {
			 throw new SkipException("skip the test");
		 }
	
	  }
	  
	  @Test(dataProvider = "AppRegisterData", dataProviderClass = staticProvider.class, groups = { "AppRegisterTest", "checkintest" },enabled =true)
	  public void AppRegisterTest(String caseDescription, String reqURL, String mobile,String imageCode,String checkCode, String loginPassword, String loginPasswordSure,String recommendMobile, String status, String isRun) 
	  {

			 Reporter.log("======================="+caseDescription+"===================================");
			 if(isRun.equals("Yes")) {
			 String fh = Util.getProperties(account_PATH, "APP_rengou_fh"); 
				String pbname = Util.getProperties(account_PATH, "APP_register_PBname");
				Map<String,String> urlParams = new HashMap<String,String>();
				urlParams.put("fh", fh);
				urlParams.put("pbname", pbname);
				urlParams.put("deviceNo","2e9b81deb75420a2");
				if(mobile.isEmpty()) {
				mobile = Tools.checkMobileUseful(url_address, test_environment);
				}
				if(imageCode.isEmpty()) {
				imageCode = Tools.AppImageCodeGenerate(url_address, test_environment);
				}
				if(checkCode.isEmpty()) {
				checkCode = Tools.AppSmsCodeGenerate(url_address,test_environment,mobile,imageCode);
				}
				if(mobile.equals("empty")) {
					mobile = "";
				 }
				if(checkCode.equals("empty")) {
					checkCode = "";
				 }
				if(imageCode.equals("empty")) {
					imageCode = "";
				 }
				
				if(recommendMobile.isEmpty()) {
					recommendMobile = Tools.checkMobileUseful(url_address, test_environment);
				 }

				
				JSONObject jsonParam = new JSONObject();  
				
				jsonParam.put("mobile", mobile);
				jsonParam.put("imageCode", imageCode);
				jsonParam.put("checkCode", checkCode);
				jsonParam.put("loginPassword",loginPassword);
				jsonParam.put("loginPasswordSure",loginPasswordSure);
				jsonParam.put("channelNo","12");
				jsonParam.put("registeredDeviceId","2e9b81deb75420a2");
				//jsonParam.put("registeredSystem","android%206.0.1");
				//jsonParam.put("registeredModel","Mi%20Note%202");
				Reporter.log(jsonParam.toJSONString());
 				
				reqURL = Util.getProperties(url_address, test_environment+"_APP_trade");
				String responseBody = HttpClientUtils.httpJsonPostURLParams(reqURL,urlParams,jsonParam);
				Reporter.log(responseBody);
				
				String retCode = responseBody.split("returnCode|'")[1].split("\"")[2];
				 logger.info(retCode);
			   		
	             Assert.assertEquals(retCode,status);
			
				}
			 else {
				 throw new SkipException("skip the test");
		    	}

	  }

	 @SuppressWarnings("deprecation")
	@Test(dataProvider = "ApploginData", dataProviderClass = staticProvider.class, groups = { "AppLoginTest", "checkintest" },enabled =true)
	  public void AppLoginTest(String caseDescription, String loginURL, String username, String password, String validateCode, String status, String isRun) 
	  {
		 Reporter.log("======================="+caseDescription+"===================================");
		 String responseBody= "";
		 if(isRun.equals("Yes")) {
			 //urlparams
		 
			String fh = Util.getProperties(account_PATH, "APP_login_fh"); 
			String pbname = Util.getProperties(account_PATH, "APP_Login_PBname");
			if(username.isEmpty()) {
			username = Util.getProperties(account_PATH, "login_mobile");
			}
			if(password.isEmpty()) {
			password = Util.getProperties(account_PATH, "login_password");	
			}

			URIBuilder builder = new URIBuilder();
	        URI uri = null;
	        String body ="{\"username\":\""+username+"\",\"password\":\""+password+"\"}";
	        loginURL= Util.getProperties(url_address, test_environment+"_APP_login");
	        
	        List<NameValuePair> nvps = new ArrayList<>();
	    	nvps.add(new BasicNameValuePair("fh", fh));
	    	nvps.add(new BasicNameValuePair("pbname", pbname));
	    	nvps.add(new BasicNameValuePair("bd", body));
	    	try {
				uri= builder.setPath(loginURL).setParameters(nvps).build();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	logger.info("=====URI: "+uri);
	    	
			//cookie stored 
			try {
				 responseBody = HttpClientUtils.HttpJsonGet(uri.toString());		
	             logger.info("responseBody :"+responseBody);
	             
	             String retCode = responseBody.split("returnCode|'")[1].split("\"")[2];;
	             logger.info(retCode);
	   		
	             Assert.assertEquals(retCode,status);
				}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		 }
		 else {
			 throw new SkipException("skip the test");
	    	}	 
	  }
		
	 @Test(dataProvider = "AppRengou", dataProviderClass = staticProvider.class, groups = { "AppRengouTest", "checkintest" },enabled =true)
	  public void AppRengouTest(String caseDescription, String reqURL, String username, String password, String tradePassword,String isOnlyNew,String productCode,String payAmount, String repeatCommitCheckCode, String status, String isRun) 
	  {
	
		 Reporter.log("======================="+caseDescription+"===================================");
		 
		 String buyerSmallestAmount ="";
		 if(isRun.equals("Yes")) {
			 
			 if(productCode.isEmpty()) {
				 List<String> productList = Tools.AppGetSellingProductList(url_address,test_environment,username,password,isOnlyNew);
				 productCode =productList.get(0).split("productCode")[1].split("\"")[2];	
				 buyerSmallestAmount= productList.get(0).split("buyerSmallestAmount")[1].split("\"")[2].replace(",","");
			 }
			
			if(payAmount.isEmpty()) {
				payAmount = buyerSmallestAmount;
				//need add query specific product smallest amount
				/*
				 *  */			
			}
			if(username.equals("unregistered")) {
				username = Tools.checkMobileUseful(url_address, test_environment);
			}
			
			if(username.equals("autoregistered")) {
				
			}
			
			logger.info("productCode:"+productCode+";payAmount:"+payAmount);
			
			Map<String,String> urlParams = new HashMap<String,String>();
			String fh = Util.getProperties(account_PATH, "APP_rengou_fh"); 
			String pbname = Util.getProperties(account_PATH, "APP_rengou_PBname");
			urlParams.put("fh", fh);
			urlParams.put("pbname", pbname);
			
			if(repeatCommitCheckCode.isEmpty()) {
			repeatCommitCheckCode = Tools.AppQueryPayInit(url_address,test_environment,username,password,isOnlyNew);
			}
			
			JSONObject jsonParam = new JSONObject();  
			jsonParam.put("userid", username);
			jsonParam.put("productCode", productCode);
			jsonParam.put("delegateNum", payAmount);
			jsonParam.put("password", tradePassword);
			jsonParam.put("payType", "subscribePay");
			jsonParam.put("repeatCommitCheckCode", repeatCommitCheckCode);
			jsonParam.put("channelNo", "12");
			jsonParam.put("kqCode", "");
			jsonParam.put("kqType", "");
			jsonParam.put("kqValue", "");
			Reporter.log(jsonParam.toJSONString());
			
			reqURL = Util.getProperties(url_address, test_environment+"_APP_trade");
			String responseBody = HttpClientUtils.httpJsonPostURLParams(reqURL,urlParams,jsonParam);
			Reporter.log(responseBody);
			String retCode = responseBody.split("returnCode|'")[1].split("\"")[2];
			Assert.assertEquals(retCode,status);
			}
		 else {
			 throw new SkipException("skip the test");
		 }
	  }

	  @AfterTest
	  public void afterTest() {
	  }

	

	  @AfterSuite
	  public void afterSuite() {
	  }
	
}
