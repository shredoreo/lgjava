package com.shred.proxy.staticProxy;

public class Test {
    public static void main(String[] args) {
        IRentingHouse rentingHosut = new RentingHosutImpl();
        RentingHouserProxy rentingHouserProxy   = new RentingHouserProxy(rentingHosut);
        rentingHouserProxy.rentHouse();
    }
}
