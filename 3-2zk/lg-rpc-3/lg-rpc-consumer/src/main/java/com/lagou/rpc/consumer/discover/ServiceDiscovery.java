package com.lagou.rpc.consumer.discover;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class ServiceDiscovery implements IDiscover {

    int count;

    JSONObject serviceMap;

    public JSONObject getServiceMap() {
        return serviceMap;
    }

    public void setServiceMap(JSONObject serviceMap) {
        this.serviceMap = serviceMap;
    }


    @Override
    public String discover(String serviceName) {
        System.out.println("查找服务:" + serviceName);

        return loadBalance(serviceName);
    }

    /**
     * 负载均衡
     *
     * @param serviceName
     * @return
     */
    private String loadBalance(String serviceName) {

        JSONArray addressArr = serviceMap.getJSONArray(serviceName);

        //简单的 负载均衡算法
        int i = count++ % addressArr.size();

        System.out.println("负载均衡：" + i);

        return addressArr.getString(i);
    }

}
