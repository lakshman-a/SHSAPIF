package TempPackaga;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Utility.Log;

public class regvalid {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		boolean result=RegExpValidator("TID0000001265","TID[0-9]+");
		System.out.println(result);
		
		
	}
	
	public static boolean RegExpValidator(String Expected,String ActualValue){
		Pattern regPatter=Pattern.compile(ActualValue);
		Matcher matches = regPatter.matcher(Expected);
		boolean statusMatch=matches.find();
		if (statusMatch) {
			Log.info("Pattern Matched Successfully for the Expected String Value:"+Expected+" Against ActualValue:"+ActualValue);
			return true;
		}
		regPatter=null;
		matches=null;
		return false;
		
	}

}
