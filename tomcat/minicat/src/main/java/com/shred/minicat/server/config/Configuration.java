package com.shred.minicat.server.config;

import com.shred.minicat.server.servlet.HttpServlet;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Configuration {
    private int port;
    private List<String> portList = new ArrayList<>();


    private void loadConfig() {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("server.xml");
        SAXReader saxReader = new SAXReader();

        try {
            Document document = saxReader.read(resourceAsStream);
            Element rootElement = document.getRootElement();
            List<Element> serviceList = rootElement.selectNodes("//service");
            for (int i = 0; i < serviceList.size(); i++) {
                Element serviceElm = serviceList.get(i);

                //connectors
                List<Element> connectorList = serviceElm.selectNodes("//connector");
                // 处理连接器
                resolveConnectors(connectorList);

                //engines
                Element engineElm = (Element) serviceElm.selectSingleNode("//engine");
                resolveEngine(engineElm);


            }

        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

    private void resolveEngine(Element engineElm) {
        List<Element> hostList = engineElm.selectNodes("//host");

        resolveHosts(hostList);

    }

    private void resolveHosts(List<Element> hostList) {
        for (Element host : hostList) {
            MappedHost mappedHost = new MappedHost();
            String name = host.attributeValue("name");
            String appBase = host.attributeValue("appBase");
            mappedHost.setName(name);
            mappedHost.setAppBase(appBase);

            List<Element> contextList = host.selectNodes("//context");
            resolveContexts(contextList, mappedHost);

        }

    }

    private void resolveContexts(List<Element> contextList, MappedHost mappedHost) {
        if (contextList == null || contextList.size() == 0) {
            String appBase = mappedHost.getAppBase();
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(appBase);
            File file = new File(appBase);
            // webapp文件夹
            File[] files = file.listFiles();
            for (File file1 : files) {
                String appName = file1.getName();
                File[] webXmls = file1.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.equalsIgnoreCase("web.xml");
                    }
                });
                //目录下存在web.xml,读取第一个
                if (webXmls.length>0){
                    File webXml = webXmls[0];
                    WebAppLoader webAppLoader = new WebAppLoader();
                    try {
                        //加载并解析web.xml
                        webAppLoader.loadServlet(new FileInputStream(webXml));
                        HashMap<String, HttpServlet> servletMap = webAppLoader.getServletMap();
                        // 创建context对象
                        MappedContext mappedContext = new MappedContext();
                        mappedContext.setAppPath(appName);
                        mappedContext.setServletMap(servletMap);

                        //将context加入到host中
                        mappedHost.getContextList().add(mappedContext);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
            readWebXml(file);
        }

        for (Element context : contextList) {


        }

    }

    private void readWebXml(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File file1 : files) {
                readWebXml(file1);
            }
        } else {
            String name = file.getName();
            if (name.equalsIgnoreCase("web.xml")) {

            }
        }
    }

    private void resolveConnectors(List<Element> connectorList) {

        for (Element element : connectorList) {
            String port = element.attributeValue("port");
            //保存端口号
            portList.add(port);
        }

    }
}
