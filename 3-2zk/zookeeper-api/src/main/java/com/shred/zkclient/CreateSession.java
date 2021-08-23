package com.shred.zkclient;

import org.I0Itec.zkclient.ZkClient;

public class CreateSession {
    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient("tx1:2181");
        System.out.println("会话创建了。。");

    }
}
