package org.jdom;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import org.jdom.input.SAXBuilder;

public class XmlParser {

	// Parser 부분
	public Document xmlParser(InputStream in)
	{
		SAXBuilder builder = new SAXBuilder();
		try 
		{
			Document doc = builder.build(in);
			Element root = doc.getRootElement();
			return doc;
		}
		catch (JDOMException e) {
			e.printStackTrace();
		    return null;
		} 
		catch (IOException e) {

				e.printStackTrace();
				return null;
			}
		}
	public Document xmlParser(String in)
	{
		SAXBuilder builder = new SAXBuilder();
	//	InputSource input = new InputSource(new StringReader(in));
		Reader input = new StringReader(in);
		try {
			Document doc = builder.build(input);
			Element root = doc.getRootElement();
			return doc;
		}
		catch (JDOMException e) {
			e.printStackTrace();
		    return null;
		} 
		catch (IOException e) {

				e.printStackTrace();
				return null;
			}
		}
	
}
