package Libraries;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
/*import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;*/

import Utility.Log;
import Libraries.Driver_Script;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;


public class Report_Functions extends Driver_Script{

	static String strPassFail = null;
	static String iFlagFailChk =null;


	/* ***** Functions available in Report Functions: *****
	public static Document generateResultReport(String strTestSuiteName,String strOS)
	public static Document CreateTestCaseElement(Document doc,String strTestCaseName)
	public static Document CreateAppComponentElement(Document doc, String strAppComponentName)
	public static Document ReportEventSuccess(Document doc,String eventStatus,String strFnName, String strDetails,int intReportType) 
	public static Document ReportEventFailure(Document doc,String strFnName, String strErrorDetails,boolean blnCaptureScreenShot)
	public static Document CaptureErrorScreenshot(Document doc, String fileName)
	public static Document CaptureErrorScreenshot(Document doc, String fileName)
	public static Document CreateEventStatusElement(Document doc,String eventStatus, String eventDetails)
	public static Document StatusTimestampUpdate(Document doc,String status)
	public static void SaveTestReport(Document doc,String XMLfilename) 
	 */

	/*
	Function	: getDateTime
	Type		: Static String
	Author		: Lakshman A
	Date		: 02-NOV-15
	Objective  	: Creates the datetime in the report needed format
	Parameters 	: None
	 */
	public static String getDateTime() throws DateTimeException{
		Date dNow=null;
		SimpleDateFormat ft=null;
		String datet=null;

		try{
			//gets the current date
			dNow = new Date( );

			//Formatting the Time to Custom Report time
			ft = new SimpleDateFormat ("MM/dd/yyyy hh:mm:ss a");
			datet=ft.format(dNow);
			//System.out.println("Current Date in string is: " + datet);
			//return datet;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			dNow=null;
			ft=null;
		}
		return datet;
	}

	/*
	Function	: generateResultReport
	Type		: Static Document
	Author		: Lakshman A
	Date		: 02-NOV-15
	Objective  	: Create the Result document and the nodes - Test Report and Test Suite with Stylesheet mentioned
	Parameters 	: Test Suite Name, Operating System
	 */
	public static Document generateResultReport(String strTestSuiteName,String strOS) throws Exception{

		DocumentBuilderFactory dbFactory=null;
		DocumentBuilder dBuilder=null;
		Document doc=null;
		Element rootElement=null;
		ProcessingInstruction stylesheet=null;
		Element TestSuite=null;

		try{	
			//obtain a parser that produces DOM object trees from XML documents.
			dbFactory = DocumentBuilderFactory.newInstance();
			//DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();

			//The Document interface represents the entire HTML or XML document. 
			//Conceptually, it is the root of the document tree, and provides the primary access to the document's data. 
			doc = dBuilder.newDocument();

			//Creating the Root Element - TestReport
			rootElement = doc.createElement("TestReport");
			doc.appendChild(rootElement);

			//Stylesheet is descibed using the processor-specific information in the text of the document. 
			stylesheet = doc.createProcessingInstruction("xml-stylesheet", "href=\'Stylesheet.xsl\' type=\'text/xsl\'");
			doc.insertBefore(stylesheet, rootElement);

			//Creating the TestSuite Element
			TestSuite = doc.createElement("TestSuite");

			//Setting Attributes to TestSuite Element
			TestSuite.setAttribute("ExecutionStarted",getDateTime());
			TestSuite.setAttribute("Description", strTestSuiteName);
			TestSuite.setAttribute("OperatingSystem",strOS);
			TestSuite.setAttribute("ExecutionEnded",getDateTime());

			//Setting the TestSuite Text Elements
			TestSuite.appendChild(doc.createTextNode(strTestSuiteName));

			//Finally Appending all the child nodes to TestReport
			rootElement.appendChild(TestSuite);

			//Save Report at Test RunTime
			SaveTestReportRuntime(doc);

			//return doc;	
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			dbFactory=null;
			dBuilder=null;
			rootElement=null;
			stylesheet=null;
			TestSuite=null;
		}

		return doc;		
	}

