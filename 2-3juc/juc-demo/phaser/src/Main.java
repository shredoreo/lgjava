import java.util.Random;
import java.util.concurrent.Phaser;

public class Main {
    public static void main(String[] args) {
        Phaser phaser = new Phaser(5);

        for (int i = 0; i < 5; i++) {
            new Thread("线程"+(i+1)){
                private final Random random = new Random();
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName()+" - 开始允许");
                    try {
                        Thread.sleep(random.nextInt(1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName()+" - finished");
                    phaser.arrive();

                }
            }.start();
        }
        System.out.println("线程启动完毕");
//        System.out.println(phaser.getPhase());
//        phaser.awaitAdvance(phaser.getPhase());
        phaser.awaitAdvance(0);//阻塞直到phase=0
        System.out.println("线程运行结束");

    }
}
