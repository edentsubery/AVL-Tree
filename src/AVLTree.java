/**
 *
 * AVLTree
 *
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 *
 */

public class AVLTree {


	private IAVLNode root;
	/**
	 * public boolean empty()
	 *
	 * returns true if and only if the tree is empty
	 *
	 *O(1)
	 */
	public AVLTree(int k,String s) {
		IAVLNode t=new AVLNode(k,s);
		if(t.getKey()<0)
			return;
		this.root= t;
	}
	public AVLTree() {
		this.root = null;
	}
	public AVLTree(IAVLNode t) {
		this.root= t;
	}
	public boolean empty() {
		if(root==null)
			return true;
		if(root.getKey()==-1)
			return true;
		return false;
	}

	/**
	 * public String search(int k)
	 *
	 * returns the info of an item with key k if it exists in the tree
	 * otherwise, returns null
	 *
	 * O(log n)
	 */
	public String search(int k)
	{

		return search2(k,root);
	}
	private String search2(int k,IAVLNode t)
	{
		if(t==null) {
			return null;
		}
		if(t.getKey()==-1) {
			return null;
		}
		if(k==t.getKey()) {
			return t.getValue();
		}
		if(k<t.getKey()) {
			return search2(k,t.getLeft());
		}
		if(k>t.getKey()) {
			return search2(k,t.getRight());
		}

		return null;
	}

