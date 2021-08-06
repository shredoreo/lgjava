package com.shred.minicat;

import com.shred.minicat.server.HttpProtocolUtil;
import com.shred.minicat.server.Request;
import com.shred.minicat.server.Response;
import com.shred.minicat.server.servlet.HttpServlet;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

public class BootStrap {
    private int port = 8080;
    private HashMap<String, HttpServlet> servletMap = new HashMap<>();

    /**
     * 初始化展开的操作
     */
    public void start() throws Exception {
        loadServlet();

        // v1.0 需求：请求http://localhost:8080/ 返回固定的字符串
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("===>>>Minicat start on port:"+ port);
       /* while (true){
            //阻塞，知道获取到socket
            Socket socket = serverSocket.accept();
            OutputStream outputStream = socket.getOutputStream();
            String meg = "hello mini cat";
            String responseText = HttpProtocolUtil.getHttpHeader200(meg.getBytes(StandardCharsets.UTF_8).length) + meg;
            outputStream.write(responseText.getBytes(StandardCharsets.UTF_8));
            socket.close();
        }*/

        /**
         * v2 请求静态资源
         */
       /* while (true){
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());
            response.outputHtml(request.getUrl());

            socket.close();
        }*/

        /**
         * v3 请求动态资源：servlet
         */
        while (true){
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            Request request = new Request(inputStream);
            Response response = new Response(socket.getOutputStream());
            String url = request.getUrl();

            // 处理请求
            HttpServlet httpServlet = servletMap.get(url);
            if (httpServlet == null){
                response.outputHtml(url);
            } else {
                httpServlet.service(request,response);
            }

            socket.close();
        }

    }

    /**
     * 加载解析web.xml 初始化servlet
     */
    private void loadServlet() {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("web.xml");
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


    public static void main(String[] args) throws Exception {
        new BootStrap().start();
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
