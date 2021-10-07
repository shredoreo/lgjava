import java.util.concurrent.Phaser;

public class PhaserForBarrier {
    public static void main(String[] args) {
        Phaser phaser = new Phaser(5);

        for (int i = 0; i < 5; i++) {
            new MyThread("thread-"+(i+1), phaser).start();
        }

        phaser.awaitAdvance(0);
        System.out.println("main terminated");
    }
}
