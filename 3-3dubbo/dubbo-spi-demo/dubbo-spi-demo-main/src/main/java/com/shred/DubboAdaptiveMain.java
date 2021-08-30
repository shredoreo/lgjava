package com.shred;

import com.shred.service.HelloService;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.ExtensionLoader;

import java.util.List;

public class DubboAdaptiveMain {
    public static void main(String[] args) {

        //hello.service helloService会将大写弄成.
        URL url = URL.valueOf("test://localhost/hello?hello.service=dog");
        URL url2 = URL.valueOf("test://localhost/hello?hello.service=human");
        //未指定，使用@SPI("dog") 默认实现 dog
        URL url3 = URL.valueOf("test://localhost/hello");

        HelloService adaptiveExtension = ExtensionLoader.getExtensionLoader(HelloService.class).getAdaptiveExtension();

        String s = adaptiveExtension.sayHello(url);
        System.out.println(s);
        String s2 = adaptiveExtension.sayHello(url2);
        System.out.println(s2);

        String s3 = adaptiveExtension.sayHello(url3);
        System.out.println(s3);
    }
}
