package Utility;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

import Libraries.Report_Functions;

@SuppressWarnings("unused")
public class ReadExcel extends Report_Functions {

	static File nfile=null;
	public static String StringerrorReturnFlag="No";
	public static Fillo fillo=null;
	static Connection filloConnection=null;
	static Recordset recordset=null;



	public static int retrieveNoOfRows(HSSFWorkbook wb,String wsName){
		HSSFSheet ws = null;	
		try{
			int sheetIndex=wb.getSheetIndex(wsName);
			if(sheetIndex==-1)
				return 0;
			else{
				ws = wb.getSheetAt(sheetIndex);
				int rowCount=ws.getLastRowNum()+1;		
				return rowCount;		
			}
		}catch(Exception e){
			throw e;
		}finally{
			ws = null;	
		}
	}

	public static int retrieveNoOfCols(HSSFWorkbook wb,String wsName){
		HSSFSheet ws = null;	
		try{
			int sheetIndex=wb.getSheetIndex(wsName);
			if(sheetIndex==-1)
				return 0;
			else{
				ws = wb.getSheetAt(sheetIndex);
				int colCount=ws.getRow(0).getLastCellNum();			
				return colCount;
			}
		}catch(Exception e){
			throw e;
		}finally{
			ws = null;	
		}
	}

	public static boolean CheckTestCaseNamedSheetExist(String filelocation, String TESTCASE_NAME) throws Exception{
		FileInputStream ipstr = null;
		HSSFWorkbook wb = null;
		boolean sheetExist=false;
		try{
			ipstr = new FileInputStream(filelocation);
			wb = new HSSFWorkbook(ipstr);
			ipstr.close();
			for(int k=0; k<wb.getNumberOfSheets(); k++){
				if(wb.getSheetName(k).equalsIgnoreCase(TESTCASE_NAME)){
					sheetExist=true;
				}
			}
		}catch(Exception e){
			return sheetExist;
		}finally{
			ipstr = null;
			if(wb!=null){
				wb.close();
				wb=null;
			}
		}
		return sheetExist;
	}

	public static int GetRowCount(String filelocation,String wsName) throws Exception{

		FileInputStream ipstr = null;
		HSSFWorkbook wb = null;
		HSSFSheet ws = null;	
		int sheetIndex;
		try{
			ipstr = new FileInputStream(filelocation);
			wb = new HSSFWorkbook(ipstr);
			ipstr.close();

			sheetIndex=wb.getSheetIndex(wsName);
			if(sheetIndex==-1)
				return 0;
			else{
				ws = wb.getSheetAt(sheetIndex);
				int rowCount=ws.getLastRowNum()+1;		
				return rowCount;		
			}				
		}catch(Exception e){
			//throw e;
			Report_Functions.ReportEventFailure(doc, "App_component","Error Occured-"+e.getMessage(), false);
			return 0;
		}finally{
			ipstr = null;
			if(wb!=null){
				wb.close();
				wb=null;
			}
			ws = null;	
		}
	}

	public static String RetrieveExecutionFlagOfTestCase(String filelocation,String wsName, String colName, int rowNum) throws Exception{

		FileInputStream ipstr = null;
		HSSFWorkbook wb = null;
		HSSFSheet ws = null;	
		HSSFRow Suiterow=null;
		HSSFRow row=null;
		HSSFCell cell=null;
		String value=null;
		int sheetIndex;
		int colNum;

		try{
			ipstr = new FileInputStream(filelocation);
			wb = new HSSFWorkbook(ipstr);
			ipstr.close();
			sheetIndex=wb.getSheetIndex(wsName);
			ws = wb.getSheetAt(sheetIndex);
			if(sheetIndex==-1)
				return "";
			else{
				colNum = retrieveNoOfCols(wb,wsName);
				int colNumber=-1;	
				Suiterow = ws.getRow(0);				
				for(int i=0; i<colNum; i++){
					if(Suiterow.getCell(i).getStringCellValue().equals(colName.trim())){
						colNumber=i;					
					}					
				}
				if(colNumber==-1){
					//throw new RuntimeException("colNumber  -1");				
					return StringerrorReturnFlag;
				}
				row = ws.getRow(rowNum);
				cell = row.getCell(colNumber);
				if(cell==null){
					//throw new RuntimeException("Cell is NULL");
					return StringerrorReturnFlag;
				}
				value = cellToString(cell);
			}						
		}catch(Exception e){
			//throw e;
			Report_Functions.ReportEventFailure(doc, "App_component","Error Occured-"+e.getMessage(), false);
			return StringerrorReturnFlag;
		}finally{
			ipstr = null;
			if(wb!=null){
				wb.close();
				wb=null;
			}
			ws = null;	
			Suiterow=null;
			row=null;
			cell=null;
		}
		return value;
	}

