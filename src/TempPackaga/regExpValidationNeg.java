package TempPackaga;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import Libraries.Function_Library;

import org.apache.xmlbeans.impl.tool.Extension.Param;

import Utility.Log;

public class regExpValidationNeg {
	

	static boolean Executionstatus=false;
	static FileInputStream fip=null;
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
//		
//		//String actvalue="XML Parsing Failed: test:1:52 error: value '' has length '0' which is less than minLength facet value '4'";
//		//String expvalue="XML Parsing Failed: test:[0-9]+:[0-9]+\\serror: value '' ";
		//XML Parsing Failed: test:1:122 error:  value '0' does not match regular expression facet '\d{19,20}'
		Properties Param=null;
		Param = new Properties();
		fip = new FileInputStream(System.getProperty("user.dir")+"//src//Property//Param.properties");
		Param.load(fip);
//		boolean status=RegExpValidator(actvalue,expvalue);
//		System.out.println("status:"+status);
		String actvalue = Param.getProperty("Temp");
		String expvalue=Param.getProperty("Temp1");
		String []splitContent = expvalue.split(":");
		String LastArray=splitContent[4].replace(Param.getProperty("esca"), Param.getProperty("doubleesca"));
		System.out.println(LastArray);
		LastArray=splitContent[4].replace("{", "\\{");
		System.out.println(LastArray);
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
		System.out.println("Expected:"+ExpectedValue+":Actual:"+ActualValue);
		if (statusMatch) {
			Executionstatus=true;
			Log.info("Pattern Matched Successfully for the Expected String Value:"+ExpectedValue+": Against ActualValue:"+ActualValue+":");
			return statusMatch;
		}
		else{
			Executionstatus=false;
			Log.info("Pattern Not Matched Successfully for the Expected String Value:"+ExpectedValue+": Against ActualValue:"+ActualValue+":");

		}

		regPatter=null;
		matches=null;
		return Executionstatus;

	}
}
