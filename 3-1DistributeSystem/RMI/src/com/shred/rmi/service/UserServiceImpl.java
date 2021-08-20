package com.shred.rmi.service;

import com.shred.rmi.pojo.User;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class UserServiceImpl  extends UnicastRemoteObject implements IUserService{
    HashMap<Integer, User> integerUserHashMap;

    public UserServiceImpl() throws RemoteException {
        super();
        User user = new User();user.setId(1);
        user.setName("流");

        User user1 = new User();
        user1.setName("s桑拿房");
        user1.setId(2);

        integerUserHashMap = new HashMap<>();
        integerUserHashMap.put(user.getId(),user);

        integerUserHashMap.put(user1.getId(), user1);
    }

    @Override
    public User getUserById(int id) throws RemoteException{
        return integerUserHashMap.get(id);
    }
}
