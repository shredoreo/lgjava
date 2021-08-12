package com.shred.minicat.server.config;

import com.shred.minicat.server.loader.WebAppClassLoader;
import com.shred.minicat.server.servlet.HttpServlet;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

public class WebAppLoader {

    private final File webXmlFile;
    private HashMap<String, Wrapper> mappedWrapper = new HashMap<String, Wrapper>();
    private WebAppClassLoader webAppClassLoader ;

    public WebAppLoader(File webXml) {
        this.webXmlFile = webXml;
        String parentDir = webXmlFile.getParent();
        //规定web.xml所在目录的server/文件夹为当前app的classpath
        String webAppClasspath = parentDir + "/"+"server/";
        this.webAppClassLoader = new WebAppClassLoader(webAppClasspath);
    }

    public HashMap<String, Wrapper> getMappedWrapper() {
        return mappedWrapper;
    }

    public void setMappedWrapper(HashMap<String, Wrapper> mappedWrapper) {
        this.mappedWrapper = mappedWrapper;
    }

    /**
     * 加载解析web.xml 初始化servlet
     */
    public void loadServlet( ) throws FileNotFoundException {

        InputStream resourceAsStream = new FileInputStream(webXmlFile);

        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();

            List<Element> list = rootElement.selectNodes("//servlet");
            for (int i = 0; i < list.size(); i++) {

                Element element = list.get(i);
                Element servletNameElm = (Element) element.selectSingleNode("servlet-name");
                String servletName = servletNameElm.getStringValue();

                Element servletClassElm = (Element) element.selectSingleNode("servlet-class");
                String servletClass = servletClassElm.getStringValue();

                //加载servlet
                Class<?> aClass = webAppClassLoader.loadClass(servletClass);

                Element servletMappingElm = (Element) rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + servletName + "']");
                String urlPattern = servletMappingElm.selectSingleNode("url-pattern").getStringValue();

                Wrapper wrapper = new Wrapper();
                //实例化servlet
                wrapper.setHttpServlet((HttpServlet) aClass.newInstance());
                //urlPattern ->servlet
                mappedWrapper.put(urlPattern, wrapper);
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