	/**
	 * public int insert(int k, String i)
	 *
	 * inserts an item with key k and info i to the AVL tree.
	 * the tree must remain valid (keep its invariants).
	 * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
	 * promotion/rotation - counted as one rebalnce operation, double-rotation is counted as 2.
	 * returns -1 if an item with key k already exists in the tree.
	 *
	 * O(log n)
	 */
	public int insert(int k, String i) {
		IAVLNode t=isin(k,root);
		if(t==null) {
			root=new AVLNode(k,i);
			root.setLeft(new AVLNode());
			root.getLeft().setParent(root);
			root.setRight(new AVLNode());
			root.getRight().setParent(root);
			root.setHeight(0);
			root.setRank(0);
			return 0;
		}
		if (t.getKey()!=-1)
			return -1;
		t=t.getParent();
		if(t.getKey()>k) {
			t.setLeft(new AVLNode(k,i));
			t.getLeft().setParent(t);
			t=t.getLeft();
			t.setHeight(0);
			t.setRank(0);
			t.setLeft(new AVLNode());
			t.getLeft().setParent(t);
			t.setRight(new AVLNode());
			t.getRight().setParent(t);
			t=t.getParent();
		}
		else {
			t.setRight(new AVLNode(k,i));
			t.getRight().setParent(t);
			t=t.getRight();
			t.setHeight(0);
			t.setRank(0);
			t.setLeft(new AVLNode());
			t.getLeft().setParent(t);
			t.setRight(new AVLNode());
			t.getRight().setParent(t);
			t=t.getParent();
		}
		int count=rebalance(t.getRank()-t.getLeft().getRank(),t.getRank()-t.getRight().getRank(),t,0,true);
		return count;
	}
	private int rebalance(int i,int j,IAVLNode t,int count,boolean keep) {
		if(i==1&&j==1) {
			t.setHeight(1+Math.max(t.getLeft().getHeight(), t.getRight().getHeight()));
			if(t.getKey()==root.getKey()) {
				return count;
			}
			t=t.getParent();
			return  rebalance(t.getRank()-t.getLeft().getRank(),t.getRank()-t.getRight().getRank(),t,count,false);
		}
		if(i==2&&j==1) {
			t.setHeight(1+Math.max(t.getLeft().getHeight(), t.getRight().getHeight()));
			if(t.getKey()==root.getKey()) {
				return count;
			}
			t=t.getParent();
			return  rebalance(t.getRank()-t.getLeft().getRank(),t.getRank()-t.getRight().getRank(),t,count,false);
		}
		if(i==1&&j==2&&keep==true) {
			t.setHeight(1+Math.max(t.getLeft().getHeight(), t.getRight().getHeight()));
			if(t.getKey()==root.getKey()) {
				return count;
			}
			t=t.getParent();
			return  rebalance(t.getRank()-t.getLeft().getRank(),t.getRank()-t.getRight().getRank(),t,count,false);
		}
		if((i==0&&j==1)||(i==1&&j==0)&&keep==true) {
			count++;
			t.setRank(t.getRank()+1);
			t.setHeight(1+Math.max(t.getLeft().getHeight(), t.getRight().getHeight()));
			if(t.getKey()==root.getKey()) {
				return count;
			}
			t=t.getParent();
			return rebalance(t.getRank()-t.getLeft().getRank(),t.getRank()-t.getRight().getRank(),t,count,true);
		}
		if(i==0&&j==2&&keep==true) {
			int m=t.getLeft().getRank();
			int k=t.getLeft().getLeft().getRank();
			int l=t.getLeft().getRight().getRank();
			if(m-k==1&&m-l==1) {
				IAVLNode z=t;
				IAVLNode x=t.getLeft();
				IAVLNode y=t.getRight();
				x.setParent(z.getParent());
				if(z.getKey()!=root.getKey()) {
					if(z.getParent().getLeft().getKey()==z.getKey()) {
						z.getParent().setLeft(x);
					}
					else {
						z.getParent().setRight(x);
					}
				}
				else {
					root=x;
				}
				z.setRight(y);
				y.setParent(z);
				z.setLeft(x.getRight());
				z.getLeft().setParent(z);
				x.setRight(z);
				z.setParent(x);
				count =count+2;
				z.setHeight(1+Math.max(z.getLeft().getHeight(), z.getRight().getHeight()));
				x.setHeight(1+Math.max(x.getLeft().getHeight(), x.getRight().getHeight()));
				x.setRank(x.getRank()+1);
				if(t.getKey()==root.getKey()) {
					return count;
				}
				t=x.getParent();
				if(t==null) {
					return count;
				}
				return rebalance(t.getRank()-t.getLeft().getRank(),t.getRank()-t.getRight().getRank(),t,count,true);
			}
			if(m-k==1&&m-l==2) {
				IAVLNode z=t;
				IAVLNode x=t.getLeft();
				IAVLNode y=t.getRight();
				x.setParent(z.getParent());
				if(z.getKey()!=root.getKey()) {
					if(z.getParent().getLeft().getKey()==z.getKey()) {
						z.getParent().setLeft(x);
					}
					else {
						z.getParent().setRight(x);
					}
				}
				else {
					root=x;
				}
				z.setRight(y);
				y.setParent(z);
				z.setLeft(x.getRight());
				z.getLeft().setParent(z);
				z.setRank(z.getRank()-1);
				x.setRight(z);
				z.setParent(x);
				count =count+2;
				z.setHeight(1+Math.max(z.getLeft().getHeight(), z.getRight().getHeight()));
				x.setHeight(1+Math.max(x.getLeft().getHeight(), x.getRight().getHeight()));
				if(t.getKey()==root.getKey()) {
					return count;
				}
				t=x.getParent();
				if(t==null) {
					return count;
				}
				return rebalance(t.getRank()-t.getLeft().getRank(),t.getRank()-t.getRight().getRank(),t,count,false);
			}
			if(m-k==2&&m-l==1&&keep==true) {
				IAVLNode z=t;
				IAVLNode x=t.getLeft();
				IAVLNode y=t.getRight();
				IAVLNode b=x.getRight();
				b.setParent(z.getParent());
				if(z.getKey()!=root.getKey()) {
					if(z.getParent().getLeft().getKey()==z.getKey()) {
						z.getParent().setLeft(b);
					}
					else {
						z.getParent().setRight(b);
					}
				}
				else {
					root=b;
				}
				z.setLeft(b.getRight());
				z.getLeft().setParent(z);
				z.setRight(y);
				y.setParent(z);
				x.setRight(b.getLeft());
				x.getRight().setParent(x);
				b.setLeft(x);
				b.setRight(z);
				x.setParent(b);
				z.setParent(b);
				z.setRank(z.getRank()-1);
				x.setRank(x.getRank()-1);
				b.setRank(b.getRank()+1);
				count =count+4;
				z.setHeight(1+Math.max(z.getLeft().getHeight(), z.getRight().getHeight()));
				x.setHeight(1+Math.max(x.getLeft().getHeight(), x.getRight().getHeight()));
				b.setHeight(1+Math.max(b.getLeft().getHeight(), b.getRight().getHeight()));
				if(t.getKey()==root.getKey()) {
					return count;
				}
				t=b.getParent();
				if(t==null) {
					return count;
				}
				return rebalance(t.getRank()-t.getLeft().getRank(),t.getRank()-t.getRight().getRank(),t,count,false);
			}
		}
		if(i==2&&j==0&&keep==true) {
			int m=t.getRight().getRank();
			int k=t.getRight().getLeft().getRank();
			int l=t.getRight().getRight().getRank();
			if(m-k==1&&m-l==1) {
				IAVLNode z=t;
				IAVLNode x=t.getRight();
				IAVLNode y=t.getLeft();
				x.setParent(z.getParent());
				if(z.getKey()!=root.getKey()) {
					if(z.getParent().getLeft().getKey()==z.getKey()) {
						z.getParent().setLeft(x);
					}
					else {
						z.getParent().setRight(x);
					}
				}
				else {
					root=x;
				}
				z.setLeft(y);
				y.setParent(z);
				z.setRight(x.getLeft());
				z.getRight().setParent(z);
				x.setLeft(z);
				z.setParent(x);
				count =count+2;
				z.setHeight(1+Math.max(z.getLeft().getHeight(), z.getRight().getHeight()));
				x.setHeight(1+Math.max(x.getLeft().getHeight(), x.getRight().getHeight()));
				x.setRank(x.getRank()+1);
				if(t.getKey()==root.getKey()) {
					return count;
				}
				t=x.getParent();
				if(t==null) {
					return count;
				}
				return rebalance(t.getRank()-t.getLeft().getRank(),t.getRank()-t.getRight().getRank(),t,count,true);
			}
			if(m-k==2&&m-l==1) {
				IAVLNode z=t;
				IAVLNode x=t.getRight();
				IAVLNode y=t.getLeft();
				x.setParent(z.getParent());
				if(z.getKey()!=root.getKey()) {
					if(z.getParent().getLeft().getKey()==z.getKey()) {
						z.getParent().setLeft(x);
					}
					else {
						z.getParent().setRight(x);
					}
				}
				else {
					root=x;
				}
				z.setLeft(y);
				y.setParent(z);
				z.setRight(x.getLeft());
				z.getRight().setParent(z);
				z.setRank(z.getRank()-1);
				x.setLeft(z);
				z.setParent(x);
				count =count+2;
				z.setHeight(1+Math.max(z.getLeft().getHeight(), z.getRight().getHeight()));
				x.setHeight(1+Math.max(x.getLeft().getHeight(), x.getRight().getHeight()));
				if(t.getKey()==root.getKey()) {
					return count;
				}
				t=x.getParent();
				if(t==null) {
					return count;
				}
				return rebalance(t.getRank()-t.getLeft().getRank(),t.getRank()-t.getRight().getRank(),t,count,false);
			}
			if(m-k==1&&m-l==2&&keep==true) {
				IAVLNode z=t;
				IAVLNode x=t.getRight();
				IAVLNode y=t.getLeft();
				IAVLNode b=x.getLeft();
				b.setParent(z.getParent());
				if(z.getKey()!=root.getKey()) {
					if(z.getParent().getLeft().getKey()==z.getKey()) {
						z.getParent().setLeft(b);
					}
					else {
						z.getParent().setRight(b);
					}
				}
				else {
					root=b;
				}
				z.setRight(b.getLeft());
				z.getRight().setParent(z);
				z.setLeft(y);
				y.setParent(z);
				x.setLeft(b.getRight());
				x.getLeft().setParent(x);
				b.setRight(x);
				b.setLeft(z);
				x.setParent(b);
				z.setParent(b);
				z.setRank(z.getRank()-1);
				x.setRank(x.getRank()-1);
				b.setRank(b.getRank()+1);
				count =count+4;
				z.setHeight(1+Math.max(z.getLeft().getHeight(), z.getRight().getHeight()));
				x.setHeight(1+Math.max(x.getLeft().getHeight(), x.getRight().getHeight()));
				b.setHeight(1+Math.max(b.getLeft().getHeight(), b.getRight().getHeight()));
				if(t.getKey()==root.getKey()) {
					return count;
				}
				t=b.getParent();
				if(t==null) {
					return count;
				}
				return rebalance(t.getRank()-t.getLeft().getRank(),t.getRank()-t.getRight().getRank(),t,count,false);
			}
		}
		if(keep==false) {
			t.setHeight(1+Math.max(t.getLeft().getHeight(), t.getRight().getHeight()));
			if(t.getKey()==root.getKey()) {
				return count;
			}
			t=t.getParent();
			return rebalance(t.getRank()-t.getLeft().getRank(),t.getRank()-t.getRight().getRank(),t,count,false);
		}
		return count;
	}
	private IAVLNode isin(int k,IAVLNode t)
	{
		if(t==null) {
			return null;
		}
		if(t.getKey()==-1) {
			return t;
		}
		if(k==t.getKey()) {
			return t;
		}
		if(k<t.getKey()) {
			return isin(k,t.getLeft());
		}
		if(k>t.getKey()) {
			return isin(k,t.getRight());
		}

		return null;
	}

