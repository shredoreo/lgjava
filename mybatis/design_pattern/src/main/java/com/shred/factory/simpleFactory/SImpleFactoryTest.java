package com.shred.factory.simpleFactory;

public class SImpleFactoryTest {
    public static void main(String[] args) {
        Computer lenovo = ComputerFactory.createComputer("lenovo");

        lenovo.start();

        //根据需要从简单工厂获取对应的实例
        ComputerFactory.createComputer("hp").start();
    }
}
