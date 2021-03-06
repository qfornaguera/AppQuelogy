package com.example.appqueology;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

/**
 * 
 * @author Joaquim Fornaguera
 * 
 * This class contains functions for read and write a graph (xml file) in an specific Relative Layout
 *
 */

public class XmlReadWriter {
	
	private RelativeLayout Rel;
	/**
	 * Constructor
	 * 
	 * 
	 * @param Rel is the relative layout where the graph is written or read
	 */
	public XmlReadWriter(RelativeLayout Rel) {
		// TODO Auto-generated constructor stub
		this.Rel = Rel;
	}
	
	/**
	 * readXML
	 * 
	 * this method deploys a graph saved into an xml file on the relative layout
	 * 
	 * 
	 * @param xml the name of the file we read
	 */
	public void readXML(String xml){
		int maxId = 0;
		try {
        	File xmlFile = new File(xml);  
        	DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();  
        	DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();  
        	Document doc = documentBuilder.parse(xmlFile);  
        	doc.getDocumentElement().normalize();
        	NodeList nodeList = doc.getElementsByTagName("node");
        	HashMap<Integer,Artifact> artifactTable = new HashMap<Integer, Artifact>();
        	HashMap<Integer,Element> nodeTable = new HashMap<Integer, Element>();
      
        	for (int i = 0; i < nodeList.getLength(); i++) {  
        		Node xmlItem = nodeList.item(i);
        		if (xmlItem.getNodeType() == Node.ELEMENT_NODE) {  
        			Element node = (Element) xmlItem;
        			Artifact newNode = new Artifact(Rel.getContext());
        			Rel.addView(newNode,100,100);
        			newNode.setId(Integer.parseInt(node.getElementsByTagName("id").item(0).getTextContent()));
        			newNode.setPrevWidth(100);
        			newNode.setPrevHeight(100);
        			newNode.setTag("node");
        			newNode.setText(node.getElementsByTagName("label").item(0).getTextContent());
        			newNode.setType(node.getElementsByTagName("type").item(0).getTextContent());
        			newNode.setInformation(node.getElementsByTagName("information").item(0).getTextContent());
        			newNode.setPosition(node.getElementsByTagName("position").item(0).getTextContent());
        			newNode.setAge(Long.parseLong(node.getElementsByTagName("age").item(0).getTextContent()));
        			newNode.setBackgroundResource(Utility.getDrawableType(newNode));
        			if("".compareTo((String) newNode.getText()) != 0){
        				newNode.matchWithText();
        			}
        			artifactTable.put(newNode.getId(), newNode);
        			nodeTable.put(newNode.getId(), node);
        			if(Integer.parseInt(node.getElementsByTagName("id").item(0).getTextContent())>maxId){
        				maxId = Integer.parseInt(node.getElementsByTagName("id").item(0).getTextContent());
        			}
        		}  
        		
        	}
        	
        	//once we created all the artifacts now we can set the fathers and sons for every node with the artifact and xmlnode tables we have filled while reading
        	Iterator it = artifactTable.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry)it.next();
                NodeList sonsList = nodeTable.get(pairs.getKey()).getElementsByTagName("son");
                if(sonsList != null){
	                for (int j = 0; j < sonsList.getLength(); j++) {
	                	artifactTable.get(pairs.getKey()).addSon(artifactTable.get(Integer.parseInt(sonsList.item(j).getTextContent())));
	                }
                }
                
                NodeList fathersList = nodeTable.get(pairs.getKey()).getElementsByTagName("father");
                if(fathersList != null){
	                for (int j = 0; j < fathersList.getLength(); j++) {
	                	artifactTable.get(pairs.getKey()).addFather(artifactTable.get(Integer.parseInt(fathersList.item(j).getTextContent())));
	                }
                }
                
            }
            
            
        }catch(Exception e){
        	Log.v("error reading xml",e.toString());
        }
		Global.ID = maxId+1;
	}
	
	/**
	 * writeXML
	 * 
	 * this method save the graph done with artifacts on the relative layout into a xml file
	 * 
	 * we save in the name of the artifacts, their fathers, their sons in case of having it, and the age
	 * 
	 * 
	 * @param xml is the name of the file we write
	 */
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
						Element id = addAttribute("id",artifact.getId()+"", document);  //we create an attribute for a node
						node.appendChild(id);//and then we attach it to the node
						
						ArrayList <Artifact> fathers = artifact.getFathers();
						if(fathers != null){
							addElement(node, "fathers", document);//for complex attribute like array of fathers we add an element to the node
							for(int j=0;j<fathers.size();j++){
								Element father = addAttribute("father",fathers.get(j).getId()+"", document);//inside this element created in the node we add all its fathers as attributes
								node.appendChild(father);
							}
						}
						
						ArrayList <Artifact> sons = artifact.getSons();
						if(sons != null){
							addElement(node, "sons", document);//for complex attribute like array of sons we add an element to the node
							for(int j=0;j<sons.size();j++){
								Element son = addAttribute("son",sons.get(j).getId()+"", document);//inside this element created in the node we add all its sons as attributes
								node.appendChild(son);
							}
						}
						
						Element label = addAttribute("label", artifact.getText()+"", document);
						node.appendChild(label);
						
						Element age = addAttribute("age", artifact.getAge()+"", document);
						node.appendChild(age);
						
						Element type = addAttribute("type", artifact.getType()+"", document);
						node.appendChild(type);
						
						Element information = addAttribute("information", artifact.getInformation()+"", document);
						node.appendChild(information);
						
						Element position = addAttribute("position", artifact.getPosition()+"", document);
						node.appendChild(position);

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
	
	/**
	 * addElement 
	 * 
	 * this method appends an element inside the rooElement contained on a document returns the element appended
	 * 
	 * @param rootElement
	 * @param type
	 * @param document
	 * @return
	 */
	public Element addElement(Element rootElement,String type,Document document){
		// define school elements  
		Element node = document.createElement(type);  
		rootElement.appendChild(node);
		return node;
	}
	
	/**
	 * addAttribute
	 * 
	 * this method creates an attribute with its value and returns it
	 * 
	 * @param name
	 * @param value
	 * @param document
	 * @return
	 */
	public Element addAttribute(String name,String value,Document document){
		Element attr = document.createElement(name);  
		attr.appendChild(document.createTextNode(value));
		return attr;
	}

}
