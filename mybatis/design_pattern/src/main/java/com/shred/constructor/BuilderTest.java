package com.shred.constructor;

public class BuilderTest {

    public static void main(String[] args) {
        ComputerBuilder computerBuilder = new ComputerBuilder();
        computerBuilder.installMonitor();
        computerBuilder.installMouse();
        computerBuilder.installCpu();
        computerBuilder.installKeyboard();
        Computer computer = computerBuilder.getComputer();

        System.out.println(computer);
    }

}
