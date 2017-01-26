package TempPackaga;

import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

import Libraries.Report_Functions;
import Utility.Log;


@SuppressWarnings("unused")
public class TempFile {

	
	public static Fillo fillo=null;
	static Connection filloConnection=null;
	static Recordset recordset=null;

	public static void main(String[] args) throws Exception {
		
		
		 }
	
	public static String RetrieveValueFromTestDataBasedOnQuery(String FileLocation,String FileName,String Select_Column_Name,String sqlQuery) throws Exception{

		String FilePath=System.getProperty("user.dir")+FileLocation+FileName;
		System.out.println("FilePath:"+FilePath);
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
			
			return e.getMessage();
			
		}
		fillo=null;
		filloConnection.close();
		return record;
		
		
	}
		


}
