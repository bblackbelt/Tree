
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


    @Override
    public V delete(K key) {
        Node<K, V> node = deleteNode(key, mRoot);
        return node != null ? node.mValue : null;
    }

    public Node<K, V> deleteNode(K key, Node<K, V> node) {
        if (node == null || key == null) {
            return null;
        }

        if (key.compareTo(node.mKey) < 0) {
            node.mLeftChild = deleteNode(key, node.mLeftChild);
        } else if (key.compareTo(node.mKey) > 0) {
            node.mRightChild = deleteNode(key, node.mRightChild);
        } else {
            // node without left child. We replace it with his right child.
            // if the right child is null, the we dealt with the situation
            // where node has both children null. If its right child is not
            // null, we dealt with the situation when node has just one child
            // which is its right
            if (node.mLeftChild == null) {
                return node.mRightChild;
            }
            // if node has just one child which is its left child
            // we replace node with its left child.
            if (node.mRightChild == null) {
                return node.mLeftChild;
            }
            Node<K, V> t = node;
            node = treeMinimum(t.mRightChild);
            node.mRightChild = deleteMin(t.mRightChild);
            node.mLeftChild = t.mLeftChild;
        }
        return node;
    }

    public void printRoot() {
        System.out.println(mRoot);
    }
}
