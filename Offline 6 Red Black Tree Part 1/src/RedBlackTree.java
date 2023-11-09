class Node{
    int data;
    Node parent;
    Node left;
    Node right;
    int color;
}

public class RedBlackTree {

    private Node root;
    private Node nullNode;

    public RedBlackTree(){
        nullNode = new Node();
        nullNode.parent = null;
        nullNode.left = null;
        nullNode.right = null;
        nullNode.color = 0;
        root = nullNode;
    }

    private Node searchTreeHelper(Node node, int key){
        if(node == nullNode || key == node.data){
            return node;
        }
        if(key < node.data){
            return searchTreeHelper(node.left, key);
        }
        else{
            return searchTreeHelper(node.right, key);
        }
    }

    public Node searchTree(int key){
        return searchTreeHelper(root, key);
    }

    public Node min(Node node){
        while(node.left != nullNode){
            node = node.left;
        }
        return node;
    }

    public Node max(Node node){
        while(node.right != nullNode){
            node = node.right;
        }
        return node;
    }

    public void rotateLeft(Node node){
        Node r = node.right;
        node.right = r.left;
        if(r.left != nullNode){
            r.left.parent = node;
        }
        r.parent = node.parent;
        if(node.parent == null){
            root = r;
        }
        else if(node == node.parent.left){
            node.parent.left = r;
        }
        else{
            node.parent.right = r;
        }
        r.left = node;
        node.parent = r;
    }

    public void rotateRight(Node node){
        Node l = node.left;
        node.left = l.right;
        if(l.right != nullNode){
            l.right.parent = node;
        }
        l.parent = node.parent;
        if(node.parent == null){
            root = l;
        }
        else if(node == node.parent.left){
            node.parent.left = l;
        }
        else{
            node.parent.right = l;
        }
        l.right = node;
        node.parent = l;
    }

    public void insert(int key){
        Node node = new Node();
        node.parent = null;
        node.left = nullNode;
        node.right = nullNode;
        node.data = key;
        node.color = 1;

        Node p = null, tmp = root;
        while(tmp != nullNode){
            p = tmp;
            if(node.data < tmp.data){
                tmp = tmp.left;
            }
            else{
                tmp = tmp.right;
            }
        }

        node.parent = p;
        if(p == null){
            root = node;
        }
        else if(p.data < node.data){
            p.right = node;
        }
        else{
            p.left = node;
        }

        if(node.parent == null){
            node.color = 0;
            return;
        }
        else if(node.parent.parent == null){
            return;
        }
        fixInsert(node);

    }

    private void fixInsert(Node node) {
        Node u;
        while(node.parent.color == 1){
            if(node.parent == node.parent.parent.right){
                u = node.parent.parent.left;
                if(u.color == 1){
                    u.color = 0;
                    node.parent.color = 0;
                    node.parent.parent.color = 1;
                    node = node.parent.parent;
                }
                else{
                    if(node == node.parent.left){
                        node = node.parent;
                        rotateRight(node);
                    }
                    node.parent.color = 0;
                    node.parent.parent.color = 1;
                    rotateLeft(node.parent.parent);
                }
            }
            else{
                u = node.parent.parent.right;
                if(u.color == 1){
                    u.color = 0;
                    node.parent.color = 0;
                    node.parent.parent.color = 1;
                    node = node.parent.parent;
                }
                else{
                    if(node == node.parent.right){
                        node = node.parent;
                        rotateLeft(node);
                    }
                    node.parent.color = 0;
                    node.parent.parent.color = 1;
                    rotateRight(node.parent.parent);
                }

            }
            if(node == root)
               break;
        }
        root.color = 0;

    }


    private void printHelper(Node root, String indent, boolean last) {
        // print the tree structure on the screen
        if (root != nullNode) {
            System.out.print(indent);
            if (last) {
                System.out.print("R----");
                indent += "     ";
            } else {
                System.out.print("L----");
                indent += "|    ";
            }

            String sColor = root.color == 1?"RED":"BLACK";
            System.out.println(root.data + "(" + sColor + ")");
            printHelper(root.left, indent, false);
            printHelper(root.right, indent, true);
        }
    }


    public void prettyPrint() {
        printHelper(this.root, "", true);
    }

    private void preOrderHelper(Node node) {
        if (node != nullNode) {
            System.out.print(node.data + " ");
            preOrderHelper(node.left);
            preOrderHelper(node.right);
        }
    }

