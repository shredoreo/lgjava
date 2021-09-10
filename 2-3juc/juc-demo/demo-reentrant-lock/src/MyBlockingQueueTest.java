public class MyBlockingQueueTest {
    public static void main(String[] args) throws InterruptedException {

        MyBlockingQueue<Integer> queue = new MyBlockingQueue<>(2);
        for (int i = 0; i < 10; i++) {
            int data = i;
            new Thread(() -> {
                try {
                    queue.enqueue(data);
                } catch (InterruptedException e) {

                }
            }).start();

        }
        for(int i=0;i<10;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Integer data = queue.dequeue();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }
}
