package com.shred.proxy.staticProxy;

public class RentingHouserProxy implements IRentingHouse{
    private IRentingHouse rentingHouse;

    public RentingHouserProxy(IRentingHouse rentingHouse){
        this.rentingHouse = rentingHouse;
    }

    @Override
    public void rentHouse() {
        System.out.println("中介 收费300");
        rentingHouse.rentHouse();
        System.out.println("中介走人");
    }
}
