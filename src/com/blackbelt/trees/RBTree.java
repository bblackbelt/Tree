package com.blackbelt.trees;
import java.lang.Comparable;

public class RBTree<K extends Comparable<K>, T> {

	private static class Node<K extends Comparable<K>, T> {
		public K mKey;
		public T mItem;
		private boolean mColor = RED;

		Node<K, T> mLeftChild;
		Node<K, T> mRightChild;
		
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
	private static final boolean RED = false;
	private static final boolean BLACK = true;

	public enum PRINT_ORDER {
		IN_ORDER,
		PRE_ORDER,
		POST_ORDER;
	} 
	

	public void insertNode(final K key, final T item) {
		mRoot = insertNodeRecursive(mRoot, key, item);
		mRoot.mColor = BLACK;
	}
	
	private Node<K, T> insertNodeRecursive(Node<K, T> current, K key, T value) {
			System.out.println(" current " + current);
			if (current == null) {
				return new Node<>(key, value);
			}
			if (key.compareTo(current.mKey) <= 0) {
				 current.mLeftChild = insertNodeRecursive(current.mLeftChild, key, value);
			} else if (key.compareTo(current.mKey) > 0) {
				 current.mRightChild = insertNodeRecursive(current.mRightChild, key, value);
			}
			if (isRed(current.mRightChild) && !isRed(current.mLeftChild)) {
				current = rotateLeft(current);
			}
			if (isRed(current.mLeftChild) && isRed(current.mLeftChild.mLeftChild)) {
				current = rotateRight(current);
			}	
			if (isRed(current.mRightChild) && isRed(current.mLeftChild)) {
				switchColours(current);
			}
			return current;
	}

	private void switchColours(Node<K, T> node) {
		node.mColor = !node.mColor;
		node.mLeftChild.mColor = !node.mLeftChild.mColor;
		node.mRightChild.mColor = !node.mRightChild.mColor;
	}

	private boolean isRed(Node<K, T> node) {
		return node != null && node.mColor == RED;
	}


	private Node<K, T> rotateLeft(Node<K, T> node) {
		Node<K, T> t = node.mRightChild;
		node.mRightChild = t.mLeftChild;
		t.mLeftChild = node;
		t.mColor = t.mLeftChild.mColor;
		t.mLeftChild.mColor = RED;
		return t;
	}

	private Node<K, T> rotateRight(Node<K, T> node) {
		Node<K, T> t = node.mLeftChild;
		node.mLeftChild = t.mRightChild;
		t.mRightChild = node;
		t.mColor = t.mRightChild.mColor;
		t.mRightChild.mColor = RED;
		return t;
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
