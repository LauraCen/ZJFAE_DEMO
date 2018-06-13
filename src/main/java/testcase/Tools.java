/**
 * 
 */
package testcase;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.testng.Reporter;
import org.testng.internal.Utils;

import com.alibaba.fastjson.JSONObject;

import dao.UserDetailDao;
import dao.UserDetailDaoImpl;
import junit.framework.Assert;
import utils.HttpClientUtils;
import utils.Util;

/**
 * @author cenjing
 *
 */
public class Tools {
	
	private static Log logger = LogFactory.getLog(Util.class);
	private static String url_address = "./config/url.properties";
	private static String test_environment = Util.getProperties(url_address, "environment");
	private static String account_PATH= "./config/testAccount.properties";
	
	static CookieStore  cookieStore = null;
	static UserDetailDao userDetail = new UserDetailDaoImpl(); 
	/*public static String checkMobileUseful(String url_address, String test_environment) {
		String mobile = null;
		//generate random mobile number
	    String trymobile = Util.getPhoneNum();	    
	    //check whether user is registered or not 
		try{
			String status = userDetail.getUserStatusByMobile(trymobile,test_environment);
			System.out.println(status);
			if(status == null) {
				mobile = trymobile;
				System.out.println("can not find: "+ mobile);
				return mobile;
			}
			else {
				System.out.println("find: "+ trymobile);
				//if mobile is already existed, recursive executed till found unregistered mobile
				return checkMobileUseful(url_address,test_environment);
			}
		}
		catch(Exception e) {
           e.printStackTrace();
		}	
		return null;
	}*/
	
