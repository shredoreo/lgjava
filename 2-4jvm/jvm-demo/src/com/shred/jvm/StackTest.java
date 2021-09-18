package com.shred.jvm;

//mac default 8168
//-Xss256k   count=1598
public class StackTest {
    static long count = 0 ;
    public static void main(String[] args) {
        count++;
        System.out.println(count); //8467
        main(args);
} }