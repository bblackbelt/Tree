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
        Node<K, V> n = deleteNodeIterative(key);
        if (n != null) {
            return n.mValue;
        }
        return null;
    }

    protected Node<K, V> deleteNodeIterative(K key) {
        boolean found = false;
        Node<K, V> tmp = mRoot;
        while (!found) {
            if (tmp == null) {
                System.out.println(" node not found ");
                return null;
            }
            if (key.compareTo(tmp.mKey) < 0) {
                tmp = tmp.mLeftChild;
            } else if (key.compareTo(tmp.mKey) > 0) {
                tmp = tmp.mRightChild;
            } else {
                System.out.println(" found " + tmp);
                if (tmp.mLeftChild == null) {
                    transplant(tmp, tmp.mRightChild);
                } else if (tmp.mRightChild == null) {
                    transplant(tmp, tmp.mLeftChild);
                } else {
                    Node<K, V> minNode = treeMinimum(tmp.mRightChild);
                    if (minNode.mParent != null) {
                        minNode.mParent.mLeftChild = minNode.mRightChild;
                        minNode.mRightChild = tmp.mRightChild;
                    }
                    transplant(tmp, minNode);
                    minNode.mLeftChild = tmp.mLeftChild;
                }
                fixUpDeletion(tmp);
                return tmp;
            }
        }
        return  null;
    }

    private void transplant(Node<K, V> from, Node<K, V> with) {
        if (from.mParent == null) {
            mRoot = with;
        } else if (from == from.mParent.mLeftChild) {
            from.mParent.mLeftChild = with;
        } else {
            from.mParent.mRightChild = with;
        }
        if (with != null) {
            with.mParent = from.mParent;
        }
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
