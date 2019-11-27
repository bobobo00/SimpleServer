package server;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
/**
 * 处理器
 * 
 * @author 
 *
 */


public class Handler extends DefaultHandler{
	private List<Entity> entitys;
	private List<Mapping> mappings;
	private Entity entity;
	private Mapping mapping;
	private String tag;//存储操作标签
	private boolean isMapping;
	
	
	public void startDocument() throws SAXException {
		entitys=new ArrayList<>();
		mappings=new ArrayList<>();
	}
	
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if(null!=qName) {
			tag=qName;//存储标签名
			if(tag.equals("servlet")) {
				entity=new Entity();
			}
			if(tag.equals("servlet-mapping")) {
				mapping=new Mapping();
				isMapping=true;
			}
		}
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException {
		String contents = new String(ch,start,length).trim();
		if(null!=tag) {//处理了空
			if(!isMapping) { //操作servlet-mapping
				if(tag.equals("servlet-name")) {
					entity.setName(contents);
				}else if(tag.equals("servlet-class")) {
					entity.setClz(contents);
				}
			}else { //操作servlet
				if(tag.equals("servlet-name")) {
					mapping.setName(contents);
				}else if(tag.equals("url-pattern")) {
					mapping.addPattern(contents);
				}
			}
		}
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if(null!=qName) {
			if(qName.equals("servlet")) {
				entitys.add(entity);
			}else if(qName.equals("servlet-mapping")) {
				mappings.add(mapping);
				isMapping=false;
			}
		}
		tag=null;//tag丢弃了
	}
	public List<Entity> getEntitys() {
		return entitys;
	}

	public List<Mapping> getMappings() {
		return mappings;
	}


}
