package TempPackaga;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class jschUnixConnectivity {
	public static String username="testteam";
	public static String host="192.168.151.90";
	public static String password="testteam";
	public static Session session=null;
	public static Properties config=null;
	 static String endLineStr = " # ";
	public static Channel channel=null;
	public static String msg=null;
	public static String path="cd /home/testteam/Products/ITG-CBOS-API-AUTO/CBOS_ITG_API_NSS_Framework_ESP";
	public static String myCommand="./start_stop_Autologs.sh ESP_UPDATE_SUBSCRIBER_006 start 1.0.0.0 ITG Rev0 /home/testteam/Products/ITG-CBOS-API-AUTO/CBOS_ITG_API_NSS_Framework_ESP 2016_May_28_09_20_02 &";
	public static void main(String[] args) throws JSchException, IOException {
		// TODO Auto-generated method stub
		try{
		JSch jsch=new JSch();
		session=jsch.getSession(username, host,22);
		session.setPassword(password);
		Properties config = new Properties();
	    config.put("StrictHostKeyChecking", "no");
	    session.setConfig(config);
		session.connect();
		System.out.println(session.isConnected());
		channel=session.openChannel("exec");
		ChannelExec exec=(ChannelExec)channel;
		exec.setCommand(path+";"+myCommand);
		exec.setErrStream(System.err);
		exec.connect();
		
		BufferedInputStream in = new BufferedInputStream(exec.getInputStream() );
		 byte[] contents = new byte[1024];

		 int bytesRead=0;
		 String strFileContents = null; 
		 while( (bytesRead = in.read(contents)) != -1){ 
		    strFileContents += new String(contents, 0, bytesRead);               
		 }
		 System.out.print(strFileContents);
		//String content = org.apache.commons.io.IOUtils.t
		
		//BufferedReader bReader=new BufferedReader(new InputStreamReader(exec.getInputStream()));
		//DataInputStream  bis=new DataInputStream(exec.getInputStream());
		
		
	    String expectedText="Remote script activity for the testcase started1";
	    Pattern regPatter=Pattern.compile(expectedText);
		Matcher matches = regPatter.matcher(strFileContents);
		boolean statusMatch=matches.find();
		if (statusMatch) {
			System.out.println("matched:"+statusMatch);
		}
		else{
			System.out.println("not matched:"+statusMatch);
		}
	    
	    
		
	    exec.disconnect();
		
		
		
	}catch(JSchException  e){
		System.out.println(e.getMessage());
	}
		finally{
			session.disconnect();
			channel.disconnect();
			System.out.println(session.isConnected());
		}
	}
		
	
	
}
