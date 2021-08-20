package com.shred.rmi.service;

import com.shred.rmi.pojo.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IUserService extends Remote {
    User getUserById(int id) throws RemoteException;
}
