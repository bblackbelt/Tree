package com.blackbelt.trees;
import java.lang.Comparable;

public class BSTree<K extends Comparable<K>, T> {

	private static class Node<K extends Comparable<K>, T> {
		public K mKey;
		public T mItem;
		Node<K, T> mLeftChild;
		Node<K, T> mRightChild;
		Node<K, T> mParent;
		
		public Node(K key, T item) {
			mKey = key;
			mItem = item;
		}

		@Override
		public String toString() {
			return "key " + mKey + " data " + mItem;
		}
	}

	private Node<K, T> mRoot;
	public enum PRINT_ORDER {
		IN_ORDER,
		PRE_ORDER,
		POST_ORDER;
	} 
	

	public void insertNode(final K key, final T item) {
		Node<K, T> node = new Node<>(key, item);
		if (mRoot == null) {
			mRoot = node;
			System.out.println("root " + mRoot);
			return;
		}
		Node<K, T> start = mRoot;
		insertNodeRecursive(start, node);
	}
	
	private Node<K, T> insertNodeRecursive(Node<K, T> current, Node<K, T> toInsert) {
			System.out.println(" current " + current);
			if (current == null) {
				return toInsert;
			}
			if (toInsert.mKey.compareTo(current.mKey) <= 0) {
				 current.mLeftChild = insertNodeRecursive(current.mLeftChild, toInsert);
			} else if (toInsert.mKey.compareTo(current.mKey) > 0) {
				 current.mRightChild = insertNodeRecursive(current.mRightChild, toInsert);
			}
			return current;
	}
	
	private void insertNodeIterative(final K key, final T item) {
		Node<K, T> node = new Node<>(key, item);
		if (mRoot == null) {
			mRoot = node;
			return;
		}
		Node<K, T> tmpNode = mRoot;
		Node<K, T> parent = null;
		while (true) {	
			parent = tmpNode;
			K tmpNodeKey = tmpNode.mKey;
			if (key.compareTo(tmpNodeKey) <= 0) {
				tmpNode = tmpNode.mLeftChild;
				if(tmpNode == null) {
					parent.mLeftChild = node;
					return;	
				}
			} else if (key.compareTo(tmpNodeKey) > 0) {
				tmpNode = tmpNode.mRightChild;
				if (tmpNode == null) {
					parent.mRightChild = node;
					return;
				}
			} 
		}
	}
	
	public void printTree(final PRINT_ORDER order) {
		
		System.out.println("Printing in: "  + order.name());
		switch(order) {
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
		System.out.println("END: "  + order.name());
	}

	private void printPreOrder(Node<K, T> node) {
		if (node != null) {
			System.out.println(node.mKey);
			printInOrder(node.mLeftChild);
			printInOrder(node.mRightChild);
		}
	}

	private void printPostOrder(Node<K, T> node) {
		if (node != null) {
			printInOrder(node.mLeftChild);
			printInOrder(node.mRightChild);
			System.out.println(node.mKey);
		}
	}


	private void printInOrder(Node<K, T> node) {
		if (node != null) {
			printInOrder(node.mLeftChild);
			System.out.println(node.mKey);
			printInOrder(node.mRightChild);
		}
	}

	public T get(K key) {
		// Node<K, T> node = findRecursive(key, mRoot);
		Node<K, T> node = searchIterative(key);
		return node != null ? node.mItem : null;
	}

	public boolean contains(K key) {
		return searchRecursive(key, mRoot) != null;
	}
	
	private Node<K, T> searchIterative(K key) {
		Node<K, T> t = mRoot;
		while (true) {
			if (t == null || key.compareTo(t.mKey) == 0 ) {
				return t;
			}
			if (key.compareTo(t.mKey) < 0) {
				t = t.mLeftChild;
			} else if (key.compareTo(t.mKey) > 0) {
				t = t.mRightChild;
			}
		}
	}

	private Node<K, T> searchRecursive(K key, Node<K, T> current) {
		if (current == null || key.compareTo(current.mKey) == 0 ) {
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

	public void deleteNode(K key) {
		mRoot = deleteNode(key, mRoot);
	}

	public Node<K, T> deleteNode(K key, Node<K, T> node) {
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
			Node<K, T> t = node;
			node = treeMinimum(t.mRightChild);
			node.mRightChild = deleteMin(t.mRightChild);
			node.mLeftChild = t.mLeftChild;
		}
		return node;
	}


	private Node<K, T> deleteMin(Node<K, T> node) {
		if (node.mLeftChild == null) {
			return node.mRightChild;
		}
		node.mLeftChild = deleteMin(node.mLeftChild);
		return node;
	}
	
	private Node<K, T> treeMinimum(Node<K, T> node) {
		while(node.mLeftChild != null) {
			node = node.mLeftChild; 
		}
		return node;
	}
	
	private Node<K, T> treeMaximum(Node<K, T> node) {
		while(node.mRightChild != null) {
			node = node.mRightChild;
		}
		return node;
	}
}