	public static String checkMobileUseful(String url_address, String test_environment) {
		String mobile = null;
		//generate random mobile number
	    String trymobile = "";	    
	    //check whether user is registered or not 
	    while (true) {
			try{
			    trymobile = Util.getPhoneNum();	 
				String status = userDetail.getUserStatusByMobile(trymobile,test_environment);
				logger.info(status);
				if(status == null) {
					mobile = trymobile;
					logger.info("Can not find: "+ mobile+", so it can be used for registration.");
					break;
				}
			}
			catch(Exception e) {
	           e.printStackTrace();
			}	
	    }
		return mobile;
	}

	
	public static void main(String[] args) {
		
	   //String px =":{\"fh\":\"VREGMZJ000000J00\",\"eh\":{\"isShare\":\"0\",\"pbname\":\"PBIFE_prdsubscribequery_prdQuerySubscribeProductListNewHome\",\"realipzjs\":\"192.168.40.171\",\"Set-Cookie\":[\"isShare=0;Domain=zjfae.com;Path=/;HttpOnly;\"]},\"body\":{\"returnCode\": \"0000\",\"returnMsg\": \"操作通过\",\"data\": {\"pageInfo\": {},\"productFinanceDetailList\": [{\"productCode\": \"LC18060019\",\"productName\": \"LQ20180607认购附件测试001\",\"buyStartDate\": \"2018-06-07 11:48:00\",\"buyEndDate\": \"2018-06-14 11:48:00\",\"buyEndTimes\": \"182660000\",\"manageStartDate\": \"2018-01-08 00:00:00\",\"manageEndDate\": \"2018-09-20 00:00:00\",\"riskLevel\": \"2\",\"riskLevelStr\": \"较低风险\",\"buyerSmallestAmount\": \"1,000\",\"buyTotalAmount\": \"1000000\",\"buyRemainAmount\": \"96000\",\"remainAmountInit\": \"1000000\",\"subjectType\": \"定向融资计划\",\"subjectTypeName\": \"定向融资计划\",\"prodSubType\": \"担保类\",\"deadline\": \"255\",\"expectedMaxAnnualRate\": \"6.20\",\"productSubType\": \"A\",\"status\": \"认购期\",\"mostHolderNum\": \"86\",\"specialCustomerNum\": \"111\",\"warnFlag\": \"1\",\"isSpecial\": \"0\",\"onlyMobile\": \"0\",\"onlyWeiXin\": \"0\",\"onlyNew\": \"0\",\"process\": \"90.40\",\"sellingStatus\": \"1\",\"specialAmountInit\": \"0\",\"iconsType\": \"9999\",\"isTransfer\": \"transfer\",\"unActualTransferUserLevel\": \"0\",\"unActualBuyUserLevel\": \"0\",\"canUseKq\": \"0\",\"iconsList\": [{\"iconsPosition\": \"l_middle\",\"uuid\": \"oV9yv5IifWnBJ5JpXo4XsiwCZVXjXb8N\"}]},{\"productCode\": \"LC18050093\",\"productName\": \"LQ20180528敬请期待测试001\",\"buyStartDate\": \"2018-06-07 15:10:00\",\"buyEndDate\": \"2018-06-14 10:50:00\",\"buyEndTimes\": \"179180000\",\"manageStartDate\": \"2018-01-08 00:00:00\",\"manageEndDate\": \"2018-09-20 00:00:00\",\"riskLevel\": \"2\",\"riskLevelStr\": \"较低风险\",\"buyerSmallestAmount\": \"1,000\",\"buyTotalAmount\": \"1000000\",\"buyRemainAmount\": \"1000000\",\"remainAmountInit\": \"1000000\",\"subjectType\": \"定向融资计划\",\"subjectTypeName\": \"定向融资计划\",\"prodSubType\": \"担保类\",\"deadline\": \"255\",\"expectedMaxAnnualRate\": \"6.20\",\"productSubType\": \"A\",\"status\": \"认购期\",\"mostHolderNum\": \"200\",\"specialCustomerNum\": \"0\",\"warnFlag\": \"1\",\"isSpecial\": \"0\",\"onlyMobile\": \"0\",\"onlyWeiXin\": \"0\",\"onlyNew\": \"0\",\"process\": \"0.00\",\"sellingStatus\": \"1\",\"specialAmountInit\": \"0\",\"iconsType\": \"9999\",\"isTransfer\": \"transfer\",\"unActualTransferUserLevel\": \"0\",\"unActualBuyUserLevel\": \"0\",\"canUseKq\": \"0\",\"iconsList\": [{\"iconsPosition\": \"l_middle\",\"uuid\": \"oV9yv5IifWnBJ5JpXo4XsiwCZVXjXb8N\"}]},{\"productCode\": \"LC18050098\",\"productName\": \"0530-1\",\"buyStartDate\": \"2018-05-30 09:47:00\",\"buyEndDate\": \"2018-06-14 10:50:00\",\"buyEndTimes\": \"179180000\",\"manageStartDate\": \"2018-01-08 00:00:00\",\"manageEndDate\": \"2018-09-20 00:00:00\",\"riskLevel\": \"2\",\"riskLevelStr\": \"较低风险\",\"buyerSmallestAmount\": \"1,000\",\"buyTotalAmount\": \"1000000\",\"buyRemainAmount\": \"984000\",\"remainAmountInit\": \"1000000\",\"subjectType\": \"定向融资计划\",\"subjectTypeName\": \"定向融资计划\",\"prodSubType\": \"担保类\",\"deadline\": \"255\",\"expectedMaxAnnualRate\": \"6.20\",\"productSubType\": \"A\",\"status\": \"认购期\",\"mostHolderNum\": \"196\",\"specialCustomerNum\": \"0\",\"warnFlag\": \"1\",\"isSpecial\": \"0\",\"onlyMobile\": \"0\",\"onlyWeiXin\": \"0\",\"onlyNew\": \"0\",\"process\": \"1.60\",\"sellingStatus\": \"1\",\"specialAmountInit\": \"0\",\"iconsType\": \"9999\",\"isTransfer\": \"transfer\",\"unActualTransferUserLevel\": \"0\",\"unActualBuyUserLevel\": \"0\",\"canUseKq\": \"0\",\"iconsList\": [{\"iconsPosition\": \"l_middle\",\"uuid\": \"oV9yv5IifWnBJ5JpXo4XsiwCZVXjXb8N\"}]},{\"productCode\": \"LC18050099\",\"productName\": \"0530-2\",\"buyStartDate\": \"2018-05-30 09:47:00\",\"buyEndDate\": \"2018-06-14 10:50:00\",\"buyEndTimes\": \"179180000\",\"manageStartDate\": \"2018-01-08 00:00:00\",\"manageEndDate\": \"2018-09-20 00:00:00\",\"riskLevel\": \"2\",\"riskLevelStr\": \"较低风险\",\"buyerSmallestAmount\": \"1,000\",\"buyTotalAmount\": \"1000000\",\"buyRemainAmount\": \"1000000\",\"remainAmountInit\": \"1000000\",\"subjectType\": \"定向融资计划\",\"subjectTypeName\": \"定向融资计划\",\"prodSubType\": \"担保类\",\"deadline\": \"255\",\"expectedMaxAnnualRate\": \"6.20\",\"productSubType\": \"A\",\"status\": \"认购期\",\"mostHolderNum\": \"200\",\"specialCustomerNum\": \"0\",\"warnFlag\": \"1\",\"isSpecial\": \"0\",\"onlyMobile\": \"0\",\"onlyWeiXin\": \"0\",\"onlyNew\": \"0\",\"process\": \"0.00\",\"sellingStatus\": \"1\",\"specialAmountInit\": \"0\",\"iconsType\": \"9999\",\"isTransfer\": \"transfer\",\"unActualTransferUserLevel\": \"0\",\"unActualBuyUserLevel\": \"0\",\"canUseKq\": \"0\",\"iconsList\": [{\"iconsPosition\": \"l_middle\",\"uuid\": \"oV9yv5IifWnBJ5JpXo4XsiwCZVXjXb8N\"}]},{\"productCode\": \"LC18050100\",\"productName\": \"0530-3\",\"buyStartDate\": \"2018-05-30 09:47:00\",\"buyEndDate\": \"2018-06-14 10:50:00\",\"buyEndTimes\": \"179180000\",\"manageStartDate\": \"2018-01-08 00:00:00\",\"manageEndDate\": \"2018-09-20 00:00:00\",\"riskLevel\": \"2\",\"riskLevelStr\": \"较低风险\",\"buyerSmallestAmount\": \"1,000\",\"buyTotalAmount\": \"1000000\",\"buyRemainAmount\": \"1000000\",\"remainAmountInit\": \"1000000\",\"subjectType\": \"定向融资计划\",\"subjectTypeName\": \"定向融资计划\",\"prodSubType\": \"担保类\",\"deadline\": \"255\",\"expectedMaxAnnualRate\": \"6.20\",\"productSubType\": \"A\",\"status\": \"认购期\",\"mostHolderNum\": \"200\",\"specialCustomerNum\": \"0\",\"warnFlag\": \"1\",\"isSpecial\": \"0\",\"onlyMobile\": \"0\",\"onlyWeiXin\": \"0\",\"onlyNew\": \"0\",\"process\": \"0.00\",\"sellingStatus\": \"1\",\"specialAmountInit\": \"0\",\"iconsType\": \"9999\",\"isTransfer\": \"transfer\",\"unActualTransferUserLevel\": \"0\",\"unActualBuyUserLevel\": \"0\",\"canUseKq\": \"0\",\"iconsList\": [{\"iconsPosition\": \"l_middle\",\"uuid\": \"oV9yv5IifWnBJ5JpXo4XsiwCZVXjXb8N\"}]},{\"productCode\": \"LC18050101\",\"productName\": \"0530-4\",\"buyStartDate\": \"2018-05-30 09:47:00\",\"buyEndDate\": \"2018-06-14 10:50:00\",\"buyEndTimes\": \"179180000\",\"manageStartDate\": \"2018-01-08 00:00:00\",\"manageEndDate\": \"2018-09-20 00:00:00\",\"riskLevel\": \"2\",\"riskLevelStr\": \"较低风险\",\"buyerSmallestAmount\": \"1,000\",\"buyTotalAmount\": \"1000000\",\"buyRemainAmount\": \"1000000\",\"remainAmountInit\": \"1000000\",\"subjectType\": \"定向融资计划\",\"subjectTypeName\": \"定向融资计划\",\"prodSubType\": \"担保类\",\"deadline\": \"255\",\"expectedMaxAnnualRate\": \"6.20\",\"productSubType\": \"A\",\"status\": \"认购期\",\"mostHolderNum\": \"200\",\"specialCustomerNum\": \"0\",\"warnFlag\": \"1\",\"isSpecial\": \"0\",\"onlyMobile\": \"0\",\"onlyWeiXin\": \"0\",\"onlyNew\": \"0\",\"process\": \"0.00\",\"sellingStatus\": \"1\",\"specialAmountInit\": \"0\",\"iconsType\": \"9999\",\"isTransfer\": \"transfer\",\"unActualTransferUserLevel\": \"0\",\"unActualBuyUserLevel\": \"0\",\"canUseKq\": \"0\",\"iconsList\": [{\"iconsPosition\": \"l_middle\",\"uuid\": \"oV9yv5IifWnBJ5JpXo4XsiwCZVXjXb8N\"}]},{\"productCode\": \"LC18050102\",\"productName\": \"0530-5\",\"buyStartDate\": \"2018-05-30 09:47:00\",\"buyEndDate\": \"2018-06-14 10:50:00\",\"buyEndTimes\": \"179180000\",\"manageStartDate\": \"2018-01-08 00:00:00\",\"manageEndDate\": \"2018-09-20 00:00:00\",\"riskLevel\": \"2\",\"riskLevelStr\": \"较低风险\",\"buyerSmallestAmount\": \"1,000\",\"buyTotalAmount\": \"1000000\",\"buyRemainAmount\": \"1000000\",\"remainAmountInit\": \"1000000\",\"subjectType\": \"定向融资计划\",\"subjectTypeName\": \"定向融资计划\",\"prodSubType\": \"担保类\",\"deadline\": \"255\",\"expectedMaxAnnualRate\": \"6.20\",\"productSubType\": \"A\",\"status\": \"认购期\",\"mostHolderNum\": \"200\",\"specialCustomerNum\": \"0\",\"warnFlag\": \"1\",\"isSpecial\": \"0\",\"onlyMobile\": \"0\",\"onlyWeiXin\": \"0\",\"onlyNew\": \"0\",\"process\": \"0.00\",\"sellingStatus\": \"1\",\"specialAmountInit\": \"0\",\"iconsType\": \"9999\",\"isTransfer\": \"transfer\",\"unActualTransferUserLevel\": \"0\",\"unActualBuyUserLevel\": \"0\",\"canUseKq\": \"0\",\"iconsList\": [{\"iconsPosition\": \"l_middle\",\"uuid\": \"oV9yv5IifWnBJ5JpXo4XsiwCZVXjXb8N\"}]},{\"productCode\": \"LC18050103\",\"productName\": \"0530-6\",\"buyStartDate\": \"2018-05-30 09:47:00\",\"buyEndDate\": \"2018-06-14 10:50:00\",\"buyEndTimes\": \"179180000\",\"manageStartDate\": \"2018-01-08 00:00:00\",\"manageEndDate\": \"2018-09-20 00:00:00\",\"riskLevel\": \"2\",\"riskLevelStr\": \"较低风险\",\"buyerSmallestAmount\": \"1,000\",\"buyTotalAmount\": \"1000000\",\"buyRemainAmount\": \"1000000\",\"remainAmountInit\": \"1000000\",\"subjectType\": \"定向融资计划\",\"subjectTypeName\": \"定向融资计划\",\"prodSubType\": \"担保类\",\"deadline\": \"255\",\"expectedMaxAnnualRate\": \"6.20\",\"productSubType\": \"A\",\"status\": \"认购期\",\"mostHolderNum\": \"200\",\"specialCustomerNum\": \"0\",\"warnFlag\": \"1\",\"isSpecial\": \"0\",\"onlyMobile\": \"0\",\"onlyWeiXin\": \"0\",\"onlyNew\": \"0\",\"process\": \"0.00\",\"sellingStatus\": \"1\",\"specialAmountInit\": \"0\",\"iconsType\": \"9999\",\"isTransfer\": \"transfer\",\"unActualTransferUserLevel\": \"0\",\"unActualBuyUserLevel\": \"0\",\"canUseKq\": \"0\",\"iconsList\": [{\"iconsPosition\": \"l_middle\",\"uuid\": \"oV9yv5IifWnBJ5JpXo4XsiwCZVXjXb8N\"}]},{\"productCode\": \"LC18050104\",\"productName\": \"0530-7\",\"buyStartDate\": \"2018-05-30 09:49:00\",\"buyEndDate\": \"2018-06-14 10:50:00\",\"buyEndTimes\": \"179180000\",\"manageStartDate\": \"2018-01-08 00:00:00\",\"manageEndDate\": \"2018-09-20 00:00:00\",\"riskLevel\": \"2\",\"riskLevelStr\": \"较低风险\",\"buyerSmallestAmount\": \"1,000\",\"buyTotalAmount\": \"1000000\",\"buyRemainAmount\": \"1000000\",\"remainAmountInit\": \"1000000\",\"subjectType\": \"定向融资计划\",\"subjectTypeName\": \"定向融资计划\",\"prodSubType\": \"担保类\",\"deadline\": \"255\",\"expectedMaxAnnualRate\": \"6.20\",\"productSubType\": \"A\",\"status\": \"认购期\",\"mostHolderNum\": \"200\",\"specialCustomerNum\": \"0\",\"warnFlag\": \"1\",\"isSpecial\": \"0\",\"onlyMobile\": \"0\",\"onlyWeiXin\": \"0\",\"onlyNew\": \"0\",\"process\": \"0.00\",\"sellingStatus\": \"1\",\"specialAmountInit\": \"0\",\"iconsType\": \"9999\",\"isTransfer\": \"transfer\",\"unActualTransferUserLevel\": \"0\",\"unActualBuyUserLevel\": \"0\",\"canUseKq\": \"0\",\"iconsList\": [{\"iconsPosition\": \"l_middle\",\"uuid\": \"oV9yv5IifWnBJ5JpXo4XsiwCZVXjXb8N\"}]},{\"productCode\": \"LC18060010\",\"productName\": \"zhu产品测试2\",\"buyStartDate\": \"2018-06-06 14:48:00\",\"buyEndDate\": \"2018-06-22 21:06:00\",\"buyEndTimes\": \"907340000\",\"manageStartDate\": \"2018-06-08 00:00:00\",\"manageEndDate\": \"2018-11-06 00:00:00\",\"riskLevel\": \"5\",\"riskLevelStr\": \"高风险\",\"buyerSmallestAmount\": \"1,000\",\"buyTotalAmount\": \"100000000\",\"buyRemainAmount\": \"1900\",\"remainAmountInit\": \"1900\",\"subjectType\": \"定向融资计划\",\"subjectTypeName\": \"定向融资计划\",\"prodSubType\": \"担保类\",\"deadline\": \"151\",\"expectedMaxAnnualRate\": \"6.20\",\"productSubType\": \"A\",\"status\": \"认购期\",\"mostHolderNum\": \"197\",\"specialCustomerNum\": \"3\",\"warnFlag\": \"1\",\"isSpecial\": \"0\",\"onlyMobile\": \"0\",\"onlyWeiXin\": \"0\",\"onlyNew\": \"0\",\"process\": \"0.00\",\"sellingStatus\": \"1\",\"specialAmountInit\": \"10000\",\"iconsType\": \"9999\",\"isTransfer\": \"transfer\",\"unActualTransferUserLevel\": \"0\",\"unActualBuyUserLevel\": \"0\",\"canUseKq\": \"0\",\"iconsList\": [{\"iconsPosition\": \"l_middle\",\"uuid\": \"oV9yv5IifWnBJ5JpXo4XsiwCZVXjXb8N\"}]},{\"productCode\": \"LC18010032\",\"productName\": \"16号起息（原17号）\",\"buyStartDate\": \"2018-01-15 17:23:00\",\"buyEndDate\": \"2019-01-19 10:44:00\",\"buyEndTimes\": \"19100420000\",\"manageStartDate\": \"2018-04-17 00:00:00\",\"manageEndDate\": \"2020-07-09 00:00:00\",\"riskLevel\": \"4\",\"riskLevelStr\": \"较高风险\",\"buyerSmallestAmount\": \"100\",\"buyTotalAmount\": \"10000\",\"buyRemainAmount\": \"8200\",\"remainAmountInit\": \"10000\",\"subjectType\": \"定向融资计划\",\"subjectTypeName\": \"定向融资计划\",\"prodSubType\": \"综合类\",\"deadline\": \"814\",\"expectedMaxAnnualRate\": \"10.00\",\"productSubType\": \"A\",\"status\": \"认购期\",\"mostHolderNum\": \"1\",\"specialCustomerNum\": \"0\",\"warnFlag\": \"1\",\"isSpecial\": \"0\",\"onlyMobile\": \"0\",\"onlyWeiXin\": \"0\",\"onlyNew\": \"0\",\"process\": \"18.00\",\"sellingStatus\": \"1\",\"specialAmountInit\": \"0\",\"iconsType\": \"9999\",\"isTransfer\": \"notTransfer\",\"unActualTransferUserLevel\": \"0\",\"unActualBuyUserLevel\": \"0\",\"canUseKq\": \"0\"}],\"scrollTime\": \"5\",\"displayNum\": \"2\"}}}";
		

		//for(int i=1; i<px.split("productCode|'").length;i++) {
		//System.out.println(px.split("productCode|'")[i].split("\"")[2]);
		//}
		String username = "15858274820";
		//String mobile = checkMobileUseful(url_address,test_environment);
		String password = "ZDIzYjRhMDVmZTg0YzY0YTliY2IwNjFkYzVjODkzMGM=";
		//String isOnlyNew = "0";
		AppRiskAssessmentCommit(url_address,test_environment,username,password);
		//AppQueryPayInit(url_address,test_environment,username,password);
		//AppRegister(url_address,test_environment,password);
		//AppSmsCodeGenerate(url_address,test_environment,mobile);
	}

	
	public static void AppLogin(String url_address, String test_environment,String username, String password) {
		
		//urlparams
		String fh = Util.getProperties(account_PATH, "APP_login_fh"); 
		String pbname = Util.getProperties(account_PATH, "APP_Login_PBname");
		//String username = Util.getProperties(account_PATH, "login_mobile");
		//String password = Util.getProperties(account_PATH, "login_password");	

		URIBuilder builder = new URIBuilder();
        URI uri = null;
        String body ="{\"username\":\""+username+"\",\"password\":\""+password+"\",\"loginMethod\":\""+0+"\"}";
        String loginURL= Util.getProperties(url_address, test_environment+"_APP_login");
        
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
			HttpClientUtils.HttpJsonGetCookie(uri.toString());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	
	}
	
public static void AppRegister(String url_address, String test_environment,String password) {
		
		//urlparams
		String fh = Util.getProperties(account_PATH, "APP_rengou_fh"); 
		String pbname = Util.getProperties(account_PATH, "APP_register_PBname");
		Map<String,String> urlParams = new HashMap<String,String>();
		urlParams.put("fh", fh);
		urlParams.put("pbname", pbname);
		urlParams.put("deviceNo","2e9b81deb75420a2");
		
		String usermobile = checkMobileUseful(url_address, test_environment);
		String imageCode = AppImageCodeGenerate(url_address, test_environment);
		String checkCode = AppSmsCodeGenerate(url_address,test_environment,usermobile,imageCode);

		
		JSONObject jsonParam = new JSONObject();  
		
		jsonParam.put("mobile", usermobile);
		jsonParam.put("imageCode", imageCode);
		jsonParam.put("checkCode", checkCode);
		jsonParam.put("loginPassword",password);
		jsonParam.put("loginPasswordSure",password);
		jsonParam.put("channelNo","12");
		jsonParam.put("registeredDeviceId","2e9b81deb75420a2");
		//jsonParam.put("registeredSystem","android%206.0.1");
		//jsonParam.put("registeredModel","Mi%20Note%202");

		
		String reqURL = Util.getProperties(url_address, test_environment+"_APP_trade");
		String responseBody = HttpClientUtils.httpJsonPostURLParams(reqURL,urlParams,jsonParam);
		
		String retCode = responseBody.split("returnCode|'")[1].split("\"")[2];;
		if(retCode.equals("0000")) 
		{
			logger.info("AppRegister successed. User: "+usermobile);
		}
		
}

