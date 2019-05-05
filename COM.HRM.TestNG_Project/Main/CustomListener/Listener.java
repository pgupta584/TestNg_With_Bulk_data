package CustomListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import TestBase.TestBase;

public class Listener extends TestBase implements ITestListener
{
		public void onTestStart(ITestResult result) 
		{	
			Reporter.log("test started running:" + result.getMethod().getMethodName() + "at:" +result.getStartMillis());	
		}
		public void onTestSuccess(ITestResult result) 
		{	
			
		}
		public void onTestFailure(ITestResult result) 
		{
			   Calendar cal=Calendar.getInstance();
			   SimpleDateFormat format=new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
			   
			   String methodname=result.getName();
			   if(!result.isSuccess()) 
			   {	   
			   File srcfile= ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);			   
				   try 
				   {
					String reportDirectory=new File(System.getProperty("user.dir")).getAbsolutePath()+"/src/main/java/DMS/AutomationDMS/FailedScreenshots/";
					File destfile=new File((String)reportDirectory+ methodname +"_"+format.format(cal.getTime()) + ".png");
					FileUtils.copyFile(srcfile, destfile);
					Reporter.log("<a href='" +destfile.getAbsolutePath()+ "'> <img src='" +destfile.getAbsolutePath()+"' height='100' width='100'/> </a>");
					//logout once failed
					WebElement Logout=getWebelement("link_logout_xpath");
					JseClick(Logout);
				   } 
				   catch (IOException e) 
				   {
					e.printStackTrace();
				   } 	
			   }
		}
		public void onTestSkipped(ITestResult result) 
		{
			// TODO Auto-generated method stub	
		}
		public void onTestFailedButWithinSuccessPercentage(ITestResult result) 
		{
			// TODO Auto-generated method stub	
		}
		public void onStart(ITestContext context)
		{
			// TODO Auto-generated method stub	
		}
		public void onFinish(ITestContext context) 
		{
			// TODO Auto-generated method stub	
		}
	}
