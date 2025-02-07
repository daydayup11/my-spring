package org.mini.beans;

import org.dom4j.Element;

/**
 * 读取xml配置文件
 */
public class XmlBeanDefinitionReader {
    BeanFactory beanFactory;
    public XmlBeanDefinitionReader(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
    public void loadBeanDefinitions(Resource resource) {
        while (resource.hasNext()) {
            Element element = (Element) resource.next();
            String beanID = element.attributeValue("id");
            String beanClassName = element.attributeValue("class");
            BeanDefinition beanDefinition = new BeanDefinition(beanID, beanClassName);
            this.beanFactory.registerBeanDefinition(beanDefinition);
        }
    }
}