	/**
	 * public int delete(int k)
	 *
	 * deletes an item with key k from the binary tree, if it is there;
	 * the tree must remain valid (keep its invariants).
	 * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
	 * demotion/rotation - counted as one rebalnce operation, double-rotation is counted as 2.
	 * returns -1 if an item with key k was not found in the tree.
	 *
	 * O(log n)
	 */
	public int delete(int k)
	{
		IAVLNode t=isin(k,root);
		if(t==null)
			return -1;
		if (t.getKey()==-1)
			return -1;
		if(size()==0) {
			root=null;
			return 0;
		}
		if(t.getLeft().getKey()!=-1&&t.getRight().getKey()!=-1) {
			IAVLNode s=succesor(t);
			int key=t.getKey();
			String val=t.getValue();
			t.setKey(s.getKey());
			t.setValue(s.getValue());
			s.setKey(key);
			s.setValue(val);
			t=s;
		}
		if(t.getLeft().getKey()==-1&&t.getRight().getKey()==-1) {
			if(t.getKey()==root.getKey()) {
				root=null;
				return 0;
			}
			IAVLNode p =t.getParent();
			if(p.getLeft().getKey()==t.getKey()) {
				p.setLeft(new AVLNode());
				p.getLeft().setParent(p);
			}
			else {
				p.setRight(new AVLNode());
				p.getRight().setParent(p);
			}
			t=p;
			int count=rebalanceDelete(t.getRank()-t.getLeft().getRank(),t.getRank()-t.getRight().getRank(),t,0,true);
			return count;
		}
		if(t.getLeft().getKey()!=-1) {
			if(t.getKey()==root.getKey()) {
				root=t.getLeft();
				root.setParent(null);
				t=root;
			}
			else {
				IAVLNode p =t.getParent();
				if(p.getLeft().getKey()==t.getKey()) {
					p.setLeft(t.getLeft());
					p.getLeft().setParent(p);
				}
				else {
					p.setRight(t.getLeft());
					p.getRight().setParent(p);
				}
				t=p;
			}
			int count=rebalanceDelete(t.getRank()-t.getLeft().getRank(),t.getRank()-t.getRight().getRank(),t,0,true);
			return count;
		}
		if(t.getRight().getKey()!=-1) {
			if(t.getKey()==root.getKey()) {
				root=t.getRight();
				root.setParent(null);
				t=root;
			}
			else {
				IAVLNode p =t.getParent();
				if(p.getLeft().getKey()==t.getKey()) {
					p.setLeft(t.getRight());
					p.getLeft().setParent(p);
				}
				else {
					p.setRight(t.getRight());
					p.getRight().setParent(p);
				}
				t=p;
			}
			int count=rebalanceDelete(t.getRank()-t.getLeft().getRank(),t.getRank()-t.getRight().getRank(),t,0,true);
			return count;
		}
		int count=rebalanceDelete(t.getRank()-t.getLeft().getRank(),t.getRank()-t.getRight().getRank(),t,0,true);
		return count;
	}
	private int rebalanceDelete(int i,int j,IAVLNode t,int count,boolean keep) {
		if(i==2&&j==1) {
			t.setHeight(1+Math.max(t.getLeft().getHeight(), t.getRight().getHeight()));
			if(t.getKey()==root.getKey()) {
				return count;
			}
			t=t.getParent();
			return  rebalanceDelete(t.getRank()-t.getLeft().getRank(),t.getRank()-t.getRight().getRank(),t,count,false);
		}
		if(i==1&&j==2) {
			t.setHeight(1+Math.max(t.getLeft().getHeight(), t.getRight().getHeight()));
			if(t.getKey()==root.getKey()) {
				return count;
			}
			t=t.getParent();
			return  rebalanceDelete(t.getRank()-t.getLeft().getRank(),t.getRank()-t.getRight().getRank(),t,count,false);
		}
		if((i==3&&j==2 )&&keep==true) {
			count++;
			t.setRank(t.getRank()-1);
			t.setHeight(1+Math.max(t.getLeft().getHeight(), t.getRight().getHeight()));
			if(t.getKey()==root.getKey()) {
				return count;
			}
			t=t.getParent();
			return rebalanceDelete(t.getRank()-t.getLeft().getRank(),t.getRank()-t.getRight().getRank(),t,count,true);
		}
		if((i==2&&j==2 )&&keep==true) {
			count++;
			t.setRank(t.getRank()-1);
			t.setHeight(1+Math.max(t.getLeft().getHeight(), t.getRight().getHeight()));
			if(t.getKey()==root.getKey()) {
				return count;
			}
			t=t.getParent();
			return rebalanceDelete(t.getRank()-t.getLeft().getRank(),t.getRank()-t.getRight().getRank(),t,count,true);
		}
		if(i==3&&j==1&&keep==true) {
			int m=t.getRight().getRank();
			int k=t.getRight().getRight().getRank();
			int l=t.getRight().getLeft().getRank();
			if(m-k==1&&m-l==1) {
				IAVLNode z=t;
				IAVLNode x=t.getLeft();
				IAVLNode y=t.getRight();
				y.setParent(z.getParent());
				if(z.getKey()!=root.getKey()) {
					if(z.getParent().getLeft().getKey()==z.getKey()) {
						z.getParent().setLeft(y);
					}
					else {
						z.getParent().setRight(y);
					}
				}
				else {
					root=y;
				}
				z.setRight(y.getLeft());
				z.getRight().setParent(z);
				z.setRank(z.getRank()-1);
				y.setLeft(z);
				z.setParent(y);
				count =count+2;
				y.setRank(y.getRank()+1);
				z.setHeight(1+Math.max(z.getLeft().getHeight(), z.getRight().getHeight()));
				y.setHeight(1+Math.max(x.getLeft().getHeight(), x.getRight().getHeight()));
				if(t.getKey()==root.getKey()) {
					return count;
				}
				t=y.getParent();
				if(t==null) {
					return count;
				}
				return rebalanceDelete(t.getRank()-t.getLeft().getRank(),t.getRank()-t.getRight().getRank(),t,count,false);
			}
			if(m-k==1&&m-l==2) {
				IAVLNode z=t;
				IAVLNode y=t.getRight();
				y.setParent(z.getParent());
				if(z.getKey()!=root.getKey()) {
					if(z.getParent().getLeft().getKey()==z.getKey()) {
						z.getParent().setLeft(y);
					}
					else {
						z.getParent().setRight(y);
					}
				}
				else {
					root=y;
				}
				z.setRight(y.getLeft());
				z.getRight().setParent(z);
				z.setRank(z.getRank()-2);
				y.setLeft(z);
				z.setParent(y);
				count =count+2;
				z.setHeight(1+Math.max(z.getLeft().getHeight(), z.getRight().getHeight()));
				y.setHeight(1+Math.max(y.getLeft().getHeight(), y.getRight().getHeight()));
				if(t.getKey()==root.getKey()) {
					return count;
				}
				t=y.getParent();
				if(t==null) {
					return count;
				}
				return rebalanceDelete(t.getRank()-t.getLeft().getRank(),t.getRank()-t.getRight().getRank(),t,count,true);
			}
			if(m-k==2&&m-l==1) {
				IAVLNode z=t;
				IAVLNode y=t.getRight();
				IAVLNode a=y.getLeft();
				a.setParent(z.getParent());
				if(z.getKey()!=root.getKey()) {
					if(z.getParent().getLeft().getKey()==z.getKey()) {
						z.getParent().setLeft(a);
					}
					else {
						z.getParent().setRight(a);
					}
				}
				else {
					root=a;
				}
				z.setRight(a.getLeft());
				z.getRight().setParent(z);
				z.setRank(z.getRank()-2);
				y.setLeft(a.getRight());
				y.getLeft().setParent(y);
				a.setRight(y);
				y.setParent(a);
				a.setLeft(z);
				z.setParent(a);
				count =count+4;
				y.setRank(y.getRank()-1);
				a.setRank(a.getRank()+1);
				z.setHeight(1+Math.max(z.getLeft().getHeight(), z.getRight().getHeight()));
				y.setHeight(1+Math.max(y.getLeft().getHeight(), y.getRight().getHeight()));
				a.setHeight(1+Math.max(a.getLeft().getHeight(), a.getRight().getHeight()));
				if(t.getKey()==root.getKey()) {
					return count;
				}
				t=a.getParent();
				if(t==null) {
					return count;
				}
				return rebalanceDelete(t.getRank()-t.getLeft().getRank(),t.getRank()-t.getRight().getRank(),t,count,true);
			}
		}
		if(i==1&&j==3&&keep==true) {
			int m=t.getLeft().getRank();
			int k=t.getLeft().getLeft().getRank();
			int l=t.getLeft().getRight().getRank();
			if(m-k==1&&m-l==1) {
				IAVLNode z=t;
				IAVLNode x=t.getRight();
				IAVLNode y=t.getLeft();
				y.setParent(z.getParent());
				if(z.getKey()!=root.getKey()) {
					if(z.getParent().getLeft().getKey()==z.getKey()) {
						z.getParent().setLeft(y);
					}
					else {
						z.getParent().setRight(y);
					}
				}
				else {
					root= y;
				}
				z.setLeft(y.getRight());
				z.getLeft().setParent(z);
				z.setRank(z.getRank()-1);
				y.setRight(z);
				z.setParent(y);
				count =count+2;
				y.setRank(y.getRank()+1);
				z.setHeight(1+Math.max(z.getLeft().getHeight(), z.getRight().getHeight()));
				y.setHeight(1+Math.max(x.getLeft().getHeight(), x.getRight().getHeight()));
				if(t.getKey()==root.getKey()) {
					return count;
				}
				t=y.getParent();
				if(t==null) {
					return count;
				}
				return rebalanceDelete(t.getRank()-t.getLeft().getRank(),t.getRank()-t.getRight().getRank(),t,count,false);
			}
			if(m-k==1&&m-l==2) {
				IAVLNode z=t;
				IAVLNode y=t.getLeft();
				y.setParent(z.getParent());
				if(z.getKey()!=root.getKey()) {
					if(z.getParent().getLeft().getKey()==z.getKey()) {
						z.getParent().setLeft(y);
					}
					else {
						z.getParent().setRight(y);
					}
				}
				else {
					root=y;
				}
				z.setLeft(y.getRight());
				z.getLeft().setParent(z);
				z.setRank(z.getRank()-2);
				y.setRight(z);
				z.setParent(y);
				count =count+2;
				z.setHeight(1+Math.max(z.getLeft().getHeight(), z.getRight().getHeight()));
				y.setHeight(1+Math.max(y.getLeft().getHeight(), y.getRight().getHeight()));
				if(t.getKey()==root.getKey()) {
					return count;
				}
				t=y.getParent();
				if(t==null) {
					return count;
				}
				return rebalanceDelete(t.getRank()-t.getLeft().getRank(),t.getRank()-t.getRight().getRank(),t,count,true);
			}
			if(m-k==2&&m-l==1) {
				IAVLNode z=t;
				IAVLNode y=t.getLeft();
				IAVLNode a=y.getRight();
				a.setParent(z.getParent());
				if(z.getKey()!=root.getKey()) {
					if(z.getParent().getLeft().getKey()==z.getKey()) {
						z.getParent().setLeft(a);
					}
					else {
						z.getParent().setRight(a);
					}
				}
				else {
					root=a;
				}
				z.setLeft(a.getRight());
				z.getLeft().setParent(z);
				z.setRank(z.getRank()-2);
				y.setRight(a.getLeft());
				y.getRight().setParent(y);
				a.setLeft(y);
				y.setParent(a);
				a.setRight(z);
				z.setParent(a);
				count =count+4;
				y.setRank(y.getRank()-1);
				a.setRank(a.getRank()+1);
				z.setHeight(1+Math.max(z.getLeft().getHeight(), z.getRight().getHeight()));
				y.setHeight(1+Math.max(y.getLeft().getHeight(), y.getRight().getHeight()));
				a.setHeight(1+Math.max(a.getLeft().getHeight(), a.getRight().getHeight()));
				if(t.getKey()==root.getKey()) {
					return count;
				}
				t=a.getParent();
				if(t==null) {
					return count;
				}
				return rebalanceDelete(t.getRank()-t.getLeft().getRank(),t.getRank()-t.getRight().getRank(),t,count,true);
			}
		}

		if(keep==false) {
			t.setHeight(1+Math.max(t.getLeft().getHeight(), t.getRight().getHeight()));
			if(t.getKey()==root.getKey()) {
				return count;
			}
			t=t.getParent();
			return rebalanceDelete(t.getRank()-t.getLeft().getRank(),t.getRank()-t.getRight().getRank(),t,count,false);
		}
		return count;
	}
	private IAVLNode succesor(IAVLNode t)
	{
		if(t.getRight().getKey()==-1) {
			while(t.getParent().getRight().getKey()==t.getKey()) {
				t=t.getParent();
			}
			t=t.getParent();
		}
		else {
			t=t.getRight();
			while(t.getLeft().getKey()!=-1) {
				t=t.getLeft();
			}
		}
		return t;
	}