	public static String RetrieveTestCaseNameFromBatch(String filelocation,String wsName, String colName, int rowNum) throws Exception{

		FileInputStream ipstr = null;
		HSSFWorkbook wb = null;
		HSSFSheet ws = null;	
		HSSFRow Suiterow=null;
		HSSFRow row=null;
		HSSFCell cell=null;
		String value=null;
		int sheetIndex;
		int colNum;

		try{
			ipstr = new FileInputStream(filelocation);
			wb = new HSSFWorkbook(ipstr);
			//ws = wb.getSheetAt(0);
			ipstr.close();
			sheetIndex=wb.getSheetIndex(wsName);
			ws = wb.getSheetAt(sheetIndex);
			if(sheetIndex==-1)
				return "";
			else{
				colNum = retrieveNoOfCols(wb,wsName);
				int colNumber=-1;	
				Suiterow = ws.getRow(0);				
				for(int i=0; i<colNum; i++){
					if(Suiterow.getCell(i).getStringCellValue().equals(colName.trim())){
						colNumber=i;					
					}					
				}
				if(colNumber==-1){
					//throw new RuntimeException("colNumber  -1");	
					return null;			
				}
				row = ws.getRow(rowNum);
				cell = row.getCell(colNumber);
				if(cell==null){
					//throw new RuntimeException("Cell is NULL");
					return null;
				}
				value = cellToString(cell);

			}						
		}catch(Exception e){
			//throw e;
			Report_Functions.ReportEventFailure(doc, "App_component","Error Occured-"+e.getMessage(), false);
			return e.getMessage();
		}finally{
			ipstr = null;
			if(wb!=null){
				wb.close();
				wb=null;
			}
			ws = null;	
			Suiterow=null;
			row=null;
			cell=null;
		}
		return value;
	}

	public static String RetrieveTestCaseDescFromBatch(String filelocation,String wsName, String colName, int rowNum) throws Exception{

		FileInputStream ipstr = null;
		HSSFWorkbook wb = null;
		HSSFSheet ws = null;	
		HSSFRow Suiterow=null;
		HSSFRow row=null;
		HSSFCell cell=null;
		String value=null;
		int sheetIndex;
		int colNum;
		String FileErrorMsg="File path Location is not provided";
		String SheetErrorMsg="Sheet Name is not provided";
		String ColErrorMsg="Column Value is not provided";
		String rowErrorMsg="Row Value is not provided";
		String nullContent=colName+" is having empty value in the cell";

		//************************************Newly added code*****************************************************************
		//Check if filelocation field is empty
		if (filelocation=="" || filelocation==null){
			Report_Functions.ReportEventFailure(doc,"App_component",FileErrorMsg, false);
			return FileErrorMsg;
		}
		if (wsName=="" || wsName==null){
			Report_Functions.ReportEventFailure(doc,"App_component",SheetErrorMsg, false);
			return SheetErrorMsg;
		}

		if (colName=="" || colName==null){
			Report_Functions.ReportEventFailure(doc,"App_component",ColErrorMsg, false);
			return ColErrorMsg;
		}
		if (rowNum==0){
			Report_Functions.ReportEventFailure(doc,"App_component",rowErrorMsg, false);
			return rowErrorMsg;
		}

		//************************************Newly added code*****************************************************************	

		try{
			ipstr = new FileInputStream(filelocation);
			wb = new HSSFWorkbook(ipstr);
			ipstr.close();
			sheetIndex=wb.getSheetIndex(wsName);
			ws = wb.getSheetAt(sheetIndex);
			if(sheetIndex==-1)
				return "";
			else{
				colNum = retrieveNoOfCols(wb,wsName);
				int colNumber=-1;	

				Suiterow = ws.getRow(0);				

				for(int i=0; i<colNum; i++){
					if(Suiterow.getCell(i).getStringCellValue().equals(colName.trim())){
						colNumber=i;					
					}					
				}
				if(colNumber==-1){
					//throw new RuntimeException("colNumber  -1");				
					return nullContent;
				}

				row = ws.getRow(rowNum);
				cell = row.getCell(colNumber);
				if(cell==null){
					throw new RuntimeException("Cell is NULL");
				}
				value = cellToString(cell);
			}						
			/*}catch(Exception e){
			throw e;*/
		}catch(Exception e){
			Report_Functions.ReportEventFailure(doc, "App_component","Error Occured-"+e.getMessage(), false);
			return e.getMessage();
		}finally{
			ipstr = null;
			if(wb!=null){
				wb.close();
				wb=null;
			}
			ws = null;	
			Suiterow=null;
			row=null;
			cell=null;
		}
		return value;
	}

