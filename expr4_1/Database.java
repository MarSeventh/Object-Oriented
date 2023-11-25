import com.oocourse.TimableOutput;

import java.util.Map;
import java.util.HashMap;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Database<K,V> {
    private final Map<K,V> map = new HashMap<>();
    
    private final ReadWriteLock lock = new ReentrantReadWriteLock(true /* fair */);
    
    // 给key分配value
    public void insert(K key, V value) {
        //TODO
        //请替换sentence1为合适内容(1)
        lock.writeLock().lock();
        try {
            verySlowly();
            map.put(key, value);
            TimableOutput.println(Thread.currentThread().getName() + ":insert(" + key + ", " + value + ")");
        } finally {
            //TODO
            //请替换sentence2为合适内容(2)
            lock.writeLock().unlock();
        }
    }
    
    // 获取给key分配的值
    public V select(K key) {
        //TODO
        //请替换sentence3为合适内容(3)
        lock.readLock().lock();
        try {
            slowly();
            //TODO
            //请替换sentence4为合适内容(4)
            return map.get(key);
        } finally {
            //TODO
            //请替换sentence5为合适内容(5)
            lock.readLock().unlock();
        }
    }
    
    // 模拟耗时的操作
    private void slowly() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    // 模拟非常耗时的操作
    private void verySlowly() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
