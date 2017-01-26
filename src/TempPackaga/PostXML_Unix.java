package TempPackaga;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.w3c.dom.Document;

import org.w3c.dom.NodeList;


public class PostXML_Unix {



@SuppressWarnings("deprecation")
public static void main(String[] args) throws ParserConfigurationException, TransformerException {
    String strURL = "http://192.168.151.90:6530/PURCHASE_FREE_SIM_WITH_CREDIT/";
    String strXMLString = "<ENVELOPE><HEADER><TRANSACTION_ID>88UuYfhW10071512117</TRANSACTION_ID><ENTITY>ITGSIM:11:CBOSAUTO</ENTITY><CHANNEL_REFERENCE>CCARE</CHANNEL_REFERENCE></HEADER><BODY><PURCHASE_FREE_SIM_WITH_CREDIT_REQUEST><DETAILS><TITLE>Miss</TITLE><FIRST_NAME>Anu</FIRST_NAME><LAST_NAME>Priya</LAST_NAME><DATE_OF_BIRTH>10/05/1991</DATE_OF_BIRTH><EMAIL_ADDRESS>yogendrareddy.mc@plintron.com</EMAIL_ADDRESS><CONTACT_NUMBER>8888800001</CONTACT_NUMBER><TYPE_OF_PERMIT>Passport</TYPE_OF_PERMIT><ID_NUMBER>45644787</ID_NUMBER><NATIONALITY>Indian</NATIONALITY><NO_OF_SIM>1</NO_OF_SIM><LANGUAGE>English</LANGUAGE><HEAR_ABOUT_US>Web</HEAR_ABOUT_US><CALL_MOST_COUNTRY>India</CALL_MOST_COUNTRY><CARD_TYPE>VI</CARD_TYPE><NAME_ON_CARD>test</NAME_ON_CARD><CARD_NO>4444333322221111</CARD_NO><ISSUE_DATE></ISSUE_DATE><EXPIRY_DATE>042019</EXPIRY_DATE><ISSUE_NO></ISSUE_NO><CVV>123</CVV><TOP_UP_AMOUNT>10</TOP_UP_AMOUNT><PROMO_CODE></PROMO_CODE><SIM_TYPE>Micro</SIM_TYPE><ADDRESS><POST_CODE>36024</POST_CODE><STREET>st</STREET><CITY>ct</CITY><COUNTRY>US</COUNTRY><HOUSE_NO>12</HOUSE_NO><COUNTY>hi</COUNTY><HOUSENO_EXTN></HOUSENO_EXTN></ADDRESS><BILLING_ADDRESS><POST_CODE>20910</POST_CODE><STREET>fgd</STREET><CITY>sdfgh</CITY><COUNTRY>gg</COUNTRY><HOUSE_NO>2</HOUSE_NO><COUNTY>gfh</COUNTY><HOUSENO_EXTN></HOUSENO_EXTN></BILLING_ADDRESS><PAYMENT_AMOUNT>20</PAYMENT_AMOUNT><WP_PAYMENT><USER_AGENT_HEADER>dghhd</USER_AGENT_HEADER><ACCEPT_HEADER>dfhjdfj</ACCEPT_HEADER><SESSION_ID>fgjhj</SESSION_ID></WP_PAYMENT><IPADDRESS></IPADDRESS><CARD_NICK_NAME></CARD_NICK_NAME><CARD_ID></CARD_ID><TAX></TAX><TAX_ID></TAX_ID></DETAILS></PURCHASE_FREE_SIM_WITH_CREDIT_REQUEST></BODY></ENVELOPE>";
    
    PostMethod post = new PostMethod(strURL);
    try {
    	DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
    	Document document=null;
    	factory.setNamespaceAware(true);
    	DocumentBuilder dbuilder=factory.newDocumentBuilder();
    	StringBuilder xmlBuilder=new StringBuilder();
    	xmlBuilder.append(strXMLString);
    	ByteArrayInputStream is;
    	try{
    		is=new ByteArrayInputStream(xmlBuilder.toString().getBytes("UTF-8"));
    		document=dbuilder.parse(is);
    		
    		NodeList root=document.getDocumentElement().getElementsByTagName("TRANSACTION_ID");
    		System.out.println("GetNodeValue:"+root.item(0).getTextContent());
    		root.item(0).setTextContent("helloText");
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    	
    	StringWriter sw = new StringWriter();
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
    	
        transformer.transform(new DOMSource(document), new StreamResult(sw));
    	String xml=sw.toString();
    	
    	
    	System.out.println("xml:"+xml);
    	post.setRequestBody(xml);
    	//post.setRequestHeader("Content-type","application/x-www-form-urlencoded");
        HttpClient httpclient = new HttpClient();
        System.out.println("kk");
        int result = httpclient.executeMethod(post);
        System.out.println("Response status code: " + result);
        System.out.println("Response body: ");
        System.out.println(post.getResponseBodyAsString());
        
        
        
        
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        post.releaseConnection();
    }
 }

}