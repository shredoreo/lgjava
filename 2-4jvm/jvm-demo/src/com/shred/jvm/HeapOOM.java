package com.shred.jvm;

import java.util.ArrayList;
import java.util.List;

/**
* 设置最大堆最小堆: -Xms20m -Xmx20m
 **/
public class HeapOOM {
    static class OOMObject {
    }
    public static void main(String[] args) {
        List<OOMObject> oomObjectList = new ArrayList<>();
        while (true) {
            oomObjectList.add(new OOMObject());
        }
} }