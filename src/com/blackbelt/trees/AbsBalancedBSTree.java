package com.blackbelt.trees;

/**
 * Created by emanuele on 07.03.15.
 */
public abstract class AbsBalancedBSTree<K extends Comparable<K>, V> extends AbsBSTree<K, V> {


    protected void rightRotate(Node<K, V> node) {
        Node<K, V> t = node.mLeftChild;
        node.mLeftChild = t.mRightChild;
        if (t.mRightChild != null) {
            t.mRightChild.mParent = node;
        }
        t.mParent = node.mParent;
        if (node.mParent == null) {
            mRoot = t;
        } else if (node == node.mParent.mLeftChild) {
            node.mParent.mLeftChild = t;
        } else {
            node.mParent.mRightChild = t;
        }
        t.mRightChild = node;
        node.mParent = t;
    }

    protected void leftRotate(Node<K, V> node) {
        Node<K, V> t = node.mRightChild;
        node.mRightChild = t.mLeftChild;
        if (t.mLeftChild != null) {
            t.mLeftChild.mParent = node;
        }
        t.mParent = node.mParent;
        if (node.mParent == null) {
            mRoot = t;
        } else if (node == node.mParent.mLeftChild) {
            node.mParent.mLeftChild = t;
        } else {
            node.mParent.mRightChild = t;
        }
        t.mLeftChild = node;
        node.mParent = t;
    }

    protected abstract void fixUpInsertion(Node<K, V> node);

    protected abstract void fixUpDeletion(Node<K, V> node);
}
