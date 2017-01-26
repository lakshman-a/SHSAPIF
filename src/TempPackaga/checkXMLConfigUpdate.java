package TempPackaga;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathFactory;

//import org.dom4j.XPath;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;

import org.apache.commons.httpclient.URI;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import Property.*;

public class checkXMLConfigUpdate {

	public static void main(String[] args) throws Exception, IOException {
		// TODO Auto-generated method stub
		String webConfigPath="D:\\SELENIUM_DEVELOPMENT\\SELENIUM_CBOS_ITG_API\\responsexml.xml";
		DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
		DocumentBuilder dbuilder=factory.newDocumentBuilder();
		Document doc=dbuilder.parse(webConfigPath);
		doc.getDocumentElement().normalize();
		XPath xpath=XPathFactory.newInstance().newXPath();
		NodeList node=(NodeList)xpath.compile("*//ERROR_DESC").evaluate(doc,XPathConstants.NODESET);
		System.out.println("error code:"+node.item(0).getTextContent());
			

		}
		
	}
	
	


