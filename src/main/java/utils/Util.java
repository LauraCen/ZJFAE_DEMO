package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONParser;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author cenjing
 *
 */

public class Util {
	private static Workbook workBook;
	private static Sheet sheet;
	private static Row xrow;
		
	private static Log logger = LogFactory.getLog(Util.class);
	private static final String CONFIGURATION_PATH = "./src/main/resources/mybatis-config.xml";
	private static final String url_PATH= "./config/url.properties";
	private static final String account_PATH= "./config/testAccount.properties";
    
    private static final Map<String, SqlSessionFactory> SQLSESSIONFACTORYS   
        = new HashMap<String, SqlSessionFactory>();
	
	 public static SqlSessionFactory getSqlSessionFactory(String environment) {  
	        
	        SqlSessionFactory sqlSessionFactory = null;
	        sqlSessionFactory = SQLSESSIONFACTORYS.get(environment);
	        if (sqlSessionFactory != null)
	            return sqlSessionFactory;
	        else {  
	            InputStream inputStream = null;
	            try {
	                inputStream = new FileInputStream(new File(CONFIGURATION_PATH));  
	                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, environment);   
	                logger.info("Get {} SqlSessionFactory successfully."+ environment);  
	            } catch (IOException e) {  
	            	logger.debug("Get {} SqlSessionFactory error."+ environment);  
	            	logger.error(e.getMessage(), e);  
	            }
//	            finally {  
//	                IOUtils.closeQuietly(inputStream);  
//	            }
	              
	            SQLSESSIONFACTORYS.put(environment, sqlSessionFactory);
	            return sqlSessionFactory;
	        }
	    }
     /*   
	 public static enum DataSourceEnvironment{
		 test104,
		 test106;
		 }*/
	 
	public static String getPhoneNum(){
		String phoneNum = null;
		phoneNum = "1"+String.valueOf(Math.random()).substring(2, 12);
		logger.info(phoneNum);
	    return phoneNum;
	}
	 
	public static String MD5(String plainText) {
		String re_md5 = new String();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}

			re_md5 = buf.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return re_md5;
	}

	@SuppressWarnings("resource")
	public static String getValuefromRedis(String key, String test_environment ) {
		
		
		String getValue ="";
		JedisCluster jedisCluster; 
		String redis_cluster= Util.getProperties(url_PATH,test_environment+"_redis");
		//System.out.println(redis_cluster);
		
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(1);
		poolConfig.setMaxIdle(1);
		poolConfig.setMaxWaitMillis(1000);
		
		//redis cluster node config
		Set<HostAndPort> nodes = new LinkedHashSet<HostAndPort>();
		
		nodes.add(new HostAndPort(redis_cluster,7001));
		nodes.add(new HostAndPort(redis_cluster,7002));
		nodes.add(new HostAndPort(redis_cluster,7003));
		nodes.add(new HostAndPort(redis_cluster,7004));
		
		jedisCluster = new JedisCluster(nodes,poolConfig);
		
		getValue =jedisCluster.get(key);
			
		try {
			jedisCluster.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getValue;
		
	}
	
	@SuppressWarnings("resource")
	public static Map<String, String> hgetallValuefromRedis(String key, String test_environment ) {
		
		StringBuilder getValueString = new StringBuilder();
		Map<String, String> getValueMap =new HashMap<String,String>();
		JedisCluster jedisCluster; 
		String redis_cluster= Util.getProperties(url_PATH,test_environment+"_redis");
		//System.out.println(redis_cluster);
		
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(1);
		poolConfig.setMaxIdle(1);
		poolConfig.setMaxWaitMillis(1000);
		
		//redis cluster node config
		Set<HostAndPort> nodes = new LinkedHashSet<HostAndPort>();
		
		nodes.add(new HostAndPort(redis_cluster,7001));
		nodes.add(new HostAndPort(redis_cluster,7002));
		nodes.add(new HostAndPort(redis_cluster,7003));
		nodes.add(new HostAndPort(redis_cluster,7004));
		
		jedisCluster = new JedisCluster(nodes,poolConfig);
		
		getValueMap =jedisCluster.hgetAll(key);
		
		try {
			jedisCluster.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return getValueMap;
		
	}

	public static String[][] getExpectationData(String path, String sheetName){
		try {
			File file = new File(path);
			FileInputStream fis = new FileInputStream(file);
			//POIFSFileSystem POIStream = new POIFSFileSystem(fis);
			if (path.endsWith("xls")) {
				 workBook = new HSSFWorkbook(fis);
				 sheet = workBook.getSheet(sheetName);
			} else {
				 workBook = new XSSFWorkbook(fis);
				 sheet = workBook.getSheet(sheetName);
			}
			
			int rowNum = sheet.getLastRowNum();
			List<String[]> results = new ArrayList<String[]>();
			
			//第一行是表头，所以从1开始，获取每个表中单元格的值
			for(int i =1; i <= rowNum; i++){
				
				xrow = sheet.getRow(i);
				int colNum = xrow.getLastCellNum();
				String[] data = new String[colNum];
				for(int j=0; j<colNum;j++) {
					try {
						data[j]= getCellValue(xrow.getCell(j));
					}
					catch(NullPointerException e) {//单元格为空时
					 data[j]="";
					}					
				}
				
				results.add(data);
			}
			fis.close();
			
			String[][] returnArray = new String[results.size()][rowNum];
			for(int i =0; i< returnArray.length;i++) {
				returnArray[i] =(String[])results.get(i);
			}
			
		    return returnArray;
			
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	private static String getCellValue(Cell cell) {

        String cellValue ="";
        DecimalFormat df = new DecimalFormat("#");
        if (cell != null) {
			switch (cell.getCellType()) {
			// 当单元格为数值类型时，作特殊处理
			case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC:
				cellValue = df.format(cell.getNumericCellValue()).toString(); ;
				break;
			// 当单元格为文本类型时，直接获取文本内容
			case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING:
				cellValue = String.valueOf(cell.getRichStringCellValue());
				break;
			// 当单元格为布尔类型时，转为文本内容TRUE或者FALSE
			case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN:
				cellValue = String.valueOf(cell.getBooleanCellValue());
				break;
			// 当单元格为公式类型时，获取公式计算所得的结果。结果若为文本直接获取文本内容，为数值则作数值处理
			case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_FORMULA:
				try {
					cellValue = String.valueOf(cell.getRichStringCellValue());
				} catch (IllegalStateException e) {
					cellValue = cell.getCellFormula();
				}
				break;
			}
		}	
		return cellValue;
	}
	
	public static String getProperties(String path, String key) {
		Configuration configuration = null;
		String value ="";
	
		try {
			configuration = new PropertiesConfiguration(path);
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(configuration.containsKey(key)) {
			value= String.valueOf(configuration.getProperty(key));
		}
		return value;
	}
	
	
	public static void setProperties(String path, String key, String value) {
	
		PropertiesConfiguration configuration = null;
		try {
			configuration = new PropertiesConfiguration(path);
			configuration.setAutoSave(true);
	
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		configuration.setProperty(key, value);
		
	}
	
	
	/*public static void main(String[] args) {
		
		setProperties(account_PATH, "register_mobile", getPhoneNum());
		System.out.println(getProperties(account_PATH, "register_mobile"));
		
	}*/
	

	
}