	/**
	 * public String min()
	 *
	 * Returns the info of the item with the smallest key in the tree,
	 * or null if the tree is empty
	 *
	 * O(log n)
	 */
	public String min()
	{
		if(this.empty()) {
			return null;
		}
		return min2(root);
	}
	private String min2(IAVLNode t)
	{
		IAVLNode left=t.getLeft();
		if(left.getKey()==-1) {
			return t.getValue();
		}
		return min2(left);
	}

	/**
	 * public String max()
	 *
	 * Returns the info of the item with the largest key in the tree,
	 * or null if the tree is empty
	 *
	 * O(log n)
	 */
	public String max()
	{
		if(this.empty()) {
			return null;
		}
		return max2(root);
	}
	private String max2(IAVLNode t)
	{
		IAVLNode right=t.getRight();
		if(right.getKey()==-1) {
			return t.getValue();
		}
		return max2(right);
	}
	public IAVLNode searchNode(IAVLNode root, int x) {
		if (isin(x,root).isRealNode()){
			IAVLNode node = root;
			while (node.isRealNode()) {
				if (x > node.getKey() && node.getRight().isRealNode()) {
					node = node.getRight();
				}
				else if (x < node.getKey() && node.getLeft().isRealNode()) {
					node = node.getLeft();
				}
				else if(x==node.getKey()){
					break;
				}
			}
			return node;
		}
		return null;
	}

