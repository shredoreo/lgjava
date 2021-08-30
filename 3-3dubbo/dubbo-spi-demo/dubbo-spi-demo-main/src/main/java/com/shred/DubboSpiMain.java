package com.shred;

import com.shred.service.HelloService;
import org.apache.dubbo.common.extension.ExtensionLoader;

import java.util.Set;

public class DubboSpiMain {

    public static void main(String[] args) {
        //获取扩展加载器
        ExtensionLoader<HelloService> extensionLoader = ExtensionLoader.getExtensionLoader(HelloService.class);

        // 便利所有支持的扩展点
        Set<String> extensions = extensionLoader.getSupportedExtensions();

        System.out.println(extensions);

        for (String extension : extensions) {
            String s = extensionLoader.getExtension(extension).sayHello();
            System.out.println(s);
        }

    }
}
