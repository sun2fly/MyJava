package com.wi1024.struct.cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * LRU Cache By LinkedHashMap
 *
 * @author songfei@xbniao.com
 * @create 2017/12/04 10:42
 **/
public class LRUCache<K,V> {

    LinkedHashMap<K,V> map;

    public LRUCache(final int capacity){
        map = new LinkedHashMap<K, V>(capacity , 0.75f , true){
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > capacity;
            }
        };
    }

    public V get(K key) {
        if(map.containsKey(key)){
            return map.get(key);
        }else{
            return null;
        }
    }

    public void put(K key , V val) {
        map.put(key , val);
    }

    public int size() {
        return map.size();
    }


}
