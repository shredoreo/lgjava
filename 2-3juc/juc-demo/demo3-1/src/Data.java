public class Data {
    private float myFloat;

    public void modify(float inc) {
        float value = this.myFloat;
        System.out.println(Thread.currentThread().getName() +"before - "+ value);

        this.myFloat+=inc;
        System.out.println(Thread.currentThread().getName() +"after - "+ myFloat);

    }

    public static void main(String[] args) {
        Data data = new Data();
        new MyThread(data).start();
        new MyThread(data).start();
        new MyThread(data).start();
    }
}