	/**
	 * public int[] keysToArray()
	 *
	 * Returns a sorted array which contains all keys in the tree,
	 * or an empty array if the tree is empty.
	 *
	 * O(n)
	 */
	public int[] keysToArray()
	{
		int[] arr = new int[size()];
		if(this.empty()) {
			return arr;
		}
		keysToArray2(root, arr,0);
		return arr;
	}
	private int keysToArray2(IAVLNode t,int[] arr,int k)
	{
		if(t.getKey()==-1)
			return k;
		int j=keysToArray2(t.getLeft(),arr,k);
		arr[j]=t.getKey();
		j++;
		return keysToArray2(t.getRight(),arr,j);
	}

	/**
	 * public String[] infoToArray()
	 *
	 * Returns an array which contains all info in the tree,
	 * sorted by their respective keys,
	 * or an empty array if the tree is empty.
	 *
	 * O(n)
	 */
	public String[] infoToArray()
	{
		String[] arr = new String[size()];
		if(this.empty()) {
			return arr;
		}
		infoToArray2(root, arr,0);
		return arr;
	}
	private int infoToArray2(IAVLNode t,String[] arr,int k)
	{
		if(t.getKey()==-1)
			return k;
		int j=infoToArray2(t.getLeft(),arr,k);
		arr[j]=t.getValue();
		j++;
		return infoToArray2(t.getRight(),arr,j);
	}

