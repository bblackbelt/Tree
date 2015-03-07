package com.blackbelt.trees;

import java.util.TreeMap;

public class AVLTree<K extends Comparable<K>, V> extends AbsBalancedBSTree<K, V> {

    public void put(K key, V value) {
        Node<K, V> node = insertNode(key, value);
        fixUpInsertion(node);
    }

    @Override
    public V get(K key) {
        Node<K, V> node = searchIterative(key);
        return node != null ? node.mValue : null;
    }

    @Override
    public V delete(K key) {
        return null;
    }


    @Override
    protected void fixUpInsertion(Node<K, V> node) {
        if (node.mParent == null || node.mParent.mParent == null) {
            return;
        }
        Node<K, V> parent = node.mParent.mParent;
        int balanceFactor = getBalance(parent);
        System.out.println("balanceFactor " + balanceFactor);
        while (balanceFactor < -1 || balanceFactor > 1) {
            if (node.mParent == node.mParent.mParent.mLeftChild) {
                if (node == node.mParent.mRightChild) {
                    node = node.mParent;
                    leftRotate(node);
                }
                node = node.mParent.mParent;
                rightRotate(node);
            } else {
                if (node == node.mParent.mLeftChild) {
                    node = node.mParent;
                    rightRotate(node);
                }
                node = node.mParent.mParent;
                leftRotate(node);
            }
            balanceFactor = getBalance(node);
            System.out.println("node " + node + "balanceFactor " + balanceFactor);
        }
    }

    @Override
    protected void fixUpDeletion(Node<K, V> node) {

    }


    public void printInOrder() {
        printInOrder(mRoot);
    }

    int getBalance(Node<K, V> node) {
        if (node == null) {
            return -1;
        }
        return height(node.mLeftChild) - height(node.mRightChild);

    }

}
