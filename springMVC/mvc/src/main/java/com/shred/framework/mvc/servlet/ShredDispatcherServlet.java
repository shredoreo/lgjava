package com.shred.framework.mvc.servlet;

import com.shred.framework.mvc.annotations.ShredAutowired;
import com.shred.framework.mvc.annotations.ShredController;
import com.shred.framework.mvc.annotations.ShredRequestMapping;
import com.shred.framework.mvc.annotations.ShredService;
import com.shred.framework.mvc.pojo.Handler;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShredDispatcherServlet extends HttpServlet {

    private static final String CONTEXT_CONFIG_LOCATION = "contextConfigLocation";

    private Properties properties = new Properties();
    private ArrayList<String> classNames = new ArrayList<>();

    /**
     * IOC
     *
     * @param config
     * @throws ServletException
     */
    private Map<String, Object> ioc = new HashMap<>();

    private ArrayList<Handler> handlerMapping = new ArrayList<>();


    @Override
    public void init(ServletConfig config) {
        //1 加载配置文件 springmvc.properties
        String contextConfigLocation = config.getInitParameter(CONTEXT_CONFIG_LOCATION);
        doLoadConfig(contextConfigLocation);

        //2 扫描相关类 扫描注解
        doScan(properties.getProperty("scanPackage"));

        // 3 初始化bean对象
        doInstance();

        // 4 实现依赖注入
        doAutowired();

        //5 狗狗在HandlerMapping 处理器映射器
        initHandlerMapping();

        System.out.println("mvc 初始化完成。");

        //6 等待请求进入，处理请求
    }

    private void doInstance() {
        try {
            doInstance1();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doLoadConfig(String contextConfigLocation) {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(contextConfigLocation);
        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initHandlerMapping() {
        if (ioc.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Class<?> aClass = entry.getValue().getClass();

            if (!aClass.isAnnotationPresent(ShredController.class)) {
                continue;
            }

            String baseUrl = "";

            if (aClass.isAnnotationPresent(ShredRequestMapping.class)) {
                baseUrl = aClass.getAnnotation(ShredRequestMapping.class).value();
            }

            Method[] methods = aClass.getMethods();
            for (Method method : methods) {
                if (!method.isAnnotationPresent(ShredRequestMapping.class)) {
                    continue;
                }

                ShredRequestMapping annotation = method.getAnnotation(ShredRequestMapping.class);
                String methodURL = annotation.value();

                String url = baseUrl + methodURL;

                Handler handler = new Handler(
                        entry.getValue(),
                        method,
                        Pattern.compile(url)
                );

                Parameter[] allParameters = method.getParameters();
                for (int i = 0; i < allParameters.length; i++) {
                    Parameter parameter = allParameters[i];

                    if (parameter.getType() == HttpServletRequest.class ||
                            parameter.getType() == HttpServletResponse.class) {
                        //对 http servlet 对象做特殊处理
                        handler.getParamIndexMapping().put(parameter.getType().getSimpleName(), i);
                    } else {
                        // 普通类型保存其 参数名称
                        handler.getParamIndexMapping().put(parameter.getName(), i);
                    }
                }

                // 建立url 和method之间的映射
                handlerMapping.add(handler);
            }

        }

    }

    private void doAutowired() {
        if (ioc.isEmpty()) {
            return;
        }

        //便利对象，查看字段中是否有Autowired注解
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Field[] declaredFields = entry.getValue().getClass().getDeclaredFields();
            for (int i = 0; i < declaredFields.length; i++) {
                Field field = declaredFields[i];

                if (!field.isAnnotationPresent(ShredAutowired.class)) {
                    continue;
                }

                ShredAutowired annotation = field.getAnnotation(ShredAutowired.class);
                String dependOnBean = annotation.value();

                if (StringUtils.isBlank(dependOnBean)) {
                    //空，使用接口注入
                    dependOnBean = field.getType().getName();
                }

                //赋值
                field.setAccessible(true);
                try {
                    field.set(entry.getValue(), ioc.get(dependOnBean));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }

    }

    //ioc容器
    // 基于classNames缓存的类的全限定类名，反射技术完成对象的创建
    private void doInstance1() throws Exception {

        if (classNames.size() == 0) {
            return;
        }

        for (int i = 0; i < classNames.size(); i++) {
            //全类名
            String className = classNames.get(i);

            Class<?> aClass = Class.forName(className);
            //区分controller 和Service
            if (aClass.isAnnotationPresent(ShredController.class)) {
                //使用首字母小写的类名作为id 放入ioc中
                String simpleName = aClass.getSimpleName();
                String id = lowerFirst(simpleName);

                Object o = aClass.newInstance();

                ioc.put(id, o);
            } else if (aClass.isAnnotationPresent(ShredService.class)) {
                ShredService annotation = aClass.getAnnotation(ShredService.class);
                String value = annotation.value();
                if (StringUtils.isBlank(value)) {
                    value = lowerFirst(aClass.getSimpleName());
                }
                ioc.put(value, aClass.getNestHost());
            } else {
                continue;
            }

            Class<?>[] interfaces = aClass.getInterfaces();
            for (int j = 0; j < interfaces.length; j++) {
                Class<?> anInterface = interfaces[j];
                //以接口 的全类名作为id放入bean中
                ioc.put(anInterface.getName(), aClass.newInstance());
            }

        }
    }

    private String lowerFirst(String simpleName) {
        char[] chars = simpleName.toCharArray();
        if ('A' <= chars[0] && chars[0] <= 'Z') {
            chars[0] += 32;
        }
        return String.valueOf(chars);
    }

    private void doScan(String scanPage) {
        String scanPackagePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + scanPage.replaceAll("\\.", "/");
        File pack = new File(scanPackagePath);
        File[] files = pack.listFiles();

        for (File file : files) {
            // 子包
            if (file.isDirectory()) {
                doScan(scanPage + "." + file.getName());
            } else if (file.getName().endsWith(".class")) {
                String className = scanPage + "." + file.getName().replaceAll(".class", "");
                classNames.add(className);
            }
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //处理请求
        Handler handler = getHandler(req);

        if (handler == null ){
            resp.getWriter().write("404 not found");
            return;
        }

        //参数绑定
        // 获取所有参数类型的数组
        Class<?>[] parameterTypes = handler.getMethod().getParameterTypes();
        //根据数组长度创建一个新的数组，参数数组
        Object[] paraValues = new Object[parameterTypes.length];

        // 向handler参数数组塞值，且保证参数顺序

        Map<String, Integer> handlerParamIndexMapping = handler.getParamIndexMapping();
        // 获取req参数
        Map<String, String[]> parameterMap = req.getParameterMap();
        parameterMap.forEach((k,v) ->{
            // 处理参数 如 name=1&name=2
            String val = StringUtils.join(v, ",");//name = 1,2
            if (!handlerParamIndexMapping.containsKey(k)){return;}

            //handle方法中参数的位置
            Integer index = handlerParamIndexMapping.get(k);// name 位置是 2

            paraValues[index] = val;
        });

        Integer reqIdx = handlerParamIndexMapping.get(HttpServletRequest.class.getSimpleName());
        paraValues[reqIdx] = req;
        Integer resIdx = handlerParamIndexMapping.get(HttpServletResponse.class.getSimpleName());
        paraValues[resIdx] = resp;

        try {
            //执行handler方法
            handler.getMethod().invoke(handler.getController(), paraValues);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private Handler getHandler(HttpServletRequest req) {
        if (handlerMapping.isEmpty()){return null;}

        String url = req.getRequestURI();
        for (Handler handler : handlerMapping) {
            Matcher matcher = handler.getPattern().matcher(url);
            if (matcher.matches()){
                return handler;
            }
        }
        return null;
    }
}
