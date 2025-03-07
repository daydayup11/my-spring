
package org.mini.batis;


import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.mini.beans.factory.annotation.Autowired;
import org.mini.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DefaultSqlSessionFactory implements SqlSessionFactory{
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	private DataSource readDataSource;
	@Autowired
	private DataSource writeDataSource;
	
	String mapperLocations;

	Map<String,MapperNode> mapperNodeMap = new HashMap<>();

	public DefaultSqlSessionFactory() {
	}

	public void init() {
	    scanLocation(this.mapperLocations);
	    for (Map.Entry<String, MapperNode> entry : this.mapperNodeMap.entrySet()) {
	    	System.out.println(entry.getKey() + " : " + entry.getValue());
	    }
	}

	@Override
	public SqlSession openSession() {
		DefaultSqlSession newSqlSession = new DefaultSqlSession();
		newSqlSession.setJdbcTemplate(jdbcTemplate);
		newSqlSession.setSqlSessionFactory(this);
		newSqlSession.setReadDataSource(readDataSource);
		newSqlSession.setWriteDataSource(writeDataSource);
		return newSqlSession;
	}

	
    private void scanLocation(String location) {
    	String sLocationPath = this.getClass().getClassLoader().getResource("").getPath()+location;
        //URL url  =this.getClass().getClassLoader().getResource("/"+location);
    	System.out.println("mapper location : "+sLocationPath);
        File dir = new File(sLocationPath);
        for (File file : dir.listFiles()) {
            if(file.isDirectory()){
            	scanLocation(location+"/"+file.getName());
            }else{
                buildMapperNodes(location+"/"+file.getName());
            }
        }
    }

	private Map<String, MapperNode> buildMapperNodes(String filePath) {
		System.out.println(filePath);
        SAXReader saxReader=new SAXReader();
        URL xmlPath=this.getClass().getClassLoader().getResource(filePath);
        try {
			Document document = saxReader.read(xmlPath);
			Element rootElement=document.getRootElement();

			String namespace = rootElement.attributeValue("namespace");

	        Iterator<Element> nodes = rootElement.elementIterator();;
	        while (nodes.hasNext()) {
	        	Element node = nodes.next();
	            String id = node.attributeValue("id");
	            String parameterType = node.attributeValue("parameterType");
	            String resultType = node.attributeValue("resultType");
				String sqlType = node.attributeValue("sqlType");
				String sql = node.getText();

	            MapperNode selectnode = new MapperNode();
	            selectnode.setNamespace(namespace);
	            selectnode.setId(id);
	            selectnode.setParameterType(parameterType);
	            selectnode.setResultType(resultType);
				selectnode.setSqlType(sqlType);
	            selectnode.setSql(sql);
	            selectnode.setParameter("");
	                
	            this.mapperNodeMap.put(namespace + "." + id, selectnode);
	        }
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	    return this.mapperNodeMap;
	}

	public MapperNode getMapperNode(String name) {
		return this.mapperNodeMap.get(name);
	}
	public Map<String, MapperNode> getMapperNodeMap() {
		return mapperNodeMap;
	}

	public String getMapperLocations() {
		return mapperLocations;
	}
	public void setMapperLocations(String mapperLocations) {
		this.mapperLocations = mapperLocations;
	}

}
