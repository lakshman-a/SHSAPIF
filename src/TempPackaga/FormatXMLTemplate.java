package TempPackaga;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;



public class FormatXMLTemplate {
	static Document doc = null;
	static XPathFactory xpathfactory=null;
	static XPath xpath=null;

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		@SuppressWarnings("unused")
		int result=0;

		String FilePath="\\Resources\\Support_Files\\";
		String FileName="XML_Template_Input_Output_Data.xls";
		String strQuery="SELECT Input_XML FROM XML_Template Where Test_Case_Name = 'ESP_UPDATE_SUBSCRIBER_001'";
//		String strQuery="SELECT Input_XML FROM XML_Template Where Test_Case_Name = 'PFS_WITH_CREDIT_001'";
//		String strURL="http://192.168.151.90:6530/PURCHASE_FREE_SIM_WITH_CREDIT/";
		
		String xmlRequest=TempFile.RetrieveValueFromTestDataBasedOnQuery(FilePath, FileName, "Input_XML", strQuery);
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		StringReader strReader = new StringReader(xmlRequest);
		
		
		InputSource is = new InputSource(strReader);
		doc = (Document) builder.parse(is);
		
		//xpathfactory = XPathFactory.newInstance();
		xpath = XPathFactory.newInstance().newXPath();
		//factory=DocumentBuilderFactory.newInstance();
		//builder=factory.newDocumentBuilder();
		//Document doc1=builder.parse(new ByteArrayInputStream(gbstrXMLResponseData.getBytes("UTF-8")));
		
		
		NodeList root=(NodeList)xpath.compile("*//TRANSACTION_ID").evaluate(doc, XPathConstants.NODESET);
		System.out.println("Length:"+root.getLength());
		root.item(0).setTextContent("hellomurali");
		
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(doc), new StreamResult(writer));
		String output = writer.getBuffer().toString();
		System.out.println(output);
		
		//node1.
		//System.out.println("Length:"+node1.length());
		
		
		


	}

}
