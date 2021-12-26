public class BacktrackingBST implements Backtrack, ADTSet<BacktrackingBST.Node> {
    private Stack stack;
    private Stack redoStack;
    BacktrackingBST.Node root = null;

    // Do not change the constructor's signature
    public BacktrackingBST(Stack stack, Stack redoStack) {
        this.stack = stack;
        this.redoStack = redoStack;
    }

    public Node getRoot() {
        return root;
    }

    public Node search(int x) {
        BacktrackingBST.Node temp=getRoot();
        while (temp!=null)
        {
            if(temp.getKey()==x)
            {
                return temp;
            }
            if (temp.getKey()>x)
            {
                temp=temp.getLeft();
            }
            else
            {
                temp=temp.getRight();
            }
        }
        return null;
    }

    public void insert(BacktrackingBST.Node z) {
        boolean side;
        if(root==null) {
            root=z;
            z.setParent(null);
            side=true;
        }
        else
        {
            BacktrackingBST.Node temp=getRoot();
            BacktrackingBST.Node tempParent=getRoot();
            while (temp!=null)
            {
                if (temp!=getRoot())
                {
                    tempParent=temp;
                }
                if (temp.getKey()>z.getKey())
                {
                    temp=temp.getLeft();
                }
                else
                {
                    temp=temp.getRight();
                }
            }
            if (tempParent.getKey()>z.getKey())
            {
                tempParent.setLeft(z);
            }
            else
            {
                tempParent.setRight(z);
            }
            z.setParent(tempParent);
            if(z.getParent().getLeft()==z)
                side=false;
            else
                side=true;
        }
        BSTData inserted= new BSTData(z,true,0,z.getParent(),side, null,true);
        stack.push(inserted);
        redoStack.clear();
    }

    public void delete(Node x) {
        boolean side=true;
        if (x.getParent()!=null)
        {
            if(x.getParent().getLeft()==x)
                side=false;
            else
                side=true;
        }

        if(x.getRight()==null & x.getLeft()==null) { // x is a leaf
            if (x.getParent()==null)
            {
                BSTData deleted= new BSTData(x,false,1,null,true,null,true);
                root=null;
                stack.push(deleted);
                redoStack.clear();
            }
            else
            {
                BSTData deleted = new BSTData(x, false, 1, x.getParent(), side, null, true);
                if (!side)
                    x.getParent().setLeft(null);
                else
                    x.getParent().setRight(null);
                stack.push(deleted);
                redoStack.clear();
            }
        }
        else if( (x.getRight()==null & x.getLeft()!=null) | (x.getRight()!=null & x.getLeft()==null)){ // x has exactly one son
            BSTData deleted= new BSTData(x,false,2,x.getParent(),side,null,true);
            if (x.getParent()==null)
            {
                if (x.getRight()!=null)
                {
                    root=x.getRight();
                    x.getRight().setParent(null);
                }
                else
                {
                    root=x.getLeft();
                    x.getLeft().setParent(null);
                }
            }
            else
            {
                if (x.getRight() == null) {
                    if (!side)
                        x.getParent().setLeft(x.getLeft());
                    else
                        x.getParent().setRight(x.getLeft());
                    x.getLeft().setParent(x.getParent());
                } else {
                    if (!side)
                        x.getParent().setLeft(x.getRight());
                    else
                        x.getParent().setRight(x.getRight());
                    x.getRight().setParent(x.getParent());
                }

            }
            stack.push(deleted);
            redoStack.clear();
        }
        else { //x has two sons
            Node current = successor(x);
            boolean SuccessParent;
            if (current.getParent().getLeft() == current)
                SuccessParent = false;
            else
                SuccessParent = true;
            BSTData deleted = new BSTData(x, false, 3, x.getParent(), side, current.getParent(), SuccessParent);
            boolean sucSide;
            if (current.getParent().getLeft() == current) {
                current.getParent().setLeft(null);
                sucSide = false;
            }
            else {
                current.getParent().setRight(null);
                sucSide = true;
            }
            current.setLeft(x.getLeft());
            current.getLeft().setParent(current);
            if (current.getRight()==null)
            {
                current.setRight(x.getRight());
                if (current.getRight()!=null)
                {
                    current.getRight().setParent(current);
                }
            }
            else
            {
                if (sucSide)
                {
                    current.getParent().setRight(current.getRight());
                    current.getParent().getRight().setParent(current.getParent());
                }
                else {
                    current.getParent().setLeft(current.getRight());
                    current.getParent().getLeft().setParent(current.getParent());
                }
                current.setRight(x.getRight());
                if (current.getRight()!=null)
                {
                    current.getRight().setParent(current);
                }
            }

            if (x.getParent()==null)
            {
                root=current;
                current.setParent(null);
            }
            else
            {
                if (!side)
                    x.getParent().setLeft(current);
                else
                    x.getParent().setRight(current);
                current.setParent(x.getParent());

            }
            stack.push(deleted);
            redoStack.clear();
        }
    }


    public Node minimum() {
        if(root==null)
            return null;
        Node x= root;
        while(x.getLeft()!=null)
            x=x.getLeft();
        return x;
    }

    public Node maximum() {
        if(root==null)
            return null;
        Node x= root;
        while(x.getRight()!=null)
            x=x.getRight();
        return x;
    }

    public Node successor(Node x) {
        if(x.getRight()!=null)
            return minimum(x.getRight());
        else {
            Node y=x;
            while(y.getParent()!=null & y.getParent().getRight()==y)
                y=y.getParent();
            return y.getParent();
        }
    }

    public Node predecessor(Node x) {
        if(x.getLeft()!=null)
            return minimum(x.getLeft());
        else {
            Node y=x;
            while(y.getParent()!=null & y.getParent().getLeft()==y)
                y=y.getParent();
            return y.getParent();
        }
    }

    @Override
    public void backtrack() {
        if(!stack.isEmpty()) {
            BSTData Curr = (BSTData) stack.pop();
            redoStack.push(Curr);
            if (!Curr.action) {
                Curr.node.setRight(null);
                Curr.node.setLeft(null);
                if (Curr.deletetype == 1) {
                    if (root == null)
                        root = Curr.node;
                    else {
                        if (Curr.side)
                            Curr.parent.setRight(Curr.node);
                        else
                            Curr.parent.setLeft(Curr.node);
                    }
                } else if (Curr.deletetype == 2) {
                    if (Curr.parent == null) {
                        if (root.getKey() > Curr.node.getKey()) {
                            Curr.node.setRight(root);
                            Curr.node.getRight().setParent(Curr.node);
                            root = Curr.node;
                        } else {
                            Curr.node.setLeft(root);
                            Curr.node.getLeft().setParent(Curr.node);
                            root = Curr.node;
                        }
                    } else if (Curr.side) {
                        if (Curr.parent.getRight().getKey() > Curr.node.getKey()) {
                            Curr.node.setRight(Curr.parent.getRight());
                            if (Curr.node.getRight() != null) {
                                Curr.node.getRight().setParent(Curr.node);
                            }
                        } else {
                            Curr.node.setLeft(Curr.parent.getRight());
                            Curr.node.getLeft().setParent(Curr.node);
                        }
                        Curr.parent.setRight(Curr.node);
                    } else {
                        if (Curr.parent.getLeft().getKey() > Curr.node.getKey()) {
                            Curr.node.setRight(Curr.parent.getLeft());
                            if (Curr.node.getRight() != null) {
                                Curr.node.getRight().setParent(Curr.node);
                            }
                        } else {
                            Curr.node.setLeft(Curr.parent.getLeft());
                            Curr.node.getLeft().setParent(Curr.node);
                        }
                        Curr.parent.setLeft(Curr.node);
                    }
                } else if (Curr.deletetype == 3) {
                    if (Curr.parent == null) {
                        Curr.node.setRight(root.getRight());

                        if (Curr.node.getRight() != null) {
                            root.getRight().setParent(Curr.node);
                        }
                        Curr.node.setLeft(root.getLeft());
                        root.getLeft().setParent(Curr.node);
                        root.setRight(null);
                        root.setLeft(null);
                        Node temp = root;
                        root = Curr.node;
                        if (Curr.successorside) {
                            if (Curr.successorparent.getRight() == null) {
                                Curr.successorparent.setRight(temp);
                            } else {
                                temp.setRight(Curr.successorparent.getRight());
                                Curr.successorparent.setRight(temp);
                            }
                        } else {
                            if (Curr.successorparent.getLeft() == null) {
                                Curr.successorparent.setLeft(temp);
                            } else {
                                temp.setRight(Curr.successorparent.getLeft());
                                Curr.successorparent.setLeft(temp);
                            }
                        }
                        temp.setParent(Curr.successorparent);
                    } else if (Curr.side) {
                        Curr.node.setRight(Curr.parent.getRight().getRight());
                        if (Curr.node.getRight() != null) {
                            Curr.node.getRight().setParent(Curr.node);
                        }
                        Curr.node.setLeft(Curr.parent.getRight().getLeft());
                        Curr.node.getLeft().setParent(Curr.node);
                        Curr.parent.getRight().setRight(null);
                        Curr.parent.getRight().setLeft(null);
                        Node temp = Curr.parent.getRight();
                        Curr.parent.setRight(Curr.node);
                        if (Curr.successorside) {
                            if (Curr.successorparent.getRight() == null) {
                                Curr.successorparent.setRight(temp);
                            } else {
                                temp.setRight(Curr.successorparent.getRight());
                                Curr.successorparent.setRight(temp);
                            }
                        } else {
                            if (Curr.successorparent.getLeft() == null) {
                                Curr.successorparent.setLeft(temp);
                            } else {
                                temp.setRight(Curr.successorparent.getLeft());
                                Curr.successorparent.setLeft(temp);
                            }
                        }
                        temp.setParent(Curr.successorparent);
                    } else {
                        Curr.node.setRight(Curr.parent.getLeft().getRight());
                        if (Curr.node.getRight() != null) {
                            Curr.node.getRight().setParent(Curr.node);
                        }
                        Curr.node.setLeft(Curr.parent.getLeft().getLeft());
                        Curr.node.getLeft().setParent(Curr.node);
                        Curr.parent.getLeft().setRight(null);
                        Curr.parent.getLeft().setLeft(null);
                        Node temp = Curr.parent.getLeft();
                        Curr.parent.setLeft(Curr.node);
                        if (Curr.successorside) {
                            if (Curr.successorparent.getRight() == null) {
                                Curr.successorparent.setRight(temp);
                            } else {
                                temp.setRight(Curr.successorparent.getRight());
                                Curr.successorparent.setRight(temp);
                            }
                        } else {
                            if (Curr.successorparent.getLeft() == null) {
                                Curr.successorparent.setLeft(temp);
                            } else {
                                temp.setRight(Curr.successorparent.getLeft());
                                Curr.successorparent.setLeft(temp);
                            }
                        }
                        temp.setParent(Curr.successorparent);
                    }

                }
            } else {
                if (Curr.side)
                    Curr.parent.setRight(null);
                else
                    Curr.parent.setLeft(null);
            }
           System.out.println("backtracking performed");
        }
    }

    @Override
    public void retrack() {
        if (!redoStack.isEmpty()) {
            BSTData curr = (BSTData) redoStack.pop();
            stack.push(curr);
            if (curr.action) {
                if (curr.parent == null) {
                    root = curr.node;
                } else {
                    if (curr.side)
                        curr.parent.setRight(curr.node);
                    else
                        curr.parent.setLeft(curr.node);
                }
            } else {
                if (curr.deletetype == 1) {
                    if (curr.parent == null)
                        root = null;
                    else {
                        if (curr.side)
                            curr.parent.setRight(null);
                        else
                            curr.parent.setLeft(null);
                    }
                } else if (curr.deletetype == 2) {
                    if (curr.parent == null) {
                        if (curr.node.getRight() != null) {
                            root = curr.node.getRight();
                            curr.node.getRight().setParent(null);
                        } else {
                            root = curr.node.getLeft();
                            curr.node.getLeft().setParent(null);
                        }
                    } else if (curr.side) {
                        if (curr.node.getRight() != null) {
                            curr.parent.setRight(curr.node.getRight());
                            curr.node.getRight().setParent(curr.parent);
                        } else {
                            curr.parent.setRight(curr.node.getLeft());
                            curr.node.getLeft().setParent(curr.parent);
                        }
                    } else {
                        if (curr.node.getRight() != null) {
                            curr.parent.setLeft(curr.node.getRight());
                            curr.node.getRight().setParent(curr.parent);
                        } else {
                            curr.parent.setLeft(curr.node.left);
                            curr.node.getLeft().setParent(curr.parent);
                        }
                    }

                } else if (curr.deletetype == 3) {
                    Node replace;
                    if (curr.successorside) {
                        replace = curr.successorparent.getRight();
                        curr.successorparent.setRight(null);
                    } else {
                        replace = curr.successorparent.getLeft();
                        curr.successorparent.setLeft(null);
                    }
                    replace.setLeft(curr.node.getLeft());
                    replace.getLeft().setParent(replace);
                    if (replace.getRight() == null) {
                        replace.setRight(curr.node.getRight());
                        if (replace.getRight() != null) {
                            replace.getRight().setParent(replace);
                        }
                    } else {
                        if (curr.successorside) {
                            curr.successorparent.setRight(replace.getRight());
                            curr.successorparent.getRight().setParent(curr.successorparent);
                        } else {
                            curr.successorparent.setLeft(replace.getRight());
                            curr.successorparent.getLeft().setParent(curr.successorparent);
                        }
                        replace.setRight(curr.node.getRight());
                        if (replace.getRight() != null) {
                            replace.getRight().setParent(replace);
                        }
                    }

                    if (curr.node.getParent() == null) {
                        root = replace;
                        replace.setParent(null);
                    } else {
                        if (!curr.side)
                            curr.node.getParent().setLeft(replace);
                        else
                            curr.node.getParent().setRight(replace);
                        replace.setParent(curr.node.getParent());

                    }
                }
            }
        }
    }



    public void printPreOrder(){
        if(root!=null)
            printPreOrder(root);
    }

    @Override
    public void print() {
        printPreOrder();
    }
    public void printPreOrder(Node x)
    {
        System.out.print(x.getKey()+" ");
        if(x.getLeft()!=null)
            printPreOrder(x.getLeft());
        if(x.getRight()!=null)
            printPreOrder(x.getRight());
    }
    public Node minimum(Node x) {
        if(x.getLeft()==null)
            return x;
        Node y=x;
        while(y.getLeft()!=null)
            y=y.getLeft();
        return y;
    }

    public Node maximum(Node x) {
        if(x.getRight()==null)
            return null;
        Node y= x;
        while(y.getRight()!=null)
            y=y.getRight();
        return y;
    }

    public static class Node{


        //These fields are public for grading purposes. By coding conventions and best practice they should be private.
        public BacktrackingBST.Node left;
        public BacktrackingBST.Node right;

        private BacktrackingBST.Node parent;
        private int key;
        private Object value;

        public Node(int key, Object value) {
            this.key = key;
            this.value = value;
        }

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        public Node getParent() {
            return parent;
        }
        public void setParent(Node parent) {
            this.parent=parent;
        }

        public int getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }
        public Node search( int x) {
            if(this.getKey()==x)
                return this;

            else {
                if (this.getKey()<x & this.right!=null)
                    return right.search(x);
                else if(this.left!=null)
                    return left.search(x);
            }
            return null;
        }
    }

    public static class BSTData {
        public BacktrackingBST.Node node;
        public boolean action; // True=Insert, False=Delete
        public Integer deletetype;   // 0 - No meaning, 1 - leaf, 2 - has one son, 3 - has two sons
        public BacktrackingBST.Node parent; // Our inserted Node's parent
        public boolean side; // True=right, False=left
        public BacktrackingBST.Node successorparent; // essential for #3 deletion: the replacing Node's parent
        public boolean successorside; // essential for #3 deletion: the replacing Node's parent side -  True=right, False=left
        public BSTData(BacktrackingBST.Node node ,boolean action ,Integer type, BacktrackingBST.Node parent, boolean side, BacktrackingBST.Node successorparent, boolean successorside ) {
            this.node=node;
            this.action=action;
            this.deletetype=type;
            this.parent=parent;
            this.side=side;
            this.successorparent=successorparent;
            this.successorside=successorside;
        }

    }

}