	/*
	Function	: CreateTestCaseElement
	Type		: Static Document
	Author		: Lakshman A
	Date		: 02-NOV-15
	Objective  	: Creates the Testcase Elements and adds the child elements to the test suite element
	Parameters 	: Document, Test case name and description
	 */
	public static Document CreateTestCaseElement(Document doc,String strTestCaseName) throws Exception{

		Element TestCase=null;
		NodeList TestSuiteNode=null;
		Element TestSuiteElement=null;

		try{
			//Creating the TestCase Element
			TestCase = doc.createElement("TestCase");

			//Setting Attributes to TestCase Element
			TestCase.setAttribute("ExecutionStarted",getDateTime());
			TestCase.setAttribute("Description", strTestCaseName);
			TestCase.setAttribute("ExecutionEnded",getDateTime());
			TestCase.setAttribute("Status","1");

			//Setting the TestCase Text Elements
			TestCase.appendChild(doc.createTextNode(strTestCaseName));

			//Process to get the latest appropriate TestSuite Element

			//Gets all the tag name in the available XML with name like TestSuite
			TestSuiteNode = doc.getElementsByTagName("TestSuite");
			//Gets the latest appropriate TestSuite Element
			TestSuiteElement =(Element) TestSuiteNode.item(TestSuiteNode.getLength()-1);

			//Finally Appending all the child  nodes to TestSuite
			TestSuiteElement.appendChild(TestCase);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			TestCase=null;
			TestSuiteNode=null;
			TestSuiteElement=null;
		}
		return doc;
	}

	/*
	Function	: CreateAppComponentElement
	Type		: Static Document
	Author		: Lakshman A
	Date		: 02-NOV-15
	Objective  	: Creates the AppComponent Element and adds the child elemnts to the Testcase element
	Parameters 	: Document, App Component Name
	 */
	public static Document CreateAppComponentElement(Document doc, String strAppComponentName) throws Exception{

		Element Appcomponent=null;
		NodeList TestCaseNode=null;
		Element TestCaseElement=null;

		try{

			//Creating the Appcomponent Element
			Appcomponent = doc.createElement("AppComponent");

			//Setting Attributes to Appcomponent Element
			Appcomponent.setAttribute("ExecutionStarted",getDateTime());
			Appcomponent.setAttribute("Description", strAppComponentName);
			Appcomponent.setAttribute("ExecutionEnded",getDateTime());

			//Setting the Appcomponent Text Elements
			Appcomponent.appendChild(doc.createTextNode(strAppComponentName));

			//Process to get the latest appropriate TestCase Element

			//Gets all the tag name in the available XML with name like TestCase
			TestCaseNode = doc.getElementsByTagName("TestCase");
			//Gets the latest appropriate TestCase Element
			TestCaseElement =(Element) TestCaseNode.item(TestCaseNode.getLength()-1);

			//Finally Appending all the child  nodes to TestCase
			TestCaseElement.appendChild(Appcomponent);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			Appcomponent=null;
			TestCaseNode=null;
			TestCaseElement=null;
		}
		return doc;
	}

	/*
	Function	: ReportEventSuccess
	Type		: Static Document
	Author		: Lakshman A
	Date		: 02-NOV-15
	Objective  	: Creates the AppComponent Element and adds the child elemnts to the Testcase element
	Parameters 	: Document, eventStatus - Event Status , 1-Pass,4-Information
				  strFnName - The name of the calling function
				  strDetails - description
				  intReportType - which reporting needs to be done. 1 - QTP only, 2 - QTP & XML, 3 - XML only
	 */
	public static Document ReportEventSuccess(Document doc,String eventStatus,String strFnName, String strDetails,int intReportType) throws Exception{
		CreateEventStatusElement(doc,eventStatus,strDetails);
		return doc;
	}