	/**
	 * public int size()
	 *
	 * Returns the number of nodes in the tree.
	 *
	 * precondition: none
	 * postcondition: none
	 *
	 * O(n)
	 */
	public int size()
	{
		return size2(root);
	}
	private int size2(IAVLNode t)
	{
		if(t==null)
			return 0;
		if(t.getKey()==-1)
			return 0;
		return 1+size2(t.getLeft())+size2(t.getRight());
	}

	/**
	 * public int getRoot()
	 *
	 * Returns the root AVL node, or null if the tree is empty
	 *
	 * precondition: none
	 * postcondition: none
	 *
	 * O(1)
	 */
	public IAVLNode getRoot()
	{
		return root;
	}
	/**
	 * public string split(int x)
	 *
	 * splits the tree into 2 trees according to the key x.
	 * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
	 * precondition: search(x) != null (i.e. you can also assume that the tree is not empty)
	 * postcondition: none
	 *
	 * O(log n)
	 */
	public AVLTree[] split(int x)
	{
		AVLTree[] answer= new AVLTree[2];
		IAVLNode t=isin(x,root);
		if(t.getLeft().getKey()!=-1)
			answer[0]=new AVLTree(t.getLeft());
		if(t.getRight().getKey()!=-1)
			answer[1]=new AVLTree(t.getRight());
		if(t.getKey()==root.getKey())
			return answer;
		t=t.getParent();
		IAVLNode p=null;
		while(t.getKey()!=root.getKey()) {
			if(t.getKey()<x) {
				p=t.getParent();
				answer[0].join(t,new AVLTree( t.getLeft()));
			}
			if(t.getKey()>x) {
				p=t.getParent();
				answer[1].join(t,new AVLTree( t.getRight()));
			}
			t=p;
		}
		if(t.getKey()<x) {
			answer[0].join(t,new AVLTree( t.getLeft()));
		}
		if(t.getKey()>x) {
			answer[1].join(t,new AVLTree( t.getRight()));
		}
		return answer;
	}
	/**
	 * public join(IAVLNode x, AVLTree t)
	 *
	 * joins t and x with the tree.
	 * Returns the complexity of the operation (|tree.rank - t.rank| + 1).
	 * precondition: keys(x,t) < keys() or keys(x,t) > keys(). t/tree might be empty (rank = -1).
	 * postcondition: none
	 *
	 * O(log n)
	 */
	public int join(IAVLNode x, AVLTree t)
	{
		IAVLNode tmp=null;
		IAVLNode t1=null;
		IAVLNode t2=null;
		if(t.getRoot()==null&&this.getRoot()==null) {
			this.insert(x.getKey(), x.getValue());
			return 1;
		}
		if(t.getRoot()==null) {
			this.insert(x.getKey(), x.getValue());
			return (this.getRoot().getHeight()+1);
		}
		if(this.getRoot()==null) {
			t.insert(x.getKey(), x.getValue());
			this.root=t.getRoot();
			return (t.getRoot().getHeight()+1);
		}
		int h1=t.getRoot().getHeight();
		int h2=this.getRoot().getHeight();
		if(h1>h2) {
			t1=this.getRoot();
			t2=t.getRoot();
		}
		if(h2>h1) {
			t2=this.getRoot();
			t1=t.getRoot();
		}
		if(h2==h1) {
			if(t.getRoot().getKey()<x.getKey()) {
				x.setLeft(t.getRoot());
				x.setRight(this.getRoot());
				root=x;
				return 1;
			}
			else {
				x.setRight(t.getRoot());
				x.setLeft(this.getRoot());
				root=x;
				return 1;
			}
		}
		//t2 is the highest tree
		tmp=t2;
		if(t2.getKey()>t1.getKey()) {
			while(tmp.getRank()>t1.getRank()) {
				tmp=tmp.getLeft();
			}
			x.setLeft(t1);
			x.setRight(tmp);
			x.setParent(tmp.getParent());
			x.getParent().setLeft(x);
			x.setHeight(1+Math.max(t1.getHeight(),tmp.getHeight()));
			x.setRank(t1.getRank()+1);
		}
		if(t2.getKey()<t1.getKey()) {
			while(tmp.getRank()>t1.getRank()) {
				tmp=tmp.getRight();
			}
			x.setLeft(tmp);
			x.setRight(t1);
			x.setParent(tmp.getParent());
			x.getParent().setRight(x);
			x.setHeight(1+Math.max(t1.getHeight(),tmp.getHeight()));
			x.setRank(t1.getRank()+1);
		}
		root = t2;
		root.setParent(null);
		x=x.getParent();
		rebalance(x.getRank()-x.getLeft().getRank(),x.getRank()-x.getRight().getRank(),x,0,true);
		return (Math.abs(h1-h2)+1);
	}
	public void setRoot(AVLTree t,IAVLNode node){
		t.root=node;
	}

