public class GeneralHash {
    public static void main(String[] args) {
        // 定义客户端ip
        String[] clients = new String[]
                {"10.78.12.3","113.25.63.1","126.12.3.8"};
        // server count
        int serverCount = 3;

        // 根据index锁定应该路由到到tomcat服务器
        for (String client:clients){
            int hash = Math.abs(client.hashCode());
            int index = hash % serverCount;
            System.out.println("客户端："+client + " 被路由到服务器:"+ index);


        }
    }
}
