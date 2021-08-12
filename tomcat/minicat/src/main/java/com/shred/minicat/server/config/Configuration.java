package com.shred.minicat.server.config;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Configuration {
    private static List<Integer> portList = new ArrayList<>();
    public static ArrayList<MappedHost> mappedHosts = new ArrayList<>();


    public List<Integer> getPortList() {
        return portList;
    }

    public void setPortList(List<Integer> portList) {
        this.portList = portList;
    }

    public ArrayList<MappedHost> getMappedHosts() {
        return mappedHosts;
    }

    public void setMappedHosts(ArrayList<MappedHost> mappedHosts) {
        this.mappedHosts = mappedHosts;
    }

    public void loadConfig() {
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
        //获取host节点
        List<Element> hostList = engineElm.selectNodes("//host");

        //处理host节点
        resolveHosts(hostList);

    }

    private void resolveHosts(List<Element> hostList) {
        for (Element host : hostList) {
            MappedHost mappedHost = new MappedHost();
            //主机名称
            String name = host.attributeValue("name");
            // app所在根目录
            String appBase = host.attributeValue("appBase");
            mappedHost.setName(name);
            mappedHost.setAppBase(appBase);

            // context节点
            List<Element> contextList = host.selectNodes("//context");
            resolveContexts(contextList, mappedHost);

            this.mappedHosts.add(mappedHost);
        }

    }

    private void resolveContexts(List<Element> contextList, MappedHost mappedHost) {
        if (contextList == null || contextList.size() == 0) {
            String appBase = mappedHost.getAppBase();
            InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(appBase);
            File file = new File(appBase);
            // webapp文件夹
            File[] files = file.listFiles(((dir, name) -> !name.startsWith(".")));
            for (File file1 : files) {
                //文件夹名称即为app名称
                String appName = file1.getName();
                File[] webXmls = file1.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.equalsIgnoreCase("web.xml");
                    }
                });
                //目录下存在web.xml,读取第一个
                if (webXmls!= null && webXmls.length>0){
                    File webXml = webXmls[0];
                    WebAppLoader webAppLoader = new WebAppLoader(webXml );
                    try {
                        //加载并解析web.xml
                        webAppLoader.loadServlet();
                        // 获取sMappedWrapper
                        //urlPattern ->Wrapper（servlet）
                        HashMap<String, Wrapper> mappedWrapper = webAppLoader.getMappedWrapper();

                        // 创建context对象
                        MappedContext mappedContext = new MappedContext();
                        mappedContext.setAppPath(appName);
                        mappedContext.setMappedWrapper(mappedWrapper);

                        //将context加入到host中
                        mappedHost.getContextList().add(mappedContext);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

       /* for (Element context : contextList) {


        }*/

    }


    private void resolveConnectors(List<Element> connectorList) {

        for (Element element : connectorList) {
            String port = element.attributeValue("port");
            //保存端口号
            portList.add(Integer.parseInt(port));
        }

    }
}