	/*
	Function	: ReportEventFailure
	Type		: Static Document
	Author		: Lakshman A
	Date		: 02-NOV-15
	Objective  	: Function to report Error Event in the Custom Report
	Parameters 	: Document, strFnName - The name of the calling function 
				  strErrorDetails - Error Details
				  blnCaptureScreenShot - True-Capture Error screenshot
	 */
	public static Document ReportEventFailure(Document doc,String strFnName, String strErrorDetails,boolean blnCaptureScreenShot) throws Exception{

		//Creates the Event Element by calling CreateEventStatusElement
		CreateEventStatusElement(doc,"3",strErrorDetails);

		if(blnCaptureScreenShot){

			//Global Variable Glbl_Error_ScreenShotName contains the AppComponent Name
			//Below line need to be synched with Driver script
			//String Glbl_Error_ScreenShotName = EnvironmentValue.getProperty("App_Component_Name");
			String Glbl_Error_ScreenShotName = "test";

			//Screenshot capturing by calling the  CaptureErrorScreenshot
			CaptureErrorScreenshot(doc,Glbl_Error_ScreenShotName);

		}

		return doc;
	}

	/*
	Function	: CaptureErrorScreenshot
	Type		: Static Document
	Author		: Lakshman A
	Date		: 02-NOV-15
	Objective  	: Function to capture the Error Screenshot during run time
	Parameters 	: Document, Error ScreenShot Name
	 */
	public static Document CaptureErrorScreenshot(Document doc, String fileName) throws Exception {

		Dimension screenSize=null;
		Rectangle screenRectangle=null;
		Robot robot=null;
		BufferedImage image=null;
		Date SSNow=null;
		SimpleDateFormat SSft=null;
		String SSdatetime=null;
		String newstrScreenShotName=null;
		String strFoldername=null;
		File filemake=null;
		String strScreenShotPath=null;
		File file=null;
		FileOutputStream fos=null;
		try{

			//The Dimension class encapsulates the width and height of a component (in integer precision) in a single object
			//getScreenSize - Gets the size of the screen
			screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
			screenRectangle = new Rectangle(screenSize);

			//Robot used to generate native system input events for the purposes of test automation
			robot = new Robot();

			//The BufferedImage subclass describes an Image with an accessible buffer of image data
			//createScreenCapture - Creates an image containing pixels read from the screen. This image does not include the mouse cursor.
			image = robot.createScreenCapture(screenRectangle);

			//Getting the DateTime in the needed format to append it with SS filename 
			SSNow = new Date( );
			SSft = new SimpleDateFormat ("dd_MM_yyyy_hh_mm_ss_a");
			SSdatetime=SSft.format(SSNow);

			//ScreenShot name:
			newstrScreenShotName = fileName+"_"+SSdatetime+".png";
			strFoldername = "_errorScreenshots" ;

			//Create a directory to place the screenshots 
			//File filemake = new File(System.getProperty("user.dir")+"\\src\\Screenshots\\"+strFoldername+"\\");
			filemake = new File(System.getProperty("user.dir")+"\\Results\\"+strFoldername+"\\");

			if (!filemake.exists()) {
				filemake.mkdir() ;
			}

			//Getting the Path of Scrrenshot with filename
			strScreenShotPath=System.getProperty("user.dir")+"\\Results\\"+strFoldername+"\\"+newstrScreenShotName;

			//Creating the Screenshot in png format
			file = new File(strScreenShotPath);
			fos = new FileOutputStream(file);
			ImageIO.write(image, "png", fos);

			String strScreenShotPath_1 = "..\\Results\\"+strFoldername+"\\"+newstrScreenShotName;
			//Adding the Screenshot path into the Custom Report as Event Node
			addScreenShotLinkInReport(doc,strScreenShotPath_1);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			screenSize=null;
			screenRectangle=null;
			robot=null;
			image=null;
			SSNow=null;
			SSft=null;
			SSdatetime=null;
			newstrScreenShotName=null;
			strFoldername=null;
			filemake=null;
			strScreenShotPath=null;
			file=null;
			fos=null;
		}
		return doc;

	}

