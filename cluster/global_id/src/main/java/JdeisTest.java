import redis.clients.jedis.Jedis;

public class JdeisTest {
    public static void main(String[] args) {
        //tx1为本机配置的一个ip别名，指向云服务器
        Jedis jedis = new Jedis("tx1", 6379);
        Long id = jedis.incr("id");
        System.out.println(id);
    }
}
