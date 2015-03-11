package com.blackbelt.trees;

/**
 * Created by emanuele on 07.03.15.
 */

public abstract class AbsBSTree<K extends Comparable<K>, V> {

    protected static class Node<K extends Comparable<K>, V> {
        K mKey;
        V mValue;

        Node<K, V> mLeftChild;
        Node<K, V> mRightChild;
        Node<K, V> mParent;

        public Node(K k, V v) {
            mKey = k;
            mValue = v;
        }

        public K getKey() {
            return mKey;
        }

        public V getValue() {
            return mValue;
        }

        @Override
        public String toString() {
            String parent = "null";
            if (mParent != null) {
                parent = ""+mParent.mKey;
                parent += " leftChild " + (mParent.mLeftChild == this) + " rightChild " + (mParent.mRightChild == this);
            }
            return "{Key: " + mKey + " Value " + mValue + " } parent " + parent;
        }
    }

    protected Node<K, V> mRoot;
    private int mSize = 0;

    protected Node<K, V> insertNode(K key, V value) {
        Node<K, V> node = new Node<>(key, value);
        if (mRoot == null) {
            mRoot = node;
            return node;
        }
        Node<K, V> tmp = mRoot;
        Node<K, V> parent = null;
        while (tmp != null) {
            parent = tmp;
            if (key.compareTo(tmp.mKey) < 0) {
                tmp = tmp.mLeftChild;
            } else if (key.compareTo(tmp.mKey) > 0) {
                tmp = tmp.mRightChild;
            } else {
                tmp.mValue = value;
                return null;
            }
        }
        node.mParent = parent;
        if (key.compareTo(parent.mKey) < 0) {
            parent.mLeftChild = node;
        } else {
            parent.mRightChild = node;
        }
        ++mSize;
        return node;
    }

    protected Node<K, V> insertNodeRecursive(Node<K, V> current, Node<K, V> toInsert) {
        System.out.println(" current " + current);
        if (current == null) {
            ++mSize;
            return toInsert;
        }
        if (toInsert.mKey.compareTo(current.mKey) <= 0) {
            current.mLeftChild = insertNodeRecursive(current.mLeftChild, toInsert);
        } else if (toInsert.mKey.compareTo(current.mKey) > 0) {
            current.mRightChild = insertNodeRecursive(current.mRightChild, toInsert);
        }
        return current;
    }

    protected Node<K, V> searchIterative(K key) {
        Node<K, V> t = mRoot;
        while (true) {
            if (t == null || key.compareTo(t.mKey) == 0) {
                return t;
            }
            if (key.compareTo(t.mKey) < 0) {
                t = t.mLeftChild;
            } else if (key.compareTo(t.mKey) > 0) {
                t = t.mRightChild;
            }
        }
    }

    protected Node<K, V> searchRecursive(K key, Node<K, V> current) {
        if (current == null || key.compareTo(current.mKey) == 0) {
            return current;
        }
        System.out.println("key " + key + " current " + current);
        if (key.compareTo(current.mKey) < 0) {
            return searchRecursive(key, current.mLeftChild);
        } else if (key.compareTo(current.mKey) > 0) {
            return searchRecursive(key, current.mRightChild);
        }
        return null;
    }

    protected Node<K, V> deleteMin(Node<K, V> node) {
        if (node.mLeftChild == null) {
            return node.mRightChild;
        }
        node.mLeftChild = deleteMin(node.mLeftChild);
        return node;
    }

    protected Node<K, V> treeMinimum(Node<K, V> node) {
        while (node != null && node.mLeftChild != null) {
            node = node.mLeftChild;
        }
        return node;
    }

    protected Node<K, V> treeMaximum(Node<K, V> node) {
        while (node.mRightChild != null) {
            node = node.mRightChild;
        }
        return node;
    }


    public int height(Node<K, V> n) {
        if (n == null) {
            return -1;
        }
        return 1 + Math.max(height(n.mLeftChild), height(n.mRightChild));
    }

    protected void printInOrder(Node<K, V> n) {
        if (n == null) {
            return;
        }
        printInOrder(n.mLeftChild);
        System.out.println(n);
        printInOrder(n.mRightChild);
    }

    protected void printPreOrder(Node<K, V> node) {
        if (node != null) {
            System.out.println(node.mKey);
            printInOrder(node.mLeftChild);
            printInOrder(node.mRightChild);
        }
    }

    protected void printPostOrder(Node<K, V> node) {
        if (node != null) {
            printInOrder(node.mLeftChild);
            printInOrder(node.mRightChild);
            System.out.println(node.mKey);
        }
    }

    public boolean contains(K key) {
        return searchRecursive(key, mRoot) != null;
    }


    protected Node<K, V> deleteNodeIterative(K key) {
        boolean found = false;
        Node<K, V> tmp = mRoot;
        Node<K, V> parent = null;
        while (!found) {
            if (tmp == null) {
                System.out.println(" node not found ");
                return null;
            }
            if (key.compareTo(tmp.mKey) < 0) {
                parent = tmp;
                tmp = tmp.mLeftChild;
            } else if (key.compareTo(tmp.mKey) > 0) {
                parent = tmp;
                tmp = tmp.mRightChild;
            } else {
                System.out.println(" found " + tmp);
                if (tmp.mLeftChild == null) {
                    transplant(tmp, tmp.mRightChild, parent);
                } else if (tmp.mRightChild == null) {
                    transplant(tmp, tmp.mLeftChild, parent);
                } else {
                    Node<K, V> minNode = tmp.mRightChild;
                    Node<K, V> pMinNode = null;
                    while (minNode != null && minNode.mLeftChild != null) {
                        pMinNode = minNode;
                        minNode = minNode.mLeftChild;
                    }
                    //     System.out.println(" minNode " + minNode + " " + pMinNode);
                    if (pMinNode != null) {
                        transplant(minNode, minNode.mRightChild, pMinNode);
                        minNode.mRightChild = tmp.mRightChild;
                       // minNode.mRightChild.mParent = minNode;
                    }
                    transplant(tmp, minNode, parent);
                    minNode.mLeftChild = tmp.mLeftChild;
                    //minNode.mLeftChild.mParent = minNode;
                }
                return tmp;
            }
        }
        return null;
    }

    private void transplant(Node<K, V> from, Node<K, V> with, Node<K, V> fromParent) {
        if (fromParent == null) {
            mRoot = with;
        } else if (fromParent.mRightChild == from) {
            fromParent.mRightChild = with;
        } else {
            fromParent.mLeftChild = with;
        }
    }


    protected Node<K, V> deleteNodeRecursive(K key, Node<K, V> node) {
        if (node == null || key == null) {
            return null;
        }

        if (key.compareTo(node.mKey) < 0) {
            node.mLeftChild = deleteNodeRecursive(key, node.mLeftChild);
        } else if (key.compareTo(node.mKey) > 0) {
            node.mRightChild = deleteNodeRecursive(key, node.mRightChild);
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

    public abstract void put(K key, V value);

    public abstract V get(K key);

    public abstract V delete(K key);
}
