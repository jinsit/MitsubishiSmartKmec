package org.jdom;


public class XmlMaker {
	/**
	 * RootElement 생성
	 * @param rootStr
	 * @return Document
	 */

	public Document Make(String rootStr)
	{
		Namespace namespace = Namespace.getNamespace("xsi","http://www.w3.org/2001/XMLSchema-instance"); 
		Namespace namespace1 = Namespace.getNamespace("xsd","http://www.w3.org/2001/XMLSchema"); 

		Element element = new Element(rootStr);
		element.addNamespaceDeclaration(namespace);
		element.addNamespaceDeclaration(namespace1);
		Document document = new Document(element);
		return document;
	}
	
	public Element addAttribute(Element targetElement, String key,String value)
	{
		Attribute childAttribute = new Attribute(key,value);
	    targetElement.setAttribute(childAttribute);
		return targetElement;
	}
	public Element addElement(Document doc,String NodeName)
	{
	//	Iterator<String> iter = data.keySet().iterator();
	//	while(iter.hasNext())
	//	{
		//	String key = iter.next();
			Element childElement = new Element(NodeName);
			//addAttribute(childElement,data);
			doc.getRootElement().addContent(childElement);
		//	try
		//	{
		//		if(data.get(key) instanceof LinkedHashMap)
			//	{
				//	addElement(childElement,(LinkedHashMap)data.get(key));
			//	}
			//	else
			//	{
				//	childElement.addContent((String)data.get(key));
			//	}
		//	}
		//	catch(Exception ex)
		//	{
				//String log = ex.getMessage();
		//		childElement.addContent(String.valueOf(data.get(key)));
		//	}
		//	targetElement.addContent(childElement);
	//	}
		
		
		return childElement;
	}
	public Element addElement(Element Element,String NodeName)
	{
		
			Element childElement = new Element(NodeName);
			
			Element.addContent(childElement);
			return childElement;
	}
}