	public static void AppSetTradePassword(String url_address, String test_environment,String username,String password,String tradePassword) {
	
		//login
		AppLogin(url_address, test_environment, username, password);
		
		
		Map<String,String> urlParams = new HashMap<String,String>();
		String fh = Util.getProperties(account_PATH, "APP_rengou_fh"); 
		String pbname = Util.getProperties(account_PATH, "APP_setTradePassword_PBname");
		urlParams.put("fh", fh);
		urlParams.put("pbname", pbname);
		
		 JSONObject jsonParam = new JSONObject();  
		 jsonParam.put("password", tradePassword);
		 jsonParam.put("passwordSure", tradePassword);
		
		 String reqURL = Util.getProperties(url_address, test_environment+"_APP_productQuery");
		 String responseBody = HttpClientUtils.httpJsonPostURLParams(reqURL,urlParams,jsonParam);
		 
		 String retCode = responseBody.split("returnCode|'")[1].split("\"")[2];;
			if(retCode.equals("0000")) 
			{
				logger.info("Set trade password successed. User: "+username);
			}

	}
	
	
	
	public static List<String> AppGetSellingProductList(String url_address, String test_environment,String username, String password,String isOnlyNew) {
		String productCode =null;
		String reqURL ="";
		
		//login firstly
		AppLogin(url_address,test_environment,username,password);
		
		Map<String,String> urlParams = new HashMap<String,String>();
		String fh = Util.getProperties(account_PATH, "APP_rengou_fh"); 
		String pbname = Util.getProperties(account_PATH, "APP_prdListqueryNew_PBname");
		urlParams.put("fh", fh);
		urlParams.put("pbname", pbname);
		
		 JSONObject jsonParam = new JSONObject();  
		 jsonParam.put("pageIndex", "1");
		 jsonParam.put("pageSize", "10");
		 jsonParam.put("terminalNo", "12");
		 jsonParam.put("sellingStatus", "1");
		 jsonParam.put("isOnlyNew", isOnlyNew);
		
		reqURL = Util.getProperties(url_address, test_environment+"_APP_productQuery");
		String responseBody = HttpClientUtils.httpJsonPostURLParams(reqURL,urlParams,jsonParam);
		
		List<String> prdCodeList = new ArrayList<String>();
			
		if(responseBody.contains("productCode")){
		/*for(int i=1; i<responseBody.split("productCode|'").length;i++) {
			prdCodeList.add(responseBody.split("productCode|'")[i].split("\"")[2]);
		}*/
		
	    //add whole product string into list
	//"productCode": "**","productName": "**","buyStartDate": "**","buyEndDate": "**","buyEndTimes": "**","manageStartDate": "**","manageEndDate": "**","riskLevel": "2","riskLevelStr": "***","buyerSmallestAmount": "**","buyTotalAmount": "1000000","buyRemainAmount": "**","remainAmountInit": "**","subjectType": "定向融资计划","subjectTypeName": "**","prodSubType": "**","deadline": "255","expectedMaxAnnualRate": "**","productSubType": "A","status": "认购期","mostHolderNum": "86","specialCustomerNum": "111","warnFlag": "1","isSpecial": "0","onlyMobile": "0","onlyWeiXin": "0","onlyNew": "0","process": "90.40","sellingStatus": "1","specialAmountInit": "0","iconsType": "**","isTransfer": "transfer","unActualTransferUserLevel": "0","unActualBuyUserLevel": "0","canUseKq": "0"
			for(int i=0; i<responseBody.split("}").length-1;i++) {
				String tmpVal = responseBody.split("}")[i].split("\\{")[1];
				if(tmpVal.contains("productCode")){
					prdCodeList.add(tmpVal);
				}
			}

		/*for(int j=0; j<prdCodeList.size();j++) {
			System.out.println("prdCodeList: "+prdCodeList.get(j));
		}*/
		return prdCodeList;
		}
		else {
			return null;
		}
	}
	
