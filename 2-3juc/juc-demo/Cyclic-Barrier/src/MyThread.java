import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class MyThread extends Thread{

    private final CyclicBarrier barrier;
    private final Random random = new Random();


    public MyThread(String name,CyclicBarrier barrier) {
        super(name);
        this.barrier = barrier;
    }

    @Override
    public void run() {

        try {
            System.out.println(Thread.currentThread().getName()+" - 开始考数学");
            Thread.sleep(random.nextInt(2000));
            System.out.println(Thread.currentThread().getName()+" - 数学考完啦");
            barrier.await();

            System.out.println(Thread.currentThread().getName()+" - 开始考语文");
            Thread.sleep(random.nextInt(2000));
            System.out.println(Thread.currentThread().getName()+" - 语文考完啦");
            barrier.await();

            System.out.println(Thread.currentThread().getName()+" - 开始考英语");
            Thread.sleep(random.nextInt(2000));
            System.out.println(Thread.currentThread().getName()+" - 英语考完啦");
            barrier.await();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}
