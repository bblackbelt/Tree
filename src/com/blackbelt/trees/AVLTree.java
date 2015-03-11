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
                //System.out.println(" found " + tmp);
                if (tmp.mLeftChild == null) {
                    transplant(tmp, tmp.mRightChild);
                } else if (tmp.mRightChild == null) {
                    transplant(tmp, tmp.mLeftChild);
                } else {
                    Node<K, V> minNode = treeMinimum(tmp.mRightChild);
                    if (minNode.mParent != null) {
                        transplant(minNode, minNode.mRightChild);
                        minNode.mRightChild = tmp.mRightChild;
                        if (minNode.mRightChild != null) {
                            minNode.mRightChild.mParent = minNode;
                        }
                    }
                    transplant(tmp, minNode);
                    minNode.mLeftChild = tmp.mLeftChild;
                    minNode.mLeftChild.mParent = minNode;
                    fixUpDeletion(tmp);
                    return tmp;
                }
                fixUpDeletion(tmp);
                return tmp;
            }
        }
        return null;
    }

    @Override
    protected void fixUpDeletion(Node<K, V> node) {
        Node<K, V> tmp = node;
        while (tmp != null) {
            int balance = getBalance(tmp);
           // System.out.println(" ss " + tmp + " " + balance);
            if (balance == 2 || balance == -2) {
                int leftBalance = getBalance(tmp.mLeftChild);
                int rightBalance = getBalance(tmp.mRightChild);
                if (leftBalance > rightBalance) {
                    Node<K, V> leftChild = tmp.mLeftChild;
                    if (getBalance(leftChild.mLeftChild) < getBalance(leftChild.mRightChild)) {
                        leftRotate(tmp.mLeftChild);
                    }
                    rightRotate(tmp);
                } else {
                    Node<K, V> rightChild = tmp.mRightChild;
                    if (getBalance(rightChild.mRightChild) < getBalance(rightChild.mLeftChild)) {
                        rightRotate(tmp.mRightChild);
                    }
                    leftRotate(tmp);
                }
            }
            tmp = tmp.mParent;
        }

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
        if (node == null || node.mParent == null || node.mParent.mParent == null) {
            return;
        }
        while (node.mParent.mParent != null) {
            Node<K, V> parent = node.mParent.mParent;
            int balanceFactor = getBalance(parent);
           // System.out.println("balanceFactor " + balanceFactor);
            if (balanceFactor < -1 || balanceFactor > 1) {
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
           //     System.out.println("node " + node + "balanceFactor " + balanceFactor);
                continue;
            }
            node = node.mParent;
        }
    }


    public void printHeight(K key) {
        Node<K, V> node = searchIterative(key);
        System.out.println(height(node));
    }


    public void printInOrder() {
        printInOrder(mRoot);
    }

    int getBalance(Node<K, V> node) {
        if (node == null) {
            return -1;
        }
        int leftHeight = height(node.mLeftChild);
        int rightHeight = height(node.mRightChild);
        return leftHeight - rightHeight;

    }

}