	public static void AppQueryProductList(String url_address, String test_environment,String username, String password) {
		String productCode ="LC18060019";
		String reqURL ="";
		//login firstly
		AppLogin(url_address,test_environment,username,password);
		
		Map<String,String> urlParams = new HashMap<String,String>();
		String fh = Util.getProperties(account_PATH, "APP_rengou_fh"); 
		String pbname = Util.getProperties(account_PATH, "APP_prdListquery_PBname");
		urlParams.put("fh", fh);
		urlParams.put("pbname", pbname);
		
		 JSONObject jsonParam = new JSONObject();  
		 jsonParam.put("pageIndex", "1");
		 jsonParam.put("pageSize", "10");
		 jsonParam.put("startDate", "2018-06-12 12:36:16");
		 jsonParam.put("productCode", productCode);
		 
		
		reqURL = Util.getProperties(url_address, test_environment+"_APP_trade");
		String responseBody = HttpClientUtils.httpJsonPostURLParams(reqURL,urlParams,jsonParam);
		
		String retCode = responseBody.split("returnCode|'")[1].split("\"")[2];;
		if(retCode.equals("0000")) 
		{
			logger.info("AppQueryProductList successed.");
		}
	}
	
	public static void AppRiskAssessmentCommit(String url_address, String test_environment,String username, String password) {
		
		String reqURL ="";
		//login firstly
		AppLogin(url_address,test_environment,username,password);
		
		Map<String,String> urlParams = new HashMap<String,String>();
		String fh = Util.getProperties(account_PATH, "APP_rengou_fh"); 
		String pbname = Util.getProperties(account_PATH, "APP_riskAssessmentCommit_PBname");
		urlParams.put("fh", fh);
		urlParams.put("pbname", pbname);
		
		 JSONObject jsonParam = new JSONObject();  
		 jsonParam.put("point", "50");
		
		reqURL = Util.getProperties(url_address, test_environment+"_APP_trade");
		String responseBody = HttpClientUtils.httpJsonPostURLParams(reqURL,urlParams,jsonParam);
		
		String retCode = responseBody.split("returnCode|'")[1].split("\"")[2];;
		if(retCode.equals("0000")) 
		{
			logger.info("AppRiskAssessmentCommit successed.");
		}
	}
	
