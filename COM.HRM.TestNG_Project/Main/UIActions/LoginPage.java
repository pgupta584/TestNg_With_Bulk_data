package UIActions;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import TestBase.TestBase;

public class LoginPage extends TestBase
{
	public static String ActualMSG;
	   public static final Logger log= Logger.getLogger(LoginPage.class.getName());
	   public LoginPage(WebDriver driver) 
	   {
		  TestBase.driver=driver; 
	   }
	   public void LoginToapp(String username,String pass) throws InterruptedException, SQLException
	   {
		    getWebelement("Link_SignIn_xpath").click();
			WebElement user_Mob=getWebelement("input_MobileNo_xpath");
			user_Mob.clear();
			user_Mob.sendKeys(username);
			log.info("Entered UserName and the object is"+user_Mob.toString());
			WebElement password=getWebelement("input_PIN_xpath");
			password.clear();
			password.sendKeys(pass);	
			log.info("Entered Password and the object is"+password.toString());
			/*WebElement loginbutton=getWebelement("btn_loginbutton_id");
			//WaitForElement(5, loginbutton);
		    loginbutton.click();		
			log.info("Clicked on login and the object is"+loginbutton.toString());*/
			driver.findElement(By.xpath(".//*[@id='login_button']")).click();
			Thread.sleep(3000);		
	        }
		public void failurelogin(String username,String pass,String ExpFailuremsg) throws Exception 
		{		
			 try 
			 {
					LoginPage login=new LoginPage(driver);
					login.LoginToapp(username, pass);
					
					WebElement failurelogin=getWebelement("LBL_loginFailure_MSG_xpath");
					if(failurelogin.getText().equals(ExpFailuremsg)) 
					 {
						log.info(" msg is :-"+failurelogin.getText());				
					 }
				    GetScreenshot(ExpFailuremsg);
				} 
			 catch (InterruptedException e) 
			 {
					e.printStackTrace();
					throw new Exception("Exception Occured");
			 }
		}
		public  void successLogin(String username,String pass,String ExpSuccessmsg) throws Exception 
		{	
		    try 
		    {
				LoginPage login=new LoginPage(driver);
				login.LoginToapp(username, pass);
				
				WebElement successlogin=getWebelement("LBL_SuccessLogin_xpath");
				if(successlogin.getText().equals(ExpSuccessmsg)) 
				 {
					log.info(" msg is :-"+successlogin.getText());	
				 }
			    GetScreenshot(ExpSuccessmsg);
			    getWebelement("link_logout_linkText").click();
	            log.info("Application logged out successfully");	
			} 
		    catch (Exception e) 
		      {
				e.printStackTrace();
				getWebelement("link_logout_xpath").click();
			   }	       
	        }
		
		
		public  void MultipleLoginAttempt(String username,String pass,String ExpLockfailuremsg) throws Exception
		{	
			for(int i=1;i<=5;i++) 
			{
             log.info("Trying--"+i);
             LoginToapp(username, pass);     
            } 
				String actualMsg=getWebelement("LBL_loginFailure_MSG_xpath").getText();
             if(actualMsg.contains(ExpLockfailuremsg)) 
             {
                   log.info("Account locked after 3 login attempt");
             }      
             else 
             {                       
             throw new ElementNotVisibleException("UI is not displaying the correct error msg. Expected is"+ExpLockfailuremsg+"But Actual is " +actualMsg);               
             }
			}
		public void forgotPassword(String MobileNo,String FPINDOB,String FPINMDOB,String Frgotconfirmmsg) throws SQLException 
		{
			try {
				WebElement loginpage=getWebelement("link_SignIn_linkText");
				loginpage.click();
				
				WebElement forgotpasswordlnk=getWebelement("link_ForgotPassword_linkText");
				forgotpasswordlnk.click();
				log.info("Cicked on forgot password and the object is"+forgotpasswordlnk.toString());
				
				WebElement MobileNo1=getWebelement("input_forgotPasswordMob_xpath");
				MobileNo1.sendKeys(MobileNo);
				log.info("User ID entered and the object is"+MobileNo1.toString());
				
				WebElement FPDOB=getWebelement("input_forgotPasswordDBO_xpath");
				FPDOB.sendKeys(FPINDOB);
				log.info("Email entered and the object is"+FPINDOB.toString());
				
				WebElement MFPMDOB=getWebelement("input_forgotPasswordMDOB_xpath");
				MFPMDOB.sendKeys(FPINMDOB);
				log.info("Email entered and the object is"+FPINMDOB.toString());
				
				WebElement NewPin=getWebelement("input_forgotPasswordNewPIN_xpath");
				NewPin.sendKeys("1111");
				
				WebElement CnfrmNewPin=getWebelement("input_forgotPasswordCnfmPin_xpath");
				CnfrmNewPin.sendKeys("1111");
				
				WebElement SubmitBTN=getWebelement("btn_ForgotPasswordsubmit_xpath");
				SubmitBTN.click();
				log.info("Clicked on Submit and the object is"+SubmitBTN.toString());
					
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				log.info("Exception Occured");
				getWebelement("link_logout_xpath").click();
			}       
		}
	    public void forgotPasswordvalid(String MobileNo,String FPINDOB,String FPINMDOB,String Frgotconfirmmsg) throws Exception 
		{			
	    	LoginPage fa=new LoginPage(driver);
	    	fa.forgotPassword(MobileNo, FPINDOB, FPINMDOB,Frgotconfirmmsg);
			String forgotpswd=getWebelement("LBL_forgotpswd_MSG_xpath").getText();
				ActualMSG=forgotpswd;
				if(ActualMSG.contains(Frgotconfirmmsg)) 
				{
					log.info(" msg is :-"+ActualMSG);	
					System.out.println("PASS");
				 }
			  GetScreenshot(Frgotconfirmmsg);
		}
	    public void forgotPasswordInvalid(String MobileNo,String FPINDOB,String FPINMDOB,String Frgotconfirmmsg) throws Exception 
		{			
	    	LoginPage fa=new LoginPage(driver);
	    	fa.forgotPassword(MobileNo, FPINDOB, FPINMDOB,Frgotconfirmmsg);
			String forgotpswd=getWebelement("LBL_forgotpswd_MSG_xpath").getText();
				ActualMSG=forgotpswd;
				if(ActualMSG.contains(Frgotconfirmmsg)) 
				{
					log.info(" msg is :-"+ActualMSG);	
					System.out.println("PASS");
				 }
			  GetScreenshot(Frgotconfirmmsg);
		}
}
