package LoginTest;

import java.io.IOException;
import java.sql.SQLException;
import org.apache.log4j.Logger;
import org.testng.SkipException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import TestBase.TestBase;
import UIActions.LoginPage;

public class LoginTest extends TestBase
{
	LoginPage loginpage;
	int count=0;
	
    public static final Logger log= Logger.getLogger(LoginPage.class.getName());
    
     @DataProvider(name="logintest")  
     public Object[][] testData()
     { 
    	 Object[][] data= getExcelData("DataSheet", "LoginData", "loginTest");
		 return data;
     }
	@BeforeTest
	public void setup() throws IOException, ClassNotFoundException, SQLException 
	{		
		init();		
		//initdb();
	}
	@AfterTest
	public void EndTest() 
	{
	   driver.quit();
	}	
    @Test(priority=1,enabled=true,dataProvider="logintest")
    public void loginToApplication(String Scenario, String RunMode,String Username,String Password,String ExpectedMSG) throws InterruptedException, Exception 
    {	
    	String ActualMSG = null;
    	if(RunMode.equalsIgnoreCase("N")) 
    	{
    		throw new SkipException("User Marked this data as No Run");
    	}
    	else if(RunMode.equalsIgnoreCase("Y")) 
    	{
    		try 
    		{
				loginpage =new LoginPage(driver);
    			switch(Scenario) 
    			{
    			case"InvalidLogin" :
        		log.info(Scenario+"::");
    			loginpage.failurelogin(Username, Password, ExpectedMSG);	
    			break;
    			case"ValidLogin" :
        		log.info(Scenario+"::");
			    loginpage.successLogin(Username, Password, ExpectedMSG);
			    break;
    			case "3 login attempt" : 
                loginpage.MultipleLoginAttempt(Username, Password, ExpectedMSG);
                break;
             
    			}
    			ActualMSG=LoginPage.ActualMSG;
    			
    			//updateResult(Scenario, "Pass", ActualMSG, ExpectedMSG);
            }
    		catch (Exception e) 
    		{
    			e.printStackTrace();
                ActualMSG=LoginPage.ActualMSG;
                //updateResult(Scenario, "Fail", ActualMSG, ExpectedMSG);
                throw new Exception("exception");
			}
    	}
    }
}
