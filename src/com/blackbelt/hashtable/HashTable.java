package com.blackbelt.hashtable;


import com.blackbelt.utils.Utils;

/**
 * Created by emanuele on 14.03.15.
 */
public class HashTable<K, V> implements TMap<K, V> {

    static class MapEntry<K, V> {
        int hash;
        K key;
        V value;
        MapEntry<K, V> next;

        public MapEntry(int h, K k, V v, MapEntry<K, V> n) {
            hash = h;
            key = k;
            value = v;
            next = n;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V v) {
            V oldValue = value;
            value = v;
            return oldValue;
        }

        @Override
        public String toString() {
            String tmp = " { key " + key + " value " + value + " hash " + hash + " } ";
            MapEntry<K, V> tmpEntry = next;
            while (tmpEntry != null) {
                tmp += "next: { key " + tmpEntry.key + " value " + tmpEntry.value + " hash " + tmpEntry.hash + " } ";
                tmpEntry = tmpEntry.next;
            }
            return tmp;
        }
    }


    private static final int WORD_LENGTH = 32;
    private static final int A_FACTOR = (int) 2654435769L;
    private static final int MAX_CAPACITY = 1 << 30;
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private MapEntry<K, V> table[];

    private final float loadFactor;
    private int kFactor = 4;
    private int threshold;
    private int size;
    private int collisionCounter = 0;

    public HashTable(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " +
                    initialCapacity);
        if (initialCapacity > MAX_CAPACITY) {
            initialCapacity = MAX_CAPACITY;
        }
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Illegal load factor: " +
                    loadFactor);
        }
        initialCapacity = Utils.roundup2(initialCapacity);
        newKFactor(initialCapacity);
        threshold = (int) Math.min(initialCapacity * loadFactor, MAX_CAPACITY + 1);
        this.loadFactor = loadFactor;
        table = new MapEntry[initialCapacity];
    }

    private void newKFactor(int capacity) {
        kFactor = (int) (Math.log(capacity) / Math.log(2));
    }

    public HashTable() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }


    private void resize(int newLength) {
        if (newLength > MAX_CAPACITY) {
            threshold = MAX_CAPACITY;
            return;
        }
        // System.out.println("resizing " + size + " old threshold " + threshold + " old kFactor " + kFactor);
        threshold = (int) Math.min(newLength * loadFactor, MAX_CAPACITY);
        newKFactor(newLength);
        // System.out.println("resizing " + " new threshold " + threshold + " new kFactor " + kFactor);
        MapEntry<K, V> newTable[] = new MapEntry[newLength];
        rehashTable(newTable);
        table = newTable;
    }

    private void rehashTable(MapEntry[] newTable) {
        int length = newTable.length;
        for (MapEntry<K, V> entry : table) {
            while (entry != null) {
                MapEntry<K, V> next = entry.next;
                entry.hash = hash(entry.key);
                int index = index(entry.hash, length);
                entry.next = newTable[index];
                newTable[index] = entry;
                entry = next;
            }
        }
    }

    @Override
    public V get(K key) {
        MapEntry<K, V> e = getMapEntry(key);
        return e != null ? e.getValue() : null;
    }

    private MapEntry<K, V> getMapEntry(K key) {
        int hash = hash(key);
        int index = index(hash, table.length);
        MapEntry<K, V> e;
        if ((e = table[index]) == null) {
            return null;
        }
        for (MapEntry<K, V> t = e; t != null; t = t.next) {
            System.out.println("bucket " + index + " ... " + t);
            if (key == t.key || key.equals(t.key)) {
                return t;
            }
        }
        return null;
    }

    private int index(int hash, int length) {
        return hash & (length - 1);
    }

    final int hash(Object k) {
        long t = k.hashCode() * (long) A_FACTOR;
        return (int) (t >> (WORD_LENGTH - kFactor));
    }

    @Override
    public V put(K key, V value) {
        int hash = hash(key);
        int index = index(hash, table.length);
        for (MapEntry<K, V> e = table[index]; e != null; e = e.next) {
            Object k;
            if (e.hash == hash && ((k = e.key) == key || key.equals(k))) {
                V oldValue = e.value;
                e.value = value;
                return oldValue;
            }
        }
        addInternal(hash, key, value, index);
        return null;
    }

    public void printAll() {
        for (int i = 0; i < table.length; i++) {
            System.out.println("slot " + i + " content:  " + table[i]);
        }
    }

    @Override
    public V remove(K key) {
        MapEntry<K, V> e = removeInternal(key);
        return (e == null) ? null : e.value;
    }

    final private MapEntry<K, V> removeInternal(K key) {
        int hash = hash(key);
        int index = index(hash, table.length);
        MapEntry<K, V> e;
        if ((e = table[index]) == null) {
            return null;
        }
        MapEntry<K, V> value = null;
        for (MapEntry<K, V> t = e, parent = null; t != null; parent = t, t = t.next) {
            System.out.println(t);
            System.out.println("parent " + parent);
            if (key == t.key || key.equals(t.key)) {
                --size;
                value = t;
                if (e == t) {
                    table[index] = t.next;
                    e = null;
                } else if (parent != null) {
                    parent.next = t.next;
                    t = null;
                }
                break;
            }
        }
        if (table.length / 4 == size) {
            resize(table.length / 2);
        }
        return value;
    }

    private void addInternal(int hash, K key, V value, int index) {
        if (size >= threshold && table[index] != null) {
            resize(2 * table.length);
            hash = hash(key);
            index = index(hash, table.length);
        }
        MapEntry<K, V> e = table[index];
        if (e != null) {
            ++collisionCounter;
        }
        MapEntry<K, V> newEntry = new MapEntry<>(hash, key, value, e);
        table[index] = newEntry;
        ++size;
    }

    public int getCollisionCounter() {
        return collisionCounter;
    }

    public int getSize() {
        return size;
    }
}