	public static String RetrieveAppCompName(String filelocation,String wsName, String colName, int rowNum) throws Exception{

		FileInputStream ipstr = null;
		HSSFWorkbook wb = null;
		HSSFSheet ws = null;	
		HSSFRow Suiterow=null;
		HSSFRow row=null;
		HSSFCell cell=null;
		String value=null;
		int sheetIndex;
		int colNum;

		try{
			ipstr = new FileInputStream(filelocation);
			wb = new HSSFWorkbook(ipstr);
			ipstr.close();
			sheetIndex=wb.getSheetIndex(wsName);
			ws = wb.getSheetAt(sheetIndex);
			if(sheetIndex==-1){
				Report_Functions.ReportEventFailure(doc, "App_component","Test sheet did not match with testcase nanme", false);
				return "";
			}
			else{
				colNum = retrieveNoOfCols(wb,wsName);
				int colNumber=-1;	
				Suiterow = ws.getRow(0);				
				for(int i=0; i<colNum; i++){
					if(Suiterow.getCell(i).getStringCellValue().equals(colName.trim())){
						colNumber=i;					
					}					
				}
				if(colNumber==-1){
					throw new RuntimeException("colNumber  -1");				
				}
				row = ws.getRow(rowNum);
				cell = row.getCell(colNumber);
				if(cell==null){
					throw new RuntimeException("Cell is NULL");
				}
				value = cellToString(cell);
			}						
		}catch(Exception e){
			//throw e;
			return "";
		}finally{
			ipstr = null;
			if(wb!=null){
				wb.close();
				wb=null;
			}
			ws = null;	
			Suiterow=null;
			row=null;
			cell=null;
		}
		return value;
	}

	public static String RetrieveAppCompDesc(String filelocation,String wsName, String colName, int rowNum) throws Exception{

		FileInputStream ipstr = null;
		HSSFWorkbook wb = null;
		HSSFSheet ws = null;	
		HSSFRow Suiterow=null;
		HSSFRow row=null;
		HSSFCell cell=null;
		String value=null;
		int sheetIndex;
		int colNum;

		try{
			ipstr = new FileInputStream(filelocation);
			wb = new HSSFWorkbook(ipstr);
			//ws = wb.getSheetAt(0);
			ipstr.close();
			sheetIndex=wb.getSheetIndex(wsName);
			ws = wb.getSheetAt(sheetIndex);
			if(sheetIndex==-1)
				return "";
			else{
				colNum = retrieveNoOfCols(wb,wsName);
				int colNumber=-1;	
				Suiterow = ws.getRow(0);				
				for(int i=0; i<colNum; i++){
					if(Suiterow.getCell(i).getStringCellValue().equals(colName.trim())){
						colNumber=i;					
					}					
				}
				if(colNumber==-1){
					//throw new RuntimeException("colNumber  -1");				
					return StringerrorReturnFlag;
				}
				row = ws.getRow(rowNum);
				cell = row.getCell(colNumber);
				if(cell==null){
					//throw new RuntimeException("Cell is NULL");
					return StringerrorReturnFlag;
				}
				value = cellToString(cell);
			}						
		}catch(Exception e){
			//throw e;
			return StringerrorReturnFlag;
		}finally{
			ipstr = null;
			if(wb!=null){
				wb.close();
				wb=null;
			}
			ws = null;	
			Suiterow=null;
			row=null;
			cell=null;
		}
		return value;
	}

