package Libraries;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Properties;

import org.w3c.dom.Document;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import Libraries.App_Components;
import Libraries.Report_Functions;
import Utility.Log;
import Utility.ReadExcel;

public class Driver_Script{
	public static Properties Param = null;
	public static Properties DBconfig = null;
	public static Properties Runtimevalue = null;
	public static Properties EnvironmentValue = null;
	protected static Document doc=null;
	public static String Filepath=null;
	public static int gblrecordsCounter;
	public static int gblAppComponentCounter;
	static Logger log = Logger.getLogger(Driver_Script.class.getName());
	public static void main(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException, NoSuchMethodException, SecurityException {
		FileInputStream fip=null;
		FileInputStream fipdb=null;
		FileInputStream fiprtv=null;
		FileInputStream fipEnv=null;
		PropertyConfigurator.configure("src\\Property\\log4j.properties");
		try{
			Log.startTestSuite();
			Log.info("Driver_Script main() Method");

			//Initialize Environment.properties file for Storing all the Driver Script Related Environment variables.
			EnvironmentValue = new Properties();
			fipEnv = new FileInputStream(System.getProperty("user.dir")+"//src//Property//Environment.properties");
			EnvironmentValue.load(fipEnv);

			//Initialize Param.properties file for storing all the General Properties for testRun.
			Param = new Properties();
			fip = new FileInputStream(System.getProperty("user.dir")+"//src//Property//Param.properties");
			Param.load(fip);

			//Initialize Runtime.properties file for Storing all the Temprory Runtime variable.
			Runtimevalue = new Properties();
			fiprtv = new FileInputStream(System.getProperty("user.dir")+"//src//Property//Runtime.properties");
			Runtimevalue.load(fiprtv);
			Log.info("All Properties file are loaded successfully.");	

			//Create an Instance for Driver Script
			Driver_Script testsuite = new Driver_Script();

			//Start the Driver Script.
			testsuite.start();

			Runtime.getRuntime().exec("taskkill /F /IM IEDriverServer.exe");
			
			//End the Driver Script - deallocate all memory.
			testsuite.end();

		}catch(Exception e){
			Log.info("Exception in Main method. Exp is : " + e.getMessage());
		}finally{
			if(fip!=null){
				fip.close();}
			fip=null;
			if(fipdb!=null){
				fipdb.close();}
			fipdb=null;
			if(fiprtv!=null){
				fiprtv.close();}
			fiprtv=null;
			if(fipEnv!=null){
				fipEnv.close();}
			fipEnv=null;
		}
	}

	//STARTING THE FRMEWORK
	public void start() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{

		String Test_Batch_Excel=null;
		String Test_Data_Excel=null;
		App_Components App_Comp=null;
		int noofTCs;
		String testCaseExecFlag=null;
		String TESTCASE_NAME=null;
		String testCaseDesc=null;
		int noOfAppComponents;
		String App_Component_Name=null;
		String appComponentDesc=null;
		String appComponentExecFlag=null;
		String strAppComponentName=null;
		int noOfRecords;
		String testDataExecFlag=null;
		String testDataObjective=null;
		Method callAppComponent=null;
		File testBatchFolder=null;
		File[] testBatchFiles=null;
		boolean testCaseSheetExist=false;

		try{
			Log.info("Start() of Driver Script starts.");

			//Create Instance for the App_Components class to invoke the App_Components in it
			App_Comp = new App_Components();

			//Retrieve the folder path of the Test Batch Spreadsheets
			testBatchFolder = new File(System.getProperty("user.dir")+Param.getProperty("testBatchFilePath"));

			//Retrieve all the TestBatch Spreadsheets from the TestBatch folder
			String regexpattern = "^Test_Batch_(\\d+)\\.xls";
			FileFilter filter = new RegexFileFilter(regexpattern);
			testBatchFiles = testBatchFolder.listFiles(filter);
			log.info("No. of TestBatch files : "+testBatchFiles.length);

			//Retrieve all the TestBatch Spreadsheets from the TestBatch folder
			testBatchFiles = testBatchFolder.listFiles();
			Log.info("No. of TestBatch files : "+testBatchFiles.length);
			
			//Function ArraySort to Sort the TestBatch file names in Numerical Order.
			Arrays.sort(testBatchFiles, new Comparator<File>() {
				@Override
				public int compare(File o1, File o2) {
					int n1 = extractNumber(o1.getName());
					int n2 = extractNumber(o2.getName());
					return n1 - n2;
				}
				
				private int extractNumber(String name) {
					int i = 0;
					try {
						int s = name.indexOf('_')+7;
						int e = name.lastIndexOf('.');
						String number = name.substring(s, e);
						i = Integer.parseInt(number);
					} catch(Exception e) {
						i = 0; 
					}
					return i;
				}
			}	);
			
			
			
			//Looping through all the Batch sheets in the TestBatch folder
			for (int testBatchPointer = 0; testBatchPointer < testBatchFiles.length; testBatchPointer++) {   

				Log.info("Executing the TestBatch Sheet : "+testBatchFiles[testBatchPointer].getName());
				EnvironmentValue.setProperty("CURRENT_TESTBATCH_FILE", testBatchFiles[testBatchPointer].getName());

				//Initialize doc to create XML nodes for the Current TestBatch Cases for Reporting.
				doc = Report_Functions.generateResultReport(Param.getProperty("testSuiteName"),System.getProperty("os.name"));

				//Get the path of Current Test Batch sheet
				Test_Batch_Excel = System.getProperty("user.dir")+Param.getProperty("testBatchFilePath")+testBatchFiles[testBatchPointer].getName();

				//Retrieving the No of Test Cases from the Batch Sheet
				noofTCs = ReadExcel.GetRowCount(Test_Batch_Excel, "Test_Case_List");
				Log.info("No of TestCase in the TestBatch sheet is : " +(noofTCs-1));

				//Looping through all the TestCases of Batch sheet
				for(int testCasePointer=1;testCasePointer<noofTCs;testCasePointer++){

					//Retrieving the Execution Flag of the test case
					testCaseExecFlag = ReadExcel.RetrieveExecutionFlagOfTestCase(Test_Batch_Excel, "Test_Case_List","Test_Case_Execute", testCasePointer);
					Log.info("");
					Log.info("TestCaseExecFlag of TestCase No. "+testCasePointer+"  is : "+ testCaseExecFlag);
					
					//If Test Case Execution Flag is Yes, the test case will be executed else will be skipped
					if(testCaseExecFlag.equalsIgnoreCase("Yes")){

						//Retrieve the Test Case Name 
						TESTCASE_NAME=ReadExcel.RetrieveTestCaseNameFromBatch(Test_Batch_Excel, "Test_Case_List", "Test_Case_Name", testCasePointer);
						EnvironmentValue.setProperty("TESTCASE_NAME", TESTCASE_NAME);

						//Retrieve the Test Case Description
						testCaseDesc=ReadExcel.RetrieveTestCaseDescFromBatch(Test_Batch_Excel, "Test_Case_List", "Test_Case_Description", testCasePointer);
						Log.startTestCase(testCasePointer,TESTCASE_NAME,testCaseDesc);

						//Add the Test Case node to test result
						Report_Functions.CreateTestCaseElement(doc, TESTCASE_NAME+": "+testCaseDesc);

						//Get the Test Data Excel Path
						Test_Data_Excel = (System.getProperty("user.dir")+Param.getProperty("testDataFilePath")+EnvironmentValue.getProperty("TESTCASE_NAME")+".xls");
						//Assign Test_Data_Excel to Static variable to use it in other Class
						Filepath=Test_Data_Excel;
						System.out.println(Filepath);
						//Check whether sheet in TestData Spreadsheet exist with the Testcase Name
						testCaseSheetExist=ReadExcel.CheckTestCaseNamedSheetExist(Filepath,TESTCASE_NAME);
						//If Sheet exist with TestCase name in TestData SpreadSheet
						if(testCaseSheetExist){

							//Retrieve the Number of Application Components in the test case	
							noOfAppComponents= ReadExcel.GetRowCount(Test_Data_Excel, EnvironmentValue.getProperty("TESTCASE_NAME"));
							Log.info("No of AppComponents in TestCase is : "+(noOfAppComponents -1));
							if (noOfAppComponents==0){
								Report_Functions.ReportEventFailure(doc, "TESTCASE_NAME", "Test Data/sheet Missing", false);

							}
							//Executing the Application Components in the test case by AppCompnents Looping
							for(int intAppComponentCounter=1;intAppComponentCounter<noOfAppComponents;intAppComponentCounter++){

								//Assign intAppComponentCounter to Static variable to use it in other classes
								gblAppComponentCounter=intAppComponentCounter;

								//Retrieve the Application Component Name 
								App_Component_Name=ReadExcel.RetrieveAppCompName(Test_Data_Excel, EnvironmentValue.getProperty("TESTCASE_NAME"), "Execution_Order", intAppComponentCounter);
								EnvironmentValue.setProperty("App_Component_Name", App_Component_Name);

								//Retrieve the Application Component Description
								appComponentDesc=ReadExcel.RetrieveTestCaseDescFromBatch(Test_Data_Excel, EnvironmentValue.getProperty("TESTCASE_NAME"), "App_Component_Description", intAppComponentCounter);

								//Retrieve the Execution Flag of the Application Component		
								appComponentExecFlag=ReadExcel.RetrieveAppCompExecFlag(Test_Data_Excel, EnvironmentValue.getProperty("TESTCASE_NAME"), "App_Component_Execute", intAppComponentCounter);
								Log.info("AppComponent Exec Flag of ["+App_Component_Name+"] is : "+ appComponentExecFlag);

								//Execute the Application Component (Skip if Execute status of App Component is No)
								if(!(appComponentExecFlag.equalsIgnoreCase("No"))){

									Log.startComponent(intAppComponentCounter,App_Component_Name, appComponentDesc);

									//Add the App Component node in the test result file	
									Report_Functions.CreateAppComponentElement(doc,EnvironmentValue.getProperty("App_Component_Name")+": "+ appComponentDesc);

									//' For naming the error screenshots
									EnvironmentValue.setProperty("Glbl_Error_ScreenShotName", App_Component_Name) ;

									//Initializing the flag App_Component_Status_Flag
									EnvironmentValue.setProperty("App_Component_Status_Flag", "1") ;

									//Retrieve the Application Component Name 
									strAppComponentName = EnvironmentValue.getProperty("App_Component_Name");

									//Retrieving the total records in the App Component			
									int noOfRecordsinAC=ReadExcel.GetRowCount(Test_Data_Excel, strAppComponentName);
									Log.info("No of Records in AppComponent is : "+(noOfRecordsinAC -1));

									//Reduce the count by -1 to leave the header in Excel uncounted in Report
									noOfRecords=noOfRecordsinAC-1;

									if(noOfRecordsinAC >2){
										Report_Functions.ReportEventSuccess(doc, "4", "componentCheck", "Total Number of Records :  "+noOfRecords, 2);
									}

									//Loop to handle Multiple Test Data in the App Component
									for(int recordsCounter=1;recordsCounter<noOfRecordsinAC;recordsCounter++){

										//Assign recordsCounter to Static variable to use it other classes.
										gblrecordsCounter=recordsCounter;

										//Retrieve the Exec Flag of the Test Data				
										testDataExecFlag=ReadExcel.RetrieveTestDataExecFlag(Test_Data_Excel, strAppComponentName, "TestData_Execute", recordsCounter);
										testDataObjective=ReadExcel.RetrieveTestDataFromSheet(Test_Data_Excel, strAppComponentName, "Objective", recordsCounter);
										Log.info("TestData Execution Flag of  ["+strAppComponentName+"]'s Dataset line "+recordsCounter+" is : "+ testDataExecFlag);

										Log.info("Test data objective of  ["+strAppComponentName+"]'s Dataset line : "+recordsCounter+" is : "+ testDataObjective);
										//Execute the test data, Skip it if NO	
										if(testDataExecFlag.equalsIgnoreCase("Yes")){
											Report_Functions.ReportEventSuccess(doc, "4", "Test Data Objective", "Test Data Objective : "+testDataObjective, 2);

											if(noOfRecordsinAC > 2){
												Report_Functions.ReportEventSuccess(doc, "4", "Processing Record", "Processing Record# : "+(recordsCounter), 2);
											}//If condition for noOfRecordsinAC

											Log.info("Calling the the App_Component : "+ strAppComponentName+"() from App_Component Class");

											//Using Reflection API, Call the corresponding App_Component function is App_Component Class.
											try{
												
												callAppComponent = App_Comp.getClass().getMethod(strAppComponentName);
												callAppComponent.invoke(App_Comp);
											
											}catch(NoSuchMethodException e){
												
												Report_Functions.ReportEventFailure(doc, "App_componenet", "App component is not present-"+e.getMessage(), false);
											}
											catch(Exception e){
												Report_Functions.ReportEventFailure(doc, "App_componenet", "Generic Exception caught.Verify Method implementation"+e.getMessage(), false);

											}

										}//testDataExecFlag to Yes in DataSet of Appcomponent check condition ends

									}//For loop for record counter when multiple dataset exist ends

									Log.endComponent(strAppComponentName);
								}//appComponentExecFlag to Yes check ends

							}//AppComp Pointer of a TestCase ends

							//Completed Execution of the Test Case updating the Test Results
							Report_Functions.ReportEventSuccess(doc, "4", "End of Test Case", "End of Test Case :  "+EnvironmentValue.getProperty("TESTCASE_NAME"), 2);

							Log.endTestCase(EnvironmentValue.getProperty("TESTCASE_NAME"));

						}
						//If Sheet does not exist with TestCase name in TestData SpreadSheet
						else{
							Log.info("Name Mismatch. TestData Excel - "+EnvironmentValue.getProperty("TESTCASE_NAME")+" does not contain sheet named : "+EnvironmentValue.getProperty("TESTCASE_NAME"));

							//Creating AppComp with testCase Name to Report the Name Mismatch.
							Report_Functions.CreateAppComponentElement(doc,EnvironmentValue.getProperty("TESTCASE_NAME"));

							//Creating failure event to Report the Name Mismatch.
							Report_Functions.ReportEventFailure(doc, "Read Sheet", "TestData Excel '"+EnvironmentValue.getProperty("TESTCASE_NAME")+"'.xls does not contain the sheet named : "+EnvironmentValue.getProperty("TESTCASE_NAME")+". Name Mismatch.", false);
						}

					} //testCaseExecFlag to Yes condition check ends
					else if(testCaseExecFlag.equalsIgnoreCase("No")){
						//Log.info("TestCase No. "+testCasePointer+"  is not Executed");
					}else {
						//if either Yes Nor No is provided in Test_Case_Execute, exit the for loop 	
						break;
					}

				}//TestCase Pointer ends

				//Saving the Report XML file
				//Report_Functions.SaveTestReport(doc,Param.getProperty("ReportfileName")+"_"+testBatchFiles[testBatchPointer].getName().replaceFirst("[.][^.]+$", ""));
				Report_Functions.SaveTestReport(doc);
				Thread.sleep(5000);
			//	Report_Functions.OpenResultFile();
				//Reseting the doc to Create a New Report File for the Next TestBatch Execution 
				doc=null;

			}//TestBatch file counter
		}catch(Exception e){
			Log.info("Exception is start() of Main Method. Exp is : "+ e);
		}finally{
			Test_Batch_Excel=null;
			Test_Data_Excel=null;
			App_Comp=null;
			testCaseExecFlag=null;
			TESTCASE_NAME=null;
			testCaseDesc=null;
			App_Component_Name=null;
			appComponentDesc=null;
			appComponentExecFlag=null;
			strAppComponentName=null;
			testDataExecFlag=null;
			callAppComponent=null;
			testBatchFolder=null;
			if(testBatchFiles!=null){
				for(int i=0;i<testBatchFiles.length;i++){
					testBatchFiles[i]=null;
				}
				testBatchFiles=null;
			}
			Filepath=null;
			DBconfig = null;
			doc=null;
		}
	}

	public void end() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{

		FileOutputStream fileOut=null;
		FileOutputStream fileOutEnv=null;
		try{
			Log.info("end() of Driver Script starts.");

			//Clearing the RuntimeFile and Saving it
			fileOut = new FileOutputStream(System.getProperty("user.dir")+"//src//Property//Runtime.properties");
			Runtimevalue.clear();
			Runtimevalue.store(fileOut, "Checking the propfile");
			fileOut.close();
			
			//Clearing the Environment file and Saving it
			fileOutEnv = new FileOutputStream(System.getProperty("user.dir")+"//src//Property//Environment.properties");
			EnvironmentValue.clear();
			EnvironmentValue.store(fileOutEnv, "Checking the Env");
			fileOutEnv.close();

			//Saving the Report XML file
			//Report_Functions.SaveTestReport(doc,Param.getProperty("ReportfileName"));

			//Explicit call for Garbage Collection
			if(Param.getProperty("GC_RUN_EXPLICIT").equalsIgnoreCase("TRUE")){
				System.gc();
			}

		}catch(Exception e){
			Log.info("Exception in end() of Main method. Exp is : "+ e);
		}finally{
			if(fileOut!=null){
				fileOut=null;
			}
			if(fileOutEnv!=null){
				fileOutEnv=null;
			}
			Param = null;
			Runtimevalue = null;
			EnvironmentValue = null;

			Log.endTestSuite();
			System.exit(1);
		}
	}
}