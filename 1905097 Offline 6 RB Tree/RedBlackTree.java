import java.io.*;
import java.util.Scanner;

class Node{
    int data;
    Node parent;
    Node left;
    Node right;
    int color;
    int cntSubtree;

    Node(){
        cntSubtree = 1;
    }
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
        nullNode.cntSubtree = 0;
        root = nullNode;
    }

    private boolean searchHelper(Node node, int key){
        if(node == nullNode){
            return false;
        }
        else if(node.data == key){
            return true;
        }
        if(key < node.data){
            return searchHelper(node.left, key);
        }
        else{
            return searchHelper(node.right, key);
        }
    }

    public boolean search(int key){
        return searchHelper(root, key);
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
        node.cntSubtree = node.right.cntSubtree + node.left.cntSubtree + 1;
        r.cntSubtree = r.right.cntSubtree + r.left.cntSubtree + 1;
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
        node.cntSubtree = node.left.cntSubtree + node.right.cntSubtree + 1;
        l.cntSubtree = l.left.cntSubtree + l.right.cntSubtree + 1;
    }

    public boolean insert(int key){
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
            else if(node.data > tmp.data){
                tmp = tmp.right;
            }
            else{
                //System.out.println("Same priority (" + key + ") exists");
                return false;
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


        if(p != null){
            p.cntSubtree++;
            p = p.parent;
            while(p != null){
                p.cntSubtree = p.right.cntSubtree + p.left.cntSubtree + 1;
                p = p.parent;
            }
        }


        if(node.parent == null){
            node.color = 0;
            return true;
        }
        else if(node.parent.parent == null){
            return true;
        }
        fixInsert(node);
        return true;

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
            System.out.println(root.data + "(" + sColor + ")(" + root.cntSubtree + ")");
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

        Node p = v.parent;
        while(p != null){
            p.cntSubtree = p.left.cntSubtree + p.right.cntSubtree + 1;
            p = p.parent;
        }


    }

    private boolean deleteNodeHelper(Node node, int key){
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
            //System.out.println("No priority (" + key + ") exists");
            return false;
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
                //System.out.println("Hi");
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

            Node tmp = node2;
            while(tmp != null){
                tmp.cntSubtree = tmp.left.cntSubtree + tmp.right.cntSubtree + 1;
                tmp = tmp.parent;
            }

        }
        if(saveColor == 0){
            fixDelete(node3);
        }
        return true;
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
                        rotateLeft(s);
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


    private int lessPriorityCountHelper(Node node, int key){
        if(node == nullNode){
            return 0;
        }
        if (node.data == key) {
            return node.left.cntSubtree;
        }
        else if (node.data < key) {
            return 1 + node.left.cntSubtree + lessPriorityCountHelper(node.right, key);
        }
        else{
            return lessPriorityCountHelper(node.left, key);
        }
    }


    public boolean delete(int key){
        return deleteNodeHelper(root, key);
    }


    public int lessPriorityCount(int key){
        return lessPriorityCountHelper(root, key);
    }


    public static void main(String [] args){
        RedBlackTree rbTree = new RedBlackTree();

        File file = new File("input.txt");
        FileReader fr = null;
        String line = null;
        FileWriter fw = null;
        try {
            fr = new FileReader(file);
            fw = new FileWriter("Output.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(fr);
        try {
            line = br.readLine();
            //fw.write("Hi");
        } catch (IOException e) {
            e.printStackTrace();
        }
        int n = Integer.parseInt(line);
        try {
            fw.write(n + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < n; i++){
            try {
                line = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String[] arr = line.split(" ");
            int x = Integer.parseInt(arr[0]);
            int y = Integer.parseInt(arr[1]);
            boolean flag;
            //System.out.println(x + " " + y + " ");
            try {
                fw.write(x + " " + y + " ");
                //System.out.println(x + " " + y + " ");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(x == 1){
                flag = rbTree.insert(y);
                if(flag) {
                    try {
                        fw.write(1 + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        fw.write(0 + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if(x == 0){
                flag = rbTree.delete(y);
                if(flag) {
                    try {
                        fw.write(1 + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        fw.write(0 + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if(x == 2){
                flag = rbTree.search(y);
                if(flag) {
                    try {
                        fw.write(1 + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        fw.write(0 + "\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else{
                try {
                    fw.write(rbTree.lessPriorityCount(y) + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //System.out.println(x + " " + y);
            //rbTree.prettyPrint();
        }
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
