package com.hls;

import lombok.Data;

import java.util.*;

@Data
public class Tree {
    private Node root;
    private int size;

    public Tree() {
        root = null;
    }


    public void add(int val) {
        size++;
        Node node = new Node(val);
        if (root == null) {
            root = node;
            root.color = Node.BLACK;
            return;
        }
        mount(root, node);
        fix(node);
    }

    private void fix(Node node) {
        if (node == root) {
            node.color = Node.BLACK;
            return;
        } else if (!Objects.equals(node.color, node.parent.color)) {
            return;
        }
        if (node.get_uncle() != Node.RED) {
            switch (node.get_type()) {
                case Node.LL -> ll(node);
                case Node.RR -> rr(node);
                case Node.LR -> lr(node);
                case Node.RL -> rl(node);
                case Node.ERROR -> System.out.println("程序错误，或数据发生意外改变，树崩溃");
            }
            return;
        }
        Node tmp = node.parent.parent;
        tmp.reverse();
        if (tmp.left != null) {
            tmp.left.reverse();
        }
        if (tmp.right != null) {
            tmp.right.reverse();
        }
        fix(tmp);
    }


    /**
     * 挂载节点
     *
     * @param root
     * @param node
     */
    private void mount(Node root, Node node) {
        if (root.data > node.data) {
            if (root.left == null) {
                root.left = node;
                node.parent = root;
                return;
            }
            mount(root.left, node);
        } else {
            if (root.right == null) {
                root.right = node;
                node.parent = root;
                return;
            }
            mount(root.right, node);
        }
    }

    public List<Node> show() {
        Queue<Node> q = new ArrayDeque<>();
        List<Node> list = new ArrayList<>();
        list.add(root);
        q.add(root);
        while (!q.isEmpty()) {
            Node tmp = q.poll();
            list.add(tmp.left);
            list.add(tmp.right);
            if (tmp.left != null) {
                q.add(tmp.left);
            }
            if (tmp.right != null) {
                q.add(tmp.right);
            }

        }
        return list;
    }

    public void del(int val) {
        while (true) {
            Node node = find(val);
            if (node == null) {
                break;
            }
            size--;
            if (node.left != null && node.right != null) {
                Node successor = get_successor(node);
                if (successor != null) {
                    node.data = successor.data;
                }
                delete(successor);
            } else if (node.left != null || node.right != null) {
                Node son = null;
                if (node.left != null) {
                    son = node.left;
                } else {
                    son = node.right;
                }
                replace(son, node);
                son.reverse();
            } else if (node.color == Node.RED) {
                replace(null, node);
            } else {
                delete(node);
            }
        }
    }

    private void delete(Node node) {
        Node brother = getBrother(node);
        if (brother != null && brother.color == Node.RED) {
            brother.reverse();
            brother.parent.reverse();
            if (node.parent.left == node) {
                left(node.parent);
            } else {
                right(node.parent);
            }
            delete(node);
        } else if (brother != null && brother.color == Node.BLACK && brother.hasRedKid()) {
            leastOneRedKid(node);
        }else {
            doubleBlack(node);
        }
    }

    /**
     * 兄弟是黑，至少有一个孩子是红
     *
     * @param node
     */
    private void leastOneRedKid(Node node) {
        Node brother = getBrother(node);
        if (brother == null || brother.color == Node.RED || !brother.hasRedKid()) {
            return;
        }
        Node parent = node.parent;
        Node nephew=null;
        if(parent.left==brother && brother.left!=null && brother.left.color == Node.RED) {
//            ll
            nephew=brother.left;
            nephew.color=brother.color;
            brother.color=parent.color;
            parent.color=Node.BLACK;
            right(parent);
        } else if (parent.left==brother && brother.right!=null && brother.right.color == Node.RED) {
//            lr
            nephew=brother.right;
            nephew.color=parent.color;
            parent.color=Node.BLACK;
            left(brother);
            right(parent);
        } else if (parent.right==brother && brother.right!=null && brother.right.color == Node.RED) {
//            rr
            nephew=brother.right;
            nephew.color=brother.color;
            brother.color=parent.color;
            parent.color=Node.BLACK;
            left(parent);
        }else{
//            rl
            nephew=brother.left;
            nephew.color=parent.color;
            parent.color=Node.BLACK;
            right(brother);
            left(parent);
        }
        replace(null, node);
    }

