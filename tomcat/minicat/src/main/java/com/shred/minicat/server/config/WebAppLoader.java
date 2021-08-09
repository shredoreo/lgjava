package com.shred.minicat.server.config;

import com.shred.minicat.server.servlet.HttpServlet;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

public class WebAppLoader {

    private HashMap<String, HttpServlet> servletMap = new HashMap<>();

    public HashMap<String, HttpServlet> getServletMap() {
        return servletMap;
    }

    public void setServletMap(HashMap<String, HttpServlet> servletMap) {
        this.servletMap = servletMap;
    }

    /**
     * 加载解析web.xml 初始化servlet
     */
    public void loadServlet( InputStream resourceAsStream ) {
//        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("web.xml");
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();

            List<Element> list = rootElement.selectNodes("//servlet");
            for (int i = 0; i < list.size(); i++) {
                /*
    <servlet>
        <servlet-name>shred</servlet-name>
        <servlet-class>com.shred.minicat.server.servlet.ShredServlet</servlet-class>
    </servlet>

    <servlet-mapping>
        <servlet-name>shred</servlet-name>
        <url-pattern>/shred</url-pattern>
    </servlet-mapping>
                 */
                Element element = list.get(i);
                Element servletNameElm = (Element) element.selectSingleNode("servlet-name");
                String servletName = servletNameElm.getStringValue();

                Element servletClassElm = (Element) element.selectSingleNode("servlet-class");
                String servletClass = servletClassElm.getStringValue();

                Element servletMappingElm = (Element) rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + servletName + "']");
                String urlPattern = servletMappingElm.selectSingleNode("url-pattern").getStringValue();

                servletMap.put(urlPattern, (HttpServlet) Class.forName(servletClass).newInstance());
            }

        } catch (DocumentException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
