package com.shred.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.nio.charset.StandardCharsets;

public class UseCurator {

    private static CuratorFramework client;
    private static String path = "/shred-curator-ppp/c1";
    private static String base = "base";

    public static void main(String[] args) throws Exception {

        //构建
        client = CuratorFrameworkFactory.builder().connectString("tx1:2181") //server地址
                .sessionTimeoutMs(5000) // 会话超时时间
                .connectionTimeoutMs(3000) // 连接超时时间
                .retryPolicy(new ExponentialBackoffRetry(1000, 5)) //重试策略
                .namespace(base) //使用了namespace， 那么节点都是在namespace下操作的
                .build();
        client.start();//连接
        System.out.println("Zookeeper session established. ");

        testCreate();

        testDelete();

        testGet();
    }

    /**
     * 测试创建
     */
    private static void testCreate() throws Exception {
        client.create().creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .forPath(path, "init".getBytes(StandardCharsets.UTF_8));

        Thread.sleep(1000);

        System.out.println("成功创建节点: " + base + path);
    }

    /**
     * 测试读取、更新
     */
    private static void testGet() throws Exception {
        Stat stat = new Stat();
        byte[] bytes = client.getData().storingStatIn(stat).forPath(path);
        System.out.println("获取节点的数据：" + new String(bytes));

        System.out.println("状态信息：" + stat);
        System.out.println("更新前的version：" + stat.getVersion());

        //更新
        Stat updatedStat = client.setData().withVersion(stat.getVersion()).forPath(path);
        System.out.println("更新后的version：" + updatedStat.getVersion());

        //尝试更新旧版本，会报错：
        // org.apache.zookeeper.KeeperException$BadVersionException: KeeperErrorCode = BadVersion for
        client.setData().withVersion(stat.getVersion()).forPath(path).getVersion();
    }

    /**
     * 测试删除
     */
    private static void testDelete() throws Exception {
        //删除节点
        client.delete().forPath(path);

        //递归删除节点及其子节点
        client.delete().deletingChildrenIfNeeded().forPath(path);

        //删除指定版本
        client.delete().withVersion(1).forPath(path);

        //保证强制删除节点
        client.delete().guaranteed().forPath(path);
        // 只要客户端会话有效，那么Curator会在后台持续进行删除操作，直到节点删除成功。比如遇到一些网 络异常的情况,
        // 此guaranteed的强制删除就会很有效果。
    }

}
