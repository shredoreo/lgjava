public class MyThread extends Thread{
    private final Data data;


    public MyThread(Data data) {

        this.data = data;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            data.modify(1);

        }
    }
}
