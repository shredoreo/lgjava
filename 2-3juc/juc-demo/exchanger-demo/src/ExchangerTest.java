import java.util.Random;
import java.util.concurrent.Exchanger;

public class ExchangerTest {
    public static void main(String[] args) {
        Exchanger<String> exchanger = new Exchanger<>();
        Random random = new Random();
        assignExchangeThread(exchanger, random, "线程1");
        assignExchangeThread(exchanger, random, "线程2");
        assignExchangeThread(exchanger, random, "线程3");
    }

    private static void assignExchangeThread(Exchanger<String> exchanger, Random random, String name) {
        new Thread(()->{
            while (true){
                try {
                    String exchange = exchanger.exchange("交换数据" + Thread.currentThread().getName());
                    System.out.println(Thread.currentThread().getName()+" 得到《== "+ exchange);
                    Thread.sleep(random.nextInt(2000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, name).start();
    }
}
