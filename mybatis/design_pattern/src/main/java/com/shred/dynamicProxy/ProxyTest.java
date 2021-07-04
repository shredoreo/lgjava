package com.shred.dynamicProxy;

public class ProxyTest {
    public static void main(String[] args) {
        System.out.println("no proxy");
        Person bod = new Bod();
        bod.doSomeThing();

        System.out.println("000000");
        System.out.println("use proxy");
        Person jdkDynamicProxy =  (Person) new JDKDynamicProxy(new Bod()).getTarget();
        jdkDynamicProxy.doSomeThing();


    }
}