	/*
	Function	: addScreenShotLinkInReport
	Type		: Static Document
	Author		: Lakshman A
	Date		: 02-NOV-15
	Objective  	: Captures the link to the screenshot in custom report
	Parameters 	: Document, strImagePath - Path of the image 
	 */
	public static Document addScreenShotLinkInReport(Document doc,String strImagePath) throws Exception{

		Element Event=null;
		NodeList AppComponentNode=null;
		Element AppComponentElement=null;
		try{

			//Creating the Event Element
			Event = doc.createElement("Event");

			//Setting Attributes to Event Element
			Event.setAttribute("ErrorScreenShotPath", strImagePath);

			//Process to get the latest appropriate AppComponent Element

			//Gets all the tag name in the available XML with name like AppComponent
			AppComponentNode = doc.getElementsByTagName("AppComponent");
			//Gets the latest appropriate AppComponent Element
			AppComponentElement =(Element) AppComponentNode.item(AppComponentNode.getLength()-1);

			//Finally Appending all the child  nodes to TestCase
			AppComponentElement.appendChild(Event);

			//Save Report at Test RunTime
			SaveTestReportRuntime(doc);

		}catch(Exception e){
			e.printStackTrace();
		}finally{

		}
		return doc;
	}

	/*
	Function	: CreateEventStatusElement
	Type		: Static Document
	Author		: Lakshman A
	Date		: 02-NOV-15
	Objective  	: Creates the Event Status Element and adds the child elemnts to the AppComponent element
	Parameters 	: eventStatus, eventDetails
				  eventStatus : 1-Pass,2-warning,3-fail,4-Information
	 */
	public static Document CreateEventStatusElement(Document doc,String eventStatus, String eventDetails) throws Exception{

		Element Event=null;
		NodeList AppComponentNode=null;
		Element AppComponentElement=null;
		try{

			//Creating the Event Element
			Event = doc.createElement("Event");

			//Setting Attributes to Event Element
			Event.setAttribute("Status", eventStatus);

			//Setting the Event Text Elements
			Event.appendChild(doc.createTextNode(eventDetails));

			//Process to get the latest appropriate TestCase Element

			//Gets all the tag name in the available XML with name like TestCase
			AppComponentNode = doc.getElementsByTagName("AppComponent");
			//Gets the latest appropriate TestCase Element
			AppComponentElement =(Element) AppComponentNode.item(AppComponentNode.getLength()-1);

			//Finally Appending all the child  nodes to TestCase
			AppComponentElement.appendChild(Event);

			//Calling the StatusTimestampUpdate to update the current time as Endtime and marking the TestCase as fail if any AC or Event is failed
			StatusTimestampUpdate(doc,eventStatus);

			//Save Report at Test RunTime
			SaveTestReportRuntime(doc);

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			Event=null;
			AppComponentNode=null;
			AppComponentElement=null;
		}
		return doc;
	}