	public static String AppQueryPayInit(String url_address, String test_environment,String username, String password,String isOnlyNew) {
		String repeatCommitCheckCode="";
		String reqURL ="";
		//List<String> productList = AppGetSellingProductList(url_address,test_environment,username,password,isOnlyNew);
		//String productCode =productList.get(0).split("productCode")[1].split("\"")[2];	
		String productCode="LC18060019";
				
		//login firstly
		//AppLogin(url_address,test_environment,username,password);
		
		Map<String,String> urlParams = new HashMap<String,String>();
		String fh = Util.getProperties(account_PATH, "APP_rengou_fh"); 
		String pbname = Util.getProperties(account_PATH, "APP_queryPayInit_PBname");
		urlParams.put("fh", fh);
		urlParams.put("pbname", pbname);
		urlParams.put("terminalNo","12");
		urlParams.put("userid", username);
				
		 JSONObject jsonParam = new JSONObject();  
		 jsonParam.put("productCode", productCode);
		 jsonParam.put("delegateNum", "1000");
		 jsonParam.put("payType", "subscribePay");
		 //jsonParam.put("orderType", "1");

		 	
		reqURL = Util.getProperties(url_address, test_environment+"_APP_trade");
		String responseBody = HttpClientUtils.httpJsonPostURLParams(reqURL,urlParams,jsonParam);
		
		String retCode = responseBody.split("returnCode|'")[1].split("\"")[2];;
		if(retCode.equals("0000")) {
			repeatCommitCheckCode= responseBody.split("repeatCommitCheckCode|'")[1].split("\"")[2];
			 
		}
		return repeatCommitCheckCode;
	}
	
