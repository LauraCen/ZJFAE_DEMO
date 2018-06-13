package testcase;

import org.testng.annotations.DataProvider;

import utils.Util;

public class staticProvider {

	@DataProvider(name ="loginData")
	public Object[][] loginData(){
		
		String file = "C:\\TestData\\APITestData.xlsx";
		Object[][] records;
		records=Util.getExpectationData(file, "Login");
		return records;
	}
	
	@DataProvider(name ="registerData")
	public Object[][] registerData(){
		
		String file = "C:\\TestData\\APITestData.xlsx";
		Object[][] records;
		records=Util.getExpectationData(file, "Register");
		return records;
	}
	
	@DataProvider(name ="AppRengou")
	public Object[][] AppRengou(){
		
		String file = "C:\\TestData\\APITestData.xlsx";
		Object[][] records;
		records=Util.getExpectationData(file, "AppRengou");
		return records;
	}
	
	@DataProvider(name ="ApploginData")
	public Object[][] ApploginData(){
		
		String file = "C:\\TestData\\APITestData.xlsx";
		Object[][] records;
		records=Util.getExpectationData(file, "AppLogin");
		return records;
	}
	
	@DataProvider(name ="AppRegisterData")
	public Object[][] AppRegisterData(){
		
		String file = "C:\\TestData\\APITestData.xlsx";
		Object[][] records;
		records=Util.getExpectationData(file, "AppRegister");
		return records;
	}
	
}
