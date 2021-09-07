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
import java.util.stream.Collectors;

@Activate(group = {CommonConstants.CONSUMER})
public class TPMonitorFilter implements Filter, Runnable {
    /**
     * 存储方法执行得相关信息（方法名称，方法执行耗时，方法执行结束时间）
     */
    Map<String, List<MethodInfo>> methodTimes = new ConcurrentHashMap<>();

    public TPMonitorFilter() {
        System.out.println("创建定时任务");
        //创建定时任务
        // 创建定时线程，每隔5s打印一次最近1分钟内每个方法的TP90、TP99的耗时情况
        Executors.newSingleThreadScheduledExecutor()
                .scheduleWithFixedDelay(this, 5,5, TimeUnit.SECONDS);
    }


    @Override
    public void run() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateStr = sdf.format(date);
        System.out.println("=================线程使用情况=================");
        /**
         * 线程使用情况
         */
        for (Map.Entry<String, List<MethodInfo>> methodInfos : methodTimes.entrySet()) {
            System.out.println(dateStr + methodInfos.getKey() + "的TP90:" + getTP(methodInfos.getValue(), 0.9) + "毫秒,"
                    + "TP99:" + getTP(methodInfos.getValue(), 0.99) + "毫秒");
        }
        System.out.println("=======================================");
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

        // 计算最近一分钟的TP90 和 TP99
        long endTime = System.currentTimeMillis();
        long startTime = System.currentTimeMillis() - 60000;

        List<MethodInfo> collect = methodInfos.stream()
                //过滤出近一分钟内的
                .filter(methodInfo ->
                        methodInfo.getEndTimes() >= startTime && methodInfo.getEndTimes() <= endTime)
                //排序
                .sorted(Comparator.comparingLong(MethodInfo::getTimes))
                .collect(Collectors.toList());

        //获取当前排序后集合中的指定百分比数值的位置，此位置存储的数据就是当前计算的tp90/99
        int index = (int) (collect.size() * rate);

        return collect.get(index).getTimes();
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
