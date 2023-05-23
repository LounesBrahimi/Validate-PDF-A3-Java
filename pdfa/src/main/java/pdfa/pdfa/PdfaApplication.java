package pdfa.pdfa;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.common.PDMetadata;

@SpringBootApplication
public class PdfaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PdfaApplication.class, args);
		try {
			PDDocument document =  PDDocument.load(new File(args[0]));
			PDDocumentCatalog catalog = document.getDocumentCatalog();
			PDMetadata metadata = catalog.getMetadata();
			String xmlString =  new String( metadata.toByteArray(), "ISO-8859-1" );
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document1 = builder.parse(new InputSource(new StringReader(xmlString)));
			Element rootElement = document1.getDocumentElement();
			NodeList itemElements = rootElement.getElementsByTagName("pdfaid:part");
			if (itemElements != null) {
				Element itemElement = (Element) itemElements.item(0);
				NodeList childNodes = itemElement.getChildNodes();
				itemElement.getNodeValue();
				System.out.println("Your file is a ===> PDF/A"+childNodes.item(0).getNodeValue());
			} else {
				itemElements = rootElement.getElementsByTagName("rdf:Description");
				for (int indx= 0; indx < itemElements.getLength(); indx++) {
					Element eElement = (Element) itemElements.item(indx);
					if (eElement.hasAttribute("pdfaid:part")) {
						System.out.println("Your file is a ===> PDF/A"+eElement.getAttribute("pdfaid:part"));
						break;
					}
				}
			}
		} catch (IOException  | ParserConfigurationException | SAXException e) {
			e.printStackTrace();
		}
	}
}