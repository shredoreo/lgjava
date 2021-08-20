package com.shred.rmi.client;

import com.shred.rmi.pojo.User;
import com.shred.rmi.service.IUserService;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {
    public static void main(String[] args) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 9998);
        IUserService userService = (IUserService) registry.lookup("userService");
        User userById = userService.getUserById(1);

        System.out.println(userById.getId() + "-----" +userById.getName());
    }
}
