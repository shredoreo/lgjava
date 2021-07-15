package com.shred.spring.factory;

import com.shred.spring.exception.AmbiguousBeanException;
import com.shred.spring.exception.BeanNoDefException;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 工厂类，生成bean对象（使用反射技术）
 */
public class BeanFactory {

    /**
     * 1、解析xml，通过反射实例化并保存（存入Map）
     * 2、对外提供获取实例的方法（key：id）
     */
    private static Map<String, Object> map = new HashMap<>();
    /**
     * 精确的类型 对应的bean
     */
    private static Map<Class<?>, Object> typeMap = new HashMap<>();

    /**
     * 类型 对应的beanName
     */
    private static Map<Class<?>, Set<String>> beanNamesByType = new HashMap<>();

    /**
     * 扫描到的的beanType
     */
    private static Set<String> registeredBeanTypes = new HashSet<>();

//    private static Set<Class>

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
                typeMap.put(aClass, o);
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
                    if (method.getName().equalsIgnoreCase("set" + name)) {
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

    public static Object getBean(String id) throws BeanNoDefException {
        return Optional.ofNullable(
                map.get(id)
        ).orElseThrow(
                () -> new BeanNoDefException("找不到对应的bean" + id)
        );
    }

    public static <T> T getBean(Class<T> type) {
        return (T) typeMap.get(type);
    }


    public static Object getBeanByClass(Class<?> type) {
        Object t = typeMap.get(type);
        if (t == null) {
            Class<?>[] interfaces = type.getInterfaces();
            for (Class<?> anInterface : interfaces) {
                Object beanByClass = getBeanByClass(anInterface);
                if (beanByClass != null) {
                    return beanByClass;
                }
            }
        }

        return t;
    }

    public static void putBean(String id, Object bean) {
        map.put(id, bean);
    }

    public static void putBean(Class<?> type, Object bean) {
        String beanName = type.getName();
        //首字母小写
        beanName = beanName.substring(0, 1).toLowerCase(Locale.ROOT).concat(beanName.substring(1));

        //原本的类型
        Class<?>[] interfaces = type.getInterfaces();
        beanNamesByType.computeIfAbsent(type, key -> new HashSet<>())
                .add(beanName);

        //实现接口
        for (Class<?> anInterface : interfaces) {
            beanNamesByType.computeIfAbsent(anInterface, key -> new HashSet<>())
                    .add(beanName);
        }

        map.put(beanName, bean);
        typeMap.put(type, bean);

    }

    public static Set<String> getBeanNamesByType(Class<?> type) {
        return beanNamesByType.get(type);
    }

    /**
     * 找到类型对应的bean
     *
     * @param type
     * @return
     * @throws BeanNoDefException
     * @throws AmbiguousBeanException
     */
    public static Object findBeanByType(Class<?> type) throws BeanNoDefException, AmbiguousBeanException {
        return findBean(null, type);
    }

    public static boolean checkExist(Class<?> type) {
        return getBeanNamesByType(type) != null;
    }

    /**
     * 找到类型对应的bean
     *
     * @param beanName
     * @param type
     * @return
     * @throws BeanNoDefException
     * @throws AmbiguousBeanException
     */
    public static Object findBean(String beanName, Class<?> type) throws BeanNoDefException, AmbiguousBeanException {
        //制定了id，直接获取
        if (StringUtils.isNotBlank(beanName)) {
            return getBean(beanName);
        }

        Set<String> beanNamesByType = getBeanNamesByType(type);
        // 找不到
        if (CollectionUtils.isEmpty(beanNamesByType)) {
            throw new BeanNoDefException("找不到类型对应的bean:" + type.getName());
        }

        // 找到过多的
        if (beanNamesByType.size() > 1) {
            throw new AmbiguousBeanException("存在多个bean " + type.getName());
        }

        // 匹配到一个，用beanName获取
        return getBean(beanNamesByType.iterator().next());

    }


    public void registerType(Class<?>type){
        registeredBeanTypes
    }

}
