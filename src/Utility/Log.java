package Utility;

import org.apache.log4j.Logger;

public class Log {


	// Initialize Log4j logs
	private static Logger Log = Logger.getLogger(Log.class.getName());//
	
	public static void startTestSuite(){
		Log.info("|=====================================================================|");
		Log.info("***	TestSuite  Starts ***");
		Log.info("");
	}
	
	public static void endTestSuite(){
		Log.info("***	TestSuite  Ends  ***");
		Log.info("|=====================================================================|");
		Log.info("");
		Log.info("");
	}

	public static void startTestCase(int sTestCaseNumber,String sTestCaseName, String sTestCaseDesc){
		Log.info("|************************************************************|");
		Log.info("|TestCase No.		: ["+sTestCaseNumber+"]");
		Log.info("|TestCase Name	: [ "+sTestCaseName+" ]");
		Log.info("|TestCase Desc 	: [ "+sTestCaseDesc+" ]");
		Log.info("|************************************************************|");
	}

	public static void endTestCase(String sTestCaseName){
		Log.info("TestCase : ["+sTestCaseName+ "] Ends.");
	}

	public static void startComponent(int startComponentNumber,String startComponent, String startComponentdesc){
		//Log.info("|--------------------------------------------------|");
		//Log.info("AppComponent No.	 	:  ["+startComponentNumber+"]");
		//Log.info("AppComponent Name 	:  [ "+startComponent+ " ]");
		Log.info("");
		Log.info("**Executing AppComponent :  ["+startComponentNumber+" - "+startComponent+"]");
		//Log.info("|AppComponent Desc 	:  [ "+startComponentdesc+" ]");
		//Log.info("|-----------------------------------------------|");
	} 

	public static void endComponent(String endComponent){ 
		Log.info("AppComponent Name :  [ "+endComponent+ " ] Ends.");
	}

	public static void info(String message) {
		Log.info(message);
	}

	public static void warn(String message) {
		Log.warn(message);
	}

	public static void error(String message) {
		Log.error(message);
	}

	public static void fatal(String message) {
		Log.fatal(message);
	}

	public static void debug(String message) {
		Log.debug(message);
	}

//	public static void info1(String message) {
//		// TODO Auto-generated method stub
//		
//	}

}