	/*
	Function	: StatusTimestampUpdate
	Type		: Static Document
	Author		: Lakshman A
	Date		: 02-NOV-15
	Objective  	: For the status[Pass or Fail] and the Attribute's Entimestamp Update
	Parameters 	: Document and Status - From CreateEventStatusElement
				  Setting the StrPassFail and  iFlagFailChk on pass and fail 
	 */	
	public static Document StatusTimestampUpdate(Document doc,String status) throws Exception{

		NodeList TestCaseNodeR=null;
		Element TestCaseElementR=null;
		NodeList TestCaseNodeREE=null;
		Element TestCaseElementREE=null;
		NodeList AppComponentNodeREE=null;
		Element AppComponentElementREE=null;
		try{


			//If Condition is to check any Event is failed i.e., status is passed as 3
			if (status.equals("3")) {

				//Process to get the Latest appropriate TestCase Element

				//Gets all the tag name in the available XML with name like TestCase
				TestCaseNodeR = doc.getElementsByTagName("TestCase");
				//Gets the latest appropriate TestCase ElementstCase
				TestCaseElementR =(Element) TestCaseNodeR.item(TestCaseNodeR.getLength()-1);

				//Removing the available attribute - Status with Value 1 and replacing it with 3 if any event is failed
				TestCaseElementR.removeAttribute("Status");
				TestCaseElementR.setAttribute("Status", status);

			}


			//Process to get the Latest appropriate TestCase and changes it Execution End time with current time

			//TestCase
			//Gets all the tag name in the available XML with name like TestCase
			TestCaseNodeREE = doc.getElementsByTagName("TestCase");
			//Gets the latest appropriate TestCase Element
			TestCaseElementREE =(Element) TestCaseNodeREE.item(TestCaseNodeREE.getLength()-1);
			//Removing the available attribute - ExecutionEnded with old time and replacing it with Currentime
			TestCaseElementREE.removeAttribute("ExecutionEnded");
			TestCaseElementREE.setAttribute("ExecutionEnded", getDateTime());

			//Process to get the Latest appropriate TestSuite and changes it Execution End time with current time

			//Gets all the tag name in the available XML with name like TestSuite
			NodeList TestSuiteNodeREE = doc.getElementsByTagName("TestSuite");
			//Gets the latest appropriate TestSuite Element
			Element TestSuiteElementREE =(Element) TestSuiteNodeREE.item(TestSuiteNodeREE.getLength()-1);
			//Removing the available attribute - ExecutionEnded with old time and replacing it with Currentime
			TestSuiteElementREE.removeAttribute("ExecutionEnded");
			TestSuiteElementREE.setAttribute("ExecutionEnded", getDateTime());


			//Process to get the Latest appropriate TestSuite and changes it Execution End time with current time

			//Gets all the tag name in the available XML with name like AppComponent
			AppComponentNodeREE = doc.getElementsByTagName("AppComponent");
			//Gets the latest appropriate AppComponent Element
			AppComponentElementREE =(Element) AppComponentNodeREE.item(AppComponentNodeREE.getLength()-1);
			//Removing the available attribute - ExecutionEnded with old time and replacing it with Currentime
			AppComponentElementREE.removeAttribute("ExecutionEnded");
			AppComponentElementREE.setAttribute("ExecutionEnded", getDateTime());

			//Setting the conditions as Pass or Fail for the strPassFail and iFlagFailChk
			if(status.equals("1"))
			{
				strPassFail="P";
			}else if(status.equals("3")){
				strPassFail="F";
				iFlagFailChk="F";
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			TestCaseNodeR=null;
			TestCaseElementR=null;
			TestCaseNodeREE=null;
			TestCaseElementREE=null;
			AppComponentNodeREE=null;
			AppComponentElementREE=null;

		}

		return doc;
	}

	/*
	Function	: SaveTestReportRuntime
	Type		: void
	Author		: Lakshman A
	Date		: 02-NOV-15
	Objective  	: Creates the Runtime Test Report file for Current TestBatch
	Parameters 	: Document
	 */
	public static void SaveTestReportRuntime(Document doc) throws Exception{

		TransformerFactory transformerFactory=null;
		Transformer transformer=null;
		DOMSource source =null;
		StreamResult file=null;
		String RunTimeXMLfilename=null;

		try{

			//Get the XML File name to Save the Report
			RunTimeXMLfilename=Param.getProperty("ReportfileName")+"_"+EnvironmentValue.getProperty("CURRENT_TESTBATCH_FILE").replaceFirst("[.][^.]+$", "");

			//for output to file, console
			//TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformerFactory = new com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl();
			//An instance of this Transformer class can transform a source tree into a result tree. 
			transformer = transformerFactory.newTransformer();
			//Setting INDENT to YES for pretty print, to NO will print the file in same line
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			//setXmlStandalone is an attribute specifying, as part of the XML declaration, whether this document is standalone
			//doc.setXmlStandalone(true);

			//DOMSource acts as a holder for a transformation Source tree in the form of a Document Object Model (DOM) tree.
			source = new DOMSource(doc);

			//To Write to custom report to Console
			//StreamResult console = new StreamResult(System.out);

			//Generate Runtime Custom report without TimeStamp so to update the same file. 
			file = new StreamResult(new File(System.getProperty("user.dir")+"\\Results\\"+RunTimeXMLfilename+".xml"));

			//Finally writing the XML data
			//transformer.transform(source, console);
			transformer.transform(source, file);

		}catch(Exception e){
			Log.info("Exception in SaveTestReportRuntime. Exp is : "+e);
		}finally{
			if(file!=null){
				file=null;
			}
			transformerFactory=null;
			transformer=null;
			source =null;
			RunTimeXMLfilename=null;
		}
	}

	/*
	Function	: SaveTestReport
	Type		: void
	Author		: Lakshman A
	Date		: 02-NOV-15
	Objective  	: Creates the Final Test Report at the end of Current Test Batch
	Parameters 	: Document
	 */
	public static void SaveTestReport(Document doc) throws Exception{

		Date SSNow=null;
		SimpleDateFormat SSft=null;
		String SSdatetime=null;
		TransformerFactory transformerFactory=null;
		Transformer transformer=null;
		DOMSource source =null;
		StreamResult file=null;
		String RunTimeFileName=null;
		File RunTimeFile=null;
		String XMLfilename=null;
		try{

			//Get the Runtime Report FileName
			RunTimeFileName=Param.getProperty("ReportfileName")+"_"+EnvironmentValue.getProperty("CURRENT_TESTBATCH_FILE").replaceFirst("[.][^.]+$", "");

			//Retrieve the RunTime Report file
			RunTimeFile = new File(System.getProperty("user.dir")+"\\Results\\"+RunTimeFileName+".xml");

			//Delete the Runtime Report file if the TestBatch is completely Executed i.e.,TestBatch not terminated abruptely.
			if(RunTimeFile.delete()){
				Log.info("Deleted the RunTime Report File : "+RunTimeFile.getName() + " Successfully");
			}else{
				Log.info("Delete operation of RunTime Report file failed.");
			}

			//Getting the DateTime in the needed format to append it with Custom filename 
			SSNow = new Date( );
			SSft = new SimpleDateFormat ("dd_MM_yyyy_hh_mm_ss_a");
			//@SuppressWarnings("unused")
			SSdatetime=SSft.format(SSNow);

			XMLfilename=Param.getProperty("ReportfileName")+"_"+EnvironmentValue.getProperty("CURRENT_TESTBATCH_FILE").replaceFirst("[.][^.]+$", "");

			//for output to file, console
			//TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformerFactory = new com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl();
			//An instance of this Transformer class can transform a source tree into a result tree. 
			transformer = transformerFactory.newTransformer();
			//Setting INDENT to YES for pretty print, to NO will print the file in same line
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			//setXmlStandalone is an attribute specifying, as part of the XML declaration, whether this document is standalone
			//doc.setXmlStandalone(true);

			//DOMSource acts as a holder for a transformation Source tree in the form of a Document Object Model (DOM) tree.
			source = new DOMSource(doc);

			//To Write to custom report to Console
			//StreamResult console = new StreamResult(System.out);

			//For generate custom report with filename and timestamp 
			file = new StreamResult(new File(System.getProperty("user.dir")+"\\Results\\"+XMLfilename+"_T"+SSdatetime+".xml"));

			Runtimevalue.setProperty("TEST_REPORT_FILENAME",System.getProperty("user.dir")+"\\Results\\"+XMLfilename+"_T"+SSdatetime+".xml");
			
			//For generate custom report with only filename 
			//StreamResult file = new StreamResult(new File(System.getProperty("user.dir")+"\\Results\\"+XMLfilename+".xml"));

			//Finally writing the XML data
			//transformer.transform(source, console);
			transformer.transform(source, file);

			//System.out.println("DONE");
		}catch(Exception e){

		}finally{
			if(file!=null){
				file=null;
			}
			SSNow=null;
			SSft=null;
			SSdatetime=null;
			transformerFactory=null;
			transformer=null;
			source =null;
			XMLfilename=null;
			RunTimeFileName=null;
			RunTimeFile=null;
			XMLfilename=null;
		}
	}
	
	/*	*//**
	 * 
	 * @Objective <b>For launch XML result file after executing 1 test batch<b>
	 * @author <b>Yogendra</b>
	 * @since <b>03-June-2016</b>
	 *//*

	public static void OpenResultFile()throws Exception  {

		try {
			//To Load IE driver Instance.
			DesiredCapabilities caps_report = DesiredCapabilities.internetExplorer();
			caps_report.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
			System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")+"\\BrowserDrivers\\IEDriverServer.exe");
			InternetExplorerDriver driver_To_Launch_XML = new InternetExplorerDriver(caps_report);
			driver_To_Launch_XML.manage().window().maximize();
			driver_To_Launch_XML.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			driver_To_Launch_XML.get(Runtimevalue.getProperty("TEST_REPORT_FILENAME"));

		} catch (Exception e) { 	
			return;
		}
	}*/
}