	public static String AppSubscribeProductPre(String url_address, String test_environment,String username,String password,String isOnlyNew) {
		String retCode="";
		//String isOnlyNew = "0";
		
		List<String> productList = AppGetSellingProductList(url_address,test_environment,username,password,isOnlyNew);
		//String productCode=productList.get(0).split(",")[0].split("\"")[3];
		String productCode =productList.get(0).split("productCode")[1].split("\"")[2];	
		System.out.println("productCode:"+productCode);
		String buyerSmallestAmount= productList.get(0).split("buyerSmallestAmount")[1].split("\"")[2];
		
		Map<String,String> urlParams = new HashMap<String,String>();
		String fh = Util.getProperties(account_PATH, "APP_rengou_fh"); 
		String pbname = Util.getProperties(account_PATH, "APP_subscribeProductPre_PBname");
		urlParams.put("fh", fh);
		urlParams.put("pbname", pbname);
		
		JSONObject jsonParam = new JSONObject();  
		jsonParam.put("userid", username);
		jsonParam.put("productCode", productCode);
         
		
		String reqURL = Util.getProperties(url_address, test_environment+"_APP_trade");
		String responseBody = HttpClientUtils.httpJsonPostURLParams(reqURL,urlParams,jsonParam);
		
		
		return retCode;
	}
	
