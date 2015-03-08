
package com.blackbelt.trees;

import java.lang.Comparable;

public class BSTree<K extends Comparable<K>, V> extends AbsBSTree<K, V> {


    public enum PRINT_ORDER {
        IN_ORDER,
        PRE_ORDER,
        POST_ORDER;
    }


    public void put(final K key, final V item) {
        Node<K, V> node = new Node<>(key, item);
        if (mRoot == null) {
            mRoot = node;
            System.out.println("root " + mRoot);
            return;
        }
        Node<K, V> start = mRoot;
        insertNodeRecursive(start, node);
    }


    public void printTree(final PRINT_ORDER order) {

        System.out.println("Printing in: " + order.name());
        switch (order) {
            case IN_ORDER:
                printInOrder(mRoot);
                break;
            case POST_ORDER:
                printPostOrder(mRoot);
                break;
            case PRE_ORDER:
                printPreOrder(mRoot);
                break;
            default:
                System.out.println("Unknown case");
                return;
        }
        System.out.println("END: " + order.name());
    }

    public V get(K key) {
        // Node<K, T> node = findRecursive(key, mRoot);
        Node<K, V> node = searchIterative(key);
        return node != null ? node.mValue : null;
    }


    public V delete(K key) {
        Node<K, V> node = deleteNodeIterative(key);
        return node != null ? node.mValue : null;
    }

    public void printRoot() {
        System.out.println(mRoot);
    }
}
