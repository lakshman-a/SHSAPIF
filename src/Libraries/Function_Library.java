package Libraries;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.microsoft.sqlserver.jdbc.SQLServerStatement;
import com.sun.xml.internal.ws.policy.sourcemodel.AssertionData;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathExpression;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
//import java.io.FileWriter;
import java.text.DateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Utility.Log;
import Utility.ReadExcel;
import Libraries.Driver_Script;

//import net.neoremind.sshxcute.core.ConnBean;
//import net.neoremind.sshxcute.core.IOptionName;
//import net.neoremind.sshxcute.core.Result;
//import net.neoremind.sshxcute.core.SSHExec;
//import net.neoremind.sshxcute.exception.TaskExecFailException;
//import net.neoremind.sshxcute.task.CustomTask;
//import net.neoremind.sshxcute.task.impl.ExecCommand;



public class Function_Library extends Driver_Script{

	private static final String NULL = null;
	static int timeout_integer=0;
	static Connection con = null;
	static Statement stmt = null;
	static ResultSet rs = null;
	static String XML_Request=null;
	static String Updated_XML_Request=null;
	static String gbstrXMLResponseData;
	static String EntityName=null;
	static String NavigateUnixAutomationPath=null;
	static String NSSScriptCommand=null;
	static String Env_ITG_Script_Case_Name=null;
	static String Version_Revision_Path=null;
	static String GeneratedCommand=null;
	static String Unix_Folder_Path=null;
	static String strDateTimeFormat=null;
	static String AppURL=null;
	static String NSS_URL_PATH=null;
	static String TransactionIDTag=null;
	static String EntityNameTag=null;
	static String ProcessedXML=null;
	//jshSession
	static JSch jsch=null;
	public static Session JSHsession=null;
	public static Properties Jschconfig=null;
	static String endLineStr = " # ";
	public static Channel channel=null;

	//static String EnvironmentValue="";
	public static Connection EshopConnection=null;
	public static Statement EShopstmt=null;
	public static Connection OracleConnection=null;
	public static Statement RRBSoraclestmt=null;
	public static ResultSet rs_SQLServer=null;
	public static ResultSet RRBS_SQLServer=null;
	public static ResultSet Eshop_SQLServer=null;
	static Connection rrbsconnection = null;
	static Statement rrbsstatement = null;
	static ResultSet rrbsresultset = null;
	static Connection exibsconnection = null;
	static Statement exibsstatement = null;
	static ResultSet exibsresultset = null;
	//Unix object variable declaration
	//	static SSHExec ssh=null;	
	//	static ConnBean cb=null;
	static boolean connectionStatus=false;
	static boolean disconnectedStatus=false;
	static boolean Executionstatus=false;
	static String ITG_NSS_Auto_Scripts_Path=null;
	//	static Result result=null;
	static String Unix_Output=null;
	//XMLRequestResponse Declaration
	static DocumentBuilderFactory factory =null;
	static DocumentBuilder builder=null;
	static XPath xpath=null;
	static XPathFactory xpathfactory=null;
	static NodeList nodeList=null;
	static String strURL=null;	
	static String RequestXML=null;
	static Document document=null;
	//	static ExecCommand ec=null;
	//	static CustomTask Execution_Output=null;
	static File file=null;
	static BufferedInputStream in=null;
	static ChannelExec exec=null;

