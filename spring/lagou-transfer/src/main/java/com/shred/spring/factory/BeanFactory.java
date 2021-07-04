package com.shred.spring.factory;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工厂类，生成bean对象（使用反射技术）
 */
public class BeanFactory {

    /**
     * 1、解析xml，通过反射实例化并保存（存入Map）
     * 2、对外提供获取实例的方法（key：id）
     */
    private static Map<String,Object> map = new HashMap<>();
    
    static {
        // 1、解析xml，通过反射实例化并保存（存入Map）
        InputStream resourceAsStream = BeanFactory.class.getClassLoader().getResourceAsStream("beans.xml");
        // parse xml
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();

            List<Element> nodes = rootElement.selectNodes("//bean");
            for (int i = 0; i < nodes.size(); i++) {
                Element element = nodes.get(i);
                String id = element.attributeValue("id");
                String type = element.attributeValue("class");

                //通过反射创建对象
                Class<?> aClass = Class.forName(type);
                Object o = aClass.newInstance();

                //保存
                map.put(id, o);
            }

            //实例化之后维护对象的依赖关系
            List<Element> propertyList = rootElement.selectNodes("//property");

            for (int i = 0; i < propertyList.size(); i++) {
                Element element = propertyList.get(i);

                String name = element.attributeValue("name");
                String ref = element.attributeValue("ref");

                //父元素就是需要被处理的bean
                Element parent = element.getParent();
                String parentId = parent.attributeValue("id");
                Object parentObject = map.get(parentId);

                Method[] methods = parentObject.getClass().getMethods();

                for (int j = 0; j < methods.length; j++) {
                    Method method = methods[j];
                    //找到对应的setter方法
                    if (method.getName().equalsIgnoreCase("set"+ name)){
                        method.invoke(parentObject, map.get(ref));
                    }
                }
                //把处理后的parentObj重新放入map中
                map.put(parentId, parentObject);
            }


        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    public static Object getBean(String id){
        return map.get(id);
    }

}