	/**
	 * public interface IAVLNode
	 * ! Do not delete or modify this - otherwise all tests will fail !
	 */
	public interface IAVLNode{
		public int getKey(); //returns node's key (for virtuval node return -1)
		public void setKey(int k); //set node's key
		public String getValue(); //returns node's value [info] (for virtuval node return null)
		public void setValue(String s); //set node's value
		public void setLeft(IAVLNode node); //sets left child
		public IAVLNode getLeft(); //returns left child (if there is no left child return null)
		public void setRight(IAVLNode node); //sets right child
		public IAVLNode getRight(); //returns right child (if there is no right child return null)
		public void setParent(IAVLNode node); //sets parent
		public IAVLNode getParent(); //returns the parent (if there is no parent return null)
		public boolean isRealNode(); // Returns True if this is a non-virtual AVL node
		public void setHeight(int height); // sets the height of the node
		public int getHeight(); // Returns the height of the node (-1 for virtual nodes)
		public int getRank(); // Returns the rank of the node (-1 for virtual nodes)
		public void setRank(int r); //sets the rank of the node
		public AVLNode getMax();
	}

	/**
	 * public class AVLNode
	 *
	 * If you wish to implement classes other than AVLTree
	 * (for example AVLNode), do it in this file, not in
	 * another file.
	 * This class can and must be modified.
	 * (It must implement IAVLNode)
	 */
	public class AVLNode implements IAVLNode {
		private int key;
		private String value;
		private IAVLNode left;
		private IAVLNode right;
		private IAVLNode parent;
		private int height;
		private int rank;

