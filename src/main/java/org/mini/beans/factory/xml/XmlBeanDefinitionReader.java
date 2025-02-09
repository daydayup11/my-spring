package org.mini.beans.factory.xml;

import org.dom4j.Element;
import org.mini.beans.factory.config.*;
import org.mini.beans.factory.support.AbstractBeanFactory;
import org.mini.beans.factory.support.SimpleBeanFactory;
import org.mini.core.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * 读取xml配置文件
 */
public class XmlBeanDefinitionReader {
    AbstractBeanFactory beanFactory;
    public XmlBeanDefinitionReader(AbstractBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
    public void loadBeanDefinitions(Resource res) {
        while (res.hasNext()) {
            Element element = (Element)res.next();
            String beanID=element.attributeValue("id");
            String beanClassName=element.attributeValue("class");

            BeanDefinition beanDefinition=new BeanDefinition(beanID,beanClassName);

            //get constructor
            List<Element> constructorElements = element.elements("constructor-arg");
            ConstructorArgumentValues AVS = new ConstructorArgumentValues();
            for (Element e : constructorElements) {
                String pType = e.attributeValue("type");
                String pName = e.attributeValue("name");
                String pValue = e.attributeValue("value");
                AVS.addArgumentValue(new ConstructorArgumentValue(pValue, pType, pName));
            }
            beanDefinition.setConstructorArgumentValues(AVS);
            //end of handle constructor

            //handle properties
            List<Element> propertyElements = element.elements("property");
            PropertyValues PVS = new PropertyValues();
            List<String> refs = new ArrayList<>();
            for (Element e : propertyElements) {
                String pType = e.attributeValue("type");
                String pName = e.attributeValue("name");
                String pValue = e.attributeValue("value");
                String pRef = e.attributeValue("ref");
                String pV = "";
                boolean isRef = false;
                if (pValue != null && !pValue.equals("")) {
                    isRef = false;
                    pV = pValue;
                } else if (pRef != null && !pRef.equals("")) {
                    isRef = true;
                    pV = pRef;
                    refs.add(pRef);
                }
                PVS.addPropertyValue(new PropertyValue(pType, pName, pV, isRef));
            }
            beanDefinition.setPropertyValues(PVS);
            String[] refArray = refs.toArray(new String[0]);
            beanDefinition.setDependsOn(refArray);
            //end of handle properties

            this.beanFactory.registerBeanDefinition(beanID,beanDefinition);
        }
    }
}