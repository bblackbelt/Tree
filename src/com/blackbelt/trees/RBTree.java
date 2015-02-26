package com.blackbelt.trees;
import java.lang.Comparable;

public class RBTree<K extends Comparable<K>, T> {

	private static class Node<K extends Comparable<K>, T> {
		public K mKey;
		public T mItem;
		private boolean mColor = RED;

		Node<K, T> mParent;
		Node<K, T> mLeftChild;
		Node<K, T> mRightChild;
		
		public Node(K key, T item) {
			mKey = key;
			mItem = item;
		}

		@Override
		public String toString() {
			String string =  "Me: { " + mKey + " " +mColor + " } ";
			return string;
		}
	}

	private Node<K, T> mRoot;
	private static final boolean RED = false;
	private static final boolean BLACK = true;

	public enum PRINT_ORDER {
		IN_ORDER,
		PRE_ORDER,
		POST_ORDER;
	} 
	

	public void put(final K key, final T item) {
		insertNodeIterative(key, item);
	}

	private void fixInsert(Node<K, T> node) {
		while (node.mParent != null && node.mParent.mColor == RED) {
			if (node.mParent == node.mParent.mParent.mLeftChild) {
				Node<K, T> uncle = node.mParent.mParent.mRightChild;
				if (uncle != null && uncle.mColor == RED) {
					node.mParent.mColor = BLACK;
					uncle.mColor = BLACK;
					node.mParent.mParent.mColor = RED;
					node = node.mParent.mParent;
				} else { 
					if (node == node.mParent.mRightChild) {
						node = node.mParent;
						leftRotate(node);
					}	
					node.mParent.mColor = BLACK;
					node.mParent.mParent.mColor = RED;
					node = node.mParent.mParent;
					rightRotate(node);
				}
			} else if (node.mParent == node.mParent.mParent.mRightChild) {
				Node<K, T> uncle = node.mParent.mParent.mLeftChild;
				if (uncle != null && uncle.mColor == RED) {
					node.mParent.mColor = BLACK;
					uncle.mColor = BLACK;
					node.mParent.mParent.mColor = RED;
					node = node.mParent.mParent;
				} else { 
					if (node == node.mParent.mLeftChild) {
						node = node.mParent;
						rightRotate(node);
					}
					node.mParent.mColor = BLACK;
					node.mParent.mParent.mColor = RED;
					node = node.mParent.mParent;
					leftRotate(node);
				}
			}
		}
		mRoot.mColor = BLACK;	
	}
	

	private void leftRotate(Node<K, T> node) {
		Node<K, T> y = node.mRightChild;
		node.mRightChild = y.mLeftChild;
		if (y.mLeftChild != null) {
			y.mLeftChild.mParent = node;
		}
		y.mParent = node.mParent;
		if (node.mParent == null) {
			mRoot = y;
		} else if (node == node.mParent.mLeftChild) {
			node.mParent.mLeftChild = y;
		} else {
			node.mParent.mRightChild = y;
		}
		y.mLeftChild = node;
		node.mParent = y;
	
	}
	
	private void rightRotate(Node<K, T> node) {
		Node<K, T> y = node.mLeftChild;
		node.mLeftChild = y.mRightChild;
		if (y.mRightChild != null) {
			y.mRightChild.mParent = node;
		}
		y.mParent = node.mParent;
		if (node.mParent == null) {
			mRoot = y;
		} else if (node == node.mParent.mRightChild) {
			node.mParent.mRightChild = y;
		} else {
			node.mParent.mLeftChild = y;
		}
		y.mRightChild = node;
		node.mParent = y;
	}

	
	private void insertNodeIterative(final K key, final T item) {
		Node<K, T> node = new Node<>(key, item);
		if (mRoot == null) {
			mRoot = node;
			mRoot.mColor = BLACK;
			return;
		}
		Node<K, T> tmpNode = mRoot;
		Node<K, T> parent = null;
		boolean inserted = false;
		while (!inserted) {	
			parent = tmpNode;
			K tmpNodeKey = tmpNode.mKey;
			if (key.compareTo(tmpNodeKey) <= 0) {
				tmpNode = tmpNode.mLeftChild;
				if(tmpNode == null) {
					parent.mLeftChild = node;
					inserted = true;
				}
			} else if (key.compareTo(tmpNodeKey) > 0) {
				tmpNode = tmpNode.mRightChild;
				if (tmpNode == null) {
					parent.mRightChild = node;
					inserted = true;
				}
			} 
		}
		node.mParent = parent;
		fixInsert(node);
	}

	public void printRoot() {
		System.out.println(mRoot);
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
