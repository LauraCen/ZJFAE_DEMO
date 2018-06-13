/**
 * 
 */
package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.cookie.CookieSpec;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BestMatchSpecFactory;
import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.apache.poi.poifs.filesystem.Entry;

import com.alibaba.fastjson.JSONObject;

/**
 * @author cenjing
 *
 */
public class HttpClientUtils {

	private static Log logger = LogFactory.getLog(Util.class);
	static CookieStore  cookieStore = null;
	static HttpClientContext context = null;
	private static String testAccount_address = "./config/testAccount.properties";
	public static String cookieFilePath = Util.getProperties(testAccount_address, "cookie_store");
	private static String url_address = "./config/url.properties";
	private static String test_environment = Util.getProperties(url_address, "environment");
	
	public static String getCookieValueByName(HttpResponse response,String name) throws Exception{
		String cookieValue = null;
		String cookieName ="";
		String cookies="";
		
		if(response.containsHeader("Set-Cookie")) {
			Header[] headers = response.getHeaders("Set-Cookie");
			for(Header header : headers) {
				cookies = header.getValue();
				if(cookies.contains(name)){
					cookieValue = cookies.substring(cookies.indexOf("=") + 1, cookies.indexOf(";"));	
					}
			}
	
		}
		return cookieValue;
	}
	
	
	