	public static String RetrieveAppCompExecFlag(String filelocation,String wsName, String colName, int rowNum) throws Exception{

		FileInputStream ipstr = null;
		HSSFWorkbook wb = null;
		HSSFSheet ws = null;	
		HSSFRow Suiterow=null;
		HSSFRow row=null;
		HSSFCell cell=null;
		String value=null;
		int sheetIndex;
		int colNum;

		try{
			ipstr = new FileInputStream(filelocation);
			wb = new HSSFWorkbook(ipstr);
			ipstr.close();
			sheetIndex=wb.getSheetIndex(wsName);
			ws = wb.getSheetAt(sheetIndex);
			if(sheetIndex==-1)
				return "";
			else{
				colNum = retrieveNoOfCols(wb,wsName);
				int colNumber=-1;	
				Suiterow = ws.getRow(0);				
				for(int i=0; i<colNum; i++){
					if(Suiterow.getCell(i).getStringCellValue().equals(colName.trim())){
						colNumber=i;					
					}					
				}
				if(colNumber==-1){
					//throw new RuntimeException("colNumber  -1");				
					return StringerrorReturnFlag;
				}
				row = ws.getRow(rowNum);
				cell = row.getCell(colNumber);
				if(cell==null){
					//throw new RuntimeException("Cell is NULL");
					return StringerrorReturnFlag;
				}
				value = cellToString(cell);
			}						
		}catch(Exception e){
			//throw e;
			return StringerrorReturnFlag;
		}finally{
			ipstr = null;
			if(wb!=null){
				wb.close();
				wb=null;
			}
			ws = null;	
			Suiterow=null;
			row=null;
			cell=null;
		}
		return value;
	}

	public static String RetrieveTestDataFromSheet(String filelocation,String wsName, String colName, int rowNum) throws Exception{

		FileInputStream ipstr = null;
		HSSFWorkbook wb = null;
		HSSFSheet ws = null;	
		HSSFRow Suiterow=null;
		HSSFRow row=null;
		HSSFCell cell=null;		
		String value=null;
		int sheetIndex;
		int colNum;

		try{
			ipstr = new FileInputStream(filelocation);
			wb = new HSSFWorkbook(ipstr);
			//ws = wb.getSheetAt(0);
			ipstr.close();
			sheetIndex=wb.getSheetIndex(wsName);
			ws = wb.getSheetAt(sheetIndex);
			if(sheetIndex==-1)
				return "";
			else{
				colNum = retrieveNoOfCols(wb,wsName);
				int colNumber=-1;	
				Suiterow = ws.getRow(0);				
				for(int i=0; i<colNum; i++){
					if(Suiterow.getCell(i).getStringCellValue().equals(colName.trim())){
						colNumber=i;					
					}					
				}
				if(colNumber==-1){
					throw new RuntimeException("colNumber  -1");				
				}
				row = ws.getRow(rowNum);
				cell = row.getCell(colNumber);
				if(cell==null){
					throw new RuntimeException("Cell is NULL");
				}
				value = cellToString(cell);
			}						
		}catch(Exception e){
			//throw e;
			return e.getMessage();
		}finally{
			ipstr = null;
			if(wb!=null){
				wb.close();
				wb=null;
			}
			ws = null;	
			Suiterow=null;
			row=null;
			cell=null;
		}
		return value;
	}

	public static String[] GetTestDatafromExcel(String filelocation,String wsName,String colName, int rowNum) throws Exception{

		FileInputStream ipstr = null;
		HSSFWorkbook wb = null;
		HSSFSheet ws = null;	
		HSSFCell cell=null;
		String value=null;
		String data[]=null;
		int sheetIndex;
		int colNum;

		try{
			ipstr = new FileInputStream(filelocation);
			wb = new HSSFWorkbook(ipstr);
			ipstr.close();
			sheetIndex=wb.getSheetIndex(wsName);
			ws = wb.getSheetAt(sheetIndex);
			if(sheetIndex==-1)
				throw new RuntimeException("sheetIndex  -1");
			else{
				colNum = retrieveNoOfCols(wb,wsName);
				data = new String[colNum];
				HSSFRow row = ws.getRow(rowNum);
				for(int j=0; j< colNum; j++){					
					if(row==null){
						data[j] = "";
					}
					else{
						cell = row.getCell(j);	

						if(cell==null){
							data[j] = "";							
						}
						else{
							cell.setCellType(Cell.CELL_TYPE_STRING);
							value = cellToString(cell);
							data[j] = value;						
						}
					}
				}				

				return data;
			}						
		}catch(Exception e){
			throw e;
		}finally{
			ipstr = null;
			if(wb!=null){
				wb.close();
				wb=null;
			}
			ws = null;	
			cell=null;
			value=null;
		}
	}