    /**
     * 兄弟是黑,并且孩子都是黑
     *
     * @param node
     */
    private void doubleBlack(Node node) {
        Node brother = getBrother(node);
        if (brother == null || brother.color == Node.RED || brother.hasRedKid()) {
            return;
        }
        brother.reverse();
        if(node.left==null && node.right == null) {
            replace(null, node);
        }
        if (node.parent != null && node.parent.color == Node.RED) {
            node.parent.reverse();
            return;
        } else if (node.parent != null && node.parent == this.root) {
            return;
        }
        delete(node.parent);
    }


    /**
     * 获取兄弟节点
     *
     * @param node
     * @return
     */
    private Node getBrother(Node node) {
        if (node.parent == null) {
            return null;
        }
        return node.parent.left != node ? node.parent.left : node.parent.right;
    }


    /**
     * 替代节点
     *
     * @param node 替代的节点可为null
     * @param root 被替代的节点,不可为空
     */
    private void replace(Node node, Node root) {
        if (root == null) {
            return;
        }
        Node parent = root.parent;
        if (parent == null) {
            this.root = node;
            return;
        }
        if (parent.left == root) {
            parent.left = node;
        } else {
            parent.right = node;
        }
        if (node != null) {
            node.parent = parent;
        }
    }

    /**
     * 获取节点的后继者
     *
     * @param root
     * @return
     */
    private Node get_successor(Node root) {
        if (root.right == null) {
            return null;
        }
        root = root.right;
        while (root.left != null) {
            root = root.left;
        }
        return root;
    }

    /**
     * 查找节点，找到返回node，没有null
     *
     * @param val
     * @return
     */
    public Node find(int val) {
        Node tmp = root;
        while (tmp != null && tmp.data != val) {
            if (tmp.data > val) {
                tmp = tmp.left;
            } else {
                tmp = tmp.right;
            }
        }
        return tmp;
    }

    /**
     * 左旋
     *
     * @param root
     */
    private void left(Node root) {
        Node son = root.right;
        if (root.parent != null && root.parent.left == root) {
            root.parent.left = son;
        } else if (root.parent != null && root.parent.right == root) {
            root.parent.right = son;
        }
        son.parent = root.parent;
        root.parent = son;
        root.right = son.left;
        if(son.left != null) {
            son.left.parent=root;
        }
        son.left = root;
        if (this.root == root) {
            this.root = son;
        }
    }


    /**
     * 右旋
     *
     * @param root
     */
    private void right(Node root) {
        Node son = root.left;
        if (root.parent != null && root.parent.left == root) {
            root.parent.left = son;
        } else if (root.parent != null && root.parent.right == root) {
            root.parent.right = son;
        }
        son.parent = root.parent;
        root.parent = son;
        root.left = son.right;
        if(son.right != null) {
            son.right.parent=root;
        }
        son.right = root;
        if (this.root == root) {
            this.root = son;
        }
    }

    private void ll(Node node) {
        Node grandparent = node.parent.parent;
        Node parent = node.parent;
        right(grandparent);
        parent.reverse();
        grandparent.reverse();
    }


    private void rr(Node node) {
        Node grandparent = node.parent.parent;
        Node parent = node.parent;
        left(grandparent);
        parent.reverse();
        grandparent.reverse();
    }

    private void lr(Node node) {
        Node parent = node.parent;
        left(parent);
        ll(parent);
    }


    private void rl(Node node) {
        Node parent = node.parent;
        right(parent);
        rr(parent);
    }

}






























