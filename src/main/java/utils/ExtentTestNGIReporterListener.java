/**
 * 
 */
package utils;

import java.io.File;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.xml.XmlSuite;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.ResourceCDN;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.TestAttribute;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;


/**
 * @author cenjing
 *
 */
public class ExtentTestNGIReporterListener implements IReporter {

	private static final String OUTPUT_FOLDER ="test-output/";
	private static final String FILE_NAME ="index.html";
	
	private ExtentReports extentReport ;
	//private ExtentReports extentReport = getInstance(OUTPUT_FOLDER + FILE_NAME);
	
	@Override
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
		init();
		
		boolean createSuiteNode = false;
		if(suites.size()>1) {
			createSuiteNode = true;
		}
		
		for(ISuite suite:suites) {
			Map<String, ISuiteResult> result = suite.getResults();
			//TestSuite 中没有用例直接跳过
            if(result.size() == 0) {
            	continue;
            }
            
            int suiteFailCount =0;
            int suitePassCount =0;
            int suiteSkipCount =0;
            ExtentTest suiteTest = null;
            
            if(createSuiteNode) {
            	suiteTest = extentReport.createTest(suite.getName()).assignCategory(suite.getName());          	
            }
            
            boolean createSuiteResultNode = false;
            if(result.size()>1) {
            	createSuiteResultNode=true;
            }
            
            for(ISuiteResult r:result.values()) {
            	ExtentTest resultNode;
            	ITestContext context = r.getTestContext();
            	if(createSuiteResultNode) {
            		if(null == suiteTest) {
            			resultNode = extentReport.createTest(r.getTestContext().getName());
            		}
            		else {
            			resultNode = suiteTest.createNode(r.getTestContext().getName());
            		}
            	}
            	else {
            		resultNode = suiteTest;
            	}
            	
            	if(resultNode != null) {
            		resultNode.getModel().setName(suite.getName()+":"+r.getTestContext().getName());
            	    if(resultNode.getModel().hasCategory()) {
            	    	resultNode.assignCategory(r.getTestContext().getName());
            	    }else {
            	    	resultNode.assignCategory(suite.getName(),r.getTestContext().getName());
            	    }
            	    
            	    resultNode.getModel().setStartTime(r.getTestContext().getStartDate());
            	    resultNode.getModel().setEndTime(r.getTestContext().getEndDate());
            	    
            	    int passSize = r.getTestContext().getPassedTests().size();
            	    int failSize = r.getTestContext().getFailedTests().size();
            	    int skipSize = r.getTestContext().getSkippedTests().size();
            	    
            	    suiteFailCount += failSize;
            	    suitePassCount += passSize;
            	    suiteSkipCount += skipSize;
            	    
            	    if(failSize>0) {
            	    	resultNode.getModel().setStatus(Status.FAIL);
            	    }
            	    
            	    resultNode.getModel().setDescription(String.format("Pass: %s; Fail: %s; Skip: %s;", passSize,failSize,skipSize));
            	}
            	
            	buildTestNodes(resultNode,context.getFailedTests(),Status.FAIL);
            	buildTestNodes(resultNode,context.getPassedTests(),Status.PASS);
            	buildTestNodes(resultNode,context.getSkippedTests(),Status.SKIP);           	
            	
            }//end of for loop
            
            if(suiteTest != null) {
            	suiteTest.getModel().setDescription(String.format("Test Suite Pass: %s; Fail: %s; Skip: %s; ", suitePassCount,suiteFailCount,suiteSkipCount));
                 
            	if(suiteFailCount>0) {
            		suiteTest.getModel().setStatus(Status.FAIL);
            	}

            }   

		}
		
		
		extentReport.flush();
		
	}

	private void buildTestNodes(ExtentTest resultNode, IResultMap tests, Status status) {
		
		String[] categories = new String[0];
		if(resultNode!=null) {
			List<TestAttribute> categoryList = resultNode.getModel().getCategoryContext().getAll();
			categories = new String[categoryList.size()];
			for(int index =0; index < categoryList.size();index++) {
				categories[index] = categoryList.get(index).getName();
			}
		
		}
		
		ExtentTest test;
		if(tests.size()>0) {
			// 调整用例排序，按时间排序
            Set<ITestResult> treeSet = new TreeSet<ITestResult>(new Comparator<ITestResult>() {
            	public int compare(ITestResult o1, ITestResult o2) {
            		return o1.getStartMillis() < o2.getStartMillis()?-1:1;
            	}
            });
		    
            treeSet.addAll(tests.getAllResults());
			for(ITestResult result:treeSet) {
				Object[] parameters = result.getParameters();
				String name="";
				
				for(Object param:parameters) {
					name += " "+param.toString();
				}
				
				if(name.length()>0) {
					if(name.length()>50) {
						name=name.substring(0, 49)+"...";
					}
				}else {
					name = result.getMethod().getMethodName();
					}
				if(resultNode == null) {
					test = extentReport.createTest(name);
				}else {
					test= resultNode.createNode(name).assignCategory(categories);
				}
				
				
			   
				for(String group:result.getMethod().getGroups()) {
					test.assignCategory(group);
				}
				
				List<String> outputList = Reporter.getOutput(result);
				for(String output:outputList) {
					test.debug(output);
				}
				
				if(result.getThrowable()!=null) {
					test.log(status, result.getThrowable());
				}
				else {
					test.log(status, "Test "+status.toString().toLowerCase()+"ed");
				}
				
				test.getModel().setStartTime(getTime(result.getStartMillis()));
				test.getModel().setEndTime(getTime(result.getEndMillis()));
			}
		}
		
		
	}
	


	private Date getTime(long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return calendar.getTime();
	}
	 /*
	 public ExtentReports getInstance(String filePath) {
	        if (extentReport == null)
	            createInstance(filePath);
	        return extentReport;
	    }
	

	 public void createInstance(String filePath) {
		   extentReport = new ExtentReports();
//	        extent.setSystemInfo("os", "Linux");
	        //关联extentx服务端
	        //<!--extentReport.attachReporter(createHtmlReporter(filePath), createExtentXReporter());-->
	        //关联klov服务端
	        extentReport.attachReporter(createHtmlReporter(filePath), createKlovReporter());
	    }

	    public ExtentHtmlReporter createHtmlReporter(String filePath){
	        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(filePath);
	        //报表位置
	        htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
	        //使报表上的图表可见
	        htmlReporter.config().setDocumentTitle("ExtentReports");
	        htmlReporter.config().setChartVisibilityOnOpen(true);
	        htmlReporter.config().setTheme(Theme.STANDARD);
	        htmlReporter.config().setDocumentTitle(filePath);
	        htmlReporter.config().setEncoding("utf-8");
	        htmlReporter.config().setReportName("plateClient自动化测试报告");
	        htmlReporter.config().setResourceCDN(ResourceCDN.EXTENTREPORTS);
	        return htmlReporter;
	    }

	    public ExtentXReporter createExtentXReporter() {
	        ExtentXReporter extentx = new ExtentXReporter("127.0.0.1",27017);
	        extentx.config().setProjectName("PlatClientTest");
	        extentx.config().setReportName("PlatClientTest Reports");
	        extentx.config().setServerUrl("http://localhost:1337");
	        return extentx;
	    }

	    public KlovReporter createKlovReporter() {
	        KlovReporter klov = new KlovReporter();
	        klov.initMongoDbConnection("localhost", 27017);
	        klov.setKlovUrl("http://localhost:80");
	        klov.setProjectName("PlatClientTest");
	        klov.setReportName("buildName");
	        return klov;
	    }
	 
	*/
	 
	private void init(){
		
		File reportDir = new File(OUTPUT_FOLDER);
		if(!reportDir.exists()&& !reportDir.isDirectory()) {
			reportDir.mkdir();
		}
		
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(OUTPUT_FOLDER+FILE_NAME);
		htmlReporter.config().setDocumentTitle("ZJFAE AutoTest Platform");
		htmlReporter.config().setReportName("ZJFAE AutoTest Report");
		htmlReporter.config().setChartVisibilityOnOpen(true);
		htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
		//htmlReporter.config().setTheme(Theme.STANDARD);
		htmlReporter.config().setResourceCDN(ResourceCDN.EXTENTREPORTS);
		htmlReporter.config().setCSS(".node.level-1  ul{ display:none;} .node.level-1.active ul{display:block;}");
		htmlReporter.setAppendExisting(true);

		
	    extentReport = new ExtentReports();
	    extentReport.attachReporter(htmlReporter);
	    extentReport.setReportUsesManualConfiguration(true);
	   
	}
}
