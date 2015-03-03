/***
The Read-Black-Tree is a data structure which is a type of self-balancing binary search tree. 
In addition to the requirments of the BS tree, 
the RB tree requiers that the following constraints must be satisfied:

1. A node is either red or black
2. the root is black
3. all leaves are black (or all the leaves are same color of the root)
4. every red node has black parent
5. every red node must have two black childrem
6. for each node, the simple path from the node to the descendant leaves container the same number of black nodes.

We call the number of black nodes on any simple path from (but not including), 
* a node x down to the leaf the "Black height of the node", denoted by bh(x). 

It is possible to demostrate that a BR tree with n nodes has height 2ln(n + 1).  

All the operations on a RB tree run whit complexity O(lg(n)). 
Insertion and delation change the tree's structure, and we have to restore it in order to mantainthe constraints

Insertion: 
we start from a valid RB-tree, and we insert a new node like we do for a BS-tree. 
The new node has color red. The new node could violate the propery 2, the root is black,
or the property 5, every red node must have two black children, and both are due to the new node colored red. 
We could think to color the new node black, but this thing will make things worse, because of the property 6. 
The first thing we do to fix it is to re-colour the new's node siblings (parent and granparent), 
inverting the node's colours:

		7b					7b				
	3b		18r			3b		18r
		10b					10r
	8r		11r			8b		11b
				15r					15r
 

this way we moved the violation two levels up. We could think to re-colour 
again to fix up the new violation, but in this case doesn't work because 
the children of 7 aren't of the same color. To make things worst the path 
from 10 to 7 is a zig-zag violations. The first thing we do is to remove thi zig-zag line. 
To do so we rigth rotate the sub-tree around 18:

		7b					7b
	3b		18r			3b		10r
		10r					8b		18r
	8b		11b	22b				11b		22b
				15r 	26b				15r		26b

we still have a red-red violation between 10 and 18. We can't re-coulor againg. 
We do a left-rotation around 7, and we recoulur to fix the black root property

		7b							10b
	3b		10r					7r		  18r
		8b		18r			      3b  8b	      11b    22b
			11b		22b					 15b    26b
				15r		26b	

We havee 6 cases, three symmetric cases. 
 Case 1: Recolouring
 Case 2: Red-Red zig-zig violation
 Case 3: Red-Red straight violation 

the pseudo-code of the fixup routing is:

FIXUP(z) {

	while (z.p.color == RED) {
		// if z is a left-child
		if (z.p == z.p.p.left) { 
			node uncle = z.p.p.rigth;
			// test for case 1
			if (node.color == RED) {
				z.p.color = BLACK;
				uncle.color = RED;
				z.p.p.color = RED;
				// fase end 1 finished. We move the violation a level up
				z = z.p.p;
			} else {
				// case 2: zig-zag violation
				if (z == z.p.right) {	
					z = z.p;
					rotateLeft(z);
				}
				// case 3. We resolved a zig-zag rotation
				// or we had just the straigth red-red rotation
				z.p.color = BLACK
				z.p.p.color = RED;
				rotateRigth(z.p.p)
			}
		} else {
			// same pseudo-code with left and right exchaged 
		}
	}
	treeRoot.color = BLACK;
}
* 
****/

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

	private static final boolean RED = false;
	private static final boolean BLACK = true;
	
	private Node<K, T> mRoot;
	private int mSize = 0;

	public enum PRINT_ORDER {
		IN_ORDER,
		PRE_ORDER,
		POST_ORDER;
	} 
	

	public void put(final K key, final T value) {
		insertNodeIterative(key, value);
		assert isBalanced();
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

        private Node<K, T> insertNodeRecursive(Node<K, T> current, Node<K, T> parent, K key, T value) {
                        if (current == null) {
				current = new Node<>(key, value);
				current.mParent = parent;
	 			if (mRoot == null) {
					mRoot = current;
				}
				System.out.println(current + " parent " + current.mParent);
                        } else if (key.compareTo(current.mKey) <= 0) {
                                 current.mLeftChild = insertNodeRecursive(current.mLeftChild, current, key, value);
                        } else if (key.compareTo(current.mKey) > 0) {
                                 current.mRightChild = insertNodeRecursive(current.mRightChild, current, key, value);
                        }
                        return current;
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
			if (key.compareTo(tmpNodeKey) < 0) {
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
			} else {
				tmpNode.mItem = item;
				return;
			} 
		}
		++mSize;
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
	
	public void deleteNode(K key) {
		Node<K, T> node = deleteNode(key, mRoot);
		if (node != null) {
			--mSize;
		}
	}

	private void fixNodeDeletion(Node<K, T> node) {
		while (node != null && node.mColor == BLACK) {
			if (node == node.mParent.mLeftChild) {
				Node<K, T> brother = node.mParent.mRightChild;
				if (brother.mColor == RED) {
					brother.mColor = BLACK;
					node.mParent.mColor = RED;
					leftRotate(node.mParent);
					brother = node.mParent.mRightChild;
				}
				if (brother.mLeftChild.mColor == BLACK && brother.mRightChild.mColor == BLACK) {
					brother.mColor = RED;
					node = node.mParent;
				} else if (brother.mRightChild.mColor == BLACK) {
					brother.mLeftChild.mColor = BLACK;
					brother.mColor = RED;
					rightRotate(brother);
					brother = brother.mParent.mRightChild;
				}
				brother.mColor = node.mParent.mColor;
				node.mParent.mColor = BLACK;
				brother.mRightChild.mColor = BLACK;
				leftRotate(node.mParent);
				node = mRoot;
			} else if (node == node.mParent.mRightChild) {
				Node<K, T> brother = node.mParent.mLeftChild;
				if (brother.mColor == RED) {
					brother.mColor = BLACK;
					node.mParent.mColor = RED;
					rightRotate(node.mParent);
					brother = node.mParent.mLeftChild;
				}
				if (brother.mLeftChild.mColor == BLACK && brother.mRightChild.mColor == BLACK) {
					brother.mColor = RED;
					node = node.mParent;
				} else if (brother.mLeftChild.mColor == BLACK) {
					brother.mRightChild.mColor = BLACK;
					brother.mColor = RED;
					leftRotate(brother);
					brother = brother.mParent.mLeftChild;
				}
				brother.mColor = node.mParent.mColor;
				node.mParent.mColor = BLACK;
				brother.mLeftChild.mColor = BLACK;
				rightRotate(node.mParent);
				node = mRoot;
			}
		}
		node.mColor = BLACK;
	}

	public Node<K, T> deleteNode(K key, Node<K, T> node) {
		if (node == null || key == null) {
			return null;
		}
		Node<K, T> tmpNode = mRoot;
		Node<K, T> parent = null;
		boolean found = false;
		while (!found) {	
			if (tmpNode == null) {
				return null;
			}
			K tmpNodeKey = tmpNode.mKey;
			if (key.compareTo(tmpNodeKey) < 0) {
				tmpNode = tmpNode.mLeftChild;
			} else if (key.compareTo(tmpNodeKey) > 0) {
				tmpNode = tmpNode.mRightChild;
			} else {
				found = true;
			} 
		}
		boolean color = node.mColor;
		// node without left child. We replace it with his right child.
		// if the right child is null, the we dealt with the situation 
		// where node has both children null. If its right child is not 
		// null, we dealt with the situation when node has just one child
		// which is its right
		if (tmpNode.mLeftChild == null) { 
			transplant(tmpNode, tmpNode.mRightChild);
		}
		// if node has just one child which is its left child
		// we replace node with its left child.
		else if (node.mRightChild == null) {
			transplant(tmpNode, tmpNode.mLeftChild);
		} else {
			Node<K, T> y = treeMinimum(tmpNode.mRightChild);
			color = y.mColor;
			Node<K, T> x = y.mRightChild;
			if (y.mParent == tmpNode) {
				if (x != null) {
					x.mParent = y;
				}
			} else {
				transplant(y, y.mRightChild);
				y.mRightChild = tmpNode.mRightChild;
				y.mRightChild.mParent = y; 
			}
			transplant(tmpNode, y);
			y.mLeftChild = tmpNode.mLeftChild;
			y.mLeftChild.mParent = y;
			y.mColor = tmpNode.mColor;
			if (color == BLACK) {
				fixNodeDeletion(x);
			}
		}
		return node;
	}

	private void transplant(Node<K, T> from, Node<K, T> with) {
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

	public int height(Node<K, T> node) {
		if (node == null) {
			return -1;
		}
		return 1 + Math.max(height(node.mLeftChild), height(node.mRightChild));
	}

	private boolean isBalanced() {
		Node<K, T> node = mRoot;
		// sum of black nodes from the root to the min. 
		int blackNodes = 0;
		while (node != null) {
			if (node.mColor == BLACK) {
				blackNodes++;
			}
			node = node.mLeftChild; 
		}
		return isBalanced(mRoot, blackNodes);
	}

	private boolean isBalanced(Node<K, T> node, int blackNodes) {
		if (node == null) {
			return blackNodes == 0;
		}
		if (node.mColor == BLACK) {
			blackNodes--;
		}
		return isBalanced(node.mLeftChild, blackNodes) && isBalanced(node.mRightChild, blackNodes);
	}

	public void removeAll() {
		mRoot = null;
		mSize = 0;
	}

	public boolean isEmpty() {
		return mRoot == null;
	}

	public int size() {
		return mSize;
	}
}