    private void inOrderHelper(Node node) {
        if (node != nullNode) {
            inOrderHelper(node.left);
            System.out.print(node.data + " ");
            inOrderHelper(node.right);
        }
    }

    private void postOrderHelper(Node node) {
        if (node != nullNode) {
            postOrderHelper(node.left);
            postOrderHelper(node.right);
            System.out.print(node.data + " ");
        }
    }

    // Pre-Order traversal
    // Node.Left Subtree.Right Subtree
    public void preorder() {
        preOrderHelper(this.root);
    }

    // In-Order traversal
    // Left Subtree . Node . Right Subtree
    public void inorder() {
        inOrderHelper(this.root);
    }

    // Post-Order traversal
    // Left Subtree . Right Subtree . Node
    public void postorder() {
        postOrderHelper(this.root);
    }

    private void nodeTransplant(Node u, Node v){
        if(u.parent == null){
            root = v;
        }
        else if(u == u.parent.left){
            u.parent.left = v;
        }
        else{
            u.parent.right = v;
        }
        v.parent = u.parent;
    }

    private void deleteNodeHelper(Node node, int key){
        Node node1 = nullNode;
        while(node != nullNode){
            if(node.data == key){
                node1 = node;
                break;
            }
            else if(key < node.data){
                node = node.left;
            }
            else{
                node = node.right;
            }
        }
        if(node1 == nullNode){
            System.out.println("No such key");
            return;
        }

        Node node2, node3;
        node2 = node1;
        int saveColor = node2.color;
        if(node1.left == nullNode){
            node3 = node1.right;
            nodeTransplant(node1, node1.right);
        }
        else if(node1.right == nullNode){
            node3 = node1.left;
            nodeTransplant(node1, node1.left);
        }
        else{
            node2 = min(node1.right);
            saveColor = node2.color;
            node3 = node2.right;
            if(node2.parent == node1){
                node3.parent = node2;
            }
            else{
                nodeTransplant(node2, node2.right);
                node2.right = node1.right;
                node2.right.parent = node2;
            }
            nodeTransplant(node1, node2);
            node2.left = node1.left;
            node2.left.parent = node2;
            node2.color = node1.color;
        }
        if(saveColor == 0){
            fixDelete(node3);
        }
    }

    private void fixDelete(Node node) {

        Node s;
        while(node != root && node.color == 0){
            if(node == node.parent.left){
                s = node.parent.right;

                if(s.color == 1){
                    s.color = 0;
                    node.parent.color = 1;
                    rotateLeft(node.parent);
                    s = node.parent.right;

                }
                if(s.left.color == 0 && s.right.color == 0){
                    s.color = 1;
                    node = node.parent;
                }

                else{
                    if(s.right.color == 0){
                        s.left.color = 0;
                        s.color = 1;
                        rotateRight(s);
                        s = node.parent.right;
                    }

                    s.color = node.parent.color;
                    node.parent.color = 0;
                    s.right.color = 0;
                    rotateLeft(node.parent);
                    node = root;
                }

            }

            else{
                s = node.parent.left;
                if(s.color == 1){
                    s.color = 0;
                    node.parent.color = 1;
                    rotateRight(node.parent);
                    s = node.parent.left;
                }
                if(s.right.color == 0 && s.left.color == 0){
                    s.color = 1;
                    node = node.parent;
                }
                else{
                    if(s.left.color == 0){
                        s.right.color = 0;
                        s.color = 1;
                        rotateRight(s);
                        s = node.parent.left;
                    }
                    s.color = node.parent.color;
                    node.parent.color = 0;
                    s.left.color = 0;
                    rotateRight(node.parent);
                    node = root;
                }
            }

        }
        node.color = 0;
    }

    public void delete(int key){
        deleteNodeHelper(root, key);
    }


    public static void main(String [] args){
        RedBlackTree bst = new RedBlackTree();
        bst.insert(8);
        bst.insert(18);
        bst.insert(5);
        bst.insert(15);
        bst.insert(17);
        bst.insert(25);
        bst.insert(40);
        bst.insert(80);
        bst.delete(25);
        bst.delete(17);
        bst.delete(18);
        bst.delete(8);
        bst.delete(40);
        bst.delete(80);
        bst.delete(15);
        //bst.delete(5);
        bst.prettyPrint();
        bst.preorder();
        System.out.println();
        bst.inorder();
        System.out.println();
        bst.postorder();
    }

}
