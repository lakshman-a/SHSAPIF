package TempPackaga;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import org.w3c.dom.Document;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class AdhocHTTPRequest {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		@SuppressWarnings("unused")
		int result=0;

		String FilePath="\\Resources\\Support_Files\\";
		String FileName="XML_Template_Input_Output_Data.xls";
		String strQuery="SELECT Input_XML FROM XML_Template Where Test_Case_Name = 'GER_MODIFY_SUBSCRIBER_112'";
		String strURL="http://192.168.151.90:6540/MODIFY_SUBSCRIBER_INFORMATION/";
//		String strQuery="SELECT Input_XML FROM XML_Template Where Test_Case_Name = 'PFS_WITH_CREDIT_001'";
//		String strURL="http://192.168.151.90:6530/PURCHASE_FREE_SIM_WITH_CREDIT/";
		
		String xmlRequest=TempFile.RetrieveValueFromTestDataBasedOnQuery(FilePath, FileName, "Input_XML", strQuery);
		
		PostMethod post = new PostMethod(strURL);
		post.setRequestBody(xmlRequest);
		System.out.println("Request:\n"+xmlRequest);
		post.setRequestHeader("Content-type","application/x-www-form-urlencoded");
		HttpClient httpclient = new HttpClient();
		result = httpclient.executeMethod(post);
		System.out.println("Response status code: " + result);
		String response=post.getResponseBodyAsString();
		System.out.println(response);
		
		XPathFactory xpathFactory = XPathFactory.newInstance();
		DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
		DocumentBuilder builder=factory.newDocumentBuilder();
		Document doc2=builder.parse(new ByteArrayInputStream(response.getBytes("UTF-8")));
		XPath xpath=XPathFactory.newInstance().newXPath();
		String node1=(String)xpath.compile("*//ERROR_CODE").evaluate(doc2,XPathConstants.STRING);
		//node1.
		//System.out.println("Length:"+node1.length());
		
		
		


	}

}