	public static String RetrieveTestDataExecFlag(String filelocation,String wsName, String colName, int rowNum) throws Exception{

		FileInputStream ipstr = null;
		HSSFWorkbook wb = null;
		HSSFSheet ws = null;	
		HSSFRow Suiterow=null;
		HSSFRow row=null;
		HSSFCell cell=null;
		String value=null;
		int sheetIndex;
		int colNum;

		try{
			ipstr = new FileInputStream(filelocation);
			wb = new HSSFWorkbook(ipstr);
			//ws = wb.getSheetAt(0);
			ipstr.close();
			sheetIndex=wb.getSheetIndex(wsName);
			ws = wb.getSheetAt(sheetIndex);
			if(sheetIndex==-1)
				return "";
			else{
				colNum = retrieveNoOfCols(wb,wsName);
				int colNumber=-1;	
				Suiterow = ws.getRow(0);				
				for(int i=0; i<colNum; i++){
					if(Suiterow.getCell(i).getStringCellValue().equals(colName.trim())){
						colNumber=i;					
					}					
				}
				if(colNumber==-1){
					//throw new RuntimeException("colNumber  -1");				
					return StringerrorReturnFlag;
				}
				row = ws.getRow(rowNum);
				cell = row.getCell(colNumber);
				if(cell==null){
					//throw new RuntimeException("Cell is NULL");
					return StringerrorReturnFlag;
				}
				value = cellToString(cell);
			}						
		}catch(Exception e){
			//throw e;
			return StringerrorReturnFlag;
		}finally{
			ipstr = null;
			if(wb!=null){
				wb.close();
				wb=null;
			}
			ws = null;	
			Suiterow=null;
			row=null;
			cell=null;
		}
		return value;
	}

	public static String  RetrieveEvents(String filelocation,String wsName, String colName, int rowNum) throws Exception{

		FileInputStream ipstr = null;
		HSSFWorkbook wb = null;
		HSSFSheet ws = null;	
		HSSFRow Suiterow=null;
		HSSFRow row=null;
		HSSFCell cell=null;
		String value;
		int sheetIndex;
		int colNum;

		try{
			ipstr = new FileInputStream(filelocation);
			wb = new HSSFWorkbook(ipstr);
			//ws = wb.getSheetAt(0);
			ipstr.close();
			sheetIndex=wb.getSheetIndex(wsName);
			ws = wb.getSheetAt(sheetIndex);
			if(sheetIndex==-1)
				return null;
			else{
				colNum = retrieveNoOfCols(wb,wsName);
				int colNumber=-1;	
				Suiterow = ws.getRow(0);				

				for(int i=0; i<colNum; i++){
					if(Suiterow.getCell(i).getStringCellValue().equals(colName.trim())){
						colNumber=i;					
					}					
				}
				if(colNumber==-1){
					throw new RuntimeException("colNumber  -1");				
				}
				row = ws.getRow(rowNum);
				cell = row.getCell(colNumber);
				if(cell==null){
					throw new RuntimeException("Cell is NULL");
				}
				value = cellToStringcom(cell);
			}						
		}catch(Exception e){
			throw e;
		}finally{
			ipstr = null;
			if(wb!=null){
				wb.close();
				wb=null;
			}
			ws = null;	
			Suiterow=null;
			row=null;
			cell=null;
		}
		return value;
	}

	public static int RetrieveTotalEventsCount(String filelocation,String wsName, String colName, int rowNum) throws Exception{


		FileInputStream ipstr = null;
		HSSFWorkbook wb = null;
		HSSFSheet ws = null;	
		HSSFRow Suiterow=null;
		HSSFRow row=null;
		HSSFCell cell=null;
		int value;
		int sheetIndex;
		int colNum;

		try{
			ipstr = new FileInputStream(filelocation);
			wb = new HSSFWorkbook(ipstr);
			//ws = wb.getSheetAt(0);
			ipstr.close();
			sheetIndex=wb.getSheetIndex(wsName);
			ws = wb.getSheetAt(sheetIndex);
			if(sheetIndex==-1)
				return -1;
			else{
				colNum = retrieveNoOfCols(wb,wsName);
				int colNumber=-1;	

				Suiterow = ws.getRow(0);				

				for(int i=0; i<colNum; i++){
					if(Suiterow.getCell(i).getStringCellValue().equals(colName.trim())){
						colNumber=i;					
					}					
				}
				if(colNumber==-1){
					throw new RuntimeException("colNumber  -1");				
				}

				row = ws.getRow(rowNum);
				cell = row.getCell(colNumber);
				if(cell==null){
					throw new RuntimeException("Cell is NULL");
				}
				value =  cellToint(cell);
				return value;			
			}			
		}catch(Exception e){
			throw e;
		}finally{
			ipstr = null;
			if(wb!=null){
				wb.close();
				wb=null;
			}
			ws = null;	
			Suiterow=null;
			row=null;
			cell=null;
		}
	}

