package DataTypes;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

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
        return arrayList.size();
    }

    private void await(){
        try{
            condition.await();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
