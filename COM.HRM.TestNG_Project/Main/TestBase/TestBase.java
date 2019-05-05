package TestBase;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.Parameters;

import DataReader.Read_Excel;
import DataReader.Write_Excel;

public class TestBase 
{
			 public static final Logger log= Logger.getLogger(TestBase.class.getName());
			    
			    public static  WebDriver driver;
				String browser= "chrome";//---------------This is for default browser
				//public ExtentReports extent;
				public WebDriverWait wait;
				public JavascriptExecutor js ;
				//public ExtentTest test;
				public Write_Excel excel;
				public static Connection connection;
			   	public static Properties config;
			   	public static Properties HRMConfig;
				public static Properties ORConfig;
			 	public PreparedStatement stmt1 = null;
				
				public void init() throws IOException, ClassNotFoundException, SQLException 
				{
					InitializePropertiesFiles();
					selectBrowser(browser);	
					getURL(HRMConfig.getProperty("HRM_URL"));
					//getURL("http://10.200.111.103:8001/ConsumerWebApp/welcome.do");
					String log4jConfig="log4j.properties";
					//PropertyConfigurator.configure(log4jConfig);//Need to check
					//initdb();
				}
				
				@Parameters("browser")//-------------------------------------for parallel runing
			    public void  selectBrowser(String browser) 
			    {
				 if(browser.equalsIgnoreCase("chrome")) 
				 {
					System.setProperty("webdriver.chrome.driver","D://SOFTWARE//Testing//Software//Automation_Jars//chromedriver.exe");
					ChromeOptions options = new ChromeOptions();
			        options.addArguments("disable-infobars");
			        options.addArguments("--start-maximized");
					log.info("Creating object of :-"+browser);			
					driver=new ChromeDriver(options);
					
				}
				 else if (browser.equalsIgnoreCase("firefox"))
				 {
					/*System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+"\\Driver\\.exe");*/
				    log.info("Creating object of :-"+browser);			
				    driver=new FirefoxDriver();		 
				 }
				 else if(browser.equalsIgnoreCase("IE")) 
				 {
					System.setProperty("webdriver.IE.driver",System.getProperty("user.dir")+"\\Driver\\IEDriverServer.exe");
					log.info("Creating object of :-"+browser);			
				    driver=new FirefoxDriver();		
				 }
			}
			    public void getURL(String url) 
			    {
				log.info("navigating to :-"+url);
				driver.manage().deleteAllCookies();
				driver.get(url);
				driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);		
			   }
			    /**
			     * @author pkgupta
			     * @param=ExcelName,SheetName,TestName
			     * Get ExcelData by implementing readExcel Class
			     */ 
			  public Object[][] getExcelData(String excelName,String SheetName,String TestName)
			  {
				  //String Excelpath="Excel_Data//"+excelName+".xlsx";
				  String Excelpath="D:\\HRM\\COM.HRM.TestNG_Project\\TestData\\DataSheet.xlsx";
				  Read_Excel Read=new Read_Excel();
				  Object[][] finddata=Read.getExcelDataBasedOnScenario(Excelpath, SheetName, TestName);
				  return finddata;
			  }
			  /**
			     * @author pkgupta
			     * @param=name
			     * Take ScreenShot
			     */ 
		      public void GetScreenshot(String name) 					
		      {
		    	  Calendar cal=Calendar.getInstance();
				  SimpleDateFormat format=new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
				  File srcfile= ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);			   
					   try 
					   {
						String reportDirectory=new File(System.getProperty("user.dir")).getAbsolutePath()+"\\Screenshot\\";
						File destfile=new File((String)reportDirectory+ name +"_"+format.format(cal.getTime()) + ".png");
						FileUtils.copyFile(srcfile, destfile);
						Reporter.log("<a href='" +destfile.getAbsolutePath()+ "'> <img src='" +destfile.getAbsolutePath()+"' height='100' width='100'/> </a>");
					} catch (IOException e) 
					{
						e.printStackTrace();
					} 	   
				} 
			//Update Excel Results
		     /* public void updateResult(String testCaseName,String testStatus,String ActRes,String ExpRes) throws IOException 
		      {
	              String OutputExcelpath=HRMConfig.getProperty("OutputExcel")+"OutPutSheet"+".xlsx";
	              excel=new Write_Excel();
	              excel.updateResult(OutputExcelpath, "ExecutionResult", testCaseName, testStatus, ActRes, ExpRes);
		      }*/
	      
		   	public static void InitializePropertiesFiles() throws IOException 
		   	{
		   		String HRM="/Config//HRM.properties";
		   		String OR="/Config//OR.properties";
		   		
		   		HRMConfig = new Properties();
		   		InputStream FI1 = TestBase.class.getResourceAsStream(HRM);	
		   		HRMConfig.load(FI1);   
		   		
		   		ORConfig = new Properties();
		   		InputStream FI2 = TestBase.class.getResourceAsStream(OR);	
		   		ORConfig.load(FI2);  
		   	}
		   	public void initdb() throws SQLException, ClassNotFoundException, IOException 	
		    {     
		   		InitializePropertiesFiles();
		        Class.forName("oracle.jdbc.driver.OracleDriver");
		        connection = DriverManager.getConnection(HRMConfig.getProperty("dbvalue"),
						 HRMConfig.getProperty("dbname"), 
						 HRMConfig.getProperty("dbpasswd"));
				System.out.println("DB Initialization is Success");
		    }
		   	/*enhance this function to handle multiple locators like xpath, css, name*/
		   	public WebElement getWebelement(String method) 
		   	{          		
		   		try
		   		{
		   		String[] methods = method.split("_");
		   		String strLocator = methods[methods.length - 1].toString();
		   		
		   		if (strLocator.contentEquals("xpath")) 
		   		{
		   			return driver.findElement(By.xpath(ORConfig.getProperty(method)));
		   		} 
		   		else if (strLocator.contentEquals("css")) 
		   		{
		   			return driver.findElement(By.cssSelector(ORConfig.getProperty(method)));

		   		} else if (strLocator.contentEquals("id")) {

		   			return driver.findElement(By.id(ORConfig.getProperty(method)));

		   		} else if (strLocator.contentEquals("name")) {

		   			return driver.findElement(By.name(ORConfig.getProperty(method)));

		   		} else if (strLocator.contentEquals("linkText")) {

		   			return driver.findElement(By.linkText(ORConfig.getProperty(method)));

		   		} else if (strLocator.contentEquals("partialLinkText")) {

		   			return driver.findElement(By.partialLinkText(ORConfig.getProperty(method)));

		   		} else if (strLocator.toUpperCase().equals("TAGNAME")) {

		   			return driver.findElement(By.tagName(ORConfig.getProperty(method)));

		   		} else if (strLocator.toUpperCase().equals("CLASSNAME")) {

		   			return driver.findElement(By.className(ORConfig.getProperty(method)));

		   		} else 
		   		{
		   			System.out.println("The specified locator not handled in framework");
		   			return null;
		   		}
		   	}
		   		catch(NoSuchElementException e)
		   	{
		   		
		   		System.out.println("Unable to find the element "+" " + method);
		   		return null;	
		   	}
		  }
		   	/**
		     * @author pkgupta
		     * @param=Webelement,time
		     * Wait For Element
		     */
		   	public void WaitForElement(int timeOutInSeconds,WebElement element) 
		   	{
		    	wait=new WebDriverWait(driver, timeOutInSeconds);
		    	wait.until(ExpectedConditions.visibilityOf(element));
		    }
		   	/**
		     * @author pkgupta
		     * ScrollPage
		     */
		   public void Pagescroller(int Xpixel,int Ypixel) 
		   {
			   js = (JavascriptExecutor) driver;
			   js.executeScript("scroll("+Xpixel+","+Ypixel+");");
		   }
		   /**
		    * @author pkgupta
		    * Scroll in to element View
		    */
		   public void ScrollIntoView(WebElement element) 
		   {
			   js = (JavascriptExecutor) driver;
			   js.executeScript("arguments[0].scrollIntoView(true);", element);
		   }
		   /**
		    * @author pkgupta
		    * JavaScript Executor Click
		    */
		   public void JseClick(WebElement element) 
		   {
			   js = (JavascriptExecutor)driver;
			   js.executeScript("arguments[0].click();", element);
		   }
		   /**
		    * @author pkgupta
		    * To Select the AngularDropDown
		    */
		   public void SelectAngularDropDown(String ElementToBeSelected) throws InterruptedException 
		   {		 
				   WebElement  wb=driver.findElement(By.xpath(".//input/following::ul[contains(@class,'dropdown-content select-dropdown')]//span[contains(text(),'"+ElementToBeSelected+"')]"));
				   WaitForElement(5, wb);
				   wb.click();	
			   }
		   public void selectaxisDDnew(String DDDetails) 
		   {
			   String[] splitdata=DDDetails.split("=");
		       String fieldheader=splitdata[0].trim();
		       String ElementToBeSelected=splitdata[1].trim();
		       WebElement DDclick=driver.findElement(By.xpath(".//label[starts-with(text(),'"+fieldheader+"')]/preceding-sibling::div/child::input[1]"));
		       WaitForElement(5, DDclick);
		       DDclick.click();
			   WebElement  liele=driver.findElement(By.xpath(".//input/following::ul[contains(@class,'dropdown-content select-dropdown')]//span[contains(text(),'"+ElementToBeSelected+"')]"));
			   WaitForElement(5, liele);
			   liele.click();
		   }
		   /**
		    * @author pkgupta
		    * @param--Exceldata
		    * To Enter Data from Excel in Bulk
		    */
		   public void EnterDatainBulk(String GivenDatas) throws Exception 
		   {   
				 if(GivenDatas.equals(null)||GivenDatas == "") 
				 {
					 throw new Exception("Data is not present in the cell.Please enter data.");	 
				 }
				 LinkedHashMap<String,String> InputData=new LinkedHashMap<String, String>();
				 
				 String[] TotalRowData=GivenDatas.split("\n{1}[\r]?");
				 
				 for(String IndividualFieldData : TotalRowData ) 
				 {
					 String[] datapair=IndividualFieldData.split("=");
					 String fieldHeader=datapair[0].trim();
					 String fieldData=datapair[1].trim();
					 
					 if(datapair.length==1) 
					 {
						 InputData.put(fieldHeader,"");
					 }
					 else 
					 {
					 InputData.put(fieldHeader,fieldData);
					 }
				 }
				 List<String> putdata=new ArrayList<String>(InputData.keySet());
						 
				 for(String individualheader:putdata) 
				 { 
					String strFieldvalue1=InputData.get(individualheader).trim();
					 boolean isFieldDisplayed=false;
					 String fieldxpath=".//label[contains(text(),'"+individualheader.trim()+"') or starts-with(text(),'"+individualheader.trim()+"')]/preceding-sibling::*[1]";
				     List<WebElement> WebEles=driver.findElements(By.xpath(fieldxpath));
				     for(int i=1;i<=WebEles.size();i++)
				     {
				    	 String modifiedFieldPath="("+fieldxpath+")["+i+"]";
				    	 isFieldDisplayed=driver.findElement(By.xpath(modifiedFieldPath)).isDisplayed();
				    	 
				    	 if(isFieldDisplayed) 
				    	 {
				    		 WebElement elefield=driver.findElement(By.xpath
				    				 (".//label[contains(text(),'"+individualheader.trim()+"') or starts-with(text(),'"+individualheader.trim()+"')]/preceding-sibling::*[1]")); 
				    		 String strTagename=elefield.getTagName();

				    		 System.out.println(strTagename);
				    	switch(strTagename) 
				    	{
				    	case "input" ://(Text Field)
				    		String textboxxpath=".//label[contains(text(),'"+individualheader.trim()+"') or starts-with(text(),'"+individualheader.trim()+"')]/preceding-sibling::input[1]";
				    		WebElement eleparam=driver.findElement(By.xpath(textboxxpath));
				    		WaitForElement(5, eleparam);
				    		ScrollIntoView(eleparam);
				    		eleparam.clear();
				    		eleparam.sendKeys(strFieldvalue1);
				    		Thread.sleep(2000);
				    		eleparam.sendKeys(Keys.TAB);
				    		Thread.sleep(2000);
				    		break;
						case "div" ://(DropDown)
							String DDxpath=".//label[contains(text(),'"+individualheader.trim()+"')or starts-with(text(),'"+individualheader.trim()+"')]/preceding-sibling::div[1]/input[1]";
				    		WebElement eleDD=driver.findElement(By.xpath(DDxpath));
				    		ScrollIntoView(eleDD);
				    		eleDD.click();
				    		Thread.sleep(5000);
				    		WebElement DDList=
				    	    driver.findElement(By.xpath("(.//label[contains(text(),'"+individualheader.trim()+"')or starts-with(text(),'"+individualheader.trim()+"')]/preceding-sibling::div[1]/ul[1]/li/span[contains(text(),'"+strFieldvalue1+"') or contains(text(),'"+strFieldvalue1+"')])[1]"));
				    		ScrollIntoView(eleDD);
				    		WaitForElement(5, DDList);
				    		JseClick(DDList);
				    	    break;
						case "textarea" ://(TextArea Field)
							String textAreaxpath=".//label[starts-with(text(),'"+individualheader.trim()+"') or contains(text(),'"+individualheader.trim()+"')]/preceding-sibling::textarea[1]";
							WebElement eleTXTArea=driver.findElement(By.xpath(textAreaxpath));
							ScrollIntoView(eleTXTArea);
				    		WaitForElement(5, eleTXTArea);
				    		eleTXTArea.clear();
				    		eleTXTArea.sendKeys(strFieldvalue1);
				    		Thread.sleep(2000);
				    		eleTXTArea.sendKeys(Keys.TAB);
				    		Thread.sleep(2000);
				    		break;	
				          }	
				    	break;
				        } 
				      }
				    }	   
			      }   
		   public void CheckCorrespondingCboxWRTLabelTABLE(String CorrespondingLebel) 
		   {
			   WebElement  wb=driver.findElement(By.xpath("(.//td[contains(text(),'"+CorrespondingLebel+"')])[1]/preceding-sibling::td/label"));
			   WaitForElement(5, wb);
			   wb.click();
		   }
}
