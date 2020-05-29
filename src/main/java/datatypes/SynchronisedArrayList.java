package datatypes;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class SynchronisedArrayList<T> {
    private int maxSize;
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private ArrayList<T> arrayList = new ArrayList<>();

    public SynchronisedArrayList(int maxSize){
        this.maxSize = maxSize;
    }

    public void add(T item){
        lock.lock();
        while(arrayList.size() == maxSize){
            await();
        }
        arrayList.add(item);
        condition.signal();
        lock.unlock();
    }

    public T get(int index){
        lock.lock();
        while(arrayList.size()-1 < index){
            await();
        }
        T item = arrayList.get(index);
        condition.signal();
        lock.unlock();
        return item;
    }

    public T remove(int index){
        lock.lock();
        while(arrayList.size()-1 < index){
            await();
        }
        T item = arrayList.remove(index);
        condition.signal();
        lock.unlock();
        return item;
    }

    public int size(){
        lock.lock();
        int size = arrayList.size();
        lock.unlock();
        return size;
    }

    private void await(){
        try{
            condition.await();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String toString(){
        StringBuffer sync = new StringBuffer();
        for(T item : arrayList){
            sync.append(item);
            sync.append("\n");
        }
        return sync.toString();
    }
}
