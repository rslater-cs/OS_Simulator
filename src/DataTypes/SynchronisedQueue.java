package DataTypes;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class SynchronisedQueue<T> {
    private Queue<T> queue = new LinkedList<>();
    private ReentrantLock lock =  new ReentrantLock();
    private Condition condition = lock.newCondition();
    private int maxQueueSize;

    public SynchronisedQueue(int maxQueueSize){
        if(maxQueueSize < 1) throw new IllegalArgumentException("Queue size must be more than zero");
        this.maxQueueSize = maxQueueSize;
    }

    public T remove(){
        lock.lock();
        while(queue.size() < 1){
            await();
        }
        T result = queue.remove();
        condition.signal();
        lock.unlock();
        return result;
    }

    public void add(T item){
        lock.lock();
        while(queue.size() == maxQueueSize){
            await();
        }
        queue.add(item);
        condition.signal();
        lock.unlock();
    }

    private void await(){
        try {
            condition.await();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
