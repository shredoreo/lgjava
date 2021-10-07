import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Phaser;

public class MyThread extends Thread{
    private final Phaser phaser;
    private Random random = new Random();


    public MyThread(String name,Phaser phaser) {
        super(name);
        this.phaser = phaser;
    }

    @Override
    public void run() {

        try {
            System.out.println(Thread.currentThread().getName()+" - 开始考数学");
            Thread.sleep(random.nextInt(2000));
            System.out.println(Thread.currentThread().getName()+" - 数学考完啦");
            phaser.arriveAndAwaitAdvance();

            System.out.println(Thread.currentThread().getName()+" - 开始考语文");
            Thread.sleep(random.nextInt(2000));
            System.out.println(Thread.currentThread().getName()+" - 语文考完啦");
            phaser.arriveAndAwaitAdvance();

            System.out.println(Thread.currentThread().getName()+" - 开始考英语");
            Thread.sleep(random.nextInt(2000));
            System.out.println(Thread.currentThread().getName()+" - 英语考完啦");
            phaser.arriveAndAwaitAdvance();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
