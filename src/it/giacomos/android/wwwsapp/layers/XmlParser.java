package it.giacomos.android.wwwsapp.layers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.util.Log;

public class XmlParser 
{
	public ArrayList<LayerItemData> parseLayerList(String xml)
	{
		ArrayList<LayerItemData> list = new ArrayList<LayerItemData>();
		Document dom;
		DocumentBuilderFactory factory;
		DocumentBuilder builder;
		InputStream is;
		LayerItemData ld = new LayerItemData();
		factory = DocumentBuilderFactory.newInstance();
		try {
			builder = factory.newDocumentBuilder();
			try 
			{
				is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
				try 
				{
					dom = builder.parse(is);
					NodeList layerNodes = dom.getElementsByTagName("layers");
					if(layerNodes.getLength() == 1)
					{
						NodeList layers = dom.getElementsByTagName("layer");
						for(int i = 0; i < layers.getLength(); i++)
						{
							Element layer = (Element) layers.item(i);
							ld.name = layer.getAttribute("name");
							try{
								ld.version = Float.parseFloat(layer.getAttribute("version"));
							}
							catch(NumberFormatException e)
							{
								Log.e("XmlParser.parseLayer NumberFormatException", "invalid float " + layer.getAttribute("version"));
								ld.version = -1;
							}
						}	
					}
				}
				catch (SAXException e) 
				{
					Log.e("XmlParser.parseLayer SAXException: decode()", e.getLocalizedMessage());
				} 
				catch (IOException e) 
				{	
					Log.e("XmlParser.parseLayer: decode()", e.getLocalizedMessage());
				}
			} 
			catch (UnsupportedEncodingException e) 
			{
				Log.e("XmlParser.parseLayer: decode()", e.getLocalizedMessage());
			}
		} 
		catch (ParserConfigurationException e1) 
		{
			Log.e("XmlParser.parseLayer: decode()", e1.getLocalizedMessage());
		}	
		return list;
	}

	public LayerItemData parseLayer(String xml)
	{		
		Document dom;
		DocumentBuilderFactory factory;
		DocumentBuilder builder;
		InputStream is;
		LayerItemData ld = new LayerItemData();
		factory = DocumentBuilderFactory.newInstance();
		try {
			builder = factory.newDocumentBuilder();
			try 
			{
				is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
				try 
				{
					dom = builder.parse(is);
					NodeList layerNodes = dom.getElementsByTagName("layer");
					if(layerNodes.getLength() == 1)
					{
						Node layerNode = layerNodes.item(0);
						NodeList children = layerNode.getChildNodes();
						for(int i = 0; i < children.getLength(); i++)
						{
							Element child = (Element) children.item(i);
							if(child.getTagName().compareTo("name") == 0 && child.getNodeType() == Node.TEXT_NODE)
								ld.name = child.getNodeValue();
							else if(child.getTagName().compareTo("title") == 0 && child.getNodeType() == Node.TEXT_NODE)
								ld.title = child.getNodeValue();
							else if(child.getTagName().compareTo("author") == 0 && child.getNodeType() == Node.TEXT_NODE)
								ld.author = child.getNodeValue();
							else if(child.getTagName().compareTo("date") == 0 && child.getNodeType() == Node.TEXT_NODE)
								ld.date = child.getNodeValue();
							else if(child.getTagName().compareTo("title") == 0 && child.getNodeType() == Node.TEXT_NODE)
								ld.date = child.getNodeValue();
							else if(child.getTagName().compareTo("description") == 0 && 
									child.getAttribute("length").compareTo("long") == 0 && 
									child.getNodeType() == Node.TEXT_NODE)
								ld.long_desc = child.getNodeValue();
							else if(child.getTagName().compareTo("description") == 0 && 
									child.getNodeType() == Node.TEXT_NODE)
								ld.short_desc = child.getNodeValue();
						}
					}
				} 
				catch (SAXException e) 
				{
					Log.e("XmlParser.parseLayer SAXException: decode()", e.getLocalizedMessage());
				} 
				catch (IOException e) 
				{	
					Log.e("XmlParser.parseLayer: decode()", e.getLocalizedMessage());
				}
			} 
			catch (UnsupportedEncodingException e) 
			{
				Log.e("XmlParser.parseLayer: decode()", e.getLocalizedMessage());
			}
		} 
		catch (ParserConfigurationException e1) 
		{
			Log.e("XmlParser.parseLayer: decode()", e1.getLocalizedMessage());
		}	

		return ld;
	}


}
