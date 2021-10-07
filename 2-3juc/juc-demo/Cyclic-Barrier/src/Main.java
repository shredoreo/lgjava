import java.util.concurrent.CyclicBarrier;

public class Main   {
    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5,()->{
            System.out.println("阶段结束");
        });
        for (int i = 0; i < 5; i++) {
            new MyThread("学生"+(i+1) , cyclicBarrier).start();
        }
    }
}