		public AVLNode(int k, String i) {
			this.key = k;
			this.value = i;
			this.height = 0;
			this.rank = 0;
			AVLNode right = new AVLNode();
			right.setParent(this);
			IAVLNode left = new AVLNode();
			left.setParent(this);
			this.right = right;
			this.left = left;
		}

		public AVLNode() {
			this.key = -1;
			this.height = -1;
			this.rank = -1;
			this.value = null;
			this.left = null;
			this.right = null;
		}

		public void setKey(int k) {
			this.key = k;
		}

		public int getKey() {
			return this.key;
		}

		public int getRank() {
			return this.rank;
		}

		public void setRank(int r) {
			this.rank = r;
		}

		public String getValue() {
			return this.value;
		}

		public void setValue(String s) {
			this.value = s;
		}

		public void setLeft(IAVLNode node) {
			this.left = node;
		}

		public IAVLNode getLeft() {
			return this.left;
		}

		public void setRight(IAVLNode node) {
			this.right = node;
		}

		public IAVLNode getRight() {
			return this.right;
		}

		public void setParent(IAVLNode node) {
			this.parent = node;
		}

		public IAVLNode getParent() {
			return this.parent;
		}

		// Returns True if this is a non-virtual AVL node
		public boolean isRealNode() {
			return this.key != -1;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public int getHeight() {
			return this.height;
		}

		public AVLNode getMax() {
			while (this.getRight().getKey() != -1) {
				return this.getRight().getMax();
			}
			return this;
		}
		}
	}