	public static String AppSubscribeProductSec(String url_address, String test_environment,String username,String password,String isOnlyNew) {
		String retCode="";
		//String isOnlyNew = "0";
		
		List<String> productList = AppGetSellingProductList(url_address,test_environment,username,password,isOnlyNew);
		//String productCode=productList.get(0).split(",")[0].split("\"")[3];
		String productCode =productList.get(0).split("productCode")[1].split("\"")[2];	
		
		String buyerSmallestAmount= productList.get(0).split("buyerSmallestAmount")[1].split("\"")[2].replace(",","");
		
		System.out.println("productCode:"+productCode+";buyerSmallestAmount:"+buyerSmallestAmount);
		
		Map<String,String> urlParams = new HashMap<String,String>();
		String fh = Util.getProperties(account_PATH, "APP_rengou_fh"); 
		String pbname = Util.getProperties(account_PATH, "APP_subscribeProductSec_PBname");
		urlParams.put("fh", fh);
		urlParams.put("pbname", pbname);
		
		JSONObject jsonParam = new JSONObject();  
		jsonParam.put("userid", username);
		jsonParam.put("productCode", productCode);
		jsonParam.put("delegateNum", buyerSmallestAmount);
         
		
		String reqURL = Util.getProperties(url_address, test_environment+"_APP_trade");
		String responseBody = HttpClientUtils.httpJsonPostURLParams(reqURL,urlParams,jsonParam);
		
		
		return retCode;
	}
	
	public static String AppRengou1(String url_address, String test_environment,String username,String password,String isOnlyNew) {
		String retCode="";
		//String isOnlyNew = "0";
		String repeatCommitCheckCode="";
		
		List<String> productList = AppGetSellingProductList(url_address,test_environment,username,password,isOnlyNew);
		//String productCode=productList.get(0).split(",")[0].split("\"")[3];
		String productCode =productList.get(0).split("productCode")[1].split("\"")[2];	
		
		String buyerSmallestAmount= productList.get(0).split("buyerSmallestAmount")[1].split("\"")[2].replace(",","");
		
		System.out.println("productCode:"+productCode+";buyerSmallestAmount:"+buyerSmallestAmount);
		
		Map<String,String> urlParams = new HashMap<String,String>();
		String fh = Util.getProperties(account_PATH, "APP_rengou_fh"); 
		String pbname = Util.getProperties(account_PATH, "APP_rengou_PBname");
		urlParams.put("fh", fh);
		urlParams.put("pbname", pbname);
		
		repeatCommitCheckCode = AppQueryPayInit(url_address,test_environment,username,password,isOnlyNew);
		
		JSONObject jsonParam = new JSONObject();  
		jsonParam.put("userid", username);
		jsonParam.put("productCode", productCode);
		jsonParam.put("delegateNum", buyerSmallestAmount);
		jsonParam.put("password", "MjI2MWY3ZjE5MGZiYWM3Yzk4MjgzYjVlMzBiYWIyYzk=");
		jsonParam.put("payType", "subscribePay");
		jsonParam.put("repeatCommitCheckCode", repeatCommitCheckCode);
		jsonParam.put("channelNo", "12");
		jsonParam.put("kqCode", "");
		jsonParam.put("kqType", "");
		jsonParam.put("kqValue", "");
         
		
		String reqURL = Util.getProperties(url_address, test_environment+"_APP_trade");
		String responseBody = HttpClientUtils.httpJsonPostURLParams(reqURL,urlParams,jsonParam);
		
		retCode = responseBody.split("returnCode|'")[1].split("\"")[2];
		//Assert.assertTrue(retCode.equals("0000"));
	
		return retCode;
	}
	
