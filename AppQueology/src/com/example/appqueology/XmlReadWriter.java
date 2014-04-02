package com.example.appqueology;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



import android.graphics.Color;
import android.util.Log;
import android.widget.RelativeLayout;

public class XmlReadWriter {
	
	RelativeLayout Rel;
	
	public XmlReadWriter(RelativeLayout Rel) {
		// TODO Auto-generated constructor stub
		this.Rel = Rel;
	}
	
	public void readXML(String xml){
        Rel.removeAllViews();
		int maxId = 0;
		try {
        	File xmlFile = new File(xml);  
        	DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();  
        	DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();  
        	Document doc = documentBuilder.parse(xmlFile);  
        	doc.getDocumentElement().normalize();
        	NodeList nodeList = doc.getElementsByTagName("node");
      
        	for (int temp = 0; temp < nodeList.getLength(); temp++) {  
        		Node xmlItem = nodeList.item(temp);
        		if (xmlItem.getNodeType() == Node.ELEMENT_NODE) {  
        			Element node = (Element) xmlItem;
        			Artifact newNode = new Artifact(Rel.getContext());
        			Rel.addView(newNode,100,100);
        			newNode.setId(Integer.parseInt(node.getElementsByTagName("id").item(0).getTextContent()));
        			newNode.setPrevWidth(100);
        			newNode.setPrevHeight(100);
        			newNode.setBackgroundColor(Color.BLACK);
        			newNode.setTag("node");
        			newNode.setX(Float.parseFloat(node.getElementsByTagName("X").item(0).getTextContent()));
        			newNode.setY(Float.parseFloat(node.getElementsByTagName("Y").item(0).getTextContent()));
        			newNode.setText(node.getElementsByTagName("label").item(0).getTextContent());
        			
        			
        			
        			if(Integer.parseInt(node.getElementsByTagName("id").item(0).getTextContent())>maxId){
        				maxId = Integer.parseInt(node.getElementsByTagName("id").item(0).getTextContent());
        			}
        		}  
        		
        	}
        	OnDragArtifact.recalculateLines(Rel);
            
            
        }catch(Exception e){
        	Log.v("error reading xml",e.toString());
        }
		Global.ID = maxId;
	}
	
	public void writeXML(String xml){
		try {
			
			
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();  
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();  
			// define root elements  
			Document document = documentBuilder.newDocument();  
			Element rootElement = document.createElement("graph");  
			document.appendChild(rootElement);
			
			for(int i=0;i<Rel.getChildCount();i++){
				if(Rel.getChildAt(i).getTag() != null){
					if(Rel.getChildAt(i).getTag().toString().compareTo("node") == 0){
						Artifact artifact = (Artifact) Rel.getChildAt(i);
						Element node = addElement(rootElement, "node", document);
						Element id = addAttribute("id",artifact.getId()+"", document);  
						node.appendChild(id);
						if(artifact.getFather() == null){
							Element nofather = addAttribute("father", "null", document);
							node.appendChild(nofather);
						}else{
							Element father = addAttribute("father", artifact.getFather().getId()+"", document);
							node.appendChild(father);
						}
						
						ArrayList <Artifact> sons = artifact.getSons();
						if(sons != null){
							addElement(node, "sons", document);
							for(int j=0;j<sons.size();j++){
								Element son = addAttribute("son",sons.get(j).getId()+"", document);
								node.appendChild(son);
							}
						}
						
						Element label = addAttribute("label", artifact.getText()+"", document);
						node.appendChild(label);
						
						Element Xpos = addAttribute("X", artifact.getX()+"", document);
						node.appendChild(Xpos);
						
						Element Ypos = addAttribute("Y", artifact.getY()+"", document);
						node.appendChild(Ypos);

					}
				}
			}
			
			
			// creating and writing to xml file  
			TransformerFactory transformerFactory = TransformerFactory.newInstance();  
			Transformer transformer = transformerFactory.newTransformer();  
			DOMSource domSource = new DOMSource(document);  
			StreamResult streamResult = new StreamResult(new File(xml));  
			transformer.transform(domSource, streamResult);
            
            
        }catch(Exception e){
        	Log.v("error writing xml",e.toString());
        }
		
		
	}
	
	public Element addElement(Element rootElement,String type,Document document){
		// define school elements  
		Element node = document.createElement(type);  
		rootElement.appendChild(node);
		return node;
	}
	
	public Element addAttribute(String name,String value,Document document){
		Element attr = document.createElement(name);  
		attr.appendChild(document.createTextNode(value));
		return attr;
	}

}