	public static CookieStore setCookieStore(){
		
		String cookies = "";
		String cookieValue ="";
		String cookieName ="";
		String cookieFilePath = Util.getProperties(testAccount_address, "cookie_store");
		FileInputStream fin = null;
		
		File file = new File(cookieFilePath);
		if(file.exists()) {
			try {
				fin = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		//read cookie into file
		ObjectInputStream in;
		try {
			in=new ObjectInputStream(fin);
			cookieStore = (CookieStore)in.readObject();
			in.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		/*
		if(response.containsHeader("Set-Cookie")) {
			Header[] headers = response.getHeaders("Set-Cookie");
			for(Header header : headers) {
				cookies = header.getValue();
				System.out.println(cookies);
				cookieName = cookies.substring(0, cookies.indexOf("="));
				cookieValue = cookies.substring(cookies.indexOf("=") + 1, cookies.indexOf(";"));	
				BasicClientCookie cookie = new BasicClientCookie(cookieName, cookieValue);
				cookie.setPath("/"); 
			
				cookie.setDomain(domain);
				cookieStore.addCookie(cookie);
				logger.info(cookie); 
				try {
					fw.write(cookie.toString()+"\r\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}   
				
			}
		}
				
				if(cookies.contains("ZJS_AUTH_CODE_ID")) {
					
					String ZJS_CUSTOMER_CHECK_COOKIE = cookies.substring("ZJS_CUSTOMER_CHECK_COOKIE=".length(), cookies.indexOf(";"));
					String ZJS_AUTH_CODE_ID = cookies.substring("ZJS_AUTH_CODE_ID=".length(), cookies.indexOf(";"));
					BasicClientCookie cookie = new BasicClientCookie("ZJS_CUSTOMER_CHECK_COOKIE", ZJS_CUSTOMER_CHECK_COOKIE);
					cookie.setPath("/"); 
					cookieStore.addCookie(cookie);
					
				    cookie = new BasicClientCookie("ZJS_AUTH_CODE_ID", ZJS_AUTH_CODE_ID);
				    cookie.setPath("/");  		    
					cookieStore.addCookie(cookie);
					logger.info("cookie：" + cookie);
				}
				//to be debugged
				else if(cookies.contains("JESSIONID")) {
					String JESSIONID = cookies.substring("JESSIONID=".length(),cookies.indexOf(";"));
					
					BasicClientCookie cookie = new BasicClientCookie("JESSIONID",JESSIONID);
					cookie.setVersion(0);
					cookie.setDomain("zjfae.com");
					cookie.setPath("/"); 
					
					cookieStore.addCookie(cookie);
					}
						
		try {
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		logger.info("cookieStore: "+cookieStore);
		return cookieStore;
			
		}
	
	
	public static void setContext() {
		logger.info("----------Set Context-----------");
		
		context = HttpClientContext.create();
		Registry<CookieSpecProvider> registery = RegistryBuilder.<CookieSpecProvider>create()
				.register(CookieSpecs.BEST_MATCH, new BestMatchSpecFactory())
				.register(CookieSpecs.BROWSER_COMPATIBILITY, new BrowserCompatSpecFactory()).build();
		
		context.setCookieSpecRegistry(registery);
		context.setCookieStore(cookieStore);
		
	}
	
	public static List<NameValuePair> getParam(Map<String,String> parameterMap){
		List<NameValuePair> formParams = new ArrayList<NameValuePair>();
		for(Map.Entry<String, String> entry: parameterMap.entrySet()){
			formParams.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
		}
		
		/*
		Iterator it = parameterMap.entrySet().iterator();
	    while (it.hasNext()) {
	      Entry parmEntry = (Entry) it.next();
	      param.add(new BasicNameValuePair((String) ((org.apache.commons.httpclient.NameValuePair) parmEntry).getKey(),
	          (String) ((org.apache.commons.httpclient.NameValuePair) parmEntry).getValue()));
	    }*/
	    
		return formParams;
	}
	
	public static String HttpJsonGet(String url) {
		String responseBody = "";
		HttpResponse httpResponse = null;
		
		 logger.info("API request URL"+url);
		
		 CloseableHttpClient httpClient= HttpClients.createDefault();
		 HttpGet httpGet = new HttpGet(url);
		 logger.info("Get method starting on "+ url);
		 
		 try {
			 httpResponse= httpClient.execute(httpGet);
			int httpResponseCode = httpResponse.getStatusLine().getStatusCode();
			logger.info("Get response status code "+httpResponseCode);	
			if(httpResponseCode == 200) {				
				HttpEntity httpEntity = httpResponse.getEntity();
				if(httpEntity!=null) {					
					responseBody=EntityUtils.toString(httpEntity,"UTF-8");	
					return responseBody;
					//logger.info("HttpEntity :"+responseBody);
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		try {
			httpClient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				 
		return responseBody;
	}
	
	//RequestConfig defaultConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();  
	//getMethod.setConfig(defaultConfig); 

	@SuppressWarnings("deprecation")
	public static HttpResponse HttpJsonGetCookie(String url) {
		String responseBody = "";
		HttpResponse httpResponse = null;
		String domain = Util.getProperties(url_address, test_environment+"_domain");

		 logger.info("API request URL:"+url);
		 
		 CookieStore cookieStore = new BasicCookieStore();
		 CloseableHttpClient  httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();  
		 HttpClientContext context = HttpClientContext.create();
		 HttpGet httpGet = new HttpGet(url);
		 logger.info("Get method starting on: "+ url);
		 
		 try {
			 httpResponse= httpClient.execute(httpGet,context);
			 Header[] headers1 = httpResponse.getAllHeaders();
			 logger.info("#Header info  ------------------------");
				for (Header h : headers1) {
					logger.info(h.getName() +":"+ h.getValue());		
					
				}
			int httpResponseCode = httpResponse.getStatusLine().getStatusCode();
			logger.info("Get response status code "+httpResponseCode);	
			if(httpResponseCode == 200) {	  /*
				  List<org.apache.http.cookie.Cookie> cookies = cookieStore.getCookies();
		            for (int i = 0; i < cookies.size(); i++) {
		            	 BasicClientCookie bcookie = new BasicClientCookie(cookies.get(i).getName(),cookies.get(i).getValue());
		            	 bcookie.setDomain(cookies.get(i).getDomain());
		            	 bcookie.setExpiryDate(cookies.get(i).getExpiryDate());
		            	 bcookie.setPath(cookies.get(i).getPath());		    
		            	 cookieStore.addCookie(bcookie);
		            	}*/
		          
		            if(httpResponse.containsHeader("Set-Cookie")) {
		        			Header[] headers = httpResponse.getHeaders("Set-Cookie");
		        			for(Header header : headers) {
		        				String cookiesSet = header.getValue();
		        				if(cookiesSet !=null){
		        					String cookieKey = cookiesSet.substring(0,cookiesSet.indexOf("="));
		        					String cookieValue = cookiesSet.substring(cookiesSet.indexOf("=") + 1, cookiesSet.indexOf(";"));	
		        					BasicClientCookie bcookie1 = new BasicClientCookie(cookieKey,cookieValue);	
		        					bcookie1.setDomain(domain);
		        					bcookie1.setPath("/");
		        					cookieStore.addCookie(bcookie1);
		        				}		        				
		        			}
		            }
		            
				HttpEntity httpEntity = httpResponse.getEntity();
				if(httpEntity!=null) {					
					responseBody=EntityUtils.toString(httpEntity,"UTF-8");	
					logger.info("HttpEntity :"+responseBody);
					
				}
				
				File file = new File(cookieFilePath);
				FileOutputStream fos = new FileOutputStream(file);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				
				logger.info("cookieStore: "+ cookieStore);
				oos.writeObject(cookieStore);
				oos.close();
				fos.close();		
				
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		try {
			httpClient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				 
		return httpResponse;
	}
	
	
	public static String httpJsonPost(String reqURL, Map<String, String> reparamters) {
		String output= "";
		
		CloseableHttpClient httpClient =HttpClients.createDefault();
		HttpPost httppost = new HttpPost(reqURL);
		HttpResponse response = null;
		
		List<NameValuePair> pairs = null;
		if (reparamters != null && !reparamters.isEmpty()) {
			pairs = new ArrayList<NameValuePair>(reparamters.size());
			for (Map.Entry<String, String> entry : reparamters.entrySet()) {
				String value = entry.getValue();
				if (value != null) {
					pairs.add(new BasicNameValuePair(entry.getKey(), value));
					
				}
			}
		}
		HttpPost httpPost = new HttpPost(reqURL);
		
		if (pairs != null && pairs.size() > 0) {
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
				System.out.println(httpPost);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			response = httpClient.execute(httpPost);
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int statusCode = response.getStatusLine().getStatusCode();
		if(statusCode ==200) {
		HttpEntity entity = response.getEntity();
		String result = null;
		if (entity != null) {
			try {
				output = EntityUtils.toString(entity, "UTF-8");
				System.out.println(output);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			EntityUtils.consume(entity);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			}
		}
		
		/*
		MultipartEntityBuilder entity =MultipartEntityBuilder.create();
		for(String key:reparamters.keySet()){
			
			entity.addPart(key, new StringBody(reparamters.get(key),ContentType.TEXT_PLAIN.withCharset("UTF-8")));
		}
		httppost.setEntity(entity.build());  	
		
		
		try {
			response=httpClient.execute(httppost);
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		int httpResponseCode=response.getStatusLine().getStatusCode();
		if(httpResponseCode==200){
			HttpEntity httpResponseEntity = response.getEntity();
			try {
				responseBody=EntityUtils.toString(httpResponseEntity,"UTF-8");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			logger.info("Response"+response);
			logger.info("Response Body: "+responseBody);

		}*/
		
		try {
			httpClient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return output;
	}
	
	public static String HttpJsonPostCookie(Map<String,String> map, String reqURL) {
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try {
        	cookieStore= setCookieStore();
        	httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        	//httpClient = HttpClients.createDefault();
            httpPost = new HttpPost(reqURL);
            HttpClientContext context = HttpClientContext.create();
            
            /*//set cookies
             if(cookieStore!= null) {
            	System.out.println(cookieStore.getCookies().size());
            	for(int i = 0; i < cookieStore.getCookies().size(); i++) {
            		httpPost.addHeader(("Cookie"),cookieStore.getCookies().get(i).getName()+"="+cookieStore.getCookies().get(i).getValue());
            	}       
            }*/
            
  
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            for(Map.Entry<String, String> entry: map.entrySet()){
                list.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
    		}
            
            if (list.size() > 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
                httpPost.setEntity(entity);
            }

            HttpResponse response = httpClient.execute(httpPost,context);
            
            /*//print for debug
            Arrays.stream(response.getAllHeaders()).forEach(System.out::println);
            System.out.println("cookies : =========");
            context.getCookieStore().getCookies().forEach(System.out::println);
            System.out.println("cookies  end: =========");*/
          
            HttpEntity httpEntity = response.getEntity();
            if(httpEntity!=null) {
            	result= EntityUtils.toString(httpEntity,"UTF-8");
            }  
            
            httpClient.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
	
	
	@SuppressWarnings("deprecation")
	public static String httpJsonPost(String reqURL) {
		String output= "";
		
		CloseableHttpClient httpClient =HttpClients.createDefault();
		HttpPost httppost = new HttpPost(reqURL);
		HttpResponse response = null;

		httppost.setHeader("Content-type", "application/json");
		httppost.setHeader("Accept", "application/json");	
		   
		try {			
			//获取结果实体
	        response = httpClient.execute(httppost);

	        int statusCode = response.getStatusLine().getStatusCode();
	        if( statusCode>= 200 && statusCode<300) 
	        {
	           output= IOUtils.readLines(response.getEntity().getContent()).toString();
	           //System.out.println("output:===="+output);
	        }
	        else {
	        	
	        	throw new ClientProtocolException("Unexpected return " + statusCode);
            }
	        
	        httpClient.close();
	        
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return output;
	}
	
	public static String httpJsonPostURLParams(String reqURL,Map<String,String> urlParams, JSONObject jsonParam) {
		String responseBody="";
		String cookieFilePath = Util.getProperties(testAccount_address, "cookie_store");
		List<NameValuePair> pairs =null;
		cookieStore= setCookieStore();
		
		CloseableHttpClient  httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
		HttpClientContext context = HttpClientContext.create();
		//CloseableHttpClient httpClient=HttpClients.createDefault();
		StringBuffer bf = new StringBuffer();
		int i = 0;  
		for (String key : urlParams.keySet()){
			if (i == 0){
				bf.append("?").append(key).append("=").append(urlParams.get(key));
				}else{
				bf.append("&").append(key).append("=").append(urlParams.get(key)); 
			    }
			    i++;
		    } 

	    
	     reqURL += bf.toString();
	
		 logger.info("request URL:" +reqURL);
		 
		HttpPost httpPost = new HttpPost(reqURL);
		httpPost.setHeader("Content-type", "application/json");
		httpPost.setHeader("Accept", "application/json");	
		//if (pairs != null && pairs.size() > 0) {
			try {
				
				 StringEntity entity = new StringEntity(jsonParam.toString(),"utf-8"); 
				 entity.setContentEncoding("UTF-8");    
				 entity.setContentType("application/json");    
				 httpPost.setEntity(entity);
				logger.info(httpPost);
				//httpPost.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
				logger.info(httpPost);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			//}
		
		try {
			HttpResponse response = httpClient.execute(httpPost,context);
					
			/*Header[] headers = response.getAllHeaders();
			System.out.println("#Header info  ------------------------");
			for (Header h : headers) {
				System.out.println(h.getName() +":"+ h.getValue());		 
			}*/

			int httpResponseCode = response.getStatusLine().getStatusCode();
			HttpEntity httpEntity = response.getEntity();
			if(httpEntity!=null) {					
				responseBody=EntityUtils.toString(httpEntity,"UTF-8");	
				logger.info("HttpEntity :"+responseBody);
			}

         httpClient.close();
		} catch (ClientProtocolException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
		
		return responseBody;
		
	}
	
	@SuppressWarnings("deprecation")
	public static String httpJsonPostURLParamsCookie(String reqURL) {
		String responseBody="";
		cookieStore= setCookieStore();
		CloseableHttpClient httpClient= HttpClients.custom().setDefaultCookieStore(cookieStore).build();
		//CloseableHttpClient httpClient =HttpClients.createDefault();
		HttpPost httppost = new HttpPost(reqURL);
		HttpResponse response = null;
		HttpClientContext context = HttpClientContext.create();
		
		httppost.setHeader("Content-type", "application/json");
		httppost.setHeader("Accept", "application/json");	
		   
		try {			
			//获取结果实体
	        response = httpClient.execute(httppost,context);
	        /*Header[] headers = response.getAllHeaders();
			System.out.println("#Header info  ------------------------");
			for (Header h : headers) {
				System.out.println(h.getName() +":"+ h.getValue());
		 
			}
			*/
	        int httpResponseCode = response.getStatusLine().getStatusCode();
	        if(httpResponseCode ==200) {
	        	HttpEntity httpEntity = response.getEntity();
	        	if(httpEntity!=null) {
	        		responseBody= EntityUtils.toString(httpEntity,"UTF-8");
	        	}   
	        	System.out.println("HttpEntity :"+responseBody);
	        	System.out.println("#Header info  ------------------------"+httpResponseCode);
	        }

	        httpClient.close();
	        return responseBody;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	public static String httpJsonPostWithParamsHeaders(String reqURL, Map<String, String> reparamters, Map<String, String> header) {
		String responseBody= "";
		
		CloseableHttpClient httpClient =HttpClients.createDefault();
		HttpPost httpost = new HttpPost(reqURL);
		HttpResponse response = null;
		List<NameValuePair> pairs = null;
		try {
		if (reparamters != null && !reparamters.isEmpty()) {
			pairs = new ArrayList<NameValuePair>(reparamters.size());
			for (Map.Entry<String, String> entry : reparamters.entrySet()) {
				String value = entry.getValue();
				if (value != null) {
					pairs.add(new BasicNameValuePair(entry.getKey(), value));
				}
			}
		
			if (null != header && !header.isEmpty()) {
				for (String key : header.keySet()) {
					httpost.addHeader(key, header.get(key));
				}
			}
			if (pairs != null && pairs.size() > 0) {
				httpost.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
			}
			response = httpClient.execute(httpost);

			int statusCode = response.getStatusLine().getStatusCode();
			//redirect
			if(statusCode==302){
				Header location = response.getFirstHeader("location");
				httpost=new HttpPost(location.getValue());
				response=httpClient.execute(httpost);
				statusCode=response.getStatusLine().getStatusCode();
			}
			
			if (statusCode != 200) {
				httpost.abort();
				throw new RuntimeException("HttpClient,error status code :" + statusCode);
			}
			//获取header信息
			Header[] headers = response.getAllHeaders();
			System.out.println("#Header info  ------------------------");
			for (Header h : headers) {
				System.out.println(h.getName() +":"+ h.getValue());
		 
			}
			System.out.println("#Header info  ------------------------");
			System.out.println("\n");
			
			
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, "UTF-8");
			}
			EntityUtils.consume(entity);
			return result;
			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
			
		
	}
	private static String jsessionid = "";	// 响应头中的JSESSIONID
	public String sendSSLPostRequest(String reqURL, Map<String,String> params) {
		
		String responseContent =null;
		HttpResponse response =null;
		CloseableHttpClient httpClient = new DefaultHttpClient();
		
		X509TrustManager xtm = new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {} 
			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {} 
			@Override
			public X509Certificate[] getAcceptedIssuers() { return null; }
		};
		
		try {
			//TLS1.x与SSL3.x使用SSLContext，tpu上使用TLS1.2
			SSLContext ctx = SSLContext.getInstance("TLS");

			//使用TrustManager来初始化该上下文，TrustManager只是被SSL的Socket所使用
			ctx.init(null, new TrustManager[]{xtm}, null);
			
			//创建HostnameVerifier
				X509HostnameVerifier hostnameVerifier = new X509HostnameVerifier() {
				@Override
				public boolean verify(String arg0, SSLSession arg1) {return true;}
				@Override
				public void verify(String arg0, SSLSocket arg1) throws IOException {}
				@Override
				public void verify(String arg0, String[] arg1, String[] arg2) throws SSLException {}
				@Override
				public void verify(String arg0, X509Certificate arg1) throws SSLException {}

			};
			
			ctx.init(null, new TrustManager[] {xtm}, null);
			SSLSocketFactory socketFactory = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			
			//通过SchemeRegistry将SSLSocketFactory注册到HttpClient上
			httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 90000);
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 90000);
 
			HttpPost httpPost = new HttpPost(reqURL);
			List<NameValuePair> formParams = new ArrayList<NameValuePair>();
			for(Map.Entry<String, String> entry: params.entrySet()) {
				formParams.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));				
			}
			
			httpPost.setEntity(new UrlEncodedFormEntity(formParams,"UTF-8"));
			
			httpPost.setHeader("Cookie","JSESSIONID="+jsessionid);
			response = httpClient.execute(httpPost); //执行post request
			HttpEntity entity = response.getEntity();
			

			if (null != entity) {
				responseContent = EntityUtils.toString(entity, "UTF-8");
				EntityUtils.consume(entity);
			}
			logger.debug("Request URI: {} "+ httpPost.getURI());
			logger.debug("Respoonse Stauts: {} "+ response.getStatusLine());
			logger.debug("Resonpse Content: {} "+ responseContent);
			
			
			if(response.containsHeader("Set-Cookie")) {
				//获取头部的JSESSIONID
				String cookies = "";
				Header[] headers = response.getHeaders("Set-Cookie");
				for(Header header : headers) {
					cookies = header.getValue();
				}
				jsessionid = cookies.substring(cookies.indexOf("=") + 1, cookies.indexOf(";"));
				logger.info("JSESSIONID：" + jsessionid);
			}
			
			httpClient.close();
		}
		catch(Exception e) {
			e.printStackTrace();
			
		}
		
		return responseContent;
	}

}
