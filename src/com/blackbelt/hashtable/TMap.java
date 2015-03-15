package com.blackbelt.hashtable;

/**
 * Created by emanuele on 14.03.15.
 */
public interface TMap<K, V> {

    public V get(K key);

    public V remove(K key);

    public V put(K key, V value);
}
