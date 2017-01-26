package Libraries;

import java.util.Arrays;

import Libraries.Driver_Script;
import Libraries.Function_Library;
import Libraries.Report_Functions;
import Utility.Log;
import Utility.ReadExcel;

public class App_Components extends Driver_Script {
	
	static boolean[] AppComponentStatus=null;
	static int[] appComponentEvents=null;

	public static int[] skipEvent(int[] appComponentEvents1,int  removeAction){
		int reorderedIndex = appComponentEvents1.length -1;
		for(int r=removeAction;r<reorderedIndex;r++){
			appComponentEvents[r]= appComponentEvents[r+1];
		}
		int[] appComponentEventstemp= Arrays.copyOf(appComponentEvents, reorderedIndex);
		return appComponentEventstemp;
	}

	public static int[] InitializeEvent(int  totalEvents, String  disableEvents){

		String[] eachDisabledEvents=null;
		int z;
		try{
			AppComponentStatus=new boolean[totalEvents];
			for(int m=0;m<(totalEvents);m++){
				AppComponentStatus[m]=true;
			}
			//Initializing the array with the values 1,2,3,4.... upto total events
			appComponentEvents =new int[totalEvents];
			for(int a=0;a<(totalEvents);a++){
				appComponentEvents[a]=a+1;
			}
			if(!( (disableEvents.equals("0") ) || (disableEvents.equals("0.0")) ) ){	
				Log.info("Disable Events available...");
				//Retrieving each event seperately
				eachDisabledEvents=disableEvents.split(",");
				for(int de=0;de<eachDisabledEvents.length;de++){
					eachDisabledEvents[de]=eachDisabledEvents[de].replace(".0", "");
				}
				z=1;
				for(String  a : eachDisabledEvents ){
					//Call the skipEvent function	
					appComponentEvents=App_Components.skipEvent(appComponentEvents, Integer.parseInt(a)-z);
					z=z+1;	
				}
			}
		}catch(Exception e){
			Log.info("Error inside InitializeEvent  : "+e);
			throw e;
		}finally{
			if(eachDisabledEvents!=null){
				for(int j=0;j<eachDisabledEvents.length;j++){
					eachDisabledEvents[j]=null;
				}
				eachDisabledEvents=null;
			}
		}
		return appComponentEvents;
	}

	public static void ValidateComponent(int[] appComponentEvents,boolean[] AppComponentStatus,int noEventStatus,String ComponentName) throws Exception{

		try{
			boolean testStepsStatus=true;
			for(int k=0;k<appComponentEvents.length;k++){
				boolean stepStatus=AppComponentStatus[k];
				if(stepStatus != true){
					testStepsStatus=false;
				}
			}
			if(noEventStatus==1){
				testStepsStatus=false;
			}
			//Vaidating the Result in Report
			Function_Library.Validate_AppComponent(ComponentName, testStepsStatus);
		}catch(Exception e){
			Log.info("Error in ValidateComponent of App_Components  : "+e);
			throw e;
		}
	}