	public static String RetrieveXMLFromSupportFiles(String filelocation,String wsName, String colName, String rowName) throws Exception{

		FileInputStream ipstr = null;
		HSSFWorkbook wb = null;
		HSSFSheet ws = null;	
		HSSFRow Suiterow=null;
		HSSFRow row=null;
		HSSFCell cell=null;
		String value=null;
		int sheetIndex;
		int colNum;

		try{
			ipstr = new FileInputStream(filelocation);
			wb = new HSSFWorkbook(ipstr);
			//ws = wb.getSheetAt(0);
			ipstr.close();
			sheetIndex=wb.getSheetIndex(wsName);
			ws = wb.getSheetAt(sheetIndex);
			if(sheetIndex==-1)
				return "";
			else{
				int rowNum = retrieveNoOfRows(wb,wsName);
				colNum = retrieveNoOfCols(wb,wsName);
				int colNumber=-1;	
				int rowNumber=-1;	
				Suiterow = ws.getRow(0);				
				for(int i=0; i<colNum; i++){
					if(Suiterow.getCell(i).getStringCellValue().equals(colName.trim())){
						colNumber=i;					
					}					
				}
				if(colNumber==-1){
					throw new RuntimeException("colNumber  -1");				
				}


				for(int j=0; j<rowNum; j++){
					HSSFRow Suitecol = ws.getRow(j);				
					if(Suitecol.getCell(1).getStringCellValue().equals(rowName.trim())){
						rowNumber=j;	
					}					
				}

				if(rowNumber==-1){
					return "";				
				}

				row = ws.getRow(rowNumber);
				cell = row.getCell(colNumber);
				if(cell==null){
					return "";
				}
				value = cellToString(cell);
			}
		}catch(Exception e){
			throw e;
		}finally{
			ipstr = null;
			if(wb!=null){
				wb.close();
				wb=null;
			}
			ws = null;	
			Suiterow=null;
			row=null;
			cell=null;
		}
		return value;
	}

	public static int cellToint(HSSFCell cell){
		double result;
		int resultint;
		try {
			result = cell.getNumericCellValue();
			resultint = (int) result;
		}catch(Exception e){
			throw new RuntimeException("Unsupportd cell - cellToint. ");			
		}
		return resultint;
	}

	public static String cellToString(HSSFCell cell){
		int type;
		Object result;
		try{
			type = cell.getCellType();		
			switch (type){
			case 0 : result = cell.getNumericCellValue();
			break;
			case 1 : result = cell.getStringCellValue();
			break;
			case 3 : result = "";
			break;
			default :
				throw new RuntimeException("Unsupportd cell - cellToString.");			
			}
			return result.toString();
		}catch(Exception e){
			throw e;
		}
	}

	public static String cellToStringcom(HSSFCell cell){
		int type;
		Object result;
		try{
			type = cell.getCellType();			
			switch (type){
			case 0 : result = cell.getNumericCellValue();
			break;
			case 1 : result = cell.getStringCellValue();
			break;
			default :
				throw new RuntimeException("Unsupportd cell - cellToString.");			
			}
			return  result.toString();
		}catch(Exception e){
			throw e;
		}
	}
	
	/**
	 * 
	 * @Objective <b>This method is to Fetch records from Excel based on SQL Query<b>
	 * @Parameter<b>Select_Column_Name - Value of the Field which is required in the Excel
	 * @Parameter<b>sqlQuery format - (Eg.,Select * from XML_Template where TestCaseName="TC_001")
	 * @author <b>Muralimohan M</b>
	 * @since <b>17-May-16</b>
	 */
	
	public static String RetrieveValueFromTestDataBasedOnQuery(String FileLocation,String FileName,String Select_Column_Name,String sqlQuery) throws Exception{

		String FilePath=System.getProperty("user.dir")+FileLocation+FileName;
		
		String record=null;
		try {
			fillo=new Fillo();
			filloConnection=fillo.getConnection(FilePath);
			recordset=filloConnection.executeQuery(sqlQuery);
			while(recordset.next()){
			record=recordset.getField(Select_Column_Name);
			
			}
			
		} catch (Exception e) {
			Log.info("FilloException::"+e.getMessage());
			Report_Functions.ReportEventFailure(doc,  "RetrieveValueFromTestDataBasedOnQuery","Error in Fetching data from Excel sheet:"+e.getMessage(), false);
			return e.getMessage();
			
		}
		fillo=null;
		filloConnection.close();
		return record;
		
		
	}

}
