package com.shred.constructor;

public class ComputerBuilder {

    private Computer computer = new Computer();

    public void installMonitor() {
        computer.setMonitor("显示器");
    }

    public void installCpu() {
        computer.setCpu("CPU");
    }

    public void installMouse() {
        computer.setMouse("鼠标");
    }

    public void installKeyboard() {
        computer.setKeyboard("键盘");
    }


    public Computer getComputer(){
        return computer;
    }
}
