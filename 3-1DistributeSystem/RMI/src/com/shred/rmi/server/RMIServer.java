package com.shred.rmi.server;

import com.shred.rmi.service.UserServiceImpl;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
    public static void main(String[] args) throws RemoteException {

        Registry registry = LocateRegistry.createRegistry(9998);
        UserServiceImpl userService = new UserServiceImpl();
        registry.rebind("userService", userService);
        System.out.println("RMI服务器启动成功！=====");
    }
}