	/**
	 * 
	 * @Objective <b>Check any particular process running already. Returns True if running</b>
	 * @param serviceName
	 * @author <b>Lakshman</b>
	 * @since <b>02-November-15</b>
	 */
	public static boolean isProcessRunning(String serviceName) throws Exception {

		Process p=null;
		BufferedReader reader=null;
		String line=null;
		try{
			p = Runtime.getRuntime().exec("tasklist");
			reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = reader.readLine()) != null) {
				if (line.contains(serviceName)) {
					return true;
				}
			}
			return false;
		}catch(Exception e){
			Log.info("Exception occured while checking the Service Running state, Returing false. Exception is : "+e);
			return false;
		}finally{
			p=null;
			reader=null;
			line=null;
		}
	}

	/**
	 * 
	 * @Objective <b>To validate the app_component test steps for test reports</b>
	 * @param ComponentName
	 * @param testStepsStatus
	 * @author <b>Lakshman</b>
	 * @since <b>02-November-15</b>
	 */
	public static boolean Validate_AppComponent(String ComponentName,boolean testStepsStatus) throws Exception{
		boolean Validate_AppComponent= false;
		if(testStepsStatus){
			Report_Functions.ReportEventSuccess(doc, "1", "Validate_AppComponent", "Application Component '"+ComponentName+"' execution is  successful", 2);
			Validate_AppComponent = true;
		}else{
			Report_Functions.ReportEventFailure(doc, "Validate_AppComponent", "Application Component '"+ComponentName+"' execution is not successful", false);
			Validate_AppComponent = true;
		}
		return Validate_AppComponent;
	}

	/**
	 * 
	 * @Objective <b>To get the app_component name from excel sheet</b>
	 * @param strFunctionName
	 * @param strColumnName
	 * @param strExecEventFlag
	 * @author <b>Lakshman</b>
	 * @since <b>02-November-15</b>
	 */
	public static String RetrieveTestDataValue(String strFunctionName,String strColumnName,int strExecEventFlag) throws Exception{
		String strData=null;
		try{
			if(strExecEventFlag!=0){
				strData =ReadExcel.RetrieveTestDataFromSheet(Filepath, EnvironmentValue.getProperty("App_Component_Name"), strColumnName, gblrecordsCounter);
			}
			return strData;
		}catch(Exception e){
			//If column name doesn't exist in the test data sheet, report the error
			String strError = (strFunctionName + " - Error in reading the value from test data sheet's  Column: '" + strColumnName + "' ." + e);
			Report_Functions.ReportEventFailure(doc, strFunctionName, strError,false);
			throw e;
		}
	}

	/**
	 * @Objective <b>Retrieve the date from local machine and set it to for test results</b>
	 * @param DATE_FORMAT <b>- Return the date from local machine</b>
	 * @author <b>Lakshman</b>
	 * @since <b>02-November-15</b>
	 */
	public static String DateFormatChange(String DATE_FORMAT)  {

		Date date=null;
		SimpleDateFormat sdf =null;
		try
		{
			date = new Date();
			sdf = new SimpleDateFormat(DATE_FORMAT);
			return sdf.format(date);
		}catch(Exception e){
			Log.info("Exception in DateFormatChange is : "+e);
			throw e;
		}finally{
			date=null;
		}
	}

	/**
	 * @Objective <b>This method is to checking and opening the SQL connection<b>
	 * @param sqlserver
	 * @param sqldbname
	 * @param sqlusername
	 * @param sqlpassword
	 * @author <b>Lakshman</b>
	 * @since <b>02-November-15</b>
	 */
	public static boolean SQLDBOpenConnection(String sqlserver, String sqldbname, String sqlusername, String sqlpassword)throws Exception  {
		boolean elementStatus= false;
		//Connection URL Syntax: "jdbc:mysql://ipaddress:portnumber/db_name"        
		String dbUrl = "jdbc:sqlserver://"+ sqlserver +";DatabaseName=" + sqldbname +";";                  
		//Database Username     
		String username = sqlusername;   
		//Database Password     
		String password = sqlpassword; 
		try {
			//Load mysql jdbc driver        
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");         
			//Create Connection to DB       
			con = DriverManager.getConnection(dbUrl,username,password);
			//Create Statement Object       
			stmt = con.createStatement(); 
			elementStatus=true;
			Report_Functions.ReportEventSuccess(doc, "1", "SQLDBOpenConnection", "SQL Connection is established Successfully.", 2);

		} catch (Exception e) { 
			elementStatus=false;
			Report_Functions.ReportEventFailure(doc,  "SQLDBOpenConnection",  "Error occured while connecting to the SQL Server.Error description is : "+ e.getMessage() +".", false);
			Log.info("SQLDBOpenConnection Error : " + e);
		}
		return elementStatus;
	}

	/**Description	: Deleting record from SQL DB
	 Parameters	: SQL tablename, condition
	 * @Objective <b>This method is to deleting records from SQL DB<b>
	 * @param sqltablename
	 * @param sqlcondition
	 * @param strExecEventFlag
	 * @author <b>Lakshman</b>
	 * @since <b>02-November-15</b>
	 */
	public static boolean SQLDBDelete(String sqltablename, String sqlcondition, int strExecEventFlag)throws Exception  {
		boolean elementStatus= false;
		String tablename = null;
		String condition = null;

		try{

			if(strExecEventFlag==1){
				tablename=RetrieveTestDataValue("SQLDBDelete",sqltablename,strExecEventFlag);
				condition=RetrieveTestDataValue("SQLDBDelete",sqlcondition,strExecEventFlag);
			}

			if(tablename==null || condition==null){
				throw new RuntimeException ("strData is null");
			}

		}catch(Exception e){
			Log.info("Error log in reading file is "+e);
			throw e;
		}

		String check = "select * from "+tablename +" where "+condition;
		String query = "Delete from "+ tablename +" where "+ condition;
		ResultSet rs = null;

		try {			
			rs = stmt.executeQuery(check);		

			int temp=0;	
			while(rs.next()){
				temp++;
			}

			if(temp >= 1){

				stmt.execute(query);
				elementStatus=true;

				Report_Functions.ReportEventSuccess(doc, "1", "SQLDBDelete", "The SQL Query : "+ query + " executed successfully.", 2);

			}//If rows not available FALSE will be returned so no delete
			else if(temp < 1){
				Report_Functions.ReportEventSuccess(doc, "1", "SQLDBDelete", "The SQL Query : "+ query + " records are not availbale in DB", 2);
				elementStatus=true;
			}

		}catch (Exception e) { 
			elementStatus=false;
			Report_Functions.ReportEventFailure(doc,  "SQLDBDelete",  "Error occured while executing the SQL query.Error description is : "+ e.getMessage() +".", false);
			Log.info("SQLDBDelete Error : " + e);
		}
		return elementStatus;
	}

	/**
	 * 
	 * @Objective <b>This method is to closing the SQL DB<b>
	 * @return
	 * @author <b>Lakshman</b>
	 * @since <b>02-November-15</b>
	 */
	public static boolean SQLDBCloseConnection()throws Exception  {
		boolean elementStatus= false;

		try {
			// closing DB Connection       
			con.close(); 
			elementStatus=true;
			Report_Functions.ReportEventSuccess(doc, "1", "SQLDBCloseConnection", "The SQL DB Connection disconnected successfully.", 2);

		} catch (Exception e) { 
			elementStatus=false;
			Report_Functions.ReportEventFailure(doc,  "SQLDBCloseConnection",  "Error occured while closing the SQL DB.Error description is : "+ e.getMessage() +".", false);
			Log.info("SQLDBCloseConnection Error : " + e);
		}
		return elementStatus;
	}

	/**
	 * 
	 * @Objective <b>Opening RRBS DB Connection</b>
	 * @param dbserver
	 * @param portnumber
	 * @param dbname
	 * @param dbusername
	 * @param dbpassword
	 * @author <b>Lakshman</b>
	 * @since <b>02-November-15</b>
	 */
	public static boolean RRBSDBOpenConnection(String dbserver, String portnumber, String dbname, String dbusername, String dbpassword)throws Exception  {
		boolean elementStatus= false;

		String serverName = Param.getProperty(dbserver);
		String portNumber = Param.getProperty(portnumber);
		String sid = Param.getProperty(dbname);


		//Connection URL Syntax: "jdbc:oracle:thin:@://ipaddress:portnumber:db_name"        
		String dbUrl = "jdbc:oracle:thin:@" + serverName + ":" + portNumber + ":" + sid; 

		//Database Username     
		String username = Param.getProperty(dbusername);   
		//Database Password     
		String password = Param.getProperty(dbpassword); 

		try {
			// Load the JDBC driver

			String driverName = "oracle.jdbc.OracleDriver";

			Class.forName(driverName);         
			//Create Connection to DB       
			rrbsconnection = DriverManager.getConnection(dbUrl,username,password);
			//Create Statement Object       
			rrbsstatement = rrbsconnection.createStatement(); 
			elementStatus=true;
			Report_Functions.ReportEventSuccess(doc, "1", "RRBSDBOpenConnection", "RRBS DB Connection is established Successfully.", 2);

		} catch (Exception e) { 
			elementStatus=false;
			Report_Functions.ReportEventFailure(doc,  "RRBSDBOpenConnection",  "Error occured while connecting to the RRBS DB Server.Error description is : "+ e.getMessage() +".", false);
			Log.info("RRBSDBOpenConnection Error : " + e);
		}
		return elementStatus;
	}

	/**
	 * 
	 * @Objective <b>Deleting the record from RRBS DB</b>
	 * @param rrbstablename
	 * @param rrbscondition
	 * @param strExecEventFlag
	 * @author <b>Lakshman</b>
	 * @since <b>02-November-15</b>
	 */
	public static boolean RRBSDBDelete(String rrbstablename, String rrbscondition, int strExecEventFlag)throws Exception  {
		boolean elementStatus= false;
		String tablename = null;
		String condition = null;

		try{

			if(strExecEventFlag==1){
				tablename=RetrieveTestDataValue("RRBSDBDelete",rrbstablename,strExecEventFlag);
				condition=RetrieveTestDataValue("RRBSDBDelete",rrbscondition,strExecEventFlag);
			}

			if(tablename==null || condition==null){
				throw new RuntimeException ("strData is null");
			}

		}catch(Exception e){
			Log.info("Errror log in reading file is "+e);
			return elementStatus;
			//throw e;

		}
		//Query to Execute      
		String check = "select * from "+tablename +" where "+condition;
		String query = "Delete from "+ tablename +" where "+ condition;

		ResultSet rs = null;

		try {

			rs = rrbsstatement.executeQuery(check);

			int temp = 0;
			while(rs.next()){

				temp++;

			}

			if(temp >= 1){

				rrbsstatement.execute(query); 
				elementStatus=true;
				Report_Functions.ReportEventSuccess(doc, "1", "RRBSDBDelete", "The RRBS Query : "+ query + " executed successfully", 2);

			}

			//If rows not available FALSE will be returned so delete operation doesn't works here

			else if(temp < 1){

				Report_Functions.ReportEventSuccess(doc, "1", "RRBSDBDelete", "The RRBS Query : "+ query + " records are not availbale in DB", 2);
				elementStatus = true;

			}

		} catch (Exception e) { 
			elementStatus=false;
			Report_Functions.ReportEventFailure(doc,  "RRBSDBDelete",  "Error occured while executing the RRBS query.Error description is : "+ e.getMessage() +".", false);
			Log.info("RRBSDBDelete Error : " + e);
		}
		return elementStatus;
	}

	/** @Objective <b>Deleting the record from RRBS DB</b>
	 * @param rrbstablename
	 * @param rrbscondition
	 * @param strExecEventFlag
	 * @author <b>Lakshman</b>
	 * @since <b>08-AUG-16</b>
	 */
	public static boolean RRBSDBDeleteConditionEnvVar(String rrbstablename, String rrbscondition,String envVariable, int strExecEventFlag)throws Exception  {
		boolean elementStatus= false;
		String tablename = null;
		String condition = null;
		String expectedCondition=null;

		try{

			if(strExecEventFlag==1){
				tablename=RetrieveTestDataValue("RRBSDBDelete",rrbstablename,strExecEventFlag);
				condition=RetrieveTestDataValue("RRBSDBDelete",rrbscondition,strExecEventFlag);
			}

			if(tablename==null || condition==null){
				throw new RuntimeException ("strData is null");
			}

		}catch(Exception e){
			Log.info("Errror log in reading file is "+e);
			return elementStatus;
			//throw e;

		}

		String envValue = EnvironmentValue.getProperty(envVariable);

		if(envValue==null){

			throw new RuntimeException ("Environment value is null");
		}

		expectedCondition = condition +"="+envValue;

		//Query to Execute      
		String check = "select * from "+tablename +" where "+expectedCondition;
		String query = "Delete from "+ tablename +" where "+ expectedCondition;

		log.info("Query is : "+query);

		ResultSet rs = null;

		try {

			rs = rrbsstatement.executeQuery(check);

			int temp = 0;
			while(rs.next()){

				temp++;

			}

			if(temp >= 1){

				rrbsstatement.execute(query); 
				elementStatus=true;
				Report_Functions.ReportEventSuccess(doc, "1", "RRBSDBDelete", "The RRBS Query : "+ query + " executed successfully", 2);

			}

			//If rows not available FALSE will be returned so delete operation doesn't works here

			else if(temp < 1){

				Report_Functions.ReportEventSuccess(doc, "1", "RRBSDBDelete", "The RRBS Query : "+ query + " records are not availbale in DB", 2);
				elementStatus = true;

			}

		} catch (Exception e) { 
			elementStatus=false;
			Report_Functions.ReportEventFailure(doc,  "RRBSDBDelete",  "Error occured while executing the RRBS query.Error description is : "+ e.getMessage() +".", false);
			Log.info("RRBSDBDelete Error : " + e);
		}
		return elementStatus;
	}

	/**
	 * 
	 * @Objective <b>Verify to compare the dates in RRBS DB</b>
	 * @param sqltablename
	 * @param strsqlcolumnname
	 * @param strsqlcondition
	 * @param Date_Format
	 * @param strExecEventFlag
	 * @author <b>Praveen Lakshmanan</b>
	 * @since <b>03-May-16</b>
	 */
	public static boolean RRBSDBDateCompare(String sqltablename, String strsqlcolumnname,String strsqlcondition,String Date_Format,int strExecEventFlag)throws Exception  {

		boolean RRBSDBDateCompare= false;
		String query = null;  
		String Table_name = null;
		String Column_name = null;
		String SQL_condition = null;
		String Expected_value = null;
		String Actual_Value = null;
		String Current_Date=null;
		String expected_db_Date = null;

		try {
			if(strExecEventFlag==1){
				Table_name=RetrieveTestDataValue("RRBSDBDateCompare",sqltablename,strExecEventFlag);
				Column_name=RetrieveTestDataValue("RRBSDBDateCompare",strsqlcolumnname,strExecEventFlag);
				SQL_condition=RetrieveTestDataValue("RRBSDBDateCompare",strsqlcondition,strExecEventFlag);
			}

			if(Table_name==null){
				Report_Functions.ReportEventFailure(doc,  "RRBSDBDateCompare",  "SQL table name is not provided in the Data Sheet.", false);
				RRBSDBDateCompare= false;
				throw new RuntimeException ("Table_name is null");
			}

			if(Column_name==null){
				Report_Functions.ReportEventFailure(doc,  "RRBSDBDateCompare",  "SQL Column name is not provided in the Data Sheet.", false);
				RRBSDBDateCompare= false;
				throw new RuntimeException ("Column_name is null");
			}

			if(SQL_condition==null){
				Report_Functions.ReportEventFailure(doc,  "RRBSDBDateCompare",  "SQL condition is not provided in the Data Sheet.", false);
				RRBSDBDateCompare= false;
				throw new RuntimeException ("SQL_condition is null");
			}

			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date date = new Date();

			Current_Date = dateFormat.format(date);

			Expected_value = Current_Date.trim();

			// Query to Execute      
			query = "select "+Column_name+" from "+Table_name+" where "+SQL_condition+"";

			rrbsresultset = rrbsstatement.executeQuery(query);

		}catch (Exception e){
			Report_Functions.ReportEventFailure(doc,  "RRBSDBDateCompare",  "Error occured while executing the SQL query.Error description is : "+ e.getMessage() +".", true);
			Log.info("RRBSDBDateCompare Error : " + e);
			RRBSDBDateCompare=false;
		}

		try{

			rrbsresultset.next();

			Actual_Value = rrbsresultset.getString(1);
			String db_Date = Actual_Value.split(" ")[0];

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
			Date dateToChange = dateFormat.parse(db_Date);			

			SimpleDateFormat finalDateFormat = new SimpleDateFormat(Date_Format);
			expected_db_Date = finalDateFormat.format(dateToChange);


		} catch (Exception ne) {           // If no record is present in the fired Query
			Report_Functions.ReportEventFailure(doc,  "RRBSDBDateCompare",  "No Record found for this query: "+query, true);
			Log.info("RRBSDBDateCompare Error : "+ne);
			RRBSDBDateCompare=false;
		}

		try{

			if(!rrbsresultset.wasNull()){            // If some value is present in the fired Query

				if(expected_db_Date.equals(Expected_value)){

					Report_Functions.ReportEventSuccess(doc, "1", "RRBSDBDateCompare", "The selected date : '"+expected_db_Date+"' in the column : '"+Column_name+"' of table : '"+Table_name+"' matches with the expected date : '"+Expected_value+"' ", 2);
					RRBSDBDateCompare=true;

				}else if(!(expected_db_Date.equals(Expected_value))){
					Report_Functions.ReportEventFailure(doc,  "RRBSDBDateCompare",  "The selected date : '"+expected_db_Date+"' in the column : '"+Column_name+"' of table : '"+Table_name+"' does not match with the expected date : '"+Expected_value+"' ", true);
					RRBSDBDateCompare=false;
				}
			}
			else if(rrbsresultset.wasNull()){        // If "NULL" value is present in the fired Query
				if(Expected_value.equals("NULL")){
					Report_Functions.ReportEventSuccess(doc, "1", "RRBSDBDateCompare", "The actual value 'NULL' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' matches with the expected value :'"+Expected_value+"'", 2);
					RRBSDBDateCompare=true;
				}

				else if(!(Expected_value.equals("NULL"))){
					Report_Functions.ReportEventFailure(doc,  "RRBSDBDateCompare",  "The actual value 'NULL' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' does not match the expected value : '"+Expected_value+"'", true);  	 
					RRBSDBDateCompare=false;
				}
			}

		}catch (Exception e){
			Report_Functions.ReportEventFailure(doc,  "RRBSDBDateCompare",  "Error occured while comparing the dates in SQL query.Error description is : "+ e.getMessage() +".", true);
			Log.info("RRBSDBDateCompare Error : " + e);
			RRBSDBDateCompare=false;
		}

		return RRBSDBDateCompare;
	}

	/**
	 * 
	 * @Objective <b>Verifying the column value in RRBS DB</b>
	 * @param rrbstablename
	 * @param rrbscolumnname
	 * @param rrbscondition
	 * @param rrbscolumnvalue
	 * @param strExecEventFlag
	 * @author <b>Lakshman</b>
	 * @since <b>02-November-15</b>
	 */
	public static boolean RRBSDBSelect(String rrbstablename, String rrbscolumnname, String rrbscondition, String rrbscolumnvalue, int strExecEventFlag)throws Exception  {
		boolean elementStatus= false;
		String tablename = null;
		String condition = null;
		String columnname = null;
		String columnvalue = null;
		String actualvalue = null;
		try{

			if(strExecEventFlag==1){
				tablename=RetrieveTestDataValue("RRBSDBSelect",rrbstablename,strExecEventFlag);
				condition=RetrieveTestDataValue("RRBSDBSelect",rrbscondition,strExecEventFlag);
				columnname=RetrieveTestDataValue("RRBSDBSelect",rrbscolumnname,strExecEventFlag);
				columnvalue=RetrieveTestDataValue("RRBSDBSelect",rrbscolumnvalue,strExecEventFlag);
			}

			if(tablename==null || condition==null || columnname==null ){
				throw new RuntimeException ("strData is null");
			}

		}catch(Exception e){
			Log.info("Errror log in reading file is "+e);
			throw e;
		}
		//Query to Execute      
		String query = "select "+ columnname +" from "+ tablename +" where "+ condition;
		System.out.println("query:"+query);
		try {
			ResultSet rrbsresultset = rrbsstatement.executeQuery(query);
			while (rrbsresultset.next()){


				actualvalue = rrbsresultset.getString(1);



			}
			if(actualvalue.equalsIgnoreCase(columnvalue))
			{
				elementStatus=true;
				Report_Functions.ReportEventSuccess(doc, "1", "RRBSDBSelect", "The actual value : '"+actualvalue+"' in column : '"+columnname+"' of table : '"+tablename+"' with condition : '"+condition+"' matches the expected value : '"+columnvalue+"'", 2);
			} else {
				elementStatus=false;
				Report_Functions.ReportEventFailure(doc,  "RRBSDBSelect",  "The actual value : "+actualvalue+ "' in column :'"+columnname+"' of table: '"+tablename+"' with condition :'"+condition+"' does not matches with the expected value :"+ columnvalue +".", false);	
			}
		} catch (Exception e) { 
			elementStatus=false;
			Report_Functions.ReportEventFailure(doc,  "RRBSDBSelect",  "Error occured while executing the RRBS query.Error description is : "+ e.getMessage() +".", false);
			Log.info("RRBSDBSelect Error : " + e);
		}
		return elementStatus;
	}

	/**
	 * 
	 * @Objective <b>Verifying the column value in RRBS DB</b>
	 * @param rrbstablename
	 * @param rrbscolumnname
	 * @param rrbscondition
	 * @param rrbscolumnvalue
	 * @param strExecEventFlag
	 * @author <b>Lakshman</b>
	 * @since <b>08-AUG-15</b>
	 */
	public static boolean RetrieveRRBSValueStoresInEnvVar(String rrbstablename, String rrbscolumnname,String rrbsCondition, String envVariable,  int strExecEventFlag)throws Exception  {

		boolean elementStatus= false;
		String tablename = null;
		String condition=null;
		String columnname = null;
		String actualvalue = null;

		try{

			if(strExecEventFlag==1){
				tablename=RetrieveTestDataValue("RRBSDBSelect",rrbstablename,strExecEventFlag);
				condition=RetrieveTestDataValue("RRBSDBSelect",rrbsCondition,strExecEventFlag);
				columnname=RetrieveTestDataValue("RRBSDBSelect",rrbscolumnname,strExecEventFlag);
			}

			if(tablename==null){
				Report_Functions.ReportEventFailure(doc,  "RRBSDBSelectConditionEnvVar",  "SQL table name is not provided in the Data Sheet.", false);
				elementStatus= false;
				throw new RuntimeException ("Table_name is null");
			}

			if(columnname==null){
				Report_Functions.ReportEventFailure(doc,  "RRBSDBSelectConditionEnvVar",  "SQL Column name is not provided in the Data Sheet.", false);
				elementStatus= false;
				throw new RuntimeException ("Column_name is null");
			}

			if(condition==null){
				Report_Functions.ReportEventFailure(doc,  "RRBSDBSelectConditionEnvVar",  "SQL condition is not provided in the Data Sheet.", false);
				elementStatus= false;
				throw new RuntimeException ("SQL_condition is null");
			}


		}catch (Exception e) {
			Report_Functions.ReportEventFailure(doc,  "RRBSDBSelectConditionEnvVar",  "Error occured .Error description is : "+ e.getMessage() +".", true);
			Log.info("EshopSQLDBEnvironmentVariableCompare Error : " + e);
			elementStatus=false;
		}

		//Query to Execute      
		String query = "select "+ columnname +" from "+ tablename +" where "+ condition;
		log.info("query is : "+query);

		try {
			ResultSet rrbsresultset = rrbsstatement.executeQuery(query);
			while (rrbsresultset.next()){
				actualvalue = rrbsresultset.getString(1);
			}

			EnvironmentValue.setProperty(envVariable, actualvalue);

			Report_Functions.ReportEventSuccess(doc,"1", "RetrieveRRBSValueStoresInEnvVar", "The value : '"+ actualvalue +"' is stored in the environment variable : '"+ envVariable +"' successfully.",3);
			elementStatus=true;
		} catch (Exception e) { 
			elementStatus=false;
			Report_Functions.ReportEventFailure(doc,  "RRBSDBSelect",  "Error occured while executing the RRBS query.Error description is : "+ e.getMessage() +".", false);
			Log.info("RRBSDBSelect Error : " + e);
		}

		return elementStatus;
	}

	/**
	 * 
	 * @Objective <b>Verifying the column value in RRBS DB</b>
	 * @param rrbstablename
	 * @param rrbscolumnname
	 * @param rrbscondition
	 * @param rrbscolumnvalue
	 * @param strExecEventFlag
	 * @author <b>Lakshman</b>
	 * @since <b>08-AUG-15</b>
	 */
	public static boolean RRBSDBSelectConditionEnvVar(String rrbstablename, String rrbscolumnname,String rrbsCondition, String rrbscolumnvalue,String envVariable,  int strExecEventFlag)throws Exception  {

		boolean elementStatus= false;
		String tablename = null;
		String condition=null;
		String expectedCondtion = null;
		String envValue=null;
		String columnname = null;
		String columnvalue = null;
		String actualvalue = null;


		try{

			if(strExecEventFlag==1){
				tablename=RetrieveTestDataValue("RRBSDBSelect",rrbstablename,strExecEventFlag);
				condition=RetrieveTestDataValue("RRBSDBSelect",rrbsCondition,strExecEventFlag);
				columnname=RetrieveTestDataValue("RRBSDBSelect",rrbscolumnname,strExecEventFlag);
				columnvalue=RetrieveTestDataValue("RRBSDBSelect",rrbscolumnvalue,strExecEventFlag);
			}

			if(tablename==null){
				Report_Functions.ReportEventFailure(doc,  "RRBSDBSelectConditionEnvVar",  "SQL table name is not provided in the Data Sheet.", false);
				elementStatus= false;
				throw new RuntimeException ("Table_name is null");
			}

			if(columnname==null){
				Report_Functions.ReportEventFailure(doc,  "RRBSDBSelectConditionEnvVar",  "SQL Column name is not provided in the Data Sheet.", false);
				elementStatus= false;
				throw new RuntimeException ("Column_name is null");
			}

			if(columnvalue==null){
				Report_Functions.ReportEventFailure(doc,  "RRBSDBSelectConditionEnvVar",  "SQL condition is not provided in the Data Sheet.", false);
				elementStatus= false;
				throw new RuntimeException ("SQL_condition is null");
			}


		}catch (Exception e) {
			Report_Functions.ReportEventFailure(doc,  "RRBSDBSelectConditionEnvVar",  "Error occured .Error description is : "+ e.getMessage() +".", true);
			Log.info("RRBSDBSelectConditionEnvVar Error : " + e);
			elementStatus=false;
		}

		envValue = EnvironmentValue.getProperty(envVariable);
		expectedCondtion = condition+"="+envValue;

		if(envValue==null){
			Report_Functions.ReportEventFailure(doc,  "RRBSDBSelectConditionEnvVar",  "The Value in environment variable: '"+ envVariable +"' is empty", true);
			elementStatus=false;
			throw new RuntimeException ("Env variable is null");
		}else{

			//Query to Execute      
			String query = "select "+ columnname +" from "+ tablename +" where "+ expectedCondtion;
			log.info("query:"+query);

			try {
				ResultSet rrbsresultset = rrbsstatement.executeQuery(query);
				while (rrbsresultset.next()){
					actualvalue = rrbsresultset.getString(1);
				}
				if(actualvalue.equalsIgnoreCase(columnvalue))
				{
					elementStatus=true;
					Report_Functions.ReportEventSuccess(doc, "1", "RRBSDBSelectConditionEnvVar", "The actual value : '"+actualvalue+"' in column : '"+columnname+"' of table : '"+tablename+"' with condition : '"+condition+"' matches the expected value : '"+columnvalue+"'", 2);
				} else {
					elementStatus=false;
					Report_Functions.ReportEventFailure(doc,  "RRBSDBSelectConditionEnvVar",  "The actual value : "+actualvalue+ "' in column :'"+columnname+"' of table: '"+tablename+"' with condition :'"+condition+"' does not matches with the expected value :"+ columnvalue +".", false);	
				}
			} catch (Exception e) { 
				elementStatus=false;
				Report_Functions.ReportEventFailure(doc,  "RRBSDBSelectConditionEnvVar",  "Error occured while executing the RRBS query.Error description is : "+ e.getMessage() +".", false);
				Log.info("RRBSDBSelectConditionEnvVar Error : " + e);
			}
		}
		return elementStatus;
	}

	/** @Objective <b>Verifying the column value in RRBS DB</b>
	 * @param rrbstablename
	 * @param rrbscolumnname
	 * @param rrbscondition
	 * @param rrbscolumnvalue
	 * @param strExecEventFlag
	 * @author <b>Lakshman</b>
	 * @since <b>08-AUG-15</b>
	 */
	public static boolean RRBSDBSelectEnvvar(String rrbstablename, String rrbscolumnname,String rrbsCondition,String envVariable,  int strExecEventFlag)throws Exception  {

		boolean elementStatus= false;
		String tablename = null;
		String condition=null;
		String expectedValue = null;
		String columnname = null;
		String actualvalue = null;

		try{

			if(strExecEventFlag==1){
				tablename=RetrieveTestDataValue("RRBSDBSelect",rrbstablename,strExecEventFlag);
				condition=RetrieveTestDataValue("RRBSDBSelect",rrbsCondition,strExecEventFlag);
				columnname=RetrieveTestDataValue("RRBSDBSelect",rrbscolumnname,strExecEventFlag);
			}

			if(tablename==null){
				Report_Functions.ReportEventFailure(doc,  "RRBSDBSelectConditionEnvVar",  "SQL table name is not provided in the Data Sheet.", false);
				elementStatus= false;
				throw new RuntimeException ("Table_name is null");
			}

			if(columnname==null){
				Report_Functions.ReportEventFailure(doc,  "RRBSDBSelectConditionEnvVar",  "SQL Column name is not provided in the Data Sheet.", false);
				elementStatus= false;
				throw new RuntimeException ("Column_name is null");
			}

		}catch (Exception e) {
			Report_Functions.ReportEventFailure(doc,  "RRBSDBSelectConditionEnvVar",  "Error occured .Error description is : "+ e.getMessage() +".", true);
			Log.info("EshopSQLDBEnvironmentVariableCompare Error : " + e);
			elementStatus=false;
		}

		expectedValue = EnvironmentValue.getProperty(envVariable);

		if(expectedValue==null){
			Report_Functions.ReportEventFailure(doc,  "RRBSDBSelectConditionEnvVar",  "The Value in environment variable: '"+ envVariable +"' is empty", true);
			elementStatus=false;
			throw new RuntimeException ("Env variable is null");
		}else{

			//Query to Execute      
			String query = "select "+ columnname +" from "+ tablename +" where "+ condition;
			log.info("query:"+query);

			try {
				ResultSet rrbsresultset = rrbsstatement.executeQuery(query);
				while (rrbsresultset.next()){
					actualvalue = rrbsresultset.getString(1);
				}
				if(actualvalue.equalsIgnoreCase(expectedValue))
				{
					elementStatus=true;
					Report_Functions.ReportEventSuccess(doc, "1", "RRBSDBSelect", "The actual value : '"+actualvalue+"' in column : '"+columnname+"' of table : '"+tablename+"' with condition : '"+condition+"' matches the expected value : '"+expectedValue+"'", 2);
				} else {
					elementStatus=false;
					Report_Functions.ReportEventFailure(doc,  "RRBSDBSelect",  "The actual value : "+actualvalue+ "' in column :'"+columnname+"' of table: '"+tablename+"' with condition :'"+condition+"' does not matches with the expected value :"+ expectedValue +"", false);	
				}
			} catch (Exception e) { 
				elementStatus=false;
				Report_Functions.ReportEventFailure(doc,  "RRBSDBSelect",  "Error occured while executing the RRBS query.Error description is : "+ e.getMessage() +".", false);
				Log.info("RRBSDBSelect Error : " + e);
			}
		}
		return elementStatus;
	}

	/**
	 * 
	 * @Objective <b>losing RRBS DB connection</b>
	 * @author <b>Lakshman</b>
	 * @since <b>02-November-15</b>
	 */
	public static boolean RRBSDBCloseConnection()throws Exception  {
		boolean elementStatus= false;

		try {
			// closing DB Connection       
			rrbsconnection.close(); 
			elementStatus=true;
			Report_Functions.ReportEventSuccess(doc, "1", "RRBSDBCloseConnection", "The RRBS DB Connection disconnected successfully.", 2);

		} catch (Exception e) { 
			elementStatus=false;
			Report_Functions.ReportEventFailure(doc,  "RRBSDBCloseConnection",  "Error occured while closing the RRBS DB.Error description is : "+ e.getMessage() +".", false);
			Log.info("RRBSDBCloseConnection Error : " + e);
		}
		return elementStatus;
	}

	/**
	 * 
	 * @Objective <b>Verifies the SQL DB Update</b>
	 * @param sqltablename
	 * @param strsqlcolumnname
	 * @param strsqlcolumnvalue
	 * @param strsqlcondition
	 * @param strExecEventFlag
	 * @author <b>Lakshman</b>
	 * @since <b>02-November-15</b>
	 */
	public static boolean SQLDBUpdate(String sqltablename, String strsqlcolumnname,String strsqlcolumnvalue,String strsqlcondition,int strExecEventFlag)throws Exception  {
		boolean SQLDBUpdate= false;
		String Table_name = null;
		String Column_name = null;
		String Column_Value = null;
		String SQL_condition = null;

		try {
			if(strExecEventFlag==1){
				Table_name=RetrieveTestDataValue("SQLDBUpdate",sqltablename,strExecEventFlag);
				Column_name=RetrieveTestDataValue("SQLDBUpdate",strsqlcolumnname,strExecEventFlag);
				Column_Value=RetrieveTestDataValue("SQLDBUpdate",strsqlcolumnvalue,strExecEventFlag);
				SQL_condition=RetrieveTestDataValue("SQLDBUpdate",strsqlcondition,strExecEventFlag);
			}

			if(Table_name==null){
				Report_Functions.ReportEventFailure(doc,  "SQLDBUpdate",  "SQL table name is not provided in the Data Sheet.", false);
				SQLDBUpdate= false;
				throw new RuntimeException ("Table_name is null");
			}

			if(Column_name==null){
				Report_Functions.ReportEventFailure(doc,  "SQLDBUpdate",  "SQL Column name is not provided in the Data Sheet.", false);
				SQLDBUpdate= false;
				throw new RuntimeException ("Column_name is null");
			}

			if(Column_Value==null){
				Report_Functions.ReportEventFailure(doc,  "SQLDBUpdate",  "SQL Column value is not provided in the Data Sheet.", false);
				SQLDBUpdate= false;
				throw new RuntimeException ("Column_Value is null");
			}

			if(SQL_condition==null){
				Report_Functions.ReportEventFailure(doc,  "SQLDBUpdate",  "SQL condition is not provided in the Data Sheet.", false);
				SQLDBUpdate= false;
				throw new RuntimeException ("SQL_condition is null");
			}

			//Query to Execute      
			String query =null;
			if(Column_Value.equals("NULL")){	
				query = "update "+Table_name+" set "+Column_name+"= NULL where "+SQL_condition;
			}else{
				query = "update "+Table_name+" set "+Column_name+"='"+Column_Value+"' where "+SQL_condition;
			}
			System.out.println("query update:"+query);
			stmt.execute(query);
			SQLDBUpdate=true;
			Report_Functions.ReportEventSuccess(doc, "1", "SQLDBUpdate", "The SQL Update Query : "+ query + " executed successfully.", 2);


		} catch (Exception e) {
			SQLDBUpdate=false;
			Report_Functions.ReportEventFailure(doc,  "SQLDBUpdate",  "Error occured while executing the SQL query.Error description is : "+ e.getMessage() +".", true);
			Log.info("SQLDBUpdate Error : " + e);
		}
		return SQLDBUpdate;
	}

	/**
	 * 
	 * @Objective <b>Verifies the SQL DB select</b>
	 * @param sqltablename
	 * @param strsqlcolumnname
	 * @param strsqlcondition
	 * @param strExpectedvalue
	 * @param strExecEventFlag
	 * @author <b>Lakshman</b>
	 * @since <b>02-November-15</b>
	 *//*
	public static boolean SQLDBSelect(String sqltablename, String strsqlcolumnname,String strsqlcondition,String strExpectedvalue,int strExecEventFlag)throws Exception  {
		boolean SQLDBSelect= false;
		String query = null;
		String Table_name = null;
		String Column_name = null;
		String SQL_condition = null;
		//String Expected_value = null;		
		String Expected_value = "";
		String Actual_Value = null;
		try {
			if(strExecEventFlag==1){
				Table_name=RetrieveTestDataValue("SQLDBSelect",sqltablename,strExecEventFlag);
				Column_name=RetrieveTestDataValue("SQLDBSelect",strsqlcolumnname,strExecEventFlag);
				SQL_condition=RetrieveTestDataValue("SQLDBSelect",strsqlcondition,strExecEventFlag);
				Expected_value=RetrieveTestDataValue("SQLDBSelect",strExpectedvalue,strExecEventFlag);
			}

			if(Table_name==null){
				Report_Functions.ReportEventFailure(doc,  "SQLDBSelect",  "SQL table name is not provided in the Data Sheet.", false);
				SQLDBSelect= false;
				throw new RuntimeException ("Table_name is null");
			}

			if(Column_name==null){
				Report_Functions.ReportEventFailure(doc,  "SQLDBSelect",  "SQL Column name is not provided in the Data Sheet.", false);
				SQLDBSelect= false;
				throw new RuntimeException ("Column_name is null");
			}

			if(SQL_condition==null){
				Report_Functions.ReportEventFailure(doc,  "SQLDBSelect",  "SQL condition is not provided in the Data Sheet.", false);
				SQLDBSelect= false;
				throw new RuntimeException ("SQL_condition is null");
			}

			//Query to Execute      
			query = "select "+Column_name+" from "+Table_name+" where "+SQL_condition+"";

			rs_SQLServer = stmt.executeQuery(query);
			//rs_SQLServer.getNString("");

			//query = "select "+Column_name+" from "+Table_name+" where "+SQL_condition+"";
			//rs_SQLServer = stmt.executeQuery(query);
			//rs_SQLServer.next();
			//Actual_Value = rs_SQLServer.getString(1).trim();
		} catch (Exception e) {
			Report_Functions.ReportEventFailure(doc,  "SQLDBSelect",  "Error occured while executing the SQL query.Error description is : "+ e.getMessage() +".", true);
			Log.info("SQLDBSelect Error : " + e);
			SQLDBSelect=false;
		}

		try{
			System.out.println("query:"+query);
			rs_SQLServer.next();
			System.out.println("before act value");
			Actual_Value = rs_SQLServer.getString(1).trim();
			
		} catch(NullPointerException e){
			
			Log.info("Null pointer occurred :"+e.getMessage());

		} catch (Exception e) {           // If no record is present in the fired Query
			Report_Functions.ReportEventFailure(doc,  "SQLDBSelect",  "No Record found for this query: "+query, true);
			Log.info("SQLDBSelect Error : ");
			SQLDBSelect=false;
		}

		try{
			if(!rs_SQLServer.wasNull()){            // If some value is present in the fired Query
				if(Actual_Value.equalsIgnoreCase(Expected_value)){
					Report_Functions.ReportEventSuccess(doc, "1","SQLDBSelect", "The actual value : '"+Actual_Value+"' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' matches the expected value : '"+Expected_value+"'", 2);
					SQLDBSelect=true;
				}else if(!(Actual_Value.equals(Expected_value))){
					Report_Functions.ReportEventFailure(doc,  "SQLDBSelect",  "The actual value : '"+Actual_Value+"' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' does not match the expected value : '"+Expected_value+"'", true);
					SQLDBSelect=false;
				}
			}

			else if(rs_SQLServer.wasNull()){        // If "NULL" value is present in the fired Query
				if(Expected_value.equals("NULL")){
					Report_Functions.ReportEventSuccess(doc, "1", "SQLDBSelect", "The actual value 'NULL' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' matches with the expected value :'"+Expected_value+"'", 2);
					SQLDBSelect=true;
				}else if(!(Expected_value.equals("NULL"))){
					Report_Functions.ReportEventFailure(doc,  "SQLDBSelect",  "The actual value 'NULL' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' does not match the expected value : '"+Expected_value+"'", true);  	 
					SQLDBSelect=false;
				}
			}
		}catch(NullPointerException e){

			Log.info("Null pointer occurred :"+e.getMessage());

		}catch (Exception e){
			Report_Functions.ReportEventFailure(doc,  "SQLDBSelect",  "Error occured while comparing the values in SQL query.Error description is : "+ e.getMessage() +".", true);
			Log.info("SQLDBSelect Error : " + e);
			SQLDBSelect=false;
		}
		return SQLDBSelect;
	}*/

	/**
	 * 
	 * @Objective <b>Verify to comapare SQL DB environment variable</b>
	 * @param sqltablename
	 * @param strsqlcolumnname
	 * @param strsqlcondition
	 * @param Propertyfilename
	 * @param strEnvironmentVariable
	 * @param strExecEventFlag
	 * @author <b>Lakshman</b>
	 * @since <b>02-November-15</b>
	 */
	public static boolean SQLDBEnvironmentVariableCompare(String sqltablename, String strsqlcolumnname,String strsqlcondition,String strEnvironmentVariable,int strExecEventFlag)throws Exception  {

		boolean SQLDBEnvironmentVariableCompare= false;
		String query = null;
		String Table_name = null;
		String Column_name = null;
		String SQL_condition = null;
		String strExpectedvalue = null;
		String Actual_Value = null;

		try {
			if(strExecEventFlag==1){
				Table_name=RetrieveTestDataValue("SQLDBEnvironmentVariableCompare",sqltablename,strExecEventFlag);
				Column_name=RetrieveTestDataValue("SQLDBEnvironmentVariableCompare",strsqlcolumnname,strExecEventFlag);
				SQL_condition=RetrieveTestDataValue("SQLDBEnvironmentVariableCompare",strsqlcondition,strExecEventFlag);
			}

			if(Table_name==null){
				Report_Functions.ReportEventFailure(doc,  "SQLDBEnvironmentVariableCompare",  "SQL table name is not provided in the Data Sheet.", false);
				return SQLDBEnvironmentVariableCompare;
			}

			if(Column_name==null){
				Report_Functions.ReportEventFailure(doc,  "SQLDBEnvironmentVariableCompare",  "SQL Column name is not provided in the Data Sheet.", false);
				SQLDBEnvironmentVariableCompare= false;
				throw new RuntimeException ("Column_name is null");
			}

			if(SQL_condition==null){
				Report_Functions.ReportEventFailure(doc,  "SQLDBEnvironmentVariableCompare",  "SQL condition is not provided in the Data Sheet.", false);
				SQLDBEnvironmentVariableCompare= false;
				throw new RuntimeException ("SQL_condition is null");
			}

		}catch (Exception e) {
			Report_Functions.ReportEventFailure(doc,  "SQLDBEnvironmentVariableCompare",  "Error occured .Error description is : "+ e.getMessage() +".", true);
			Log.info("SQLDBEnvironmentVariableCompare Error : " + e);
			SQLDBEnvironmentVariableCompare=false;
		}


		strExpectedvalue=EnvironmentValue.getProperty(strEnvironmentVariable);

		try {
			if(strExpectedvalue==null){
				Report_Functions.ReportEventFailure(doc,  "SQLDBEnvironmentVariableCompare",  "The Value in environment variable: '"+ strEnvironmentVariable +"' is empty", true);
				SQLDBEnvironmentVariableCompare=false;
			}else{
				//Query to Execute      
				query = "select "+Column_name+" from "+Table_name+" where "+SQL_condition+"";
				System.out.println("query ------:"+query);
				rs_SQLServer= stmt.executeQuery(query);
			}
		} catch (Exception e){
			Report_Functions.ReportEventFailure(doc,  "SQLDBEnvironmentVariableCompare",  "Error occured while executing the SQL query.Error description is : "+ e.getMessage() +".", true);
			Log.info("SQLDBEnvironmentVariableCompare Error : " + e);
			SQLDBEnvironmentVariableCompare=false;
		}

		try{
			rs_SQLServer.next();
			Actual_Value = rs_SQLServer.getString(1);

		} catch (Exception NullPointerException) {           // If no record is present in the fired Query
			Report_Functions.ReportEventFailure(doc,  "SQLDBEnvironmentVariableCompare",  "No Record found for this query: "+query, true);
			Log.info("SQLDBEnvironmentVariableCompare Error : ");
			SQLDBEnvironmentVariableCompare=false;
		}

		try{

			if(!rs_SQLServer.wasNull()){            // If some value is present in the fired Query
				if(Actual_Value.equals(strExpectedvalue)){
					Report_Functions.ReportEventSuccess(doc, "1", "SQLDBEnvironmentVariableCompare", "The selected value : '"+Actual_Value+"' in the column : '"+Column_name+"' of table : '"+Table_name+"' matches with the expected value : '"+strExpectedvalue+"' ", 2);
					SQLDBEnvironmentVariableCompare=true;
				}else if(!(Actual_Value.equals(strExpectedvalue))){
					Report_Functions.ReportEventFailure(doc,  "SQLDBEnvironmentVariableCompare",  "The Selected value : '"+Actual_Value+"' in the column : '"+Column_name+"' of table : '"+Table_name+"' does not match with the expected value : '"+strExpectedvalue+"' ", true);
					SQLDBEnvironmentVariableCompare=false;
				}
			}else if(rs_SQLServer.wasNull()){        // If "NULL" value is present in the fired Query
				if(strExpectedvalue.equals("NULL")){
					Report_Functions.ReportEventSuccess(doc, "1", "SQLDBEnvironmentVariableCompare", "The actual value 'NULL' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' matches with the expected value :'"+strExpectedvalue+"'", 2);
					SQLDBEnvironmentVariableCompare=true;
				}else if(!(strExpectedvalue.equals("NULL"))){
					Report_Functions.ReportEventFailure(doc,  "SQLDBEnvironmentVariableCompare",  "The actual value 'NULL' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' does not match the expected value : '"+strExpectedvalue+"'", true);  	 
					SQLDBEnvironmentVariableCompare=false;
				}
			}
		}catch (Exception e) {
			Report_Functions.ReportEventFailure(doc,  "SQLDBEnvironmentVariableCompare",  "Error occured while comparing the values in SQL Query.Error description is : "+ e.getMessage() +".", true);
			Log.info("SQLDBEnvironmentVariableCompare Error : " + e);
			SQLDBEnvironmentVariableCompare=false;
		}
		return SQLDBEnvironmentVariableCompare;
	}

	/**
	 * 
	 * @Objective <b>Verifies to check value is exist in SQL DB</b>
	 * @param sqltablename
	 * @param strsqlcolumnname
	 * @param strsqlcondition
	 * @param strExecEventFlag
	 * @author <b>Lakshman</b>
	 * @since <b>02-November-15</b>
	 */
	public static boolean SQLDBCheckValueExist(String sqltablename, String strsqlcolumnname,String strsqlcondition,int strExecEventFlag)throws Exception  {
		boolean SQLDBCheckValueExist= false;
		String query=null;
		String Table_name = null;
		String Column_name = null;
		String SQL_condition = null;
		String Actual_Value = null;
		try {
			if(strExecEventFlag==1){
				Table_name=RetrieveTestDataValue("SQLDBCheckValueExist",sqltablename,strExecEventFlag);
				Column_name=RetrieveTestDataValue("SQLDBCheckValueExist",strsqlcolumnname,strExecEventFlag);
				SQL_condition=RetrieveTestDataValue("SQLDBCheckValueExist",strsqlcondition,strExecEventFlag);
			}

			if(Table_name==null){
				Report_Functions.ReportEventFailure(doc,  "SQLDBCheckValueExist",  "SQL table name is not provided in the Data Sheet.", false);
				SQLDBCheckValueExist= false;
				throw new RuntimeException ("Table_name is null");
			}

			if(Column_name==null){
				Report_Functions.ReportEventFailure(doc,  "SQLDBCheckValueExist",  "SQL Column name is not provided in the Data Sheet.", false);
				SQLDBCheckValueExist= false;
				throw new RuntimeException ("Column_name is null");
			}

			if(SQL_condition==null){
				Report_Functions.ReportEventFailure(doc,  "SQLDBCheckValueExist",  "SQL condition is not provided in the Data Sheet.", false);
				SQLDBCheckValueExist= false;
				throw new RuntimeException ("SQL_condition is null");
			}

			//Query to Execute      
			query = "select "+Column_name+" from "+Table_name+" where "+SQL_condition+"";
			rs_SQLServer= stmt.executeQuery(query);

		} catch (Exception e) {
			Report_Functions.ReportEventFailure(doc,  "SQLDBCheckValueExist",  "Error occured while executing the Eshop SQL query.Error description is : "+ e.getMessage() +".", true);
			Log.info("SQLDBCheckValueExist Error : " + e);
			SQLDBCheckValueExist=false;
		}

		try{
			rs_SQLServer.next();
			Actual_Value = rs_SQLServer.getString(1);
		} catch (Exception NullPointerException) {           // If no record is present in the fired Query
			Report_Functions.ReportEventFailure(doc,  "SQLDBCheckValueExist",  "No Record found for this query: "+query, true);
			Log.info("EShopSQLDBCheckValueExist Error : ");
			SQLDBCheckValueExist=false;
		}

		try{
			if(!rs_SQLServer.wasNull()){            // If some value is present in the fired Query
				if(!(Actual_Value==null)){
					Report_Functions.ReportEventSuccess(doc, "1", "SQLDBCheckValueExist", "The Value '"+Actual_Value+"' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' exists. ", 2);
					SQLDBCheckValueExist=true;
				}else if(Actual_Value==null){
					Report_Functions.ReportEventFailure(doc,  "SQLDBCheckValueExist",  "The Value in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' does not exists. ", true);
					SQLDBCheckValueExist=false;
				}
			}else if(rs_SQLServer.wasNull()){        // If "NULL" value is present in the fired Query
				Report_Functions.ReportEventFailure(doc,  "SQLDBCheckValueExist",  "The value 'NULL'  is present in the column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"'", true);  	 
				SQLDBCheckValueExist=false;
			}
		} catch (Exception e) {
			Report_Functions.ReportEventFailure(doc,  "SQLDBCheckValueExist",  "Error occured while checking whether the executed EShop query is having any record (or) not. Error description is : "+ e.getMessage() +".", true);
			Log.info("SQLDBCheckValueExist Error : " + e);
			SQLDBCheckValueExist=false;
		}
		return SQLDBCheckValueExist;
	}

	/**
	 * 
	 * @Objective <b>Verifies to check date comparision in SQL DB</b>
	 * @param sqltablename
	 * @param strsqlcolumnname
	 * @param strsqlcondition
	 * @param Date_Format
	 * @param strExecEventFlag
	 * @author <b>Lakshman</b>
	 * @since <b>02-November-15</b>
	 */
	public static boolean SQLDBDateCompare(String sqltablename, String strsqlcolumnname,String strsqlcondition,String Date_Format,int strExecEventFlag)throws Exception  {
		boolean SQLDBDateCompare= false;
		String query = null;  
		String Table_name = null;
		String Column_name = null;
		String SQL_condition = null;
		String Expected_value = null;
		String Actual_Value = null;
		String Current_Date=null;
		String expected_db_Date = null;

		try {
			if(strExecEventFlag==1){
				Table_name=RetrieveTestDataValue("SQLDBDateCompare",sqltablename,strExecEventFlag);
				Column_name=RetrieveTestDataValue("SQLDBDateCompare",strsqlcolumnname,strExecEventFlag);
				SQL_condition=RetrieveTestDataValue("SQLDBDateCompare",strsqlcondition,strExecEventFlag);
			}

			if(Table_name==null){
				Report_Functions.ReportEventFailure(doc,  "SQLDBDateCompare",  "SQL table name is not provided in the Data Sheet.", false);
				SQLDBDateCompare= false;
				throw new RuntimeException ("Table_name is null");
			}

			if(Column_name==null){
				Report_Functions.ReportEventFailure(doc,  "SQLDBDateCompare",  "SQL Column name is not provided in the Data Sheet.", false);
				SQLDBDateCompare= false;
				throw new RuntimeException ("Column_name is null");
			}

			if(SQL_condition==null){
				Report_Functions.ReportEventFailure(doc,  "SQLDBDateCompare",  "SQL condition is not provided in the Data Sheet.", false);
				SQLDBDateCompare= false;
				throw new RuntimeException ("SQL_condition is null");
			}

			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date date = new Date();
			Current_Date = dateFormat.format(date);
			Expected_value = Current_Date.trim();

			// Query to Execute      
			query = "select "+Column_name+" from "+Table_name+" where "+SQL_condition+"";
			rs_SQLServer= stmt.executeQuery(query);

		}catch (Exception e){
			Report_Functions.ReportEventFailure(doc,  "SQLDBDateCompare",  "Error occured while executing the SQL query.Error description is : "+ e.getMessage() +".", true);
			Log.info("SQLDBDateCompare Error : " + e);
			SQLDBDateCompare=false;
		}

		try{
			rs_SQLServer.next();
			Actual_Value = rs_SQLServer.getString(1);
			String db_Date = Actual_Value.split(" ")[0];

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
			Date dateToChange = dateFormat.parse(db_Date);

			SimpleDateFormat finalDateFormat = new SimpleDateFormat(Date_Format);
			expected_db_Date = finalDateFormat.format(dateToChange);	

		} catch (Exception NullPointerException) {           // If no record is present in the fired Query
			Report_Functions.ReportEventFailure(doc,  "SQLDBDateCompare",  "No Record found for this query: "+query, true);
			Log.info("SQLDBDateCompare Error : ");
			SQLDBDateCompare=false;
		}

		try{

			if(!rs_SQLServer.wasNull()){            // If some value is present in the fired Query
				if(expected_db_Date.equals(Expected_value)){
					Report_Functions.ReportEventSuccess(doc, "1", "SQLDBDateCompare", "The selected date : '"+expected_db_Date+"' in the column : '"+Column_name+"' of table : '"+Table_name+"' matches with the expected date : '"+Expected_value+"' ", 2);
					SQLDBDateCompare=true;
				}else if(!(expected_db_Date.equals(Expected_value))){
					Report_Functions.ReportEventFailure(doc,  "SQLDBDateCompare",  "The selected date : '"+expected_db_Date+"' in the column : '"+Column_name+"' of table : '"+Table_name+"' does not match with the expected date : '"+Expected_value+"' ", true);
					SQLDBDateCompare=false;
				}
			}else if(rs_SQLServer.wasNull()){        // If "NULL" value is present in the fired Query
				if(Expected_value.equals("NULL")){
					Report_Functions.ReportEventSuccess(doc, "1", "SQLDBDateCompare", "The actual value 'NULL' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' matches with the expected value :'"+Expected_value+"'", 2);
					SQLDBDateCompare=true;
				}else if(!(Expected_value.equals("NULL"))){
					Report_Functions.ReportEventFailure(doc,  "SQLDBDateCompare",  "The actual value 'NULL' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' does not match the expected value : '"+Expected_value+"'", true);  	 
					SQLDBDateCompare=false;
				}
			}

		}catch (Exception e){
			Report_Functions.ReportEventFailure(doc,  "SQLDBDateCompare",  "Error occured while comparing the dates in SQL query.Error description is : "+ e.getMessage() +".", true);
			Log.info("SQLDBDateCompare Error : " + e);
			SQLDBDateCompare=false;
		}
		return SQLDBDateCompare;
	}

	/**
	 * 
	 * @Objective <b>Verifies to retrieve the value for environment variable in SQL DB</b>
	 * @param sqltablename
	 * @param strsqlcolumnname
	 * @param sqlcondition
	 * @param strEnvironmentVariable
	 * @param strExecEventFlag
	 * @author <b>Lakshman</b>
	 * @since <b>02-November-15</b>
	 */
	public static boolean RetrieveSQLValueStoresInEnvVar(String sqltablename, String strsqlcolumnname,String sqlcondition,String strEnvironmentVariable,int strExecEventFlag)throws Exception  {
		boolean RetrieveSQLValueStoresInEnvVar= false;
		String query = null;
		String Table_name = null;
		String Column_Name = null;
		String SQL_condition = null;
		String Actual_Value = null;
		try {
			if(strExecEventFlag==1){
				Table_name=RetrieveTestDataValue("RetrieveSQLValueStoresInEnvVar",sqltablename,strExecEventFlag);
				Column_Name=RetrieveTestDataValue("RetrieveSQLValueStoresInEnvVar",strsqlcolumnname,strExecEventFlag);
				SQL_condition=RetrieveTestDataValue("RetrieveSQLValueStoresInEnvVar",sqlcondition,strExecEventFlag);
			}

			if(Table_name==null){
				Report_Functions.ReportEventFailure(doc,  "RetrieveSQLValueStoresInEnvVar",  "SQL table name is not provided in the Data Sheet.", false);
				RetrieveSQLValueStoresInEnvVar= false;
				throw new RuntimeException ("Table name is null");
			}

			if(Column_Name==null){
				Report_Functions.ReportEventFailure(doc,  "RetrieveSQLValueStoresInEnvVar",  "SQL Column Name is not provided in the Data Sheet.", false);
				RetrieveSQLValueStoresInEnvVar= false;
				throw new RuntimeException ("Column Name is null");
			}

			if(SQL_condition==null){
				Report_Functions.ReportEventFailure(doc,  "RetrieveSQLValueStoresInEnvVar",  "SQL condition is not provided in the Data Sheet.", false);
				RetrieveSQLValueStoresInEnvVar= false;
				throw new RuntimeException ("SQL condition is null");
			}

			//Query to Execute
			query = "select "+Column_Name+" from "+Table_name+" where "+SQL_condition+"";
			rs_SQLServer= stmt.executeQuery(query);

		} catch (Exception e) {
			Report_Functions.ReportEventFailure(doc,  "RetrieveSQLValueStoresInEnvVar",  "Error occured while executing the SQL query.Error description is : "+ e.getMessage() +".", true);
			Log.info("RetrieveSQLValueStoresInEnvVar Error : " + e);
			RetrieveSQLValueStoresInEnvVar=false;
		}

		try{
			rs_SQLServer.next();
			Actual_Value = rs_SQLServer.getString(1);
		}catch(Exception e){
			Report_Functions.ReportEventFailure(doc,  "RetrieveSQLValueStoresInEnvVar",  "Error has occured. Error description is :"+e.getMessage(), true);
			RetrieveSQLValueStoresInEnvVar=false;
		}	
		try{
			EnvironmentValue.setProperty(strEnvironmentVariable, Actual_Value);
		}catch(Exception e){
			Report_Functions.ReportEventFailure(doc,  "RetrieveSQLValueStoresInEnvVar",  "Error occured while storing the value :'"+Actual_Value+"' in the Environment variable '"+strEnvironmentVariable+"'", true);
			RetrieveSQLValueStoresInEnvVar=false;
		}	

		try{
			if(!(Actual_Value==null)){
				Report_Functions.ReportEventSuccess(doc, "1", "RetrieveSQLValueStoresInEnvVar", "The Value:'" +Actual_Value+ "' is stored in the Environment Variable: '"+ strEnvironmentVariable +"' successfully", 2);
				RetrieveSQLValueStoresInEnvVar=true;
			}else{
				Report_Functions.ReportEventFailure(doc,  "RetrieveSQLValueStoresInEnvVar",  "No Value is stored in Environment Variable: '"+ strEnvironmentVariable +"'", true);
				RetrieveSQLValueStoresInEnvVar=false;
			}
		} catch (Exception e) {
			Report_Functions.ReportEventFailure(doc,  "RetrieveSQLValueStoresInEnvVar",  "Error occured while checking whether the SQL query value '"+Actual_Value+"' is null (or) not", true);
			Log.info("RetrieveSQLValueStoresInEnvVar Error : " + e);
			RetrieveSQLValueStoresInEnvVar=false;
		}
		return RetrieveSQLValueStoresInEnvVar;
	}

	/**
	 * 
	 * @Objective <b>To delete the files in folder</b>
	 * @param strFilePath
	 * @param strFileName
	 * @param strExecEventFlag
	 * @author <b>Lakshman</b>
	 * @since <b>02-November-15</b>
	 */
	public static boolean Files_Delete_in_Folder(String strFilePath, String strFileName,int strExecEventFlag) throws Exception  {
		boolean Files_Delete_in_Folder= false;
		String File_Name = null;
		String File_Name_Actual = null;
		String strFolder = null;
		int flag = 0;

		try {
			if(strExecEventFlag==1){
				File_Name=RetrieveTestDataValue("Files_Delete_in_Folder",strFileName,strExecEventFlag);
			}
		} catch (Exception e) {
			Report_Functions.ReportEventFailure(doc,  "Files_Delete_in_Folder",  "Function parameters are not provided correctly for the function 'Files_Delete_in_Folder'", true);
			Log.info("Files_Delete_in_Folder Error : " + e);
			Files_Delete_in_Folder=false;
		}

		try{
			strFolder = Param.getProperty(strFilePath);
		} catch (Exception e) {
			Report_Functions.ReportEventFailure(doc,  "Files_Delete_in_Folder",  "The File path '"+strFilePath+"' is empty in the function parameter 'Files_Delete_in_Folder'", true);
			Log.info("Files_Delete_in_Folder Error : " + e);
			Files_Delete_in_Folder=false;
		}

		try {
			File[] files = new File(strFolder).listFiles();

			for (File file:files) {
				if (file.isFile()) {
					File_Name_Actual = file.getName();
					if(File_Name_Actual.startsWith(File_Name)){
						file.delete();
						flag = 1;
					}
				}
			}

		} catch (Exception e) {
			Report_Functions.ReportEventFailure(doc,  "Files_Delete_in_Folder",  "Error occured while deleting the files '"+File_Name+"' in the location '"+strFolder+"'.Error description is : "+ e.getMessage() +".", false);
			Files_Delete_in_Folder=false;
			Log.info("Files_Delete_in_Folder Error : " + e);
		}

		try {
			if(flag == 1){
				Report_Functions.ReportEventSuccess(doc, "1", "Files_Delete_in_Folder", "The log files under the name '"+File_Name+"' are deleted successfully in the location '"+strFolder+"'", 2);
				Files_Delete_in_Folder=true;
			} else if(flag == 0){
				Report_Functions.ReportEventSuccess(doc, "1", "Files_Delete_in_Folder", "No log files under the name '"+File_Name+"' are present in the location '"+strFolder+"'", 2);
				Files_Delete_in_Folder=true;
			}
		} catch (Exception e) {
			Report_Functions.ReportEventFailure(doc,  "Files_Delete_in_Folder",  "Error occured while validating the function 'Files_Delete_in_Folder'. Error description is : "+ e.getMessage() +".", false);
			Files_Delete_in_Folder=false;
			Log.info("Files_Delete_in_Folder Error : " + e);
		}
		return Files_Delete_in_Folder;
	}

	/**
	 * 
	 * @Objective <b>Verify the SMS log content for registration in DB(enter button)</b>
	 * @param strFilePath
	 * @param strFileName
	 * @param strPatternString
	 * @param strExecEventFlag
	 * @author <b>Lakshman</b>
	 * @since <b>02-November-15</b>
	 */
	public static boolean Registration_SMSLog_Content_Verify(String strFilePath, String strFileName,String strPatternString,int strExecEventFlag) throws Exception  {
		boolean Registration_SMSLog_Content_Verify= false;
		String File_Name = null;
		String File_Name_Actual = null;
		String strFolder = null;
		String Pattern_String = null;
		String File_to_be_Opened = null;
		String sReadTxt = null;
		Matcher matchedPattern = null;
		boolean matchedStatus=false;

		try {
			if(strExecEventFlag==1){
				File_Name=RetrieveTestDataValue("Registration_SMSLog_Content_Verify",strFileName,strExecEventFlag);
				Pattern_String=RetrieveTestDataValue("Registration_SMSLog_Content_Verify",strPatternString,strExecEventFlag);
			}
		} catch (Exception e) {
			Report_Functions.ReportEventFailure(doc,  "Registration_SMSLog_Content_Verify",  "Function parameters are not provided correctly for the function 'Registration_SMSLog_Content_Verify'", true);
			Log.info("Registration_SMSLog_Content_Verify Error : " + e);
			Registration_SMSLog_Content_Verify=false;
		}

		try{
			strFolder = Param.getProperty(strFilePath);
		} catch (Exception e) {
			Report_Functions.ReportEventFailure(doc,  "Registration_SMSLog_Content_Verify",  "The File path '"+strFilePath+"' is empty in the function parameter 'Registration_SMSLog_Content_Verify'", true);
			Log.info("Registration_SMSLog_Content_Verify Error : " + e);
			Registration_SMSLog_Content_Verify=false;
		}

		FileReader r=null;
		BufferedReader br=null;

		try {
			File[] files = new File(strFolder).listFiles();
			for (File file : files) {
				if (file.isFile()) {
					File_Name_Actual = file.getName();
					if(File_Name_Actual.startsWith(File_Name)){
						File_to_be_Opened = File_Name_Actual;
					}
				}
			}

			r = new FileReader(strFolder+File_to_be_Opened);
			br = new BufferedReader(r);	
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			sReadTxt = sb.toString().trim();

			Pattern expPattern=Pattern.compile(Pattern_String);
			matchedPattern = expPattern.matcher(sReadTxt);
			matchedStatus=matchedPattern.find();

		} catch (Exception e) {
			Report_Functions.ReportEventFailure(doc,  "Registration_SMSLog_Content_Verify",  "Error occured while reading the file '"+File_to_be_Opened+"' in the location '"+strFolder+"'.Error description is : "+ e.getMessage() +".", false);
			Registration_SMSLog_Content_Verify=false;
			Log.info("Registration_SMSLog_Content_Verify Error : " + e);
		}finally{
			r=null;
			br.close();
			br=null;
		}
		try {
			if(matchedStatus==true){
				Report_Functions.ReportEventSuccess(doc, "1", "Registration_SMSLog_Content_Verify", "The Expected pattern:'"+Pattern_String+"' matches with the actual pattern '"+matchedPattern.group()+"' in the log file '"+strFolder+File_to_be_Opened+"'", 2);
				Registration_SMSLog_Content_Verify=true;
			} else{
				Report_Functions.ReportEventFailure(doc,  "Registration_SMSLog_Content_Verify",  "The Expected pattern:'"+Pattern_String+"' does not match with the actual pattern '"+matchedPattern.group()+"' in the log file '"+strFolder+File_to_be_Opened+"'", false);	
				Registration_SMSLog_Content_Verify=false;
			}
		} catch (Exception e) {
			Report_Functions.ReportEventFailure(doc,  "Registration_SMSLog_Content_Verify",  "Error occured while validating the function 'Registration_SMSLog_Content_Verify'. Error description is : "+ e.getMessage() +".", false);
			Registration_SMSLog_Content_Verify=false;
			Log.info("Registration_SMSLog_Content_Verify Error : " + e);
		}
		return Registration_SMSLog_Content_Verify;
	}

	/**
	 * 
	 * @Objective <b>Verifies to update the data for RRBS in SQL DB</b>
	 * @param sqltablename
	 * @param strsqlcolumnname
	 * @param strsqlcolumnvalue
	 * @param strsqlcondition
	 * @param strExecEventFlag
	 * @author <b>Lakshman</b>
	 * @since <b>02-November-15</b>
	 */
	public static boolean RRBSDBUpdate(String sqltablename, String strsqlcolumnname,String strsqlcolumnvalue,String strsqlcondition,int strExecEventFlag)throws Exception  {

		boolean elementStatus= false;
		String Table_name = null;
		String Column_name = null;
		String Column_Value = null;
		String SQL_condition = null;

		try {
			if(strExecEventFlag==1){
				Table_name=RetrieveTestDataValue("RRBSDBUpdate",sqltablename,strExecEventFlag);
				Column_name=RetrieveTestDataValue("RRBSDBUpdate",strsqlcolumnname,strExecEventFlag);
				Column_Value=RetrieveTestDataValue("RRBSDBUpdate",strsqlcolumnvalue,strExecEventFlag);
				SQL_condition=RetrieveTestDataValue("RRBSDBUpdate",strsqlcondition,strExecEventFlag);
			}

			if(Table_name==null){
				Report_Functions.ReportEventFailure(doc,  "RRBSDBUpdate",  "RRBS table name is not provided in the Data Sheet.", false);
				elementStatus= false;
				throw new RuntimeException ("Table_name is null");
			}

			if(Column_name==null){
				Report_Functions.ReportEventFailure(doc,  "RRBSDBUpdate",  "RRBS Column name is not provided in the Data Sheet.", false);
				elementStatus= false;
				throw new RuntimeException ("Column_name is null");
			}

			if(Column_Value==null){
				Report_Functions.ReportEventFailure(doc,  "RRBSDBUpdate",  "RRBS Column value is not provided in the Data Sheet.", false);
				elementStatus= false;
				throw new RuntimeException ("Column_Value is null");
			}

			if(SQL_condition==null){
				Report_Functions.ReportEventFailure(doc,  "RRBSDBUpdate",  "RRBS condition is not provided in the Data Sheet.", false);

				return false;
			}

			//Query to Execute      
			String query = "update "+Table_name+" set "+Column_name+"="+Column_Value+" where "+SQL_condition;
			rrbsstatement.execute(query); 
			elementStatus=true;
			Report_Functions.ReportEventSuccess(doc, "1", "RRBSDBUpdate", "The RRBS Update Query : "+ query + " executed successfully.", 2);

		} catch (Exception e) { 
			elementStatus=false;
			Report_Functions.ReportEventFailure(doc,  "RRBSDBUpdate",  "Error occured while executing the SQL query.Error description is : "+ e.getMessage() +".", true);
			Log.info("RRBSDBUpdate Error : " + e);
		}
		return elementStatus;
	}

	/**
	 * 
	 * @Objective <b>Opening EXIBS DB Connection</b>
	 * @param dbserver
	 * @param portnumber
	 * @param dbname
	 * @param dbusername
	 * @param dbpassword
	 * @author <b>Lakshman</b>
	 * @since <b>02-November-15</b>
	 */

	public static boolean EXIBSDBOpenConnection(String dbserver, String portnumber, String dbname, String dbusername, String dbpassword)throws Exception  {
		boolean elementStatus= false;

		String serverName = Param.getProperty(dbserver);
		String portNumber = Param.getProperty(portnumber);
		String sid = Param.getProperty(dbname);


		//Connection URL Syntax: "jdbc:oracle:thin:@://ipaddress:portnumber:db_name"        
		String dbUrl = "jdbc:oracle:thin:@" + serverName + ":" + portNumber + ":" + sid;                  
		//Database Username     
		String username = Param.getProperty(dbusername);   
		//Database Password     
		String password = Param.getProperty(dbpassword); 
		try {
			// Load the JDBC driver
			String driverName = "oracle.jdbc.OracleDriver";
			Class.forName(driverName);         
			//Create Connection to DB       
			exibsconnection = DriverManager.getConnection(dbUrl, username, password);
			//Create Statement Object       
			exibsstatement = exibsconnection.createStatement(); 
			elementStatus=true;
			Report_Functions.ReportEventSuccess(doc, "1", "EXIBSDBOpenConnection", "EXIBS DB Connection is established Successfully.", 2);

		} catch (Exception e) { 
			elementStatus=false;
			Report_Functions.ReportEventFailure(doc,  "EXIBSDBOpenConnection",  "Error occured while connecting to the EXIBS DB Server.Error description is : "+ e.getMessage() +".", false);
			Log.info("EXIBSDBOpenConnection Error : " + e);
		}
		return elementStatus;
	}

	/**
	 * 
	 * @Objective <b>Deleting the record from RRBS DB</b>
	 * @param exibstablename
	 * @param exibscondition
	 * @param strExecEventFlag
	 * @author <b>Lakshman</b>
	 * @since <b>02-November-15</b>
	 */
	public static boolean EXIBSDBDelete(String exibstablename, String exibscondition, int strExecEventFlag)throws Exception  {
		boolean elementStatus= false;
		String tablename = null;
		String condition = null;

		try{

			if(strExecEventFlag==1){
				tablename=RetrieveTestDataValue("RRBSDBDelete",exibstablename,strExecEventFlag);
				condition=RetrieveTestDataValue("RRBSDBDelete",exibscondition,strExecEventFlag);
			}

			if(tablename==null || condition==null){
				throw new RuntimeException ("strData is null");
			}

		}catch(Exception e){
			Log.info("Errror log in reading file is "+e);
			throw e;
		}
		//Query to Execute      
		String query = "Delete from "+ tablename +" where "+ condition;
		System.out.println("rrbs delete:"+query);
		try {
			exibsstatement.execute(query); 
			elementStatus=true;
			Report_Functions.ReportEventSuccess(doc, "1", "EXIBSDBDelete", "The EXIBS Query : "+ query + " executed successfully.", 2);

		} catch (Exception e) { 
			elementStatus=false;
			Report_Functions.ReportEventFailure(doc,  "EXIBSDBDelete",  "Error occured while executing the EXIBS query.Error description is : "+ e.getMessage() +".", false);
			Log.info("EXIBSDBDelete Error : " + e);
		}
		return elementStatus;
	}

	/**
	 * 
	 * @Objective <b>Verifying the column value in RRBS DB</b>
	 * @param exibstablename
	 * @param exibscolumnname
	 * @param exibscondition
	 * @param exibscolumnvalue
	 * @param strExecEventFlag
	 * @author <b>Lakshman</b>
	 * @since <b>02-November-15</b>
	 */
	public static boolean EXIBSDBSelect(String exibstablename, String exibscolumnname, String exibscondition, String exibscolumnvalue, int strExecEventFlag)throws Exception  {
		boolean elementStatus= false;
		String tablename = null;
		String condition = null;
		String columnname = null;
		String columnvalue = null;
		String actualvalue = null;
		try{

			if(strExecEventFlag==1){
				tablename=RetrieveTestDataValue("EXIBSDBSelect",exibstablename,strExecEventFlag);
				condition=RetrieveTestDataValue("EXIBSDBSelect",exibscondition,strExecEventFlag);
				columnname=RetrieveTestDataValue("EXIBSDBSelect",exibscolumnname,strExecEventFlag);
				columnvalue=RetrieveTestDataValue("EXIBSDBSelect",exibscolumnvalue,strExecEventFlag);
			}

			if(tablename==null || condition==null || columnname==null ){
				throw new RuntimeException ("strData is null");
			}

		}catch(Exception e){
			Log.info("Errror log in reading file is "+e);
			throw e;
		}
		//Query to Execute      
		String query = "select "+ columnname +" from "+ tablename +" where "+ condition;

		try {
			ResultSet exibsresultset = exibsstatement.executeQuery(query);
			while (exibsresultset.next()){
				actualvalue = exibsresultset.getString(1);
			}
			if(actualvalue.equalsIgnoreCase(columnvalue))
			{
				elementStatus=true;
				Report_Functions.ReportEventSuccess(doc, "1", "EXIBSDBSelect", "The actual value : "+ actualvalue + " matches with the expected value :"+ columnvalue +".", 2);
			} else {
				elementStatus=false;
				Report_Functions.ReportEventFailure(doc,  "EXIBSDBSelect",  "The actual value : "+ actualvalue + " doesnot matches with the expected value :"+ columnvalue +".", false);	
			}
		} catch (Exception e) { 
			elementStatus=false;
			Report_Functions.ReportEventFailure(doc,  "EXIBSDBSelect",  "Error occured while executing the EXIBS query.Error description is : "+ e.getMessage() +".", false);
			Log.info("EXIBSDBSelect Error : " + e);
		}
		return elementStatus;
	}

	/**
	 * 
	 * @Objective <b>Verifies the update value in RRBS DB</b>
	 * @param sqltablename
	 * @param strsqlcolumnname
	 * @param strsqlcolumnvalue
	 * @param strsqlcondition
	 * @param strExecEventFlag
	 * @author <b>Lakshman</b>
	 * @since <b>02-November-15</b>
	 */
	public static boolean EXIBSDBUpdate(String sqltablename, String strsqlcolumnname,String strsqlcolumnvalue,String strsqlcondition,int strExecEventFlag)throws Exception  {

		boolean elementStatus = false;
		String Table_name = null;
		String Column_name = null;
		String Column_Value = null;
		String SQL_condition = null;

		try {
			if(strExecEventFlag==1){
				Table_name=RetrieveTestDataValue("EXIBSDBUpdate",sqltablename,strExecEventFlag);
				Column_name=RetrieveTestDataValue("EXIBSDBUpdate",strsqlcolumnname,strExecEventFlag);
				Column_Value=RetrieveTestDataValue("EXIBSDBUpdate",strsqlcolumnvalue,strExecEventFlag);
				SQL_condition=RetrieveTestDataValue("EXIBSDBUpdate",strsqlcondition,strExecEventFlag);
			}

			if(Table_name==null){
				Report_Functions.ReportEventFailure(doc,  "EXIBSDBUpdate",  "EXIBS table name is not provided in the Data Sheet.", false);
				elementStatus= false;
				throw new RuntimeException ("Table_name is null");
			}

			if(Column_name==null){
				Report_Functions.ReportEventFailure(doc,  "EXIBSDBUpdate",  "EXIBS Column name is not provided in the Data Sheet.", false);
				elementStatus= false;
				throw new RuntimeException ("Column_name is null");
			}

			if(Column_Value==null){
				Report_Functions.ReportEventFailure(doc,  "EXIBSDBUpdate",  "EXIBS Column value is not provided in the Data Sheet.", false);
				elementStatus= false;
				throw new RuntimeException ("Column_Value is null");
			}

			if(SQL_condition==null){
				Report_Functions.ReportEventFailure(doc,  "EXIBSDBUpdate",  "EXIBS condition is not provided in the Data Sheet.", false);
				elementStatus= false;
				throw new RuntimeException ("SQL_condition is null");
			}

			//Query to Execute      
			String query = "update "+Table_name+" set "+Column_name+"="+Column_Value+" where "+SQL_condition;
			exibsstatement.execute(query); 
			elementStatus=true;
			Report_Functions.ReportEventSuccess(doc, "1", "EXIBSDBUpdate", "The EXIBS Update Query : "+ query + " executed successfully.", 2);

		} catch (Exception e) { 
			elementStatus=false;
			Report_Functions.ReportEventFailure(doc,  "EXIBSDBUpdate",  "Error occured while executing the SQL query.Error description is : "+ e.getMessage() +".", true);
			Log.info("EXIBSDBUpdate Error : " + e);
		}
		return elementStatus;
	}

	/**
	 * 
	 * @Objective <b>Verifies closing RRBS DB connection</b>
	 * @author <b>Lakshman</b>
	 * @since <b>02-November-15</b>
	 */
	public static boolean EXIBSDBCloseConnection()throws Exception  {
		boolean elementStatus= false;

		try {
			// closing DB Connection       
			exibsconnection.close(); 
			elementStatus=true;
			Report_Functions.ReportEventSuccess(doc, "1", "EXIBSDBCloseConnection", "The EXIBS DB Connection disconnected successfully.", 2);

		} catch (Exception e) { 
			elementStatus=false;
			Report_Functions.ReportEventFailure(doc,  "EXIBSDBCloseConnection",  "Error occured while closing the EXIBS DB.Error description is : "+ e.getMessage() +".", false);
			Log.info("EXIBSDBCloseConnection Error : " + e);
		}
		return elementStatus;
	}

	/**
	 * 
	 * @Objective <b>This method is to change the values in web.config file</b>
	 * @param filePath
	 * @param attributeKey
	 * @param validInvalidKey
	 * @param attributeValue
	 * @param validInvalidValue
	 * @param strExecEventFlag
	 * @author <b>Praveen Lakshmanan</b>
	 * @since <b>26-April-16</b>
	 */
	public static boolean XMLValueUpdate(String filePath, String key, String value, int strExecEventFlag) throws Exception{

		boolean elementStatus = false;

		String Path = null;
		String validKey = null;
		String validValue = null;
		String prevValue = null;
		String newValue = null;

		try{

			if(strExecEventFlag == 1){

				Path = Param.getProperty(RetrieveTestDataValue("XMLValueUpdate", filePath, strExecEventFlag));
				validKey = RetrieveTestDataValue("XMLValueUpdate", key, strExecEventFlag);
				validValue = RetrieveTestDataValue("XMLValueUpdate", value, strExecEventFlag);

				/*System.out.println("Path :"+Path);
				System.out.println("key :"+validKey);
				System.out.println("value :"+validValue);*/

			}

			if(Path == null){

				Report_Functions.ReportEventFailure(doc,  "XMLValueUpdate",  "Path is not provided in the Data Sheet.", false);
				elementStatus = false;
				throw new RuntimeException("Path is null");

			}

			if(validKey == null){

				Report_Functions.ReportEventFailure(doc,  "XMLValueUpdate",  "Key detail is not provided in the Data Sheet.", false);
				elementStatus = false;
				throw new RuntimeException("Valid/Invalid detail is null");
			}

			if(validValue == null){

				Report_Functions.ReportEventFailure(doc,  "XMLValueUpdate",  "Value is not provided in the Data Sheet.", false);
				elementStatus = false;
				throw new RuntimeException("Value is null");
			}

			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			File input = new File("//\\" +Path);

			Document doc = builder.parse(input);
			xpath = XPathFactory.newInstance().newXPath();

			//Xpath Expression
			String expression = "//*[@key='"+validKey+"']";
			//Using node and elements to get the all child attributes 
			nodeList = (NodeList) xpath.compile(expression).evaluate(doc, XPathConstants.NODESET);

			for(int i = 0; i<nodeList.getLength(); i++){

				Node nNode = nodeList.item(i);
				if(nNode.getNodeType() == Node.ELEMENT_NODE){

					Element eElement = (Element) nNode;
					if(eElement.getAttribute("value") != null){

						prevValue = eElement.getAttribute("value");
						Log.info("Current Value :"+prevValue);

						eElement.setAttribute("value", validValue);

						newValue = eElement.getAttribute("value");

						Log.info("Updated Value :"+newValue);

					} else {

						Log.info("Given attribute value is not available");

					}

				}

				// write the content into config file

				TransformerFactory transFormerFactory = TransformerFactory.newInstance();
				Transformer transFormer = transFormerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(input);
				transFormer.transform(source, result);
				elementStatus = true;

			}

		}catch(XPathExpressionException | ParserConfigurationException | SAXException | IOException e){

			e.printStackTrace();
			elementStatus = false;

		}catch(Exception e){

			e.printStackTrace();
			elementStatus = false;

		}

		if(elementStatus){
			Report_Functions.ReportEventSuccess(doc,"1","","XML value '"+newValue+"' is updated for key '"+validKey+"' successfully",3);
		}else{
			Report_Functions.ReportEventFailure(doc,"","XML value '"+newValue+"' is not updated for key '"+validKey+"'" , true);
		}

		return elementStatus;
	}

	/**
	 * 
	 * @Objective <b>Open Eshop SQL Connection</b>
	 * @author <b>Muralimohan M</b>
	 * @since <b>09-May-2016</b>
	 * */
	public static boolean EshopSQLDBOpenConnection()throws Exception  {
		boolean elementStatus= false;
		//Connection URL Syntax: "jdbc:mysql://ipaddress:portnumber/db_name"        
		String dbUrl = "jdbc:sqlserver://"+ Param.getProperty("ESHOP_SQL_Server") +";DatabaseName=" + Param.getProperty("ESHOP_SQL_Server_DB_Name") +";";                  
		//Database Username     
		String username = Param.getProperty("ESHOP_SQL_Server_UID");   
		//Database Password     
		String password = Param.getProperty("ESHOP_SQL_Server_PWD"); 
		try {
			//Load mysql jdbc driver        
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");         
			//Create Connection to DB       
			EshopConnection = DriverManager.getConnection(dbUrl,username,password);
			//Create Statement Object       
			EShopstmt = EshopConnection.createStatement(); 
			elementStatus=true;
			Report_Functions.ReportEventSuccess(doc, "1", "EshopSQLDBOpenConnection", "Eshop SQL Connection is established Successfully.", 2);

		} catch (Exception e) { 
			elementStatus=false;
			Report_Functions.ReportEventFailure(doc,  "EshopSQLDBOpenConnection",  "Error occured while connecting to the SQL Server.Error description is : "+ e.getMessage() +".", false);
			Log.info("EshopSQLDBOpenConnection Error : " + e);
		}
		return elementStatus;
	}

	/**
	 * 
	 * @Objective <b>Verifies the Eshop SQL DB Update</b>
	 * @param sqltablename
	 * @param strsqlcolumnname
	 * @param strsqlcolumnvalue
	 * @param strsqlcondition
	 * @param strExecEventFlag
	 * @author <b>Muralimohan M</b>
	 * @since <b>09-May-16</b>
	 */
	public static boolean EshopSQLDBUpdate(String sqltablename, String strsqlcolumnname,String strsqlcolumnvalue,String strsqlcondition,int strExecEventFlag)throws Exception  {
		boolean EshopSQLDBUpdate= false;
		String Table_name = null;
		String Column_name = null;
		String Column_Value = null;
		String SQL_condition = null;

		try {
			if(strExecEventFlag==1){
				Table_name=RetrieveTestDataValue("EshopSQLDBUpdate",sqltablename,strExecEventFlag);
				Column_name=RetrieveTestDataValue("EshopSQLDBUpdate",strsqlcolumnname,strExecEventFlag);
				Column_Value=RetrieveTestDataValue("EshopSQLDBUpdate",strsqlcolumnvalue,strExecEventFlag);
				SQL_condition=RetrieveTestDataValue("EshopSQLDBUpdate",strsqlcondition,strExecEventFlag);
			}

			if(Table_name==null){
				Report_Functions.ReportEventFailure(doc,  "EshopSQLDBUpdate",  "SQL table name is not provided in the Data Sheet.", false);
				EshopSQLDBUpdate= false;
				throw new RuntimeException ("Table_name is null");
			}

			if(Column_name==null){
				Report_Functions.ReportEventFailure(doc,  "EshopSQLDBUpdate",  "SQL Column name is not provided in the Data Sheet.", false);
				EshopSQLDBUpdate= false;
				throw new RuntimeException ("Column_name is null");
			}

			if(Column_Value==null){
				Report_Functions.ReportEventFailure(doc,  "EshopSQLDBUpdate",  "SQL Column value is not provided in the Data Sheet.", false);
				EshopSQLDBUpdate= false;
				throw new RuntimeException ("Column_Value is null");
			}

			if(SQL_condition==null){
				Report_Functions.ReportEventFailure(doc,  "EshopSQLDBUpdate",  "SQL condition is not provided in the Data Sheet.", false);
				EshopSQLDBUpdate= false;
				throw new RuntimeException ("SQL_condition is null");
			}

			//Query to Execute      
			String query = "update "+Table_name+" set "+Column_name+"="+Column_Value+" where "+SQL_condition;
			EShopstmt.execute(query);
			EshopSQLDBUpdate=true;
			Report_Functions.ReportEventSuccess(doc, "1", "EshopSQLDBUpdate", "The SQL Update Query : "+ query + " executed successfully.", 2);

		} catch (Exception e) {
			EshopSQLDBUpdate=false;
			Report_Functions.ReportEventFailure(doc,  "EshopSQLDBUpdate",  "Error occured while executing the SQL query.Error description is : "+ e.getMessage() +".", true);
			Log.info("EshopSQLDBUpdate Error : " + e);
		}
		return EshopSQLDBUpdate;
	}

	/**
	 * @Objective <b>This method is to deleting records from EShop SQL DB<b>
	 * @param sqltablename
	 * @param sqlcondition
	 * @param strExecEventFlag
	 * @author <b>Muralimohan M</b>
	 * @since <b>09-May-16</b>
	 */
	public static boolean EshopSQLDBDelete(String sqltablename, String sqlcondition, int strExecEventFlag)throws Exception  {
		boolean elementStatus= false;
		String tablename = null;
		String condition = null;

		try{
			if(strExecEventFlag==1){
				tablename=RetrieveTestDataValue("EshopSQLDBDelete",sqltablename,strExecEventFlag);
				condition=RetrieveTestDataValue("EshopSQLDBDelete",sqlcondition,strExecEventFlag);
			}

			if(tablename==null || condition==null){
				throw new RuntimeException ("strData is null");
			}

		}catch(Exception e){
			Log.info("Error log in reading file is "+e);
			throw e;
		}

		String check = "select * from "+tablename +" where "+condition;
		String query = "Delete from "+ tablename +" where "+ condition;
		ResultSet rs = null;

		try {			
			rs = EShopstmt.executeQuery(check);		

			int temp=0;	
			while(rs.next()){
				temp++;
			}

			if(temp >= 1){

				EShopstmt.execute(query);
				elementStatus=true;

				Report_Functions.ReportEventSuccess(doc, "1", "EshopSQLDBDelete", "The SQL Query : "+ query + " executed successfully.", 2);

			}//If rows not available FALSE will be returned so no delete
			else if(temp < 1){
				Report_Functions.ReportEventSuccess(doc, "1", "EshopSQLDBDelete", "The SQL Query : "+ query + " records are not availbale in DB", 2);
				elementStatus=true;
			}

		}catch (Exception e) { 
			elementStatus=false;
			Report_Functions.ReportEventFailure(doc,  "EshopSQLDBDelete",  "Error occured while executing the SQL query.Error description is : "+ e.getMessage() +".", false);
			Log.info("EshopSQLDBDelete Error : " + e);
		}
		return elementStatus;
	}

	/**
	 * 
	 * @Objective <b>This method is to closing the SQL DB<b>
	 * @return
	 * @author <b>Lakshman</b>
	 * @since <b>02-November-15</b>
	 */
	public static boolean EshopSQLDBCloseConnection()throws Exception  {
		boolean elementStatus= false;

		try {
			// closing DB Connection       
			EshopConnection.close(); 
			elementStatus=true;
			Report_Functions.ReportEventSuccess(doc, "1", "SQLDBCloseConnection", "The SQL DB Connection disconnected successfully.", 2);

		} catch (Exception e) { 
			elementStatus=false;
			Report_Functions.ReportEventFailure(doc,  "SQLDBCloseConnection",  "Error occured while closing the SQL DB.Error description is : "+ e.getMessage() +".", false);
			Log.info("SQLDBCloseConnection Error : " + e);
		}
		return elementStatus;
	}

	
	
	public static void updateNodeValue(Document document, String Inputdata,String Entity_Name) throws Exception {
		XPath xpath=null;
		xpath = XPathFactory.newInstance().newXPath();

		NodeList nodes =null;

		EntityName="ENTITY_"+Entity_Name;
		EntityName=Param.getProperty(EntityName);
		TransactionIDTag="TRANSACTIONTAG_"+Entity_Name;
		TransactionIDTag=Param.getProperty(TransactionIDTag);
		String CHANNEL_REFERENCETag="CHANNEL_TAG_"+Entity_Name;
		CHANNEL_REFERENCETag=Param.getProperty(CHANNEL_REFERENCETag);
		EntityNameTag="ENTITYTAG_"+Entity_Name;
		EntityNameTag=Param.getProperty(EntityNameTag);
		String[] Input_data = Inputdata.split(";");


		for (int i = 0; i < Input_data.length; i++) {
			String[] XML_Info = Input_data[i].split("=");
			if (!XML_Info[0].contains("CHANNEL_REFERENCE") && !XML_Info[0].contains(TransactionIDTag)&& !XML_Info[0].contains("ENTITY")){
				try{
					nodes=(NodeList)xpath.compile("*//"+XML_Info[0]).evaluate(document,XPathConstants.NODESET);
					//System.out.println(nodes.getLength());
					if(XML_Info.length==2)
						nodes.item(0).setTextContent(XML_Info[1]);
					else if(XML_Info.length==1){
						nodes.item(0).setTextContent("");
					}else{
						Log.info("*************Error **************:Kindly check any space in the input XML data from Excel sheet");
					}

				}catch (Exception e){
					Report_Functions.ReportEventFailure(doc,  "updateNodeValue",  "Verify whether node '"+XML_Info[0]+"' you are trying to update is exists in the Template XML."+e.getMessage(), false);
				}
			}
		}
		try{
			NodeList root=document.getDocumentElement().getElementsByTagName(TransactionIDTag);
			root.item(0).setTextContent(Param.getProperty("CBOS_ITG_TRANSACTION_ID"));

			NodeList root1 = document.getDocumentElement().getElementsByTagName(EntityNameTag);
			root1.item(0).setTextContent(EntityName);

			NodeList root2=document.getDocumentElement().getElementsByTagName(CHANNEL_REFERENCETag);
			root2.item(0).setTextContent("CCARE");

		}catch(Exception e){
			Report_Functions.ReportEventFailure(doc,  "updateNodeValue",  "Verify whether node TRANSACTION_ID OR ENTITY TAG or CHANNEL_REFERENCE you are trying to update is CORRECT."+e.getMessage(), false);
		}

	}


	public static Document StringToDocument(String strXml) throws Exception {

		Document doc = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			StringReader strReader = new StringReader(strXml);
			InputSource is = new InputSource(strReader);
			doc = (Document) builder.parse(is);
			System.out.println("inside try StringToDocument");
		} catch (Exception e) {
			System.out.println("StringToDocument:"+e.getMessage());
			e.printStackTrace();
			throw e;
		}

		return doc;
	}

	public static String DocumentToString(Document doc) throws Exception {

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(doc), new StreamResult(writer));
		String output = writer.getBuffer().toString();
		return output;
	}

	@SuppressWarnings("deprecation")
	/**
	 * 
	 * @Objective <b>This method is to Sending XML Requets and validating the response from server<b>
	 * @return true/false
	 * @author <b>Muralimohan M</b>
	 * @since <b>07-Jun-16</b>
	 */
	public static boolean Verify_XML_Response_Data(String RequestName,String Input_DATAXML,String Output_DATAXML,String XLDB_Input_Query,String Entity_Name,int strExecEventFlag)throws Exception  {

		String XML_Request=null;
		int result=0;
		String Request_Name=null;
		if(strExecEventFlag==1){
			Request_Name=RetrieveTestDataValue("Verify_XML_Response_Data",RequestName,strExecEventFlag);
			Input_DATAXML=RetrieveTestDataValue("Verify_XML_Response_Data",Input_DATAXML,strExecEventFlag);
			XLDB_Input_Query=RetrieveTestDataValue("Verify_XML_Response_Data", XLDB_Input_Query, strExecEventFlag);
			Output_DATAXML=RetrieveTestDataValue("Verify_XML_Response_Data", Output_DATAXML, strExecEventFlag);
			EntityName=RetrieveTestDataValue("Verify_XML_Response_Data", Entity_Name, strExecEventFlag);
		}
		NSS_URL_PATH="Request_URL_"+EntityName;
		AppURL=Param.getProperty(NSS_URL_PATH);
		try{
			//Reading testdata from XMLTemplate
			XML_Request=ReadExcel.RetrieveValueFromTestDataBasedOnQuery(Param.getProperty("SupportFiles_Location"),Param.getProperty("XML_Template_File_Name"),"Input_XML",XLDB_Input_Query);

			System.out.println("Retrieved Excel from Templated:"+XML_Request);
			factory=DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder=factory.newDocumentBuilder();
			StringBuilder xmlBuilder=new StringBuilder();
			xmlBuilder.append(XML_Request);
			ByteArrayInputStream  is;
			try{
				is=new ByteArrayInputStream(xmlBuilder.toString().getBytes("UTF-8"));
				document=builder.parse(is);
			}catch(Exception e){
				e.printStackTrace();
			}
			//updating node values from XMLtemplate based on input data from testdata sheet
			updateNodeValue(document,Input_DATAXML,EntityName);
			String Updated_XML_Request = DocumentToString(document);

			try{
				PostMethod post = new PostMethod(AppURL+Request_Name+"/");
				post.setRequestBody(Updated_XML_Request);
				HttpClient httpclient = new HttpClient();
				try{
					result = httpclient.executeMethod(post);
					Report_Functions.ReportEventSuccess(doc, "1", "Verify_XML_Response_Data", "The xml request : '"+ Updated_XML_Request + "' has been sent successfully.", 2);
				}catch(Exception httpException){
					httpException.printStackTrace();
					Report_Functions.ReportEventFailure(doc,  "Verify_XML_Response_Data",  "httpException:Error occured while sending the request to the server."+httpException.getMessage(), false);	
				}
				gbstrXMLResponseData= post.getResponseBodyAsString();

				//To Enable SOAP XML Header Validations

				if(result==200){
					Executionstatus=true;
					Report_Functions.ReportEventSuccess(doc, "1", "Verify_XML_Response_Data", "The xml response : '"+ gbstrXMLResponseData.toString() + "' received successfully.", 2);
					//validating XML headers and footers
					if(Param.getProperty("SOAP_Validation_Enable").equalsIgnoreCase("true")){

						Executionstatus=Verify_XML_Header_Footer_Validation(Param.getProperty("XML_Envelope"), gbstrXMLResponseData);
						Executionstatus=Verify_XML_Header_Footer_Validation(Param.getProperty("XML_Header"), gbstrXMLResponseData);
						Executionstatus=Verify_XML_Header_Footer_Validation(Param.getProperty("XML_LastLine"), gbstrXMLResponseData);
						Executionstatus=Verify_XML_Header_Footer_Validation(Param.getProperty("XML_LastsecondLine"), gbstrXMLResponseData);
						Executionstatus=Verify_XML_Header_Footer_Validation(Param.getProperty("XML_ClosingHeader"), gbstrXMLResponseData);
						Executionstatus=Verify_XML_Header_Footer_Validation(Param.getProperty("XML_OpeningBody"), gbstrXMLResponseData);
					}
					try{
						xpathfactory = XPathFactory.newInstance();
						xpath = XPathFactory.newInstance().newXPath();
						factory=DocumentBuilderFactory.newInstance();
						builder=factory.newDocumentBuilder();
						document=builder.parse(new ByteArrayInputStream(gbstrXMLResponseData.getBytes("UTF-8")));

						String[] Output_data = Output_DATAXML.split(";");

						for (int i = 0; i < Output_data.length; i++) {
							String[] Output_XML_Info = Output_data[i].split("=",2);
							String node_value=(String)xpath.compile("*//"+Output_XML_Info[0]).evaluate(document,XPathConstants.STRING);
							String[] ActualValue=Output_XML_Info[1].split(":");
							int arrLength=ActualValue.length;
							//TO validate ERROR_CODE tag- String to String match

							if( Output_XML_Info[0].contains("ERROR_CODE") ||  Output_XML_Info[0].contains("EXPIRY_DATE_TIME") ||Output_XML_Info[0].contains("ACTIVATIONDATE")){
								Executionstatus=StringValidator(node_value,Output_XML_Info[1]);	
							}else{

								if(arrLength>1 ){
									ProcessedXML=ErrorDescriptionRegExpProcessor(Output_XML_Info[1]);
									Executionstatus=RegExpValidator(node_value,ProcessedXML);


								}else
									if(Output_XML_Info[0].equalsIgnoreCase("TRANSACTION_NUMBER")){
										Executionstatus=RegExpValidator(node_value,Output_XML_Info[1]);
									}

									else{
										Executionstatus=StringValidator(node_value,Output_XML_Info[1]);
									}
							}
							if(Executionstatus==true){
								Report_Functions.ReportEventSuccess(doc, "1", "Verify_XML_Response_Data_NodeValtoEnv", "The actual node value of  : '//"+ Output_XML_Info[0] + "' is '"+ node_value +"' and matched with the expected value '"+ Output_XML_Info[1] +"'.", 2);
								Executionstatus=true;;
							} else{
								Report_Functions.ReportEventFailure(doc,  "Verify_XML_Response_Data_NodeValtoEnv",  "The actual node value of  : '//"+ Output_XML_Info[0] + "' is '"+ node_value +"' and no matching with the expected value '"+ Output_XML_Info[1] +"'.", false);
								Executionstatus=false;
							}
						}

					}catch(Exception responseXMLException){
						responseXMLException.printStackTrace();
						Report_Functions.ReportEventFailure(doc,  "Verify_XML_Response_Data",  "responseXMLException:Error in Parsing response XML.:"+responseXMLException.getMessage(), false);	
						return Executionstatus;
					}
				}


			}catch(Exception xmlFormException){
				System.out.println(xmlFormException.getMessage());
			}


		}catch(Exception e){
			return Executionstatus;
		}finally{
			xpathfactory=null;
			document=null;
			factory=null;
			ProcessedXML=null;
		}

		return Executionstatus;

	}

	@SuppressWarnings("deprecation")
	public static boolean Verify_XML_Response_Data_NodeValtoEnv(String RequestName,String Input_DATAXML,String Output_DATAXML,String XLDB_Input_Query,String NodeValToEnv,String NodeValToEnv2, String Entity_Name,int strExecEventFlag)throws Exception  {
		String XML_Request=null;
		int result=0;
		String Request_Name=null;

		if(strExecEventFlag==1){
			Request_Name=RetrieveTestDataValue("Verify_XML_Response_Data_NodeValtoEnv2",RequestName,strExecEventFlag);
			Input_DATAXML=RetrieveTestDataValue("Verify_XML_Response_Data_NodeValtoEnv2",Input_DATAXML,strExecEventFlag);
			XLDB_Input_Query=RetrieveTestDataValue("Verify_XML_Response_Data_NodeValtoEnv2", XLDB_Input_Query, strExecEventFlag);
			Output_DATAXML=RetrieveTestDataValue("Verify_XML_Response_Data_NodeValtoEnv2", Output_DATAXML, strExecEventFlag);
			EntityName=RetrieveTestDataValue("Verify_XML_Response_Data_NodeValtoEnv",Entity_Name,strExecEventFlag);
		}

		if (NodeValToEnv!=""){
			NodeValToEnv=RetrieveTestDataValue("Verify_XML_Response_Data_NodeValtoEnv2",NodeValToEnv,strExecEventFlag);
		}

		if (NodeValToEnv2!=""){
			NodeValToEnv2=RetrieveTestDataValue("Verify_XML_Response_Data_NodeValtoEnv",NodeValToEnv2,strExecEventFlag);
		}

		NSS_URL_PATH="Request_URL_"+EntityName;
		AppURL=Param.getProperty(NSS_URL_PATH);

		try{
			//Reading testdata from XMLTemplate
			XML_Request=ReadExcel.RetrieveValueFromTestDataBasedOnQuery(Param.getProperty("SupportFiles_Location"),Param.getProperty("XML_Template_File_Name"),"Input_XML",XLDB_Input_Query);

			Log.info("Raw XML_Request is : "+XML_Request);

			factory=DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder=factory.newDocumentBuilder();
			StringBuilder xmlBuilder=new StringBuilder();
			xmlBuilder.append(XML_Request);
			ByteArrayInputStream  is;
			try{
				is=new ByteArrayInputStream(xmlBuilder.toString().getBytes("UTF-8"));
				document=builder.parse(is);
			}catch(Exception e){
				e.printStackTrace();
			}
			updateNodeValue(document,Input_DATAXML,EntityName);
			String Updated_XML_Request = DocumentToString(document);
			Log.info("Updated_XML_Request : "+Updated_XML_Request);

			try{
				PostMethod post = new PostMethod(AppURL+Request_Name+"/");
				post.setRequestHeader("Content-type","application/x-www-form-urlencoded");
				post.setRequestBody(Updated_XML_Request);

				HttpClient httpclient = new HttpClient();
				try{
					result = httpclient.executeMethod(post);
					Report_Functions.ReportEventSuccess(doc, "1", "Verify_XML_Response_Data_NodeValtoEnv", "The xml request : '"+ Updated_XML_Request + "' has been sent successfully.", 2);
				}catch(Exception httpException){
					httpException.printStackTrace();
					Report_Functions.ReportEventFailure(doc,  "Verify_XML_Response_Data_NodeValtoEnv",  "httpException:Error occured while sending the request to the server."+httpException.getMessage(), false);	
				}
				gbstrXMLResponseData= post.getResponseBodyAsString();

				Log.info("Response is : "+ gbstrXMLResponseData);

				if(result==200){
					Executionstatus=true;
					Report_Functions.ReportEventSuccess(doc, "1", "Verify_XML_Response_Data_NodeValtoEnv", "The xml response : '"+ gbstrXMLResponseData.toString() + "' received successfully.", 2);

					if(Param.getProperty("SOAP_Validation_Enable").equalsIgnoreCase("true")){

						Executionstatus=Verify_XML_Header_Footer_Validation(Param.getProperty("XML_Envelope"), gbstrXMLResponseData);
						Executionstatus=Verify_XML_Header_Footer_Validation(Param.getProperty("XML_Header"), gbstrXMLResponseData);
						Executionstatus=Verify_XML_Header_Footer_Validation(Param.getProperty("XML_LastLine"), gbstrXMLResponseData);
						Executionstatus=Verify_XML_Header_Footer_Validation(Param.getProperty("XML_LastsecondLine"), gbstrXMLResponseData);
						Executionstatus=Verify_XML_Header_Footer_Validation(Param.getProperty("XML_ClosingHeader"), gbstrXMLResponseData);
						Executionstatus=Verify_XML_Header_Footer_Validation(Param.getProperty("XML_OpeningBody"), gbstrXMLResponseData);
					}

					try{

						builder = factory.newDocumentBuilder();
						Document doc2 = builder.parse(new ByteArrayInputStream(gbstrXMLResponseData.getBytes("UTF-8")));
						XPathFactory xpathFactory = XPathFactory.newInstance();
						XPath xpath = xpathFactory.newXPath();
						String node_value = null;

						try{
							//If Env values are present then store it in Env properties file before response node validation
							if (NodeValToEnv!=""){
								NodeList node=doc2.getDocumentElement().getElementsByTagName(NodeValToEnv);
								String EnvironmentValue1=node.item(0).getTextContent();
								EnvironmentValue.setProperty("NodeVal_to_Environment",EnvironmentValue1);

								Report_Functions.ReportEventSuccess(doc, "1", "Verify_XML_Response_Data_NodeValtoEnv", "Dynamic Node '"+NodeValToEnv+"' has been stored in the Environment variable '"
										+ "' successfully with Value as '"+EnvironmentValue1+"'", 2);

								if(NodeValToEnv2!=""){
									NodeList node2=doc2.getDocumentElement().getElementsByTagName(NodeValToEnv2);
									String EnvironmentValue2=node2.item(0).getTextContent();
									if(EnvironmentValue2!=""){
										EnvironmentValue.setProperty("NodeVal_to_Environment2",EnvironmentValue2);
										Report_Functions.ReportEventSuccess(doc, "1", "Verify_XML_Response_Data_NodeValtoEnv", "Dynamic Node '"+NodeValToEnv2+"' has been stored in the Environment variable 'NodeVal_to_Environment2' successfully with Value as '"+EnvironmentValue2+"'", 2);
									}
								}

							}
						}catch(Exception e){

							log.info(e.getMessage());
							//Need to check this line
							//Report_Functions.ReportEventFailure(doc,  "Verify_XML_Response_Data_NodeValtoEnv",  "Error occured while storing the Dynamic values from Response XML. Check the Response XML for Dynamic values.", false);
						}

						//Output data split from Excel sheet
						String[] Output_data = Output_DATAXML.split(";");

						for (int i = 0; i < Output_data.length; i++) {

							String[] Output_XML_Info = Output_data[i].split("=",-1);
							XPathExpression expr = xpath.compile("//"+ Output_XML_Info[0]);
							node_value = (String) expr.evaluate(doc2, XPathConstants.STRING);
							String[] ActualValue=Output_XML_Info[1].split(":");
							int arrLength=ActualValue.length;

							//Specific node validation
							if( Output_XML_Info[0].contains("ERROR_DESC") && arrLength>1 ){

								//Validating the Error desc tag for all Negative responses. -- Reg Validation
								ProcessedXML=ErrorDescriptionRegExpProcessor(Output_XML_Info[1]);
								Executionstatus=RegExpValidator(node_value,ProcessedXML);

							}else if ( (NodeValToEnv !="")  && (Output_XML_Info[0].contains(NodeValToEnv)) ){

								//Validation of Dynamic value 1
								Executionstatus=RegExpValidator(node_value,Output_XML_Info[1]);

							}else if ( (NodeValToEnv2 !="")  && (Output_XML_Info[0].contains(NodeValToEnv2)) ){

								//Validation of Dynamic value 1
								Executionstatus=RegExpValidator(node_value,Output_XML_Info[1]);

							}else {

								//String validator for all NOdes except the Dynamic values and error desc
								Executionstatus=StringValidator(node_value,Output_XML_Info[1]);
							}

							if(Executionstatus==true){
								Report_Functions.ReportEventSuccess(doc, "1", "Verify_XML_Response_Data_NodeValtoEnv", "The actual node value of  : '//"+ Output_XML_Info[0] + "' is '"+ node_value +"' and matched with the expected value '"+ Output_XML_Info[1] +"'.", 2);
								Executionstatus=true;
							} else{
								Report_Functions.ReportEventFailure(doc,  "Verify_XML_Response_Data_NodeValtoEnv",  "The actual node value of  : '//"+ Output_XML_Info[0] + "' is '"+ node_value +"' and no matching with the expected value '"+ Output_XML_Info[1] +"'.", false);
								Executionstatus=false;
							}
						}

					}catch(Exception responseXMLException){
						Report_Functions.ReportEventFailure(doc,  "Verify_XML_Response_Data_NodeValtoEnv",  "responseXMLException:Error in Parsing response XML.:"+responseXMLException.getMessage(), false);	
						return Executionstatus;
					}
				}

			}catch(Exception xmlFormException){
				log.info(xmlFormException.getMessage());
			}

		}catch(Exception e){
			return Executionstatus;
		}

		return Executionstatus;
	}

	/**
	 * Verify_XML_Response_Data_NodeValtoEnv2
	 * @author Lakshman 
	 * @since Aug 3, 2016
	 */
	@SuppressWarnings("deprecation")
	public static boolean Verify_XML_Response_Data_NodeValtoEnv2(String RequestName,String Input_DATAXML,String Output_DATAXML,String XLDB_Input_Query,String NodeValToEnv,String NodeValToEnv2,String NodeValToEnv3,String validityDays, String Entity_Name,int strExecEventFlag)throws Exception  {

		String XML_Request=null;
		int result=0;
		String Request_Name=null;
		int Days_To_Add = 0;
		String expectedDate=null;
		DateFormat dateFormat; 
		Calendar c;
		String EnvironmentValue1=null;
		if(strExecEventFlag==1){
			Request_Name=RetrieveTestDataValue("Verify_XML_Response_Data_NodeValtoEnv2",RequestName,strExecEventFlag);
			Input_DATAXML=RetrieveTestDataValue("Verify_XML_Response_Data_NodeValtoEnv2",Input_DATAXML,strExecEventFlag);
			XLDB_Input_Query=RetrieveTestDataValue("Verify_XML_Response_Data_NodeValtoEnv2", XLDB_Input_Query, strExecEventFlag);
			Output_DATAXML=RetrieveTestDataValue("Verify_XML_Response_Data_NodeValtoEnv2", Output_DATAXML, strExecEventFlag);
			EntityName=RetrieveTestDataValue("Verify_XML_Response_Data_NodeValtoEnv",Entity_Name,strExecEventFlag);
		}


		if (NodeValToEnv!=""){

			NodeValToEnv=RetrieveTestDataValue("Verify_XML_Response_Data_NodeValtoEnv2",NodeValToEnv,strExecEventFlag);

			if(NodeValToEnv==null){
				System.out.println("node value matching null condition");
				NodeValToEnv="";
			}

		}

		if (NodeValToEnv2!="" || NodeValToEnv2!=NULL){
			NodeValToEnv2=RetrieveTestDataValue("Verify_XML_Response_Data_NodeValtoEnv",NodeValToEnv2,strExecEventFlag);
			if(NodeValToEnv2==null){
				System.out.println("node value matching null condition");
				NodeValToEnv2="";
			}

		}
		if (NodeValToEnv3!="" || NodeValToEnv3!=NULL){
			NodeValToEnv3=RetrieveTestDataValue("Verify_XML_Response_Data_NodeValtoEnv",NodeValToEnv3,strExecEventFlag);
			if(NodeValToEnv3==null){
				System.out.println("node value matching null condition");
				NodeValToEnv3="";
			}
		}

		log.info("Env values are : "+ NodeValToEnv +" | "+NodeValToEnv2);
		//Validity Days validation from Spreadsheet. If not available this validation will be avoided
		if(validityDays!=""){
			validityDays=RetrieveTestDataValue("Verify_XML_Response_Data_NodeValtoEnv2",validityDays,strExecEventFlag);
		}

		NSS_URL_PATH="Request_URL_"+EntityName;
		AppURL=Param.getProperty(NSS_URL_PATH);

		try{
			//Reading testdata from XMLTemplate
			XML_Request=ReadExcel.RetrieveValueFromTestDataBasedOnQuery(Param.getProperty("SupportFiles_Location"),Param.getProperty("XML_Template_File_Name"),"Input_XML",XLDB_Input_Query);

			Log.info("Raw XML_Request is : "+XML_Request);

			factory=DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder=factory.newDocumentBuilder();
			StringBuilder xmlBuilder=new StringBuilder();
			xmlBuilder.append(XML_Request);
			ByteArrayInputStream  is;
			try{
				is=new ByteArrayInputStream(xmlBuilder.toString().getBytes("UTF-8"));
				document=builder.parse(is);
			}catch(Exception e){
				e.printStackTrace();
			}
			updateNodeValue(document,Input_DATAXML,EntityName);
			String Updated_XML_Request = DocumentToString(document);
			Log.info("Updated_XML_Request : "+Updated_XML_Request);

			try{
				PostMethod post = new PostMethod(AppURL+Request_Name+"/");
				post.setRequestHeader("Content-type","application/x-www-form-urlencoded");
				post.setRequestBody(Updated_XML_Request);

				HttpClient httpclient = new HttpClient();
				try{
					result = httpclient.executeMethod(post);
					Report_Functions.ReportEventSuccess(doc, "1", "Verify_XML_Response_Data_NodeValtoEnv", "The xml request : '"+ Updated_XML_Request + "' has been sent successfully.", 2);
				}catch(Exception httpException){
					httpException.printStackTrace();
					Report_Functions.ReportEventFailure(doc,  "Verify_XML_Response_Data_NodeValtoEnv",  "httpException:Error occured while sending the request to the server."+httpException.getMessage(), false);	
				}
				gbstrXMLResponseData= post.getResponseBodyAsString();

				Log.info("Response is : "+ gbstrXMLResponseData);

				if(result==200){
					Executionstatus=true;
					Report_Functions.ReportEventSuccess(doc, "1", "Verify_XML_Response_Data_NodeValtoEnv", "The xml response : '"+ gbstrXMLResponseData.toString() + "' received successfully.", 2);

					if(Param.getProperty("SOAP_Validation_Enable").equalsIgnoreCase("true")){

						Executionstatus=Verify_XML_Header_Footer_Validation(Param.getProperty("XML_Envelope"), gbstrXMLResponseData);
						Executionstatus=Verify_XML_Header_Footer_Validation(Param.getProperty("XML_Header"), gbstrXMLResponseData);
						Executionstatus=Verify_XML_Header_Footer_Validation(Param.getProperty("XML_LastLine"), gbstrXMLResponseData);
						Executionstatus=Verify_XML_Header_Footer_Validation(Param.getProperty("XML_LastsecondLine"), gbstrXMLResponseData);
						Executionstatus=Verify_XML_Header_Footer_Validation(Param.getProperty("XML_ClosingHeader"), gbstrXMLResponseData);
						Executionstatus=Verify_XML_Header_Footer_Validation(Param.getProperty("XML_OpeningBody"), gbstrXMLResponseData);
					}

					try{

						builder = factory.newDocumentBuilder();
						Document doc2 = builder.parse(new ByteArrayInputStream(gbstrXMLResponseData.getBytes("UTF-8")));
						XPathFactory xpathFactory = XPathFactory.newInstance();
						XPath xpath = xpathFactory.newXPath();
						String node_value = null;

						try{
							//If Env values are present then store it in Env properties file before response node validation

							if (NodeValToEnv!=""){
								NodeList node=doc2.getDocumentElement().getElementsByTagName(NodeValToEnv);
								EnvironmentValue1=node.item(0).getTextContent();
								EnvironmentValue.setProperty("NodeVal_to_Environment",EnvironmentValue1);

								Report_Functions.ReportEventSuccess(doc, "1", "Verify_XML_Response_Data_NodeValtoEnv", "Dynamic Node '"+NodeValToEnv+"' has been stored in the Environment variable 'NodeVal_to_Environment1' successfully with Value as '"+EnvironmentValue1+"'", 2);

								if(NodeValToEnv2!=""){
									NodeList node2=doc2.getDocumentElement().getElementsByTagName(NodeValToEnv2);
									String EnvironmentValue2=node2.item(0).getTextContent();
									if(EnvironmentValue2!=""){
										EnvironmentValue.setProperty("NodeVal_to_Environment2",EnvironmentValue2);
										Report_Functions.ReportEventSuccess(doc, "1", "Verify_XML_Response_Data_NodeValtoEnv", "Dynamic Node '"+NodeValToEnv2+"' has been stored in the Environment variable 'NodeVal_to_Environment2' successfully with Value as '"+EnvironmentValue2+"'", 2);
									}
								}

								if(NodeValToEnv3!=""){
									NodeList node2=doc2.getDocumentElement().getElementsByTagName(NodeValToEnv3);
									String EnvironmentValue3=node2.item(0).getTextContent();
									if(EnvironmentValue3!=""){
										EnvironmentValue.setProperty("NodeVal_to_Environment3",EnvironmentValue3);
										Report_Functions.ReportEventSuccess(doc, "1", "Verify_XML_Response_Data_NodeValtoEnv", "Dynamic Node '"+NodeValToEnv3+"' has been stored in the Environment variable 'NodeVal_to_Environment2' successfully with Value as '"+EnvironmentValue3+"'", 2);
									}
								}

							}

						}catch(Exception e){
							//Need to check
							//Report_Functions.ReportEventFailure(doc,  "Verify_XML_Response_Data_NodeValtoEnv",  "Error occured while storing the Dynamic values from Response XML. Check the Response XML for Dynamic values.", false);
							log.info(e.getMessage());

						}

						//Output data split from Excel sheet
						String[] Output_data = Output_DATAXML.split(";");

						for (int i = 0; i < Output_data.length; i++) {

							String[] Output_XML_Info = Output_data[i].split("=",-1);
							XPathExpression expr = xpath.compile("//"+ Output_XML_Info[0]);
							node_value = (String) expr.evaluate(doc2, XPathConstants.STRING);
							String[] ActualValue=Output_XML_Info[1].split(":");
							int arrLength=ActualValue.length;

							//Specific node validation
							if( Output_XML_Info[0].contains("ERROR_DESC") && arrLength>1 ){

								//Validating the Error desc tag for all Negative responses. -- Reg Validation
								ProcessedXML=ErrorDescriptionRegExpProcessor(Output_XML_Info[1]);
								Executionstatus=RegExpValidator(node_value,ProcessedXML);

							}else if ( (NodeValToEnv !="")  && (Output_XML_Info[0].contains(NodeValToEnv)) ){

								//Validation of Dynamic value 1
								Executionstatus=RegExpValidator(node_value,Output_XML_Info[1]);

							}else if ( (NodeValToEnv2 !="")  && (Output_XML_Info[0].contains(NodeValToEnv2)) ){

								//Validation of Dynamic value 1
								Executionstatus=RegExpValidator(node_value,Output_XML_Info[1]);

							}else if ( (NodeValToEnv3 !="")  && (Output_XML_Info[0].contains(NodeValToEnv3)) ){

								//Validation of Dynamic value 1
								Executionstatus=RegExpValidator(node_value,Output_XML_Info[1]);

							}else {
								//Future date validation only if Validity days are available
								switch(Output_XML_Info[1]){
								case "FUTUREDATE":
									Days_To_Add=Integer.parseInt(validityDays);
									Days_To_Add=Days_To_Add-1;
									dateFormat= new SimpleDateFormat("dd/MM/yyyy");
									c = Calendar.getInstance();    
									c.setTime(new Date());
									c.add(Calendar.DATE, Days_To_Add);
									expectedDate = dateFormat.format(c.getTime());
									Output_XML_Info[1]=expectedDate;
									break;
								case "CURRENTDATE":
									dateFormat= new SimpleDateFormat("dd/MM/yyyy");
									c = Calendar.getInstance(); 
									c.setTime(new Date());
									expectedDate = dateFormat.format(c.getTime());
									Output_XML_Info[1]=expectedDate;
									break;
								default:
									log.info("Normal value is compared[Non date validation]:"+Output_XML_Info[1]);
								}
								//String validator for all NOdes except the Dynamic values and error desc
								Executionstatus=StringValidator(node_value,Output_XML_Info[1]);
							}

							if(Executionstatus==true){
								Report_Functions.ReportEventSuccess(doc, "1", "Verify_XML_Response_Data_NodeValtoEnv", "The actual node value of  : '//"+ Output_XML_Info[0] + "' is '"+ node_value +"' and matched with the expected value '"+ Output_XML_Info[1] +"'.", 2);
								Executionstatus=true;
							} else{
								Report_Functions.ReportEventFailure(doc,  "Verify_XML_Response_Data_NodeValtoEnv",  "The actual node value of  : '//"+ Output_XML_Info[0] + "' is '"+ node_value +"' and no matching with the expected value '"+ Output_XML_Info[1] +"'.", false);
								Executionstatus=false;
							}
						}

					}catch(Exception responseXMLException){
						Report_Functions.ReportEventFailure(doc,  "Verify_XML_Response_Data_NodeValtoEnv",  "responseXMLException:Error in Parsing response XML.:"+responseXMLException.getMessage(), false);	
						return Executionstatus;
					}
				}

			}catch(Exception xmlFormException){
				log.info(xmlFormException.getMessage());
			}

		}catch(Exception e){
			return Executionstatus;
		}

		return Executionstatus;
	}

	/**
	 * 
	 * @Objective <b>This method is to Generate NSS Script commands<b>
	 * @return
	 * @author <b>Muralimohan M</b>
	 * @throws Exception 
	 * @since <b>18-May-16</b>
	 */
	public static String Generate_Runtime_Data_ITG(String Command_Type,String EntityName) throws Exception{

		strDateTimeFormat=DateFormatChange("yyyy_MMM_dd_HH_mm_ss");

		Unix_Folder_Path="ITG_NSS_Auto_Scripts_Path_"+EntityName;

		Env_ITG_Script_Case_Name=Param.getProperty("Start_Stop_Shell_Script")+" "+EnvironmentValue.getProperty("TESTCASE_NAME");
		Version_Revision_Path=" "+Param.getProperty("Env_ITG_Version_Rev")+" "+Param.getProperty(Unix_Folder_Path)+" "+strDateTimeFormat;

		switch(Command_Type){
		case "PathCommand"	:GeneratedCommand="cd "+Param.getProperty(Unix_Folder_Path);break;
		case "Command_Start":GeneratedCommand="./"+Env_ITG_Script_Case_Name+" start "+Version_Revision_Path+" &";break;
		case "Command_Stop"	:
			//Thread.sleep(5000L);
			GeneratedCommand="./"+Env_ITG_Script_Case_Name+" stop "+Version_Revision_Path+" &";break;
		case "CDR"			:GeneratedCommand="./"+Env_ITG_Script_Case_Name+" CDR "+Version_Revision_Path+" &";break;
		default:
			Report_Functions.ReportEventFailure(doc,  "Generate_Runtime_Data_ITG",  "Selected Command is not available in the Option", false);
			GeneratedCommand="";
			break;
		}
		return GeneratedCommand;
	}

	/**
	 * @Objective:Regular Expression Validator to match Actual pattern against Expected
	 * @author <b>Muralimohan M</b>
	 * @return true/false
	 * */
	public static boolean RegExpValidator(String ActualValue,String ExpectedValue){
		Executionstatus=false;
		Pattern regPatter=Pattern.compile(ExpectedValue.trim());
		Matcher matches = regPatter.matcher(ActualValue.trim());
		boolean statusMatch=matches.find();
		if (statusMatch) {
			Executionstatus=true;
			return statusMatch;
		}
		else{
			Executionstatus=false;

		}

		regPatter=null;
		matches=null;
		return Executionstatus;

	}

	/**
	 * @Objective:Regular Expression Validator to match Actual pattern against Expected
	 * @author <b>Muralimohan M</b>
	 * @return true/false
	 * */
	public static boolean StringValidator(String ActualValue,String ExpectedValue){

		Executionstatus=false;

		boolean statusMatch=ActualValue.contentEquals(ExpectedValue);
		if (statusMatch) {
			Executionstatus=true;
			return statusMatch;
		}
		else{
			Executionstatus=false;

		}
		return Executionstatus;

	}

	/**
	 * 
	 * @Objective <b>Verifies the EShopSQLDBSelect</b>
	 * @param sqltablename
	 * @param strsqlcolumnname
	 * @param strsqlcondition
	 * @param strExpectedvalue
	 * @param strExecEventFlag 1-StringMatchValidation ,2-Regular Expression validation
	 * @author <b>Muralimohan M</b>
	 * @since <b>20-May-15</b>
	 */
	public static boolean EShopSQLDBSelect(String sqltablename, String strsqlcolumnname,String strsqlcondition,String strExpectedvalue,int strExecEventFlag)throws Exception  {

		boolean Executionstatus = false;

		String query = null;
		String Table_name = null;
		String Column_name = null;
		String SQL_condition = null;
		String Expected_value = "";
		String Actual_Value = null;

		try {
			if(strExecEventFlag==1){
				Table_name=RetrieveTestDataValue("EShopSQLDBSelect",sqltablename,strExecEventFlag);
				Column_name=RetrieveTestDataValue("EShopSQLDBSelect",strsqlcolumnname,strExecEventFlag);
				SQL_condition=RetrieveTestDataValue("EShopSQLDBSelect",strsqlcondition,strExecEventFlag);
				Expected_value=RetrieveTestDataValue("EShopSQLDBSelect",strExpectedvalue,strExecEventFlag);

			}

			if(strExecEventFlag==2){
				Table_name=RetrieveTestDataValue("EShopSQLDBSelect",sqltablename,strExecEventFlag);
				Column_name=RetrieveTestDataValue("EShopSQLDBSelect",strsqlcolumnname,strExecEventFlag);
				SQL_condition=RetrieveTestDataValue("EShopSQLDBSelect",strsqlcondition,strExecEventFlag);
				Expected_value=RetrieveTestDataValue("EShopSQLDBSelect",strExpectedvalue,strExecEventFlag);

			}

			if(Table_name==null){
				Report_Functions.ReportEventFailure(doc,  "EShopSQLDBSelect",  "SQL table name is not provided in the Data Sheet.", false);
				Executionstatus= false;
				throw new RuntimeException ("Table_name is null");
			}

			if(Column_name==null){
				Report_Functions.ReportEventFailure(doc,  "EShopSQLDBSelect",  "SQL Column name is not provided in the Data Sheet.", false);
				Executionstatus= false;
				throw new RuntimeException ("Column_name is null");
			}

			if(SQL_condition==null){
				Report_Functions.ReportEventFailure(doc,  "EShopSQLDBSelect",  "SQL condition is not provided in the Data Sheet.", false);
				Executionstatus= false;
				throw new RuntimeException ("SQL_condition is null");
			}

			//Query to Execute      
			query = "select "+Column_name+" from "+Table_name+" where "+SQL_condition+"";
			Eshop_SQLServer = EShopstmt.executeQuery(query);


		} catch (Exception e) {
			Report_Functions.ReportEventFailure(doc,  "EShopSQLDBSelect",  "Error occured while executing the SQL query.Error description is : "+ e.getMessage() +".", false);
			Log.info("EShopSQLDBSelect Error : " + e.getMessage());
			Executionstatus=false;
			return Executionstatus;
		}

		try{

			Eshop_SQLServer.next();
			Actual_Value = Eshop_SQLServer.getString(1).trim();

		}catch(NullPointerException e){

			Log.info("Null value occurs " +e.getMessage());

		}

		catch (Exception e) {           // If no record is present in the fired Query
			Report_Functions.ReportEventFailure(doc,  "EShopSQLDBSelect",  "No Record found for this query: "+query, true);
			Log.info("EShopSQLDBSelect Error : "+e.getMessage());
			Executionstatus=false;
			return Executionstatus;
		}

		try{
			if(!Eshop_SQLServer.wasNull()){       // If some value is present in the fired Query
				if(strExecEventFlag==1){
					Executionstatus=StringValidator(Actual_Value, Expected_value);
				}else{
					Executionstatus=RegExpValidator(Actual_Value, Expected_value);
				}

				if(Executionstatus){
					Report_Functions.ReportEventSuccess(doc, "1","EShopSQLDBSelect", "The actual value : '"+Actual_Value+"' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' matches the expected value : '"+Expected_value+"'", 2);
					Executionstatus=true;
				}else{
					Report_Functions.ReportEventFailure(doc,  "EShopSQLDBSelect",  "The actual value : '"+Actual_Value+"' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' does not match the expected value : '"+Expected_value+"'", true);
					Executionstatus=false;
				}



			}else if(Eshop_SQLServer.wasNull()){        // If "NULL" value is present in the fired Query
				if(Expected_value.equals("NULL")){
					Report_Functions.ReportEventSuccess(doc, "1", "EShopSQLDBSelect", "The actual value 'NULL' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' matches with the expected value :'"+Expected_value+"'", 2);
					Executionstatus=true;
				}else if(!(Expected_value.equals("NULL"))){
					Report_Functions.ReportEventFailure(doc,  "EShopSQLDBSelect",  "The actual value 'NULL' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' does not match the expected value : '"+Expected_value+"'", true);  	 
					Executionstatus=false;
				}
			}
		} catch(NullPointerException e){

			System.out.println("Null value occurs " +e.getMessage());
			return Executionstatus;

		}

		catch (Exception e){
			Report_Functions.ReportEventFailure(doc,  "EShopSQLDBSelect",  "Error occured while comparing the values in SQL query.Error description is : "+ e.getMessage() +".", true);
			Log.info("EShopSQLDBSelect Error : "+e.getMessage());
			Executionstatus=false;
			return Executionstatus;
		}
		return Executionstatus;
	}
	
	
	
	
	public static boolean SQLDBSelect(String sqltablename, String strsqlcolumnname,String strsqlcondition,String strExpectedvalue,int strExecEventFlag)throws Exception  {

		boolean Executionstatus = false;

		String query = null;
		String Table_name = null;
		String Column_name = null;
		String SQL_condition = null;
		String Expected_value = "";
		String Actual_Value = null;

		try {
			if(strExecEventFlag==1){
				Table_name=RetrieveTestDataValue("SQLDBSelect",sqltablename,strExecEventFlag);
				Column_name=RetrieveTestDataValue("SQLDBSelect",strsqlcolumnname,strExecEventFlag);
				SQL_condition=RetrieveTestDataValue("SQLDBSelect",strsqlcondition,strExecEventFlag);
				Expected_value=RetrieveTestDataValue("SQLDBSelect",strExpectedvalue,strExecEventFlag);

			}

			if(strExecEventFlag==2){
				Table_name=RetrieveTestDataValue("SQLDBSelect",sqltablename,strExecEventFlag);
				Column_name=RetrieveTestDataValue("SQLDBSelect",strsqlcolumnname,strExecEventFlag);
				SQL_condition=RetrieveTestDataValue("SQLDBSelect",strsqlcondition,strExecEventFlag);
				Expected_value=RetrieveTestDataValue("SQLDBSelect",strExpectedvalue,strExecEventFlag);

			}

			if(Table_name==null){
				Report_Functions.ReportEventFailure(doc,  "SQLDBSelect",  "SQL table name is not provided in the Data Sheet.", false);
				Executionstatus= false;
				throw new RuntimeException ("Table_name is null");
			}

			if(Column_name==null){
				Report_Functions.ReportEventFailure(doc,  "SQLDBSelect",  "SQL Column name is not provided in the Data Sheet.", false);
				Executionstatus= false;
				throw new RuntimeException ("Column_name is null");
			}

			if(SQL_condition==null){
				Report_Functions.ReportEventFailure(doc,  "SQLDBSelect",  "SQL condition is not provided in the Data Sheet.", false);
				Executionstatus= false;
				throw new RuntimeException ("SQL_condition is null");
			}

			//Query to Execute      
			query = "select "+Column_name+" from "+Table_name+" where "+SQL_condition+"";
			rs_SQLServer = stmt.executeQuery(query);


		} catch (Exception e) {
			Report_Functions.ReportEventFailure(doc,  "SQLDBSelect",  "Error occured while executing the SQL query.Error description is : "+ e.getMessage() +".", false);
			Log.info("EShopSQLDBSelect Error : " + e.getMessage());
			Executionstatus=false;
			return Executionstatus;
		}

		try{

			rs_SQLServer.next();
			Actual_Value = rs_SQLServer.getString(1).trim();

		}catch(NullPointerException e){

			Log.info("Null value occurs " +e.getMessage());

		}

		catch (Exception e) {           // If no record is present in the fired Query
			Report_Functions.ReportEventFailure(doc,  "SQLDBSelect",  "No Record found for this query: "+query, true);
			Log.info("SQLDBSelect Error : "+e.getMessage());
			Executionstatus=false;
			return Executionstatus;
		}

		try{
			if(!rs_SQLServer.wasNull()){       // If some value is present in the fired Query
				if(strExecEventFlag==1){
					Executionstatus=StringValidator(Actual_Value, Expected_value);
				}else{
					Executionstatus=RegExpValidator(Actual_Value, Expected_value);
				}

				if(Executionstatus){
					Report_Functions.ReportEventSuccess(doc, "1","SQLDBSelect", "The actual value : '"+Actual_Value+"' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' matches the expected value : '"+Expected_value+"'", 2);
					Executionstatus=true;
				}else{
					Report_Functions.ReportEventFailure(doc,  "SQLDBSelect",  "The actual value : '"+Actual_Value+"' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' does not match the expected value : '"+Expected_value+"'", true);
					Executionstatus=false;
				}



			}else if(rs_SQLServer.wasNull()){        // If "NULL" value is present in the fired Query
				if(Expected_value.equals("NULL")){
					Report_Functions.ReportEventSuccess(doc, "1", "SQLDBSelect", "The actual value 'NULL' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' matches with the expected value :'"+Expected_value+"'", 2);
					Executionstatus=true;
				}else if(!(Expected_value.equals("NULL"))){
					Report_Functions.ReportEventFailure(doc,  "SQLDBSelect",  "The actual value 'NULL' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' does not match the expected value : '"+Expected_value+"'", true);  	 
					Executionstatus=false;
				}
			}
		} catch(NullPointerException e){

			System.out.println("Null value occurs " +e.getMessage());
			return Executionstatus;

		}

		catch (Exception e){
			Report_Functions.ReportEventFailure(doc,  "SQLDBSelect",  "Error occured while comparing the values in SQL query.Error description is : "+ e.getMessage() +".", true);
			Log.info("SQLDBSelect Error : "+e.getMessage());
			Executionstatus=false;
			return Executionstatus;
		}
		return Executionstatus;
	}

	/**
	 * 
	 * @Objective <b>Verifies the EShopSQLDBSelectFromEnv</b>
	 * @param sqltablename
	 * @param strsqlcolumnname
	 * @param strsqlcondition
	 * @param strExpectedvalue
	 * @param strExecEventFlag
	 * @author <b>Muralimohan M</b>
	 * @since <b>20-May-15</b>
	 */
	public static boolean EShopSQLDBSelectFromEnv(String sqltablename, String strsqlcolumnname,String strsqlcondition,String NodeVal_to_Environment,String strExpectedvalue,int strExecEventFlag)throws Exception  {

		String query = null;
		String Table_name = null;
		String Column_name = null;
		String SQL_condition = null;
		String Expected_value = "";
		String Actual_Value = null;
		try {
			if(strExecEventFlag==1){
				Table_name=RetrieveTestDataValue("EShopSQLDBSelectFromEnv",sqltablename,strExecEventFlag);
				Column_name=RetrieveTestDataValue("EShopSQLDBSelectFromEnv",strsqlcolumnname,strExecEventFlag);
				SQL_condition=RetrieveTestDataValue("EShopSQLDBSelectFromEnv",strsqlcondition,strExecEventFlag);
				Expected_value=RetrieveTestDataValue("EShopSQLDBSelectFromEnv",strExpectedvalue,strExecEventFlag);

			}

			if(Table_name==null){
				Report_Functions.ReportEventFailure(doc,  "EShopSQLDBSelectFromEnv",  "SQL table name is not provided in the Data Sheet.", false);
				Executionstatus= false;
				throw new RuntimeException ("Table_name is null");
			}

			if(Column_name==null){
				Report_Functions.ReportEventFailure(doc,  "EShopSQLDBSelectFromEnv",  "SQL Column name is not provided in the Data Sheet.", false);
				Executionstatus= false;
				throw new RuntimeException ("Column_name is null");
			}

			if(SQL_condition==null){
				Report_Functions.ReportEventFailure(doc,  "EShopSQLDBSelectFromEnv",  "SQL condition is not provided in the Data Sheet.", false);
				Executionstatus= false;
				throw new RuntimeException ("SQL_condition is null");
			}

			//Query to Execute      
			query = "select "+Column_name+" from "+Table_name+" where "+SQL_condition+"='"+EnvironmentValue.getProperty(NodeVal_to_Environment)+"'";
			System.out.println("query:"+query);

			Eshop_SQLServer = EShopstmt.executeQuery(query);


		} catch (Exception e) {
			Report_Functions.ReportEventFailure(doc,  "EShopSQLDBSelectFromEnv",  "Error occured while executing the SQL query.Error description is : "+ e.getMessage() +".", true);
			Log.info("EShopSQLDBSelectFromEnv Error : " + e);
			e.printStackTrace();
			Executionstatus=false;
		}

		try{
			Eshop_SQLServer.next();
			Actual_Value = Eshop_SQLServer.getString(1).trim();

		} catch (Exception e) {           // If no record is present in the fired Query
			Report_Functions.ReportEventFailure(doc,  "EShopSQLDBSelectFromEnv",  "No Record found for this query: "+query, true);
			Log.info("SQLDBSelect Error : ");
			Executionstatus=false;
		}

		try{
			if(!Eshop_SQLServer.wasNull()){            // If some value is present in the fired Query
				if(Actual_Value.equalsIgnoreCase(Expected_value.toString())){
					Report_Functions.ReportEventSuccess(doc, "1","EShopSQLDBSelectFromEnv", "The actual value : '"+Actual_Value+"' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' matches the expected value : '"+Expected_value+"'", 2);
					Executionstatus=true;
				}else if(!(Actual_Value.equals(Expected_value))){
					Report_Functions.ReportEventFailure(doc,  "EShopSQLDBSelectFromEnv",  "The actual value : '"+Actual_Value+"' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' does not match the expected value : '"+Expected_value+"'", true);
					Executionstatus=false;
				}
			}else if(Eshop_SQLServer.wasNull()){        // If "NULL" value is present in the fired Query
				if(Expected_value.equals("NULL")){
					Report_Functions.ReportEventSuccess(doc, "1", "EShopSQLDBSelectFromEnv", "The actual value 'NULL' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' matches with the expected value :'"+Expected_value+"'", 2);
					Executionstatus=true;
				}else if(!(Expected_value.equals("NULL"))){
					Report_Functions.ReportEventFailure(doc,  "EShopSQLDBSelectFromEnv",  "The actual value 'NULL' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' does not match the expected value : '"+Expected_value+"'", true);  	 
					Executionstatus=false;
				}
			}
		} catch (Exception e){
			Report_Functions.ReportEventFailure(doc,  "EShopSQLDBSelectFromEnv",  "Error occured while comparing the values in SQL query.Error description is : "+ e.getMessage() +".", true);
			Log.info("EShopSQLDBSelectFromEnv Error : " + e);
			Executionstatus=false;
		}
		return Executionstatus;
	}

	/**
	 * 
	 * @Objective <b>Verify to comapare EshopSQL DB environment variable</b>
	 * @param sqltablename
	 * @param strsqlcolumnname
	 * @param strsqlcondition
	 * @param Propertyfilename
	 * @param strEnvironmentVariable
	 * @param strExecEventFlag
	 * @author <b>Lakshman</b>
	 * @since <b>02-November-15</b>
	 */
	public static boolean EshopSQLDBEnvironmentVariableCompare(String sqltablename, String strsqlcolumnname,String strsqlcondition,String strEnvironmentVariable,int strExecEventFlag)throws Exception  {
		boolean EshopSQLDBEnvironmentVariableCompare= false;
		String query = null;
		String Table_name = null;
		String Column_name = null;
		String SQL_condition = null;
		String strExpectedvalue = null;
		String Actual_Value = null;
		try {
			if(strExecEventFlag==1){
				Table_name=RetrieveTestDataValue("EshopSQLDBEnvironmentVariableCompare",sqltablename,strExecEventFlag);
				Column_name=RetrieveTestDataValue("EshopSQLDBEnvironmentVariableCompare",strsqlcolumnname,strExecEventFlag);
				SQL_condition=RetrieveTestDataValue("EshopSQLDBEnvironmentVariableCompare",strsqlcondition,strExecEventFlag);
			}

			if(Table_name==null){
				Report_Functions.ReportEventFailure(doc,  "EshopSQLDBEnvironmentVariableCompare",  "SQL table name is not provided in the Data Sheet.", false);
				EshopSQLDBEnvironmentVariableCompare= false;
				throw new RuntimeException ("Table_name is null");
			}

			if(Column_name==null){
				Report_Functions.ReportEventFailure(doc,  "EshopSQLDBEnvironmentVariableCompare",  "SQL Column name is not provided in the Data Sheet.", false);
				EshopSQLDBEnvironmentVariableCompare= false;
				throw new RuntimeException ("Column_name is null");
			}

			if(SQL_condition==null){
				Report_Functions.ReportEventFailure(doc,  "EshopSQLDBEnvironmentVariableCompare",  "SQL condition is not provided in the Data Sheet.", false);
				EshopSQLDBEnvironmentVariableCompare= false;
				throw new RuntimeException ("SQL_condition is null");
			}


		}catch (Exception e) {
			Report_Functions.ReportEventFailure(doc,  "EshopSQLDBEnvironmentVariableCompare",  "Error occured .Error description is : "+ e.getMessage() +".", true);
			Log.info("EshopSQLDBEnvironmentVariableCompare Error : " + e);
			EshopSQLDBEnvironmentVariableCompare=false;
		}

		strExpectedvalue=EnvironmentValue.getProperty(strEnvironmentVariable);

		try {
			if(strExpectedvalue==null){
				Report_Functions.ReportEventFailure(doc,  "EshopSQLDBEnvironmentVariableCompare",  "The Value in environment variable: '"+ strEnvironmentVariable +"' is empty", true);
				EshopSQLDBEnvironmentVariableCompare=false;
			}else{
				//Query to Execute      
				query = "select "+Column_name+" from "+Table_name+" where "+SQL_condition+"";
				Eshop_SQLServer= EShopstmt.executeQuery(query);
			}
		} catch (Exception e){
			Report_Functions.ReportEventFailure(doc,  "EshopSQLDBEnvironmentVariableCompare",  "Error occured while executing the SQL query.Error description is : "+ e.getMessage() +".", true);
			Log.info("EshopSQLDBEnvironmentVariableCompare Error : " + e);
			EshopSQLDBEnvironmentVariableCompare=false;
		}

		try{
			Eshop_SQLServer.next();
			Actual_Value = Eshop_SQLServer.getString(1);
		} catch (Exception NullPointerException) {           // If no record is present in the fired Query
			Report_Functions.ReportEventFailure(doc,  "EshopSQLDBEnvironmentVariableCompare",  "No Record found for this query: "+query, true);
			Log.info("EshopSQLDBEnvironmentVariableCompare Error : ");
			EshopSQLDBEnvironmentVariableCompare=false;
		}

		try{
			if(!Eshop_SQLServer.wasNull()){            // If some value is present in the fired Query
				if(Actual_Value.equals(strExpectedvalue)){
					Report_Functions.ReportEventSuccess(doc, "1", "EshopSQLDBEnvironmentVariableCompare", "The selected value : '"+Actual_Value+"' in the column : '"+Column_name+"' of table : '"+Table_name+"' matches with the expected value : '"+strExpectedvalue+"' ", 2);
					EshopSQLDBEnvironmentVariableCompare=true;
				}else if(!(Actual_Value.equals(strExpectedvalue))){
					Report_Functions.ReportEventFailure(doc,  "EshopSQLDBEnvironmentVariableCompare",  "The Selected value : '"+Actual_Value+"' in the column : '"+Column_name+"' of table : '"+Table_name+"' does not match with the expected value : '"+strExpectedvalue+"' ", true);
					EshopSQLDBEnvironmentVariableCompare=false;
				}
			}else if(Eshop_SQLServer.wasNull()){        // If "NULL" value is present in the fired Query
				if(strExpectedvalue.equals("NULL")){
					Report_Functions.ReportEventSuccess(doc, "1", "EshopSQLDBEnvironmentVariableCompare", "The actual value 'NULL' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' matches with the expected value :'"+strExpectedvalue+"'", 2);
					EshopSQLDBEnvironmentVariableCompare=true;
				}else if(!(strExpectedvalue.equals("NULL"))){
					Report_Functions.ReportEventFailure(doc,  "EshopSQLDBEnvironmentVariableCompare",  "The actual value 'NULL' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' does not match the expected value : '"+strExpectedvalue+"'", true);  	 
					EshopSQLDBEnvironmentVariableCompare=false;
				}
			}
		}catch (Exception e) {
			Report_Functions.ReportEventFailure(doc,  "EshopSQLDBEnvironmentVariableCompare",  "Error occured while comparing the values in SQL Query.Error description is : "+ e.getMessage() +".", true);
			Log.info("EshopSQLDBEnvironmentVariableCompare Error : " + e);
			EshopSQLDBEnvironmentVariableCompare=false;
		}
		return EshopSQLDBEnvironmentVariableCompare;
	}

	/**
	 * 
	 * @Objective <b>To update the XML node value based on XPath</b>
	 * @param Location
	 * @param AttributeXPath
	 * @param strsqlcondition
	 * @param ValueToSet
	 * @param strExecEventFlag
	 * @author <b>Muralimohan M</b>
	 * @since <b>23-May-15</b>
	 */
	public static boolean XMLTextUpdate(String Location,String AttributeXPath,String ValueToSet,int strExecEventFlag) throws Exception, IOException,FileNotFoundException{
		System.out.println("XMLTextUpdate");

		if (Location==""){
			Report_Functions.ReportEventFailure(doc,  "XMLTextUpdate",  "Location Path for WebConfig is missing", true);
			Executionstatus=false;
		}
		if (AttributeXPath==""){
			Report_Functions.ReportEventFailure(doc,  "XMLTextUpdate",  "AttributeXPath Path for WebConfig is missing", true);
			Executionstatus=false;
		}
		if (ValueToSet==""){
			Report_Functions.ReportEventFailure(doc,  "XMLTextUpdate",  "ValueToSet in the node for WebConfig is missing", true);
			Executionstatus=false;
		}
		if(strExecEventFlag==1){
			System.out.println("strExecEventFlag");
			Location=RetrieveTestDataValue("XMLTextUpdate",Location,strExecEventFlag);
			Location=Param.getProperty(Location);
			AttributeXPath=RetrieveTestDataValue("XMLTextUpdate",AttributeXPath,strExecEventFlag);
			ValueToSet=RetrieveTestDataValue("XMLTextUpdate",ValueToSet,strExecEventFlag);

			ValueToSet=Param.getProperty(ValueToSet);
			if (ValueToSet==""){
				Report_Functions.ReportEventFailure(doc,  "XMLTextUpdate",  "Value present in Property File Seems to Empty.Please check the property file.", true);
				return Executionstatus;
			}
		}

		try {
			factory=DocumentBuilderFactory.newInstance();
			builder=factory.newDocumentBuilder();;
			file=new File("//\\"+Location);

			document=builder.parse(file);
			document.getDocumentElement().normalize();
			NodeList nodeList=null;
			xpath=XPathFactory.newInstance().newXPath();
			nodeList=(NodeList)xpath.compile(AttributeXPath).evaluate(document,XPathConstants.NODESET);

			nodeList.item(0).setTextContent(ValueToSet);
			TransformerFactory transFormerFactory = TransformerFactory.newInstance();
			Transformer transFormer = transFormerFactory.newTransformer();
			DOMSource source = new DOMSource(document);

			StreamResult result = new StreamResult(file);
			transFormer.transform(source, result);
			result.getOutputStream().close();
			Executionstatus=true;
			Report_Functions.ReportEventSuccess(doc, "1", "XMLTextUpdate", "XML config File '"+Location+"' has been updated successfully for the tag '"+AttributeXPath+"' with the value set as '"+ValueToSet+"'", 2);
			Thread.sleep(2000);
		} catch (ParserConfigurationException e) {
			System.out.println("catch ParserConfigurationException");
			Report_Functions.ReportEventFailure(doc,  "XMLTextUpdate",  "ValueToSet in the node for WebConfig is Not successfull due to reason: '"+e.getMessage()+"'", false);
			Executionstatus=false;

		} catch (Exception e) {
			System.out.println("catch Excpetion");
			Report_Functions.ReportEventFailure(doc,  "XMLTextUpdate",  "Exception occured during XMLTextUpdate. Reason: '"+e.getMessage()+"'", false);
			Executionstatus=false;
		}
		builder=null;
		xpath=null;
		nodeList=null;
		return Executionstatus;
	}

	/**
	 * @Description <b>To validate the headers coming as response using regular Expression pattern</b>
	 * @author<b>Muralimohan M</b>
	 * @param <b>Connection Parameter are coming from Param.Properties File</b>
	 * @return<b>boolean</b>
	 * */
	public static boolean Verify_XML_Header_Footer_Validation(String XML_Validation_Part,String XML_To_Validate) throws Exception{
		Executionstatus=false;
		boolean matchFoundPattern=RegExpValidator(XML_To_Validate, XML_Validation_Part);
		if(matchFoundPattern){
			Executionstatus=true;
			Report_Functions.ReportEventSuccess(doc, "1", "Verify_XML_Header_Footer_Validation", "'"+XML_Validation_Part+"' is  Present in the Response XML", 2);
		}else{
			Executionstatus=false;
			Report_Functions.ReportEventFailure(doc,  "Verify_XML_Header_Footer_Validation",  "'"+XML_Validation_Part+"' is not Present in the Response XML", true);	
		}
		return Executionstatus;

	}

	/**
	 * 
	 * @Objective <b>To Change the Expected value from testdata in to Regular Expression Pattern(Contd)
	 * for the actual value from XML response(Applicable for the response coming with(Contd)
	 * Regular Expression validation for the Application</b>
	 * @param strExpectedvalue
	 * @author <b>Muralimohan M</b>
	 * @since <b>23-May-15</b>
	 */
	public static String ErrorDescriptionRegExpProcessor(String strExpectedvalue){
		String []splitExpectedData=strExpectedvalue.split(":");
		String FormRegularExpressionWithExpectedResult=splitExpectedData[0]+":"+splitExpectedData[1]+":[0-9]+:[0-9]+\\s+error:\\s+";
		String LastPart=splitExpectedData[4].replace("\\", "\\\\");
		LastPart=LastPart.replace("{","\\{");
		LastPart=LastPart.replace("}", "\\}");
		LastPart=LastPart.replace("(", "\\(");
		LastPart=LastPart.replace(")", "\\)");
		LastPart=LastPart.replace("|", "\\|");
		LastPart=LastPart.replace("[", "\\[");
		LastPart=LastPart.replace("]", "\\]");
		LastPart=LastPart.replace("*", "\\*");
		LastPart=LastPart.replace("+", "\\+");
		LastPart=LastPart.replace("?", "\\?");
		LastPart=LastPart.replace("$", "\\$");
		LastPart=LastPart.replace("%", "\\%");
		LastPart=LastPart.replace("^", "\\^");
		FormRegularExpressionWithExpectedResult=FormRegularExpressionWithExpectedResult+(LastPart.trim());
		System.out.println("LastPart : "+FormRegularExpressionWithExpectedResult);
		return FormRegularExpressionWithExpectedResult;

	}

	/**
	 * @Description  <b>To open Connection to the unix Server</b>
	 * @author <b>muralimohan</b>
	 * @param <b>Connection Parameter are coming from Param.Properties File</b>
	 * @return <b>boolean</b>
	 * */
	public static boolean jsh_Unix_Open_Connection() throws Exception{
		Executionstatus=false;
		jsch=new JSch();
		String ipaddress=Param.getProperty("Unix_ITG_Server_IP");
		String username =Param.getProperty("Unix_ITG_Server_Username");
		String password =Param.getProperty("Unix_ITG_Server_Password");

		try {
			JSHsession=jsch.getSession(username, ipaddress);
			JSHsession.setPassword(password);
			Properties JSHProperties=new Properties();
			JSHProperties.put("StrictHostKeyChecking", "no");
			JSHsession.setConfig(JSHProperties);
			JSHsession.connect();


			if(JSHsession.isConnected()){
				Report_Functions.ReportEventSuccess(doc, "1", "Open_Unix_Connection", "UNIX Server with IP Address '"+ipaddress+"' has been connected successfully", 2);
				Executionstatus=true;
			}else{
				Report_Functions.ReportEventFailure(doc,  "Open_Unix_Connection",  "Unable to connect to the UNIX Server with IP Address==> "+ipaddress+".", false);
				Executionstatus=true;
			}

		} catch (JSchException e) {
			Report_Functions.ReportEventFailure(doc,  "Open_Unix_Connection",  "Unable to connect to the UNIX Server with IP Address==> "+ipaddress+"."+e.getMessage(), false);
			//e.printStackTrace();
			return Executionstatus;
		}
		return Executionstatus;
	}

	/**
	 * 
	 * @Objective <b>This method is to Close Unix connection to the server<b>
	 * @return boolean
	 * @author <b>Muralimohan M</b>
	 * @since <b>17-May-16</b>
	 */
	public static boolean Jsh_closeUnixSession() throws Exception{
		Executionstatus=false;
		try{
			channel.disconnect();
			JSHsession.disconnect();
			if(JSHsession.isConnected()){
				Report_Functions.ReportEventFailure(doc,  "Jsh_closeUnixSession",  "Unable to disconnect  UNIX Server with IP Address==> "+Param.getProperty("Unix_ITG_Server_IP")+".", false);
				Executionstatus=false;
			}else{
				Report_Functions.ReportEventSuccess(doc, "1", "Jsh_closeUnixSession", "Unix Connection disconnected successfully", 2);
				Executionstatus=true;
			}
		}catch(Exception e){
			Report_Functions.ReportEventFailure(doc,  "closeUnixSession",  "Unable to disconnect  UNIX Server with IP Address "+Param.getProperty("Unix_ITG_Server_IP")+".Exception Occured:"+e.getMessage(), false);
			Log.info("Close Unix connection- Exception occured==>"+e.getMessage());
			return Executionstatus;
		}
		return Executionstatus;
	}

	/**
	 * 
	 * @Objective <b>This method is to Pass the command to Unix Server and return the output from the server<b>
	 * @return
	 * @author <b>Muralimohan M</b>
	 * @since <b>17-May-16</b>
	 */
	public static boolean Unix_ITG_OutputResult_Verify(String Command,String ExpectedOutput,int StrEventExecFlag,String StrEntityName) throws Exception{
		Executionstatus=false;
		if(StrEventExecFlag==1){
			EntityName=RetrieveTestDataValue("Unix_ITG_OutputResult_Verify",StrEntityName,1);
			ExpectedOutput=RetrieveTestDataValue("Unix_ITG_OutputResult_Verify",ExpectedOutput, 1);
		}

		if(EntityName!=""||EntityName!=null){
			NSS_URL_PATH="ITG_NSS_Auto_Scripts_Path_"+EntityName;
			ITG_NSS_Auto_Scripts_Path=Param.getProperty(NSS_URL_PATH);

		}else{
			Report_Functions.ReportEventFailure(doc,  "Unix_ITG_OutputResult_Verify",  "EntityName  value is not provided in the testdata", false);
			return Executionstatus;
		}

		NavigateUnixAutomationPath=Generate_Runtime_Data_ITG("PathCommand",EntityName);
		NSSScriptCommand=Generate_Runtime_Data_ITG(Command,EntityName);

		try{
			channel=JSHsession.openChannel("exec");
			exec=(ChannelExec)channel;
			exec.setCommand(NavigateUnixAutomationPath+";"+NSSScriptCommand);
			exec.connect();
			in = new BufferedInputStream(exec.getInputStream());

			byte[] contents = new byte[1024];
			int bytesRead=0;
			String strInputStream = "";

			while( (bytesRead = in.read(contents)) != -1){
				strInputStream += new String(contents, 0, bytesRead);
			}
			Report_Functions.ReportEventSuccess(doc, "1", "Unix_ITG_OutputResult_Verify", "Command Executed for "+Command+": '"+NSSScriptCommand+"' Executed successfully", 2);
			Unix_Output=strInputStream;
			Executionstatus=RegExpValidator(Unix_Output, ExpectedOutput);

			if(Executionstatus){
				Report_Functions.ReportEventSuccess(doc, "1", "Unix_ITG_OutputResult_Verify", "Output from Unix server '"+Unix_Output+"' matched with with Expected output '"+ExpectedOutput+"'", 2);
				Executionstatus=true;
			}else{
				Report_Functions.ReportEventFailure(doc,  "Unix_ITG_OutputResult_Verify",  "Output from Unix server '"+Unix_Output+" ' did not matched with Expected output '"+ExpectedOutput+"'", false);
				Executionstatus=false;
			}


		}catch(Exception  e){
			Report_Functions.ReportEventFailure(doc,  "Unix_ITG_OutputResult_Verify",  "Output from Unix server '"+Unix_Output+" ' did not matched with Expected output '"+ExpectedOutput+"'.Exception Occured:"+e.getMessage(), false);
			//	Log.info(e.getStackTrace());
			return Executionstatus;
		}
		finally{
			Unix_Output=null;
			in.close();
			exec.disconnect();

		}
		return Executionstatus;
	}

	/**
	 * 
	 * @Objective <b>This method is used to update current date as well as future date in SQL Server<b>
	 * @return
	 * @author <b>Karthik</b>
	 * @since <b>9-Jun-16</b>
	 */
	public static boolean SQLDBFutureDateUpdate(String sqltablename, String strsqlcolumnname,String strsqlnoofdays,String strsqlcondition,int strExecEventFlag)throws Exception  {
		boolean SQLDBFutureDateUpdate= false;
		String Table_name = null;
		String Column_name = null;
		String No_of_Days = null;
		String SQL_condition = null;

		try {
			if(strExecEventFlag==1){
				Table_name=RetrieveTestDataValue("SQLDBFutureDateUpdate",sqltablename,strExecEventFlag);
				Column_name=RetrieveTestDataValue("SQLDBFutureDateUpdate",strsqlcolumnname,strExecEventFlag);
				No_of_Days=RetrieveTestDataValue("SQLDBFutureDateUpdate",strsqlnoofdays,strExecEventFlag);
				SQL_condition=RetrieveTestDataValue("SQLDBFutureDateUpdate",strsqlcondition,strExecEventFlag);
			}

			if(Table_name==null){
				Report_Functions.ReportEventFailure(doc,  "SQLDBFutureDateUpdate",  "SQL table name is not provided in the Data Sheet.", false);
				SQLDBFutureDateUpdate= false;
				throw new RuntimeException ("Table_name is null");
			}

			if(Column_name==null){
				Report_Functions.ReportEventFailure(doc,  "SQLDBFutureDateUpdate",  "SQL Column name is not provided in the Data Sheet.", false);
				SQLDBFutureDateUpdate= false;
				throw new RuntimeException ("Column_name is null");
			}

			if(No_of_Days==null){
				Report_Functions.ReportEventFailure(doc,  "SQLDBFutureDateUpdate",  "SQL No_of_Days is not provided in the Data Sheet.", false);
				SQLDBFutureDateUpdate= false;
				throw new RuntimeException ("No_of_Days is null");
			}

			if(SQL_condition==null){
				Report_Functions.ReportEventFailure(doc,  "SQLDBFutureDateUpdate",  "SQL condition is not provided in the Data Sheet.", false);
				SQLDBFutureDateUpdate= false;
				throw new RuntimeException ("SQL_condition is null");
			}

			//Query to Execute      
			String query = "update "+Table_name+" set "+Column_name+" = GETDATE()+"+No_of_Days+" where "+SQL_condition;
			System.out.println("query update:"+query);
			stmt.execute(query);
			SQLDBFutureDateUpdate=true;
			Report_Functions.ReportEventSuccess(doc, "1", "SQLDBFutureDateUpdate", "The SQL Update Query : "+ query + " executed successfully.", 2);

		} catch (Exception e) {
			SQLDBFutureDateUpdate=false;
			Report_Functions.ReportEventFailure(doc,  "SQLDBFutureDateUpdate",  "Error occured while executing the SQL query.Error description is : "+ e.getMessage() +".", true);
			Log.info("SQLDBFutureDateUpdate Error : " + e);
		}
		return SQLDBFutureDateUpdate;
	}

	/**
	 * 
	 * @Objective <b>This method is used to insert a record in RRBS DB and verify whether it is successfully inserted<b>
	 * @return
	 * @author <b>Karthik</b>
	 * @since <b>15-Jun-16</b>
	 */
	public static boolean RRBSDBInsert(String sqltablename, String strsqlcolumnnames, String strsqlcolumnvalues, int strExecEventFlag) throws Exception  {
		boolean RRBSDBInsert= false;
		String Table_name = null;
		String Column_names = null;
		String Column_Values = null;

		try {
			if(strExecEventFlag==1){
				Table_name=RetrieveTestDataValue("RRBSDBInsert",sqltablename,strExecEventFlag);
				Column_names=RetrieveTestDataValue("RRBSDBInsert",strsqlcolumnnames,strExecEventFlag);
				Column_Values=RetrieveTestDataValue("RRBSDBInsert",strsqlcolumnvalues,strExecEventFlag);
			}

			if(Table_name==null){
				Report_Functions.ReportEventFailure(doc,  "RRBSDBInsert",  "RRBS table name is not provided in the Data Sheet.", false);
				RRBSDBInsert= false;
				throw new RuntimeException ("Table_name is null");
			}

			if(Column_names==null){
				Report_Functions.ReportEventFailure(doc,  "RRBSDBInsert",  "RRBS Column names is not provided in the Data Sheet.", false);
				RRBSDBInsert= false;
				throw new RuntimeException ("Column_names is null");
			}

			if(Column_Values==null){
				Report_Functions.ReportEventFailure(doc,  "RRBSDBInsert",  "RRBS Column_Values is not provided in the Data Sheet.", false);
				RRBSDBInsert= false;
				throw new RuntimeException ("Column_Values is null");
			}

			//Query to Execute      
			String query = "INSERT INTO "+Table_name+" ("+Column_names+") VALUES ("+Column_Values+")";
			System.out.println("Insert query is :"+query);
			try{
				rrbsstatement.executeUpdate(query);
				Report_Functions.ReportEventSuccess(doc, "1", "RRBSDBInsert", "Records are  inserted into  : "+ Table_name + " successfully with column values '"+Column_Values+"' against column names '"+Column_names+"'", 2);
				RRBSDBInsert=true;
			}catch(Exception e1){
				RRBSDBInsert=false;
				e1.printStackTrace();
				Report_Functions.ReportEventFailure(doc,"RRBSDBInsert", "Records are not inserted into  : "+ Table_name + " successfully with column values '"+Column_Values+"' against column names '"+Column_names+"' Exception:"+e1.getMessage(), true);
			}
		} catch (Exception e) {
			RRBSDBInsert=false;
			Report_Functions.ReportEventFailure(doc,  "RRBSDBInsert",  "Error occured while inserting the record in RRBS DB. Error description is : "+ e.getMessage() +".", true);
			Log.info("RRBSDBInsert Error : " + e);
		}

		return RRBSDBInsert;
	}



	//*************************************************************************************************************************************************

	public static boolean Verify_XML_Response_Data_DB(String RequestName,String Input_DATAXML,String Output_DATAXML,String XLDB_Input_Query, String SQLQuery,String ParentNode,String PrimaryNode,String DynamicNodes,String Entity_Name,int strExecEventFlag)throws Exception  {
		Connection Sqlconnection=null;
		Statement SQLStmt=null;
		String DynamicNodes_Value=null;
		String constructXpath=null;
		String NodeValue=null;
		String ExpectedNodeValue=null;
		String XML_Request=null;
		int result=0;
		String Request_Name=null;
		String Updated_XML_Request=null;
		boolean Verify_XML_Response_Data_DB=false;

		if(strExecEventFlag==1){
			Request_Name=RetrieveTestDataValue("Verify_XML_Response_Data_DB",RequestName,strExecEventFlag);
			Input_DATAXML=RetrieveTestDataValue("Verify_XML_Response_Data_DB",Input_DATAXML,strExecEventFlag);
			XLDB_Input_Query=RetrieveTestDataValue("Verify_XML_Response_Data_DB", XLDB_Input_Query, strExecEventFlag);
			Output_DATAXML=RetrieveTestDataValue("Verify_XML_Response_Data_DB", Output_DATAXML, strExecEventFlag);
			SQLQuery=RetrieveTestDataValue("Verify_XML_Response_Data_DB", SQLQuery, strExecEventFlag);
			ParentNode=RetrieveTestDataValue("Verify_XML_Response_Data_DB", ParentNode, strExecEventFlag);
			PrimaryNode=RetrieveTestDataValue("Verify_XML_Response_Data_DB", PrimaryNode, strExecEventFlag);
			DynamicNodes_Value=RetrieveTestDataValue("Verify_XML_Response_Data_DB", DynamicNodes, strExecEventFlag);
			EntityName=RetrieveTestDataValue("Verify_XML_Response_Data_DB", Entity_Name, strExecEventFlag);
		}
		NSS_URL_PATH="Request_URL_"+EntityName;
		AppURL=Param.getProperty(NSS_URL_PATH);
		try{
			//Reading testdata from XMLTemplate
			XML_Request=ReadExcel.RetrieveValueFromTestDataBasedOnQuery(Param.getProperty("SupportFiles_Location"),Param.getProperty("XML_Template_File_Name"),"Input_XML",XLDB_Input_Query);
			factory=DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder=factory.newDocumentBuilder();
			StringBuilder xmlBuilder=new StringBuilder();
			xmlBuilder.append(XML_Request);
			ByteArrayInputStream  is;
			try{
				is=new ByteArrayInputStream(xmlBuilder.toString().getBytes("UTF-8"));
				document=builder.parse(is);
			}catch(Exception e){
				e.printStackTrace();
			}
			//updating node values from XMLtemplate based on input data from testdata sheet


			try{
				updateNodeValue(document,Input_DATAXML,EntityName);
				Updated_XML_Request = DocumentToString(document);
				PostMethod post = new PostMethod(AppURL+Request_Name+"/");
				post.setRequestBody(Updated_XML_Request);
				HttpClient httpclient = new HttpClient();
				try{
					result = httpclient.executeMethod(post);
					Report_Functions.ReportEventSuccess(doc, "1", "Verify_XML_Response_Data", "The xml request : '"+ Updated_XML_Request + "' has been sent successfully.", 2);
				}catch(Exception httpException){
					httpException.printStackTrace();
					Report_Functions.ReportEventFailure(doc,  "Verify_XML_Response_Data",  "httpException:Error occured while sending the request to the server."+httpException.getMessage(), false);	
				}
				gbstrXMLResponseData= post.getResponseBodyAsString();

				//To Enable SOAP XML Header Validations

				if(result==200){
					Executionstatus=true;
					Report_Functions.ReportEventSuccess(doc, "1", "Verify_XML_Response_Data", "The xml response : '"+ gbstrXMLResponseData.toString() + "' received successfully.", 2);
					//validating XML headers and footers
					if(Param.getProperty("SOAP_Validation_Enable").equalsIgnoreCase("true")){

						Executionstatus=Verify_XML_Header_Footer_Validation(Param.getProperty("XML_Envelope"), gbstrXMLResponseData);
						Executionstatus=Verify_XML_Header_Footer_Validation(Param.getProperty("XML_Header"), gbstrXMLResponseData);
						Executionstatus=Verify_XML_Header_Footer_Validation(Param.getProperty("XML_LastLine"), gbstrXMLResponseData);
						Executionstatus=Verify_XML_Header_Footer_Validation(Param.getProperty("XML_LastsecondLine"), gbstrXMLResponseData);
						Executionstatus=Verify_XML_Header_Footer_Validation(Param.getProperty("XML_ClosingHeader"), gbstrXMLResponseData);
						Executionstatus=Verify_XML_Header_Footer_Validation(Param.getProperty("XML_OpeningBody"), gbstrXMLResponseData);
					}
					try{
						xpathfactory = XPathFactory.newInstance();
						xpath = XPathFactory.newInstance().newXPath();
						factory=DocumentBuilderFactory.newInstance();
						builder=factory.newDocumentBuilder();
						document=builder.parse(new ByteArrayInputStream(gbstrXMLResponseData.getBytes("UTF-8")));

						//Open SQL Connection to retrieve the data specific to the testcase
						SQLDBOpenConnection(Param.getProperty("SQL_Server"), Param.getProperty("SQL_Server_DB_Name"),Param.getProperty("SQL_Server_UID"), Param.getProperty("SQL_Server_PWD"));
						rs_SQLServer = stmt.executeQuery(SQLQuery);

						//Validating static nodes

						String[] Output_data = Output_DATAXML.split(";");

						for (int i = 0; i < Output_data.length; i++) {
							String[] Output_XML_Info = Output_data[i].split("=",2);
							String node_value=(String)xpath.compile("*//"+Output_XML_Info[0]).evaluate(document,XPathConstants.STRING);
							String[] ActualValue=Output_XML_Info[1].split(":");
							int arrLength=ActualValue.length;
							//TO validate ERROR_CODE tag- String to String match

							if( Output_XML_Info[0].contains("ERROR_CODE") ||  Output_XML_Info[0].contains("EXPIRY_DATE_TIME")){
								Executionstatus=StringValidator(node_value,Output_XML_Info[1]);	
							}else{

								if(arrLength>1 ){
									ProcessedXML=ErrorDescriptionRegExpProcessor(Output_XML_Info[1]);
									Executionstatus=RegExpValidator(node_value,ProcessedXML);


								}else
									if(Output_XML_Info[0].equalsIgnoreCase("TRANSACTION_NUMBER")){
										Executionstatus=RegExpValidator(node_value,Output_XML_Info[1]);
									}

									else{
										Executionstatus=StringValidator(node_value,Output_XML_Info[1]);
									}
							}
							if(Executionstatus==true){
								Report_Functions.ReportEventSuccess(doc, "1", "Verify_XML_Response_Data_NodeValtoEnv", "The actual node value of  : '//"+ Output_XML_Info[0] + "' is '"+ node_value +"' and matched with the expected value '"+ Output_XML_Info[1] +"'.", 2);
								Executionstatus=true;;
							} else{
								Report_Functions.ReportEventFailure(doc,  "Verify_XML_Response_Data_NodeValtoEnv",  "The actual node value of  : '//"+ Output_XML_Info[0] + "' is '"+ node_value +"' and no matching with the expected value '"+ Output_XML_Info[1] +"'.", false);
								Executionstatus=false;
							}
						}



						//validating dynamic nodes
						int recordCount=0;
						while(rs_SQLServer.next()){
							String[] Output_XML_Info = PrimaryNode.split("=");
							String[] DynamicNodeValues=DynamicNodes_Value.split(";");
							//Splitting the dynamic values and validate each node against database value
							for (int i=0;i<DynamicNodeValues.length;i++){
								String[] DynamicNode=DynamicNodeValues[i].split("=");
								constructXpath=".//"+ParentNode+"["+Output_XML_Info[0]+"='"+rs_SQLServer.getString(Output_XML_Info[1])+"']/"+DynamicNode[0];
								NodeValue=(String)xpath.compile(constructXpath).evaluate(document, XPathConstants.STRING);
								//MODE is specific to testcase[GET_BUNDLE_DETAILS]
								if (DynamicNode[0].equalsIgnoreCase("MODE")){
									//Setting the expected value based on db value
									ExpectedNodeValue=rs_SQLServer.getString(DynamicNode[1]);
									//below logic was implemented for a specific testcase
									//***start
									if (ExpectedNodeValue.contentEquals("1")){
										ExpectedNodeValue="Fixed";
									}else{
										ExpectedNodeValue="Variable";
									}

									//***fixed
								}else{

									try{
										ExpectedNodeValue=rs_SQLServer.getString(DynamicNode[1]);
									}catch(Exception e1){
										if(rs_SQLServer.wasNull()){
											ExpectedNodeValue="";
										}
									}
								}
								//validates the nodevalue against expected node value
								Verify_XML_Response_Data_DB=StringValidator(NodeValue,ExpectedNodeValue);
								if(Verify_XML_Response_Data_DB){
									Report_Functions.ReportEventSuccess(doc, "1", "Verify_XML_Response_Data_DB", "The actual node value of  : '"+ constructXpath + "' is '"+ NodeValue +"' and matched with the expected value '"+ ExpectedNodeValue +"'.", 2);
									Verify_XML_Response_Data_DB=true;
								} else{
									Report_Functions.ReportEventFailure(doc,  "Verify_XML_Response_Data_DB",  "The actual node value of  : '"+ constructXpath + "' is '"+ NodeValue +"' and no matching with the expected value '"+ ExpectedNodeValue +"'.", false);
									Verify_XML_Response_Data_DB=false;
								}
							}
							//this is to find number of records in db
							recordCount=recordCount+1;
						}
						//Take parent node count
						System.out.println("ParentNode:"+ParentNode);
						NodeList parentnodecount=(NodeList)xpath.compile("//"+ParentNode).evaluate(document, XPathConstants.NODESET);
						int numberofretrievedNodesResponse=parentnodecount.getLength();
						System.out.println("numberofretrievedNodesResponse:"+numberofretrievedNodesResponse);

						if(recordCount==numberofretrievedNodesResponse){
							Report_Functions.ReportEventSuccess(doc, "1", "Verify_XML_Response_Data_DB", "Number of records retrieved in XML '"+numberofretrievedNodesResponse+"' matched with the number of records present in database '"+recordCount+"' for the provided input query", 2);
							Verify_XML_Response_Data_DB=true;
						}else{
							Report_Functions.ReportEventFailure(doc,  "Verify_XML_Response_Data_DB",  "Number of records retrieved in XML '"+numberofretrievedNodesResponse+"' did not matched with the number of records present in database '"+recordCount+"' for the provided input query", false);	
							Verify_XML_Response_Data_DB=false;
						}

					}catch(Exception responseXMLException){
						Report_Functions.ReportEventFailure(doc,  "Verify_XML_Response_Data_DB",  "responseXMLException:Error in Parsing response XML.:"+responseXMLException.getMessage(), false);	
						Verify_XML_Response_Data_DB=false;
					}
				}


			}catch(Exception xmlFormException){
				Report_Functions.ReportEventFailure(doc,  "Verify_XML_Response_Data_DB",  "responseXMLException:Error in Parsing response XML.:"+xmlFormException.getMessage(), false);	
				Verify_XML_Response_Data_DB=false;
			}

		}catch(Exception e){
			return Verify_XML_Response_Data_DB;
		}finally{
			xpathfactory=null;
			document=null;
			factory=null;
			ProcessedXML=null;
			//closing the DB Connection at any cause
			stmt.close();
			rs_SQLServer.close();
			con.close();
		}

		return Verify_XML_Response_Data_DB;

	}



	/**
	 * 
	 * @Objective <b>Verifies to check date comparision in SQL DB</b>
	 * @param sqltablename
	 * @param strsqlcolumnname
	 * @param strsqlcondition
	 * @param Date_Format
	 * @param strExecEventFlag
	 * @author <b>Lakshman</b>
	 * @since <b>30-AUG-16</b>
	 */

	public static boolean SQLDBDateFormatCompare(String sqltablename, String strsqlcolumnname,String strsqlcondition,String dateFormatFromExcel,int strExecEventFlag)throws Exception  {
		boolean SQLDBDateCompare= false;
		String query = null;  
		String Table_name = null;
		String Column_name = null;
		String SQL_condition = null;
		String Expected_value = null;
		String Actual_Value = null;
		String Current_Date=null;
		String expected_db_Date = null;
		String Date_Format=null;

		try {
			if(strExecEventFlag==1){
				Table_name=RetrieveTestDataValue("SQLDBDateCompare",sqltablename,strExecEventFlag);
				Column_name=RetrieveTestDataValue("SQLDBDateCompare",strsqlcolumnname,strExecEventFlag);
				SQL_condition=RetrieveTestDataValue("SQLDBDateCompare",strsqlcondition,strExecEventFlag);
				Date_Format=RetrieveTestDataValue("SQLDBDateCompare",dateFormatFromExcel,strExecEventFlag);
			}

			log.info("Table_name is : "+Table_name);
			log.info("Column_name is : "+Column_name);
			log.info("SQL_condition is : "+SQL_condition);
			log.info("Date_Format is : "+Date_Format);

			if(Table_name==null || Column_name==null || SQL_condition==null || Date_Format ==null){
				Report_Functions.ReportEventFailure(doc,  "SQLDBDateCompare",  "Required details are not provided in test data sheet.", false);
				return false;
			}

			//DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			DateFormat dateFormat = new SimpleDateFormat(Date_Format);
			Date date = new Date();
			Current_Date = dateFormat.format(date);
			Expected_value = Current_Date.trim();

			// Query to Execute      
			query = "select "+Column_name+" from "+Table_name+" where "+SQL_condition+"";
			rs_SQLServer= stmt.executeQuery(query);

			log.info("Quer is : "+query);

		}catch (Exception e){
			Report_Functions.ReportEventFailure(doc,  "SQLDBDateCompare",  "Error occured while executing the SQL query.Error description is : "+ e.getMessage() +".", true);
			log.info("SQLDBDateCompare Error : " + e);
			SQLDBDateCompare=false;
		}

		try{
			rs_SQLServer.next();
			Actual_Value = rs_SQLServer.getString(1);
			String db_Date = Actual_Value.split(" ")[0];

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
			Date dateToChange = dateFormat.parse(db_Date);

			SimpleDateFormat finalDateFormat = new SimpleDateFormat(Date_Format);
			expected_db_Date = finalDateFormat.format(dateToChange);	

		} catch (Exception NullPointerException) {           // If no record is present in the fired Query
			Report_Functions.ReportEventFailure(doc,  "SQLDBDateCompare",  "No Record found for this query: "+query, true);
			log.info("SQLDBDateCompare Error : ");
			SQLDBDateCompare=false;
		}

		try{

			if(!rs_SQLServer.wasNull()){            // If some value is present in the fired Query
				if(expected_db_Date.equalsIgnoreCase(Expected_value)){
					Report_Functions.ReportEventSuccess(doc, "1", "SQLDBDateCompare", "The selected date : '"+expected_db_Date+"' in the column : '"+Column_name+"' of table : '"+Table_name+"' matches with the expected date : '"+Expected_value+"' ", 2);
					SQLDBDateCompare=true;
				}else if(!(expected_db_Date.equalsIgnoreCase(Expected_value))){
					Report_Functions.ReportEventFailure(doc,  "SQLDBDateCompare",  "The selected date : '"+expected_db_Date+"' in the column : '"+Column_name+"' of table : '"+Table_name+"' does not match with the expected date : '"+Expected_value+"' ", true);
					SQLDBDateCompare=false;
				}
			}else if(rs_SQLServer.wasNull()){        // If "NULL" value is present in the fired Query
				if(Expected_value.equalsIgnoreCase("NULL")){
					Report_Functions.ReportEventSuccess(doc, "1", "SQLDBDateCompare", "The actual value 'NULL' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' matches with the expected value :'"+Expected_value+"'", 2);
					SQLDBDateCompare=true;
				}else if(!(Expected_value.equalsIgnoreCase("NULL"))){
					Report_Functions.ReportEventFailure(doc,  "SQLDBDateCompare",  "The actual value 'NULL' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' does not match the expected value : '"+Expected_value+"'", true);  	 
					SQLDBDateCompare=false;
				}
			}

		}catch (Exception e){
			Report_Functions.ReportEventFailure(doc,  "SQLDBDateCompare",  "Error occured while comparing the dates in SQL query.Error description is : "+ e.getMessage() +".", true);
			log.info("SQLDBDateCompare Error : " + e);
			SQLDBDateCompare=false;
		}
		return SQLDBDateCompare;
	}

	/**
	 * 
	 * @Objective <b>Verifies to check date comparision in SQL DB</b>
	 * @param sqltablename
	 * @param strsqlcolumnname
	 * @param strsqlcondition
	 * @param Date_Format
	 * @param strExecEventFlag
	 * @author <b>Lakshman</b>
	 * @since <b>30-AUG-16</b>
	 */

	public static boolean EshopSQLDBDateFormatCompare(String sqltablename, String strsqlcolumnname,String strsqlcondition,String dateFormatFromExcel,int strExecEventFlag)throws Exception  {
		boolean EshopSQLDBDateFormatCompare= false;
		String query = null;  
		String Table_name = null;
		String Column_name = null;
		String SQL_condition = null;
		String Expected_value = null;
		String Actual_Value = null;
		String Current_Date=null;
		String expected_db_Date = null;
		String Date_Format=null;

		try {
			if(strExecEventFlag==1){
				Table_name=RetrieveTestDataValue("EshopSQLDBDateFormatCompare",sqltablename,strExecEventFlag);
				Column_name=RetrieveTestDataValue("EshopSQLDBDateFormatCompare",strsqlcolumnname,strExecEventFlag);
				SQL_condition=RetrieveTestDataValue("EshopSQLDBDateFormatCompare",strsqlcondition,strExecEventFlag);
				Date_Format=RetrieveTestDataValue("EshopSQLDBDateFormatCompare",dateFormatFromExcel,strExecEventFlag);
			}

			log.info("Table_name is : "+Table_name);
			log.info("Column_name is : "+Column_name);
			log.info("SQL_condition is : "+SQL_condition);
			log.info("Date_Format is : "+Date_Format);

			if(Table_name==null || Column_name==null || SQL_condition==null || Date_Format ==null){
				Report_Functions.ReportEventFailure(doc,  "EshopSQLDBDateFormatCompare",  "Required details are not provided in test data sheet.", false);
				return false;
			}

			//DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			DateFormat dateFormat = new SimpleDateFormat(Date_Format);
			Date date = new Date();
			Current_Date = dateFormat.format(date);
			Expected_value = Current_Date.trim();

			// Query to Execute      
			query = "select "+Column_name+" from "+Table_name+" where "+SQL_condition+"";
			Eshop_SQLServer= EShopstmt.executeQuery(query);

			log.info("Quer is : "+query);

		}catch (Exception e){
			Report_Functions.ReportEventFailure(doc,  "EshopSQLDBDateFormatCompare",  "Error occured while executing the SQL query.Error description is : "+ e.getMessage() +".", true);
			log.info("EshopSQLDBDateFormatCompare Error : " + e);
			EshopSQLDBDateFormatCompare=false;
		}

		try{
			Eshop_SQLServer.next();
			Actual_Value = Eshop_SQLServer.getString(1);
			String db_Date = Actual_Value.split(" ")[0];

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
			Date dateToChange = dateFormat.parse(db_Date);

			SimpleDateFormat finalDateFormat = new SimpleDateFormat(Date_Format);
			expected_db_Date = finalDateFormat.format(dateToChange);	

		} catch (Exception NullPointerException) {           // If no record is present in the fired Query
			Report_Functions.ReportEventFailure(doc,  "EshopSQLDBDateFormatCompare",  "No Record found for this query: "+query, true);
			log.info("SQLDBDateCompare Error : ");
			EshopSQLDBDateFormatCompare=false;
		}

		try{

			if(!Eshop_SQLServer.wasNull()){            // If some value is present in the fired Query
				if(expected_db_Date.equalsIgnoreCase(Expected_value)){
					Report_Functions.ReportEventSuccess(doc, "1", "EshopSQLDBDateFormatCompare", "The selected date : '"+expected_db_Date+"' in the column : '"+Column_name+"' of table : '"+Table_name+"' matches with the expected date : '"+Expected_value+"' ", 2);
					EshopSQLDBDateFormatCompare=true;
				}else if(!(expected_db_Date.equalsIgnoreCase(Expected_value))){
					Report_Functions.ReportEventFailure(doc,  "EshopSQLDBDateFormatCompare",  "The selected date : '"+expected_db_Date+"' in the column : '"+Column_name+"' of table : '"+Table_name+"' does not match with the expected date : '"+Expected_value+"' ", true);
					EshopSQLDBDateFormatCompare=false;
				}
			}else if(Eshop_SQLServer.wasNull()){        // If "NULL" value is present in the fired Query
				if(Expected_value.equalsIgnoreCase("NULL")){
					Report_Functions.ReportEventSuccess(doc, "1", "EshopSQLDBDateFormatCompare", "The actual value 'NULL' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' matches with the expected value :'"+Expected_value+"'", 2);
					EshopSQLDBDateFormatCompare=true;
				}else if(!(Expected_value.equalsIgnoreCase("NULL"))){
					Report_Functions.ReportEventFailure(doc,  "EshopSQLDBDateFormatCompare",  "The actual value 'NULL' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' does not match the expected value : '"+Expected_value+"'", true);  	 
					EshopSQLDBDateFormatCompare=false;
				}
			}

		}catch (Exception e){
			Report_Functions.ReportEventFailure(doc,  "EshopSQLDBDateFormatCompare",  "Error occured while comparing the dates in SQL query.Error description is : "+ e.getMessage() +".", true);
			log.info("EshopSQLDBDateFormatCompare Error : " + e);
			EshopSQLDBDateFormatCompare=false;
		}
		return EshopSQLDBDateFormatCompare;
	}






	/**
	 * 
	 * @Objective <b>Verifies to check date comparision in SQL DB</b>
	 * @param sqltablename
	 * @param strsqlcolumnname
	 * @param strsqlcondition
	 * @param Date_Format
	 * @param strExecEventFlag
	 * @author <b>Lakshman</b>
	 * @since <b>02-November-15</b>
	 */

	public static boolean SQLDBFutureDateCompare(String sqltablename, String strsqlcolumnname,String strsqlcondition,String Date_Format,String Days_to_add,int strExecEventFlag)throws Exception  {
		boolean SQLDBDateCompare= false;
		String query = null;  
		String Table_name = null;
		String Column_name = null;
		String SQL_condition = null;
		String Expected_value = null;
		String Actual_Value = null;
		String Current_Date=null;
		String expected_db_Date = null;
		String daystoadd = null;


		try {
			if(strExecEventFlag==1){
				Table_name=RetrieveTestDataValue("SQLDBDateCompare",sqltablename,strExecEventFlag);
				Column_name=RetrieveTestDataValue("SQLDBDateCompare",strsqlcolumnname,strExecEventFlag);
				SQL_condition=RetrieveTestDataValue("SQLDBDateCompare",strsqlcondition,strExecEventFlag);
				daystoadd = RetrieveTestDataValue("RRBSDBFutureDateCompare", Days_to_add, strExecEventFlag);
			}

			if(Table_name==null || Column_name==null || SQL_condition==null){
				Report_Functions.ReportEventFailure(doc,  "SQLDBDateCompare",  "Required details are not provided in test data sheet.", false);
				return false;
			}

			int Add_Days = Integer.parseInt(daystoadd);

			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date date = new Date();

			Calendar expdate = Calendar.getInstance();
			expdate.setTime(date);
			expdate.add(Calendar.DATE, Add_Days);
			Current_Date = dateFormat.format(expdate.getTime());

			Expected_value = Current_Date.trim();

			// Query to Execute      
			query = "select "+Column_name+" from "+Table_name+" where "+SQL_condition+"";
			rs_SQLServer= stmt.executeQuery(query);

		}catch (Exception e){
			Report_Functions.ReportEventFailure(doc,  "SQLDBDateCompare",  "Error occured while executing the SQL query.Error description is : "+ e.getMessage() +".", true);
			log.info("SQLDBDateCompare Error : " + e);
			SQLDBDateCompare=false;
		}

		try{
			rs_SQLServer.next();
			Actual_Value = rs_SQLServer.getString(1);
			String db_Date = Actual_Value.split(" ")[0];

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
			Date dateToChange = dateFormat.parse(db_Date);

			SimpleDateFormat finalDateFormat = new SimpleDateFormat(Date_Format);
			expected_db_Date = finalDateFormat.format(dateToChange);	

		} catch (Exception NullPointerException) {           // If no record is present in the fired Query
			Report_Functions.ReportEventFailure(doc,  "SQLDBDateCompare",  "No Record found for this query: "+query, true);
			log.info("SQLDBDateCompare Error : ");
			SQLDBDateCompare=false;
		}

		try{

			if(!rs_SQLServer.wasNull()){            // If some value is present in the fired Query
				if(expected_db_Date.equalsIgnoreCase(Expected_value)){
					Report_Functions.ReportEventSuccess(doc, "1", "SQLDBDateCompare", "The selected date : '"+expected_db_Date+"' in the column : '"+Column_name+"' of table : '"+Table_name+"' matches with the expected date : '"+Expected_value+"' ", 2);
					SQLDBDateCompare=true;
				}else if(!(expected_db_Date.equalsIgnoreCase(Expected_value))){
					Report_Functions.ReportEventFailure(doc,  "SQLDBDateCompare",  "The selected date : '"+expected_db_Date+"' in the column : '"+Column_name+"' of table : '"+Table_name+"' does not match with the expected date : '"+Expected_value+"' ", true);
					SQLDBDateCompare=false;
				}
			}else if(rs_SQLServer.wasNull()){        // If "NULL" value is present in the fired Query
				if(Expected_value.equalsIgnoreCase("NULL")){
					Report_Functions.ReportEventSuccess(doc, "1", "SQLDBDateCompare", "The actual value 'NULL' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' matches with the expected value :'"+Expected_value+"'", 2);
					SQLDBDateCompare=true;
				}else if(!(Expected_value.equalsIgnoreCase("NULL"))){
					Report_Functions.ReportEventFailure(doc,  "SQLDBDateCompare",  "The actual value 'NULL' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' does not match the expected value : '"+Expected_value+"'", true);  	 
					SQLDBDateCompare=false;
				}
			}

		}catch (Exception e){
			Report_Functions.ReportEventFailure(doc,  "SQLDBDateCompare",  "Error occured while comparing the dates in SQL query.Error description is : "+ e.getMessage() +".", true);
			log.info("SQLDBDateCompare Error : " + e);
			SQLDBDateCompare=false;
		}
		return SQLDBDateCompare;
	}

	/**
	 * 
	 * @Objective <b>Verifies to check date comparision in SQL DB</b>
	 * @param sqltablename
	 * @param strsqlcolumnname
	 * @param strsqlcondition
	 * @param Date_Format
	 * @param strExecEventFlag
	 * @author <b>Lakshman</b>
	 * @since <b>02-November-15</b>
	 */

	public static boolean EshopSQLDBFutureDateCompare(String sqltablename, String strsqlcolumnname,String strsqlcondition,String Date_Format,String Days_to_add,int strExecEventFlag)throws Exception  {
		boolean EshopSQLDBFutureDateCompare= false;
		String query = null;  
		String Table_name = null;
		String Column_name = null;
		String SQL_condition = null;
		String Expected_value = null;
		String Actual_Value = null;
		String Current_Date=null;
		String expected_db_Date = null;
		String daystoadd = null;


		try {
			if(strExecEventFlag==1){
				Table_name=RetrieveTestDataValue("EshopSQLDBFutureDateCompare",sqltablename,strExecEventFlag);
				Column_name=RetrieveTestDataValue("EshopSQLDBFutureDateCompare",strsqlcolumnname,strExecEventFlag);
				SQL_condition=RetrieveTestDataValue("EshopSQLDBFutureDateCompare",strsqlcondition,strExecEventFlag);
				daystoadd = RetrieveTestDataValue("EshopSQLDBFutureDateCompare", Days_to_add, strExecEventFlag);
			}

			if(Table_name==null || Column_name==null || SQL_condition==null){
				Report_Functions.ReportEventFailure(doc,  "EshopSQLDBFutureDateCompare",  "Required details are not provided in test data sheet.", false);
				return false;
			}

			int Add_Days = Integer.parseInt(daystoadd);

			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date date = new Date();

			Calendar expdate = Calendar.getInstance();
			expdate.setTime(date);
			expdate.add(Calendar.DATE, Add_Days);
			Current_Date = dateFormat.format(expdate.getTime());

			Expected_value = Current_Date.trim();

			// Query to Execute      
			query = "select "+Column_name+" from "+Table_name+" where "+SQL_condition+"";
			Eshop_SQLServer= EShopstmt.executeQuery(query);

		}catch (Exception e){
			Report_Functions.ReportEventFailure(doc,  "EshopSQLDBFutureDateCompare",  "Error occured while executing the SQL query.Error description is : "+ e.getMessage() +".", true);
			log.info("EshopSQLDBFutureDateCompare Error : " + e);
			EshopSQLDBFutureDateCompare=false;
		}

		try{
			Eshop_SQLServer.next();
			Actual_Value = Eshop_SQLServer.getString(1);
			String db_Date = Actual_Value.split(" ")[0];

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
			Date dateToChange = dateFormat.parse(db_Date);

			SimpleDateFormat finalDateFormat = new SimpleDateFormat(Date_Format);
			expected_db_Date = finalDateFormat.format(dateToChange);	

		} catch (Exception NullPointerException) {           // If no record is present in the fired Query
			Report_Functions.ReportEventFailure(doc,  "EshopSQLDBFutureDateCompare",  "No Record found for this query: "+query, true);
			log.info("EshopSQLDBFutureDateCompare Error : ");
			EshopSQLDBFutureDateCompare=false;
		}

		try{

			if(!Eshop_SQLServer.wasNull()){            // If some value is present in the fired Query
				if(expected_db_Date.equalsIgnoreCase(Expected_value)){
					Report_Functions.ReportEventSuccess(doc, "1", "EshopSQLDBFutureDateCompare", "The selected date : '"+expected_db_Date+"' in the column : '"+Column_name+"' of table : '"+Table_name+"' matches with the expected date : '"+Expected_value+"' ", 2);
					EshopSQLDBFutureDateCompare=true;
				}else if(!(expected_db_Date.equalsIgnoreCase(Expected_value))){
					Report_Functions.ReportEventFailure(doc,  "EshopSQLDBFutureDateCompare",  "The selected date : '"+expected_db_Date+"' in the column : '"+Column_name+"' of table : '"+Table_name+"' does not match with the expected date : '"+Expected_value+"' ", true);
					EshopSQLDBFutureDateCompare=false;
				}
			}else if(Eshop_SQLServer.wasNull()){        // If "NULL" value is present in the fired Query
				if(Expected_value.equalsIgnoreCase("NULL")){
					Report_Functions.ReportEventSuccess(doc, "1", "EshopSQLDBFutureDateCompare", "The actual value 'NULL' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' matches with the expected value :'"+Expected_value+"'", 2);
					EshopSQLDBFutureDateCompare=true;
				}else if(!(Expected_value.equalsIgnoreCase("NULL"))){
					Report_Functions.ReportEventFailure(doc,  "EshopSQLDBFutureDateCompare",  "The actual value 'NULL' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' does not match the expected value : '"+Expected_value+"'", true);  	 
					EshopSQLDBFutureDateCompare=false;
				}
			}

		}catch (Exception e){
			Report_Functions.ReportEventFailure(doc,  "EshopSQLDBFutureDateCompare",  "Error occured while comparing the dates in SQL query.Error description is : "+ e.getMessage() +".", true);
			log.info("SQLDBDateCompare Error : " + e);
			EshopSQLDBFutureDateCompare=false;
		}
		return EshopSQLDBFutureDateCompare;
	}

	/**
	 * @Objective <b>Verifies the SQL GBR DB select with Expected Value from ENV Variable</b>
	 * @param sqltablename
	 * @param strsqlcolumnname
	 * @param strsqlcondition
	 * @param strExpectedvalue
	 * @param strExecEventFlag
	 * @author <b>Lakshman</b>
	 * @since <b>10-AUG-16</b>
	 */

	public static boolean SQLDBSelectFromEnv(String sqltablename, String strsqlcolumnname,String strsqlcondition,String strEnvVariableColumn,int strExecEventFlag)throws Exception  {
		boolean SQLDBSelect= false;
		String query = null;
		String Table_name = null;
		String Column_name = null;
		String SQL_condition = null;
		//String Expected_value = null;		
		String Expected_value = null;
		String envVariable=null;
		String Actual_Value = null;
		try {
			if(strExecEventFlag==1){
				Table_name=RetrieveTestDataValue("SQLDBSelect",sqltablename,strExecEventFlag);
				Column_name=RetrieveTestDataValue("SQLDBSelect",strsqlcolumnname,strExecEventFlag);
				SQL_condition=RetrieveTestDataValue("SQLDBSelect",strsqlcondition,strExecEventFlag);
				envVariable=RetrieveTestDataValue("SQLDBSelect",strEnvVariableColumn,strExecEventFlag);
			}

			if(Table_name==null || Column_name==null || SQL_condition==null){
				Report_Functions.ReportEventFailure(doc,  "SQLDBSelect",  "Required details are not provided in test data sheet.", false);
				return false;
			}

			Expected_value=EnvironmentValue.getProperty(envVariable);


			if(Expected_value==null){
				Report_Functions.ReportEventFailure(doc,  "SQLDBSelect",  "Dynamic Variable '"+envVariable+"' has no value.", false);
				return false;
			}

			//Query to Execute      
			query = "select "+Column_name+" from "+Table_name+" where "+SQL_condition+" order by 1 desc";

			rs_SQLServer = stmt.executeQuery(query);


		} catch (Exception e) {
			Report_Functions.ReportEventFailure(doc,  "SQLDBSelect",  "Error occured while executing the SQL query.Error description is : "+ e.getMessage() +".", true);
			log.info("SQLDBSelect Error : " + e);
			SQLDBSelect=false;
		}

		try{
			rs_SQLServer.next();
			Actual_Value = rs_SQLServer.getString(1).trim();

		} catch(NullPointerException e){

			log.info("Null pointer occurred :"+e.getMessage());

		} catch (Exception e) {           // If no record is present in the fired Query
			Report_Functions.ReportEventFailure(doc,  "SQLDBSelect",  "No Record found for this query: "+query, true);
			log.info("SQLDBSelect Error : ");
			SQLDBSelect=false;
		}

		try{
			if(!rs_SQLServer.wasNull()){            // If some value is present in the fired Query
				if(Actual_Value.equalsIgnoreCase(Expected_value)){
					Report_Functions.ReportEventSuccess(doc, "1","SQLDBSelect", "The actual value : '"+Actual_Value+"' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' matches the expected Dynamic value : '"+Expected_value+"'", 2);
					SQLDBSelect=true;
				}else if(!(Actual_Value.equalsIgnoreCase(Expected_value))){
					Report_Functions.ReportEventFailure(doc,  "SQLDBSelect",  "The actual value : '"+Actual_Value+"' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' does not match the expected Dynamic value : '"+Expected_value+"'", true);
					SQLDBSelect=false;
				}
			}

			else if(rs_SQLServer.wasNull()){        // If "NULL" value is present in the fired Query
				if(Expected_value.equalsIgnoreCase("NULL")){
					Report_Functions.ReportEventSuccess(doc, "1", "SQLDBSelect", "The actual value 'NULL' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' matches with the expected Dynamic value :'"+Expected_value+"'", 2);
					SQLDBSelect=true;
				}else if(!(Expected_value.equalsIgnoreCase("NULL"))){
					Report_Functions.ReportEventFailure(doc,  "SQLDBSelect",  "The actual value 'NULL' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' does not match the expected Dynamic value : '"+Expected_value+"'", true);  	 
					SQLDBSelect=false;
				}
			}
		}catch(NullPointerException e){

			log.info("Null pointer occurred :"+e.getMessage());

		}catch (Exception e){
			Report_Functions.ReportEventFailure(doc,  "SQLDBSelect",  "Error occured while comparing the values in SQL query.Error description is : "+ e.getMessage() +".", true);
			log.info("SQLDBSelect Error : " + e);
			SQLDBSelect=false;
		}
		return SQLDBSelect;
	}


	/**
	 * @Objective <b>Verifies the SQL GBR DB select with Expected Value from ENV Variable</b>
	 * @param sqltablename
	 * @param strsqlcolumnname
	 * @param strsqlcondition
	 * @param strExpectedvalue
	 * @param strExecEventFlag
	 * @author <b>Lakshman</b>
	 * @since <b>10-AUG-16</b>
	 */

	public static boolean EshopSQLDBSelectFromEnv(String sqltablename, String strsqlcolumnname,String strsqlcondition,String strEnvVariableColumn,int strExecEventFlag)throws Exception  {
		boolean EshopSQLDBSelectFromEnv= false;
		String query = null;
		String Table_name = null;
		String Column_name = null;
		String SQL_condition = null;
		//String Expected_value = null;		
		String Expected_value = null;
		String envVariable=null;
		String Actual_Value = null;
		try {
			if(strExecEventFlag==1){
				Table_name=RetrieveTestDataValue("EshopSQLDBSelectFromEnv",sqltablename,strExecEventFlag);
				Column_name=RetrieveTestDataValue("EshopSQLDBSelectFromEnv",strsqlcolumnname,strExecEventFlag);
				SQL_condition=RetrieveTestDataValue("EshopSQLDBSelectFromEnv",strsqlcondition,strExecEventFlag);
				envVariable=RetrieveTestDataValue("EshopSQLDBSelectFromEnv",strEnvVariableColumn,strExecEventFlag);
			}

			if(Table_name==null || Column_name==null || SQL_condition==null){
				Report_Functions.ReportEventFailure(doc,  "EshopSQLDBSelectFromEnv",  "Required details are not provided in test data sheet.", false);
				return false;
			}

			Expected_value=EnvironmentValue.getProperty(envVariable);

			if(Expected_value==null){
				Report_Functions.ReportEventFailure(doc,  "EshopSQLDBSelectFromEnv",  "Dynamic Variable '"+envVariable+"' has no value.", false);
				return false;
			}

			//Query to Execute      
			query = "select "+Column_name+" from "+Table_name+" where "+SQL_condition+" order by 1 desc";

			Eshop_SQLServer = EShopstmt.executeQuery(query);


		} catch (Exception e) {
			Report_Functions.ReportEventFailure(doc,  "EshopSQLDBSelectFromEnv",  "Error occured while executing the SQL query.Error description is : "+ e.getMessage() +".", true);
			log.info("SQLDBSelect Error : " + e);
			EshopSQLDBSelectFromEnv=false;
			return EshopSQLDBSelectFromEnv;
		}

		try{
			Eshop_SQLServer.next();
			Actual_Value = Eshop_SQLServer.getString(1).trim();

		} catch(NullPointerException e){

			log.info("Null pointer occurred :"+e.getMessage());
			return EshopSQLDBSelectFromEnv;


		} catch (Exception e) {           // If no record is present in the fired Query
			Report_Functions.ReportEventFailure(doc,  "EshopSQLDBSelectFromEnv",  "No Record found for this query: "+query, true);
			log.info("SQLDBSelect Error : ");
			EshopSQLDBSelectFromEnv=false;
			return EshopSQLDBSelectFromEnv;

		}

		try{
			if(!Eshop_SQLServer.wasNull()){            // If some value is present in the fired Query
				if(Actual_Value.equalsIgnoreCase(Expected_value)){
					Report_Functions.ReportEventSuccess(doc, "1","EshopSQLDBSelectFromEnv", "The actual value : '"+Actual_Value+"' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' matches the expected Dynamic value : '"+Expected_value+"'", 2);
					EshopSQLDBSelectFromEnv=true;
				}else if(!(Actual_Value.equalsIgnoreCase(Expected_value))){
					Report_Functions.ReportEventFailure(doc,  "EshopSQLDBSelectFromEnv",  "The actual value : '"+Actual_Value+"' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' does not match the expected Dynamic value : '"+Expected_value+"'", true);
					EshopSQLDBSelectFromEnv=false;
				}
			}

			else if(Eshop_SQLServer.wasNull()){        // If "NULL" value is present in the fired Query
				if(Expected_value.equalsIgnoreCase("NULL")){
					Report_Functions.ReportEventSuccess(doc, "1", "EshopSQLDBSelectFromEnv", "The actual value 'NULL' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' matches with the expected Dynamic value :'"+Expected_value+"'", 2);
					EshopSQLDBSelectFromEnv=true;
				}else if(!(Expected_value.equalsIgnoreCase("NULL"))){
					Report_Functions.ReportEventFailure(doc,  "EshopSQLDBSelectFromEnv",  "The actual value 'NULL' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' does not match the expected Dynamic value : '"+Expected_value+"'", true);  	 
					EshopSQLDBSelectFromEnv=false;
				}
			}
		}catch(NullPointerException e){

			log.info("Null pointer occurred :"+e.getMessage());

		}catch (Exception e){
			Report_Functions.ReportEventFailure(doc,  "EshopSQLDBSelectFromEnv",  "Error occured while comparing the values in SQL query.Error description is : "+ e.getMessage() +".", true);
			log.info("SQLDBSelect Error : " + e);
			EshopSQLDBSelectFromEnv=false;
			return EshopSQLDBSelectFromEnv;

		}
		return EshopSQLDBSelectFromEnv;
	}


	/**
	 * 
	 * @Objective <b>Verifies to update the data for RRBS in SQL DB</b>
	 * @param sqltablename
	 * @param strsqlcolumnname
	 * @param strsqlcolumnvalue
	 * @param strsqlcondition
	 * @param strExecEventFlag
	 * @author <b>Lakshman</b>
	 * @since <b>1-Sep-16</b>
	 */

	public static boolean DBCommonPreCondition(String dbType, String actionName,String sqltablename, String strsqlcolumnname,String strsqlcolumnvalue,String strsqlcondition,int strExecEventFlag)throws Exception  {

		boolean status= false;
		String action=null;
		String Database=null;
		try {

			if (strExecEventFlag==1){
				Database=RetrieveTestDataValue("",dbType,strExecEventFlag);
				action=RetrieveTestDataValue("",actionName,strExecEventFlag);
			}
			if(action==null){
				Report_Functions.ReportEventFailure(doc,  "DBCommonPreCondition",  "Required details are not provided in the data sheet.", false);
				return false;
			}

			switch(Database.toUpperCase()){
			case "SQL" : switch(action.toUpperCase()){
			case "UPDATE":status=SQLDBUpdate(sqltablename, strsqlcolumnname, strsqlcolumnvalue, strsqlcondition, strExecEventFlag);break;
			case "DELETE":status=SQLDBDelete(sqltablename, strsqlcondition, strExecEventFlag);break;
			default:Report_Functions.ReportEventFailure(doc,  "",  "Selected action is in appropriate - "+action, false);break;
			}
			break;
			case "RRBS" : switch(action){
			case "UPDATE":status=RRBSDBUpdate(sqltablename, strsqlcolumnname, strsqlcolumnvalue, strsqlcondition, strExecEventFlag);break;
			case "DELETE":status=RRBSDBDelete(sqltablename, strsqlcondition, strExecEventFlag);break;
			default:Report_Functions.ReportEventFailure(doc,  "",  "Selected action is in appropriate - "+action, false);break;
			}
			break;
			case "ESHOP" : switch(action){
			case "UPDATE":status=EshopSQLDBUpdate(sqltablename, strsqlcolumnname, strsqlcolumnvalue, strsqlcondition, strExecEventFlag);break;
			case "DELETE":status=EshopSQLDBDelete(sqltablename, strsqlcondition, strExecEventFlag);break;
			default:Report_Functions.ReportEventFailure(doc,  "",  "Selected action is in appropriate - "+action, false);
			break;
			}
			break;

			default:Report_Functions.ReportEventFailure(doc,  "",  "In Appropriate DB is selected - "+action, false);break;

			}
		} catch (Exception e) { 
			status=false;
			Report_Functions.ReportEventFailure(doc,  "DBCommonPreCondition",  "Error occured while executing the SQL query.Error description is : "+ e.getMessage() +".", true);
			log.info("DBCommonPreCondition Error : " + e);
		}
		return status;
	}


	/**
	 * 
	 * @Objective <b>This method is to Sending XML Request and validating the response from server<b>
	 * @return true/false
	 * @author <b>Muralimohan M</b>
	 * @since <b>07-Jun-16</b>
	 */
	public static boolean Verify_XMLDynamicRequest_Response_Data(String RequestName,String Input_DATAXML,String Output_DATAXML,String XLDB_Input_Query,String stringRunTimeEnvironmentValue,String Entity_Name,int strExecEventFlag)throws Exception  {
		String XML_Request=null;
		int result=0;
		String Request_Name=null;
		String RunTimeEnvironmentValue=null;

		if(strExecEventFlag==1){
			Request_Name=RetrieveTestDataValue("Verify_XML_Response_Data",RequestName,strExecEventFlag);
			Input_DATAXML=RetrieveTestDataValue("Verify_XML_Response_Data",Input_DATAXML,strExecEventFlag);
			XLDB_Input_Query=RetrieveTestDataValue("Verify_XML_Response_Data", XLDB_Input_Query, strExecEventFlag);
			Output_DATAXML=RetrieveTestDataValue("Verify_XML_Response_Data", Output_DATAXML, strExecEventFlag);
			RunTimeEnvironmentValue=RetrieveTestDataValue("Verify_XML_Response_Data", stringRunTimeEnvironmentValue, strExecEventFlag);
			EntityName=RetrieveTestDataValue("Verify_XML_Response_Data", Entity_Name, strExecEventFlag);
		}
		NSS_URL_PATH="Request_URL_"+EntityName;
		AppURL=Param.getProperty(NSS_URL_PATH);
		try{
			//Reading testdata from XMLTemplate
			XML_Request=ReadExcel.RetrieveValueFromTestDataBasedOnQuery(Param.getProperty("SupportFiles_Location"),Param.getProperty("XML_Template_File_Name"),"Input_XML",XLDB_Input_Query);

			factory=DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder=factory.newDocumentBuilder();
			StringBuilder xmlBuilder=new StringBuilder();
			xmlBuilder.append(XML_Request);
			ByteArrayInputStream  is;
			try{
				is=new ByteArrayInputStream(xmlBuilder.toString().getBytes("UTF-8"));
				document=builder.parse(is);
			}catch(Exception e){
				e.printStackTrace();
			}
			//updating node values from XMLtemplate based on input data from testdata sheet

			updateDynamicNodeValue(document,Input_DATAXML,RunTimeEnvironmentValue,EntityName);
			String Updated_XML_Request = DocumentToString(document);
			System.out.println("Updated_XML_Request:"+Updated_XML_Request);

			try{
				PostMethod post = new PostMethod(AppURL+Request_Name+"/");
				post.setRequestBody(Updated_XML_Request);
				HttpClient httpclient = new HttpClient();
				try{
					result = httpclient.executeMethod(post);
					Report_Functions.ReportEventSuccess(doc, "1", "Verify_XML_Response_Data", "The xml request : '"+ Updated_XML_Request + "' has been sent successfully.", 2);
				}catch(Exception httpException){
					httpException.printStackTrace();
					Report_Functions.ReportEventFailure(doc,  "Verify_XML_Response_Data",  "httpException:Error occured while sending the request to the server."+httpException.getMessage(), false);	
				}
				gbstrXMLResponseData= post.getResponseBodyAsString();

				//To Enable SOAP XML Header Validations

				if(result==200){
					Executionstatus=true;
					Report_Functions.ReportEventSuccess(doc, "1", "Verify_XML_Response_Data", "The xml response : '"+ gbstrXMLResponseData.toString() + "' received successfully.", 2);
					//validating XML headers and footers
					if(Param.getProperty("SOAP_Validation_Enable").equalsIgnoreCase("true")){

						Executionstatus=Verify_XML_Header_Footer_Validation(Param.getProperty("XML_Envelope"), gbstrXMLResponseData);
						Executionstatus=Verify_XML_Header_Footer_Validation(Param.getProperty("XML_Header"), gbstrXMLResponseData);
						Executionstatus=Verify_XML_Header_Footer_Validation(Param.getProperty("XML_LastLine"), gbstrXMLResponseData);
						Executionstatus=Verify_XML_Header_Footer_Validation(Param.getProperty("XML_LastsecondLine"), gbstrXMLResponseData);
						Executionstatus=Verify_XML_Header_Footer_Validation(Param.getProperty("XML_ClosingHeader"), gbstrXMLResponseData);
						Executionstatus=Verify_XML_Header_Footer_Validation(Param.getProperty("XML_OpeningBody"), gbstrXMLResponseData);
					}
					try{
						xpathfactory = XPathFactory.newInstance();
						xpath = XPathFactory.newInstance().newXPath();
						factory=DocumentBuilderFactory.newInstance();
						builder=factory.newDocumentBuilder();
						document=builder.parse(new ByteArrayInputStream(gbstrXMLResponseData.getBytes("UTF-8")));

						String[] Output_data = Output_DATAXML.split(";");

						for (int i = 0; i < Output_data.length; i++) {
							String[] Output_XML_Info = Output_data[i].split("=",2);
							String node_value=(String)xpath.compile("*//"+Output_XML_Info[0]).evaluate(document,XPathConstants.STRING);
							String[] ActualValue=Output_XML_Info[1].split(":");
							int arrLength=ActualValue.length;
							//TO validate ERROR_CODE tag- String to String match

							if( Output_XML_Info[0].contains("ERROR_CODE") ||  Output_XML_Info[0].contains("EXPIRY_DATE_TIME") ||Output_XML_Info[0].contains("ACTIVATIONDATE")){
								Executionstatus=StringValidator(node_value,Output_XML_Info[1]);	
							}else{

								if(arrLength>1 ){
									ProcessedXML=ErrorDescriptionRegExpProcessor(Output_XML_Info[1]);
									Executionstatus=RegExpValidator(node_value,ProcessedXML);
								}else
									if(Output_XML_Info[0].equalsIgnoreCase("TRANSACTION_NUMBER")){
										Executionstatus=RegExpValidator(node_value,Output_XML_Info[1]);
									}
									else{
										Executionstatus=StringValidator(node_value,Output_XML_Info[1]);
									}
							}



							if(Executionstatus==true){
								Report_Functions.ReportEventSuccess(doc, "1", "Verify_XML_Response_Data_NodeValtoEnv", "The actual node value of  : '//"+ Output_XML_Info[0] + "' is '"+ node_value +"' and matched with the expected value '"+ Output_XML_Info[1] +"'.", 2);
								Executionstatus=true;;
							} else{
								Report_Functions.ReportEventFailure(doc,  "Verify_XML_Response_Data_NodeValtoEnv",  "The actual node value of  : '//"+ Output_XML_Info[0] + "' is '"+ node_value +"' and no matching with the expected value '"+ Output_XML_Info[1] +"'.", false);
								Executionstatus=false;
							}
						}

					}catch(Exception responseXMLException){
						responseXMLException.printStackTrace();
						Report_Functions.ReportEventFailure(doc,  "Verify_XML_Response_Data",  "responseXMLException:Error in Parsing response XML.:"+responseXMLException.getMessage(), false);	
						return Executionstatus;
					}
				}


			}catch(Exception xmlFormException){
				System.out.println(xmlFormException.getMessage());
			}


		}catch(Exception e){
			return Executionstatus;
		}finally{
			xpathfactory=null;
			document=null;
			factory=null;
			ProcessedXML=null;
		}

		return Executionstatus;

	}

	/**
	 * 
	 * @Objective <b>This method is to Update the dynamic node value based on the previous request and supports static nodes as well<b>
	 * @return true/false
	 * @author <b>Muralimohan M</b>
	 * @since <b>07-Jun-16</b>
	 */


	public static void updateDynamicNodeValue(Document document, String Inputdata,String RunTimeEnvironmentValue,String Entity_Name) throws Exception {
		XPath xpath=null;
		String CHANNEL_REFERENCETag=NULL;
		NodeList nodes =null;
		
		xpath = XPathFactory.newInstance().newXPath();
		EntityName="ENTITY_"+Entity_Name;
		EntityName=Param.getProperty(EntityName);
		TransactionIDTag="TRANSACTIONTAG_"+Entity_Name;
		TransactionIDTag=Param.getProperty(TransactionIDTag);
		CHANNEL_REFERENCETag="CHANNEL_TAG_"+Entity_Name;
		CHANNEL_REFERENCETag=Param.getProperty(CHANNEL_REFERENCETag);
		EntityNameTag="ENTITYTAG_"+Entity_Name;
		EntityNameTag=Param.getProperty(EntityNameTag);

		//Splitting and framing the XML nodes for static values

		String[] Input_data = Inputdata.split(";");

		for (int i = 0; i < Input_data.length; i++) {
			String[] XML_Info = Input_data[i].split("=");
			if (!XML_Info[0].contains("CHANNEL_REFERENCE")){
				try{
					nodes=(NodeList)xpath.compile("*//"+XML_Info[0]).evaluate(document,XPathConstants.NODESET);
					nodes.item(0).setTextContent(XML_Info[1]);
					Report_Functions.ReportEventSuccess(doc, "1", "updateDynamicNodeValue", "Node Tag: '"+ XML_Info[0] + "' is set with value '"+XML_Info[1]+"' for sending request.", 2);
				}catch (Exception e){
					Report_Functions.ReportEventFailure(doc,  "updateDynamicNodeValue",  "Verify whether node '"+XML_Info[0]+"' you are trying to update is exists in the Template XML."+e.getMessage(), false);
				}
			}
		}

		//Splitting and framing the XML nodes for dynamic nodes which requires the node value from environment variable
		//if(RunTimeEnvironmentValue!=null){
		String[] Dynamic_Data = RunTimeEnvironmentValue.split(";");

		for (int i = 0; i < Dynamic_Data.length; i++) {

			String []DynamicNodes=Dynamic_Data[i].split("=");

			try{
				nodes=(NodeList)xpath.compile("*//"+DynamicNodes[0]).evaluate(document,XPathConstants.NODESET);
				nodes.item(0).setTextContent(EnvironmentValue.getProperty(DynamicNodes[1]));

			}catch(Exception e){
				Report_Functions.ReportEventFailure(doc,  "updateDynamicNodeValue",  "Verify whether node '"+Dynamic_Data[0]+"' you are trying to update is exists in the Template XML."+e.getMessage(), false);
			}
			//	}
			//Sending the transaction ID in the request XML
		}

		try{
			NodeList root=document.getDocumentElement().getElementsByTagName(TransactionIDTag);
			root.item(0).setTextContent(Param.getProperty("CBOS_ITG_TRANSACTION_ID"));

			NodeList root1 = document.getDocumentElement().getElementsByTagName(EntityNameTag);
			root1.item(0).setTextContent(EntityName);

			NodeList root2=document.getDocumentElement().getElementsByTagName(CHANNEL_REFERENCETag);
			root2.item(0).setTextContent("CCARE");


		}catch(Exception e){
			Report_Functions.ReportEventFailure(doc,  "updateDynamicNodeValue",  "Verify whether node TRANSACTION_ID OR ENTITY TAG or CHANNEL_REFERENCE you are trying to update is CORRECT."+e.getMessage(), false);
		}

	}


	public static boolean RRBSSQLDBSelectFromEnv(String sqltablename, String strsqlcolumnname,String strsqlcondition,String strExpectedvalue,int strExecEventFlag)throws Exception  {

		String query = null;
		String Table_name = null;
		String Column_name = null;
		String SQL_condition = null;
		String Expected_value = "";
		String Actual_Value = null;
		try {
			if(strExecEventFlag==1){
				Table_name=RetrieveTestDataValue("RRBSSQLDBSelectFromEnv",sqltablename,strExecEventFlag);
				Column_name=RetrieveTestDataValue("RRBSSQLDBSelectFromEnv",strsqlcolumnname,strExecEventFlag);
				SQL_condition=RetrieveTestDataValue("RRBSSQLDBSelectFromEnv",strsqlcondition,strExecEventFlag);
				Expected_value=EnvironmentValue.getProperty(RetrieveTestDataValue("RRBSSQLDBSelectFromEnv",strExpectedvalue,strExecEventFlag));

			}

			if(Table_name==null){
				Report_Functions.ReportEventFailure(doc,  "RRBSSQLDBSelectFromEnv",  "SQL table name is not provided in the Data Sheet.", false);
				Executionstatus= false;
				throw new RuntimeException ("Table_name is null");
			}

			if(Column_name==null){
				Report_Functions.ReportEventFailure(doc,  "RRBSSQLDBSelectFromEnv",  "SQL Column name is not provided in the Data Sheet.", false);
				Executionstatus= false;
				throw new RuntimeException ("Column_name is null");
			}

			if(SQL_condition==null){
				Report_Functions.ReportEventFailure(doc,  "RRBSSQLDBSelectFromEnv",  "SQL condition is not provided in the Data Sheet.", false);
				Executionstatus= false;
				throw new RuntimeException ("SQL_condition is null");
			}

			//Query to Execute      
			query = "select "+Column_name+" from "+Table_name+" where "+SQL_condition;
			System.out.println("query:"+query);

			rrbsresultset = rrbsstatement.executeQuery(query);


		} catch (Exception e) {
			Report_Functions.ReportEventFailure(doc,  "RRBSSQLDBSelectFromEnv",  "Error occured while executing the SQL query.Error description is : "+ e.getMessage() +".", true);
			Log.info("EShopSQLDBSelectFromEnv Error : " + e);
			e.printStackTrace();
			Executionstatus=false;
		}

		try{

			rrbsresultset.next();
			Actual_Value = rrbsresultset.getString(1).trim();

		} catch (Exception e) {           // If no record is present in the fired Query
			Report_Functions.ReportEventFailure(doc,  "RRBSSQLDBSelectFromEnv",  "No Record found for this query: "+query, true);
			Log.info("RRBSSQLDBSelectFromEnv Error : ");
			Executionstatus=false;
		}

		try{
			if(!rrbsresultset.wasNull()){            // If some value is present in the fired Query
				if(Actual_Value.equalsIgnoreCase(Expected_value.toString())){
					Report_Functions.ReportEventSuccess(doc, "1","RRBSSQLDBSelectFromEnv", "The actual value : '"+Actual_Value+"' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' matches the expected value : '"+Expected_value+"'", 2);
					Executionstatus=true;
				}else if(!(Actual_Value.equals(Expected_value))){
					Report_Functions.ReportEventFailure(doc,  "RRBSSQLDBSelectFromEnv",  "The actual value : '"+Actual_Value+"' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' does not match the expected value : '"+Expected_value+"'", true);
					Executionstatus=false;
				}
			}else if(rrbsresultset.wasNull()){        // If "NULL" value is present in the fired Query
				if(Expected_value.equals("NULL")){
					Report_Functions.ReportEventSuccess(doc, "1", "RRBSSQLDBSelectFromEnv", "The actual value 'NULL' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' matches with the expected value :'"+Expected_value+"'", 2);
					Executionstatus=true;
				}else if(!(Expected_value.equals("NULL"))){
					Report_Functions.ReportEventFailure(doc,  "RRBSSQLDBSelectFromEnv",  "The actual value 'NULL' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' does not match the expected value : '"+Expected_value+"'", true);  	 
					Executionstatus=false;
				}
			}
		} catch (Exception e){
			Report_Functions.ReportEventFailure(doc,  "RRBSSQLDBSelectFromEnv",  "Error occured while comparing the values in SQL query.Error description is : "+ e.getMessage() +".", true);
			Log.info("RRBSSQLDBSelectFromEnv Error : " + e);
			Executionstatus=false;
		}
		return Executionstatus;
	}




	/**
	 * @Objective <b>Verifies the SQL GBR DB select with Expected Value from ENV Variable</b>
	 * @param sqltablename
	 * @param strsqlcolumnname
	 * @param strsqlcondition
	 * @param strExpectedvalue
	 * @param strExecEventFlag
	 * @author <b>Muralimohan M</b>
	 * @since <b>10-AUG-16</b>
	 */

	public static boolean EShopSQLDBSelectAppendConditionEnvVar(String sqltablename, String strsqlcolumnname,String strsqlcondition,String strExpectedValue,String environmentVariableString,String StrAppend,int strExecEventFlag)throws Exception  {
		boolean EShopSQLDBSelectAppendConditionEnvVar= false;
		String query = null;
		String Table_name = null;
		String Column_name = null;
		String SQL_condition = null;
		//String Expected_value = null;		
		String Expected_value = null;
		String envVariable=null;
		String Actual_Value = null;
		String AppendValue=null;
		String SQLConditionAppend=null;
		System.out.println("valuuuue:::"+EnvironmentValue.getProperty("FreeSIM_TransactionID"));
		try {
			if(strExecEventFlag==1){
				Table_name=RetrieveTestDataValue("EShopSQLDBSelectAppendConditionEnvVar",sqltablename,strExecEventFlag);
				Column_name=RetrieveTestDataValue("EShopSQLDBSelectAppendConditionEnvVar",strsqlcolumnname,strExecEventFlag);
				SQL_condition=RetrieveTestDataValue("EShopSQLDBSelectAppendConditionEnvVar",strsqlcondition,strExecEventFlag);
				envVariable=RetrieveTestDataValue("EShopSQLDBSelectAppendConditionEnvVar",environmentVariableString,strExecEventFlag);
				System.out.println("envVariable value from excel::::"+envVariable);
				envVariable=EnvironmentValue.getProperty(envVariable);
				System.out.println("EnvValue for that condition:"+envVariable);
				AppendValue=RetrieveTestDataValue("EShopSQLDBSelectAppendConditionEnvVar",StrAppend,strExecEventFlag);
				Expected_value=RetrieveTestDataValue("EShopSQLDBSelectAppendConditionEnvVar",strExpectedValue,strExecEventFlag);
			}

			if(Table_name==null || Column_name==null || SQL_condition==null){
				Report_Functions.ReportEventFailure(doc,  "EShopSQLDBSelectAppendConditionEnvVar",  "Required details are not provided in test data sheet.", false);
				return false;
			}

			SQLConditionAppend=envVariable+AppendValue;
			System.out.println("SQLConditionAppend:"+SQLConditionAppend);
			if(Expected_value==null){
				Report_Functions.ReportEventFailure(doc,  "EShopSQLDBSelectAppendConditionEnvVar",  "Dynamic Variable '"+envVariable+"' has no value.", false);
				return false;
			}
			//envVariable=EnvironmentValue.getProperty(envVariable);
			//Query to Execute      
			query = "select "+Column_name+" from "+Table_name+" where "+SQL_condition+"='"+SQLConditionAppend+"' order by 1 desc";
			Log.info("Query framed in EShopSQLDBSelectAppendConditionEnvVar::\n"+query);
			Eshop_SQLServer = EShopstmt.executeQuery(query);


		} catch (Exception e) {
			Report_Functions.ReportEventFailure(doc,  "EShopSQLDBSelectAppendConditionEnvVar",  "Error occured while executing the SQL query.Error description is : "+ e.getMessage() +".", true);
			log.info("SQLDBSelect Error : " + e);
			EShopSQLDBSelectAppendConditionEnvVar=false;
		}

		try{
			Eshop_SQLServer.next();
			Actual_Value = Eshop_SQLServer.getString(1).trim();

		} catch(NullPointerException e){

			log.info("Null pointer occurred :"+e.getMessage());

		} catch (Exception e) {           // If no record is present in the fired Query
			Report_Functions.ReportEventFailure(doc,  "EShopSQLDBSelectAppendConditionEnvVar",  "No Record found for this query: "+query, true);
			log.info("SQLDBSelect Error : ");
			EShopSQLDBSelectAppendConditionEnvVar=false;
		}

		try{
			if(!Eshop_SQLServer.wasNull()){            // If some value is present in the fired Query
				if(Actual_Value.equalsIgnoreCase(Expected_value)){
					Report_Functions.ReportEventSuccess(doc, "1","EShopSQLDBSelectAppendConditionEnvVar", "The actual value : '"+Actual_Value+"' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"='"+SQLConditionAppend+"' matches the expected Dynamic value : '"+Expected_value+"'", 2);
					EShopSQLDBSelectAppendConditionEnvVar=true;
				}else if(!(Actual_Value.equalsIgnoreCase(Expected_value))){
					Report_Functions.ReportEventFailure(doc,  "EShopSQLDBSelectAppendConditionEnvVar",  "The actual value : '"+Actual_Value+"' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"='"+SQLConditionAppend+"' does not match the expected Dynamic value : '"+Expected_value+"'", true);
					EShopSQLDBSelectAppendConditionEnvVar=false;
				}
			}

			else if(Eshop_SQLServer.wasNull()){        // If "NULL" value is present in the fired Query
				if(Expected_value.equalsIgnoreCase("NULL")){
					Report_Functions.ReportEventSuccess(doc, "1", "EShopSQLDBSelectAppendConditionEnvVar", "The actual value 'NULL' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' matches with the expected Dynamic value :'"+Expected_value+"'", 2);
					EShopSQLDBSelectAppendConditionEnvVar=true;
				}else if(!(Expected_value.equalsIgnoreCase("NULL"))){
					Report_Functions.ReportEventFailure(doc,  "EShopSQLDBSelectAppendConditionEnvVar",  "The actual value 'NULL' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' does not match the expected Dynamic value : '"+Expected_value+"'", true);  	 
					EShopSQLDBSelectAppendConditionEnvVar=false;
				}
			}
		}catch(NullPointerException e){

			log.info("Null pointer occurred :"+e.getMessage());

		}catch (Exception e){
			Report_Functions.ReportEventFailure(doc,  "EShopSQLDBSelectAppendConditionEnvVar",  "Error occured while comparing the values in SQL query.Error description is : "+ e.getMessage() +".", true);
			log.info("EShopSQLDBSelectAppendConditionEnvVar Error : " + e);
			EShopSQLDBSelectAppendConditionEnvVar=false;
		}
		return EShopSQLDBSelectAppendConditionEnvVar;
	}
	
	



	/**
	 * @Objective <b>Verifies the SQL GBR DB select with Expected Value from ENV Variable</b>
	 * @param sqltablename
	 * @param strsqlcolumnname
	 * @param strsqlcondition
	 * @param strExpectedvalue
	 * @param strExecEventFlag
	 * @author <b>Muralimohan M</b>
	 * @since <b>10-AUG-16</b>
	 */

	public static boolean EshopSQLDBSelect_AppendAndCompare(String sqltablename, String strsqlcolumnname,String strsqlcondition,String strExpectedValue,String StrAppend,int strExecEventFlag)throws Exception  {
		String query = null;
		String Table_name = null;
		String Column_name = null;
		String SQL_condition = null;
		String Expected_value = "";
		String Actual_Value = null;
		String appendValue=null;
		try {
			if(strExecEventFlag==1){
				Table_name=RetrieveTestDataValue("EShopSQLDBSelectFromEnv",sqltablename,strExecEventFlag);
				Column_name=RetrieveTestDataValue("EShopSQLDBSelectFromEnv",strsqlcolumnname,strExecEventFlag);
				SQL_condition=RetrieveTestDataValue("EShopSQLDBSelectFromEnv",strsqlcondition,strExecEventFlag);
				Expected_value=RetrieveTestDataValue("EShopSQLDBSelectFromEnv",strExpectedValue,strExecEventFlag);
				appendValue=RetrieveTestDataValue("EShopSQLDBSelectFromEnv",StrAppend,strExecEventFlag);
			}

			if(Table_name==null){
				Report_Functions.ReportEventFailure(doc,  "EShopSQLDBSelectFromEnv",  "SQL table name is not provided in the Data Sheet.", false);
				Executionstatus= false;
				throw new RuntimeException ("Table_name is null");
			}

			if(Column_name==null){
				Report_Functions.ReportEventFailure(doc,  "EShopSQLDBSelectFromEnv",  "SQL Column name is not provided in the Data Sheet.", false);
				Executionstatus= false;
				throw new RuntimeException ("Column_name is null");
			}

			if(SQL_condition==null){
				Report_Functions.ReportEventFailure(doc,  "EShopSQLDBSelectFromEnv",  "SQL condition is not provided in the Data Sheet.", false);
				Executionstatus= false;
				throw new RuntimeException ("SQL_condition is null");
			}

			//Query to Execute      
			query = "select "+Column_name+" from "+Table_name+" where "+SQL_condition;
			System.out.println("query:"+query);

			Eshop_SQLServer = EShopstmt.executeQuery(query);


		} catch (Exception e) {
			Report_Functions.ReportEventFailure(doc,  "EShopSQLDBSelectFromEnv",  "Error occured while executing the SQL query.Error description is : "+ e.getMessage() +".", true);
			Log.info("EShopSQLDBSelectFromEnv Error : " + e);
			e.printStackTrace();
			Executionstatus=false;
		}

		try{
			Eshop_SQLServer.next();
			Actual_Value = Eshop_SQLServer.getString(1).trim();

		} catch (Exception e) {           // If no record is present in the fired Query
			Report_Functions.ReportEventFailure(doc,  "EShopSQLDBSelectFromEnv",  "No Record found for this query: "+query, true);
			Log.info("SQLDBSelect Error : ");
			Executionstatus=false;
		}
		Expected_value=EnvironmentValue.getProperty(Expected_value)+appendValue;

		try{
			if(!Eshop_SQLServer.wasNull()){            // If some value is present in the fired Query
				if(Actual_Value.equalsIgnoreCase((Expected_value.toString()))){
					Report_Functions.ReportEventSuccess(doc, "1","EShopSQLDBSelectFromEnv", "The actual value : '"+Actual_Value+"' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' matches the expected value : '"+Expected_value+"'", 2);
					Executionstatus=true;
				}else if(!(Actual_Value.equals(Expected_value))){
					Report_Functions.ReportEventFailure(doc,  "EShopSQLDBSelectFromEnv",  "The actual value : '"+Actual_Value+"' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' does not match the expected value : '"+Expected_value+"'", true);
					Executionstatus=false;
				}
			}else if(Eshop_SQLServer.wasNull()){        // If "NULL" value is present in the fired Query
				if(Expected_value.equals("NULL")){
					Report_Functions.ReportEventSuccess(doc, "1", "EShopSQLDBSelectFromEnv", "The actual value 'NULL' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' matches with the expected value :'"+Expected_value+"'", 2);
					Executionstatus=true;
				}else if(!(Expected_value.equals("NULL"))){
					Report_Functions.ReportEventFailure(doc,  "EShopSQLDBSelectFromEnv",  "The actual value 'NULL' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' does not match the expected value : '"+Expected_value+"'", true);  	 
					Executionstatus=false;
				}
			}
		} catch (Exception e){
			Report_Functions.ReportEventFailure(doc,  "EShopSQLDBSelectFromEnv",  "Error occured while comparing the values in SQL query.Error description is : "+ e.getMessage() +".", true);
			Log.info("EShopSQLDBSelectFromEnv Error : " + e);
			Executionstatus=false;
		}
		return Executionstatus;
	}



	/**
	 * 
	 * @Objective <b>Verifies to update the data for RRBS in SQL DB</b>
	 * @param sqltablename
	 * @param strsqlcolumnname
	 * @param strsqlcolumnvalue
	 * @param strsqlcondition
	 * @param strExecEventFlag
	 * @author <b>Muralimohan M</b>
	 * @since <b>19-Dec-16</b>
	 */

	public static boolean DBCommonPostCondition(String dbType, String strformatType,String sqltablename, String strsqlcolumnname,String strsqlcondition,String strExpectedvalue,String Days_to_add,String environmentVariable,String Append,int strExecEventFlag)throws Exception{
		boolean status= false;
		String formatType=null;
		String Database=null;
		try {
			if (strExecEventFlag==1){
				Database=RetrieveTestDataValue("DBCommonPostCondition",dbType,strExecEventFlag);
				formatType=RetrieveTestDataValue("DBCommonPostCondition",strformatType,strExecEventFlag);
			}
			if(Database==null || formatType==null){
				Report_Functions.ReportEventFailure(doc,  "DBCommonPostCondition",  "Required details are not provided in the data sheet.", false);
				return false;
			}

			switch(Database.toLowerCase()){
					case "sql":	switch(formatType.toLowerCase()){
								case "normal":status= SQLDBSelect(sqltablename, strsqlcolumnname, strsqlcondition, strExpectedvalue, strExecEventFlag);break;
								case "date":status= SQLDBDateFormatCompare(sqltablename, strsqlcolumnname, strsqlcondition, strExpectedvalue, strExecEventFlag);break;
								case "futuredate": status= SQLDBFutureDateCompare(sqltablename, strsqlcolumnname, strsqlcondition, "dd/MM/yyyy", Days_to_add, strExecEventFlag);break;
								case "envvariable":status= SQLDBSelectFromEnv(sqltablename, strsqlcolumnname, strsqlcondition, strExpectedvalue, strExecEventFlag);break;
			   					case "sqlconditionappendvar":status= SQLDBSelectAppendConditionEnvVar(sqltablename, strsqlcolumnname,strsqlcondition, strExpectedvalue,environmentVariable,Append,1);break;
								case "retrievevalueandstoreinenv":status= MVNO_RetrieveValueAndStoreInEnv(sqltablename, strsqlcolumnname,strsqlcondition, environmentVariable,1);break;
								default:
								log.info("Invalid Action item from Excel and not present in the switch case::"+formatType);
								Report_Functions.ReportEventFailure(doc,  "",  "Invalid Action Type described in Excel sheet for SQL - "+formatType, false);break;
								}break;
			   		case "rrbs": switch(formatType.toLowerCase()){
								case "normal":status= RRBSDBSelect(sqltablename, strsqlcolumnname, strsqlcondition, strExpectedvalue, strExecEventFlag);break;
								case "date":status= RRBSDBDateCompare(sqltablename, strsqlcolumnname, strsqlcondition, "dd/MM/yyyy", strExecEventFlag);break;
								case "futuredate":status= SQLDBFutureDateCompare(sqltablename, strsqlcolumnname, strsqlcondition, "dd/MM/yyyy", Days_to_add, strExecEventFlag);break;
								case "envvariable":status=RRBSSQLDBSelectFromEnv(sqltablename, strsqlcolumnname, strsqlcondition, strExpectedvalue, strExecEventFlag);break;
								default:
								log.info("Invalid Action item from Excel and not present in the switch case::"+formatType);
								Report_Functions.ReportEventFailure(doc,  "",  "Invalid Action Type described in Excel sheet for SQL - "+formatType, false);break;
							   }break;
			   		case "eshop":switch(formatType.toLowerCase()){
			   					 case "normal":status= EShopSQLDBSelect(sqltablename, strsqlcolumnname, strsqlcondition, strExpectedvalue, strExecEventFlag);break;
			   					 case "date":status= EshopSQLDBDateFormatCompare(sqltablename, strsqlcolumnname, strsqlcondition, "dd/MM/yyyy", strExecEventFlag);break;
			   					 case "envvariable":status= EshopSQLDBSelectFromEnv(sqltablename, strsqlcolumnname, strsqlcondition, strExpectedvalue, strExecEventFlag);break;
			   					 case "futuredate":status= EshopSQLDBFutureDateCompare(sqltablename, strsqlcolumnname, strsqlcondition, "dd/MM/yyyy", Days_to_add, strExecEventFlag);break;
			   					 case "sqlconditionappendvar":status= EShopSQLDBSelectAppendConditionEnvVar(sqltablename, strsqlcolumnname,strsqlcondition, strExpectedvalue,environmentVariable,Append,1);break;
			   					 case "sqlvalueappendandcompare":status= EshopSQLDBSelect_AppendAndCompare(sqltablename, strsqlcolumnname,strsqlcondition, strExpectedvalue,Append,1);break;
			   					 case "retrievevalueandstoreinenv":status= Eshop_RetrieveValueAndStoreInEnv(sqltablename, strsqlcolumnname,strsqlcondition, environmentVariable,1);break;
		   					default:
								log.info("Invalid Action item from Excel and not present in the switch case::"+formatType);
								Report_Functions.ReportEventFailure(doc,  "",  "Invalid Action Type described in Excel sheet for SQL - "+formatType, false);break;
			   					}break;
				   	default:
					Report_Functions.ReportEventFailure(doc,  "",  "Invalid Database Type described in Excel sheet for SQL - "+formatType, false);break;
			}		
		} catch (Exception e) { 
			status=false;
			Report_Functions.ReportEventFailure(doc,  "DBCommonPostCondition",  "Error occured while executing the SQL query.Error description is : "+ e.getMessage() +".", true);
			log.info("DBCommonPostCondition Error : " + e);
		}
		return status;
	}

	
	/**
	 * 
	 * @Objective <b>Take value from the column value in Eshop and store in environment variable</b>
	 * @param tablename
	 * @param columnname
	 * @param condition
	 * @param envVariable
	 * @param strExecEventFlag
	 * @author <b>Muralimohan M</b>
	 * @since <b>20-Dec - 16</b>
	 */
	public static boolean Eshop_RetrieveValueAndStoreInEnv(String tablename, String columnname,String condition, String envVariable,  int strExecEventFlag)throws Exception  {

		boolean elementStatus= false;
		String actualvalue = null;
		String environmentVariable= null;

		try{

			if(strExecEventFlag==1){
				tablename=RetrieveTestDataValue("Eshop_RetrieveValueAndStoreInEnv",tablename,strExecEventFlag);
				condition=RetrieveTestDataValue("Eshop_RetrieveValueAndStoreInEnv",condition,strExecEventFlag);
				columnname=RetrieveTestDataValue("Eshop_RetrieveValueAndStoreInEnv",columnname,strExecEventFlag);
				environmentVariable=RetrieveTestDataValue("Eshop_RetrieveValueAndStoreInEnv",envVariable,strExecEventFlag);
			}

			if(tablename==null){
				Report_Functions.ReportEventFailure(doc,  "Eshop_RetrieveValueAndStoreInEnv",  "SQL table name is not provided in the Data Sheet.", false);
				elementStatus= false;
				throw new RuntimeException ("Table_name is null");
			}

			if(columnname==null){
				Report_Functions.ReportEventFailure(doc,  "Eshop_RetrieveValueAndStoreInEnv",  "SQL Column name is not provided in the Data Sheet.", false);
				elementStatus= false;
				throw new RuntimeException ("Column_name is null");
			}

			if(condition==null){
				Report_Functions.ReportEventFailure(doc,  "Eshop_RetrieveValueAndStoreInEnv",  "SQL condition is not provided in the Data Sheet.", false);
				elementStatus= false;
				throw new RuntimeException ("SQL_condition is null");
			}


		}catch (Exception e) {
			Report_Functions.ReportEventFailure(doc,  "Eshop_RetrieveValueAndStoreInEnv",  "Error occured .Error description is : "+ e.getMessage() +".", true);
			Log.info("EshopSQLDBEnvironmentVariableCompare Error : " + e);
			elementStatus=false;
		}

		//Query to Execute      
		String query = "select "+ columnname +" from "+ tablename +" where "+ condition;
		log.info("query is : "+query);

		try {
			ResultSet Eshopresultset = EShopstmt.executeQuery(query);
			while (Eshopresultset.next()){
				actualvalue = Eshopresultset.getString(1);
			}

			EnvironmentValue.setProperty(environmentVariable, actualvalue);

			Report_Functions.ReportEventSuccess(doc,"1", "Eshop_RetrieveValueAndStoreInEnv", "The value : '"+ actualvalue +"' is stored in the environment variable : '"+ environmentVariable +"' successfully.",3);
			elementStatus=true;
		} catch (Exception e) { 
			elementStatus=false;
			Report_Functions.ReportEventFailure(doc,  "Eshop_RetrieveValueAndStoreInEnv",  "Error occured while executing the RRBS query.Error description is : "+ e.getMessage() +".", false);
			Log.info("Eshop_RetrieveValueAndStoreInEnv Error : " + e);
		}

		return elementStatus;
	}
	
	/**
	 * 
	 * @Objective <b>Take value from the column value in Eshop and store in environment variable</b>
	 * @param tablename
	 * @param columnname
	 * @param condition
	 * @param envVariable
	 * @param strExecEventFlag
	 * @author <b>Muralimohan M</b>
	 * @since <b>20-Dec - 16</b>
	 */
	public static boolean MVNO_RetrieveValueAndStoreInEnv(String tablename, String columnname,String condition, String envVariable,  int strExecEventFlag)throws Exception  {

		boolean elementStatus= false;
		String actualvalue = null;
		String environmentVariable= null;

		try{

			if(strExecEventFlag==1){
				tablename=RetrieveTestDataValue("MVNO_RetrieveValueAndStoreInEnv",tablename,strExecEventFlag);
				condition=RetrieveTestDataValue("MVNO_RetrieveValueAndStoreInEnv",condition,strExecEventFlag);
				columnname=RetrieveTestDataValue("MVNO_RetrieveValueAndStoreInEnv",columnname,strExecEventFlag);
				environmentVariable=RetrieveTestDataValue("MVNO_RetrieveValueAndStoreInEnv",envVariable,strExecEventFlag);
			}

			if(tablename==null){
				Report_Functions.ReportEventFailure(doc,  "MVNO_RetrieveValueAndStoreInEnv",  "SQL table name is not provided in the Data Sheet.", false);
				elementStatus= false;
				throw new RuntimeException ("Table_name is null");
			}

			if(columnname==null){
				Report_Functions.ReportEventFailure(doc,  "MVNO_RetrieveValueAndStoreInEnv",  "SQL Column name is not provided in the Data Sheet.", false);
				elementStatus= false;
				throw new RuntimeException ("Column_name is null");
			}

			if(condition==null){
				Report_Functions.ReportEventFailure(doc,  "MVNO_RetrieveValueAndStoreInEnv",  "SQL condition is not provided in the Data Sheet.", false);
				elementStatus= false;
				throw new RuntimeException ("SQL_condition is null");
			}


		}catch (Exception e) {
			Report_Functions.ReportEventFailure(doc,  "MVNO_RetrieveValueAndStoreInEnv",  "Error occured .Error description is : "+ e.getMessage() +".", true);
			Log.info("MVNO_RetrieveValueAndStoreInEnv Error : " + e);
			elementStatus=false;
		}

		//Query to Execute      
		String query = "select "+ columnname +" from "+ tablename +" where "+ condition;
		log.info("query is : "+query);

		try {
			ResultSet Eshopresultset = stmt.executeQuery(query);
			while (Eshopresultset.next()){
				actualvalue = Eshopresultset.getString(1);
			}
			EnvironmentValue.setProperty(environmentVariable, actualvalue);
			Report_Functions.ReportEventSuccess(doc,"1", "MVNO_RetrieveValueAndStoreInEnv", "The value : '"+ actualvalue +"' is stored in the environment variable : '"+ environmentVariable +"' successfully.",3);
			elementStatus=true;
		} catch (Exception e) { 
			elementStatus=false;
			Report_Functions.ReportEventFailure(doc,  "MVNO_RetrieveValueAndStoreInEnv",  "Error occured while executing the RRBS query.Error description is : "+ e.getMessage() +".", false);
			Log.info("MVNO_RetrieveValueAndStoreInEnv Error : " + e);
		}

		return elementStatus;
	}
	
	/**
	 * @Objective <b>Verifies the SQL GBR DB select with Expected Value from ENV Variable</b>
	 * @param sqltablename
	 * @param strsqlcolumnname
	 * @param strsqlcondition
	 * @param strExpectedvalue
	 * @param strExecEventFlag
	 * @author <b>Muralimohan M</b>
	 * @since <b>10-AUG-16</b>
	 */

	public static boolean SQLDBSelectAppendConditionEnvVar(String sqltablename, String strsqlcolumnname,String strsqlcondition,String strExpectedValue,String environmentVariableString,String StrAppend,int strExecEventFlag)throws Exception  {
		boolean SQLDBSelectAppendConditionEnvVar= false;
		String query = null;
		String Table_name = null;
		String Column_name = null;
		String SQL_condition = null;
		//String Expected_value = null;		
		String Expected_value = null;
		String envVariable=null;
		String Actual_Value = null;
		String AppendValue=null;
		String SQLConditionAppend=null;
		ResultSet rs = null;
		//Statement statement =null;
		try {
			if(strExecEventFlag==1){
				Table_name=RetrieveTestDataValue("SQLDBSelectAppendConditionEnvVar",sqltablename,strExecEventFlag);
				Column_name=RetrieveTestDataValue("SQLDBSelectAppendConditionEnvVar",strsqlcolumnname,strExecEventFlag);
				SQL_condition=RetrieveTestDataValue("SQLDBSelectAppendConditionEnvVar",strsqlcondition,strExecEventFlag);
				envVariable=RetrieveTestDataValue("SQLDBSelectAppendConditionEnvVar",environmentVariableString,strExecEventFlag);
				envVariable=EnvironmentValue.getProperty(envVariable);
				AppendValue=RetrieveTestDataValue("SQLDBSelectAppendConditionEnvVar",StrAppend,strExecEventFlag);
				Expected_value=RetrieveTestDataValue("SQLDBSelectAppendConditionEnvVar",strExpectedValue,strExecEventFlag);
			}

			if(Table_name==null || Column_name==null || SQL_condition==null){
				Report_Functions.ReportEventFailure(doc,  "SQLDBSelectAppendConditionEnvVar",  "Required details are not provided in test data sheet.", false);
				return false;
			}

			SQLConditionAppend=envVariable+AppendValue;
			if(Expected_value==null){
				Report_Functions.ReportEventFailure(doc,  "SQLDBSelectAppendConditionEnvVar",  "Dynamic Variable '"+envVariable+"' has no value.", false);
				return false;
			}
			//envVariable=EnvironmentValue.getProperty(envVariable);
			//Query to Execute      
			query = "select "+Column_name+" from "+Table_name+" where "+SQL_condition+"='"+SQLConditionAppend+"' order by 1 desc";
			Log.info("Query framed in SQLDBSelectAppendConditionEnvVar::\n"+query);
			rs = stmt.executeQuery(query);
			Log.info("Query has been Executed successfully");

		} catch (Exception e) {
			Report_Functions.ReportEventFailure(doc,  "SQLDBSelectAppendConditionEnvVar",  "Error occured while executing the SQL query.Error description is : "+ e.getMessage() +".", true);
			log.info("SQLDBSelect Error : " + e);
			SQLDBSelectAppendConditionEnvVar=false;
		}

		try{
			rs.next();
			Actual_Value = rs.getString(1).trim();

		} catch(NullPointerException e){

			log.info("Null pointer occurred :"+e.getMessage());

		} catch (Exception e) {           // If no record is present in the fired Query
			Report_Functions.ReportEventFailure(doc,  "SQLDBSelectAppendConditionEnvVar",  "No Record found for this query: "+query, true);
			log.info("SQLDBSelect Error : ");
			SQLDBSelectAppendConditionEnvVar=false;
		}

		try{
			if(!rs.wasNull()){            // If some value is present in the fired Query
				if(Actual_Value.equalsIgnoreCase(Expected_value)){
					Report_Functions.ReportEventSuccess(doc, "1","SQLDBSelectAppendConditionEnvVar", "The actual value : '"+Actual_Value+"' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"='"+SQLConditionAppend+"' matches the expected Dynamic value : '"+Expected_value+"'", 2);
					SQLDBSelectAppendConditionEnvVar=true;
				}else if(!(Actual_Value.equalsIgnoreCase(Expected_value))){
					Report_Functions.ReportEventFailure(doc,  "SQLDBSelectAppendConditionEnvVar",  "The actual value : '"+Actual_Value+"' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"='"+SQLConditionAppend+"' does not match the expected Dynamic value : '"+Expected_value+"'", true);
					SQLDBSelectAppendConditionEnvVar=false;
				}
			}

			else if(rs.wasNull()){        // If "NULL" value is present in the fired Query
				if(Expected_value.equalsIgnoreCase("NULL")){
					Report_Functions.ReportEventSuccess(doc, "1", "SQLDBSelectAppendConditionEnvVar", "The actual value 'NULL' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' matches with the expected Dynamic value :'"+Expected_value+"'", 2);
					SQLDBSelectAppendConditionEnvVar=true;
				}else if(!(Expected_value.equalsIgnoreCase("NULL"))){
					Report_Functions.ReportEventFailure(doc,  "SQLDBSelectAppendConditionEnvVar",  "The actual value 'NULL' in column : '"+Column_name+"' of table : '"+Table_name+"' with condition : '"+SQL_condition+"' does not match the expected Dynamic value : '"+Expected_value+"'", true);  	 
					SQLDBSelectAppendConditionEnvVar=false;
				}
			}
		}catch(NullPointerException e){

			log.info("Null pointer occurred :"+e.getMessage());

		}catch (Exception e){
			Report_Functions.ReportEventFailure(doc,  "SQLDBSelectAppendConditionEnvVar",  "Error occured while comparing the values in SQL query.Error description is : "+ e.getMessage() +".", true);
			log.info("SQLDBSelectAppendConditionEnvVar Error : " + e);
			SQLDBSelectAppendConditionEnvVar=false;
		}
		return SQLDBSelectAppendConditionEnvVar;
	}
	
	

}
