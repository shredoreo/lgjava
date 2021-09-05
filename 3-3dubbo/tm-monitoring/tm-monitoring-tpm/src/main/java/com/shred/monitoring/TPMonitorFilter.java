package com.shred.monitoring;

import com.shred.monitoring.model.MethodInfo;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Activate(group = {CommonConstants.CONSUMER})
public class TPMonitorFilter implements Filter, Runnable {
    /**
     * 存储方法执行得相关信息（方法名称，方法执行耗时，方法执行结束时间）
     */
    Map<String, List<MethodInfo>> methodTimes = new ConcurrentHashMap<>();

    public TPMonitorFilter() {
        // 创建定时线程，每隔5s打印一次最近1分钟内每个方法的TP90、TP99的耗时情况
        Executors.newSingleThreadScheduledExecutor()
                .scheduleWithFixedDelay(this, 5, 5, TimeUnit.SECONDS);
    }


    @Override
    public void run() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateStr = sdf.format(date);
        /**
         * 线程使用情况
         */
        for (Map.Entry<String, List<MethodInfo>> methodInfos : methodTimes.entrySet()) {
            System.out.println(dateStr + methodInfos.getKey() + "的TP90:" + getTP(methodInfos.getValue(), 0.9) + "毫秒,"
                    + "TP99:" + getTP(methodInfos.getValue(), 0.99) + "毫秒");
        }
    }


    /**
     * 后续会每隔5s调用一次此方法，打印最近1分钟内每个方法的TP90、TP99的耗时情况
     * <p>
     * 计算tp90和tp99
     *
     * @param methodInfos
     * @param rate        代表百分比 90 传入 0.9 即可  99 就是 0.99
     * @return
     */
    private long getTP(List<MethodInfo> methodInfos, double rate) {
        // 构建一个临时集合保存 用于满足1一分钟之内的数据
        List<MethodInfo> sortInfo = new ArrayList<>();
        // 计算最近一分钟的TP90 和 TP99
        long endTime = System.currentTimeMillis();
        long startTime = System.currentTimeMillis() - 60000;

        // 遍历列表集合
        int length = methodInfos.size();
        for (MethodInfo methodInfo : methodInfos) {
            if (methodInfo.getEndTimes() >= startTime && methodInfo.getEndTimes() <= endTime) {
                //将满足条件的方法信息存储到临时集合中
                sortInfo.add(methodInfo);
            }
        }

        //对满足1一分钟之内的数据进行排序
        sortInfo.sort(Comparator.comparingLong(MethodInfo::getTimes));

        //获取当前排序后集合中的指定百分比数值的位置，此位置存储的数据就是当前计算的tp90/99
        int index = (int) (sortInfo.size() * rate);

        return sortInfo.get(index).getTimes();
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        long l = System.currentTimeMillis();
        Result res = invoker.invoke(invocation);

        long costTime = System.currentTimeMillis() - l;
        String methodName = invocation.getMethodName();
        System.out.printf("%s 耗时：%s 毫秒%n", methodName, costTime);
        methodTimes.computeIfAbsent(methodName, k -> new ArrayList<>())
                .add(
                        new MethodInfo(invocation.getMethodName(), costTime, System.currentTimeMillis())
                );

        return res;
    }
}