	public static void SQLDB_Delete() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=1;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.SQLDBDelete("Table_Name_Delete", "Condition", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	public static void Close_SQL_Connection() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=1;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.SQLDBCloseConnection();
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	public static void Open_RRBS_Connection() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			//totalEvents=ReadExcel.RetrieveTotalEventsCount(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Total_Events", gblAppComponentCounter);
			totalEvents=1;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.RRBSDBOpenConnection("RRBS_DB_Server", "RRBS_DB_Portnumber", "RRBS_DB_Name", "RRBS_DB_Username", "RRBS_DB_Password");
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );
					break;
					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" + EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus,  EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	public static void Close_RRBS_Connection() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;
		try{
			totalEvents=1;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1"  :AppComponentStatus[testEvent] = Function_Library.RRBSDBCloseConnection();
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );
					break;
					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" + EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus,  EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	public static void Open_EXIBS_Connection() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			//totalEvents=ReadExcel.RetrieveTotalEventsCount(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Total_Events", gblAppComponentCounter);
			totalEvents=1;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.EXIBSDBOpenConnection("EXIBS_DB_Server", "EXIBS_DB_Portnumber", "EXIBS_DB_Name", "EXIBS_DB_Username", "EXIBS_DB_Password");;
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );
					break;
					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" + EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus,  EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	public static void Close_EXIBS_Connection() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;
		try{
			totalEvents=1;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1"  :AppComponentStatus[testEvent] = Function_Library.EXIBSDBCloseConnection();
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );
					break;
					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" + EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus,  EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	public static void XMLConfigValidInvalid() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=2;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.XMLValueUpdate("Location", "Key", "PreCondition_Value", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );
					break;
					case "ACEvent2": AppComponentStatus[testEvent] = Function_Library.XMLValueUpdate("Location", "Key", "PostCondition_Value", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );
					break;
					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	public static void EShopSQLDBOpenConnection_USA() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=1;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.EshopSQLDBOpenConnection();
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	public static void EshopSQLDB_CloseConnection() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=1;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.EshopSQLDBCloseConnection();
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	/**
	 * @author <b>LAKSHMAN M</b>
	 * @see SQL DB Delete for USA
	 * @throws Exception
	 */
	public static void Preconditions_REG_USA() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=1;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.SQLDBDelete("Table_Name", "Condition", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	public static void Open_Command_Prompt() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=1;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.jsh_Unix_Open_Connection();
					break;

					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	public static void Close_Command_Prompt() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=1;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.Jsh_closeUnixSession();
					break;

					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	public static void Start_Script_XMLVal_SinglePair() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=2;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.Unix_ITG_OutputResult_Verify("Command_Start","Output_START_Result",1,"Entity_Name");
					break;
					case "ACEvent2": AppComponentStatus[testEvent] = Function_Library.Verify_XML_Response_Data("Request_Type","Input_XML_Data","Output_XML_Data","XLDB_Input_Query","Entity_Name",1);
					break;
					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}
	
	
	public static void Start_Script_XMLVal_Dynamic() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=2;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.Unix_ITG_OutputResult_Verify("Command_Start","Output_START_Result",1,"Entity_Name");
					break;
					case "ACEvent2": AppComponentStatus[testEvent] = Function_Library.Verify_XML_Response_Data_DB("Request_Type","Input_XML_Data","Output_XML_Static_Nodes","XLDB_Input_Query","SQLQuery","PARENT_NODE","PRIMARY_NODE","EXPECTED_NODE_VALUE","Entity_Name",1);
					break;
					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	public static void Stop_Script() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=1;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.Unix_ITG_OutputResult_Verify("Command_Stop","Output_STOP_Result",1,"Entity_Name");
					
					break;
					
					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	public static void Validating_CDR() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=1;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.Unix_ITG_OutputResult_Verify("CDR","Output_CDR_Result",1,"Entity_Name");
					break;

					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	public static void Start_Script_XMLVal_SP_NodeEnv() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=2;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.Unix_ITG_OutputResult_Verify("Command_Start","Output_START_Result",1,"Entity_Name");
					break;
					case "ACEvent2": AppComponentStatus[testEvent] = Function_Library.Verify_XML_Response_Data_NodeValtoEnv("Request_Type","Input_XML_Data","Output_XML_Data","XLDB_Input_Query","NodeVal_to_Environment","","Entity_Name",1);
					break;
					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}
	

	public static void EShopSQLDB_Select() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=2;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.EShopSQLDBSelect("Table_Name","Column_Name","Condition","Column_Value",1);
					break;
					case "ACEvent2": AppComponentStatus[testEvent] = Function_Library.EShopSQLDBSelectFromEnv("Table_Name","Column_Name","Condition","NodeVal_to_Environment","Expected_Value",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	public static void Insert_Update_Delete_EShopSQL() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=3;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.EShopSQLDBSelect("Table_Name","Column_Name","Condition","Column_Value",1);
					case "ACEvent2": AppComponentStatus[testEvent] = Function_Library.EshopSQLDBUpdate("Table_Name","Column_Name","Column_Value","Condition_update",1);
					case "ACEvent3": AppComponentStatus[testEvent] = Function_Library.EshopSQLDBDelete("Table_Name","Condition",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	public static void Update_RRBS() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=1;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.RRBSDBUpdate("Table_Name","Column_Name","Column_Value","Condition",1);
					
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	public static void RRBSDB_Select() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=1;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name","ColumnName","Condition","ColumnValue",1);

					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	public static void XMLTextChanges() throws Exception{

		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=2;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.XMLTextUpdate("Location","Attribute","Value_1",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );
					break;
					case "ACEvent2": AppComponentStatus[testEvent] = Function_Library.XMLTextUpdate("Location","Attribute","Value_2",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );
					break;

					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	/**
	 * XMLTextUpdate
	* 
	* @author Laks3339 
	* @since Aug 8, 2016
	 */
	public static void XMLTextUpdate() throws Exception{

		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=2;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.XMLTextUpdate("Location","Attribute","Value_1",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );
					break;
					case "ACEvent2": AppComponentStatus[testEvent] = Function_Library.XMLTextUpdate("Location","Attribute","Value_2",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );
					break;

					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	public static void Open_USA_SQL_Connection() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=1;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.SQLDBOpenConnection(Param.getProperty("SQL_Server"), Param.getProperty("SQL_Server_DB_Name"), Param.getProperty("SQL_Server_UID"), Param.getProperty("SQL_Server_PWD"));
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	public static void Start_Script_XMLVal_SP_NodeEnv2() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=2;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.Unix_ITG_OutputResult_Verify("Command_Start","Output_START_Result",1,"Entity_Name");
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent2": AppComponentStatus[testEvent] = Function_Library.Verify_XML_Response_Data_NodeValtoEnv("Request_Type","Input_XML_Data","Output_XML_Data","XLDB_Input_Query","NodeVal_to_Environment","NodeVal_to_Environment2","Entity_Name",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	/**
	 * Start_Script_XMLVal_SP_NodeEnv3
	 * @author Lakshman A
	 * @since Aug 3, 2016
	 */
	public static void Start_Script_XMLVal_SP_NodeEnv3() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=2;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.Unix_ITG_OutputResult_Verify("Command_Start","Output_START_Result",1,"Entity_Name");
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent2": AppComponentStatus[testEvent] = Function_Library.Verify_XML_Response_Data_NodeValtoEnv2("Request_Type","Input_XML_Data","Output_XML_Data","XLDB_Input_Query","NodeVal_to_Environment","NodeVal_to_Environment2","","Validity_Days","Entity_Name",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	/**
	 * Start_Script_XMLVal_SP_NodeEnv4
	 * @author Lakshman A
	 * @since Aug 5, 2016
	 */
	public static void Start_Script_XMLVal_SP_NodeEnv4() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=2;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.Unix_ITG_OutputResult_Verify("Command_Start","Output_START_Result",1,"Entity_Name");
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent2": AppComponentStatus[testEvent] = Function_Library.Verify_XML_Response_Data_NodeValtoEnv2("Request_Type","Input_XML_Data","Output_XML_Data","XLDB_Input_Query","NodeVal_to_Environment","","","","Entity_Name",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	/** Start_Script_XMLVal_SP_NodeEnv3
	 * @author Lakshman A
	 * @since Aug 5, 2016
	 */
	public static void Start_Script_XMLVal_SP_NodeEnv5() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=2;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.Unix_ITG_OutputResult_Verify("Command_Start","Output_START_Result",1,"Entity_Name");
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent2": AppComponentStatus[testEvent] = Function_Library.Verify_XML_Response_Data_NodeValtoEnv2("Request_Type","Input_XML_Data","Output_XML_Data","XLDB_Input_Query","","","","Validity_Days","Entity_Name",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}
	
	/** Start_Script_XMLVal_SP_NodeEnv3
	 * @author Lakshman A
	 * @since Aug 5, 2016
	 */
	public static void Start_Script_XMLVal_SP_NodeEnv6() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=2;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.Unix_ITG_OutputResult_Verify("Command_Start","Output_START_Result",1,"Entity_Name");
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent2": AppComponentStatus[testEvent] = Function_Library.Verify_XML_Response_Data_NodeValtoEnv2("Request_Type","Input_XML_Data","Output_XML_Data","XLDB_Input_Query","NodeVal_to_Environment","NodeVal_to_Environment2","NodeVal_to_Environment3","Validity_Days","Entity_Name",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	public static void Pre_RRBS_OnlineTopup() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=2;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.RRBSDBUpdate("TABLE_RRBS_SUBSCRIBER_PROFILE","ACCT_BALANCE_COLUMN_NAME","ACCT_BALANCE_COLUMN_VALUE","RRBS_UPDATE_CONDITION",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent2": AppComponentStatus[testEvent] = Function_Library.RRBSDBDelete("TABLE_RRBS_POS_TOPUP","RRBS_DELETE_CONDITION",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	public static void Pre_OnLineTopup_Eshop() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=5;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.EshopSQLDBUpdate("TBL_FEATUREGATEWAYMAPPING","PRIMARYGATEWAYVENDOR_COLUMNNAME","PRIMARYGATEWAYVENDOR_COLUMNVALUE","ESHOPSQLDB_UPDATE_CONDITION",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent2": AppComponentStatus[testEvent] = Function_Library.EshopSQLDBDelete("TBL_USERCARDDETAILS","ESHOPSQLDB_DELETE_CONDITION",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent3": AppComponentStatus[testEvent] = Function_Library.EshopSQLDBDelete("TBL_AUTHORIZENETLOG","ESHOPSQLDB_DELETE_CONDITION",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent4": AppComponentStatus[testEvent] = Function_Library.EshopSQLDBDelete("TBL_WPTRASACTIONLOG","ESHOPSQLDB_DELETE_CONDITION",1);
					break;
					case "ACEvent5": AppComponentStatus[testEvent] = Function_Library.EshopSQLDBDelete("TBL_REDTRANSACTIONLOG","ESHOPSQLDB_DELETE_CONDITION",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;

					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	public static void Pre_OnlineTopup_MVNO() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=2;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.SQLDBDelete("TBL_MSTTOPUP","MSTTOPUP_DELETE_CONDITION",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent2": AppComponentStatus[testEvent] = Function_Library.SQLDBUpdate("TBL_MSTCUSTOMER","ISAUTOTOPUP_COLUMN_NAME","ISAUTOTOPUP_COLUMN_VALUE","MSTCUSTOMER_UPDATE_CONDITION",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					
					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	public static void Post_OnlineTopup() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=39;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1" : AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_MSTCUSTOMER","ISAUTOTOPUP_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","MSTCUSTOMER_ISAUTOTOPUP_EXPECTED_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent2" : AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_MSTTOPUP","ISAUTOTOPUP_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","MSTTOPUP_ISAUTOTOPUP_EXPECTED_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent3" : AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_MSTTOPUP","MSISDN_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","MSISDN_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent4" : AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_MSTTOPUP","TOPUPAMOUNT_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","TOPUPAMOUNT_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent5" : AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_MSTTOPUP","CARDID_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","CARDID_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent6" : AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_MSTTOPUP","TOPUPMODE_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","TOPUPMODE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent7" : AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_MSTTOPUP","STATUS_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","STATUS_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent8" : AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_MSTTOPUP","ERRORDESC_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","ERRORDESC_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent9" : AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_MSTTOPUP","MSISDN_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","MSISDN_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent10" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("TBL_MSTTOPUP","TAXAMT_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","TAXAMT_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent11" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("TBL_MSTTOPUP","VATAMT_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","VATAMT_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent12" : AppComponentStatus[testEvent]= Function_Library.SQLDBEnvironmentVariableCompare("TBL_MSTTOPUP","DYNAMIC_TRANSACTIONID_CONDITION","MSTCUSTOMER_SELECT_CONDITION","NodeVal_to_Environment",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent13" : AppComponentStatus[testEvent]= Function_Library.SQLDBEnvironmentVariableCompare("TBL_MSTTOPUP","DYNAMIC_REDORDERID_CONDITION","MSTCUSTOMER_SELECT_CONDITION","NodeVal_to_Environment2",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent14" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","USERCARDDET_ISAUTOTOPUP_COLUMN_NAME","SUBSCRIBERID_CONDITION","USERCARDDET_ISAUTOTOPUP_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent15" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","NAMEONCARD_COLUMN_NAME","SUBSCRIBERID_CONDITION","NAMEONCARD_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent16" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","CARDISSUENUMBER_COLUMN_NAME","SUBSCRIBERID_CONDITION","CARDISSUENUMBER_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent17" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","HOUSENUMBER_COLUMN_NAME","SUBSCRIBERID_CONDITION","HOUSENUMBER_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent18" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","ADDRESSLINE1_COLUMN_NAME","SUBSCRIBERID_CONDITION","ADDRESSLINE1_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent19" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","CITY_COLUMN_NAME","SUBSCRIBERID_CONDITION","CITY_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent20" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","COUNTY_COLUMN_NAME","SUBSCRIBERID_CONDITION","COUNTY_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent21" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","COUNTRY_COLUMN_NAME","SUBSCRIBERID_CONDITION","COUNTRY_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent22" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","POSTCODE_COLUMN_NAME","SUBSCRIBERID_CONDITION","POSTCODE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent23" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","EMAIL_COLUMN_NAME","SUBSCRIBERID_CONDITION","EMAIL_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent24" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","NICKNAME_COLUMN_NAME","SUBSCRIBERID_CONDITION","NICKNAME_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent25" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","AUTHNET_AMOUNT_COLUMN_NAME","SUBSCRIBERID_CONDITION","AUTHNET_AMOUNT_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent26" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","ORDERDESC_COLUMN_NAME","SUBSCRIBERID_CONDITION","ORDERDESC_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent27" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","ITEMUNITPRICE_COLUMN_NAME","SUBSCRIBERID_CONDITION","ITEMUNITPRICE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent28" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","STATUS_TEXT_COLUMN_NAME","SUBSCRIBERID_CONDITION","STATUS_TEXT_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent29" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","ACCOUNTNUMBER_COLUMN_NAME","SUBSCRIBERID_CONDITION","ACCOUNTNUMBER_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent30" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","ACCOUNTTYPE_COLUMN_NAME","SUBSCRIBERID_CONDITION","ACCOUNTTYPE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent31" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","MESSAGECODE_COLUMN_NAME","SUBSCRIBERID_CONDITION","MESSAGECODE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent32" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","MESSAGEDESC_COLUMN_NAME","SUBSCRIBERID_CONDITION","MESSAGEDESC_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent33" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","AUTH_TAXID_COLUMN_NAME","SUBSCRIBERID_CONDITION","AUTH_TAXID_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent34" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","AUTH_VATID_COLUMN_NAME","SUBSCRIBERID_CONDITION","AUTH_VATID_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent35" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","SUBSCRIBERID_COLUMN_NAME","SUBSCRIBERID_CONDITION","SUBSCRIBERID_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent36" : AppComponentStatus[testEvent]= Function_Library.EshopSQLDBEnvironmentVariableCompare("TABLE_AUTHORIZENET","DYNAMIC_TRANSID_CONDITION","SUBSCRIBERID_CONDITION","NodeVal_to_Environment",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent37" : AppComponentStatus[testEvent]= Function_Library.EshopSQLDBEnvironmentVariableCompare("TABLE_AUTHORIZENET","AUTH_INVOICENUMBER_CONDITION","SUBSCRIBERID_CONDITION","NodeVal_to_Environment2",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent38" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","CARDSTARTDATE_COLUMN_NAME","SUBSCRIBERID_CONDITION","CARDSTARTDATE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent39" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","CARDEXPIRYDATE_COLUMN_NAME","SUBSCRIBERID_CONDITION","CARDEXPIRYDATE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;

					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}
	
	
	/**
	 * Post_OnlineTopup_VAT
	 * or performing Post conditions after doing Bundle Topup.
	 * @author Lakshman
	 * @since Aug 9, 2016
	 */
	
	public static void Post_OnlineTopup_VAT() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=39;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1" : AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_MSTCUSTOMER","ISAUTOTOPUP_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","MSTCUSTOMER_ISAUTOTOPUP_EXPECTED_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent2" : AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_MSTTOPUP","ISAUTOTOPUP_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","MSTTOPUP_ISAUTOTOPUP_EXPECTED_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent3" : AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_MSTTOPUP","MSISDN_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","MSISDN_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent4" : AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_MSTTOPUP","TOPUPAMOUNT_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","TOPUPAMOUNT_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent5" : AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_MSTTOPUP","CARDID_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","CARDID_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent6" : AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_MSTTOPUP","TOPUPMODE_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","TOPUPMODE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent7" : AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_MSTTOPUP","STATUS_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","STATUS_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent8" : AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_MSTTOPUP","ERRORDESC_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","ERRORDESC_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent9" : AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_MSTTOPUP","MSISDN_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","MSISDN_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent10" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("TBL_MSTTOPUP","TAXAMT_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","TAXAMT_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent11" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("TBL_MSTTOPUP","VATAMT_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","VATAMT_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent12" : AppComponentStatus[testEvent]= Function_Library.SQLDBEnvironmentVariableCompare("TBL_MSTTOPUP","DYNAMIC_TRANSACTIONID_CONDITION","MSTCUSTOMER_SELECT_CONDITION","NodeVal_to_Environment",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent13" : AppComponentStatus[testEvent]= Function_Library.SQLDBEnvironmentVariableCompare("TBL_MSTTOPUP","DYNAMIC_REDORDERID_CONDITION","MSTCUSTOMER_SELECT_CONDITION","NodeVal_to_Environment2",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent14" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","USERCARDDET_ISAUTOTOPUP_COLUMN_NAME","SUBSCRIBERID_CONDITION","USERCARDDET_ISAUTOTOPUP_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent15" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","NAMEONCARD_COLUMN_NAME","SUBSCRIBERID_CONDITION","NAMEONCARD_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent16" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","CARDISSUENUMBER_COLUMN_NAME","SUBSCRIBERID_CONDITION","CARDISSUENUMBER_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent17" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","HOUSENUMBER_COLUMN_NAME","SUBSCRIBERID_CONDITION","HOUSENUMBER_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent18" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","ADDRESSLINE1_COLUMN_NAME","SUBSCRIBERID_CONDITION","ADDRESSLINE1_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent19" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","CITY_COLUMN_NAME","SUBSCRIBERID_CONDITION","CITY_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent20" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","COUNTY_COLUMN_NAME","SUBSCRIBERID_CONDITION","COUNTY_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent21" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","COUNTRY_COLUMN_NAME","SUBSCRIBERID_CONDITION","COUNTRY_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent22" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","POSTCODE_COLUMN_NAME","SUBSCRIBERID_CONDITION","POSTCODE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent23" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","EMAIL_COLUMN_NAME","SUBSCRIBERID_CONDITION","EMAIL_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent24" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","NICKNAME_COLUMN_NAME","SUBSCRIBERID_CONDITION","NICKNAME_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent25" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","AUTHNET_AMOUNT_COLUMN_NAME","SUBSCRIBERID_CONDITION","AUTHNET_AMOUNT_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent26" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","ORDERDESC_COLUMN_NAME","SUBSCRIBERID_CONDITION","ORDERDESC_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent27" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","ITEMUNITPRICE_COLUMN_NAME","SUBSCRIBERID_CONDITION","ITEMUNITPRICE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent28" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","STATUS_TEXT_COLUMN_NAME","SUBSCRIBERID_CONDITION","STATUS_TEXT_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent29" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","ACCOUNTNUMBER_COLUMN_NAME","SUBSCRIBERID_CONDITION","ACCOUNTNUMBER_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent30" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","ACCOUNTTYPE_COLUMN_NAME","SUBSCRIBERID_CONDITION","ACCOUNTTYPE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent31" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","MESSAGECODE_COLUMN_NAME","SUBSCRIBERID_CONDITION","MESSAGECODE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent32" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","MESSAGEDESC_COLUMN_NAME","SUBSCRIBERID_CONDITION","MESSAGEDESC_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent33" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","AUTH_TAXID_COLUMN_NAME","SUBSCRIBERID_CONDITION","AUTH_TAXID_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent34" : AppComponentStatus[testEvent]= Function_Library.EshopSQLDBEnvironmentVariableCompare("TABLE_AUTHORIZENET","AUTH_VATID_COLUMN_NAME","SUBSCRIBERID_CONDITION","NodeVal_to_Environment3",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent35" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","SUBSCRIBERID_COLUMN_NAME","SUBSCRIBERID_CONDITION","SUBSCRIBERID_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent36" : AppComponentStatus[testEvent]= Function_Library.EshopSQLDBEnvironmentVariableCompare("TABLE_AUTHORIZENET","DYNAMIC_TRANSID_CONDITION","SUBSCRIBERID_CONDITION","NodeVal_to_Environment",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent37" : AppComponentStatus[testEvent]= Function_Library.EshopSQLDBEnvironmentVariableCompare("TABLE_AUTHORIZENET","AUTH_INVOICENUMBER_CONDITION","SUBSCRIBERID_CONDITION","NodeVal_to_Environment2",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent38" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","CARDSTARTDATE_COLUMN_NAME","SUBSCRIBERID_CONDITION","CARDSTARTDATE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent39" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","CARDEXPIRYDATE_COLUMN_NAME","SUBSCRIBERID_CONDITION","CARDEXPIRYDATE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;

					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}
	
	/**Post_OnlineTopup_VATTAX
	 * For performing Pre condition before doing Online Topup from mvno 
	 * @author Laks3339
	* @since Aug 9, 2016
	 */
	public static void Post_OnlineTopup_VATTAX() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=39;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1" : AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_MSTCUSTOMER","ISAUTOTOPUP_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","MSTCUSTOMER_ISAUTOTOPUP_EXPECTED_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent2" : AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_MSTTOPUP","ISAUTOTOPUP_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","MSTTOPUP_ISAUTOTOPUP_EXPECTED_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent3" : AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_MSTTOPUP","MSISDN_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","MSISDN_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent4" : AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_MSTTOPUP","TOPUPAMOUNT_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","TOPUPAMOUNT_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent5" : AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_MSTTOPUP","CARDID_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","CARDID_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent6" : AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_MSTTOPUP","TOPUPMODE_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","TOPUPMODE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent7" : AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_MSTTOPUP","STATUS_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","STATUS_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent8" : AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_MSTTOPUP","ERRORDESC_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","ERRORDESC_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent9" : AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_MSTTOPUP","MSISDN_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","MSISDN_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent10" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("TBL_MSTTOPUP","TAXAMT_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","TAXAMT_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent11" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("TBL_MSTTOPUP","VATAMT_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","VATAMT_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent12" : AppComponentStatus[testEvent]= Function_Library.SQLDBEnvironmentVariableCompare("TBL_MSTTOPUP","DYNAMIC_TRANSACTIONID_CONDITION","MSTCUSTOMER_SELECT_CONDITION","NodeVal_to_Environment",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent13" : AppComponentStatus[testEvent]= Function_Library.SQLDBEnvironmentVariableCompare("TBL_MSTTOPUP","DYNAMIC_REDORDERID_CONDITION","MSTCUSTOMER_SELECT_CONDITION","NodeVal_to_Environment2",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent14" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","USERCARDDET_ISAUTOTOPUP_COLUMN_NAME","SUBSCRIBERID_CONDITION","USERCARDDET_ISAUTOTOPUP_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent15" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","NAMEONCARD_COLUMN_NAME","SUBSCRIBERID_CONDITION","NAMEONCARD_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent16" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","CARDISSUENUMBER_COLUMN_NAME","SUBSCRIBERID_CONDITION","CARDISSUENUMBER_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent17" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","HOUSENUMBER_COLUMN_NAME","SUBSCRIBERID_CONDITION","HOUSENUMBER_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent18" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","ADDRESSLINE1_COLUMN_NAME","SUBSCRIBERID_CONDITION","ADDRESSLINE1_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent19" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","CITY_COLUMN_NAME","SUBSCRIBERID_CONDITION","CITY_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent20" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","COUNTY_COLUMN_NAME","SUBSCRIBERID_CONDITION","COUNTY_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent21" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","COUNTRY_COLUMN_NAME","SUBSCRIBERID_CONDITION","COUNTRY_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent22" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","POSTCODE_COLUMN_NAME","SUBSCRIBERID_CONDITION","POSTCODE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent23" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","EMAIL_COLUMN_NAME","SUBSCRIBERID_CONDITION","EMAIL_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent24" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","NICKNAME_COLUMN_NAME","SUBSCRIBERID_CONDITION","NICKNAME_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent25" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","AUTHNET_AMOUNT_COLUMN_NAME","SUBSCRIBERID_CONDITION","AUTHNET_AMOUNT_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent26" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","ORDERDESC_COLUMN_NAME","SUBSCRIBERID_CONDITION","ORDERDESC_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent27" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","ITEMUNITPRICE_COLUMN_NAME","SUBSCRIBERID_CONDITION","ITEMUNITPRICE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent28" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","STATUS_TEXT_COLUMN_NAME","SUBSCRIBERID_CONDITION","STATUS_TEXT_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent29" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","ACCOUNTNUMBER_COLUMN_NAME","SUBSCRIBERID_CONDITION","ACCOUNTNUMBER_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent30" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","ACCOUNTTYPE_COLUMN_NAME","SUBSCRIBERID_CONDITION","ACCOUNTTYPE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent31" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","MESSAGECODE_COLUMN_NAME","SUBSCRIBERID_CONDITION","MESSAGECODE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent32" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","MESSAGEDESC_COLUMN_NAME","SUBSCRIBERID_CONDITION","MESSAGEDESC_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent33" : AppComponentStatus[testEvent]= Function_Library.EshopSQLDBEnvironmentVariableCompare("TABLE_AUTHORIZENET","AUTH_TAXID_COLUMN_NAME","SUBSCRIBERID_CONDITION","NodeVal_to_Environment",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent34" : AppComponentStatus[testEvent]= Function_Library.EshopSQLDBEnvironmentVariableCompare("TABLE_AUTHORIZENET","AUTH_VATID_COLUMN_NAME","SUBSCRIBERID_CONDITION","NodeVal_to_Environment3",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent35" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","SUBSCRIBERID_COLUMN_NAME","SUBSCRIBERID_CONDITION","SUBSCRIBERID_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent36" : AppComponentStatus[testEvent]= Function_Library.EshopSQLDBEnvironmentVariableCompare("TABLE_AUTHORIZENET","DYNAMIC_TRANSID_CONDITION","SUBSCRIBERID_CONDITION","NodeVal_to_Environment",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent37" : AppComponentStatus[testEvent]= Function_Library.EshopSQLDBEnvironmentVariableCompare("TABLE_AUTHORIZENET","AUTH_INVOICENUMBER_CONDITION","SUBSCRIBERID_CONDITION","NodeVal_to_Environment2",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent38" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","CARDSTARTDATE_COLUMN_NAME","SUBSCRIBERID_CONDITION","CARDSTARTDATE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent39" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","CARDEXPIRYDATE_COLUMN_NAME","SUBSCRIBERID_CONDITION","CARDEXPIRYDATE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;

					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	/**
	 * Post_Do_BundleTopup
	 * or performing Post conditions after doing Bundle Topup.
	 * @author Lakshman
	 * @since Aug 3, 2016
	 */
	public static void Post_Do_BundleTopup() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=52;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_SUBS_Bundle_Buckets","ColumnName_Bundle_Code","Condition_RRBS_SUBS_Bundle_Buckets","ColumnValue_Bundle_Code",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent2" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_SUBS_Bundle_Buckets","ColumnName_Status","Condition_RRBS_SUBS_Bundle_Buckets","ColumnValue_Status1",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent3" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_SUBS_Bundle_Buckets","ColumnName_Charge_Mode","Condition_RRBS_SUBS_Bundle_Buckets","ColumnValue_Charge_Mode",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent4" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_SUBS_THRESH_LIMIT","ColumnName_ONNET_BUNDLE_COUNTER","Condition_RRBS_SUBS_THRESH_LIMIT","ColumnValue_ONNET_BUNDLE_COUNTER",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent5" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_POS_TOPUP","ColumnName_AMOUNT","Condition_RRBS_POS_TOPUP","ColumnValue_AMOUNT",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent6" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_POS_TOPUP","ColumnName_TOPUP","Condition_RRBS_POS_TOPUP","ColumnValue_TOPUP",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent7" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_POS_TOPUP","ColumnName_CARD_ID","Condition_RRBS_POS_TOPUP","ColumnValue_CARD_ID",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent8" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_POS_TOPUP","ColumnName_VAT_AMOUNT","Condition_RRBS_POS_TOPUP","ColumnValue_VAT_AMOUNT",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent9" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_POS_TOPUP","ColumnName_TAX_AMOUNT","Condition_RRBS_POS_TOPUP","ColumnValue_TAX_AMOUNT",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent10" : AppComponentStatus[testEvent]= Function_Library.SQLDBEnvironmentVariableCompare("Table_Name_Tbl_bundle_payment","ColumnName_TransactionID","Condition_Tbl_bundle_payment","NodeVal_to_Environment",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent11" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_BundleCode","Condition_Tbl_bundle_payment","ColumnValue_BundleCode",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent12" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_TopupAmount","Condition_Tbl_bundle_payment","ColumnValue_TopupAmount",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent13" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_TopupMode","Condition_Tbl_bundle_payment","ColumnValue_TopupMode",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent14" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_Status","Condition_Tbl_bundle_payment","ColumnValue_Status",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent15" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_ErrorDesc","Condition_Tbl_bundle_payment","ColumnValue_ErrorDesc",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent16" : AppComponentStatus[testEvent]= Function_Library.SQLDBEnvironmentVariableCompare("Table_Name_Tbl_bundle_payment","ColumnName_redorderid","Condition_Tbl_bundle_payment","NodeVal_to_Environment2",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent17" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_modeOfPayment","Condition_Tbl_bundle_payment","ColumnValue_modeOfPayment",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent18" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_isautotopup","Condition_Tbl_bundle_payment","ColumnValue_isautotopup",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent19" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_BundleName","Condition_Tbl_bundle_payment","ColumnValue_BundleName",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent20" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_Discount","Condition_Tbl_bundle_payment","ColumnValue_Discount",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent21" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_TaxAmount","Condition_Tbl_bundle_payment","ColumnValue_TaxAmount",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent22" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_VATamount","Condition_Tbl_bundle_payment","ColumnValue_VATamount",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent23" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_VATtransid","Condition_Tbl_bundle_payment","ColumnValue_VATtransid",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent24" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_TotalAmount","Condition_Tbl_bundle_payment","ColumnValue_TotalAmount",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent25" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_BundleMonths","Condition_Tbl_bundle_payment","ColumnValue_BundleMonths",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent26" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","USERCARDDET_ISAUTOTOPUP_COLUMN_NAME","SUBSCRIBERID_CONDITION","USERCARDDET_ISAUTOTOPUP_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent27" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","NAMEONCARD_COLUMN_NAME","SUBSCRIBERID_CONDITION","NAMEONCARD_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent28" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","CARDSTARTDATE_COLUMN_NAME","SUBSCRIBERID_CONDITION","CARDSTARTDATE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent29" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","CARDEXPIRYDATE_COLUMN_NAME","SUBSCRIBERID_CONDITION","CARDEXPIRYDATE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent30" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","CARDISSUENUMBER_COLUMN_NAME","SUBSCRIBERID_CONDITION","CARDISSUENUMBER_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent31" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","HOUSENUMBER_COLUMN_NAME","SUBSCRIBERID_CONDITION","HOUSENUMBER_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent32" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","ADDRESSLINE1_COLUMN_NAME","SUBSCRIBERID_CONDITION","ADDRESSLINE1_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent33" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","CITY_COLUMN_NAME","SUBSCRIBERID_CONDITION","CITY_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent34" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","COUNTY_COLUMN_NAME","SUBSCRIBERID_CONDITION","COUNTY_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent35" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","COUNTRY_COLUMN_NAME","SUBSCRIBERID_CONDITION","COUNTRY_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent36" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","POSTCODE_COLUMN_NAME","SUBSCRIBERID_CONDITION","POSTCODE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent37" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","EMAIL_COLUMN_NAME","SUBSCRIBERID_CONDITION","EMAIL_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent38" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","NICKNAME_COLUMN_NAME","SUBSCRIBERID_CONDITION","NICKNAME_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent39" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","AUTHNET_AMOUNT_COLUMN_NAME","SUBSCRIBERID_CONDITION","AUTHNET_AMOUNT_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent40" : AppComponentStatus[testEvent]= Function_Library.EshopSQLDBEnvironmentVariableCompare("TABLE_AUTHORIZENET","DYNAMIC_TRANSID_CONDITION","SUBSCRIBERID_CONDITION","NodeVal_to_Environment",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent41" : AppComponentStatus[testEvent]= Function_Library.EshopSQLDBEnvironmentVariableCompare("TABLE_AUTHORIZENET","AUTH_INVOICENUMBER_CONDITION","SUBSCRIBERID_CONDITION","NodeVal_to_Environment2",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent42" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","ORDERDESC_COLUMN_NAME","SUBSCRIBERID_CONDITION","ORDERDESC_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent43" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","ITEMUNITPRICE_COLUMN_NAME","SUBSCRIBERID_CONDITION","ITEMUNITPRICE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent44" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","STATUS_TEXT_COLUMN_NAME","SUBSCRIBERID_CONDITION","STATUS_TEXT_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent45" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","ACCOUNTNUMBER_COLUMN_NAME","SUBSCRIBERID_CONDITION","ACCOUNTNUMBER_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent46" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","ACCOUNTTYPE_COLUMN_NAME","SUBSCRIBERID_CONDITION","ACCOUNTTYPE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent47" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","MESSAGECODE_COLUMN_NAME","SUBSCRIBERID_CONDITION","MESSAGECODE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent48" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","MESSAGEDESC_COLUMN_NAME","SUBSCRIBERID_CONDITION","MESSAGEDESC_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent49" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","AUTH_TAXID_COLUMN_NAME","SUBSCRIBERID_CONDITION","AUTH_TAXID_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent50" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","AUTH_VATID_COLUMN_NAME","SUBSCRIBERID_CONDITION","AUTH_VATID_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent51" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_Tbluserbundledetails","ColumnName_Bundlecode","Condition_Tbluserbundledetails","ColumnValue_Bundlecode",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent52" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_Tbluserbundledetails","ColumnName_IS_renewal","Condition_Tbluserbundledetails","ColumnValue_IS_renewal",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;

					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}
	
	/**
	 * Post_Do_BundleTopup
	 * or performing Post conditions after doing Bundle Topup.
	 * @author Lakshman
	 * @since Aug 3, 2016
	 */
	public static void Post_Do_BundleTopup5() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=52;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelectConditionEnvVar("Table_Name_RRBS_SUBS_Bundle_Buckets","ColumnName_Bundle_Code","Condition_RRBS_SUBS_Bundle_Buckets","ColumnValue_Bundle_Code","NodeVal_to_Environment3",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent2" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelectConditionEnvVar("Table_Name_RRBS_SUBS_Bundle_Buckets","ColumnName_Status","Condition_RRBS_SUBS_Bundle_Buckets","ColumnValue_Status1","NodeVal_to_Environment3",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent3" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelectConditionEnvVar("Table_Name_RRBS_SUBS_Bundle_Buckets","ColumnName_Charge_Mode","Condition_RRBS_SUBS_Bundle_Buckets","ColumnValue_Charge_Mode","NodeVal_to_Environment3",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent4" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelectConditionEnvVar("Table_Name_RRBS_SUBS_THRESH_LIMIT","ColumnName_ONNET_BUNDLE_COUNTER","Condition_RRBS_SUBS_THRESH_LIMIT","ColumnValue_ONNET_BUNDLE_COUNTER","NodeVal_to_Environment3",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent5" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelectConditionEnvVar("Table_Name_RRBS_POS_TOPUP","ColumnName_AMOUNT","Condition_RRBS_POS_TOPUP","ColumnValue_AMOUNT","NodeVal_to_Environment3",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent6" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelectConditionEnvVar("Table_Name_RRBS_POS_TOPUP","ColumnName_TOPUP","Condition_RRBS_POS_TOPUP","ColumnValue_TOPUP","NodeVal_to_Environment3",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent7" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelectConditionEnvVar("Table_Name_RRBS_POS_TOPUP","ColumnName_CARD_ID","Condition_RRBS_POS_TOPUP","ColumnValue_CARD_ID","NodeVal_to_Environment3",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent8" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelectConditionEnvVar("Table_Name_RRBS_POS_TOPUP","ColumnName_VAT_AMOUNT","Condition_RRBS_POS_TOPUP","ColumnValue_VAT_AMOUNT","NodeVal_to_Environment3",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent9" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelectConditionEnvVar("Table_Name_RRBS_POS_TOPUP","ColumnName_TAX_AMOUNT","Condition_RRBS_POS_TOPUP","ColumnValue_TAX_AMOUNT","NodeVal_to_Environment3",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent10" : AppComponentStatus[testEvent]= Function_Library.SQLDBEnvironmentVariableCompare("Table_Name_Tbl_bundle_payment","ColumnName_TransactionID","Condition_Tbl_bundle_payment","NodeVal_to_Environment",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent11" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_BundleCode","Condition_Tbl_bundle_payment","ColumnValue_BundleCode",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent12" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_TopupAmount","Condition_Tbl_bundle_payment","ColumnValue_TopupAmount",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent13" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_TopupMode","Condition_Tbl_bundle_payment","ColumnValue_TopupMode",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent14" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_Status","Condition_Tbl_bundle_payment","ColumnValue_Status",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent15" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_ErrorDesc","Condition_Tbl_bundle_payment","ColumnValue_ErrorDesc",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent16" : AppComponentStatus[testEvent]= Function_Library.SQLDBEnvironmentVariableCompare("Table_Name_Tbl_bundle_payment","ColumnName_redorderid","Condition_Tbl_bundle_payment","NodeVal_to_Environment2",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent17" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_modeOfPayment","Condition_Tbl_bundle_payment","ColumnValue_modeOfPayment",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent18" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_isautotopup","Condition_Tbl_bundle_payment","ColumnValue_isautotopup",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent19" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_BundleName","Condition_Tbl_bundle_payment","ColumnValue_BundleName",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent20" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_Discount","Condition_Tbl_bundle_payment","ColumnValue_Discount",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent21" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_TaxAmount","Condition_Tbl_bundle_payment","ColumnValue_TaxAmount",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent22" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_VATamount","Condition_Tbl_bundle_payment","ColumnValue_VATamount",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent23" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_VATtransid","Condition_Tbl_bundle_payment","ColumnValue_VATtransid",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent24" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_TotalAmount","Condition_Tbl_bundle_payment","ColumnValue_TotalAmount",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent25" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_BundleMonths","Condition_Tbl_bundle_payment","ColumnValue_BundleMonths",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent26" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","USERCARDDET_ISAUTOTOPUP_COLUMN_NAME","SUBSCRIBERID_CONDITION","USERCARDDET_ISAUTOTOPUP_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent27" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","NAMEONCARD_COLUMN_NAME","SUBSCRIBERID_CONDITION","NAMEONCARD_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent28" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","CARDSTARTDATE_COLUMN_NAME","SUBSCRIBERID_CONDITION","CARDSTARTDATE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent29" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","CARDEXPIRYDATE_COLUMN_NAME","SUBSCRIBERID_CONDITION","CARDEXPIRYDATE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent30" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","CARDISSUENUMBER_COLUMN_NAME","SUBSCRIBERID_CONDITION","CARDISSUENUMBER_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent31" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","HOUSENUMBER_COLUMN_NAME","SUBSCRIBERID_CONDITION","HOUSENUMBER_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent32" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","ADDRESSLINE1_COLUMN_NAME","SUBSCRIBERID_CONDITION","ADDRESSLINE1_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent33" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","CITY_COLUMN_NAME","SUBSCRIBERID_CONDITION","CITY_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent34" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","COUNTY_COLUMN_NAME","SUBSCRIBERID_CONDITION","COUNTY_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent35" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","COUNTRY_COLUMN_NAME","SUBSCRIBERID_CONDITION","COUNTRY_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent36" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","POSTCODE_COLUMN_NAME","SUBSCRIBERID_CONDITION","POSTCODE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent37" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","EMAIL_COLUMN_NAME","SUBSCRIBERID_CONDITION","EMAIL_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent38" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","NICKNAME_COLUMN_NAME","SUBSCRIBERID_CONDITION","NICKNAME_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent39" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","AUTHNET_AMOUNT_COLUMN_NAME","SUBSCRIBERID_CONDITION","AUTHNET_AMOUNT_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent40" : AppComponentStatus[testEvent]= Function_Library.EshopSQLDBEnvironmentVariableCompare("TABLE_AUTHORIZENET","DYNAMIC_TRANSID_CONDITION","SUBSCRIBERID_CONDITION","NodeVal_to_Environment",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent41" : AppComponentStatus[testEvent]= Function_Library.EshopSQLDBEnvironmentVariableCompare("TABLE_AUTHORIZENET","AUTH_INVOICENUMBER_CONDITION","SUBSCRIBERID_CONDITION","NodeVal_to_Environment2",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent42" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","ORDERDESC_COLUMN_NAME","SUBSCRIBERID_CONDITION","ORDERDESC_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent43" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","ITEMUNITPRICE_COLUMN_NAME","SUBSCRIBERID_CONDITION","ITEMUNITPRICE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent44" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","STATUS_TEXT_COLUMN_NAME","SUBSCRIBERID_CONDITION","STATUS_TEXT_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent45" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","ACCOUNTNUMBER_COLUMN_NAME","SUBSCRIBERID_CONDITION","ACCOUNTNUMBER_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent46" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","ACCOUNTTYPE_COLUMN_NAME","SUBSCRIBERID_CONDITION","ACCOUNTTYPE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent47" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","MESSAGECODE_COLUMN_NAME","SUBSCRIBERID_CONDITION","MESSAGECODE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent48" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","MESSAGEDESC_COLUMN_NAME","SUBSCRIBERID_CONDITION","MESSAGEDESC_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent49" : AppComponentStatus[testEvent]= Function_Library.EshopSQLDBEnvironmentVariableCompare("TABLE_AUTHORIZENET","AUTH_TAXID_COLUMN_NAME","SUBSCRIBERID_CONDITION","NodeVal_to_Environment",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent50" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","AUTH_VATID_COLUMN_NAME","SUBSCRIBERID_CONDITION","AUTH_VATID_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent51" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_Tbluserbundledetails","ColumnName_Bundlecode","Condition_Tbluserbundledetails","ColumnValue_Bundlecode",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent52" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_Tbluserbundledetails","ColumnName_IS_renewal","Condition_Tbluserbundledetails","ColumnValue_IS_renewal",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent53" : AppComponentStatus[testEvent]= Function_Library.RRBSDBSelectEnvvar("Table_Name_RRBS_FAMILY_MAPPING","ColumnName_FAMILY_ID","Condition_RRBS_FAMILY_MAPPING","NodeVal_to_Environment3",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent54" : AppComponentStatus[testEvent]= Function_Library.RRBSDBSelect("Table_Name_RRBS_FAMILY_MAPPING","ColumnName_PRIMARY_IND","Condition_RRBS_FAMILY_MAPPING","ColumnValue_PRIMARY_IND",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent55" : AppComponentStatus[testEvent]= Function_Library.RRBSDBSelect("Table_Name_RRBS_FAMILY_MAPPING","ColumnName_Status","Condition_RRBS_FAMILY_MAPPING","ColumnValue_Status_FamilyMapping",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;

					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}
	
	

	/**
	 * Post_Do_BundleTopup
	 * or performing Post conditions after doing Bundle Topup.
	 * @author Lakshman
	 * @since Aug 8, 2016
	 */
	public static void Post_Do_BundleTopup4() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=54;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_SUBS_Bundle_Buckets","ColumnName_Bundle_Code","Condition_RRBS_SUBS_Bundle_Buckets","ColumnValue_Bundle_Code",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent2" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_SUBS_Bundle_Buckets","ColumnName_Status","Condition_RRBS_SUBS_Bundle_Buckets","ColumnValue_Status1",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent3" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_SUBS_Bundle_Buckets","ColumnName_Charge_Mode","Condition_RRBS_SUBS_Bundle_Buckets","ColumnValue_Charge_Mode",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent4" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_SUBS_THRESH_LIMIT","ColumnName_ONNET_BUNDLE_COUNTER","Condition_RRBS_SUBS_THRESH_LIMIT","ColumnValue_ONNET_BUNDLE_COUNTER",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent5" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_POS_TOPUP","ColumnName_AMOUNT","Condition_RRBS_POS_TOPUP","ColumnValue_AMOUNT",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent6" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_POS_TOPUP","ColumnName_TOPUP","Condition_RRBS_POS_TOPUP","ColumnValue_TOPUP",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent7" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_POS_TOPUP","ColumnName_CARD_ID","Condition_RRBS_POS_TOPUP","ColumnValue_CARD_ID",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent8" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_POS_TOPUP","ColumnName_VAT_AMOUNT","Condition_RRBS_POS_TOPUP","ColumnValue_VAT_AMOUNT",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent9" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_POS_TOPUP","ColumnName_TAX_AMOUNT","Condition_RRBS_POS_TOPUP","ColumnValue_TAX_AMOUNT",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent10" : AppComponentStatus[testEvent]= Function_Library.SQLDBEnvironmentVariableCompare("Table_Name_Tbl_bundle_payment","ColumnName_TransactionID","Condition_Tbl_bundle_payment","NodeVal_to_Environment",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent11" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_BundleCode","Condition_Tbl_bundle_payment","ColumnValue_BundleCode",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent12" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_TopupAmount","Condition_Tbl_bundle_payment","ColumnValue_TopupAmount",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent13" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_TopupMode","Condition_Tbl_bundle_payment","ColumnValue_TopupMode",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent14" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_Status","Condition_Tbl_bundle_payment","ColumnValue_Status",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent15" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_ErrorDesc","Condition_Tbl_bundle_payment","ColumnValue_ErrorDesc",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent16" : AppComponentStatus[testEvent]= Function_Library.SQLDBEnvironmentVariableCompare("Table_Name_Tbl_bundle_payment","ColumnName_redorderid","Condition_Tbl_bundle_payment","NodeVal_to_Environment2",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent17" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_modeOfPayment","Condition_Tbl_bundle_payment","ColumnValue_modeOfPayment",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent18" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_isautotopup","Condition_Tbl_bundle_payment","ColumnValue_isautotopup",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent19" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_BundleName","Condition_Tbl_bundle_payment","ColumnValue_BundleName",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent20" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_Discount","Condition_Tbl_bundle_payment","ColumnValue_Discount",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent21" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_TaxAmount","Condition_Tbl_bundle_payment","ColumnValue_TaxAmount",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent22" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_VATamount","Condition_Tbl_bundle_payment","ColumnValue_VATamount",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent23" : AppComponentStatus[testEvent]= Function_Library.SQLDBEnvironmentVariableCompare("Table_Name_Tbl_bundle_payment","ColumnName_VATtransid","Condition_Tbl_bundle_payment","NodeVal_to_Environment3",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent24" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_TotalAmount","Condition_Tbl_bundle_payment","ColumnValue_TotalAmount",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent25" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_BundleMonths","Condition_Tbl_bundle_payment","ColumnValue_BundleMonths",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent26" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","USERCARDDET_ISAUTOTOPUP_COLUMN_NAME","SUBSCRIBERID_CONDITION","USERCARDDET_ISAUTOTOPUP_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent27" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","NAMEONCARD_COLUMN_NAME","SUBSCRIBERID_CONDITION","NAMEONCARD_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent28" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","CARDSTARTDATE_COLUMN_NAME","SUBSCRIBERID_CONDITION","CARDSTARTDATE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent29" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","CARDEXPIRYDATE_COLUMN_NAME","SUBSCRIBERID_CONDITION","CARDEXPIRYDATE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent30" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","CARDISSUENUMBER_COLUMN_NAME","SUBSCRIBERID_CONDITION","CARDISSUENUMBER_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent31" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","HOUSENUMBER_COLUMN_NAME","SUBSCRIBERID_CONDITION","HOUSENUMBER_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent32" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","ADDRESSLINE1_COLUMN_NAME","SUBSCRIBERID_CONDITION","ADDRESSLINE1_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent33" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","CITY_COLUMN_NAME","SUBSCRIBERID_CONDITION","CITY_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent34" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","COUNTY_COLUMN_NAME","SUBSCRIBERID_CONDITION","COUNTY_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent35" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","COUNTRY_COLUMN_NAME","SUBSCRIBERID_CONDITION","COUNTRY_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent36" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","POSTCODE_COLUMN_NAME","SUBSCRIBERID_CONDITION","POSTCODE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent37" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","EMAIL_COLUMN_NAME","SUBSCRIBERID_CONDITION","EMAIL_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent38" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","NICKNAME_COLUMN_NAME","SUBSCRIBERID_CONDITION","NICKNAME_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent39" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","AUTHNET_AMOUNT_COLUMN_NAME","SUBSCRIBERID_CONDITION","AUTHNET_AMOUNT_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent40" : AppComponentStatus[testEvent]= Function_Library.EshopSQLDBEnvironmentVariableCompare("TABLE_AUTHORIZENET","DYNAMIC_TRANSID_CONDITION","SUBSCRIBERID_CONDITION","NodeVal_to_Environment",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent41" : AppComponentStatus[testEvent]= Function_Library.EshopSQLDBEnvironmentVariableCompare("TABLE_AUTHORIZENET","AUTH_INVOICENUMBER_CONDITION","SUBSCRIBERID_CONDITION","NodeVal_to_Environment2",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent42" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","ORDERDESC_COLUMN_NAME","SUBSCRIBERID_CONDITION","ORDERDESC_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent43" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","ITEMUNITPRICE_COLUMN_NAME","SUBSCRIBERID_CONDITION","ITEMUNITPRICE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent44" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","STATUS_TEXT_COLUMN_NAME","SUBSCRIBERID_CONDITION","STATUS_TEXT_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent45" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","ACCOUNTNUMBER_COLUMN_NAME","SUBSCRIBERID_CONDITION","ACCOUNTNUMBER_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent46" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","ACCOUNTTYPE_COLUMN_NAME","SUBSCRIBERID_CONDITION","ACCOUNTTYPE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent47" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","MESSAGECODE_COLUMN_NAME","SUBSCRIBERID_CONDITION","MESSAGECODE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent48" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","MESSAGEDESC_COLUMN_NAME","SUBSCRIBERID_CONDITION","MESSAGEDESC_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent49" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","AUTH_TAXID_COLUMN_NAME","SUBSCRIBERID_CONDITION","AUTH_TAXID_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent50" : AppComponentStatus[testEvent]= Function_Library.EshopSQLDBEnvironmentVariableCompare("TABLE_AUTHORIZENET","AUTH_TAXID_COLUMN_NAME","SUBSCRIBERID_CONDITION","NodeVal_to_Environment",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent51" : AppComponentStatus[testEvent]= Function_Library.EshopSQLDBEnvironmentVariableCompare("TABLE_AUTHORIZENET","AUTH_VATID_COLUMN_NAME","SUBSCRIBERID_CONDITION","NodeVal_to_Environment3",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent52" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","AUTH_VATID_COLUMN_NAME","SUBSCRIBERID_CONDITION","AUTH_VATID_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent53" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_Tbluserbundledetails","ColumnName_Bundlecode","Condition_Tbluserbundledetails","ColumnValue_Bundlecode",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent54" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_Tbluserbundledetails","ColumnName_IS_renewal","Condition_Tbluserbundledetails","ColumnValue_IS_renewal",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;

					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}
	
	
	/**
	 * Post_Do_BundleTopup
	 * or performing Post conditions after doing Bundle Topup.
	 * @author Lakshman
	 * @since Aug 3, 2016
	 */
	public static void Post_Do_BundleTopup3() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=52;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_SUBS_Bundle_Buckets","ColumnName_Bundle_Code","Condition_RRBS_SUBS_Bundle_Buckets","ColumnValue_Bundle_Code",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent2" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_SUBS_Bundle_Buckets","ColumnName_Status","Condition_RRBS_SUBS_Bundle_Buckets","ColumnValue_Status1",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent3" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_SUBS_Bundle_Buckets","ColumnName_Charge_Mode","Condition_RRBS_SUBS_Bundle_Buckets","ColumnValue_Charge_Mode",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent4" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_SUBS_THRESH_LIMIT","ColumnName_ONNET_BUNDLE_COUNTER","Condition_RRBS_SUBS_THRESH_LIMIT","ColumnValue_ONNET_BUNDLE_COUNTER",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent5" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_POS_TOPUP","ColumnName_AMOUNT","Condition_RRBS_POS_TOPUP","ColumnValue_AMOUNT",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent6" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_POS_TOPUP","ColumnName_TOPUP","Condition_RRBS_POS_TOPUP","ColumnValue_TOPUP",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent7" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_POS_TOPUP","ColumnName_CARD_ID","Condition_RRBS_POS_TOPUP","ColumnValue_CARD_ID",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent8" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_POS_TOPUP","ColumnName_VAT_AMOUNT","Condition_RRBS_POS_TOPUP","ColumnValue_VAT_AMOUNT",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent9" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_POS_TOPUP","ColumnName_TAX_AMOUNT","Condition_RRBS_POS_TOPUP","ColumnValue_TAX_AMOUNT",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent10" : AppComponentStatus[testEvent]= Function_Library.SQLDBEnvironmentVariableCompare("Table_Name_Tbl_bundle_payment","ColumnName_TransactionID","Condition_Tbl_bundle_payment","NodeVal_to_Environment",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent11" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_BundleCode","Condition_Tbl_bundle_payment","ColumnValue_BundleCode",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent12" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_TopupAmount","Condition_Tbl_bundle_payment","ColumnValue_TopupAmount",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent13" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_TopupMode","Condition_Tbl_bundle_payment","ColumnValue_TopupMode",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent14" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_Status","Condition_Tbl_bundle_payment","ColumnValue_Status",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent15" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_ErrorDesc","Condition_Tbl_bundle_payment","ColumnValue_ErrorDesc",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent16" : AppComponentStatus[testEvent]= Function_Library.SQLDBEnvironmentVariableCompare("Table_Name_Tbl_bundle_payment","ColumnName_redorderid","Condition_Tbl_bundle_payment","NodeVal_to_Environment2",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent17" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_modeOfPayment","Condition_Tbl_bundle_payment","ColumnValue_modeOfPayment",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent18" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_isautotopup","Condition_Tbl_bundle_payment","ColumnValue_isautotopup",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent19" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_BundleName","Condition_Tbl_bundle_payment","ColumnValue_BundleName",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent20" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_Discount","Condition_Tbl_bundle_payment","ColumnValue_Discount",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent21" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_TaxAmount","Condition_Tbl_bundle_payment","ColumnValue_TaxAmount",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent22" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_VATamount","Condition_Tbl_bundle_payment","ColumnValue_VATamount",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent23" : AppComponentStatus[testEvent]= Function_Library.SQLDBEnvironmentVariableCompare("Table_Name_Tbl_bundle_payment","ColumnName_VATtransid","Condition_Tbl_bundle_payment","NodeVal_to_Environment3",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent24" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_TotalAmount","Condition_Tbl_bundle_payment","ColumnValue_TotalAmount",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent25" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_BundleMonths","Condition_Tbl_bundle_payment","ColumnValue_BundleMonths",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent26" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","USERCARDDET_ISAUTOTOPUP_COLUMN_NAME","SUBSCRIBERID_CONDITION","USERCARDDET_ISAUTOTOPUP_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent27" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","NAMEONCARD_COLUMN_NAME","SUBSCRIBERID_CONDITION","NAMEONCARD_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent28" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","CARDSTARTDATE_COLUMN_NAME","SUBSCRIBERID_CONDITION","CARDSTARTDATE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent29" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","CARDEXPIRYDATE_COLUMN_NAME","SUBSCRIBERID_CONDITION","CARDEXPIRYDATE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent30" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","CARDISSUENUMBER_COLUMN_NAME","SUBSCRIBERID_CONDITION","CARDISSUENUMBER_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent31" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","HOUSENUMBER_COLUMN_NAME","SUBSCRIBERID_CONDITION","HOUSENUMBER_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent32" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","ADDRESSLINE1_COLUMN_NAME","SUBSCRIBERID_CONDITION","ADDRESSLINE1_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent33" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","CITY_COLUMN_NAME","SUBSCRIBERID_CONDITION","CITY_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent34" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","COUNTY_COLUMN_NAME","SUBSCRIBERID_CONDITION","COUNTY_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent35" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","COUNTRY_COLUMN_NAME","SUBSCRIBERID_CONDITION","COUNTRY_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent36" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","POSTCODE_COLUMN_NAME","SUBSCRIBERID_CONDITION","POSTCODE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent37" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","EMAIL_COLUMN_NAME","SUBSCRIBERID_CONDITION","EMAIL_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent38" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","NICKNAME_COLUMN_NAME","SUBSCRIBERID_CONDITION","NICKNAME_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent39" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","AUTHNET_AMOUNT_COLUMN_NAME","SUBSCRIBERID_CONDITION","AUTHNET_AMOUNT_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent40" : AppComponentStatus[testEvent]= Function_Library.EshopSQLDBEnvironmentVariableCompare("TABLE_AUTHORIZENET","DYNAMIC_TRANSID_CONDITION","SUBSCRIBERID_CONDITION","NodeVal_to_Environment",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent41" : AppComponentStatus[testEvent]= Function_Library.EshopSQLDBEnvironmentVariableCompare("TABLE_AUTHORIZENET","AUTH_INVOICENUMBER_CONDITION","SUBSCRIBERID_CONDITION","NodeVal_to_Environment2",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent42" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","ORDERDESC_COLUMN_NAME","SUBSCRIBERID_CONDITION","ORDERDESC_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent43" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","ITEMUNITPRICE_COLUMN_NAME","SUBSCRIBERID_CONDITION","ITEMUNITPRICE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent44" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","STATUS_TEXT_COLUMN_NAME","SUBSCRIBERID_CONDITION","STATUS_TEXT_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent45" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","ACCOUNTNUMBER_COLUMN_NAME","SUBSCRIBERID_CONDITION","ACCOUNTNUMBER_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent46" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","ACCOUNTTYPE_COLUMN_NAME","SUBSCRIBERID_CONDITION","ACCOUNTTYPE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent47" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","MESSAGECODE_COLUMN_NAME","SUBSCRIBERID_CONDITION","MESSAGECODE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent48" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","MESSAGEDESC_COLUMN_NAME","SUBSCRIBERID_CONDITION","MESSAGEDESC_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent49" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","AUTH_TAXID_COLUMN_NAME","SUBSCRIBERID_CONDITION","AUTH_TAXID_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent50" : AppComponentStatus[testEvent]= Function_Library.EshopSQLDBEnvironmentVariableCompare("TABLE_AUTHORIZENET","AUTH_VATID_COLUMN_NAME","SUBSCRIBERID_CONDITION","NodeVal_to_Environment3",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent51" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_Tbluserbundledetails","ColumnName_Bundlecode","Condition_Tbluserbundledetails","ColumnValue_Bundlecode",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent52" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_Tbluserbundledetails","ColumnName_IS_renewal","Condition_Tbluserbundledetails","ColumnValue_IS_renewal",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;

					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}
	
	/**
	 * Post_Do_BundleTopup_Sucess
	 * or performing Post conditions after doing Bundle Topup.
	 * @author Lakshman
	 * @since Aug 8, 2016
	 */
	public static void Post_Do_BundleTopup_Sucess() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=52;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_SUBS_Bundle_Buckets","ColumnName_Bundle_Code","Condition_RRBS_SUBS_Bundle_Buckets","ColumnValue_Bundle_Code",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent2" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_SUBS_Bundle_Buckets","ColumnName_Status","Condition_RRBS_SUBS_Bundle_Buckets","ColumnValue_Status1",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent3" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_SUBS_Bundle_Buckets","ColumnName_Charge_Mode","Condition_RRBS_SUBS_Bundle_Buckets","ColumnValue_Charge_Mode",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent4" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_SUBS_THRESH_LIMIT","ColumnName_ONNET_BUNDLE_COUNTER","Condition_RRBS_SUBS_THRESH_LIMIT","ColumnValue_ONNET_BUNDLE_COUNTER",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent5" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_POS_TOPUP","ColumnName_AMOUNT","Condition_RRBS_POS_TOPUP","ColumnValue_AMOUNT",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent6" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_POS_TOPUP","ColumnName_TOPUP","Condition_RRBS_POS_TOPUP","ColumnValue_TOPUP",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent7" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_POS_TOPUP","ColumnName_CARD_ID","Condition_RRBS_POS_TOPUP","ColumnValue_CARD_ID",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent8" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_POS_TOPUP","ColumnName_VAT_AMOUNT","Condition_RRBS_POS_TOPUP","ColumnValue_VAT_AMOUNT",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent9" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_POS_TOPUP","ColumnName_TAX_AMOUNT","Condition_RRBS_POS_TOPUP","ColumnValue_TAX_AMOUNT",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent10" : AppComponentStatus[testEvent]= Function_Library.SQLDBEnvironmentVariableCompare("Table_Name_Tbl_bundle_payment","ColumnName_TransactionID","Condition_Tbl_bundle_payment","NodeVal_to_Environment",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent11" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_BundleCode","Condition_Tbl_bundle_payment","ColumnValue_BundleCode",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent12" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_TopupAmount","Condition_Tbl_bundle_payment","ColumnValue_TopupAmount",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent13" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_TopupMode","Condition_Tbl_bundle_payment","ColumnValue_TopupMode",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent14" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_Status","Condition_Tbl_bundle_payment","ColumnValue_Status",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent15" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_ErrorDesc","Condition_Tbl_bundle_payment","ColumnValue_ErrorDesc",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent16" : AppComponentStatus[testEvent]= Function_Library.SQLDBEnvironmentVariableCompare("Table_Name_Tbl_bundle_payment","ColumnName_redorderid","Condition_Tbl_bundle_payment","NodeVal_to_Environment2",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent17" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_modeOfPayment","Condition_Tbl_bundle_payment","ColumnValue_modeOfPayment",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent18" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_isautotopup","Condition_Tbl_bundle_payment","ColumnValue_isautotopup",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent19" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_BundleName","Condition_Tbl_bundle_payment","ColumnValue_BundleName",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent20" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_Discount","Condition_Tbl_bundle_payment","ColumnValue_Discount",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent21" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_TaxAmount","Condition_Tbl_bundle_payment","ColumnValue_TaxAmount",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent22" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_VATamount","Condition_Tbl_bundle_payment","ColumnValue_VATamount",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent23" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_VATtransid","Condition_Tbl_bundle_payment","ColumnValue_VATtransid",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent24" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_TotalAmount","Condition_Tbl_bundle_payment","ColumnValue_TotalAmount",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent25" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("Table_Name_Tbl_bundle_payment","ColumnName_BundleMonths","Condition_Tbl_bundle_payment","ColumnValue_BundleMonths",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent26" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","USERCARDDET_ISAUTOTOPUP_COLUMN_NAME","SUBSCRIBERID_CONDITION","USERCARDDET_ISAUTOTOPUP_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent27" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","NAMEONCARD_COLUMN_NAME","SUBSCRIBERID_CONDITION","NAMEONCARD_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent28" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","CARDSTARTDATE_COLUMN_NAME","SUBSCRIBERID_CONDITION","CARDSTARTDATE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent29" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","CARDEXPIRYDATE_COLUMN_NAME","SUBSCRIBERID_CONDITION","CARDEXPIRYDATE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent30" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","CARDISSUENUMBER_COLUMN_NAME","SUBSCRIBERID_CONDITION","CARDISSUENUMBER_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent31" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","HOUSENUMBER_COLUMN_NAME","SUBSCRIBERID_CONDITION","HOUSENUMBER_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent32" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","ADDRESSLINE1_COLUMN_NAME","SUBSCRIBERID_CONDITION","ADDRESSLINE1_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent33" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","CITY_COLUMN_NAME","SUBSCRIBERID_CONDITION","CITY_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent34" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","COUNTY_COLUMN_NAME","SUBSCRIBERID_CONDITION","COUNTY_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent35" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","COUNTRY_COLUMN_NAME","SUBSCRIBERID_CONDITION","COUNTRY_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent36" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","POSTCODE_COLUMN_NAME","SUBSCRIBERID_CONDITION","POSTCODE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent37" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","EMAIL_COLUMN_NAME","SUBSCRIBERID_CONDITION","EMAIL_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent38" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","NICKNAME_COLUMN_NAME","SUBSCRIBERID_CONDITION","NICKNAME_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent39" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","AUTHNET_AMOUNT_COLUMN_NAME","SUBSCRIBERID_CONDITION","AUTHNET_AMOUNT_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent40" : AppComponentStatus[testEvent]= Function_Library.EshopSQLDBEnvironmentVariableCompare("TABLE_AUTHORIZENET","DYNAMIC_TRANSID_CONDITION","SUBSCRIBERID_CONDITION","NodeVal_to_Environment",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent41" : AppComponentStatus[testEvent]= Function_Library.EshopSQLDBEnvironmentVariableCompare("TABLE_AUTHORIZENET","AUTH_INVOICENUMBER_CONDITION","SUBSCRIBERID_CONDITION","NodeVal_to_Environment2",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent42" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","ORDERDESC_COLUMN_NAME","SUBSCRIBERID_CONDITION","ORDERDESC_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent43" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","ITEMUNITPRICE_COLUMN_NAME","SUBSCRIBERID_CONDITION","ITEMUNITPRICE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent44" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","STATUS_TEXT_COLUMN_NAME","SUBSCRIBERID_CONDITION","STATUS_TEXT_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent45" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","ACCOUNTNUMBER_COLUMN_NAME","SUBSCRIBERID_CONDITION","ACCOUNTNUMBER_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent46" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","ACCOUNTTYPE_COLUMN_NAME","SUBSCRIBERID_CONDITION","ACCOUNTTYPE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent47" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","MESSAGECODE_COLUMN_NAME","SUBSCRIBERID_CONDITION","MESSAGECODE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent48" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","MESSAGEDESC_COLUMN_NAME","SUBSCRIBERID_CONDITION","MESSAGEDESC_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent49" : AppComponentStatus[testEvent]= Function_Library.EshopSQLDBEnvironmentVariableCompare("TABLE_AUTHORIZENET","AUTH_TAXID_COLUMN_NAME","SUBSCRIBERID_CONDITION","NodeVal_to_Environment",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent50" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","AUTH_VATID_COLUMN_NAME","SUBSCRIBERID_CONDITION","AUTH_VATID_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent51" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_Tbluserbundledetails","ColumnName_Bundlecode","Condition_Tbluserbundledetails","ColumnValue_Bundlecode",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent52" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_Tbluserbundledetails","ColumnName_IS_renewal","Condition_Tbluserbundledetails","ColumnValue_IS_renewal",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;

					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	/**
	 * Post_Do_BundleTopup2
	 * or performing Post conditions after doing Bundle Topup.
	 * @author Lakshman
	 * @since Aug 5, 2016
	 */
	public static void Post_Do_BundleTopup2() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=9;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_SUBS_Bundle_Buckets","ColumnName_Bundle_Code","Condition_RRBS_SUBS_Bundle_Buckets","ColumnValue_Bundle_Code",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent2" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_SUBS_Bundle_Buckets","ColumnName_Status","Condition_RRBS_SUBS_Bundle_Buckets","ColumnValue_Status",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent3" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_SUBS_Bundle_Buckets","ColumnName_Charge_Mode","Condition_RRBS_SUBS_Bundle_Buckets","ColumnValue_Charge_Mode",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent4" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_SUBS_THRESH_LIMIT","ColumnName_ONNET_BUNDLE_COUNTER","Condition_RRBS_SUBS_THRESH_LIMIT","ColumnValue_ONNET_BUNDLE_COUNTER",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent5" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_POS_TOPUP","ColumnName_AMOUNT","Condition_RRBS_POS_TOPUP","ColumnValue_AMOUNT",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent6" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_POS_TOPUP","ColumnName_TOPUP","Condition_RRBS_POS_TOPUP","ColumnValue_TOPUP",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent7" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_POS_TOPUP","ColumnName_CARD_ID","Condition_RRBS_POS_TOPUP","ColumnValue_CARD_ID",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent8" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_POS_TOPUP","ColumnName_VAT_AMOUNT","Condition_RRBS_POS_TOPUP","ColumnValue_VAT_AMOUNT",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent9" : AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("Table_Name_RRBS_POS_TOPUP","ColumnName_TAX_AMOUNT","Condition_RRBS_POS_TOPUP","ColumnValue_TAX_AMOUNT",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;

					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	/**
	 * Pre_Do_BundleTopup
	 * For Performing Preconditions for doing a Bundle Topup 
	 * @author Lakshman A
	 * @since Aug 2, 2016
	 */
	public static void Pre_Do_BundleTopup() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=10;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1" : AppComponentStatus[testEvent] = Function_Library.RRBSDBUpdate("TABLE_RRBS_TOPUP_THRESHLIMIT","PLAN_CHANGER_COLUMN_NAME","PLAN_CHANGER_COLUMN_VALUE","RRBS_UPDATE_CONDITION_1",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent2" : AppComponentStatus[testEvent] = Function_Library.RRBSDBUpdate("TABLE_RRBS_SUBS_SERVICES","BUNDLE_TOPUP_COLUMN_NAME","BUNDLE_TOPUP_COLUMN_VALUE","RRBS_UPDATE_CONDITION_2",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent3" : AppComponentStatus[testEvent] = Function_Library.RRBSDBUpdate("TABLE_RRBS_SUBSCRIBER_PROFILE","SERVICE_CONTROL_COLUMN_NAME","SERVICE_CONTROL_COLUMN_VALUE","RRBS_UPDATE_CONDITION_3",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent4" : AppComponentStatus[testEvent] = Function_Library.RRBSDBDelete("TABLE_RRBS_SUBS_BUNDLE_BUCKETS","RRBS_DELETE_CONDITION",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent5" : AppComponentStatus[testEvent] = Function_Library.RRBSDBDelete("TABLE_RRBS_SUBS_THRESH_LIMIT","RRBS_DELETE_CONDITION",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent6" : AppComponentStatus[testEvent] = Function_Library.RRBSDBDelete("TABLE_RRBS_POS_TOPUP","RRBS_DELETE_CONDITION",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent7" : AppComponentStatus[testEvent] = Function_Library.SQLDBDelete("Table_Name","Condition_1",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent8" : AppComponentStatus[testEvent] = Function_Library.EshopSQLDBDelete("Table_Name_1","Condition_2",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent9" : AppComponentStatus[testEvent] = Function_Library.EshopSQLDBDelete("Table_Name_2","Condition_2",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent10" : AppComponentStatus[testEvent] = Function_Library.EshopSQLDBDelete("Table_Name_3","Condition_2",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent11" : AppComponentStatus[testEvent]= Function_Library.EshopSQLDBDelete("Table_Name_4","Condition_2",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;

					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}
	
	/**
	 * Pre_Do_BundleTopup2
	 * For Performing Preconditions for doing a Bundle Topup 
	 * @author Lakshman A
	 * @since Aug 8, 2016
	 */
	public static void Pre_Do_BundleTopup2() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=9;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					
					case "ACEvent1" : AppComponentStatus[testEvent] = Function_Library.RetrieveRRBSValueStoresInEnvVar("Table_Name_Family_Mapping","Column_Name_Family_ID","Condition_Family_Mapping","Env_FamilyID",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent2" : AppComponentStatus[testEvent] = Function_Library.RRBSDBDelete("Table_Name_Family_Mapping","Condition_Family_Mapping",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent3" : AppComponentStatus[testEvent] = Function_Library.RRBSDBDeleteConditionEnvVar("Table_Name_Thresh_Limit","Condition_Name","Env_FamilyID",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent4" : AppComponentStatus[testEvent] = Function_Library.RRBSDBDeleteConditionEnvVar("Table_Name_SUBS_BUNDLE_BUCKETS","Condition_Name","Env_FamilyID",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent5" : AppComponentStatus[testEvent] = Function_Library.RRBSDBDeleteConditionEnvVar("Table_Name_SUBS_Services","Condition_Name","Env_FamilyID",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent6" : AppComponentStatus[testEvent] = Function_Library.RRBSDBDeleteConditionEnvVar("Table_Name_SUBS_PROFILE","Condition_Name","Env_FamilyID",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent7" : AppComponentStatus[testEvent] = Function_Library.RRBSDBDeleteConditionEnvVar("Table_Name_RRBS_POS_TOPUP","Condition_Name","Env_FamilyID",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent8" : AppComponentStatus[testEvent] = Function_Library.RRBSDBUpdate("Table_Name_SUBS_PROFILE","ACCT_BALANCE_COLUMN_NAME","ACCT_BALANCE_COLUMN_VALUE","RRBS_UPDATE_CONDITION",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent9" : AppComponentStatus[testEvent] = Function_Library.RRBSDBUpdate("Table_Name_SUBS_PROFILE","SERVICE_CONTROL_COLUMN_NAME","SERVICE_CONTROL_COLUMN_VALUE","RRBS_UPDATE_CONDITION",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;

					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	/**
	 * @author <b>LAKSHMAN A</b>
	 * @throws Exception
	 */
	public static void Post_OnlineTopup_TAX() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=38;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1" : AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_MSTCUSTOMER","ISAUTOTOPUP_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","MSTCUSTOMER_ISAUTOTOPUP_EXPECTED_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent2" : AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_MSTTOPUP","ISAUTOTOPUP_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","MSTTOPUP_ISAUTOTOPUP_EXPECTED_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent3" : AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_MSTTOPUP","MSISDN_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","MSISDN_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent4" : AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_MSTTOPUP","TOPUPAMOUNT_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","TOPUPAMOUNT_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent5" : AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_MSTTOPUP","CARDID_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","CARDID_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent6" : AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_MSTTOPUP","TOPUPMODE_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","TOPUPMODE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent7" : AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_MSTTOPUP","STATUS_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","STATUS_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent8" : AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_MSTTOPUP","ERRORDESC_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","ERRORDESC_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent9" : AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_MSTTOPUP","MSISDN_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","MSISDN_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent10" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("TBL_MSTTOPUP","TAXAMT_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","TAXAMT_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent11" : AppComponentStatus[testEvent]= Function_Library.SQLDBSelect("TBL_MSTTOPUP","VATAMT_COLUMN_NAME","MSTCUSTOMER_SELECT_CONDITION","VATAMT_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent12" : AppComponentStatus[testEvent]= Function_Library.SQLDBEnvironmentVariableCompare("TBL_MSTTOPUP","DYNAMIC_TRANSACTIONID_CONDITION","MSTCUSTOMER_SELECT_CONDITION","NodeVal_to_Environment",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent13" : AppComponentStatus[testEvent]= Function_Library.SQLDBEnvironmentVariableCompare("TBL_MSTTOPUP","DYNAMIC_REDORDERID_CONDITION","MSTCUSTOMER_SELECT_CONDITION","NodeVal_to_Environment2",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent14" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","USERCARDDET_ISAUTOTOPUP_COLUMN_NAME","SUBSCRIBERID_CONDITION","USERCARDDET_ISAUTOTOPUP_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent15" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","NAMEONCARD_COLUMN_NAME","SUBSCRIBERID_CONDITION","NAMEONCARD_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent16" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","CARDSTARTDATE_COLUMN_NAME","SUBSCRIBERID_CONDITION","CARDSTARTDATE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent17" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","CARDEXPIRYDATE_COLUMN_NAME","SUBSCRIBERID_CONDITION","CARDEXPIRYDATE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent18" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","CARDISSUENUMBER_COLUMN_NAME","SUBSCRIBERID_CONDITION","CARDISSUENUMBER_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent19" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","HOUSENUMBER_COLUMN_NAME","SUBSCRIBERID_CONDITION","HOUSENUMBER_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent20" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","ADDRESSLINE1_COLUMN_NAME","SUBSCRIBERID_CONDITION","ADDRESSLINE1_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent21" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","CITY_COLUMN_NAME","SUBSCRIBERID_CONDITION","CITY_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent22" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","COUNTY_COLUMN_NAME","SUBSCRIBERID_CONDITION","COUNTY_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent23" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","COUNTRY_COLUMN_NAME","SUBSCRIBERID_CONDITION","COUNTRY_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent24" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","POSTCODE_COLUMN_NAME","SUBSCRIBERID_CONDITION","POSTCODE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent25" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","EMAIL_COLUMN_NAME","SUBSCRIBERID_CONDITION","EMAIL_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent26" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_USERCARDDETAILS","NICKNAME_COLUMN_NAME","SUBSCRIBERID_CONDITION","NICKNAME_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent27" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","AUTHNET_AMOUNT_COLUMN_NAME","SUBSCRIBERID_CONDITION","AUTHNET_AMOUNT_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent28" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","ORDERDESC_COLUMN_NAME","SUBSCRIBERID_CONDITION","ORDERDESC_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent29" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","ITEMUNITPRICE_COLUMN_NAME","SUBSCRIBERID_CONDITION","ITEMUNITPRICE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent30" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","STATUS_TEXT_COLUMN_NAME","SUBSCRIBERID_CONDITION","STATUS_TEXT_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent31" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","ACCOUNTNUMBER_COLUMN_NAME","SUBSCRIBERID_CONDITION","ACCOUNTNUMBER_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent32" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","ACCOUNTTYPE_COLUMN_NAME","SUBSCRIBERID_CONDITION","ACCOUNTTYPE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent33" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","MESSAGECODE_COLUMN_NAME","SUBSCRIBERID_CONDITION","MESSAGECODE_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent34" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","MESSAGEDESC_COLUMN_NAME","SUBSCRIBERID_CONDITION","MESSAGEDESC_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent35" : AppComponentStatus[testEvent]= Function_Library.EshopSQLDBEnvironmentVariableCompare("TABLE_AUTHORIZENET","AUTH_TAXID_COLUMN_NAME","SUBSCRIBERID_CONDITION","NodeVal_to_Environment",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent36" : AppComponentStatus[testEvent]= Function_Library.EShopSQLDBSelect("TABLE_AUTHORIZENET","SUBSCRIBERID_COLUMN_NAME","SUBSCRIBERID_CONDITION","SUBSCRIBERID_COLUMN_VALUE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent37" : AppComponentStatus[testEvent]= Function_Library.EshopSQLDBEnvironmentVariableCompare("TABLE_AUTHORIZENET","DYNAMIC_TRANSID_CONDITION","SUBSCRIBERID_CONDITION","NodeVal_to_Environment",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent38" : AppComponentStatus[testEvent]= Function_Library.EshopSQLDBEnvironmentVariableCompare("TABLE_AUTHORIZENET","AUTH_INVOICENUMBER_CONDITION","SUBSCRIBERID_CONDITION","NodeVal_to_Environment2",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	/**
	 * @author <b>Muralimohan M</b>
	 * @see Open SQL Connection for ESP
	 * @throws Exception
	 */
	public static void Open_ESP_SQL_Connection() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=1;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.SQLDBOpenConnection(Param.getProperty("SQL_Server2"), Param.getProperty("SPAIN_DB"), Param.getProperty("SQL_Server_UID2"), Param.getProperty("SQL_Server_PWD2"));
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	public static void SQLDB_Select() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=1;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1" : AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("Table_Name","Column_Name","Condition","Column_Value",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	public static void Open_Command_Prompt1() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=1;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.jsh_Unix_Open_Connection();
					break;

					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	public static void SQLDB_Update() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=1;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.SQLDBUpdate("TABLE_NAME","COLUMN_NAME","COLUMN_VALUE","CONDITION",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;


					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	public static void Open_Germany_SQL_Connection() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=1;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.SQLDBOpenConnection(Param.getProperty("SQL_Server2"), Param.getProperty("GERMANY_DB"), Param.getProperty("SQL_Server_UID2"), Param.getProperty("SQL_Server_PWD2"));
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;


					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	public static void SQLDB_FutureDateUpdate() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=1;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.SQLDBFutureDateUpdate("TABLE_NAME","COLUMN_NAME","NO_OF_DAYS","CONDITION",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;

					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	public static void Open_AUT_SQL_Connection() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=1;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.SQLDBOpenConnection(Param.getProperty("SQL_Server"), Param.getProperty("AUT_DB"), Param.getProperty("SQL_Server_UID"), Param.getProperty("SQL_Server_PWD"));
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	public static void EShopSQLDB_Select_RegExp() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=1;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.EShopSQLDBSelect("Table_Name","Column_Name","Condition","Column_Value",2);
					break;

					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	public static void EShopSQLDB_Update() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=1;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.EshopSQLDBUpdate("TABLE_NAME","COLUMN_NAME","COLUMN_VALUE","CONDITION",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;


					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	public static void Insert_RRBS() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=1;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.RRBSDBInsert("TABLE_NAME_RRBS_INSERT","COLUMN_NAMES_RRBS_INSERT","COLUMN_VALUES_RRBS_INSERT",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	public static void Delete_RRBS() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=1;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.RRBSDBDelete("TABLE_NAME_RRBS_DELETE","CONDITION_RRBS_DELETE",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}

	/**
	 *@author mura2261
	 * */
	public static void Precond_Cancel_Bundle() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=2;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {

					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.SQLDBDelete("TABLE_NAME","DELETE_CONDITION", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );
					break;

					case "ACEvent2": AppComponentStatus[testEvent] = Function_Library.RRBSDBUpdate("Table_Name_RRBS","COLUMN_NAME_RRBS","COLUMN_VALUE_RRBS","RRBS_CONDITION", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );
					break;
					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}
	/**
	 *@author mura2261
	 * */
	public static void Precond_AddUserPayment() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=12;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.EshopSQLDBUpdate("TBL_FEATUREGATEWAYMAPPING","PRIMARYGATEWAYVENDOR_COLUMNNAME","PRIMARYGATEWAYVENDOR_COLUMNVALUE","ESHOPSQLDB_UPDATE_CONDITION",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent2": AppComponentStatus[testEvent] = Function_Library.EshopSQLDBDelete("TBL_AUTHORIZENETLOG","ESHOPSQLDB_DELETE_CONDITION",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					case "ACEvent3": AppComponentStatus[testEvent] = Function_Library.SQLDBDelete("TBL_FAMILYPLANTRANSACTION","SQLDB_DELETE_CONDITION",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );
					break;
					case "ACEvent4": AppComponentStatus[testEvent] = Function_Library.RRBSDBUpdate("TBL_RRBS_SUBSCRIBER_PROFILE", "COLUMN_NAME_ACCT_BALANCE", "COLUMN_VALUE_ACCT_BALANCE", "RRBS_UPDATE_CONDITION_ACCID", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );
					break;
					case "ACEvent5": AppComponentStatus[testEvent] = Function_Library.RRBSDBUpdate("TBL_RRBS_SUBSCRIBER_PROFILE", "COLUMN_NAME_TOPUP_FACE_VALUE", "COLUMN_VALUE_TOPUP_FACE_VALUE", "RRBS_UPDATE_CONDITION_ACCID", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );
					break;
					case "ACEvent6": AppComponentStatus[testEvent] = Function_Library.RRBSDBUpdate("TBL_RRBS_SUBSCRIBER_PROFILE", "COLUMN_NAME_ACCT_BALANCE", "COLUMN_VALUE_ACCT_BALANCE", "RRBS_UPDATE_CONDITION_MSISDN", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );
					break;
					case "ACEvent7": AppComponentStatus[testEvent] = Function_Library.RRBSDBUpdate("TBL_RRBS_SUBSCRIBER_PROFILE", "COLUMN_NAME_TOPUP_FACE_VALUE", "COLUMN_VALUE_TOPUP_FACE_VALUE", "RRBS_UPDATE_CONDITION_MSISDN", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );
					break;
					case "ACEvent8": AppComponentStatus[testEvent] = Function_Library.RRBSDBUpdate("TBL_RRBS_SUBSCRIBER_PROFILE", "COLUMNNAME_SERVICE_CONTROL", "COLUMN_VALUE_SERVICE_CONTROL", "RRBS_UPDATE_CONDITION_ACCID", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );
					break;
					case "ACEvent9": AppComponentStatus[testEvent] = Function_Library.RRBSDBUpdate("TBL_RRBS_SUBSCRIBER_PROFILE", "COLUMNNAME_SERVICE_CONTROL", "COLUMN_VALUE_SERVICE_CONTROL", "RRBS_UPDATE_CONDITION_MSISDN", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );
					break;
					case "ACEvent10": AppComponentStatus[testEvent] = Function_Library.RRBSDBDelete("TBL_RRBS_FAMILY_MAPPING","RRBS_UPDATE_CONDITION_ACCOUNT_ID", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );
					break;
					case "ACEvent11": AppComponentStatus[testEvent] = Function_Library.EshopSQLDBDelete("TBL_USERBUNDLE_DETAILS","DELETE_USERBUNDLE_CONDITION", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );
					break;
					case "ACEvent12": AppComponentStatus[testEvent] = Function_Library.EshopSQLDBDelete("TBL_USERCARD_DETAILS","ESHOPSQLDB_DELETE_CONDITION", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );
					break;
					
					
					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}
	/**
	 *@author mura2261
	 * */
	public static void Postcond_AddUserPayment() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=42;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_FAMILY_MAPPING", "COLUMNNAME_Family_ID", "CONDITION_PARENT_ACCOUNT_ID", "COLUMNVALUE_Family_ID", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;

					case "ACEvent2": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_FAMILY_MAPPING", "COLUMNNAME_PRIMARY_IND", "CONDITION_PARENT_ACCOUNT_ID", "COLUMNVALUE_PRIMARY_IND", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent3": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_FAMILY_MAPPING", "COLUMNNAME_STATUS", "CONDITION_PARENT_ACCOUNT_ID", "COLUMNVALUE_STATUS", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;

					case "ACEvent4": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_FAMILY_MAPPING", "COLUMNNAME_Family_ID", "CONDITION_CHILD_ACCOUNT_ID", "COLUMNVALUE_Family_ID", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent5": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_FAMILY_MAPPING", "COLUMNNAME_PRIMARY_IND", "CONDITION_CHILD_ACCOUNT_ID", "CHILD_PRIMARY_ID", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;

					case "ACEvent6": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_FAMILY_MAPPING", "COLUMNNAME_STATUS", "CONDITION_CHILD_ACCOUNT_ID", "COLUMNVALUE_STATUS", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent7": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_SUBSCRIBER_PROFILE", "COLUMNNAME_ACCTBALANCE", "CONDITION_FAMILYID", "FAMILYACCOUNT_ACCTBALANCE_VALUE", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent8": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_SUBSCRIBER_PROFILE", "COLUMNNAME_TOPUP_FACE_VALUE", "CONDITION_FAMILYID", "FAMILYACCT_TOPUP_FACE_VALUE", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent9": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_SUBSCRIBER_PROFILE", "COLUMNAME_SERVICE_CONTROL", "CONDITION_FAMILYID", "FAMILYID_SERVICE_CONTROL_VALUE", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent10": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_SUBSCRIBER_PROFILE", "COLUMNNAME_ACCTBALANCE", "RRBS_PARENT_CONDITION", "PARENT_ACCTBALANCE", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent11": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_SUBSCRIBER_PROFILE", "COLUMNNAME_TOPUP_FACE_VALUE", "RRBS_PARENT_CONDITION", "PARENT_TOPUP_FACE_VALUE", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent12": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_SUBSCRIBER_PROFILE", "COLUMNAME_SERVICE_CONTROL", "RRBS_PARENT_CONDITION", "PARENT_SERVICE_CONTROL_VALUE", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent13": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_SUBSCRIBER_PROFILE", "COLUMNNAME_ACCTBALANCE", "RRBS_CHILD_CONDITION", "CHILD_ACCT_BALANCE", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent14": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_SUBSCRIBER_PROFILE", "COLUMNNAME_TOPUP_FACE_VALUE", "RRBS_CHILD_CONDITION", "CHILD_TOPUP_FACE_VALUE", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent15": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_SUBSCRIBER_PROFILE", "COLUMNAME_SERVICE_CONTROL", "RRBS_CHILD_CONDITION", "CHILD_SERVICE_CONTROL_VALUE", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent16": AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_tblFamilyplantransaction", "COLUMNAME_CHILDMSISDN", "MVNO_MSISDN_CONDITION", "COLUMNVALUE_CHILDMSISDN", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent17": AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_tblFamilyplantransaction", "COLUMNNAME_CardID", "MVNO_MSISDN_CONDITION", "COLUMNVALUE_CARDID", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent18": AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_tblFamilyplantransaction", "COLUMNNAME_TopupAmount", "MVNO_MSISDN_CONDITION", "COLUMNVALUE_TopupAmount", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent19": AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_tblFamilyplantransaction", "COLUMNNAME_TopupMode", "MVNO_MSISDN_CONDITION", "COLUMNVALUE_TopupMode", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent20": AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_tblFamilyplantransaction", "COLUMNSTATUS_status1", "MVNO_MSISDN_CONDITION", "COLUMNSTATUS_status1_value", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent21": AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_tblFamilyplantransaction", "COLUMNNAME_ErrorDesc", "MVNO_MSISDN_CONDITION", "COLUMNVALUE_ErrorDesc", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent22": AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_tblFamilyplantransaction", "COLUMNNAME_FamilyAccID", "MVNO_MSISDN_CONDITION", "COLUMNVALUE_FamilyAccID", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent23": AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_tblFamilyplantransaction", "COLUMNNAME_TaxAmount", "MVNO_MSISDN_CONDITION", "COLUMNVALUE_TaxAmount", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent24": AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_tblFamilyplantransaction", "COLUMNNAME_VATtransid", "MVNO_MSISDN_CONDITION", "COLUMNVALUE_VATtransid", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent25": AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_tblFamilyplantransaction", "COLUMNNAME_VATamount", "MVNO_MSISDN_CONDITION", "COLUMNVALUE_VATamount", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent26": AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_tblFamilyplantransaction", "COLUMNNAME_VATperc", "MVNO_MSISDN_CONDITION", "COLUMNVALUE_VATperc", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent27": AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_tblFamilyplantransaction", "COLUMNNAME_TotalAmount", "MVNO_MSISDN_CONDITION", "COLUMNVALUE_TotalAmount", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent28": AppComponentStatus[testEvent] = Function_Library.SQLDBEnvironmentVariableCompare("TBL_tblFamilyplantransaction", "COLUMNNAME_TRANSACTIONID", "MVNO_MSISDN_CONDITION", "NodeVal_to_Environment", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent29": AppComponentStatus[testEvent] = Function_Library.EShopSQLDBSelect("TBL_tbl_AuthorizenetLog", "COLUMNNAME_TransactionType", "subscriberid_CONDITION", "COLUMNVALUE_TransactionType", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent30": AppComponentStatus[testEvent] = Function_Library.EshopSQLDBEnvironmentVariableCompare("TBL_tbl_AuthorizenetLog", "COLUMNNAME_transId", "subscriberid_CONDITION", "NodeVal_to_Environment", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent31": AppComponentStatus[testEvent] = Function_Library.EShopSQLDBSelect("TBL_tbl_AuthorizenetLog", "COLUMNNAME_TransactionType", "subscriberid_CONDITION", "COLUMNVALUE_TransactionType", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent32": AppComponentStatus[testEvent] = Function_Library.EShopSQLDBSelect("TBL_tbl_AuthorizenetLog", "COLUMNNAME_Amount", "subscriberid_CONDITION", "COLUMNVALUE_Amount", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent33": AppComponentStatus[testEvent] = Function_Library.EShopSQLDBSelect("TBL_tbl_AuthorizenetLog", "COLUMNNAME_invoiceNumber", "subscriberid_CONDITION", "COLUMNVALUE_invoiceNumber", 2);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent34": AppComponentStatus[testEvent] = Function_Library.EShopSQLDBSelect("TBL_tbl_AuthorizenetLog", "COLUMNNAME_PONumber", "subscriberid_CONDITION", "COLUMNVALUE_PONumber", 2);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent35": AppComponentStatus[testEvent] = Function_Library.EShopSQLDBSelect("TBL_tbl_AuthorizenetLog", "COLUMNNAME_ItemunitPrice", "subscriberid_CONDITION", "COLUMNVALUE_ItemunitPrice", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent36": AppComponentStatus[testEvent] = Function_Library.EShopSQLDBSelect("TBL_tbl_AuthorizenetLog", "COLUMNNAME_StatusCode", "subscriberid_CONDITION", "COLUMNVALUE_StatusCode", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent37": AppComponentStatus[testEvent] = Function_Library.EShopSQLDBSelect("TBL_tbl_AuthorizenetLog", "COLUMNNAME_Status_text", "subscriberid_CONDITION", "COLUMNVALUE_Status_text", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent38": AppComponentStatus[testEvent] = Function_Library.EShopSQLDBSelect("TBL_tbl_AuthorizenetLog", "COLUMNNAME_accountNumber", "subscriberid_CONDITION", "COLUMNVALUE_accountNumber", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent39": AppComponentStatus[testEvent] = Function_Library.EShopSQLDBSelect("TBL_tbl_AuthorizenetLog", "COLUMNNAME_accountType", "subscriberid_CONDITION", "COLUMNVALUE_accountType", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent40": AppComponentStatus[testEvent] = Function_Library.EShopSQLDBSelect("TBL_tbl_AuthorizenetLog", "COLUMNNAME_messages_desc", "subscriberid_CONDITION", "COLUMNVALUE_messages_desc", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent41": AppComponentStatus[testEvent] = Function_Library.EShopSQLDBSelect("TBL_tbl_AuthorizenetLog", "COLUMNAME_Taxid", "subscriberid_CONDITION", "COLUMNVALUE_Taxid", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent42": AppComponentStatus[testEvent] = Function_Library.EShopSQLDBSelect("TBL_tbl_AuthorizenetLog", "COLUMNNAME_vatid", "subscriberid_CONDITION", "COLUMNVALUE_vatid", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					
					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}
	/**
	 *@author mura2261
	 * */
	public static void Postcond_AddUserPayment_1() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=15;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_FAMILY_MAPPING", "COLUMNNAME_Family_ID", "CONDITION_PARENT_ACCOUNT_ID", "COLUMNVALUE_Family_ID", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;

					case "ACEvent2": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_FAMILY_MAPPING", "COLUMNNAME_PRIMARY_IND", "CONDITION_PARENT_ACCOUNT_ID", "COLUMNVALUE_PRIMARY_IND", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent3": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_FAMILY_MAPPING", "COLUMNNAME_STATUS", "CONDITION_PARENT_ACCOUNT_ID", "COLUMNVALUE_STATUS", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;

					case "ACEvent4": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_FAMILY_MAPPING", "COLUMNNAME_Family_ID", "CONDITION_CHILD_ACCOUNT_ID", "COLUMNVALUE_Family_ID", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent5": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_FAMILY_MAPPING", "COLUMNNAME_PRIMARY_IND", "CONDITION_CHILD_ACCOUNT_ID", "CHILD_PRIMARY_ID", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;

					case "ACEvent6": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_FAMILY_MAPPING", "COLUMNNAME_STATUS", "CONDITION_CHILD_ACCOUNT_ID", "COLUMNVALUE_STATUS", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent7": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_SUBSCRIBER_PROFILE", "COLUMNNAME_ACCTBALANCE", "CONDITION_FAMILYID", "FAMILYACCOUNT_ACCTBALANCE_VALUE", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent8": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_SUBSCRIBER_PROFILE", "COLUMNNAME_TOPUP_FACE_VALUE", "CONDITION_FAMILYID", "FAMILYACCT_TOPUP_FACE_VALUE", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent9": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_SUBSCRIBER_PROFILE", "COLUMNAME_SERVICE_CONTROL", "CONDITION_FAMILYID", "FAMILYID_SERVICE_CONTROL_VALUE", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent10": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_SUBSCRIBER_PROFILE", "COLUMNNAME_ACCTBALANCE", "RRBS_PARENT_CONDITION", "PARENT_ACCTBALANCE", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent11": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_SUBSCRIBER_PROFILE", "COLUMNNAME_TOPUP_FACE_VALUE", "RRBS_PARENT_CONDITION", "PARENT_TOPUP_FACE_VALUE", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent12": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_SUBSCRIBER_PROFILE", "COLUMNAME_SERVICE_CONTROL", "RRBS_PARENT_CONDITION", "PARENT_SERVICE_CONTROL_VALUE", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent13": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_SUBSCRIBER_PROFILE", "COLUMNNAME_ACCTBALANCE", "RRBS_CHILD_CONDITION", "CHILD_ACCT_BALANCE", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent14": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_SUBSCRIBER_PROFILE", "COLUMNNAME_TOPUP_FACE_VALUE", "RRBS_CHILD_CONDITION", "CHILD_TOPUP_FACE_VALUE", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent15": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_SUBSCRIBER_PROFILE", "COLUMNAME_SERVICE_CONTROL", "RRBS_CHILD_CONDITION", "CHILD_SERVICE_CONTROL_VALUE", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					default:
					Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}
	
	/**
	 *@author mura2261
	 * */
	public static void Postcond_AddUserPayment_TAX() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=42;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_FAMILY_MAPPING", "COLUMNNAME_Family_ID", "CONDITION_PARENT_ACCOUNT_ID", "COLUMNVALUE_Family_ID", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;

					case "ACEvent2": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_FAMILY_MAPPING", "COLUMNNAME_PRIMARY_IND", "CONDITION_PARENT_ACCOUNT_ID", "COLUMNVALUE_PRIMARY_IND", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent3": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_FAMILY_MAPPING", "COLUMNNAME_STATUS", "CONDITION_PARENT_ACCOUNT_ID", "COLUMNVALUE_STATUS", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;

					case "ACEvent4": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_FAMILY_MAPPING", "COLUMNNAME_Family_ID", "CONDITION_CHILD_ACCOUNT_ID", "COLUMNVALUE_Family_ID", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent5": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_FAMILY_MAPPING", "COLUMNNAME_PRIMARY_IND", "CONDITION_CHILD_ACCOUNT_ID", "CHILD_PRIMARY_ID", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;

					case "ACEvent6": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_FAMILY_MAPPING", "COLUMNNAME_STATUS", "CONDITION_CHILD_ACCOUNT_ID", "COLUMNVALUE_STATUS", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent7": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_SUBSCRIBER_PROFILE", "COLUMNNAME_ACCTBALANCE", "CONDITION_FAMILYID", "FAMILYACCOUNT_ACCTBALANCE_VALUE", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent8": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_SUBSCRIBER_PROFILE", "COLUMNNAME_TOPUP_FACE_VALUE", "CONDITION_FAMILYID", "FAMILYACCT_TOPUP_FACE_VALUE", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent9": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_SUBSCRIBER_PROFILE", "COLUMNAME_SERVICE_CONTROL", "CONDITION_FAMILYID", "FAMILYID_SERVICE_CONTROL_VALUE", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent10": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_SUBSCRIBER_PROFILE", "COLUMNNAME_ACCTBALANCE", "RRBS_PARENT_CONDITION", "PARENT_ACCTBALANCE", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent11": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_SUBSCRIBER_PROFILE", "COLUMNNAME_TOPUP_FACE_VALUE", "RRBS_PARENT_CONDITION", "PARENT_TOPUP_FACE_VALUE", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent12": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_SUBSCRIBER_PROFILE", "COLUMNAME_SERVICE_CONTROL", "RRBS_PARENT_CONDITION", "PARENT_SERVICE_CONTROL_VALUE", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent13": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_SUBSCRIBER_PROFILE", "COLUMNNAME_ACCTBALANCE", "RRBS_CHILD_CONDITION", "CHILD_ACCT_BALANCE", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent14": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_SUBSCRIBER_PROFILE", "COLUMNNAME_TOPUP_FACE_VALUE", "RRBS_CHILD_CONDITION", "CHILD_TOPUP_FACE_VALUE", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent15": AppComponentStatus[testEvent] = Function_Library.RRBSDBSelect("TBL_RRBS_SUBSCRIBER_PROFILE", "COLUMNAME_SERVICE_CONTROL", "RRBS_CHILD_CONDITION", "CHILD_SERVICE_CONTROL_VALUE", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent16": AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_tblFamilyplantransaction", "COLUMNAME_CHILDMSISDN", "MVNO_MSISDN_CONDITION", "COLUMNVALUE_CHILDMSISDN", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent17": AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_tblFamilyplantransaction", "COLUMNNAME_CardID", "MVNO_MSISDN_CONDITION", "COLUMNVALUE_CARDID", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent18": AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_tblFamilyplantransaction", "COLUMNNAME_TopupAmount", "MVNO_MSISDN_CONDITION", "COLUMNVALUE_TopupAmount", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent19": AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_tblFamilyplantransaction", "COLUMNNAME_TopupMode", "MVNO_MSISDN_CONDITION", "COLUMNVALUE_TopupMode", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent20": AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_tblFamilyplantransaction", "COLUMNSTATUS_status1", "MVNO_MSISDN_CONDITION", "COLUMNSTATUS_status1_value", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent21": AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_tblFamilyplantransaction", "COLUMNNAME_ErrorDesc", "MVNO_MSISDN_CONDITION", "COLUMNVALUE_ErrorDesc", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent22": AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_tblFamilyplantransaction", "COLUMNNAME_FamilyAccID", "MVNO_MSISDN_CONDITION", "COLUMNVALUE_FamilyAccID", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent23": AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_tblFamilyplantransaction", "COLUMNNAME_TaxAmount", "MVNO_MSISDN_CONDITION", "COLUMNVALUE_TaxAmount", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent24": AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_tblFamilyplantransaction", "COLUMNNAME_VATtransid", "MVNO_MSISDN_CONDITION", "COLUMNVALUE_VATtransid", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent25": AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_tblFamilyplantransaction", "COLUMNNAME_VATamount", "MVNO_MSISDN_CONDITION", "COLUMNVALUE_VATamount", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent26": AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_tblFamilyplantransaction", "COLUMNNAME_VATperc", "MVNO_MSISDN_CONDITION", "COLUMNVALUE_VATperc", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent27": AppComponentStatus[testEvent] = Function_Library.SQLDBSelect("TBL_tblFamilyplantransaction", "COLUMNNAME_TotalAmount", "MVNO_MSISDN_CONDITION", "COLUMNVALUE_TotalAmount", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent28": AppComponentStatus[testEvent] = Function_Library.SQLDBEnvironmentVariableCompare("TBL_tblFamilyplantransaction", "COLUMNNAME_TRANSACTIONID", "MVNO_MSISDN_CONDITION", "NodeVal_to_Environment", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent29": AppComponentStatus[testEvent] = Function_Library.EShopSQLDBSelect("TBL_tbl_AuthorizenetLog", "COLUMNNAME_TransactionType", "subscriberid_CONDITION", "COLUMNVALUE_TransactionType", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent30": AppComponentStatus[testEvent] = Function_Library.EshopSQLDBEnvironmentVariableCompare("TBL_tbl_AuthorizenetLog", "COLUMNNAME_transId", "subscriberid_CONDITION", "NodeVal_to_Environment", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent31": AppComponentStatus[testEvent] = Function_Library.EShopSQLDBSelect("TBL_tbl_AuthorizenetLog", "COLUMNNAME_TransactionType", "subscriberid_CONDITION", "COLUMNVALUE_TransactionType", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent32": AppComponentStatus[testEvent] = Function_Library.EShopSQLDBSelect("TBL_tbl_AuthorizenetLog", "COLUMNNAME_Amount", "subscriberid_CONDITION", "COLUMNVALUE_Amount", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent33": AppComponentStatus[testEvent] = Function_Library.EShopSQLDBSelect("TBL_tbl_AuthorizenetLog", "COLUMNNAME_invoiceNumber", "subscriberid_CONDITION", "COLUMNVALUE_invoiceNumber", 2);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent34": AppComponentStatus[testEvent] = Function_Library.EShopSQLDBSelect("TBL_tbl_AuthorizenetLog", "COLUMNNAME_PONumber", "subscriberid_CONDITION", "COLUMNVALUE_PONumber", 2);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent35": AppComponentStatus[testEvent] = Function_Library.EShopSQLDBSelect("TBL_tbl_AuthorizenetLog", "COLUMNNAME_ItemunitPrice", "subscriberid_CONDITION", "COLUMNVALUE_ItemunitPrice", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent36": AppComponentStatus[testEvent] = Function_Library.EShopSQLDBSelect("TBL_tbl_AuthorizenetLog", "COLUMNNAME_StatusCode", "subscriberid_CONDITION", "COLUMNVALUE_StatusCode", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent37": AppComponentStatus[testEvent] = Function_Library.EShopSQLDBSelect("TBL_tbl_AuthorizenetLog", "COLUMNNAME_Status_text", "subscriberid_CONDITION", "COLUMNVALUE_Status_text", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent38": AppComponentStatus[testEvent] = Function_Library.EShopSQLDBSelect("TBL_tbl_AuthorizenetLog", "COLUMNNAME_accountNumber", "subscriberid_CONDITION", "COLUMNVALUE_accountNumber", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent39": AppComponentStatus[testEvent] = Function_Library.EShopSQLDBSelect("TBL_tbl_AuthorizenetLog", "COLUMNNAME_accountType", "subscriberid_CONDITION", "COLUMNVALUE_accountType", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent40": AppComponentStatus[testEvent] = Function_Library.EShopSQLDBSelect("TBL_tbl_AuthorizenetLog", "COLUMNNAME_messages_desc", "subscriberid_CONDITION", "COLUMNVALUE_messages_desc", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
																						
					case "ACEvent41": AppComponentStatus[testEvent] = Function_Library.EshopSQLDBEnvironmentVariableCompare("TBL_tbl_AuthorizenetLog", "COLUMNAME_Taxid","subscriberid_CONDITION","NodeVal_to_Environment",1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					case "ACEvent42": AppComponentStatus[testEvent] = Function_Library.EShopSQLDBSelect("TBL_tbl_AuthorizenetLog", "COLUMNNAME_vatid", "subscriberid_CONDITION", "COLUMNVALUE_vatid", 1);
					Log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );	
					break;
					
					
					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}
	
	
	public static void Start_Script_DynamicNodeProcess() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=2;
			Log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			Log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.Unix_ITG_OutputResult_Verify("Command_Start","Output_START_Result",1,"Entity_Name");
					break;
					case "ACEvent2": AppComponentStatus[testEvent] = Function_Library.Verify_XMLDynamicRequest_Response_Data("Request_Type","Input_XML_Data","Output_XML_Data","XLDB_Input_Query","DYNAMIC_NODE_FOR_REQUESTXML","Entity_Name",1);
					break;
					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" +EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							Log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus, EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			Log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}
	
	
	/**
	 * @Objective <b>Update the Preconditions SQL Update or Delete</b>
	 * @author <b>Lakshman A</b>
	 * @since <b>2-SEP-16</b>
	 */

	public static void DB_COMMON_POSTCONDITION_1() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=1;
			log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.DBCommonPostCondition("DATABASE", "FORMAT", "TABLE_NAME", "COLUMN_NAME", "CONDITION", "EXPECTED_VALUE", "DAYS_TO_ADD","ENVIRONMENT_VALUE","APPEND", 1);
					log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );
					break;

					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" + EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus,  EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}	
	
	
	/**
	 * @Objective <b>Update the Preconditions SQL Update or Delete</b>
	 * @author <b>Lakshman A</b>
	 * @since <b>2-SEP-16</b>
	 */

	public static void DB_COMMON_POSTCONDITION() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=1;
			log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.DBCommonPostCondition("DATABASE", "FORMAT", "TABLE_NAME", "COLUMN_NAME", "CONDITION", "EXPECTED_VALUE", "DAYS_TO_ADD","ENVIRONMENT_VALUE","APPEND", 1);
					log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );
					break;

					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" + EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus,  EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}	
	

	/**
	 * @Objective <b>Update the Preconditions SQL Update or Delete</b>
	 * @author <b>Lakshman A</b>
	 * @since <b>1-SEP-16</b>
	 */

	public static void DB_COMMON_PRECONDITION() throws Exception{
		int noEventStatus = 0;
		int  totalEvents;
		String  disableEvents=null;
		String testFlow=null;

		try{
			totalEvents=1;
			log.info("Total Events in the Component is : "+ totalEvents);
			disableEvents=ReadExcel.RetrieveEvents(Filepath, EnvironmentValue.getProperty("TESTCASE_NAME"), "Disable_Events", gblAppComponentCounter);
			log.info("Disable Events in the Component is : "+ disableEvents);

			if(totalEvents !=-1 &&  !(disableEvents.equals(""))){
				appComponentEvents=InitializeEvent(totalEvents,disableEvents);

				for (int testEvent=0;testEvent<appComponentEvents.length;testEvent++){
					testFlow="ACEvent" + appComponentEvents[testEvent];
					switch (testFlow) {
					case "ACEvent1": AppComponentStatus[testEvent] = Function_Library.DBCommonPreCondition("DB_TYPE","ACTION","TABLE_NAME", "COLUMN_NAME", "COLUMN_VALUE", "CONDITION", 1);
					log.info(testFlow +" value is "+ AppComponentStatus[testEvent] );
					break;

					default: Report_Functions.ReportEventFailure(doc, "App_Component", "Error in reading the step of the Application Component '" + EnvironmentValue.getProperty("App_Component_Name")+"'", false);
					noEventStatus=1;
					break;
					}

					if(!AppComponentStatus[testEvent]){
						if((Param.getProperty("Exclude_AP_If_Event_Failed")).equals("True") ){
							log.info("Exiting For Loop as TestEvent failed and breaking the AppComponent.");
							break;
						}
					}
				}
			}else{
				noEventStatus=1;
				Report_Functions.ReportEventFailure(doc, "App_componenet", "Enter valid values in the Test Data spreadsheet in the columns Total_Events/Disable_Events", false);
			}

			ValidateComponent(appComponentEvents, AppComponentStatus,noEventStatus,  EnvironmentValue.getProperty("App_Component_Name"));

		}catch(Exception e){
			log.info("Exception in AppComponent - "+EnvironmentValue.getProperty("App_Component_Name")+" is : "+ e);
			throw e;
		}finally{
			disableEvents=null;
			testFlow=null;
			AppComponentStatus=null;
			appComponentEvents=null;
		}
	}	

	

	

}