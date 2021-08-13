import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistenHashNoVirtual {
    public static void main(String[] args) {
        //1 初始化 将服务器节点对应到hash环
        // 定义服务器ip
        String[] tomcatServers =new String[]
                {"123.111.0.0","123.101.3.1","111.20.35.2","123.98.26.3"};

        SortedMap<Integer, String> hashServerMap = new TreeMap<>();
        //2 客户端ip求出hash
        for (String tomcatServer : tomcatServers) {
            int abs = Math.abs(tomcatServer.hashCode());
            hashServerMap.put(abs, tomcatServer);
        }
        System.out.println(hashServerMap);

        //3 针对客户端找到能够处理当前客户端请求到服务器
        String[] clients = new String[]
                {"10.78.12.3","113.25.63.1","126.12.3.8"};
        for (String client : clients) {
            int clientHash = Math.abs(client.hashCode());
            //根据客户端的hash值，找到服务器节点
            SortedMap<Integer, String> integerStringSortedMap = hashServerMap.tailMap(clientHash);
            Integer integer;
            if (integerStringSortedMap.isEmpty()){
                // 取hash环上的第一台服务器
                integer = hashServerMap.firstKey();
            } else {
                integer = integerStringSortedMap.firstKey();
            }
            System.out.println("====>客户端："+ client+ " hash：\t"+ clientHash +"===>服务端:"+ hashServerMap.get(integer));

        }

    }

}