	public static String AppImageCodeGenerate(String url_address, String test_environment) {
		
		String cookieValue = "";
		String redisValue = "";
		String reqURL = Util.getProperties(url_address, test_environment+"_app_url_prefix")+Util.getProperties(url_address, test_environment+"_APP_imageGenerate");
	
		String cookieName="ZJSRANDOMID";
		try {
			cookieValue = HttpClientUtils.getCookieValueByName(HttpClientUtils.HttpJsonGetCookie(reqURL),cookieName);
			System.out.println(cookieValue);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String redisKey = "authcode:-"+cookieValue;
		Map<String,String> redisValueMap = Util.hgetallValuefromRedis(redisKey ,"App");
		for(Map.Entry<String, String>entry: redisValueMap.entrySet()) {
			if(entry.getKey().contains("code")) {
				redisValue=entry.getValue().substring(1);
			}
		}
		
		return redisValue;
	}
	
	public static String AppSmsCodeGenerate(String url_address, String test_environment,String mobile, String imageCode) {
	   String checkCode="";
		
	   	String reqURL = Util.getProperties(url_address, test_environment+"_APP_trade");

		Map<String,String> urlParams = new HashMap<String,String>();
		String fh = Util.getProperties(account_PATH, "APP_rengou_fh"); 
		String pbname = Util.getProperties(account_PATH, "APP_smsValidateCode_PBname");
		urlParams.put("fh", fh);
		urlParams.put("pbname", pbname);
		
		JSONObject jsonParam = new JSONObject();  
		jsonParam.put("mobile", mobile);
		jsonParam.put("authCode", imageCode);
		logger.info(jsonParam.toJSONString());
		
		String responseBody = HttpClientUtils.httpJsonPostURLParams(reqURL,urlParams,jsonParam);
	    String retCode = responseBody.split("returnCode|'")[1].split("\"")[2];;
		if(retCode.equals("0000"))
		{
			String redisKey =  "validatecodeobject:-"+mobile+"A";
			Map<String,String> redisValueMap = Util.hgetallValuefromRedis(redisKey,"App");
			for(Map.Entry<String, String>entry: redisValueMap.entrySet()) {
				if(entry.getKey().contains("validateCode")) {
					checkCode=entry.getValue().substring(1);
					logger.info(checkCode);
				}
			}
		}
	   return checkCode;
	}
	
	
	public static String checkImageCodeGenerate(String url_address, String test_environment) {

		String redisKey = "";
		String redisValue = "";
		String cookieValue = "";
		String cookieName="ZJS_AUTH_CODE_ID";
		String test_prefix = test_environment+"_url_prefix";
		String authCode_url =Util.getProperties(url_address, test_prefix)+ Util.getProperties(url_address, "authCode_suffix");
		
		//generate image auth code, and fetch from redis cluster
		try {
			cookieValue = HttpClientUtils.getCookieValueByName(HttpClientUtils.HttpJsonGetCookie(authCode_url),cookieName);
		   
			Pattern p = Pattern.compile("\\w*$");
			Matcher m = p.matcher(cookieValue);
			if(m.find()) {
				redisKey= m.group();
		    	logger.info("redisValue is "+ redisKey);
		    	}
		
			logger.info("fetch cookie: "+ redisKey);
			 p = Pattern.compile("\\w{4}$");
			 redisValue = Util.getValuefromRedis(redisKey,test_environment);
			 logger.info("fetch redis value: "+ redisValue);
	         m = p.matcher(redisValue);
		    if(m.find()) {
		    	redisValue= m.group();
		    	logger.info("imageCode is "+redisValue);
		    	}
					
			return redisValue;
						
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
				
	}
	
	public static String SmsCodeGenerate(String url_address, String test_environment,String mobile) {
		
		String checkCode="";
		String imageCode ="";
		String test_prefix = test_environment+"_url_prefix";
		
		imageCode = checkImageCodeGenerate(url_address,test_environment);
		String authCode_url =Util.getProperties(url_address, test_prefix)+ Util.getProperties(url_address, "authCode_suffix");
		String smsImageValiCode_url =Util.getProperties(url_address, test_prefix)+ Util.getProperties(url_address, "smsImageValidateCode_suffix");;
		
		//HttpClientUtils.HttpJsonGetCookie(authCode_url);
		//String output = HttpClientUtils.doPost(smsImageValiCode_url);
		
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("mobile", mobile);
		parameterMap.put("imageCode", imageCode);
		parameterMap.put("smsValidateCodeType", "A");
		logger.info(parameterMap);
		
		String output = HttpClientUtils.HttpJsonPostCookie(parameterMap,smsImageValiCode_url);
		logger.info("send sms validate code response:"+output);
				
		//return code = 0000, fetch sms check code from redis
		if(output.contains("\"returnCode\":\"0")) {
			logger.info("return code = 0000, fetch sms check code from redis!");
			String redisKey = mobile+"A";
			String redisValue = Util.getValuefromRedis(redisKey,test_environment);
			logger.info("fetch redis value: "+ redisValue);
			Pattern p = Pattern.compile("\\d{5}t");
		    Matcher m = p.matcher(redisValue);
		    if(m.find()) {
		    	checkCode= m.group();
		    	p= Pattern.compile("\\d{5}");
		    	m =p.matcher(checkCode);
		    	if(m.find()) {
		    		checkCode = m.group();
		    	}
		    	logger.info("sms Validate Code is "+checkCode);
		    }
		}
	
	    return checkCode;
	}

}
