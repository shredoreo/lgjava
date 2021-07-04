package com.shred.proxy.dynamicProxy;

public class CglibProxy {
    public static void main(String[] args) {
        IRentingHouse rentingHouse = new RentingHouseImpl();

        RentingHouseImpl rentingHouse1 = (RentingHouseImpl) ProxyFactory.getInstance().getCglibProxy(rentingHouse);

        rentingHouse1.rentHouse();

    }
}
