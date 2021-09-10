import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MyBlockingQueue<E> {
    int size;
    ReentrantLock lock = new ReentrantLock();

    LinkedList<E> list = new LinkedList<>();

    Condition notFull = lock.newCondition();
    Condition notEmpty = lock.newCondition();

    public MyBlockingQueue(int size) {
        this.size = size;
    }

    public void enqueue(E e) throws InterruptedException {
        lock.lock();
        try {
            while (list.size() == size) {
                // 等待非满的信号
                notFull.await();
            }
            list.offer(e);
            System.out.println("入队 "+e);
            // 发送非空信号
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public E dequeue() throws InterruptedException {
        E e;
        lock.lock();
        try {
            while (list.size() == 0){
                // 等待非空 信号
                notEmpty.await();
            }
            e= list.pollFirst();
            System.out.println("出队 "+e);
            //非满 这个条件 发送信号
            notFull.signal();
            return e;
        } finally {
            lock.unlock();
        }
    }
}
